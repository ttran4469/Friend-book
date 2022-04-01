import java.util.ArrayList;
import java.util.Scanner;

public class Profile {
	private String userID = "";
	private String password = "";
	private String name = "";
	private String school = "";

	private DataStorage data;
	

	int num_update = 0;
	
	public Profile(String id, String password, String name, String school) {
		userID = id;
		this.password = password;
		this.name = name;
		this.school = school;
	}
	public void welcome() {
		System.out.println();
		System.out.println("*****Hello " + name + ", welcome to your profile.*****");
		System.out.println();
		num_update = data.showWall(userID);
		
		Scanner input = new Scanner(System.in);
		String sel = "";
			
		while (!sel.equals("x")) {
			System.out.println();
			System.out.println("Please make your selection: ");
			System.out.println("1. Select an update and post");
			System.out.println("2. Check notifications");
			System.out.println("3. Create a new post");
			System.out.println("4. Friends");
			System.out.println("5. Update profile");
			System.out.println("6. Send a message");
			System.out.println("7. Send a friend request");
			System.out.println("8. See hashtag in trends");
			System.out.println("x. Sign out");
			System.out.println();
			
			sel = input.next();
			if (sel.equals("1")) {
				checkUpdatePost();
			}
			else if (sel.equals("2")) {
				checkNotification();
			}
			else if (sel.equals("3")) {
				createPost("post");
			}
			else if (sel.equals("4")) {
				seeFriendlist();
			}
			else if (sel.equals("5")) {
				updateProfile();
			}
			else if (sel.equals("6")) {
				sendMessage("message");
			}
			else if (sel.equals("7")) {
				requestFriend();
			}
			else if (sel.equals("8")) {
				seeTrendyHashtag();
			}
			else {
				;
			}
		}
	}
	
	public void checkUpdatePost() {
		// TODO Auto-generated method stub
		Scanner input = new Scanner (System.in);
		String s = "";
		num_update = data.showWall(userID);		
		if (num_update>1) {
			System.out.println("Enter the according number to select and give comment: ");
			System.out.println("or '-1' to go back to main menu: ");
			System.out.println();
			s = input.next();
			try {
				int n = Integer.parseInt(s);
				data.commentPostUpdate(userID, n);
				}
			catch (NumberFormatException e){
				System.out.println("**Invalid number.**");
				}						
		}	
		else {
			System.out.println("*****You have no post/ update from your friend to check.*****");
		}
	}
	
	public void checkNotification() {
//		//show total notification
//		int notiCount = data.showNoti(userID);
//		
//		//check notification
//		Scanner input = new Scanner(System.in);
//		String s = "";
//		String respond = "";
//		int n;
//		if (notiCount != 0) {
//			System.out.println("*****Total new notifications: " + notiCount + "*****");
//			System.out.println();
//			System.out.println("Enter the number you want to check: ");
//			s = input.next();
//			n = Integer.parseInt(s);
//			String type = data.checkNoti(userID, n);
//			if (type.equals("fr_request")) {
//				System.out.println("Do you want to accept this friend request? Type 'y' for yes and other key for no:");
//				respond = input.next();
//				if (respond.equals("y")) {
//					data.updateFriendStatus(userID);
//				}
//			}
//		}
//		else {
//			System.out.println("*****No new notification!*****");
//		}
		data.checkNoti(userID);
	}

	public void createPost(String type) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner (System.in);

		String content = "";

		System.out.println("Please type your post content: ");
		content = input.nextLine();
		
		data.createPost(userID, content, type, -1);		
	}
	
	public void seeFriendlist() {
		// TODO Auto-generated method stub
		//array list of friends
		ArrayList<Profile> friendList = new ArrayList<Profile>();
		ArrayList<String> friendID = new ArrayList<String>();
		friendID = data.getFriendID(userID);
		//show fiend list ID
		for (int i=0; i<friendID.size(); i++) {
			System.out.println((i+1) + ". " + friendID.get(i));
			Profile p = data.getProfile(friendID.get(i));
			friendList.add(p);
		}
		
		//check friend profile
		if(friendID.size()!=0) {
			Scanner input = new Scanner (System.in);
			String s = "";
			int n=0;
			System.out.println();
			System.out.println("Enter the according number to see your friend's profile ");
			System.out.println("or '-1' to go back to main menu: ");
			s = input.next();
			try {
				n = Integer.parseInt(s);
				if(n>=1 && n<=friendList.size()){
					System.out.println("***** UserID: "+ friendList.get(n-1).getuserID() + " - Name: "+ friendList.get(n-1).getName() + " - School: " + friendList.get(n-1).getSchool() + "*****");
				}
			}
			catch (NumberFormatException e){
				System.out.println("**Invalid number.**");
				}
		}
		else {
			System.out.println("*****Your friend list is empty!*****");
		}
			
	}
	
	private void updateProfile() {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		String sel = "";
		System.out.println("Do you want to update your name? Type 'y' for YES or any key for NO: ");
		sel = input.next();
		
		if (sel.equals("y")) {
			System.out.println("Plese enter the name you would like to update: ");
			String newname = input.next();
			data.updateProfileName(userID, newname);			
		}
		System.out.println("Do you want to update your school? Type 'y' for YES or any key for NO: ");
		sel = input.next();
		if (sel.equals("y")) {
			System.out.println("Plese enter the school you would like to update: ");
			String newschool = input.next();
			data.updateProfileSchool(userID, newschool);			
		}
//		else if (sel.equals("3")) {
//			System.out.println("Plese enter the name you would like to update: ");
//			String newname = input.next();
//			System.out.println("Plese enter the school you would like to update: ");
//			String newschool = input.next();
//			data.updateProfile(userID, newname, newschool);
//			System.out.println("*****Your profile has been updated!*****");
//		}		
	}
	
	public void sendMessage(String type) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		String profileID;
		String mess = "";
		System.out.println("Enter the userID that you want to send message:");
		profileID = input.next();
		
		Scanner input1 = new Scanner(System.in);
		System.out.println("Enter your message: ");
		mess = input1.nextLine();
		
		data.sendMessage(userID, profileID, mess, type, "pending");
		//(String id1, String id2, String mess, String type, String status);
	}
	
	
	public void requestFriend() {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		String friendID = "";
		System.out.println("Enter the userID that you want to request friend:");
		friendID = input.next();
		
//		String s = "";
//		String mess = "";
//		System.out.println("Type 'a' to send an automatic message: 'Hi " + friendID + ", I would like add you as a friend.'");
//		System.out.println("or any key to custom your message:");
//		s = input.next();
//		if (s.equals("auto")) {
//			mess = "Hi, I would like add you as a friend.";			
//		}
//		else {
//			Scanner input1 = new Scanner (System.in);
//			System.out.println("Enter your customed message:");
//			mess = input1.nextLine();
//		}
//		data.requestFriend(userID, friendID, mess, "f_request", "pending");
		data.requestFriend(userID, friendID);
	}
	
	public void seeTrendyHashtag() {
		// TODO Auto-generated method stub
		data.seeHashtag(userID);
	}
	
	public String getuserID() {
		return userID;
	}
	public void setuserID(String userID) {
		this.userID = userID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public DataStorage getData() {
		return data;
	}
	public void setData(DataStorage data) {
		this.data = data;
	}
	

}
