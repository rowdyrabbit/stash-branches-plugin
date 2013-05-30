package au.com.denisefernandez.stash.plugin.branchlist.service;

import com.atlassian.stash.repository.Branch;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.util.Page;

public interface BranchService {
	
	Page<BranchComparison> getDiffsBetweenAllBranchesAndComparisonBranch(Repository repo, Branch compareBranch);
	
	BaseBranch getBaseBranchForBranch(Repository repo, Branch branch);

}