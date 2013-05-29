package au.com.denisefernandez.stash.plugin.branchlist.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.atlassian.stash.repository.Branch;

@XmlAccessorType(XmlAccessType.FIELD)
public class RestBranch {
	
	@XmlElement(name = "id")
	private String id;
	
	@XmlElement(name = "displayId")
	private String displayId;
	
	@XmlElement(name = "latestChangeset")
	private String latestChangeset;
	
	@XmlElement(name = "isDefault")
	private boolean isDefault;

	public RestBranch(Branch branch) {
		this.id = branch.getId();
		this.displayId = branch.getDisplayId();
		this.isDefault = branch.getIsDefault();
		this.latestChangeset = branch.getLatestChangeset();
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

}
