package study;

import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

	// First name of the user
	private String firstName;

	// Last name of the user
	private String lastName;

	// ID number of the user
	private String uuid;

	// MD5 hash(데이터를 128비트 해시값으로 변환하는 함수) of the user's pin number
	private byte pinHash[];

	// List of accounts for this user
	private ArrayList<Account> accounts;
	
	/**
	 * Create a new user
	 * @param firstName the user's first name
	 * @param lastName  the user's last name
	 * @param pin       the user's account pin number
	 * @param theBank   the Bank object that the user is a customer of
	 */
	public User(String firstName, String lastName, String pin, Bank theBank) {
		
		// set user's name
		this.firstName = firstName;
		this.lastName = lastName;
		
		// 보안 상의 이유로 원래 값 대신에 핀의 MD5 해시값 저장
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			this.pinHash = md.digest(pin.getBytes());
		} catch (NoSuchAlgorithmException e) { // 예외 처리
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		
		// get a new, unique universal ID for the user
		this.uuid = theBank.getNewUserUUID();
		
		//create empty list of accounts
		this.accounts = new ArrayList<Account>();
		
		//print log message
		System.out.printf("안녕하세요, %s%s님 계정이 등록되었습니다. ID: %s \n", firstName, lastName, this.uuid);
	}
	
	/**
	 * Add an account for the user
	 * @param anAcct	the account to add
	 */
	public void addAccount(Account anAcct) {
		this.accounts.add(anAcct);
	}
	
	/**
	 * Return the user's UUID
	 * @return	the uuid
	 */
	public String getUUID() {
		return this.uuid;
	}
	
	/**
	 * Check whether a given pin matched the true User pin
	 * @param aPin	the pin to check
	 * @return		whether the pin is valid or not
	 */
	public boolean validatePin(String aPin) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return MessageDigest.isEqual(md.digest(aPin.getBytes()), pinHash);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		
		return false;
	}
	
	/**
	 * Return the user's first name;
	 * @return	the first name
	 */
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	 * Return the user's last name;
	 * @return	the last name
	 */
	public String getLastName() {
		return this.lastName;
	}
	
	public void printAccountSummary() {
		
		System.out.printf("\n\n%s%s님의 계좌 목록\n", this.firstName, this.lastName);
		for(int a =0; a < this.accounts.size(); a++) {
			System.out.printf("%d) %s\n", a+1, this.accounts.get(a).getSummaryLine());
		}
	}
	
	/**
	 * Get the number of accounts of the user
	 * @return	the number of accounts
	 */
	public int numAccounts() {
		return this.accounts.size();
	}
	
	/**
	 * print transaction history for a particular account
	 * @param acctIdx	the index of the account to use
	 */
	public void printAcctTransHistory(int acctIdx) {
		this.accounts.get(acctIdx).printTransHistory();
	}
	
	/**
	 * Get the balance of a particular account
	 * @param acctIdx	the index of the account to use
	 * @return			the balance of the account
	 */
	public int getAcctBalance(int acctIdx) {
		return this.accounts.get(acctIdx).getBalance();
	}
	
	/**
	 * Get the UUID of a particular account
	 * @param acctIdx	the index of the account to use
	 * @return			the UUID of the account
	 */
	public String getAcctUUID(int acctIdx) {
		return this.accounts.get(acctIdx).getUUID();
	}
	
	public void addAcctTransaction(int acctIdx, int amount, String memo) {
		this.accounts.get(acctIdx).addTransaction(amount,memo);
		
	}
}
