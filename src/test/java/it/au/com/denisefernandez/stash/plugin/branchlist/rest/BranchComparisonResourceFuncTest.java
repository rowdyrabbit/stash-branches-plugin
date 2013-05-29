package it.au.com.denisefernandez.stash.plugin.branchlist.rest;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import au.com.denisefernandez.stash.plugin.branchlist.rest.BranchComparisonResource;
import au.com.denisefernandez.stash.plugin.branchlist.rest.BranchComparisonResourceModel;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

public class BranchComparisonResourceFuncTest {

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void messageIsValid() {

        String baseUrl = System.getProperty("baseurl");
        String resourceUrl = baseUrl + "/rest/branchcomparison/1.0/message";

        RestClient client = new RestClient();
        Resource resource = client.resource(resourceUrl);

        BranchComparisonResourceModel message = resource.get(BranchComparisonResourceModel.class);

        assertEquals("wrong message","Hello World",message.getMessage());
    }
}
