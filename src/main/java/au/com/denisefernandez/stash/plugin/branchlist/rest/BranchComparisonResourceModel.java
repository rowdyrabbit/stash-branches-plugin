package au.com.denisefernandez.stash.plugin.branchlist.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import au.com.denisefernandez.stash.plugin.branchlist.service.BranchComparison;

import com.atlassian.stash.util.Page;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class BranchComparisonResourceModel {

    @XmlElement(name = "values")
    private List<RestBranchComparison> branchList;
    

    public BranchComparisonResourceModel(Page<BranchComparison> branchStatusList) {
    	this.branchList = transformList(branchStatusList);
    }

	public List<RestBranchComparison> getBranchList() {
		return branchList;
	}
	
	private List<RestBranchComparison> transformList(Page<BranchComparison> branchStatusList) {
		List<RestBranchComparison> list = new ArrayList<RestBranchComparison>();
		
		Iterable<BranchComparison> it = branchStatusList.getValues();
		if (it != null) {
			Iterator<BranchComparison> iterator = it.iterator();
			while(iterator.hasNext()) {
				BranchComparison branch = iterator.next();
				RestBranchComparison restBranch = new RestBranchComparison(branch);
				list.add(restBranch);
			}
		}
		
		return list;
	}
}