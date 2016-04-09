package mw.path;

import javax.xml.ws.Endpoint;

public class MWPathPubishSrv {
	public static void main(String[] args) throws Exception {

		
		String wsdl = "http://10.101.8.30:12346/MWMyPathSrv?wsdl";

	
		MWPathServiceInterface adder = new MWPathService();

	
		Endpoint e = Endpoint.publish(wsdl, adder);

	
		System.out.println("Adder ready: " + e.isPublished());

	
		while(true) {
			
			Thread.sleep(Long.MAX_VALUE);

		}

	}

}
