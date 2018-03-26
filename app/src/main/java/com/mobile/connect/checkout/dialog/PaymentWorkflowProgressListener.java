package com.mobile.connect.checkout.dialog;

import com.mobile.connect.checkout.meta.PWConnectCheckoutPaymentMethod;
import com.mobile.connect.payment.PWAccount;

/**
 * Interface providing feedback functionality between the activity and the currently active fragment.
 * 
 */
public interface PaymentWorkflowProgressListener {

	/**
	 * Called by a fragment, if the complete process should be canceled.
	 */
	public void paymentProcessCanceled();

	/**
	 * Called by the choose payment method fragment to indicate a selection.
	 * @param paymentMethod	the selected payment method
	 */
	public void paymentMethodProvided(PWConnectCheckoutPaymentMethod paymentMethod);
	
	/**
	 * Called by the choose payment method fragment to indicate that an account has been selected
	 * @param account the selected account
	 */
	public void accountProvided(PWAccount account);

	/**
	 * Called by the insert payment data fragment to request to change the payment method
	 */
	public void requestToChangePaymentMethod();

	/**
	 * Called by the insert cc payment data fragment to indicate that data is ready.
	 * @param name	the name on the cc
	 * @param ccNumber	the cc number
	 * @param expiryMonth	the cc expiration month
	 * @param expiryYear	the cc expiration year
	 * @param cvv	the cc security code
	 */
	public void paymentCreditCardDataProvided(String name, String ccNumber, String expiryMonth, String expiryYear, String cvv);

	/**
	 * Called by the insert debit payment data fragment to indicate that data is ready.
	 * @param name	the name of the account holder
	 * @param accountNumber	the bank account number
	 * @param bankNumber	the bank identifier/number
	 * @param bankCountry	the country in which the bank is located
	 */
	public void paymentDirectDebitDataProvided(String name, String accountNumber, String bankNumber, String bankCountry);

	/**
	 * Called by the review payment data fragment to request to change the payment data.
	 */
	public void requestToChangePaymentData();

	/**
	 * Called by the review payment data fragment to indicate that the user has reviewed and accepted the payment data.
	 * @param didChooseStore	If the user actively chose to store payment details.
	 */
	public void paymentDataAccepted(boolean didChooseStore);

	/**
	 * The user reviewed the account and accepted it.
	 */
	public void accountAccepted();


}
