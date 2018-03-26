package com.mobile.connect.checkout.dialog.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.connect.checkout.dialog.PaymentWorkflowProgressListener;
import com.mobile.connect.checkout.meta.PWConnectCheckoutPaymentMethod;

import com.airsme.R;
@SuppressLint("ValidFragment")
public class ReviewAccountFragment extends Fragment implements OnClickListener {

	private PaymentWorkflowProgressListener _workflowCallback;
	private String _providedCCName;
	private String _providedCCNumberStripped;

	private TextView _inputName, _inputCCNumber;
	private PWConnectCheckoutPaymentMethod _paymentMethod;
	private ImageView _paymentIcon;
	private Button _confirmButton;
	
	public ReviewAccountFragment(PaymentWorkflowProgressListener callback, String name, String ccNumberStripped, PWConnectCheckoutPaymentMethod paymentMethod) {
		_workflowCallback = callback;

		_providedCCName = name;
		_providedCCNumberStripped = ccNumberStripped;
		this._paymentMethod = paymentMethod;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.connect_checkout_review_account, container, false);
		_inputName = (TextView) view.findViewById(R.id.connect_checkout_review_account_name);
		_inputName.setText(_providedCCName);
		_confirmButton = (Button) view.findViewById(R.id.connect_checkout_review_account_button);
		_confirmButton.setOnClickListener(this);
		_inputCCNumber = (TextView) view.findViewById(R.id.connect_checkout_review_account_ccnumber);
		_inputCCNumber.setText("*-" + _providedCCNumberStripped);
		_paymentIcon = (ImageView) view.findViewById(R.id.connect_checkout_review_account_payment_method_icon);
		if(_paymentMethod.getIconMapping() != 0x00) {
			_paymentIcon.setImageResource(_paymentMethod.getIconMapping());
		}

		return view;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.connect_checkout_review_account_button) {
			_workflowCallback.accountAccepted();
		}
	}

}
