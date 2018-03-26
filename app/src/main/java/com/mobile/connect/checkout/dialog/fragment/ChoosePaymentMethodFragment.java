package com.mobile.connect.checkout.dialog.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.airsme.R;
import com.mobile.connect.checkout.dialog.PaymentWorkflowProgressListener;
import com.mobile.connect.checkout.meta.PWConnectCheckoutPaymentMethod;
import com.mobile.connect.payment.Account;
import com.mobile.connect.payment.AccountType;
import com.mobile.connect.payment.PWAccount;

import java.util.List;




/**
 * Fragment that displays a list of available payment methods.
 */

public class ChoosePaymentMethodFragment extends Fragment implements OnItemClickListener {

	private PaymentWorkflowProgressListener _workflowCallback;
	private ItemAdapter _adapter;
	private ListView _list;
	private PWConnectCheckoutPaymentMethod[] _supportedPaymentMethods;
	private List<PWAccount> _storedAccounts;

	@SuppressLint("ValidFragment")
	public ChoosePaymentMethodFragment(PaymentWorkflowProgressListener callback, PWConnectCheckoutPaymentMethod[] supportedPaymentMethods, List<PWAccount> storedAccounts) {
		_workflowCallback = callback;
		_supportedPaymentMethods = supportedPaymentMethods;
		_storedAccounts = storedAccounts;
	}

	public ChoosePaymentMethodFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_adapter = new ItemAdapter(getActivity(), _supportedPaymentMethods, _storedAccounts);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.connect_checkout_choose_payment, container, false);
		_list = (ListView) view.findViewById(R.id.connect_checkout_choose_payment_list);
		_list.setAdapter(_adapter);
		_list.setOnItemClickListener(this);
		return view;
	}

	private class ItemAdapter extends BaseAdapter {

		private LayoutInflater _inflater;
		private Object[] _items;

		public ItemAdapter(Context context, PWConnectCheckoutPaymentMethod[] paymentMethods, List<PWAccount> _storedAccounts) {
			Object[] items = new Object[paymentMethods.length+_storedAccounts.size()];
			for(int i = 0; i < paymentMethods.length; i++) {
				items[i] = paymentMethods[i];
			}
			for(int j = 0; j < _storedAccounts.size(); j++) {
				items[paymentMethods.length+j]=_storedAccounts.get(j);
			}
			
			_items = items;
			
			
			_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return _items.length;
		}

		@Override
		public Object getItem(int position) {
			return _items[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public int getItemViewType(int position) {
			if(_items[position] instanceof PWConnectCheckoutPaymentMethod) {
				return 0;
			} else {
				return 1;
			}
		};
		
		@Override
		public int getViewTypeCount() { return 2; };

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Object item = getItem(position);
			if(item instanceof PWConnectCheckoutPaymentMethod) {
				if (convertView == null) {
					convertView = _inflater.inflate(R.layout.connect_checkout_choose_payment_item, null);
				}
				
				ImageView _icon = (ImageView) convertView.findViewById(R.id.connect_checkout_choose_payment_item_icon);
				TextView _name = (TextView) convertView.findViewById(R.id.connect_checkout_choose_payment_item_description);
				
				PWConnectCheckoutPaymentMethod method = (PWConnectCheckoutPaymentMethod) getItem(position);

				int iconMapping = method.getIconMapping();
				if (iconMapping == 0x00) {
					_icon.setVisibility(View.INVISIBLE);
				} else {
					_icon.setVisibility(View.VISIBLE);
					_icon.setImageResource(iconMapping);
				}
				_name.setText(getString(method.getStringMapping()));

				return convertView;
			} else {
				if(convertView == null) {
					convertView = _inflater.inflate(R.layout.connect_checkout_choose_payment_stored_account, null);
				}
				
				PWAccount account = (PWAccount) getItem(position);
				
				TextView _name = (TextView) convertView.findViewById(R.id.connect_checkout_choose_payment_stored_account_title);
				TextView _accountDescription = (TextView) convertView.findViewById(R.id.connect_checkout_choose_payment_stored_account_description);
				ImageView _icon = (ImageView) convertView.findViewById(R.id.connect_checkout_choose_payment_stored_account_icon);

				
				// here we use the private API of Account
				Account accountPrivateAPI = (Account) account;
				
				if(accountPrivateAPI.getHolder() != null && !accountPrivateAPI.getHolder().isEmpty()) {
					_name.setText(accountPrivateAPI.getHolder());
				} else {
					if(accountPrivateAPI.getAccountType() == AccountType.CREDIT_CARD) {
						_name.setText(getString(R.string.connect_checkout_layout_text_stored_account_credit_card));
					} else {
						_name.setText(getString(R.string.connect_checkout_layout_text_stored_account_direct_debit_card));
					}
				}
				
				_icon.setVisibility(View.VISIBLE);
				_icon.setImageResource(R.drawable.connect_checkout_payment_account);
				
				String cardDescription = "";
				
				if(accountPrivateAPI.getAccountType() == AccountType.CREDIT_CARD) {
					cardDescription = (getString(R.string.connect_checkout_layout_text_stored_account_credit_card_description));
				} else {
					cardDescription = (getString(R.string.connect_checkout_layout_text_stored_account_direct_debit_card_description));
				}
				
				
				// ends with <XXXX>, <HOLDER>
				_accountDescription.setText(String.format("%s, %s %s", cardDescription, getString(R.string.connect_checkout_layout_text_stored_account_ending_with), accountPrivateAPI.getDigits()));
				
				return convertView;
			}
			
			
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object item = _adapter.getItem(position);
		if(item instanceof PWConnectCheckoutPaymentMethod) {
			_workflowCallback.paymentMethodProvided((PWConnectCheckoutPaymentMethod) item);
		} else {
			_workflowCallback.accountProvided((PWAccount) item);
		}
		
	}

}
