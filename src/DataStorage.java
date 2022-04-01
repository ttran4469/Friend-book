import java.util.ArrayList;

public interface DataStorage {
	void createProfile(String userID, String password, String name, String school);
	Profile login(String id, String password);
	void createPost(String userID, String content, String type, int parent);
	void updateProfileName (String userID, String newname);
	void updateProfileSchool (String userID, String newschool);
	void requestFriend(String id1, String id2);
	void sendMessage (String id1, String id2, String mess, String type, String status);
//	int showNoti (String userID);
//	String checkNoti (String userID, int n);
//	void updateFriendStatus (String userID);
	void checkNoti (String userID);
	ArrayList<String> getFriendID (String userID);
	Profile getProfile(String userID);
//	void getFriendList(String userID);
	int showWall(String userID);
	void commentPostUpdate(String userID, int n);
	void seeHashtag(String userID);
}	
