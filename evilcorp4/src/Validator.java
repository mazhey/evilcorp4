import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Validator {

	public static String getString(Scanner sc, String prompt) {
		System.out.print(prompt);
		String name = sc.next(); // read user entry
		sc.nextLine(); // discard any other data entered on the line
		return name;
	}

	public static double getDouble(Scanner sc, String prompt) {
		double d = 0;
		boolean isValid = false;
		while (isValid == false) {
			System.out.print(prompt);
			try {
				d = sc.nextDouble();
				isValid = true;
			} catch (InputMismatchException e) {
				System.out
						.println("Incorrect input. Please enter a valid number.");
			}

			sc.nextLine(); // discard any other data entered on the line
		}
		return d;
	}

	public static Date getDate(Scanner sc, String prompt) {

		Date date = new Date();
		
		boolean isValid = false;
		while (isValid == false) {
			System.out.println(prompt);

			try {
				String dateStr = sc.next();

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

				date = formatter.parse(dateStr);
				isValid = true;
			} catch (ParseException e) {
				// e.printStackTrace();
				System.out.println("Please enter a date (mm/dd/yyyy).");
				System.out.println();
			}

			sc.nextLine(); // discard any other data entered on the line
		}

		return date;

	}
	
	public static boolean validateTransactionType(String type){
		boolean isValid = false;
		switch(type){
		case "check": isValid = true;
		case "debit card": isValid = true;
		case "deposit": isValid = true;
		case "withdrawal": isValid = true;
		}
		return isValid;	
	}
}