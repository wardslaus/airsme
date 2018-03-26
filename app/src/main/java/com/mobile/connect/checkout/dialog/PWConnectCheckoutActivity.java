package com.mobile.connect.checkout.dialog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airsme.R;
import com.mobile.connect.checkout.dialog.fragment.ChoosePaymentMethodFragment;
import com.mobile.connect.checkout.dialog.fragment.ExecutePaymentFragment;
import com.mobile.connect.checkout.dialog.fragment.creditcard.InsertCreditCardPaymentDataFragment;
import com.mobile.connect.checkout.dialog.fragment.creditcard.ReviewCreditCardPaymentDataFragment;
import com.mobile.connect.checkout.dialog.fragment.directdebit.InsertDirectDebitPaymentDataFragment;
import com.mobile.connect.checkout.dialog.fragment.directdebit.ReviewDirectDebitPaymentDataFragment;
import com.mobile.connect.checkout.meta.PWConnectCheckoutCreateToken;
import com.mobile.connect.checkout.meta.PWConnectCheckoutPaymentMethod;
import com.mobile.connect.checkout.meta.PWConnectCheckoutSettings;
import com.mobile.connect.checkout.meta.PWConnectCheckoutTransactionType;
import com.mobile.connect.exception.PWError;
import com.mobile.connect.exception.PWException;
import com.mobile.connect.listener.PWTokenObtainedListener;
import com.mobile.connect.listener.PWTransactionListener;
import com.mobile.connect.payment.AccountFactory;
import com.mobile.connect.payment.AccountType;
import com.mobile.connect.payment.PWAccount;
import com.mobile.connect.payment.PWCurrency;
import com.mobile.connect.payment.PWPaymentParams;
import com.mobile.connect.payment.PWPaymentParamsFactory;
import com.mobile.connect.payment.credit.PWCreditCardType;
import com.mobile.connect.provider.PWTransaction;
import com.mobile.connect.service.PWConnectService;
import com.mobile.connect.service.PWProviderBinder;

import java.util.List;

public class PWConnectCheckoutActivity extends FragmentActivity implements PWTransactionListener,
		PWTokenObtainedListener, PaymentWorkflowProgressListener, OnClickListener {
	public static final int CONNECT_CHECKOUT_ACTIVITY = 242;
	public static final String CONNECT_CHECKOUT_SETTINGS = "com.mobile.connect.com.mobile.connect.checkout.meta.PWConnectCheckoutSettings";
	public static final String CONNECT_CHECKOUT_GENERIC_PAYMENT_PARAMS = "com.mobile.connect.checkout.meta.PWConnectCheckoutGenericPaymentParams";

	// stuff contained in the result intent
	public static final String CONNECT_CHECKOUT_RESULT_ACCOUNT = "com.mobile.connect.checkout.meta.PWConnectCheckoutResult.account";
	public static final String CONNECT_CHECKOUT_RESULT_SETTINGS = "com.mobile.connect.checkout.meta.PWConnectCheckoutResult.settings";
	public static final String CONNECT_CHECKOUT_RESULT_GENERIC_PAYMENT_PARAMS = "com.mobile.connect.checkout.meta.PWConnectCheckoutResultGenericPaymentParams";
	public static final String CONNECT_CHECKOUT_RESULT_TRANSACTION = "com.mobile.connect.checkout.meta.PWConnectCheckoutResult.transaction";
	public static final String CONNECT_CHECKOUT_RESULT_ERROR = "com.mobile.connect.checkout.meta.PWConnectCheckoutResult.error";
	
	private static final String TAG = "PWConnectActivity";

	private TextView _footerText;
	private ImageView _headerIcon;
	private TextView _headerPaymentAmount, _headerPaymentVAT, _headerPaymentTitle;
	private ImageButton _headerClose;

	private PWConnectCheckoutPaymentMethod[] _supportedMethods;
	private String[] _supportedCountries;
	private Drawable _applicationIcon;
	private double _vatAmount;
	private String _title;

	public static PWConnectCheckoutSettings checkoutSettings;
	private PWConnectCheckoutSettings _checkoutSettings;
	private PWPaymentParams _genericPaymentParams;

	private double _paymentAmount;
	private PWCurrency _paymentCurrency;
	private String _paymentSubject;

	private PWConnectCheckoutTransactionType _transactionType;
	private PWConnectCheckoutTransactionType _transactionTypeAfterTokenization;
	private PWConnectCheckoutPaymentMethod _selectedPaymentMethod;
	private PWPaymentParams _paymentParams;

	private boolean _inExecuteStep;

	private PWProviderBinder _binder;
	private Handler _handler;

	private Intent _resultIntent = new Intent();

	private boolean _showToasts;
	private List<PWAccount> _storedAccounts;
	private PWAccount _selectedAccount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect_checkout_template);
		_handler = new Handler();
System.out.println("vvvvcccccccccccccccccccccccccccccccccccccccccccccccc "+checkoutSettings.toString());
		_checkoutSettings=checkoutSettings;
		System.out.println("vvvvcccccccccccccccccccccccccccccccccccccccccccccccc "+_checkoutSettings.toString());
		checkoutSettings=null;

		System.out.println("vvvvcccccccccccccccccccccccccccccccccccccccccccccccc "+_checkoutSettings.toString());
		bindService(new Intent(this, PWConnectService.class), serviceConnection,
				Context.BIND_AUTO_CREATE);

		_showToasts = true;
		Intent startingIntent = getIntent();
		_inExecuteStep = false;

		_checkoutSettings = startingIntent
				.getParcelableExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_SETTINGS);
		_genericPaymentParams = startingIntent
				.getParcelableExtra(PWConnectCheckoutActivity.CONNECT_CHECKOUT_GENERIC_PAYMENT_PARAMS);

		if (_checkoutSettings == null&&false) {
			Log.d(TAG, "no settings provided, exiting checkout overlay");
			putExtras(null, null);
			setResult(RESULT_CANCELED, _resultIntent);
			finish();
			return;
		}

		// extract checkout information
		// image to be displayed
		int iconResource = _checkoutSettings.getHeaderIconResource();
		if (iconResource == 0x00) {
			ApplicationInfo info = getApplicationInfo();
			PackageManager pk = getPackageManager();
			_applicationIcon = pk.getApplicationIcon(info);
		} else {
			_applicationIcon = getResources().getDrawable(iconResource);
		}

		// methods offered
		_supportedMethods = _checkoutSettings.getSupportedPaymentMethods();
		// countries for direct debit transactions
		_supportedCountries = _checkoutSettings.getSupportedDirectDebitCountries();
		// transaction type
		_transactionType = _checkoutSettings.getCheckoutTransactionType();
		// vat amount
		_vatAmount = _checkoutSettings.getPaymentVATAmount();
		// header title
		_title = _checkoutSettings.getHeaderDescription();

		_storedAccounts = _checkoutSettings.getStoredAccounts();

		// extract payment related data
		// currency
		_paymentCurrency = extractAndDecideOnCurrency();
		if (_paymentCurrency == null) {
			Log.d(TAG, "payment currency not provided, exiting checkout overlay");
			putExtras(null, null);
			setResult(RESULT_CANCELED, _resultIntent);
			finish();
			return;
		}
		// amount
		_paymentAmount = extractAndDecideOnAmount();
		if (_paymentAmount <= 0.0) {
			Log.d(TAG, "payment amount not provided or to low, exiting checkout overlay");
			putExtras(null, null);
			setResult(RESULT_CANCELED, _resultIntent);
			finish();
			return;
		}
		// subject
		_paymentSubject = extractAndDecideOnSubject();

		// setup the ui widgets
		_headerIcon = (ImageView) findViewById(R.id.connect_checkout_template_header_icon);
		_headerIcon.setImageDrawable(_applicationIcon);
		_headerPaymentAmount = (TextView) findViewById(R.id.connect_checkout_template_header_amount);
		_headerPaymentAmount.setText(String.format(
				getString(R.string.connect_checkout_layout_text_header_amount), _paymentAmount,
				_paymentCurrency.getIdentifier()));

		_headerPaymentVAT = (TextView) findViewById(R.id.connect_checkout_template_header_vat);
		if (_vatAmount >= 0.0)
			_headerPaymentVAT.setText(String.format(
					getString(R.string.connect_checkout_layout_text_header_vat), _vatAmount,
					_paymentCurrency.getIdentifier()));
		else
			_headerPaymentVAT.setVisibility(View.INVISIBLE);

		_headerPaymentTitle = (TextView) findViewById(R.id.connect_checkout_template_header_title);
		if (_title != null)
			_headerPaymentTitle.setText(_title);
		else
			_headerPaymentTitle.setVisibility(View.INVISIBLE);

		_headerClose = (ImageButton) findViewById(R.id.connect_checkout_template_header_close);
		_headerClose.setOnClickListener(this);
		_footerText = (TextView) findViewById(R.id.connect_checkout_template_footer_text);

		// init the first fragment, showing the list of payment methods
		Fragment newFragment = new ChoosePaymentMethodFragment(this, _supportedMethods,
				_storedAccounts);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.connect_checkout_template_content, newFragment);
		transaction.commit();
		_footerText.setText(getString(R.string.connect_checkout_layout_text_footer_step0));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (_binder != null) {
			_binder.removeTransactionListener(this);
			_binder.removeTokenObtainedListener(this);
		}
		unbindService(serviceConnection);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		// close icon touched
		if (id == R.id.connect_checkout_template_header_close)
			paymentProcessCanceled();
	}

	@Override
	public void paymentProcessCanceled() {
		putExtras(null, null);
		setResult(RESULT_CANCELED, _resultIntent);
		finish();
	}

	@Override
	public void paymentMethodProvided(PWConnectCheckoutPaymentMethod paymentMethod) {
		_selectedPaymentMethod = paymentMethod;

		Fragment newFragment;

		if (paymentMethod == PWConnectCheckoutPaymentMethod.DIRECT_DEBIT) {
			newFragment = new InsertDirectDebitPaymentDataFragment(this, _selectedPaymentMethod,
					_supportedCountries);
		} else {
			newFragment = new InsertCreditCardPaymentDataFragment(this, _selectedPaymentMethod);
		}

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
				R.anim.slide_in_left, R.anim.slide_out_right);
		transaction.replace(R.id.connect_checkout_template_content, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
		_footerText.setText(getString(R.string.connect_checkout_layout_text_footer_step1));
	}

	@Override
	public void accountProvided(PWAccount account) {
		/*_selectedAccount = account;

		Account accountPrivateAPI = (Account) account;

		PWConnectCheckoutPaymentMethod method = accountPrivateAPI.getAccountType() == AccountType.CREDIT_CARD ? PWConnectCheckoutPaymentMethod
				.fromCreditCardType(accountPrivateAPI.getCreditCardType())
				: PWConnectCheckoutPaymentMethod.DIRECT_DEBIT;
		Fragment newFragment = new ReviewAccountFragment(this, accountPrivateAPI.getHolder(),
				accountPrivateAPI.getDigits(), method);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
				R.anim.slide_in_left, R.anim.slide_out_right);
		transaction.replace(R.id.connect_checkout_template_content, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
		_footerText.setText(getString(R.string.connect_checkout_layout_text_footer_step2));*/
	}

	@Override
	public void requestToChangePaymentMethod() {
		_selectedPaymentMethod = null;
		getSupportFragmentManager().popBackStack();
		_footerText.setText(getString(R.string.connect_checkout_layout_text_footer_step0));
	}

	@Override
	public void paymentCreditCardDataProvided(String name, String ccNumber, String expiryMonth,
                                              String expiryYear, String cvv) {
		if (_binder == null)
			return;

		try {
			PWPaymentParamsFactory factory = _binder.getPaymentParamsFactory();
			PWCreditCardType ccType = _selectedPaymentMethod.getCreditCardTypeMapping();
System.out.println("raaaaaaaaaaaaaaaaaaaaaaaaaaaaaiiiiiiiiiiiiiiisssssssssssssssseeeeeeeeeeeeeeeeeee "+_paymentAmount);
			// when we have generic params, use them as a reference
			if (_genericPaymentParams != null) {
				_paymentParams = factory.createCreditCardPaymentParams(_genericPaymentParams, name,
						ccType, ccNumber, expiryYear, expiryMonth, cvv);
			} else {
				_paymentParams = factory.createCreditCardPaymentParams(_paymentAmount,
						_paymentCurrency, _paymentSubject, name, ccType, ccNumber, expiryYear,
						expiryMonth, cvv);
			}

		} catch (PWException ee) {
			makeToast(ee.getError().getErrorMessage());
			return;
		}

		String strippedNumber = ccNumber.substring(ccNumber.length() > 4 ? ccNumber.length() - 4
				: 0);
		boolean showStorePaymentData = _checkoutSettings
				.getCreateTokenForPaymentMethod(_selectedPaymentMethod) == PWConnectCheckoutCreateToken.PROMPT;
		Fragment newFragment = new ReviewCreditCardPaymentDataFragment(this,
				_selectedPaymentMethod, name, strippedNumber, expiryMonth, expiryYear,
				cvv.length(), showStorePaymentData);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
				R.anim.slide_in_left, R.anim.slide_out_right);
		transaction.replace(R.id.connect_checkout_template_content, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
		_footerText.setText(getString(R.string.connect_checkout_layout_text_footer_step2));
	}

	@Override
	public void paymentDirectDebitDataProvided(String name, String accountNumber,
                                               String bankNumber, String bankCountry) {
		if (_binder == null)
			return;

		try {
			PWPaymentParamsFactory factory = _binder.getPaymentParamsFactory();

			// when we have generic params, use them as a reference
			if (_genericPaymentParams != null) {
				_paymentParams = factory.createNationalDirectDebitPaymentParams(
						_genericPaymentParams, name, accountNumber, bankNumber, bankCountry, null);
			} else {
				_paymentParams = factory.createNationalDirectDebitPaymentParams(_paymentAmount,
						_paymentCurrency, _paymentSubject, name, accountNumber, bankNumber,
						bankCountry, null);
			}

		} catch (PWException ee) {
			makeToast(ee.getError().getErrorMessage());
			return;
		}

		String strippedNumber = accountNumber.substring(accountNumber.length() > 2 ? accountNumber
				.length() - 2 : 0);
		boolean showStorePaymentData = _checkoutSettings
				.getCreateTokenForPaymentMethod(_selectedPaymentMethod) == PWConnectCheckoutCreateToken.PROMPT;
		Fragment newFragment = new ReviewDirectDebitPaymentDataFragment(this,
				_selectedPaymentMethod, name, strippedNumber, bankNumber, bankCountry,
				showStorePaymentData);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
				R.anim.slide_in_left, R.anim.slide_out_right);
		transaction.replace(R.id.connect_checkout_template_content, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
		_footerText.setText(getString(R.string.connect_checkout_layout_text_footer_step2));
	}

	@Override
	public void requestToChangePaymentData() {
		_paymentParams = null;
		getSupportFragmentManager().popBackStack();
		_footerText.setText(getString(R.string.connect_checkout_layout_text_footer_step1));
	}

	private void createAndRegisterTransaction(PWPaymentParams paymentParams) {
		try {
			switch (_transactionType) {
			case DEBIT:
				_binder.createAndRegisterDebitTransaction(_paymentParams);
				break;
			case PREAUTHORIZE:
				_binder.createAndRegisterPreauthorizationTransaction(_paymentParams);
				break;
			default:
				break;
			}
		} catch (PWException ee) {
			makeConditionalToast(ee.getError().getErrorMessage());
			return;
		}
	}

	@Override
	public void paymentDataAccepted(boolean choseStoreData) {
		if (_binder == null)
			return;

		try {
			// tokenize first if tokenization required
			if (_checkoutSettings.getCreateTokenForPaymentMethod(_selectedPaymentMethod) == PWConnectCheckoutCreateToken.ALWAYS
					|| (_checkoutSettings.getCreateTokenForPaymentMethod(_selectedPaymentMethod) == PWConnectCheckoutCreateToken.PROMPT && choseStoreData)) {
				// change _transactionType to tokenization for now
				_transactionTypeAfterTokenization = _transactionType;
				_transactionType = PWConnectCheckoutTransactionType.TOKENIZATION;
				_binder.createAndRegisterObtainTokenTransaction(_paymentParams);

			} else {
				createAndRegisterTransaction(_paymentParams);
			}
		} catch (PWException ee) {
			makeConditionalToast(ee.getError().getErrorMessage());
			return;
		}

		disableBackAndClose();

		Fragment newFragment = new ExecutePaymentFragment(this);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);// no
																						// popping
																						// from
																						// backstack
																						// allowed
		transaction.replace(R.id.connect_checkout_template_content, newFragment);
		transaction.commit();
		_footerText.setText(getString(R.string.connect_checkout_layout_text_footer_step3));

	}

	@Override
	public void accountAccepted() {
	/*	if (_binder == null) {
			return;
		}
		// create payment params
		Account accountPrivateAPI = (Account) _selectedAccount;

		try {
			if(_genericPaymentParams != null) {
				_paymentParams = _binder.getPaymentParamsFactory()
						.createTokenPaymentParams(_genericPaymentParams, accountPrivateAPI.getToken());
			} else {
				_paymentParams = _binder.getPaymentParamsFactory()
						.createTokenPaymentParams(_paymentAmount, _paymentCurrency, _paymentSubject,
								accountPrivateAPI.getToken());
			}
			// do not tokenize
			_checkoutSettings.setCreateTokenForPaymentMethod(_selectedPaymentMethod,
					PWConnectCheckoutCreateToken.NEVER);
			// do transaction
			paymentDataAccepted(false);
		} catch (PWException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	private void disableBackAndClose() {
		_inExecuteStep = true;
		_headerClose.setEnabled(false);
		_headerClose.setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		if (_inExecuteStep)
			return;

		super.onBackPressed();
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			_binder = (PWProviderBinder) service;
			// directly add as listener
			_binder.addTransactionListener(PWConnectCheckoutActivity.this);
			_binder.addTokenObtainedListener(PWConnectCheckoutActivity.this);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			_binder = null;
		}
	};

	@Override
	public void transactionSucceeded(PWTransaction transaction) {
		if (_transactionType == PWConnectCheckoutTransactionType.TOKENIZATION) {
			// tokenization transaction succeeded, switch to original
			// transaction
			_transactionType = _transactionTypeAfterTokenization;
			createAndRegisterTransaction(_paymentParams);
		} else {
			makeConditionalToast("transaction successful");
			// by the way, pack the original settings in the result
			putExtras(transaction, null);
			setResult(Activity.RESULT_OK, _resultIntent);
			finish();
		}
	}

	@Override
	public void transactionFailed(PWTransaction transaction, PWError error) {
		makeConditionalToast("transaction failed:\n" + error.getErrorMessage());
		putExtras(transaction, error);
		setResult(Activity.RESULT_CANCELED, _resultIntent);
		finish();
	}

	@Override
	public void creationAndRegistrationSucceeded(PWTransaction transaction) {
		makeConditionalToast("transaction registration successful");
		try {
			switch (_transactionType) {
			case DEBIT:
				_binder.debitTransaction(transaction);
				break;
			case PREAUTHORIZE:
				_binder.preauthorizeTransaction(transaction);
				break;
			case TOKENIZATION:
				_binder.obtainToken(transaction);
				break;
			}
		} catch (PWException ee) {
			putExtras(transaction, null);
			setResult(Activity.RESULT_CANCELED, _resultIntent);
			finish();
			Log.d("TAG", "error starting to execute the transaction " + ee.getMessage());
		}
	}

	@Override
	public void obtainedToken(String token, PWTransaction transaction) {
		try {
			// put the account in the intent that is returned before changing
			// the payment parameters
			// Private!
			AccountType accountType = _selectedPaymentMethod == PWConnectCheckoutPaymentMethod.DIRECT_DEBIT ? AccountType.DIRECT_DEBIT
					: AccountType.CREDIT_CARD;
			_resultIntent.putParcelableArrayListExtra(CONNECT_CHECKOUT_RESULT_ACCOUNT,
					AccountFactory.createAccountSingletonArrayList(accountType,
							_selectedPaymentMethod.getCreditCardTypeMapping(), transaction.getPaymentParams(),
							token) /* Private API */);

			if (_genericPaymentParams != null) {
				_paymentParams = _binder.getPaymentParamsFactory().createTokenPaymentParams(
						_genericPaymentParams, token);
			} else {
				_paymentParams = _binder.getPaymentParamsFactory().createTokenPaymentParams(
						_paymentParams.getAmount(), _paymentParams.getCurrency(),
						_paymentParams.getSubject(), token);
			}

		} catch (PWException e) {
			makeConditionalToast(e.getError().getErrorMessage());
		}

	}

	@Override
	public void creationAndRegistrationFailed(PWTransaction transaction, PWError error) {
		putExtras(transaction, error);
		makeConditionalToast("transaction registration failed:\n" + error.getErrorMessage());
		setResult(Activity.RESULT_CANCELED, _resultIntent);
		finish();
	}

	private void makeToast(final String toast) {
		_handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PWConnectCheckoutActivity.this, toast, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void makeConditionalToast(final String toast) {
		if (!_showToasts)
			return;

		_handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PWConnectCheckoutActivity.this, toast, Toast.LENGTH_LONG).show();
			}
		});
	}

	private PWCurrency extractAndDecideOnCurrency() {
		PWCurrency currency = _checkoutSettings.getPaymentCurrency();
		if (_genericPaymentParams != null)
			currency = _genericPaymentParams.getCurrency();

		return currency;
	}

	private double extractAndDecideOnAmount() {
		double amount = _checkoutSettings.getPaymentAmount();
		if (_genericPaymentParams != null)
			amount = _genericPaymentParams.getAmount();

		return amount;
	}

	private String extractAndDecideOnSubject() {
		String subject = _checkoutSettings.getPaymentSubject();
		if (_genericPaymentParams != null)
			subject = _genericPaymentParams.getSubject();

		return subject;
	}
	
	private void putExtras(PWTransaction transaction, PWError error) {
		_resultIntent.putExtra(CONNECT_CHECKOUT_RESULT_TRANSACTION, transaction);
		_resultIntent.putExtra(CONNECT_CHECKOUT_RESULT_SETTINGS, _checkoutSettings);
		_resultIntent.putExtra(CONNECT_CHECKOUT_RESULT_GENERIC_PAYMENT_PARAMS,
				_genericPaymentParams);
		_resultIntent.putExtra(CONNECT_CHECKOUT_RESULT_ERROR, error);
	}

}
