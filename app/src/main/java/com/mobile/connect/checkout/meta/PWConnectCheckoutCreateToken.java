package com.mobile.connect.checkout.meta;

import android.os.Parcel;
import android.os.Parcelable;

public enum PWConnectCheckoutCreateToken implements Parcelable {
	ALWAYS, NEVER, PROMPT;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.ordinal());
	}

	public static final Parcelable.Creator<PWConnectCheckoutCreateToken> CREATOR = new Parcelable.Creator<PWConnectCheckoutCreateToken>() {
		public PWConnectCheckoutCreateToken createFromParcel(Parcel in) {
			return PWConnectCheckoutCreateToken.values()[in.readInt()];
		}

		public PWConnectCheckoutCreateToken[] newArray(int size) {
			return new PWConnectCheckoutCreateToken[size];
		}
	};
}
