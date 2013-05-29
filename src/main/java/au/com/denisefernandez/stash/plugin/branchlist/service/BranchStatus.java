package au.com.denisefernandez.stash.plugin.branchlist.service;

import com.atlassian.stash.repository.Branch;


public class BranchStatus {
	
	private static final float MAX_NUM_COMMITS_AHEAD_OR_BEHIND = 50;
	
	private Branch branch;
	private int aheadCount;
	private int behindCount;
	
	public BranchStatus(Branch branch, int aheadCount, int behindCount) {
		this.setBranch(branch);
		this.setAheadCount(aheadCount);
		this.setBehindCount(behindCount);
	}

	public int getBehindCount() {
		return behindCount;
	}

	public void setBehindCount(int behindCount) {
		this.behindCount = behindCount;
	}
	
	public float getBehindPercentage() {
		return (behindCount / MAX_NUM_COMMITS_AHEAD_OR_BEHIND) * 100;
	}

	public int getAheadCount() {
		return aheadCount;
	}

	public void setAheadCount(int aheadCount) {
		this.aheadCount = aheadCount;
	}
	
	public float getAheadPercentage() {
		return (aheadCount / MAX_NUM_COMMITS_AHEAD_OR_BEHIND) * 100;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}


}
