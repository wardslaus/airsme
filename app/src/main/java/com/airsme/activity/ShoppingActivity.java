package com.airsme.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.airsme.R;
import com.mobile.connect.PWConnect.PWProviderMode;
import com.mobile.connect.checkout.dialog.PWConnectCheckoutActivity;
import com.mobile.connect.checkout.meta.PWConnectCheckoutCreateToken;
import com.mobile.connect.checkout.meta.PWConnectCheckoutPaymentMethod;
import com.mobile.connect.checkout.meta.PWConnectCheckoutSettings;
import com.mobile.connect.exception.PWException;
import com.mobile.connect.exception.PWProviderNotInitializedException;
import com.mobile.connect.payment.PWAccount;
import com.mobile.connect.payment.PWCurrency;
import com.mobile.connect.payment.PWPaymentParams;
import com.mobile.connect.service.PWConnectService;
import com.mobile.connect.service.PWProviderBinder;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends Activity {

    public static final String PREFS_NAME = "MCOMMERCE_SAMPLE";
    public static final String ACCOUNTS = "ACCOUNTS";

	private static final String APPLICATIONIDENTIFIER = "payworks.swipeandbuy";
	private static final String PROFILETOKEN = "8a82941861898e3c016189984c690030";


	private PWProviderBinder _binder;
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			_binder = (PWProviderBinder) service;
			try {
				// replace by custom sandbox access
				_binder.initializeProvider(PWProviderMode.LIVE, APPLICATIONIDENTIFIER, PROFILETOKEN);
			} catch (PWException ee) {
				ee.printStackTrace();
			}
			Log.i("mainactivity", "bound to remote service...!");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			_binder = null;
		}
	};

	/**
	 * A list of the stored accounts
	 */
	private List<PWAccount> accounts;

	/**
	 * Reference to the preferences where the accounts are stored
	 */
	private SharedPreferences sharedSettings;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.shoppingactivity);

		// start the PWConnect service
		startService(new Intent(this, PWConnectService.class));
		bindService(new Intent(this, PWConnectService.class), serviceConnection, Context.BIND_AUTO_CREATE);

		findViewById(R.id.payment).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ShoppingActivity.this, PWConnectCheckoutActivity.class);
				PWConnectCheckoutSettings settings = null;
				PWPaymentParams genericParams = null;

				try {
					// configure amount, currency, and subject of the transaction
					genericParams = _binder.getPaymentParamsFactory().createGenericPaymentParams(1, PWCurrency.SOUTH_AFRICA_RAND, "test subject");
					// configure payment params with customer data 
					genericParams.setCustomerGivenName("Aliza");
					genericParams.setCustomerFamilyName("Foo");
					genericParams.setCustomerAddressCity("Sampletown");
					genericParams.setCustomerAddressCountryCode("ZA");
					genericParams.setCustomerAddressState("PA");
					genericParams.setCustomerAddressStreet("123 Grande St");
					genericParams.setCustomerAddressZip("1234");
					genericParams.setCustomerEmail("aliza.foo@foomail.com");
					genericParams.setCustomerIP("255.0.255.0");
					genericParams.setCustomIdentifier("myCustomIdentifier");
					

					// create the settings for the payment screens
					settings = new PWConnectCheckoutSettings();
					settings.setHeaderDescription("mobile.connect");
					//settings.setHeaderIconResource(R.drawable.icon);
					settings.setPaymentVATAmount(4.5);
					settings.setSupportedDirectDebitCountries(new String[] { "ZA" });
					settings.setSupportedPaymentMethods(new PWConnectCheckoutPaymentMethod[] { PWConnectCheckoutPaymentMethod.VISA, PWConnectCheckoutPaymentMethod.MASTERCARD,
							PWConnectCheckoutPaymentMethod.DIRECT_DEBIT });
					// ask the user if she wants to store the account
					settings.setCreateToken(PWConnectCheckoutCreateToken.PROMPT);

					// retrieve the stored accounts from the settings
					accounts = _binder.getAccountFactory().deserializeAccountList(sharedSettings.getString(ACCOUNTS, _binder.getAccountFactory().serializeAccountList(new ArrayList<PWAccount>())));
					settings.setPaymentAmount(66);



					i.putExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_SETTINGS, settings);
					i.putExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_GENERIC_PAYMENT_PARAMS, genericParams);
					startActivityForResult(i, PWConnectCheckoutActivity.CONNECT_CHECKOUT_ACTIVITY);
				} catch (PWException e) {
					Log.e("connect", "error creating the payment page", e);
				}
				
			}
		});
		
	    sharedSettings = getSharedPreferences(PREFS_NAME, 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unbindService(serviceConnection);
		stopService(new Intent(this, PWConnectService.class));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			Log.i("mobile.connect.checkout", "user canceled the checkout process/error");
			updateText("Checkout cancelled or an error occurred.");
		}
		else if (resultCode == RESULT_OK) {
			updateText("Thank you for shopping!");
			// if the user added a new account, store it
			if(data.hasExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_RESULT_ACCOUNT)) {
				Log.i("mainactivity", "checkout went through, callback has an account");
				ArrayList<PWAccount> newAccounts = data.getExtras().getParcelableArrayList(PWConnectCheckoutActivity.CONNECT_CHECKOUT_RESULT_ACCOUNT);
				accounts.addAll(newAccounts);
				try {
					sharedSettings.edit().putString(ACCOUNTS, _binder.getAccountFactory().serializeAccountList(accounts)).commit();
				} catch (PWProviderNotInitializedException e) {
					e.printStackTrace();
				}
			} else {
				Log.i("mainactivity", "checkout went through, callback has transaction result");
			}
		}
	}
	
	private void updateText(final String string) {
		runOnUiThread(new Runnable() {
		     public void run() {
		 		((TextView) findViewById(R.id.status)).setText(string);
		    }
		});
	}

}
