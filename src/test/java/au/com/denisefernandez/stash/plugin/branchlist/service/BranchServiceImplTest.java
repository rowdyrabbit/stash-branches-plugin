package au.com.denisefernandez.stash.plugin.branchlist.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.atlassian.stash.history.HistoryService;
import com.atlassian.stash.nav.NavBuilder;
import com.atlassian.stash.repository.Branch;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.scm.BranchesCommandParameters;
import com.atlassian.stash.scm.Command;
import com.atlassian.stash.scm.ScmCommandFactory;
import com.atlassian.stash.scm.ScmService;
import com.atlassian.stash.scm.git.GitCommandBuilderFactory;
import com.atlassian.stash.util.Page;
import com.atlassian.stash.util.PageRequest;

public class BranchServiceImplTest {
	
	private static final Branch MASTER = createBranch("refs/heads/master", "master", "cafebabe", true);
	private static final Repository REP_1 = createRepository();

	private static Branch createBranch(String id, String displayId, String latestChangeset, boolean isDefault) {
		Branch branch = mock(Branch.class);
		when(branch.getId()).thenReturn(id);
		when(branch.getDisplayId()).thenReturn(displayId);
		when(branch.getLatestChangeset()).thenReturn(latestChangeset);
		when(branch.getIsDefault()).thenReturn(isDefault);
		return branch;
	}
	
	private static Repository createRepository() {
		Repository repo = mock(Repository.class);
		return repo;
	}
	
	@Mock
	private GitCommandBuilderFactory mockGitCommandFactory;
	@Mock
	private ScmService mockScmService;
	@Mock
	private NavBuilder mockNavBuilder;
	@Mock
	private ScmCommandFactory mockScmCommandFactory;
	@Mock
	private HistoryService historyService;

	private BranchService branchService;
	
	
	
	@Before
	public void setup() {
		initMocks(this);
		when(mockScmService.getCommandFactory(REP_1)).thenReturn(mockScmCommandFactory);
		branchService = new BranchServiceImpl(mockGitCommandFactory, mockScmService, mockNavBuilder, historyService);
	}
	

	@Test
	public void shouldReturnListWithTheComparisonBranchWhenNoOtherBranchesExist() {
		Page<Branch> mockPage = mock(Page.class);
		when(mockPage.getValues()).thenReturn(null);
		Command<Page<Branch>> command = mock(Command.class);
        when(command.call()).thenReturn(mockPage);
        
        when(mockScmCommandFactory.branches(Mockito.any(BranchesCommandParameters.class), Mockito.any(PageRequest.class))).thenReturn(command);
        
		Page<BranchComparison> branchList = branchService.getDiffsBetweenAllBranchesAndComparisonBranch(REP_1, MASTER);
		assertThat(branchList.getIsLastPage(), is(true));
		assertThat(branchList.getLimit(), is(1000));
		assertThat(branchList.getSize(), is(0));
	}
}
