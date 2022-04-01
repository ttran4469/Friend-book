import java.util.ArrayList;
import java.util.Scanner;

public class Friendsbook {

	public static void main(String[] args) {
		// TODO Auto-generated method stub	
//		String [] a = {"1","2", "3"};
//		System.out.println(a.length);
//		ArrayList<String> a1 = new ArrayList<String>();
//		a1.add("1");
//		System.out.println(a1.size());
		
		
		Scanner input = new Scanner(System.in);
		String sel = "";
		DataStorage data = new SQL_Database();
		
		while(!sel.equals("x")) {
			System.out.println();
			System.out.println("Welcome to Friendsbook. Please make a selection:");
			System.out.println("1. Register");
			System.out.println("2. Login");
			System.out.println("x. Leave");
			
			//user enter an option
			sel = input.nextLine();
			if(sel.equals("1")) {
				//register
				new AccountCreator(data).register();
			}
			else if(sel.equals("2")) {
				//login
				new OnlineSystem(data).login();
			}
		}
	}

}
