package au.com.denisefernandez.stash.plugin.branchlist.rest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import au.com.denisefernandez.stash.plugin.branchlist.service.BranchComparison;
import au.com.denisefernandez.stash.plugin.branchlist.service.BranchService;

import com.atlassian.stash.repository.Branch;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.repository.RepositoryMetadataService;
import com.atlassian.stash.repository.RepositoryService;
import com.atlassian.stash.util.Page;
import com.atlassian.stash.util.PageImpl;

public class BranchComparisonResourceTest {
	
	private BranchComparisonResource branchComparisonResource;
	
	@Mock
	private BranchService mockBranchService;
	@Mock
	private RepositoryService mockRepositoryService;
	@Mock
	private RepositoryMetadataService mockRepositoryMetadataService;

    @Before
    public void setup() {
    	initMocks(this);
    	branchComparisonResource = new BranchComparisonResource(mockBranchService, mockRepositoryService, mockRepositoryMetadataService);
    }
    
    @Test
    public void shouldReturn200Response() {
    	Repository mockRepo = mock(Repository.class);
    	Branch mockBranch = mock(Branch.class);
    	Page<BranchComparison> mockBranchList = mock(PageImpl.class);
    	when(mockRepositoryService.getBySlug("PROJECT_1", "rep_1")).thenReturn(mockRepo);
    	when(mockRepositoryMetadataService.resolveRef(mockRepo, "master")).thenReturn(mockBranch);
    	when(mockBranchService.getDiffsBetweenAllBranchesAndComparisonBranch(mockRepo, mockBranch)).thenReturn(mockBranchList);
    	
    	Response resp = branchComparisonResource.getBranchComparison("rep_1", "PROJECT_1", "master");
    	assertThat(resp.getStatus(), is(200));
    }

}
