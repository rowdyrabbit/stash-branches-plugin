package au.com.denisefernandez.stash.plugin.branchlist.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import au.com.denisefernandez.stash.plugin.branchlist.service.BranchStatus;

import com.atlassian.stash.util.Page;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class BranchComparisonResourceModel {

    @XmlElement(name = "values")
    private List<RestBranchStatus> branchList;
    

    public BranchComparisonResourceModel(Page<BranchStatus> branchStatusList) {
    	this.branchList = transformList(branchStatusList);
    }


	public List<RestBranchStatus> getBranchList() {
		return branchList;
	}
	
	private List<RestBranchStatus> transformList(Page<BranchStatus> branchStatusList) {
		List<RestBranchStatus> list = new ArrayList<RestBranchStatus>();
		Iterator<BranchStatus> it = branchStatusList.getValues().iterator();
		
		while(it.hasNext()) {
			BranchStatus b = it.next();
			RestBranchStatus r = new RestBranchStatus(b);
			list.add(r);
		}
		return list;
	}
}