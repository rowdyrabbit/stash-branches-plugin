package au.com.denisefernandez.stash.plugin.branchlist.rest;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.atlassian.stash.repository.Branch;

import au.com.denisefernandez.stash.plugin.branchlist.service.BaseBranch;

@XmlAccessorType(XmlAccessType.FIELD)
public class RestBaseBranch {
	
	@XmlElement(name = "id")
	private String id;
	
	@XmlElement(name = "displayId")
	private String displayId;
	
	@XmlElement(name = "latestChangeset")
	private String latestChangeset;
	
	@XmlElement(name = "isDefault")
	private boolean isDefault;
	
	@XmlElement(name = "latestChangeTimestamp")
	private Date latestChangeTimestamp;

	@XmlElement(name = "authorName")
	private String authorName;
	
	@XmlElement(name = "branchUrl")
	private String branchUrl;

	public RestBaseBranch(BaseBranch branch) {
		Branch b = branch.getBranch();
		this.id = b.getId();
		this.displayId = b.getDisplayId();
		this.isDefault = b.getIsDefault();
		this.latestChangeset = b.getLatestChangeset();
		this.latestChangeTimestamp = branch.getLatestChangeTimestamp();
		this.authorName = branch.getAuthorName();
		this.branchUrl = branch.getBranchUrl();
	}

	public String getId() {
		return id;
	}

	public String getDisplayId() {
		return displayId;
	}

	public String getLatestChangeset() {
		return latestChangeset;
	}

	public boolean isDefault() {
		return isDefault;
	}
	
	public Date getLatestChangeTimestamp() {
		return this.latestChangeTimestamp;
	}
	
	public String getAuthorName() {
		return this.authorName;
	}
	
	public String getBranchUrl() {
		return this.branchUrl;
	}

}
