package ut.au.com.denisefernandez.stash.plugin.branchlist.rest;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import au.com.denisefernandez.stash.plugin.branchlist.rest.BranchComparisonResource;
import au.com.denisefernandez.stash.plugin.branchlist.rest.BranchComparisonResourceModel;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericEntity;

public class BranchComparisonResourceTest {

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void messageIsValid() {
        BranchComparisonResource resource = new BranchComparisonResource();

        Response response = resource.getMessage();
        final BranchComparisonResourceModel message = (BranchComparisonResourceModel) response.getEntity();

        assertEquals("wrong message","Hello World",message.getMessage());
    }
}
