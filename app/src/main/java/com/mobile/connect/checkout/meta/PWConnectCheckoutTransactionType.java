package com.mobile.connect.checkout.meta;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The transaction types supported by the checkout overlay. Other types are supported by the native connect library.
 */
public enum PWConnectCheckoutTransactionType implements Parcelable {
	PREAUTHORIZE, DEBIT, TOKENIZATION;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.ordinal());
	}

	public static final Parcelable.Creator<PWConnectCheckoutTransactionType> CREATOR = new Parcelable.Creator<PWConnectCheckoutTransactionType>() {
		public PWConnectCheckoutTransactionType createFromParcel(Parcel in) {
			return PWConnectCheckoutTransactionType.values()[in.readInt()];
		}

		public PWConnectCheckoutTransactionType[] newArray(int size) {
			return new PWConnectCheckoutTransactionType[size];
		}
	};
}
