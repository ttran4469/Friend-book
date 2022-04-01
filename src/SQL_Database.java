import java.sql.*;
import java.util.*;

public class SQL_Database implements DataStorage{
	
	final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/trant4469?useSSL=false";
	final String db_username = "trant4469";
	final String db_password = "1770317";
	
	Connection conn = null;
	Statement stm = null;
	Statement stm1 = null;
	Statement stm2 = null;
	Statement stm3 = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	
	
	
	@Override
	public void createProfile(String usereID, String password, String name, String school) {
		// TODO Auto-generated method 
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			rs = stm.executeQuery("Select * from user where id ='" + usereID + "'");
			if (rs.next()) {
				System.out.println("*****Account creation failed. The usereID was used.*****");
			}
			else {
				int r = stm.executeUpdate("Insert into user values ('" + usereID + "', '" + password + "', '" + name + "', '" + school + "')");
				System.out.println("*****Account creation sucessful!*****");
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {	             
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

	@Override
	public Profile login(String id, String password) {
		// TODO Auto-generated method stub
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			stm = conn.createStatement();
			rs = stm.executeQuery("Select * from user where id = '" + id + "'");
			if (rs.next()) {
				if (password.equals(rs.getString(2))) {
					return new Profile(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));					
				}
				else {
					return null;
				}
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void createPost(String userID, String content, String type, int parent) {
		// TODO Auto-generated method stub
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password );
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			String dt = DateAndTime.DateTime();
			//add record to post table
			int r = stm.executeUpdate("Insert into post (userID, content, datetime, type, parent) values ('" + userID + "', '" + content + "', '" + dt+ "', '" + type + "', '" + parent + "')");
			
			//query and add record to updates table
			rs = stm.executeQuery("select max(id) from post where userID = '" + userID + "'");
			if (rs.next()) {
				int r1 = stm.executeUpdate("Insert into updates (userID, type, postID) values ('" + userID + "', '" + type + "', '" + rs.getInt(1) + "')");
			}
			
//			String content = "#alo hi there #hello #hi #hi";
//			Pattern p = Pattern.compile("\\B#[a-zA-Z0-9]+\\b");
//			Matcher m = p.matcher(content);
//			while (m.find()) {
//				System.out.println(m.group(0));
//			}
			String[] words = content.split("\s");
			for(String w: words) {
				if (w.charAt(0)=='#') {
					Statement stm1 = conn.createStatement();
					ResultSet rs1 = stm.executeQuery("Select * from hashtag where word = '" + w.toUpperCase() + "'");
					if (rs1.next()) {
						Statement stm2 = conn.createStatement();
						int r2 = stm2.executeUpdate("Update hashtag set freq = freq +1 where word = '" + w.toUpperCase() + "'");							
					}
					else {
						Statement stm3 = conn.createStatement();
						int r3 = stm3.executeUpdate("Insert into hashtag (word, freq) values ('" + w.toUpperCase() + "', 1)");
					}
				}			
			}
			System.out.println("*****Your post has been created!*****");
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void updateProfileName (String userID, String newname) {
		// TODO Auto-generated method stub
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			
			int r = stm.executeUpdate("Update user set name = '" + newname + "' where id = '" + userID + "'");
			
			int r1 = stm.executeUpdate("Insert into updates (userID, type, postID) values ('" + userID + "', 'updateName', -1)");
			System.out.println("*****Your name has been updated!*****");
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void updateProfileSchool (String userID, String newschool) {
		// TODO Auto-generated method stub
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			
			int rs = stm.executeUpdate("Update user set school = '" + newschool + "' where id = '" + userID + "'");
			int r1 = stm.executeUpdate("Insert into updates (userID, type, postID) values ('" + userID + "', 'updateSch', -1)");
			
			System.out.println("*****Your school has been updated!*****");
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void requestFriend(String id1, String id2) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner (System.in);
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			String dt = DateAndTime.DateTime();
			//check if id2 exists or not
			rs = stm.executeQuery("Select * from user where id = '" + id2 + "'");
			if (rs.next()) {
				//check if id1 has sent friend request to id2	
				Statement stm1 = conn.createStatement();				
				ResultSet rs1 = stm.executeQuery("Select * from notification where (userid1 = '"+ id1 + "' and userid2 = '" + id2 + "') or (userid1 = '"+ id2 + "' and userid2 = '" + id1 + "') and type = 'f_request'");
				if (rs1.next()) {
					//yes
					System.out.println("*****!!!!!!!Failed. A friend request was sent before!*****");
				}
				else { //not yet
					String s = "";
					String mess = "";
					System.out.println("Type 'a' to send an automatic message: 'Hi " + id2 + ", I would like add you as a friend.'");
					System.out.println("or any key to custom your message:");
					s = input.next();
					if (s.equals("a")) {
						mess = "Hi, I would like add you as a friend.";			
					}
					else {
						Scanner input1 = new Scanner (System.in);
						System.out.println("Enter your customed message:");
						mess = input1.nextLine();
					}
					
					int r = stm.executeUpdate("Insert into friend (id1, id2, status) values ('" + id1 + "', '" + id2 + "', 'pending')");								
					int r1 = stm.executeUpdate("Insert into notification (userid1, userid2, message, datetime, type, status) values ('" + id1 + "', '" + id2 + "', '" + mess + "', '" + dt + "', 'f_request', 'pending')");
					System.out.println("*****Your friend request to " + id2 + " with a message: '" + mess + "' has been sent!******");
				}				
			}
			else {
				System.out.println("*****UserID " + id2 + " does not exist!*****");
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
				stm.close();
				rs.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	@Override
	public void sendMessage(String id1, String id2, String mess, String type, String status) {
		// TODO Auto-generated method stub
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			String dt = DateAndTime.DateTime();
			rs = stm.executeQuery("Select * from friend where (id1 = '" + id1 + "' and id2='" + id2 +"' and status = 'accepted') or (id2 = '" + id1 + "' and id1 = '" + id2 + "' and status = 'accepted')");
			if(rs.next()) {
				int r = stm.executeUpdate("Insert into notification (userid1, userid2, message, datetime, type, status) values ('" + id1 + "', '" + id2 + "', '" + mess + "', '" + dt + "', '" + type + "', '" + status + "')");
				
				System.out.println("*****Your message has been sent to " + id2 + " successfully!*****");
			}
			else {
				System.out.println("*****FAILED. You and " + id2 + " are not friends, so you are not allowed to send a secure message.*****");
			}
			
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void checkNoti(String userID) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner (System.in);
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			
			// list out all notifications
			int notiCount = 0;
			rs = stm.executeQuery("Select count(*) from notification where userid2 = '" + userID + "' and status = 'pending'");
			if (rs.next()) {
				notiCount= rs.getInt(1);
			}
			System.out.println("*****Total new notifications: " + notiCount + "*****");
			
			
			//show total notifications
			int counter = 1;
			rs = stm.executeQuery("Select * from notification where userid2 = '" + userID + "' and status = 'pending'");
			while(rs.next()) {
				if (rs.getString("type").equals("message")) {
					System.out.println(counter + ". \t" + "Message");
				}
				else {
					System.out.println(counter + ". \t" + "Friend request");
				}				
				counter++;			
			}
			 System.out.println();
			 
			 
			//view noti
			 counter=1;
			 /////////////////need to execute query again///////////////
			rs = stm.executeQuery("Select * from notification where userid2 = '" + userID + "' and status = 'pending'");
			int noti_id = 0;	
			String status = "";
			String id1 = "";
			String message = "";
			String dt = DateAndTime.DateTime();
			 if (notiCount != 0) {
					String s = "";
					int n = 0;
					
					System.out.println("Enter the according number to check ");
					System.out.println("or '-1' to go back to main menu: ");
					s = input.next();
					try {
						n = Integer.parseInt(s);
						}
					catch (NumberFormatException e){
						System.out.println("**Invalid number.**");
						}	
					while(rs.next()) {
						if (n==counter && rs.getString("type").equals("message")) {
							System.out.println(counter + ". \t" + rs.getString("datetime") + "\t MESSAGE from " + rs.getString("userid1") + ": \t" + rs.getString("message"));
							noti_id = rs.getInt(1);	
							id1 = rs.getString("userid1");
							System.out.println();
							
							//ask for response							
							String response = "";
							System.out.println("Do you response? Type 'y' for yes and other key for no:");
							response = input.next();
							if (response.equals("y")) {
								Scanner input1 = new Scanner(System.in);
								System.out.println("Enter your message: ");
								message = input1.nextLine();
								Statement stm1 = conn.createStatement();
								int rs = stm1.executeUpdate("Insert into notification (userid1, userid2, message, datetime, type, status) values ('" + userID + "', '" + id1 + "', '" + message + "', '" + dt + "', 'message', 'pending')");
								System.out.println("*****Your message has been sent to " + id1 + " successfully!*****");
							}			
						}
						else if (n==counter && rs.getString("type").equals("f_request")){
							System.out.println(counter + ". \t" + rs.getString("datetime") + "\t FRIEND REQUEST from " + rs.getString("userid1") + " with a message: \t" + rs.getString("message"));							
							noti_id = rs.getInt(1);	
							id1 = rs.getString("userid1");
							System.out.println();
							
							//ask confirmation of friend request							
							String response = "";
							System.out.println("Do you want to accept this friend request? Type 'y' for yes and other key for no:");
							response = input.next();
							if (response.equals("y")) {
								status = "accepted";
								System.out.println("*****You accepted the friend request from "+ id1 +"*****");
							}
							else {
								status = "denied";
								System.out.println("*****You rejected the friend request from "+ id1 +"*****");
							}	
							Statement stm2 = conn.createStatement();
							int r1 = stm2.executeUpdate("Update friend set status ='" + status +"' where id1 = '" + id1 + "' and id2 = '" + userID + "'");
						}
						counter++;			
					}
					int r = stm.executeUpdate("Update notification set status = 'viewed' where id = '" + noti_id + "'");
			 }										
			conn.commit();
			conn.setAutoCommit(true);
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}	
	}

	@Override
	public ArrayList<String> getFriendID (String userID) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			stm = conn.createStatement();
			rs = stm.executeQuery("Select * from friend where (id1 = '" + userID + "' or id2 = '" + userID + "') and status = 'accepted'");
			ArrayList<String> idList = new ArrayList<String>();
			while(rs.next()) {
				String fr_id = "";
				if(userID.equals(rs.getString("id1"))) {
					fr_id = rs.getString("id2");
				}
				else {
					fr_id = rs.getString("id1");
				}
				idList.add(fr_id);
			}
			return idList;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public Profile getProfile(String userID) {
		// TODO Auto-generated method stub
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			stm = conn.createStatement();
			rs = stm.executeQuery("Select * from user where id = '" + userID + "'");
			ArrayList <Profile> aList = new ArrayList <Profile>();
			if(rs.next()) {
				Profile p = new Profile(rs.getString(1), rs.getString(1), rs.getString(3), rs.getString(4));
				return p;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
//	@Override
//	public void getFriendList(String userID) {
//		// TODO Auto-generated method stub
//		Scanner input = new Scanner(System.in);
//		try {
//			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
//			stm = conn.createStatement();
//			rs = stm.executeQuery("Select * from friend where id1 = '" + userID + "'");
////			ArrayList <Profile> aList = new ArrayList <Profile>();
//			ArrayList<String> idList = new ArrayList<String>();
//			while(rs.next()) {
//				if (rs.getString("status").equals("accepted")) {
//					String id2 = rs.getString("id2");
//					idList.add(id2);
//				}
////				String id2 = rs.getString("id2");
////				System.out.println(id2);
////				rs = stm.executeQuery("Select * from user where id = '" + id2 + "'");
////				System.out.println(rs.getString(1) + rs.getString(1)+ rs.getString(3)+ rs.getString(4));
////				Profile p = new Profile(rs.getString(1), rs.getString(1), rs.getString(3), rs.getString(4));
////				aList.add(p);
//			}
//			for (int i = 0; i<idList.size(); i++) {
//				System.out.printf("%s: %s\n",i+1, idList.get(i));
//			}
//			System.out.println();
//			String s = "";
//			System.out.println("Enter number to see your friend's profile: ");
//			s = input.next();
//			int n = Integer.parseInt(s);
//			String fr_id = idList.get(n-1);
//			System.out.println(fr_id);
//			
//			rs = stm.executeQuery("Select * from user where id = '" + fr_id + "'");
//			System.out.println(rs);
//			System.out.println("UserID: " + rs.getString(1) + "' \t Name: " + rs.getString(3) + "' \t School: " + rs.getString(4));
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally {
//			try {
//				conn.close();
//				stm.close();
//				rs.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	@Override
	public int showWall(String userID) {
		// TODO Auto-generated method stub
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			stm = conn.createStatement();
			rs = stm.executeQuery("Select id, userID, type, postID from updates where userID in (select id2 from friend where id1 = '" + userID +"' and status = 'accepted') or userID in (select id1 from friend where id2 = '" + userID + "' and status = 'accepted') order by id desc limit 3");
			int counter = 1;
			
			//System.out.println("**Newest friend's updates and posts**");
			//System.out.println();

				while(rs.next()) {
				if(rs.getString("type").equals("post")) {
					String postID = rs.getString("postID");
					Statement stm1 = conn.createStatement();
					ResultSet rs1 = stm1.executeQuery("Select * from post where id ='" + postID +"'");
					if (rs1.next()) {
						System.out.println(counter + ". Post from " + rs1.getString("userID")+ " - " + rs1.getString("datetime") + " - " + rs1.getString("content"));						
					}					
				}
				else if (rs.getString("type").equals("updateName")) {
					String frID = rs.getString("userID");
					Statement stm2 = conn.createStatement();
					ResultSet rs2 = stm2.executeQuery("Select * from user where id ='" + frID +"'");
					if (rs2.next()) {
						System.out.println(counter + ". " + frID + " updated his/ her name to " + rs2.getString("name"));							
					}		
				}
				else if (rs.getString("type").equals("updateSch")) {
					String frID = rs.getString("userID");
					Statement stm3 = conn.createStatement();
					ResultSet rs3 = stm3.executeQuery("Select * from user where id ='" + userID +"'");
					if (rs3.next()) {
						System.out.println(counter + ". " + frID + " updated his/ her school to " + rs3.getString("school"));							
					}		
				}
				System.out.println();
				counter++;			
			}
			return counter;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}finally {
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void commentPostUpdate(String userID, int n) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			conn.setAutoCommit(false);
			stm = conn.createStatement();
			rs = stm.executeQuery("Select id, userID, type, postID from updates where userID in (select id2 from friend where id1 = '" + userID +"' and status = 'accepted') or userID in (select id1 from friend where id2 = '" + userID + "' and status = 'accepted') order by id desc limit 3");
			int counter = 1;
			String s = "";
			String comment = "";
			String dt = DateAndTime.DateTime();
			
				while(rs.next()) {
				if(n==counter && rs.getString("type").equals("post")) {
					String postID = rs.getString("postID");
					Statement stm1 = conn.createStatement();
					ResultSet rs1 = stm1.executeQuery("Select * from post where id ='" + postID +"'");
					int cmt_count = 1;
					if (rs1.next()) {
						System.out.println(counter + ". Post from " + rs1.getString("userID")+ " - " + rs1.getString("datetime") + " - " + rs1.getString("content"));
						Statement stm2 = conn.createStatement();
						ResultSet rs2 = stm2.executeQuery("Select * from post where parent = '" + postID + "'" );
						while(rs2.next()) {
							System.out.println("\t Comment #" + cmt_count +": By " + rs2.getString("userID") + " - " + rs2.getString("datetime") + " - " + rs2.getString("content"));
							cmt_count++;
						}
						System.out.println();
						System.out.println("Do you want to give comment? Type 'y' for yes and any other key for no:");
						s = input.nextLine();
						if (s.equals("y")) {
							System.out.println("Enter your comment:");
							comment = input.nextLine();
							int r = stm1.executeUpdate("Insert into post (userID, content, datetime, type, parent) values ('" + userID + "', '" + comment + "', '" + dt+ "', 'comment', '" + postID + "')");
							System.out.println("*****Your comment has been added successfully*****");
						}				
					}					
				}
				else if (n==counter && rs.getString("type").equals("updateName")) {
					String postID = rs.getString("postID");
					String frID = rs.getString("userID");
					Statement stm2 = conn.createStatement();
					ResultSet rs2 = stm2.executeQuery("Select * from user where id ='" + frID +"'");
					if (rs2.next()) {
						System.out.println(counter + ". " + frID + " updated his/ her name to " + rs2.getString("name"));	
//						System.out.println();
//						System.out.println("Do you want to give comment? Type 'y' for yes and any other key for no:");
//						s = input.nextLine();
//						if (s.equals("y")) {
//							System.out.println("Enter your comment:");
//							comment = input.nextLine();
//							int r = stm2.executeUpdate("Insert into post (userID, content, datetime, type, parent) values ('" + userID + "', '" + comment + "', '" + dt+ "', 'comment', '" + postID + "')");
//							System.out.println("*****Your comment has been added successfully*****");
//						}					
					}		
				}
				else if (n==counter && rs.getString("type").equals("updateSch")) {
					String postID = rs.getString("postID");
					String frID = rs.getString("userID");
					Statement stm3 = conn.createStatement();
					ResultSet rs3 = stm3.executeQuery("Select * from user where id ='" + userID +"'");
					if (rs3.next()) {
						System.out.println(counter + ". " + frID + " updated his/ her school to " + rs3.getString("school"));	
//						System.out.println();
//						System.out.println("Do you want to give comment? Type 'y' for yes and any other key for no:");
//						s = input.nextLine();
//						if (s.equals("y")) {
//							System.out.println("Enter your comment:");
//							comment = input.nextLine();
//							int r = stm3.executeUpdate("Insert into post (userID, content, datetime, type, parent) values ('" + userID + "', '" + comment + "', '" + dt+ "', 'comment', '" + postID + "')");
//							System.out.println("*****Your comment has been added successfully*****");
//						}	
					}		
				}
				counter++;			
			}	
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	


	@Override
	public void seeHashtag(String userID) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		try {
			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
			stm = conn.createStatement();
			rs = stm.executeQuery("Select * from hashtag order by freq desc limit 3");
			int counter = 1;
			while(rs.next()) {
				System.out.println(counter + ". " + rs.getString("word"));
				counter++;
			}
			System.out.println();
			
			
			String sel = "";
			while(!sel.equals("x")) {
				System.out.println("Enter the according number to see your friend's post(s) with that hashtag ");
				System.out.println("or 'x' to go back to main menu: ");
				sel = input.next();
				
				rs = stm.executeQuery("Select * from hashtag order by freq desc limit 3");
				counter = 1;				
				while(rs.next()) {
					if (Integer.toString(counter).equals(sel)) {
						String hashtag = rs.getString("word");
						stm1 = conn.createStatement();
						rs1 = stm1.executeQuery("Select * from post where userID in (select id1 from friend where id2 = '" + userID + "' and status='accepted') or userID in (select id2 from friend where id1 = '" + userID + "' and status='accepted') ");		
						///	select * from post where userID in (select id1 from friend where id2 = 'thao1#') or userID in (select id2 from friend where id1 = 'thao1#')			
						int count = 1;
						while(rs1.next()) {
							String content = rs1.getString("content");
							String [] words = content.split("\s");							
							for (String w: words) {
								w = w.toUpperCase();
								if (w.equals(hashtag)) {
									System.out.println(count + ". Post from " + rs1.getString("userID") + " - " + rs1.getString("datetime") + ": " + content);		
									System.out.println();
									count++;
									break;
								}								
							}							
						}
					}
					counter++;
				}
			}
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
				stm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
	}



//	@Override
//	public int showNoti (String userID) {
//		// TODO Auto-generated method stub
//		Scanner input = new Scanner(System.in);
//		try {						
//			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
//			stm = conn.createStatement();
//			// list out all notifications
//			rs = stm.executeQuery("Select * from notification where userid1 = '" + userID + "' and status = 'pending'");
//			int counter = 1;
//
//			while(rs.next()) {
//				if (rs.getString("type").equals("message")) {
//					System.out.println(counter + ". \t" + "Message");
//				}
//				else {
//					System.out.println(counter + ". \t" + "Friend request");
//				}				
//				counter++;			
//			}
//			//show total notifications
//			rs = stm.executeQuery("Select count(*) from notification where userid1 = '" + userID + "' and status = 'pending'");
//			if (rs.next()) {
//				return rs.getInt(1);
//			}
//			return 0;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return 0;
//		}finally {
//			try {
//				conn.close();
//				stm.close();
//				rs.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();		
//			}
//		}						
//	}
//	@Override
//	public String checkNoti(String userID, int n) {
//		// TODO Auto-generated method stub
//		Scanner input = new Scanner(System.in);
//		try {						
//			conn = DriverManager.getConnection(DATABASE_URL, db_username, db_password);
//			conn.setAutoCommit(false);
//			stm = conn.createStatement();
//			int counter = 1;
//			int noti_id = 0;
//			String type  = "";
//			rs = stm.executeQuery("Select * from notification where userid1 = '" + userID + "' and status = 'pending'");
//			while(rs.next()) {
//				if (n==counter && rs.getString("type").equals("message")) {
//					System.out.println(counter + ". \t" + rs.getString("datetime") + "\t MESSAGE from " + rs.getString("userid2") + ": \t" + rs.getString("message"));
//					noti_id = rs.getInt(1);	
//					type = rs.getString("type");
//				}
//				else if (n==counter && rs.getString("type").equals("f_request")){
//					System.out.println(counter + ". \t" + rs.getString("datetime") + "\t FRIEND REQUEST from " + rs.getString("userid2") + " with message: \t" + rs.getString("message"));
//					noti_id = rs.getInt(1);
//					type = rs.getString("type");
//				}
//				counter++;			
//			}
//			int r = stm.executeUpdate("Update notification set status = 'viewed' where id = '" + noti_id + "'");
//			conn.commit();
//			conn.setAutoCommit(true);
//			return type;
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}finally {
//			try {
//				conn.close();
//				stm.close();
//				rs.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				
//			}
//		}
//	}
//


}

