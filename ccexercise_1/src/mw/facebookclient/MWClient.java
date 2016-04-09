package mw.facebookclient;

import java.util.ArrayList;
import java.util.List;

public class MWClient {
	private MWMyFacebookService MyFacebookService;
	public MWClient(){
		MyFacebookService  = new MWFacebookService().getMWMyFacebookServicePort();
	}
	public void searchIDs(String name){
		StringArray arrayofId = MyFacebookService.searchIDs(name);
		List<String> idList = arrayofId.getItem();
		if(idList.size() == 0){
			System.out.println("no found!!");
		}
		else{
			for(int i=0;i<idList.size();i++){
				String id = idList.get(i);
				System.out.println(id);
			}
		}
	} 
	public void getFriends(String id){
		try {
			StringArray arrayOfFriend = MyFacebookService.getFriends(id);
			List<String> friendList = arrayOfFriend.getItem();
			List<String> idOfFriendList = new ArrayList<String>();
			if(friendList.size() == 0){
				System.out.println("no found!!");
			}
			for(int i=0;i<friendList.size();i++){
				String idOfFriend = friendList.get(i);
				idOfFriendList.add(idOfFriend);
				//System.out.println( idOfFriend);
			}
			for(int i=0;i<idOfFriendList.size();i++){
				String name = MyFacebookService.getName(idOfFriendList.get(i));
				System.out.println(name);
			}
		} catch (MWUnknownIDException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MWClient client = new MWClient();
		client. getFriends("1694452301");
		client.searchIDs("Samy Kartit");
	}

}
