package au.com.denisefernandez.stash.plugin.branchlist.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.denisefernandez.stash.plugin.branchlist.service.BranchService;

import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.repository.RepositoryService;
import com.google.common.collect.ImmutableMap;

public class BranchListServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(BranchListServlet.class);
    
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final RepositoryService repositoryService;
    private final BranchService branchService;
    private final WebResourceManager webResourceManager;
    
    public BranchListServlet(SoyTemplateRenderer soyTemplateRenderer, RepositoryService repositoryService, BranchService branchService, WebResourceManager webResourceManager) {
    	this.soyTemplateRenderer = soyTemplateRenderer;
    	this.repositoryService = repositoryService;
    	this.branchService = branchService;
    	this.webResourceManager = webResourceManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// Get repoSlug from path
        String pathInfo = req.getPathInfo();
        String[] components = pathInfo.split("/");
        if (components.length < 3) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Repository repository = repositoryService.getBySlug(components[1], components[2]);
        if (repository == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        
    	
    	String template =  "stash.plugin.branchlist.viewbranches";
    	render(resp, template, ImmutableMap.<String, Object>builder()
        		.put("repository", repository)
        		.put("branchListPage", branchService.getDiffsBetweenBranchesAndMaster(repository))
        		.put("defaultBranch", branchService.getDefaultBranch(repository))
        		.build()
        		);
    }
    
    private void render(HttpServletResponse resp, String templateName, Map<String, Object> data) throws IOException, ServletException {
    	webResourceManager.requireResourcesForContext("stash-branch-list-plugin");
        resp.setContentType("text/html;charset=UTF-8");
        try {
            soyTemplateRenderer.render(resp.getWriter(),
                    "au.com.denisefernandez.stash-branch-list-plugin:branchlist-soy-server",
                    templateName,
                    data);
        } catch (SoyException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new ServletException(e);
        }
    }

}