package com.mobile.connect.checkout.dialog.fragment.directdebit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobile.connect.checkout.dialog.PaymentWorkflowProgressListener;
import com.mobile.connect.checkout.meta.PWConnectCheckoutPaymentMethod;

import com.airsme.R;

@SuppressLint("ValidFragment")
public class InsertDirectDebitPaymentDataFragment extends Fragment implements OnClickListener {

	private PaymentWorkflowProgressListener _workflowCallback;
	private String[] _supportedCountries;

	private EditText _inputName, _inputDDAccount, _inputDDBank;
	private TextView _inputDDCountryFixed;
	private Spinner _inputDDCountrySelect;
	private Button _reviewButton;

	ArrayAdapter<String> _adapter;

	public InsertDirectDebitPaymentDataFragment(PaymentWorkflowProgressListener callback, PWConnectCheckoutPaymentMethod paymentMethod, String[] supportedCountries) {
		_workflowCallback = callback;
		_supportedCountries = supportedCountries;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (_supportedCountries.length > 1) {
			_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, _supportedCountries);
			_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		}
	}

	@Override
	public void onPause() {
		_inputName.setText("");
		_inputDDAccount.setText("");
		_inputDDBank.setText("");
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.connect_checkout_insert_dd_payment_data, container, false);
		_inputName = (EditText) view.findViewById(R.id.connect_checkout_insert_dd_payment_data_name);
		_inputDDAccount = (EditText) view.findViewById(R.id.connect_checkout_insert_dd_payment_data_account);
		_inputDDBank = (EditText) view.findViewById(R.id.connect_checkout_insert_dd_payment_data_bank);
		_inputDDCountryFixed = (TextView) view.findViewById(R.id.connect_checkout_insert_dd_payment_data_country_fixed);
		_inputDDCountrySelect = (Spinner) view.findViewById(R.id.connect_checkout_insert_dd_payment_data_country_spinner);

		if (_supportedCountries.length > 1) {
			_inputDDCountryFixed.setVisibility(View.GONE);
			_inputDDCountrySelect.setVisibility(View.VISIBLE);
			_inputDDCountrySelect.setAdapter(_adapter);

		} else {
			_inputDDCountrySelect.setVisibility(View.GONE);
			_inputDDCountryFixed.setVisibility(View.VISIBLE);
			_inputDDCountryFixed.setText(_supportedCountries[0]);
		}

		_reviewButton = (Button) view.findViewById(R.id.connect_checkout_insert_dd_payment_data_button);
		_reviewButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.connect_checkout_insert_dd_payment_data_button) {
			String country;
			if (_supportedCountries.length > 1)
				country = (String) _inputDDCountrySelect.getSelectedItem();
			else
				country = _supportedCountries[0];

			_workflowCallback.paymentDirectDebitDataProvided(_inputName.getText().toString(), _inputDDAccount.getText().toString(), _inputDDBank.getText().toString(), country);
		}
	}

}
