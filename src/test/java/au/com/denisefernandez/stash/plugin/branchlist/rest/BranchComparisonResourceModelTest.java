package au.com.denisefernandez.stash.plugin.branchlist.rest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import au.com.denisefernandez.stash.plugin.branchlist.service.BranchComparison;

import com.atlassian.stash.repository.Branch;
import com.atlassian.stash.util.Page;
import com.atlassian.stash.util.PageImpl;
import com.atlassian.stash.util.PageRequestImpl;

public class BranchComparisonResourceModelTest {
	
	
	private static final Branch MASTER = createBranch("refs/heads/master", "master", "cafebabe", true);

	private static Branch createBranch(String id, String displayId, String latestChangeset, boolean isDefault) {
		Branch branch = mock(Branch.class);
		when(branch.getId()).thenReturn(id);
		when(branch.getDisplayId()).thenReturn(displayId);
		when(branch.getLatestChangeset()).thenReturn(latestChangeset);
		when(branch.getIsDefault()).thenReturn(isDefault);
		return branch;
	}
	
	private BranchComparisonResourceModel resourceModel;
	
	@Before
	public void setup() {
	}

	@Test
	public void testModelSizeIsZeroWhenNoBranchStatusesAddedToModel() {
		List<BranchComparison> branches = new ArrayList<BranchComparison>();
		Page<BranchComparison> branchStatusList = new PageImpl<BranchComparison>(new PageRequestImpl(0, 100), branches, true);
		resourceModel = new BranchComparisonResourceModel(branchStatusList);
		assertThat(resourceModel.getBranchList().size(), is(0));
	}
	
	@Test
	public void testModelTransformedWhenBranchStatusesAddedToModel() {
		List<BranchComparison> branches = new ArrayList<BranchComparison>();
		branches.add(new BranchComparison(MASTER, 5, 2, "/branches/test"));
		Page<BranchComparison> branchStatusList = new PageImpl<BranchComparison>(new PageRequestImpl(0, 100), branches, true);
		resourceModel = new BranchComparisonResourceModel(branchStatusList);
		
		List<RestBranchComparison> branchList = resourceModel.getBranchList();
		
		assertThat(branchList.size(), is(1));
		RestBranchComparison comparison = branchList.get(0);
		assertThat(comparison.getAheadCount(), is(5));
		assertThat(comparison.getAheadPercentage(), is(10f));
		assertThat(comparison.getBehindCount(), is(2));
		assertThat(comparison.getBehindPercentage(), is(4f));
		assertThat(comparison.getBranchUrl(), is("/branches/test"));
		RestBranch branch = comparison.getBranch();
		assertThat(branch.getDisplayId(), is(MASTER.getDisplayId()));
		assertThat(branch.getId(), is(MASTER.getId()));
		assertThat(branch.getLatestChangeset(), is(MASTER.getLatestChangeset()));
		assertThat(branch.isDefault(), is(true));
	}

}
