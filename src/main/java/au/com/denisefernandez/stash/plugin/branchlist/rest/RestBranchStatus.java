package au.com.denisefernandez.stash.plugin.branchlist.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import au.com.denisefernandez.stash.plugin.branchlist.service.BranchStatus;

@XmlRootElement(name = "value")
@XmlAccessorType(XmlAccessType.FIELD)
public class RestBranchStatus {
	
	private static final float MAX_NUM_COMMITS_AHEAD_OR_BEHIND = 50;
	@XmlElement(name = "branch")
	private RestBranch branch;
	@XmlElement(name = "aheadCount")
	private int aheadCount;
	@XmlElement(name = "behindCount")
	private int behindCount;
	@XmlElement(name = "aheadPercentage")
	private float aheadPercentage;
	@XmlElement(name = "behindPercentage")
	private float behindPercentage;
	
	public RestBranchStatus(BranchStatus branchStatus) {
		this.setBranch(new RestBranch(branchStatus.getBranch()));
		this.setAheadCount(branchStatus.getAheadCount());
		this.setBehindCount(branchStatus.getBehindCount());
		this.aheadPercentage = branchStatus.getAheadPercentage();
		this.behindPercentage = branchStatus.getBehindPercentage();
	}

	public int getBehindCount() {
		return behindCount;
	}

	public void setBehindCount(int behindCount) {
		this.behindCount = behindCount;
		this.behindPercentage = (behindCount / MAX_NUM_COMMITS_AHEAD_OR_BEHIND) * 100;
		System.out.println("behindPercentage = "+ behindPercentage + " beacause "+(behindCount / MAX_NUM_COMMITS_AHEAD_OR_BEHIND) + " but: "+behindCount);
	}
	
	public int getAheadCount() {
		return aheadCount;
	}

	public void setAheadCount(int aheadCount) {
		this.aheadCount = aheadCount;
		this.aheadPercentage = ((aheadCount / MAX_NUM_COMMITS_AHEAD_OR_BEHIND) * 100);
		System.out.println("aheadPercentage = "+ aheadPercentage);
	}

	public RestBranch getBranch() {
		return branch;
	}

	public void setBranch(RestBranch branch) {
		this.branch = branch;
	}

	public float getAheadPercentage() {
		return aheadPercentage;
	}
	public float getBehindPercentage() {
		return behindPercentage;
	}

	

}
