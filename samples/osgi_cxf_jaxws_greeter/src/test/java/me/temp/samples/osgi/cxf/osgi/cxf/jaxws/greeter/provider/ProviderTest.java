package me.temp.samples.osgi.cxf.osgi.cxf.jaxws.greeter.provider;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import me.temp.samples.osgi.cxf.jaxws.greeter.provider.GreeterImpl;

import org.apache.cxf.hello_world_soap_http.Greeter;
import org.apache.cxf.hello_world_soap_http.GreeterService;
import org.apache.cxf.testutil.common.TestUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProviderTest extends Assert {
    private static final String PORT = TestUtil.getPortNumber(ProviderTest.class);
    private static final String ADDRESS = "http://localhost:" + PORT + "/cxf";

    private Endpoint endpoint;
    
    @Before
    public void setup() {
        GreeterImpl impl = new GreeterImpl();
        endpoint = Endpoint.publish(ADDRESS, impl);
        
    }
    
    @After
    public void cleanup() {
        if (endpoint != null) {
            endpoint.stop();
        }
    }
    
    @Test
    public void testServices() throws Exception {
        GreeterService service = new GreeterService(GreeterService.class.getResource("/wsdl/hello_world.wsdl"));
        Greeter greeter = service.getGreeterPort();
        ((BindingProvider)greeter).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ADDRESS);        

        System.out.println("say hi ...");
        String hi = greeter.sayHi();
        System.out.println("said: " + hi);
        assertEquals("Bonjour", hi);
        
        System.out.println("greet me ...");
        String me = greeter.greetMe("Wanna");
        System.out.println("said: " + me);
        assertEquals("Hello Wanna", me);
    }
}
