package au.com.denisefernandez.stash.plugin.branchlist.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.atlassian.stash.content.Changeset;
import com.atlassian.stash.history.HistoryService;
import com.atlassian.stash.nav.NavBuilder;
import com.atlassian.stash.repository.Branch;
import com.atlassian.stash.repository.RefOrder;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.scm.BranchesCommandParameters;
import com.atlassian.stash.scm.CommandOutputHandler;
import com.atlassian.stash.scm.ScmCommandFactory;
import com.atlassian.stash.scm.ScmService;
import com.atlassian.stash.scm.git.GitCommandBuilderFactory;
import com.atlassian.stash.scm.git.GitScmCommandBuilder;
import com.atlassian.stash.util.Page;
import com.atlassian.stash.util.PageImpl;
import com.atlassian.stash.util.PageRequest;
import com.atlassian.stash.util.PageRequestImpl;
import com.atlassian.utils.process.StringOutputHandler;
import com.google.common.collect.ImmutableList;

public class BranchServiceImpl implements BranchService {
	
	private final GitCommandBuilderFactory gitCommandFactory;
	private final ScmService scmService;
	private final NavBuilder navBuilder;
	private final HistoryService historyService;
	//When pagination is implemented this can be removed. If a repo has > 1000 branches I think it's got issues anyway.
	private static final int MAX_NUMBER_OF_BRANCHES = 1000;
	
	
	public BranchServiceImpl(GitCommandBuilderFactory gitCommandFactory, ScmService scmService, NavBuilder navBuilder, HistoryService historyService) {
		this.gitCommandFactory = gitCommandFactory;
		this.scmService = scmService;
		this.navBuilder = navBuilder;
		this.historyService = historyService;
	}
	
	public BaseBranch getBaseBranchForBranch(Repository repo, Branch branch) {
		Changeset latestChangeset =  historyService.getChangeset(repo, branch.getLatestChangeset());
		String branchUrl = navBuilder.project(repo.getProject()).repo(repo).browse().atRevision(branch.getId()).buildAbsolute();
		return new BaseBranch(branch, branchUrl, latestChangeset);
	}

	public Page<BranchComparison> getDiffsBetweenAllBranchesAndComparisonBranch(Repository repo, Branch compareBranch) {
		return calculateDifferences(repo, compareBranch,  new PageRequestImpl(0, MAX_NUMBER_OF_BRANCHES));
	}
	
	private Page<BranchComparison> calculateDifferences(Repository repo, Branch compareBranch, PageRequest pageRequest) {
		ScmCommandFactory factory = scmService.getCommandFactory(repo);
		Page<Branch> page = factory.branches(new BranchesCommandParameters.Builder().order(RefOrder.ALPHABETICAL).build(), pageRequest).call();

		Iterator<Branch> allBranches = page.getValues() != null ? page.getValues().iterator() : null;
		List<BranchComparison> branchList = new ArrayList<BranchComparison>();
		if (allBranches != null) {
			branchList = doBranchComparisons(allBranches, compareBranch, repo);
		}
		
		return new PageImpl<BranchComparison>(pageRequest, ImmutableList.copyOf(branchList), true);
	}
	
	private List<BranchComparison> doBranchComparisons(Iterator<Branch> allBranches, Branch compareBranch, Repository repo) {
		List<BranchComparison> branchComparisonList = new ArrayList<BranchComparison>();
    	while(allBranches.hasNext()) {
    		Branch currentBranch = allBranches.next();
    		if (currentBranch.getDisplayId().equalsIgnoreCase(compareBranch.getDisplayId())) {
    			//Found the branch we are comparing to, so exclude it from the list
    		} else {
    			BranchComparison branchComparison = buildBranchComparison(compareBranch, repo, currentBranch);
    			branchComparisonList.add(branchComparison);
    		}
    	}
    	return branchComparisonList;
	}

	private BranchComparison buildBranchComparison(Branch compareBranch, Repository repo, Branch currentBranch) {
		String out = executeRevListLeftRightCompareCommand(compareBranch, repo, currentBranch);
		String lines[] = out.split("\\r?\\n");
		int aheadCount = 0;
		int behindCount = 0;
		for (int i=0; i<lines.length; i++) {
			if (StringUtils.startsWith(lines[i], "<")) {
				behindCount ++;
			} else if (StringUtils.startsWith(lines[i], ">")) {
				aheadCount ++;
			}
		}
		String branchUrl = navBuilder.project(repo.getProject()).repo(repo).browse().atRevision(currentBranch.getId()).buildAbsolute();
		Changeset latestChangeset = historyService.getChangeset(repo, currentBranch.getLatestChangeset());
		
		return new BranchComparison(currentBranch, aheadCount, behindCount, branchUrl, latestChangeset);
	}
	

	private String executeRevListLeftRightCompareCommand(Branch compareBranch, Repository repo, Branch currentBranch) {
		GitScmCommandBuilder command = gitCommandFactory.builder(repo).command("rev-list");
		command.clearArguments();
		command.argument("--no-merges").argument("--left-right").argument(compareBranch.getDisplayId()+"..."+currentBranch.getDisplayId());
		CommandOutputHandler<String> output = stringOut();
		command.build(output).call();
		String out = output.getOutput();
		return out;
	}
	
	
	private CommandOutputHandler<String> stringOut() {
        return new StringCommandOutputHandler();
    }
	
	private class StringCommandOutputHandler extends StringOutputHandler implements CommandOutputHandler<String> {
	}

}
