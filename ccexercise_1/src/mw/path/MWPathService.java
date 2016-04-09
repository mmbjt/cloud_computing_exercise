package mw.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import mw.facebookclient.MWFacebookService;
import mw.facebookclient.MWMyFacebookService;
import mw.facebookclient.MWUnknownIDException_Exception;
import mw.facebookclient.StringArray;


@WebService(name = "MWMyPathService", serviceName = "MWMyPathSrv")
@SOAPBinding(style = SOAPBinding.Style.RPC)



public class MWPathService implements MWPathServiceInterface{
	

	private MWMyFacebookService MyFacebookService;
	
	
	public MWPathService(){
		MyFacebookService  = new MWFacebookService().getMWMyFacebookServicePort();
	}
	
	@WebMethod
	public String[] calculatePath(String startID, String endID) throws MWNoPathException {
		
		List<String> sAllFriends = null,eAllFriends = null;
		Set sAF = null,eAF = null;
		Set sAFtemp = new HashSet(), eAFtemp = new HashSet();
		try {
			StringArray newStartFriends = MyFacebookService.getFriends(startID);
			StringArray newEndFriends = MyFacebookService.getFriends(endID);
			sAllFriends = newStartFriends.getItem();
			eAllFriends = newEndFriends.getItem();
			
			sAF = new HashSet(sAllFriends);
			eAF = new HashSet(eAllFriends);
			sAF.add(startID);
			eAF.add(endID);
				
			
		} catch (MWUnknownIDException_Exception e) {
			
			e.printStackTrace();
		}
		
		
		/*Iterator<String> sit = sAF.iterator();
		while(sit.hasNext())
		{
			
				
				System.out.println(sit.next());
				
		}
		
		System.out.println("++++++++++++");
		
		Iterator<String> eit = eAF.iterator();
		while(eit.hasNext())
		{
				System.out.println(eit.next());
		}
		*/
		
		
		
		
		
		while(true)
		{
			if(!Collections.disjoint(sAF, eAF))
			{
				break;
			}
			
			Iterator<String> sit = sAF.iterator();
			while(sit.hasNext())
			{
				try {
					
					sAFtemp.addAll(MyFacebookService.getFriends(sit.next()).getItem());
					
				} catch (MWUnknownIDException_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int sSize1 = sAF.size();
			sAF.addAll(sAFtemp);
			int sSize2 = sAF.size();
			
			
			sAFtemp.clear();
					
			
			//System.out.println("+++++++++");
			
			
			
			Iterator<String> eit = eAF.iterator();
			while(eit.hasNext())
			{
				try {
					eAFtemp.addAll(MyFacebookService.getFriends(eit.next()).getItem());
				} catch (MWUnknownIDException_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			int eSize1 = eAF.size();
			eAF.addAll(eAFtemp);
			int eSize2 = eAF.size();
			eAFtemp.clear();
			
			
			
			if(sSize1 == sSize2 && eSize1 == eSize2)
			{
				throw new MWNoPathException("noPath");
			}
			
		}
		
		
		System.out.println("********");
		
		Set aF = new HashSet();
		aF.addAll(sAF);
		aF.addAll(eAF);
		
		Iterator<String> it = aF.iterator(); 
		while(it.hasNext())
		{
			String value = it.next();
			System.out.println(value);
		}
		

		
		return MWDijkstra.getShortestPath(MyFacebookService, aF, startID, endID);
	}
	
	
	public static void main(String[] args) {
        MWPathService s = new MWPathService();
        //String[] path = s.calculatePath("1694452301", "100000859170147");
        String[] path;
		try {
			//path = s.calculatePath("1694452301", "100000859170147");
			path = s.calculatePath("1832770518", "100000690315984");
			for (String str : path) {
			    System.out.println(str);
			}
			
		} catch (MWNoPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	
}
