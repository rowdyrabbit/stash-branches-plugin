package au.com.denisefernandez.stash.plugin.branchlist.service;

import java.util.Date;

import com.atlassian.stash.content.Changeset;
import com.atlassian.stash.repository.Branch;


public class BranchComparison {
	
	private static final float MAX_NUM_COMMITS_AHEAD_OR_BEHIND = 50;
	
	private Branch branch;
	private int aheadCount;
	private int behindCount;
	private String branchUrl;
	private Changeset latestChangeset;
	
	public BranchComparison(Branch branch, int aheadCount, int behindCount, String branchUrl, Changeset latestChangeset) {
		this.branch = branch;
		this.aheadCount = aheadCount;
		this.behindCount = behindCount;
		this.branchUrl = branchUrl;
		this.latestChangeset = latestChangeset;
	}

	public int getBehindCount() {
		return behindCount;
	}

	public float getBehindPercentage() {
		return (behindCount / MAX_NUM_COMMITS_AHEAD_OR_BEHIND) * 100;
	}

	public int getAheadCount() {
		return aheadCount;
	}

	public float getAheadPercentage() {
		return (aheadCount / MAX_NUM_COMMITS_AHEAD_OR_BEHIND) * 100;
	}

	public Branch getBranch() {
		return branch;
	}

	public String getBranchUrl() {
		return branchUrl;
	}
	
	public Changeset getLatestChangeset() {
		return this.latestChangeset;
	}
	
	public String getAuthorName() {
		return this.latestChangeset.getAuthor().getName();
	}
	
	public Date getLatestChangeTimestamp() {
		return this.latestChangeset.getAuthorTimestamp();
	}


}
