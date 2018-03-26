package com.mobile.connect.checkout.dialog.fragment.creditcard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mobile.connect.checkout.dialog.PaymentWorkflowProgressListener;
import com.mobile.connect.checkout.meta.PWConnectCheckoutPaymentMethod;

import com.airsme.R;

/**
 * Fragment used to query the credit card details from the user.
 *
 */
@SuppressLint("ValidFragment")
public class InsertCreditCardPaymentDataFragment extends Fragment implements OnClickListener, TextWatcher {

	private PaymentWorkflowProgressListener _workflowCallback;
	private PWConnectCheckoutPaymentMethod _selectedPaymenMethod;
	
	private View _container;

	private EditText _inputName, _inputCCNumber, _inputCCExpiryMonth, _inputCCExpiryYear, _inputCCCVV;
	private Button _reviewButton;
	private ImageView _paymentIcon;

	public InsertCreditCardPaymentDataFragment(PaymentWorkflowProgressListener callback, PWConnectCheckoutPaymentMethod paymentMethod) {
		_workflowCallback = callback;
		_selectedPaymenMethod = paymentMethod;
	}

	@Override
	public void onPause() {
		_inputName.setText("");
		_inputCCNumber.setText("");
		_inputCCCVV.setText("");
		_inputCCExpiryMonth.setText("");
		_inputCCExpiryYear.setText("");
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		_container = inflater.inflate(R.layout.connect_checkout_insert_cc_payment_data, container, false);
		_inputName = (EditText) _container.findViewById(R.id.connect_checkout_insert_cc_payment_data_name);
		_inputCCNumber = (EditText) _container.findViewById(R.id.connect_checkout_insert_cc_payment_data_ccnumber);
		_inputCCExpiryMonth = (EditText) _container.findViewById(R.id.connect_checkout_insert_cc_payment_data_expiry_month);
		_inputCCExpiryMonth.addTextChangedListener(this);
		
		_inputCCExpiryYear = (EditText) _container.findViewById(R.id.connect_checkout_insert_cc_payment_data_expiry_year);
		_inputCCExpiryYear.addTextChangedListener(this);
		
		_inputCCCVV = (EditText) _container.findViewById(R.id.connect_checkout_insert_cc_payment_data_cvv);
		_reviewButton = (Button) _container.findViewById(R.id.connect_checkout_insert_cc_payment_data_button);
		_reviewButton.setOnClickListener(this);
		_paymentIcon = (ImageView) _container.findViewById(R.id.connect_checkout_insert_cc_payment_data_payment_method_icon);
		_paymentIcon.setImageResource(_selectedPaymenMethod.getIconMapping());
		return _container;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.connect_checkout_insert_cc_payment_data_button) {
			_workflowCallback.paymentCreditCardDataProvided(_inputName.getText().toString(), _inputCCNumber.getText().toString(), _inputCCExpiryMonth.getText().toString(), _inputCCExpiryYear
					.getText().toString(), _inputCCCVV.getText().toString());
		}
	}

	@Override
	public void afterTextChanged(Editable editable) {
		View currentFocus = _container.findFocus();
		if(currentFocus == _inputCCExpiryMonth) {
			if(editable.length() == 2) {
				_inputCCExpiryYear.requestFocus();
			}
		} else if(currentFocus == _inputCCExpiryYear) {
			if(editable.length() == 4) {
				_inputCCCVV.requestFocus();
			}
		}
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
	}
		
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

}