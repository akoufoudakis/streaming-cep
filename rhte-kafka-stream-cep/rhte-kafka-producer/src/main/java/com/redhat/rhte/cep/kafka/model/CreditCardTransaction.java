package com.redhat.rhte.cep.kafka.model;

import java.util.Date;

public class CreditCardTransaction {
	private String creditCardId;
	private String itemPurchased;
	private double amount;
	private Date purchaseDate;
	private String zipCode;
	private String countryOfTransaction;
	public String getCountryOfTransaction() {
		return countryOfTransaction;
	}

	public void setCountryOfTransaction(String countryOfTransaction) {
		this.countryOfTransaction = countryOfTransaction;
	}

	private String storeId;

	public CreditCardTransaction() {
	}

	public CreditCardTransaction(String creditCardId, String itemPurchased, double amount, Date purchaseDate,
			String countryOfTransaction,String zipCode, String storeId) {

		this.creditCardId = creditCardId;
		this.itemPurchased = itemPurchased;
		this.amount = amount;
		this.purchaseDate = purchaseDate;
		this.countryOfTransaction = countryOfTransaction;
		this.zipCode = zipCode;
		this.storeId = storeId;
	}

	public CreditCardTransaction(CreditCardTransaction copy) {

		this.creditCardId = copy.creditCardId;
		this.itemPurchased = copy.itemPurchased;
		this.amount = copy.amount;
		this.purchaseDate = copy.purchaseDate;
		this.zipCode = copy.zipCode;
		this.storeId = copy.storeId;
	}

	public String getCreditCardId() {
		return creditCardId;
	}

	public String getItemPurchased() {
		return itemPurchased;
	}

	public double getAmount() {
		return amount;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getStoreId() {
		return storeId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof CreditCardTransaction))
			return false;

		CreditCardTransaction creditCardTrans = (CreditCardTransaction) o;

		if (amount != creditCardTrans.amount)
			return false;
		if (Double.compare(creditCardTrans.amount, amount) != 0)
			return false;
		if (creditCardId != null ? !creditCardId.equals(creditCardTrans.creditCardId)
				: creditCardTrans.creditCardId != null)
			return false;
		if (itemPurchased != null ? !itemPurchased.equals(creditCardTrans.itemPurchased)
				: creditCardTrans.itemPurchased != null)
			return false;
		if (countryOfTransaction != null ? !countryOfTransaction.equals(creditCardTrans.countryOfTransaction) : creditCardTrans.countryOfTransaction != null)
			return false;
		if (zipCode != null ? !zipCode.equals(creditCardTrans.zipCode) : creditCardTrans.zipCode != null)
			return false;
		return storeId != null ? storeId.equals(creditCardTrans.storeId) : creditCardTrans.storeId == null;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = creditCardId != null ? creditCardId.hashCode() : 0;
		result = 31 * result + (itemPurchased != null ? itemPurchased.hashCode() : 0);
		temp = Double.doubleToLongBits(amount);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (countryOfTransaction != null ? countryOfTransaction.hashCode() : 0);
		result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
		result = 31 * result + (storeId != null ? storeId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "CreditCardTransaction{creditCardId='" + creditCardId + '\'' + ", itemPurchased='" + itemPurchased + '\''
				+ ", amount=" + amount + ", purchaseDate=" + purchaseDate+ ", countryOfTransaction='" + countryOfTransaction  + ", zipCode='" + zipCode + '\''
				+ ", storeId='" + storeId + '\'' + '}';
	}
}
