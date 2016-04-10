package mw.path;

import javax.xml.ws.Endpoint;

public class MWPathPubishSrv {
	public static void main(String[] args) throws Exception {

		
		String wsdl = "http://localhost:12345/MWPathSrv?wsdl";

	
		MWPathServiceInterface adder = new MWMyPathService();

	
		Endpoint e = Endpoint.publish(wsdl, adder);

	
		System.out.println("Adder ready: " + e.isPublished());

	
		while(true) {
			
			Thread.sleep(Long.MAX_VALUE);

		}

	}

}
