package study;

import java.util.Date;

public class Transaction {

	// Amount of this transaction
	private int amount;
	
	// Time and date of this transaction
	private Date timestamp;
	
	// Memo for this transaction
	private String memo;
	
	// Account in which the transaction was performed
	private Account inAccount;
	
	/**
	 * Create a new transaction
	 * @param amount	the amount transacted
	 * @param inAccount	the account the transaction belongs to
	 */
	public Transaction(int amount, Account inAccount) {
		
		this.amount = amount;
		this.inAccount = inAccount;
		this.timestamp = new Date();
		this.memo = "";
	}
	
	/**
	 * Create a new transaction
	 * @param amount	the amount transacted
	 * @param memo		the memo for the transaction
	 * @param inAccount	the account the transaction belongs to
	 */
	public Transaction(int amount, String memo, Account inAccount) {
		
		// call the two-arg constructor first
		this(amount, inAccount);
		
		// set the memo
		this.memo = memo;
	}
	
	/**
	 * Get the amount of the transaction
	 * @return	the amount
	 */
	public int getAmount() {
		return this.amount;
	}
	
	/**
	 * Get a string summarizing the transaction
	 * @return	the summary string
	 */
	public String getSummaryLine() {
		
		if(this.amount >= 0) {
			return String.format("%s : %d원 : %s", this.timestamp.toString(), this.amount, this.memo);
		} else {
			return String.format("%s : -%d원 : %s", this.timestamp.toString(), -this.amount, this.memo);
		}
	}
}
