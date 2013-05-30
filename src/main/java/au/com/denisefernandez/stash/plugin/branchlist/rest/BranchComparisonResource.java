package au.com.denisefernandez.stash.plugin.branchlist.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import au.com.denisefernandez.stash.plugin.branchlist.service.BaseBranch;
import au.com.denisefernandez.stash.plugin.branchlist.service.BranchComparison;
import au.com.denisefernandez.stash.plugin.branchlist.service.BranchService;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.atlassian.stash.repository.Branch;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.repository.RepositoryMetadataService;
import com.atlassian.stash.repository.RepositoryService;
import com.atlassian.stash.util.Page;
import com.sun.jersey.spi.resource.Singleton;

@AnonymousAllowed
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/projects/{projectKey}/repos/{repositorySlug}")
@Singleton
public class BranchComparisonResource {
	
	private final BranchService branchService;
	private final RepositoryService repositoryService;
	private final RepositoryMetadataService repositoryMetadataService;
	
	public BranchComparisonResource(BranchService branchService, RepositoryService repositoryService, RepositoryMetadataService repositoryMetadataService) {
		this.branchService = branchService;
		this.repositoryService = repositoryService;
		this.repositoryMetadataService = repositoryMetadataService;
	}

    @GET
	public Response getBranchComparison(@PathParam("repositorySlug") String repositorySlug, @PathParam("projectKey") String projectKey, @QueryParam("branch") String branchId) {
    	Repository repo = repositoryService.getBySlug(projectKey, repositorySlug);
    	Branch comparisonBranch = (Branch) repositoryMetadataService.resolveRef(repo, branchId);
    	BaseBranch baseBranch = branchService.getBaseBranchForBranch(repo, comparisonBranch);
    	Page<BranchComparison> branchList = branchService.getDiffsBetweenAllBranchesAndComparisonBranch(repo, comparisonBranch);
    	
    	return Response.ok(new BranchComparisonResourceModel(branchList, baseBranch)).build();
    }
}