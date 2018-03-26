package com.mobile.connect.checkout.meta;

import android.os.Parcel;
import android.os.Parcelable;

import com.mobile.connect.checkout.dialog.PWConnectCheckoutActivity;
import com.mobile.connect.payment.Account;
import com.mobile.connect.payment.PWAccount;
import com.mobile.connect.payment.PWCurrency;
import com.mobile.connect.payment.PWPaymentParams;
import com.mobile.connect.payment.PWPaymentParamsFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.airsme.R;

/**
 * Settings for initializing and setting up the checkout screens.
 */
public class PWConnectCheckoutSettings implements Parcelable {

	private int _headerIconResource;
	private String _headerDescription;

	private String[] _supportedDirectDebitCountries;
	private PWConnectCheckoutPaymentMethod[] _supportedPaymentMethods;
	private PWConnectCheckoutTransactionType _checkoutTransactionType;
	
	private PWConnectCheckoutCreateToken _createToken;

	private double _paymentAmount;
	private PWCurrency _paymentCurrency;
	private String _paymentSubject;
//	private String _customIdentifier;

	private double _paymentVATAmount;

	private List<PWAccount> _storedAccounts;
 	
	private Map<PWConnectCheckoutPaymentMethod, PWConnectCheckoutCreateToken> _createTokenForPaymentMethod = new HashMap<PWConnectCheckoutPaymentMethod, PWConnectCheckoutCreateToken>();
	
	
	/**
	 * Creates a new setting object with default settings and provide them to {@link PWConnectCheckoutActivity} as an extra with key {@link PWConnectCheckoutActivity#CONNECT_CHECKOUT_SETTINGS}.
	 */
	public PWConnectCheckoutSettings() {
		_headerIconResource = 0x00;
		_headerDescription = null;

		_supportedDirectDebitCountries = new String[] {};
		_supportedPaymentMethods = new PWConnectCheckoutPaymentMethod[] {};
		_storedAccounts = new LinkedList<PWAccount>();
		_checkoutTransactionType = PWConnectCheckoutTransactionType.DEBIT;
		
		_createToken = PWConnectCheckoutCreateToken.NEVER;

		_paymentAmount = -1.0;
		_paymentCurrency = null;
		_paymentVATAmount = -1.0;
		_paymentSubject = null;
//		_customIdentifier = null;
	}

	/**
	 * Creates a new settings object with default settings.
	 * @param paymentAmount	The amount of the transaction
	 * @param paymentCurrency	The currency to use
	 * @param paymentSubject The subject of the transaction
	 * @deprecated Instead use {@link PWPaymentParamsFactory#createGenericPaymentParams(double, PWCurrency, String)} to generate generic payment params and add provide them with key {@link PWConnectCheckoutActivity#CONNECT_CHECKOUT_GENERIC_PAYMENT_PARAMS}.
	 */
	public PWConnectCheckoutSettings(double paymentAmount, PWCurrency paymentCurrency, String paymentSubject) {
		this();
		_paymentAmount = paymentAmount;
		_paymentCurrency = paymentCurrency;
		_paymentSubject = paymentSubject;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_headerIconResource);
		dest.writeString(_headerDescription);

		dest.writeStringArray(_supportedDirectDebitCountries);
		dest.writeTypedArray(_supportedPaymentMethods, 0);
		dest.writeParcelable(_checkoutTransactionType, 0);
		
		dest.writeParcelable(_createToken, 0);

		dest.writeDouble(_paymentAmount);
		dest.writeParcelable(_paymentCurrency, 0);
		dest.writeDouble(_paymentVATAmount);
		dest.writeString(_paymentSubject);
		dest.writeTypedArray(_createTokenForPaymentMethod.keySet().toArray(new PWConnectCheckoutPaymentMethod[0]), 0);
		dest.writeTypedArray(_createTokenForPaymentMethod.values().toArray(new PWConnectCheckoutCreateToken[0]), 0);
		
		dest.writeTypedList(_storedAccounts);
	}

	public static final Parcelable.Creator<PWConnectCheckoutSettings> CREATOR = new Parcelable.Creator<PWConnectCheckoutSettings>() {
		public PWConnectCheckoutSettings createFromParcel(Parcel in) {
			return new PWConnectCheckoutSettings(in);
		}

		public PWConnectCheckoutSettings[] newArray(int size) {
			return new PWConnectCheckoutSettings[size];
		}
	};

	private PWConnectCheckoutSettings(Parcel in) {
		_headerIconResource = in.readInt();
		_headerDescription = in.readString();

		_supportedDirectDebitCountries = in.createStringArray();
		_supportedPaymentMethods = in.createTypedArray(PWConnectCheckoutPaymentMethod.CREATOR);
		_checkoutTransactionType = in.readParcelable(PWConnectCheckoutTransactionType.class.getClassLoader());
		
		_createToken = in.readParcelable(PWConnectCheckoutCreateToken.class.getClassLoader());

		_paymentAmount = in.readDouble();
		_paymentCurrency = in.readParcelable(PWCurrency.class.getClassLoader());
		_paymentVATAmount = in.readDouble();
		_paymentSubject = in.readString();
		
		PWConnectCheckoutPaymentMethod[] keys = in.createTypedArray(PWConnectCheckoutPaymentMethod.CREATOR);
		PWConnectCheckoutCreateToken[] values = in.createTypedArray(PWConnectCheckoutCreateToken.CREATOR);
		
		_createTokenForPaymentMethod = new HashMap<PWConnectCheckoutPaymentMethod, PWConnectCheckoutCreateToken>();
		
		for(int i = 0; i < keys.length; i++) {
			_createTokenForPaymentMethod.put(keys[i], values[i]);
		}
		
		_storedAccounts = new LinkedList<PWAccount>();
		ArrayList<Account> accounts = in.createTypedArrayList(Account.CREATOR);
		_storedAccounts.addAll(accounts);
	}

	/**
	 * Gets the icon that is displayed in the header on the left side.
	 * @return The icon to show
	 */
	public int getHeaderIconResource() {
		return _headerIconResource;
	}

	/**
	 * Sets the icon that is displayed in the header on the left side.
	 * @param headerIconResource The icon to show
	 * @return Itself for chained execution
	 */
	public PWConnectCheckoutSettings setHeaderIconResource(int headerIconResource) {
		_headerIconResource = headerIconResource;
		return this;
	}

	/**
	 * Gets the description displayed above the amount.
	 * @return The description
	 */
	public String getHeaderDescription() {
		return _headerDescription;
	}

	/**
	 * Sets the description displayed above the amount.
	 * @param headerDescription The description
	 * @return Itself for chained execution
	 */
	public PWConnectCheckoutSettings setHeaderDescription(String headerDescription) {
		_headerDescription = headerDescription;
		return this;
	}

	/**
	  * Gets the supported direct debit countries.
	 * @return  The array of supported countries
	 * @see PWConnectCheckoutSettings#setSupportedDirectDebitCountries(String[])
	 */
	public String[] getSupportedDirectDebitCountries() {
		return _supportedDirectDebitCountries;
	}

	/**
	 * Sets the supported direct debit countries. The strings within the array must be in the ISO 3166-1 2 char format.
	 * If the array is empty no direct debit option will be shown (regardless of the {@link PWConnectCheckoutSettings#setSupportedPaymentMethods(PWConnectCheckoutPaymentMethod[])}.
	 * @param supportedDirectDebitCountries The array of supported countries (alpha2)
	 * @return Itself for chained execution
	 */
	public PWConnectCheckoutSettings setSupportedDirectDebitCountries(String[] supportedDirectDebitCountries) {
		_supportedDirectDebitCountries = supportedDirectDebitCountries;
		return this;
	}

	/**
	 * Sets the supported payment methods.
	 * @return The array of supported payment methods
	 * @see PWConnectCheckoutSettings#setSupportedPaymentMethods(PWConnectCheckoutPaymentMethod[])
	 */
	public PWConnectCheckoutPaymentMethod[] getSupportedPaymentMethods() {
		return _supportedPaymentMethods;
	}

	/**
	 * Sets the supported payment methods. The order in the array corresponds to the position in the list.
	 * @param supportedPaymentMethods The array of supported payment methods
	 * @return Itself for chained execution
	 */
	public PWConnectCheckoutSettings setSupportedPaymentMethods(PWConnectCheckoutPaymentMethod[] supportedPaymentMethods) {
		_supportedPaymentMethods = supportedPaymentMethods;
		return this;
	}

	/**
	 * Gets the transaction type the checkout overlay should use.
	 * @return The transaction type to use
	 */
	public PWConnectCheckoutTransactionType getCheckoutTransactionType() {
		return _checkoutTransactionType;
	}

	/**
	 * Sets the transaction type the checkout overlay should use. Default is {@code PWConnectCheckoutTransactionType#DEBIT}.
	 * @param checkoutTransactionType The transaction type to use
	 * @return Itself for chained execution
	 */
	public PWConnectCheckoutSettings setCheckoutTransactionType(PWConnectCheckoutTransactionType checkoutTransactionType) {
		_checkoutTransactionType = checkoutTransactionType;
		return this;
	}

	/**
	 * Gets the amount of the upcoming transaction.
	 * @return The transaction amount
	 * @deprecated Instead provide generic {@link PWPaymentParams} as an additional extra, see {@link PWConnectCheckoutSettings#PWConnectCheckoutSettings(double, PWCurrency, String)} for more infos
	 */
	public double getPaymentAmount() {
		return _paymentAmount;
	}

	/**
	 * Sets the amount of the upcoming transaction. Must be > 0.0.
	 * @param paymentAmount The transaction amount
	 * @return Itself for chained execution
	 * @deprecated Instead provide generic {@link PWPaymentParams} as an additional extra, see {@link PWConnectCheckoutSettings#PWConnectCheckoutSettings(double, PWCurrency, String)} for more infos
	 */
	public PWConnectCheckoutSettings setPaymentAmount(double paymentAmount) {
		_paymentAmount = paymentAmount;
		return this;
	}

	/**
	 * Gets the currency of the upcoming transaction.
	 * @return The transaction currency
	 * @deprecated Instead provide generic {@link PWPaymentParams} as an additional extra, see {@link PWConnectCheckoutSettings#PWConnectCheckoutSettings(double, PWCurrency, String)} for more infos
	 */
	public PWCurrency getPaymentCurrency() {
		return _paymentCurrency;
	}

	/**
	 * Sets the currency of the upcoming transaction.
	 * @param paymentCurrency The transaction currency
	 * @return Itself for chained execution
	 * @deprecated Instead provide generic {@link PWPaymentParams} as an additional extra, see {@link PWConnectCheckoutSettings#PWConnectCheckoutSettings(double, PWCurrency, String)} for more infos
	 */
	public PWConnectCheckoutSettings setPaymentCurrency(PWCurrency paymentCurrency) {
		_paymentCurrency = paymentCurrency;
		return this;
	}

	/**
	 * Gets the VAT amount to be displayed below the amount.
	 * @return The VAT amount
	 */
	public double getPaymentVATAmount() {
		return _paymentVATAmount;
	}

	/**
	 * Sets the VAT amount to be displayed below the amount.
	 * @param paymentVATAmount The VAT amount
	 * @return Itself for chained execution
	 */
	public PWConnectCheckoutSettings setPaymentVATAmount(double paymentVATAmount) {
		_paymentVATAmount = paymentVATAmount;
		return this;
	}

	/**
	 * Gets the subject of the upcoming transaction.
	 * @return The transaction subject
	 * @deprecated Instead provide generic {@link PWPaymentParams} as an additional extra, see {@link PWConnectCheckoutSettings#PWConnectCheckoutSettings(double, PWCurrency, String)} for more infos
	 */
	public String getPaymentSubject() {
		return _paymentSubject;
	}

	/**
	 * Sets the subject of the upcoming transaction.
	 * @param paymentSubject The transaction subject
	 * @return Itself for chained execution
	 * @deprecated Instead provide generic {@link PWPaymentParams} as an additional extra, see {@link PWConnectCheckoutSettings#PWConnectCheckoutSettings(double, PWCurrency, String)} for more infos
	 */
	public PWConnectCheckoutSettings setPaymentSubject(String paymentSubject) {
		_paymentSubject = paymentSubject;
		return this;
	}
	
	/**
	 * Sets whether a token should be obtained for this transaction or not.
	 * @param createToken Obtain token for this transaction
	 * @return Itself for chained execution
	 */
	public PWConnectCheckoutSettings setCreateToken(PWConnectCheckoutCreateToken createToken) {
		_createToken = createToken;
		return this;
	}
	
	/**
	 * Gets whether a token should be obtained for this transaction or not.
	 * @return Indicating if a token should be created prior to payment execution 
	 */
	public PWConnectCheckoutCreateToken getCreateToken() {
		return _createToken;
	}
	/**
	 * Set how a token is created for the specific payment method.
	 * Overrides the default.
	 * @param method
	 * @param createToken
	 */
	public void setCreateTokenForPaymentMethod(PWConnectCheckoutPaymentMethod method, PWConnectCheckoutCreateToken createToken) {
		_createTokenForPaymentMethod.put(method, createToken);
	}
	
	/**
	 * Get how a token is created for the specific payment method.
	 * Returns the default behavior if you did not explicitely set a behavior for the given payment method.
	 * @param method
	 * @return
	 */
	public PWConnectCheckoutCreateToken getCreateTokenForPaymentMethod(PWConnectCheckoutPaymentMethod method) {
		if(_createTokenForPaymentMethod.containsKey(method)) {
			return _createTokenForPaymentMethod.get(method);
		} else {
			return getCreateToken();
		}
	}
	
	// TODO document
	public PWConnectCheckoutSettings setStoredAccounts(List<PWAccount> storedAccounts) {
		this._storedAccounts = storedAccounts;
		return this;
	}
	
	public List<PWAccount> getStoredAccounts() {
		return _storedAccounts;
	}
}
