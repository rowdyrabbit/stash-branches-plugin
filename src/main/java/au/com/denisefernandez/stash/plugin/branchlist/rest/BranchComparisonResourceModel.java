package au.com.denisefernandez.stash.plugin.branchlist.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import au.com.denisefernandez.stash.plugin.branchlist.service.BranchComparison;

import com.atlassian.stash.repository.Branch;
import com.atlassian.stash.util.Page;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class BranchComparisonResourceModel {

    @XmlElement(name = "values")
    private List<RestBranchComparison> branchList;
    
    @XmlElement(name="comparisonBranch")
    private RestBranch comparisonBranch;
    
    public BranchComparisonResourceModel(Page<BranchComparison> branchStatusList, Branch comparisonBranch) {
    	this.branchList = transformList(branchStatusList);
    	this.comparisonBranch = new RestBranch(comparisonBranch);
    }

	public List<RestBranchComparison> getBranchList() {
		return this.branchList;
	}
	
	public RestBranch getComparisonBranch() {
		return this.comparisonBranch;
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