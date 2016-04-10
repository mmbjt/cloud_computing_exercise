package mw.path;

import java.util.*;

import javax.jws.*;
import javax.jws.soap.*;

import mw.*;
import mw.facebookclient.*;


public class MWMyPathService {

	
    protected MWMyFacebookService fb = new MWFacebookService().getMWMyFacebookServicePort();
   
    public String[] calculatePath(String startID, String endID) throws MWNoPathException {

    		Set<String> friendsAll = new HashSet<String>(); 
            List<String> friendsStart, friendsEnd;
            try {
                friendsStart = fb.getFriends(startID).getItem();
                friendsEnd = fb.getFriends(endID).getItem();
            } catch(MWUnknownIDException_Exception e) {
            	throw new MWNoPathException("Start or end not found!");
            }

           
            List<String> newFriendsStart = new ArrayList(friendsStart), newFriendsEnd = new ArrayList(friendsEnd);
            
            //Hauptloop
            while(true) {
            	
            	
                if(!Collections.disjoint(friendsStart, friendsEnd)) {
                    break;
                }

                {
                    // list for newly found friends
                    List<String> newFriends = new ArrayList<String>();
                    // iterate over new friends (from last loop)
                    for(String id : newFriendsStart) {
                        try {
                            newFriends.addAll(fb.getFriends(id).getItem());
                        } catch(MWUnknownIDException_Exception e) {
                            System.err.println("Bad person in friendlist?");
                        }
                    }
                    // next loop's newFriendsStart is all new friends
                    newFriendsStart = new ArrayList<String>(newFriends);
                    // minus everyone who's already in the friend list
                    newFriendsStart.removeAll(friendsStart);
                    // then add new friends to the list
                    friendsStart.addAll(newFriends);
                }

                {
                    List<String> newFriends = new ArrayList<String>();
                    for(String id : newFriendsEnd) {
                        try {
                            newFriends.addAll(fb.getFriends(id).getItem());
                        } catch(MWUnknownIDException_Exception e) {
                            System.err.println("Bad person in friendlist?");
                        }
                    }
                    newFriendsEnd = new ArrayList<String>(newFriends);
                    newFriendsEnd.removeAll(friendsEnd);
                    friendsEnd.addAll(newFriends);
                }

            }
            // combine the friend lists
            friendsAll.addAll(friendsStart);
            friendsAll.addAll(friendsEnd);
        
        friendsAll.add(startID);
        friendsAll.add(endID);

        return MWDijkstra.getShortestPath(fb, friendsAll, startID, endID);

    }

    public static void main(String[] args) {
        MWMyPathService s = new MWMyPathService();
        try {
        	String[] path = s.calculatePath("1832770518", "100000690315984");
            for (String str : path) {
                System.out.println(str);
            }
        } catch(MWNoPathException e) {
            e.printStackTrace();
        }
    }

}