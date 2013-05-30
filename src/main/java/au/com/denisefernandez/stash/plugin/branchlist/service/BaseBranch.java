package au.com.denisefernandez.stash.plugin.branchlist.service;

import java.util.Date;

import com.atlassian.stash.content.Changeset;
import com.atlassian.stash.repository.Branch;

public class BaseBranch {
	
	private Branch branch;
	private String branchUrl;
	private Changeset latestChangeset;
	
	public BaseBranch(Branch branch, String branchUrl, Changeset latestChangeset) {
		this.branch = branch;
		this.branchUrl = branchUrl;
		this.latestChangeset = latestChangeset;
	}
	
	public String getId() {
		return branch.getId();
	}
	
	public boolean isDefault() {
		return branch.getIsDefault();
	}
	
	public String getDisplayId() {
		return branch.getDisplayId();
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
