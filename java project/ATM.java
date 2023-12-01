package study;

import java.util.Scanner;
import java.io.Console;

public class ATM {

	public static void main(String[] args) {
		
		// initialize scanner
		Scanner sc = new Scanner(System.in);
		
		// initialize bank
		Bank theBank = new Bank("신한은행");
		
		// add a user, which also creates a savings account
		User aUser = theBank.addUser("채", "희주", "1234");
		
		// add a checking account for our user
		Account newAccount = new Account("입출금통장", aUser, theBank);
		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		User curUser;
		while(true) {
			
			// stay in the login prompt until successful login
			curUser = ATM.mainMenuPrompt(theBank, sc);
			
			// stay in main menu until user quits
			ATM.printUserMenu(curUser, sc);
		}
	}
	
	/**
	 * print the ATM's login menu
	 * @param theBank	the Bank object whose accounts to use
	 * @param sc		the Scanner object to use for user input
	 * @return			the authenticated User object
	 */
	public static User mainMenuPrompt(Bank theBank, Scanner sc) {
		
		// initialize
		String userID;
		char[] pin;
		User authUser;
		
		// prompt the user for user ID/pin combo until a correct one is reached
		do{
			System.out.printf("\n\n%s에 오신 것을 환영합니다.\n", theBank.getName());
	        System.out.print("ID: ");
	        userID = sc.nextLine();

	        Console console = System.console();
	        if (console != null) {
	            pin = console.readPassword("pin: ");
	        } else {
	            // If running in an environment without console, fallback to regular input
	            System.out.print("pin: ");
	            pin = sc.nextLine().toCharArray();
	        }

	        // try to get the user object corresponding to the ID and pin combo
	        authUser = theBank.userLongin(userID, new String(pin));
	        if (authUser == null) {
	            System.out.println("ID와 pin번호가 일치하지 않습니다. 다시 입력하시기 바랍니다.");
	        }

	        // Clear the PIN from memory for security
	        java.util.Arrays.fill(pin, ' ');

		}while(authUser == null); // continue looping until successful login
		
		return authUser;
	}
	
	public static void printUserMenu(User theUser, Scanner sc) {
		
		// print a summary of the User's accounts
		theUser.printAccountSummary();
		
		//initialize
		int choice;
		
		// user menu
		do{
			System.out.printf("\n%s%s님 환영합니다, 무엇을 도와드릴까요?\n", theUser.getFirstName(),theUser.getLastName());
			System.out.println("  1) 거래내역 조회");
			System.out.println("  2) 출금");
			System.out.println("  3) 입금");
			System.out.println("  4) 계좌이체");
			System.out.println("  5) 종료");
			System.out.println();
			System.out.println("선택: ");
			choice = sc.nextInt();
			
			if(choice < 1 || choice > 5) {
				System.out.println("1~5번 중 골라 주시기 바랍니다.");
			}
		} while(choice < 1 || choice > 5);
		
		//process the choice
		
		switch(choice) {
		
		case 1:
			ATM.showTransHistory(theUser, sc);
			break;
		case 2:
			ATM.withdrawFunds(theUser, sc);
			break;
		case 3:
			ATM.depositFunds(theUser, sc);
			break;
		case 4:
			ATM.transferFunds(theUser, sc);
			break;
		case 5:
			// gobble up rest of previous input
			sc.nextLine();
			break;
		}
		
		// redisplay this menu unless the user wants to quit
		if(choice != 5) {
			ATM.printUserMenu(theUser, sc);
		}
	}
	
	/**
	 * Show the transaction history for an account
	 * @param theUser	the logged-in User object
	 * @param sc		the Scanner object used for user input
	 */
	public static void showTransHistory(User theUser, Scanner sc) {
		
		int theAcct;
		
		// get account whose transaction history to look at
		do{
			System.out.printf("거래내역 조회를 희망하는 계좌를 고르세요. (1~%d)", theUser.numAccounts());
			theAcct = sc.nextInt()-1;
			if(theAcct < 0 || theAcct >= theUser.numAccounts()) {
				System.out.println("다시 입력하시기 바랍니다.");
			}
		}while(theAcct <0 || theAcct >= theUser.numAccounts());
		
		//print the transaction history
		theUser.printAcctTransHistory(theAcct);
		
	}
	
	/**
	 * Process transferring funds from on account to another
	 * @param theUser	the logged-in User object
	 * @param sc		the Scanner object used for user input
	 */
	public static void transferFunds(User theUser, Scanner sc) {
		
		// initialize
		int fromAcct;
		int toAcct;
		int amount;
		int acctBal;
		
		//get the account to transfer from
		do {
			System.out.printf("이체할 계좌를 고르세요. (1~%d)", theUser.numAccounts());
			fromAcct = sc.nextInt()-1;
			if(fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("다시 입력하시기 바랍니다.");
			}
		}while(fromAcct <0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);
		
		//get the account to transfer to
		do {
			System.out.printf("이체받을 계좌를 고르세요. (1~%d)", theUser.numAccounts());
			toAcct = sc.nextInt()-1;
			if(toAcct < 0 || toAcct >= theUser.numAccounts()) {
				System.out.println("다시 입력하시기 바랍니다.");
			}
		}while(toAcct <0 || toAcct >= theUser.numAccounts());
		
		// get the amount to transfer
		do {
			System.out.printf("이체할 금액을 입력하세요. (잔액: %d원): ", acctBal);
			amount = sc.nextInt();
			if(amount <0) {
				System.out.println("다시 입력하시기 바랍니다.");
			} else if(amount > acctBal) {
				System.out.printf("현재 잔액은 %d원 입니다.\n", acctBal);
			}
		}while(amount < 0 || amount > acctBal);
		
		// finally, do the transfer
		theUser.addAcctTransaction(fromAcct, -1*amount, String.format("출금(%s계좌)", theUser.getAcctUUID(toAcct)));
		theUser.addAcctTransaction(toAcct, amount, String.format("입금(%s계좌)", theUser.getAcctUUID(fromAcct)));
	}
	
	/**
	 * Process a fund withdraw from an account
	 * @param theUser	the logged-in User object
	 * @param sc		the Scanner object user for user input
	 */
	public static void withdrawFunds(User theUser, Scanner sc) {
		
		// initialize
		int fromAcct;
		int amount;
		int acctBal;
		String memo;
		
		//get the account to transfer from
		do {
			System.out.printf("출금할 계좌를 고르세요. (1~%d)", theUser.numAccounts());
			fromAcct = sc.nextInt()-1;
			if(fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("다시 입력하시기 바랍니다.");
			}
		}while(fromAcct <0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);
		
		// get the amount to transfer
		do {
			System.out.printf("출금할 금액을 입력하세요. (잔액: %d원): ", acctBal);
			amount = sc.nextInt();
			if(amount <0) {
				System.out.println("다시 입력하시기 바랍니다.");
			} else if(amount > acctBal) {
				System.out.printf("현재 잔액은 %d원 입니다.\n", acctBal);
			}
		}while(amount < 0 || amount > acctBal);
		
		// gobble up rest of previous input
		sc.nextLine();
		
		// get a memo
		System.out.println("메모를 입력하세요.");
		memo = sc.nextLine();
		
		// do the withdraw
		theUser.addAcctTransaction(fromAcct, -1*amount, memo);
	}

	/**
	 * Process a fund deposit to an account
	 * @param theUser	the logged-in User object
	 * @param sc		the Scanner object user for user input
	 */
	public static void depositFunds(User theUser, Scanner sc) {
		// initialize
		int toAcct;
		int amount;
		int acctBal;
		String memo;
		
		//get the account to transfer from
		do {
			System.out.printf("입금할 계좌를 고르세요. (1~%d)", theUser.numAccounts());
			toAcct = sc.nextInt()-1;
			if(toAcct < 0 || toAcct >= theUser.numAccounts()) {
				System.out.println("다시 입력하시기 바랍니다.");
			}
		}while(toAcct <0 || toAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(toAcct);
		
		// get the amount to transfer
		do {
			System.out.printf("입금할 금액을 입력하세요. (잔액: %d원): ", acctBal);
			amount = sc.nextInt();
			if(amount <0) {
				System.out.println("다시 입력하시기 바랍니다.");
			}
		}while(amount < 0);
		
		// gobble up rest of previous input
		sc.nextLine();
		
		// get a memo
		System.out.println("메모를 입력하세요.");
		memo = sc.nextLine();
		
		// do the withdraw
		theUser.addAcctTransaction(toAcct, amount, memo);
	}
}

