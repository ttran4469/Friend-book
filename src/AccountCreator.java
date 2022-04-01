import java.util.Scanner;

public class AccountCreator {
	private DataStorage data;
	public AccountCreator(DataStorage d) {
		data = d;
	}
	public void register() {
		String loginID = "";
		String password = "";
		String name = "";
		String school = "";
		
		Scanner input = new Scanner(System.in);
		boolean loginReqLen = false;
		boolean loginReqLetter = false;
		boolean loginReqDigit = false;
		boolean loginReqSpecial = false;
		
		while (!loginReqLen || !loginReqLetter || !loginReqDigit || !loginReqSpecial) {
			System.out.println("Please enter your new login ID for registration:");
			loginID = input.next(); //loginID no space so we use next()
			if (loginID.length()>=3 && loginID.length()<=10) {
				loginReqLen = true;
			}
			for (int i=0; i<loginID.length(); i++) {
				char c = loginID.charAt(i); // get each character in the string
				if (c=='#' || c=='?' || c=='!' || c=='*') {
					loginReqSpecial = true;
				}
				int ascii = (int)c; //convert character into ascii code, then google to see the range of digits and letters
				if (ascii>=48 && ascii<=57)	{
					loginReqDigit = true;
				}
			
				if ((ascii>=65 && ascii<=90)||(ascii>=97 && ascii<=122))	{
					loginReqLetter = true;
				}
			}
		}
		
//		boolean validPw = false;
		while(true) {
			System.out.println("Please enter your password:");
			password = input.next();
			if (!password.equals(loginID)) {
//				validPw = true;
				break;
			}
			else {
				System.out.println("Password cannot be the same as login ID.");
			}
		}
		
		Scanner input1 = new Scanner(System.in);
		System.out.println("Please enter your name:");
		name = input1.nextLine();
		
		Scanner input2 = new Scanner(System.in);
		System.out.println("Please enter your school:");
		school = input2.nextLine();
		
		data.createProfile(loginID, password, name, school);
	}
}
