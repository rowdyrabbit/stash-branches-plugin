package au.com.denisefernandez.stash.plugin.branchlist.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import au.com.denisefernandez.stash.plugin.branchlist.service.BaseBranch;
import au.com.denisefernandez.stash.plugin.branchlist.service.BranchComparison;

import com.atlassian.stash.util.Page;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class BranchComparisonResourceModel {

    @XmlElement(name = "values")
    private List<RestBranchComparison> branchList;
    
    @XmlElement(name="comparisonBranch")
    private RestBaseBranch comparisonBranch;
    
    public BranchComparisonResourceModel(Page<BranchComparison> branchStatusList, BaseBranch comparisonBranch) {
    	this.branchList = transformList(branchStatusList);
    	this.comparisonBranch = new RestBaseBranch(comparisonBranch);
    }

	public List<RestBranchComparison> getBranchList() {
		return this.branchList;
	}
	
	public RestBaseBranch getComparisonBranch() {
		return this.comparisonBranch;
	}
	
	
	private List<RestBranchComparison> transformList(Page<BranchComparison> branchStatusList) {
		List<RestBranchComparison> list = new ArrayList<RestBranchComparison>();
		
		Iterable<BranchComparison> it = branchStatusList.getValues();
		if (it != null) {
			Iterator<BranchComparison> iterator = it.iterator();
			while(iterator.hasNext()) {
				BranchComparison branchComparison = iterator.next();
				RestBranchComparison restBranch = new RestBranchComparison(branchComparison);
				list.add(restBranch);
			}
		}
		
		return list;
	}
}