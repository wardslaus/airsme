package com.mobile.connect.checkout.meta;

import android.os.Parcel;
import android.os.Parcelable;

import com.mobile.connect.payment.credit.PWCreditCardType;

import com.airsme.R;

/**
 * Payment methods supported by the checkout overlay.
 */
public enum PWConnectCheckoutPaymentMethod implements Parcelable {

	MASTERCARD, VISA, AMERICAN_EXPRESS, DINERS, DIRECT_DEBIT;

	/**
	 * Returns the respective {@link PWCreditCardType} for the payment method.
	 * 
	 * @return The credit card type
	 */
	public PWCreditCardType getCreditCardTypeMapping() {
		switch (this) {
		case MASTERCARD:
			return PWCreditCardType.MASTERCARD;
		case VISA:
			return PWCreditCardType.VISA;
		case AMERICAN_EXPRESS:
			return PWCreditCardType.AMEX;
		case DINERS:
			return PWCreditCardType.DINERS;
		default:
			return null;
		}
	}

	public static PWConnectCheckoutPaymentMethod fromCreditCardType(PWCreditCardType creditCardType) {
		switch (creditCardType) {
		case AMEX:
			return AMERICAN_EXPRESS;
		case DINERS:
			return DINERS;
		case MASTERCARD:
			return MASTERCARD;
		case VISA:
			return VISA;
		default: {
			return null;
		}
		}
	}

	/**
	 * Returns the icon resource for the payment method.
	 * 
	 * @return The icon resource
	 */
	public int getIconMapping() {
		switch (this) {
		case MASTERCARD:
			return R.drawable.connect_checkout_payment_mastercard;
		case VISA:
			return R.drawable.connect_checkout_payment_visa;
		case AMERICAN_EXPRESS:
			return R.drawable.connect_checkout_payment_amex;
		case DINERS:
			return R.drawable.connect_checkout_payment_diners;
		default:
			return 0x00;
		}

	}

	/**
	 * Returns the localized string mapping for the payment method.
	 * 
	 * @return The string mapping (based on locale)
	 */
	public int getStringMapping() {
		switch (this) {
		case MASTERCARD:
			return R.string.connect_checkout_payment_mastercard;
		case VISA:
			return R.string.connect_checkout_payment_visa;
		case AMERICAN_EXPRESS:
			return R.string.connect_checkout_payment_amex;
		case DINERS:
			return R.string.connect_checkout_payment_diners;
		case DIRECT_DEBIT:
			return R.string.connect_checkout_payment_direct_debit;
		default:
			return 0x00;
		}

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.ordinal());
	}

	public static final Parcelable.Creator<PWConnectCheckoutPaymentMethod> CREATOR = new Parcelable.Creator<PWConnectCheckoutPaymentMethod>() {
		public PWConnectCheckoutPaymentMethod createFromParcel(Parcel in) {
			return PWConnectCheckoutPaymentMethod.values()[in.readInt()];
		}

		public PWConnectCheckoutPaymentMethod[] newArray(int size) {
			return new PWConnectCheckoutPaymentMethod[size];
		}
	};

}
