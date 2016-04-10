package mw;

import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.security.auth.login.CredentialException;
import javax.xml.namespace.QName;
import javax.xml.registry.*;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;




public class MWRegistryAccess {
		
		// Aufbau der Verbindung
		private Connection connection;
		private RegistryService regSvc;
		
		
		public void openConnection(String queryMangerURL, String lifeCycleManagerURL) {
				// Zusammenstellung der Verbindungsdaten
				Properties props = new Properties();
				//The Properties class represents a persistent set of properties. 
				//The Properties can be saved to a stream or loaded from a stream. 
				//Each key and its corresponding value in the property list is a string.
				props.setProperty("javax.xml.registry.queryManagerURL", queryMangerURL);
				props.setProperty("javax.xml.registry.lifeCycleManagerURL",lifeCycleManagerURL);
				try {
					ConnectionFactory fact = ConnectionFactory.newInstance();
					fact.setProperties(props);
					
					connection = fact.createConnection();
					//connection.setCredentials(authenticate("group8", ""));
					//Rein lesende Operationen ! keine Authentifizierung notwendig
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
					//Suchen Services
					BulkResponse br = m.findServices(null, null, namePatterns, null, null);
					
			
					//Wenn man möchtet Organizations suchen, kann man schreiben:
					//BulkResponse br = m.findOrganizations(findQualifiers,namePatterns, null, null, null, null);
					//Collection<Organization> orgs = br.getCollection();
			
					Collection<Service> services = br.getCollection();
					//System.out.println(services.size());
			
					for(Service s: services){
						Collection<ServiceBinding> sb  = s.getServiceBindings();
						for(ServiceBinding address: sb){
							System.out.println(address.getAccessURI());
						}
					}
				} catch(Exception e) { 
					System.out.println("list WSDL faild!");
				}
		}
		
		
		//Einloggen mit Benutzername und Passwort
		public void authenticate(String userName, String password){
			
			PasswordAuthentication pa = new PasswordAuthentication(userName,password.toCharArray());
			Set<PasswordAuthentication> credentials =new HashSet<PasswordAuthentication>();
			credentials.add(pa);
			try {
				connection.setCredentials(credentials);
			} catch (JAXRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//Registration von  Service
		public void registerService(String orgName, String serviceName, String wsdlURL)
		{
			BusinessLifeCycleManager lcm;
			try {
				lcm = regSvc.getBusinessLifeCycleManager();
				
				//1. createOrganization
				InternationalString os = lcm.createInternationalString(orgName);
				Organization organization = lcm.createOrganization(os);
				
				//2.createService
				InternationalString ss = lcm.createInternationalString(serviceName);
				Service service = lcm.createService(ss);
				//organization.addService(service);
				organization.removeService(service);
				
				//3.createServiceBinding
				ServiceBinding binding = lcm.createServiceBinding();
				binding.setAccessURI(wsdlURL);
				//service.addServiceBinding(binding);
				service.removeServiceBinding(binding);
				
				Collection<Organization> orgs = new ArrayList<Organization>(1);
				orgs.add(organization);
				BulkResponse response = lcm.saveOrganizations(orgs);
				
			} catch (JAXRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		public static void main(String[] args) {
			Locale.setDefault(Locale.US);
			// TODO Auto-generated method stub
			String registryURL = 	"http://134.169.47.184:4222/juddi";
			String queryManagerURL = registryURL + "/inquiry";
			String lifeCycleManagerURL = registryURL + "/publish";
			MWRegistryAccess MWRA = new MWRegistryAccess();
			MWRA.openConnection(queryManagerURL, lifeCycleManagerURL);
			
			String uri = "http://10.101.8.30:12346/MWMyPathSrv";
			
			MWRA.authenticate("gruppe8", "");
			MWRA.registerService("gruppe8", "MWPathService", uri + "?wsdl");
			
			MWRA.closeConnection();
			
		}
		
}
