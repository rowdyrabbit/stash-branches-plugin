package au.com.denisefernandez.stash.plugin.branchlist.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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

public class BranchService {
	
	private final GitCommandBuilderFactory gitCommandFactory;
	private final ScmService scmService;
	
	
	public BranchService(GitCommandBuilderFactory gitCommandFactory, ScmService scmService) {
		this.gitCommandFactory = gitCommandFactory;
		this.scmService = scmService;
	}

	public Page<BranchStatus> getDiffsBetweenBranchesAndSelectedBranch(Repository repo, Branch compareBranch) {
		return getDifferences(repo, compareBranch,  new PageRequestImpl(0, 100));
	}
	
	public Page<BranchStatus> getDiffsBetweenBranchesAndSelectedBranch(Repository repo, Branch compareBranch, PageRequest pageRequest) {
		return getDifferences(repo, compareBranch, pageRequest);
	}

	private Page<BranchStatus> getDifferences(Repository repo, Branch compareBranch, PageRequest pageRequest) {
		ScmCommandFactory factory = scmService.getCommandFactory(repo);
		Page<Branch> page = factory.branches(new BranchesCommandParameters.Builder().order(RefOrder.ALPHABETICAL).build(),pageRequest).call();
		Iterator<Branch> allBranches = page.getValues().iterator();
		List<BranchStatus> branchStatusList = getBranchComparisons(allBranches, compareBranch, repo);
		return new PageImpl<BranchStatus>(pageRequest, ImmutableList.copyOf(branchStatusList), true);
	}
	
	private List<BranchStatus> getBranchComparisons(Iterator<Branch> allBranches, Branch compareBranch, Repository repo) {
		GitScmCommandBuilder command = gitCommandFactory.builder(repo).command("rev-list");
		List<BranchStatus> branchStatusList = new ArrayList<BranchStatus>();
    	while(allBranches.hasNext()) {
    		Branch b = allBranches.next();
    		if (b.getDisplayId().equalsIgnoreCase(compareBranch.getDisplayId())) {
    			System.out.println("FOUND COMPARE BRANCH: "+compareBranch.getDisplayId());
    		} else {
    			System.out.println("Id --> "+b.getId()+ " Name: "+b.getDisplayId());
    			command.clearArguments();
    			command.argument("--no-merges").argument("--left-right").argument(compareBranch.getDisplayId()+"..."+b.getDisplayId());
    			CommandOutputHandler<String> output = stringOut();
    			command.build(output).call();
    			String out = output.getOutput();
    			System.out.println("Diff string: "+out);
    			String lines[] = out.split("\\r?\\n");
    			int aheadCount = 0;
    			int behindCount = 0;
    			for (int i=0;i<lines.length;i++) {
    				if (StringUtils.startsWith(lines[i], "<")) {
    					behindCount ++;
    				} else if (StringUtils.startsWith(lines[i], ">")) {
    					aheadCount ++;
    				}
    			}
    			branchStatusList.add(new BranchStatus(b, aheadCount, behindCount));
    		}
    	}
    	return branchStatusList;
	}
	
	
	private CommandOutputHandler<String> stringOut() {
        return new StringCommandOutputHandler();
    }
	
	private class StringCommandOutputHandler extends StringOutputHandler implements CommandOutputHandler<String> {
	}

}
