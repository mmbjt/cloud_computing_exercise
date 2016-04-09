package mw;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.security.auth.login.CredentialException;
import javax.xml.registry.*;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;


public class MWRegistryAccess {
		
		// Aufbau der Verbindung
		public Connection connection;
		public RegistryService regSvc;
		public Set<PasswordAuthentication>  verification(){
			String user = "gruppe0"; // entspr. Gruppennummer einsetzen
			String password = "";
			// Credentials erzeugen
			PasswordAuthentication pa = new PasswordAuthentication(user,password.toCharArray());
			Set<PasswordAuthentication> credentials =new HashSet<PasswordAuthentication>();
			credentials.add(pa);
			return credentials;
		}
		public void openConnection(String queryMangerURL, String lifeCycleManagerURL) {
				// Zusammenstellung der Verbindungsdaten
				Properties props = new Properties();
				props.setProperty("javax.xml.registry.queryManagerURL", queryMangerURL);
				props.setProperty("javax.xml.registry.lifeCycleManagerURL",lifeCycleManagerURL);
				try {
					ConnectionFactory fact = ConnectionFactory.newInstance();
					fact.setProperties(props);
					
					connection = fact.createConnection();
					//connection.setCredentials(verification());
					//System.out.println(connection.isClosed());
					regSvc = connection.getRegistryService();
					} catch(Exception e) {
						System.out.println("connection faild!");
					}
		}
		
		public void closeConnection(){
				try {
					connection.close();
					//System.out.println(connection.isClosed());
				} catch (JAXRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} 
		
		public void listWSDLs(String serviceName){
			
			// Erzeugung der Suchkriterien
			Collection<String> findQualifiers = new ArrayList<String>();
			findQualifiers.add(FindQualifier.SORT_BY_NAME_ASC);
			// Erzeugung der Namenskriterien
			Collection<String> namePatterns = new ArrayList<String>();
			namePatterns.add("%"+serviceName+"%");
			// Ausfuehrung der Suche
			try {
			BusinessQueryManager m = regSvc.getBusinessQueryManager();
			BulkResponse br = m.findServices(null, null, namePatterns, null, null);;
			Collection<Service> services = br.getCollection();
			//System.out.println(services.size());
			
			for(Service s: services){
				Collection<ServiceBinding> sb  = s.getServiceBindings();
				for(ServiceBinding address: sb){
					System.out.println(address.getAccessURI());
				}
			}
            //Iterator<Service> its = services.iterator();
            //while(its.hasNext()){
            	//System.out.println("gut");
                 
            	//s.getWSDLDocumentLocation();
          //  }
			} catch(Exception e) { 
				System.out.println("list WSDL faild!");
			}
		}
}
