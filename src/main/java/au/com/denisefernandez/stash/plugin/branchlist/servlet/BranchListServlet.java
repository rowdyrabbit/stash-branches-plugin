package au.com.denisefernandez.stash.plugin.branchlist.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.denisefernandez.stash.plugin.branchlist.service.BaseBranch;
import au.com.denisefernandez.stash.plugin.branchlist.service.BranchService;

import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.stash.repository.Branch;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.repository.RepositoryMetadataService;
import com.atlassian.stash.repository.RepositoryService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

public class BranchListServlet extends HttpServlet {

	private static final long serialVersionUID = 584740550301449195L;

    private final SoyTemplateRenderer soyTemplateRenderer;
    private final RepositoryService repositoryService;
    private final BranchService branchService;
    private final WebResourceManager webResourceManager;
    private final RepositoryMetadataService repositoryMetadataService;
    
    public BranchListServlet(SoyTemplateRenderer soyTemplateRenderer, 
    						 RepositoryService repositoryService, 
    						 BranchService branchService, 
    						 WebResourceManager webResourceManager, 
    						 RepositoryMetadataService repositoryMetadataService) {
    	this.soyTemplateRenderer = soyTemplateRenderer;
    	this.repositoryService = repositoryService;
    	this.branchService = branchService;
    	this.webResourceManager = webResourceManager;
    	this.repositoryMetadataService = repositoryMetadataService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// Get repoSlug from path
        String pathInfo = req.getPathInfo();
        if (Strings.isNullOrEmpty(pathInfo) || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String[] pathComponents = pathInfo.split("/");
        if (pathComponents.length < 3) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Repository repository = repositoryService.getBySlug(pathComponents[1], pathComponents[2]);
        if (repository == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        doView(repository, req, resp);
        
    }
    
    private void doView(Repository repository, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String template =  "stash.plugin.branchlist.viewbranches";
    	Branch defaultBranch = repositoryMetadataService.getDefaultBranch(repository);
    	BaseBranch baseBranch = branchService.getBaseBranchForBranch(repository, defaultBranch);
    	render(resp, template, ImmutableMap.<String, Object>builder()
    			.put("repository", repository)
    			.put("branchListPage", branchService.getDiffsBetweenAllBranchesAndComparisonBranch(repository, defaultBranch))
    			.put("defaultBranch", baseBranch)
    			.build()
		);
		
	}


	private void render(HttpServletResponse resp, String templateName, Map<String, Object> data) throws IOException, ServletException {
    	webResourceManager.requireResourcesForContext("stash-branch-list-plugin");
        resp.setContentType("text/html;charset=UTF-8");
        try {
            soyTemplateRenderer.render(resp.getWriter(), "au.com.denisefernandez.stash-branch-list-plugin:branchlist-soy-server", templateName, data);
        } catch (SoyException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new ServletException(e);
        }
    }

}