/*
 * Copyright (c) Microsoft Corporation
 * <p/>
 * All rights reserved.
 * <p/>
 * MIT License
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.microsoft.tooling.msservices.serviceexplorer.azure.rediscache;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.ImmutableList;
import com.microsoft.azure.management.Azure;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.authmanage.SubscriptionManager;
import com.microsoft.azuretools.sdkmanage.AzureManager;
import com.microsoft.rest.RestClient;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.microsoft.tooling.IntegrationTestBase;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.helpers.UIHelper;
import com.microsoft.tooling.msservices.helpers.collections.ObservableList;
import com.microsoft.tooling.msservices.serviceexplorer.Node;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionListener;

import junit.framework.Assert;

@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({
    AuthMethodManager.class, 
    AzureManager.class, 
    SubscriptionManager.class,
    DefaultLoader.class
})

public class RedisCacheIntegrationTest extends IntegrationTestBase {
    
    @Mock
    private AuthMethodManager authMethodManagerMock;
    
    @Mock
    private AzureManager azureManagerMock;
    
    @Mock
    private SubscriptionManager subscriptionManagerMock;
    
    @Mock
    private UIHelper uiHelper;
    
    private Azure azure;
    
    private RedisCacheModule redisModule;
    
    private String defaultSubscription;
    
    @Before
    public void setUp() throws Exception {
        setUpStep();
    }
    
    @Test
    public void testRedisCache() {
        try {
            redisModule.refreshItems();
            ObservableList<Node> nodes = redisModule.getChildNodes();
            assertEquals(1, nodes.size());
            
            assertTrue(nodes.get(0) instanceof RedisCacheNode);
            RedisCacheNode redisCacheNode = (RedisCacheNode) nodes.get(0);
            
            redisModule.removeNode(this.defaultSubscription, redisCacheNode.getResourceId(), redisCacheNode);
            nodes = redisModule.getChildNodes();
            assertEquals(0, nodes.size());
            // TODO: add create case
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            fail();
        }
    }
    
    @After
    public void tearDown() throws Exception {
        resetTest(name.getMethodName());
    }
    
    @Override
    protected void initialize(RestClient restClient, String defaultSubscription, String domain) throws Exception {
        Azure.Authenticated azureAuthed = Azure.authenticate(restClient, defaultSubscription, domain);
        azure = azureAuthed.withSubscription(defaultSubscription);
        this.defaultSubscription = defaultSubscription;
        Set<String> sidList = Stream.of(defaultSubscription).collect(Collectors.toSet());

        PowerMockito.mockStatic(AuthMethodManager.class);
        when(AuthMethodManager.getInstance()).thenReturn(authMethodManagerMock);
        when(authMethodManagerMock.getAzureManager()).thenReturn(azureManagerMock);
        when(azureManagerMock.getAzure(anyString())).thenReturn(azure);
        when(azureManagerMock.getSubscriptionManager()).thenReturn(subscriptionManagerMock);
        when(subscriptionManagerMock.getAccountSidList()).thenReturn(sidList);
        
        PowerMockito.mockStatic(DefaultLoader.class);
        when(DefaultLoader.getUIHelper()).thenReturn(uiHelper);
        when(uiHelper.isDarkTheme()).thenReturn(false);

        redisModule = new RedisCacheModule(null){
            protected void loadActions(){}
        };
        
        Node.setNode2Actions(new HashMap<Class<? extends Node>, ImmutableList<Class<? extends NodeActionListener> > >());
    }
}
