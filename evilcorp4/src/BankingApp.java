import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class BankingApp {

	public static void main(String[] args) throws SQLException, ParseException {

		Scanner keyboard = new Scanner(System.in);
		String accountNum, accountName, response, id, customerName, accountType;
		double balance, amount, withdraw, deposit, accountBalance, transfer, balance2;
		Date date = new Date();
		Date customerBirth = new Date();

		Account myAccount = new Account();
		AccountHolder holder = new AccountHolder();

		System.out.println("Welcome to Evil Corp Savings and Loan");
		System.out.println("=====================================");
		System.out.println("\n\n");

		// list options

		response = Validator
				.getString(
						keyboard,
						"What you want to do:\n 1.Add accounts.\n 2.Remove accounts."
								+ "\n 3.Close accounts.\n 4. Withdraw. \n 5.Deposit \n 6.Check."
								+ "\n 7. Print account history. \n 8. Transfer between Accounts."
								+ "\n 9. Quit.");
		System.out.println("\n");
		while (!response.equals("9")) {

			//===================================================================
			// 								ADD ACCOUNT
			//===================================================================
			if (response.equals("1")) {

				id = Validator.getString(keyboard,
						"Enter your personal ID in file : ");
				holder.setid(id);
				ResultSet rs = holder.searchAccountHolder();
				if (!rs.next()) {
					customerName = Validator.getString(keyboard,
							"Enter your name: ");
					holder.setCustomer_name(customerName);
					customerBirth = Validator.getDate(keyboard,
							"Enter your Date of Birth: ");
					DateFormat shortDf = DateFormat
							.getDateInstance(DateFormat.SHORT);
					String dateStr = shortDf.format(customerBirth);
					holder.setCustomer_birth(dateStr);
					if (holder.addNewAccountHolder()) {
						System.out
								.println("Account Holder Created Successfully.");

						accountNum = Validator.getString(keyboard,
								"Enter new account # :  ");
						myAccount.setAccountNum(accountNum);

						accountName = Validator.getString(keyboard,
								"Enter the name for acct #" + accountNum);
						myAccount.setAccountName(accountName);

						accountType = Validator.getString(keyboard,
								"Enter Account Type");

						while (!accountType.equalsIgnoreCase("checking")
								|| !accountType.equalsIgnoreCase("savings")) {
							System.out.println("Invalid account type.");
							accountType = Validator.getString(keyboard,
									"Enter Account Type");
						}

						if (accountType.equalsIgnoreCase("checking"))
							myAccount.setType(1);
						else if (accountType.equalsIgnoreCase("savings"))
							myAccount.setType(2);

						accountBalance = Validator.getDouble(keyboard,
								"Enter account starting balance: ");
						myAccount.setBalance(accountBalance);

						if (myAccount.addNewAccount()) {

							System.out.println("Account Created Successfully.");
						} else {
							System.out
									.println("An error occurred. Please try again later.");
						}
					}

				} else {
					holder.setCustomer_id(rs.getInt("customer_id"));

					accountNum = Validator.getString(keyboard,
							"Enter new account # :  ");
					myAccount.setAccountNum(accountNum);

					ResultSet result = myAccount.getAccount();

					if (!result.next()) {
						// account does not exists; we can add a new account

						accountName = Validator.getString(keyboard,
								"Enter the name for acct #" + accountNum);
						myAccount.setAccountName(accountName);
						accountType = Validator.getString(keyboard,
								"Enter Account Type");

						while (!accountType.equalsIgnoreCase("checking")
								|| !accountType.equalsIgnoreCase("savings")) {
							System.out.println("Invalid account type.");
							accountType = Validator.getString(keyboard,
									"Enter Account Type");
						}

						int type = myAccount.getAccountType(accountType);
						result = holder.getAccountTypeByAccountHolderID(type);
						if (!result.next()) {
							myAccount.setType(type);
							accountBalance = Validator.getDouble(keyboard,
									"Enter account starting balance: ");
							myAccount.setBalance(accountBalance);
							if (myAccount.addNewAccount()) {

								System.out
										.println("Account Created Successfully.");
							} else {
								System.out
										.println("An error occurred. Please try again later.");
							}
						} else {
							System.out.println("An account of type "
									+ accountType + " already exists.");
						}

					} else {
						// account already exists
						System.out.println("Account already exists. ");
					}
					myAccount.closeConnection();
					// remove method
				}
				
				//===================================================================
				// 							REMOVE ACCOUNT
				//===================================================================
			} else if (response.equals("2")) {

				accountNum = Validator.getString(keyboard,
						"Give the account# you want to remove : ");
				myAccount.setAccountNum(accountNum);
				ResultSet result = myAccount.getAccount();
				if (result.next()) {
					myAccount.setaccountID(result.getInt("Account_ID"));
					myAccount.removeAccount();
					System.out.println("Account found and removed ");
				} else {
					System.out.println("Account not found.");
				}
				myAccount.closeConnection();
			}

			//===================================================================
			// 								CLOSE ACCOUNT
			//===================================================================
			else if (response.equals("3")) {
				accountNum = Validator.getString(keyboard,
						"Give the account# you want to remove : ");
				myAccount.setAccountNum(accountNum);
				ResultSet result = myAccount.getAccount();

				if (result.next()) {
					// need code to calculate balance from transaction table
					myAccount.colseAccount();

					System.out.println("Account closed ");
				} else {
					System.out.println("Account not found.");
				}
				myAccount.closeConnection();
			}

			//===================================================================
			// 						WITHDRAWAL FROM AN ACCOUNT
			//===================================================================
			else if (response.equals("4")) {

				accountNum = Validator
						.getString(keyboard,
								"Enter an account # : ");
				myAccount.setAccountNum(accountNum);
				ResultSet result = myAccount.getAccount();

				if (result.next()) {

					if (result.getInt("type") == 1) {
						
						myAccount.setaccountID(result.getInt("Account_ID"));
						myAccount.setBalance(result.getDouble("Balance"));
						balance = myAccount.getBalance();
						System.out
								.println("You current balance is: " + balance);
						withdraw = Validator.getDouble(keyboard,
								"How much you want to withdraw?");
						if(myAccount.checkOverdraft(withdraw)){
							System.out.println("there will be an overdraft.");
						}
						if (myAccount.withdrawalTransaction(withdraw)) {
							System.out.println("Here is your money.");
							System.out.println("Your new balance is: "
									+ myAccount.getBalance());
						} else {
							System.out.println("Run into deficit.");
						}
					} else {
						System.out.println("You account already closed.");
					}
				} else {
					System.out.println("Account not found ");
				}
				myAccount.closeConnection();
			}

			//===================================================================
			// 							DEPOSIT TO AN ACCOUNT
			//===================================================================
			else if (response.equals("5")) {
				accountNum = Validator
						.getString(keyboard,
								"Enter an account #: ");
				myAccount.setAccountNum(accountNum);

				ResultSet result = myAccount.getAccount();
				if (result.next()) {

					if (result.getInt("status") == 1) {
						myAccount.setaccountID(result.getInt("Account_ID"));
						balance = myAccount.getCurrentBalance();
						System.out
								.println("You current balance is: " + balance);
						deposit = Validator.getDouble(keyboard,
								"How much you want to deposit?");
						myAccount.depositTranscation(deposit);
						System.out.println("You current balance is: "
								+ (balance + deposit));
					} else {
						System.out.println("You account already closed.");
					}
				} else {
					System.out.println("Account not found ");
				}
				myAccount.closeConnection();
			}

			//===================================================================
			// 						ADD CHECK TO AN ACCOUNT
			//===================================================================
			else if (response.equals("6")) {
				accountNum = Validator
						.getString(keyboard,
								"Enter an account # : ");
				myAccount.setAccountNum(accountNum);

				ResultSet result = myAccount.getAccount();
				if (result.next()) {

					if (result.getInt("status") == 1) {
						myAccount.setaccountID(result.getInt("Account_ID"));
						balance = myAccount.getCurrentBalance();
						System.out
								.println("You current balance is: " + balance);

						amount = Validator.getDouble(keyboard,
								"Enter the amount of the check: ");
						date = Validator.getDate(keyboard,
								"Enter the date of the check:");
						myAccount.checkTranscation(amount, date);
						System.out.println("You current balance is: "
								+ (balance - amount));
					} else {
						System.out.println("You account already closed.");
					}
				} else {
					System.out.println("Account not found ");
				}
				myAccount.closeConnection();
			}
			//===================================================================
			// 						PRINT ACCOUNT HISTORY
			//===================================================================
			else if (response.equals("7")) {
				accountNum = Validator
						.getString(keyboard,
								"Enter an account # :  ");
				myAccount.setAccountNum(accountNum);

				ResultSet result = myAccount.getAccount();
				if (result.next()) {
					myAccount.setAccountName(result.getString("Account_Name"));

					if (result.getInt("status") == 1) {
						myAccount.setaccountID(result.getInt("Account_ID"));
						System.out.println(myAccount.getAccountID());
						System.out.println("Account Number: "
								+ myAccount.getAccountNum());
						System.out.println("Account Name: "
								+ myAccount.getAccountName());
						System.out.println(myAccount.getAccountTransaction());
						System.out.println("Account Balance: "
								+ myAccount.getCurrentBalance());
					} else {
						System.out.println("Account is closed.");
					}
				} else {
					System.out.println("Account not found.");
					
				}
				
				myAccount.closeConnection();
				
				//===================================================================
				// 					TRANSFER FROM AN ACCOUNT
				//===================================================================
			} else if (response.equals("8")) {
				id = Validator.getString(keyboard,
						"Enter your personal ID in file : ");
				holder.setid(id);
				ResultSet rs = holder.searchAccountHolder();
				if (rs.next()) {
					holder.setCustomer_id(rs.getInt("customer_id"));
					ResultSet accountResult = holder
							.getAccountByAccountHolderID();
					ArrayList<Account> accountLists = new ArrayList<Account>();

					while (accountResult.next()) {
						myAccount.setAccountNum(accountResult
								.getString("Account_Num"));
						myAccount
								.setBalance(accountResult.getDouble("Balance"));
						myAccount.setType(accountResult.getInt("type"));

						accountLists.add(myAccount);
					}

					if (accountLists.size() < 2) {
						System.out
								.println("Not enough accounts. Cannot transfer.");
					} else {

						for (int i = 0; i < accountLists.size(); i++) {
							System.out.println((i + 1) + "- Account#"
									+ accountLists.get(i).getAccountNum());
							System.out.println("Account Type: "
									+ accountLists.get(i)
											.getAccountDescription());
							System.out.println("Account Balance: "
									+ accountLists.get(i).getBalance());
						}
						int choice = Validator
								.getInt(keyboard,
										"Which account type to transfer from? (1 or 2)");
						while (choice != 1 || choice != 2) {
							System.out.println("Invalid choice. Enter 1 or 2.");
							choice = Validator.getInt(keyboard,
									"Which account type to transfer from?");
						}
						transfer = Validator.getDouble(keyboard,
								"How much you want to transfer?");
						if (accountLists.get(choice - 1).getBalance() >= transfer) {
							balance = accountLists.get(choice - 1).getBalance()
									- transfer;
							accountLists.get(choice - 1).setBalance(balance);
							balance2 = accountLists.get(choice).getBalance()
									+ transfer;
							accountLists.get(choice).setBalance(balance2);
							for (int i = 0; i < accountLists.size(); i++) {
								System.out.println("Account#"
										+ accountLists.get(i).getAccountNum());
								System.out.println("Account Type: "
										+ accountLists.get(i)
												.getAccountDescription());
								System.out.println("Account Balance: "
										+ accountLists.get(i).getBalance());
							}
						} else {
							System.out.println("Insufficient Amount");
						}
					}

				} 
				else {
					System.out
							.println("Cusotmer does not exist. Please create an account first.");
				}

			}

			else {
				System.out.println("\n Out of options range ");
			}

			System.out.println("\n\n");
			response = Validator
					.getString(
							keyboard,
							"What you want to do:\n 1.Add accounts.\n 2.Remove accounts."
									+ "\n 3.Close accounts.\n 4. Withdraw. \n 5.Deposit \n 6.Check."
									+ "\n 7. Print account history. \n 8. Transfer between Accounts."
									+ "\n 9. Quit.");
		}

	}

}
