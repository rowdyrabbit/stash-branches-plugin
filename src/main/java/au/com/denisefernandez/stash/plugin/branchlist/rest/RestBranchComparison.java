package au.com.denisefernandez.stash.plugin.branchlist.rest;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import au.com.denisefernandez.stash.plugin.branchlist.service.BranchComparison;

@XmlRootElement(name = "value")
@XmlAccessorType(XmlAccessType.FIELD)
public class RestBranchComparison {
	
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
	@XmlElement(name = "branchUrl")
	private String branchUrl;
	@XmlElement(name = "latestChangeTimestamp")
	private Date latestChangeTimestamp;
	@XmlElement(name = "authorName")
	private String authorName;
	
	
	
	public RestBranchComparison(BranchComparison branchComparison) {
		this.branch = new RestBranch(branchComparison.getBranch());
		this.aheadCount = branchComparison.getAheadCount();
		this.behindCount = branchComparison.getBehindCount();
		this.aheadPercentage = branchComparison.getAheadPercentage();
		this.behindPercentage = branchComparison.getBehindPercentage();
		this.branchUrl = branchComparison.getBranchUrl();
		this.latestChangeTimestamp = branchComparison.getLatestChangeTimestamp();
		this.authorName = branchComparison.getAuthorName();
	}

	public RestBranch getBranch() {
		return branch;
	}
	
	public int getBehindCount() {
		return behindCount;
	}

	public int getAheadCount() {
		return aheadCount;
	}

	public float getAheadPercentage() {
		return aheadPercentage;
	}
	
	public float getBehindPercentage() {
		return behindPercentage;
	}
	
	public String getBranchUrl() {
		return branchUrl;
	}
	
	public Date getLatestChangeTimestamp() {
		return latestChangeTimestamp;
	}
	
	public String getAuthorName() {
		return this.authorName;
	}

	

}
