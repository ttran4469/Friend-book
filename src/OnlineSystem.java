import java.util.Scanner;

public class OnlineSystem {
	
	private Profile theLoginAccount;
	
	private DataStorage data;
	
	public OnlineSystem(DataStorage d) {
		data = d;
		theLoginAccount = null;
	}
	public void login() {
			
			Scanner input = new Scanner(System.in);
			String loginID = "";
			String password = "";
			
			System.out.println("Please enter your login ID:");
			loginID = input.next();
			System.out.println("Please enter your password:");
			password = input.next();
			
	        theLoginAccount = data.login(loginID, password); 
	        if(theLoginAccount != null)
	        {
	            theLoginAccount.setData(data);
	            theLoginAccount.welcome();
	        }
	        else
	        {
	            System.out.println("The login failed");
	            System.out.println();
	        }
	}

}
