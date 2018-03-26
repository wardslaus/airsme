package com.mobile.connect.checkout.dialog.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mobile.connect.checkout.dialog.PaymentWorkflowProgressListener;

import com.airsme.R;
/**
 * Fragment displaying a progress view while the transaction is being executed.
 */
@SuppressWarnings("unused")
public class ExecutePaymentFragment extends Fragment {


	private PaymentWorkflowProgressListener _workflowCallback;
	private ProgressBar _animator;

	@SuppressLint("ValidFragment")
	public ExecutePaymentFragment(PaymentWorkflowProgressListener callback) {
		_workflowCallback = callback;
	}

	public ExecutePaymentFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.connect_checkout_transaction_execute, container, false);
		_animator = (ProgressBar) view.findViewById(R.id.connect_checkout_transaction_execute_indicator);
		return view;
	}

}
