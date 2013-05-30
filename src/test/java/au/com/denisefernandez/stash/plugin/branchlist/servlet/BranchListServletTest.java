package au.com.denisefernandez.stash.plugin.branchlist.servlet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import au.com.denisefernandez.stash.plugin.branchlist.service.BaseBranch;
import au.com.denisefernandez.stash.plugin.branchlist.service.BranchComparison;
import au.com.denisefernandez.stash.plugin.branchlist.service.BranchService;

import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.stash.repository.Branch;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.repository.RepositoryMetadataService;
import com.atlassian.stash.repository.RepositoryService;
import com.atlassian.stash.util.Page;

public class BranchListServletTest {

	private BranchListServlet servlet;
	
	@Mock
    private HttpServletRequest mockRequest;
    @Mock
	private HttpServletResponse mockResponse;
    @Mock
    private SoyTemplateRenderer mockSoyTemplateRenderer;
    @Mock
    private RepositoryService mockRepositoryService;
    @Mock
    private BranchService mockBranchService;
    @Mock
    private WebResourceManager mockWebResourceManager;
    @Mock
    private RepositoryMetadataService mockRepositoryMetadataService;

    @Before
    public void setup() {
    	initMocks(this);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        this.servlet = new BranchListServlet(mockSoyTemplateRenderer, mockRepositoryService, mockBranchService, mockWebResourceManager, mockRepositoryMetadataService);
    }

    @Test
    public void shouldSend404ErrorWhenPathInfoIsEmpty() throws Exception {
    	servlet.doGet(mockRequest, mockResponse);
    	
    	verify(mockResponse).sendError(404);
    }
    
    @Test
    public void shouldSend404ErrorWhenPathInfoIsSingleForwardSlash() throws Exception {
    	when(mockRequest.getPathInfo()).thenReturn("/");
    	servlet.doGet(mockRequest, mockResponse);
    	
    	verify(mockResponse).sendError(404);
    }
    
    @Test
    public void shouldSend404ErrorWhenPathInfoHasLessThanExpectedPathComponents() throws Exception {
    	when(mockRequest.getPathInfo()).thenReturn("branches/PROJECT_1");
    	servlet.doGet(mockRequest, mockResponse);
    	
    	verify(mockResponse).sendError(404);
    }
    
    @Test
    public void shouldSend404ErrorWhenRepositoryNotFound() throws Exception {
    	when(mockRequest.getPathInfo()).thenReturn("branches/PROJECT_1/rep_1/");
    	when(mockRepositoryService.getBySlug("PROJECT_1", "rep_1")).thenReturn(null);
    	servlet.doGet(mockRequest, mockResponse);
    	
    	verify(mockResponse).sendError(404);
    }
    
    @Test
    public void shouldLookupRepositoryFromRepositoryServiceWhenPathInfoCorrect() throws Exception {
    	when(mockRequest.getPathInfo()).thenReturn("branches/PROJECT_1/rep_1/");
    	Repository mockRepo = mock(Repository.class);
    	Branch mockBranch = mock(Branch.class);
    	BaseBranch baseBranch = mock(BaseBranch.class);
    	Page<BranchComparison> mockBranchComparisonPage = mock(Page.class);
    	when(mockRepositoryMetadataService.getDefaultBranch(mockRepo)).thenReturn(mockBranch);
    	when(mockRepositoryService.getBySlug("PROJECT_1", "rep_1")).thenReturn(mockRepo);
    	when(mockBranchService.getDiffsBetweenAllBranchesAndComparisonBranch(mockRepo, mockBranch)).thenReturn(mockBranchComparisonPage);
    	when(mockBranchService.getBaseBranchForBranch(mockRepo, mockBranch)).thenReturn(baseBranch);
    	
    	servlet.doGet(mockRequest, mockResponse);
    	
    	verify(mockRepositoryService, times(1)).getBySlug(Mockito.anyString(), Mockito.anyString());
    }
}
