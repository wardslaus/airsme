package com.mobile.connect.checkout.dialog.fragment.directdebit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mobile.connect.checkout.dialog.PaymentWorkflowProgressListener;
import com.mobile.connect.checkout.meta.PWConnectCheckoutPaymentMethod;


import com.airsme.R;
@SuppressLint("ValidFragment")
public class ReviewDirectDebitPaymentDataFragment extends Fragment implements OnClickListener {

	private PaymentWorkflowProgressListener _workflowCallback;
	private String _providedDDName;
	private String _providedDDAccountNumberStripped;
	private String _providedDDBankNumber;
	private String _providedDDBankCountry;

	private TextView _inputName, _inputDDAccountNumber, _inputDDBankNumber, _inputDDBankCountry;
	private Button _confirmButton;
	private boolean _showStorePaymentData;
	private CheckBox _storePaymentData;

	@SuppressLint("ValidFragment")
	public ReviewDirectDebitPaymentDataFragment(PaymentWorkflowProgressListener callback, PWConnectCheckoutPaymentMethod paymentMethod, String name, String accountNumberStripped, String bankNumber,
                                                String bankCountry, boolean showStorePaymentData) {
		_workflowCallback = callback;

		_providedDDName = name;
		_providedDDAccountNumberStripped = accountNumberStripped;
		_providedDDBankNumber = bankNumber;
		_providedDDBankCountry = bankCountry;
		this._showStorePaymentData = showStorePaymentData;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.connect_checkout_review_dd_payment_data, container, false);
		_inputName = (TextView) view.findViewById(R.id.connect_checkout_review_dd_payment_data_name);
		_inputName.setText(_providedDDName);
		_inputDDAccountNumber = (TextView) view.findViewById(R.id.connect_checkout_review_dd_payment_data_account);
		_inputDDAccountNumber.setText("*" + _providedDDAccountNumberStripped);
		_inputDDBankNumber = (TextView) view.findViewById(R.id.connect_checkout_review_dd_payment_data_bank);
		_inputDDBankNumber.setText(_providedDDBankNumber);
		_inputDDBankCountry = (TextView) view.findViewById(R.id.connect_checkout_review_dd_payment_data_country);
		_inputDDBankCountry.setText(_providedDDBankCountry);
		
		_storePaymentData = (CheckBox) view.findViewById(R.id.connect_checkout_review_dd_payment_data_store_account_data);

		_confirmButton = (Button) view.findViewById(R.id.connect_checkout_review_dd_payment_data_button);
		_confirmButton.setOnClickListener(this);
		
		if(_showStorePaymentData == true) {
			_storePaymentData.setVisibility(View.VISIBLE);
		} else {
			_storePaymentData.setVisibility(View.INVISIBLE);
		}
		
		
		return view;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.connect_checkout_review_dd_payment_data_button) {
			_workflowCallback.paymentDataAccepted(_storePaymentData.isChecked());
		}
	}

}
