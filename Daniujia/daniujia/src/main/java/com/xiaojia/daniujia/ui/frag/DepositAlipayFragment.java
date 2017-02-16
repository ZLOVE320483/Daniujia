package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.DepositSuccessDlg;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.DepositAccountRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.UIUtil;


public class DepositAlipayFragment extends BaseFragment implements View.OnClickListener {

	private EditText etAliAccount;
	private EditText etAliName;
	private EditText etDepositMoney;
	private Button btnConfirm;

	private float balance = 0.0f;

	private String accountNo;
	private String name;
	private String depositMoney;
	private DepositSuccessDlg successDlg = null;

	@Override
	protected int getInflateLayout() {
		return R.layout.frag_balance_to_ali;
	}

	@Override
	protected void setUpView(View view) {
		Intent intent = getActivity().getIntent();
		if (intent.hasExtra(IntentKey.INTENT_KEY_BALANCE)) {
			balance = intent.getFloatExtra(IntentKey.INTENT_KEY_BALANCE, 0.0f);
		}

		etAliAccount = (EditText) view.findViewById(R.id.alipay_account);
		etAliName = (EditText) view.findViewById(R.id.receiver_name);
		etDepositMoney = (EditText) view.findViewById(R.id.money);
		btnConfirm = (Button) view.findViewById(R.id.confirm);
		btnConfirm.setOnClickListener(this);

		etDepositMoney.setHint("本次最多可转出" + balance + "元");
		etDepositMoney.setFilters(new InputFilter[]{UIUtil.getTwoDecimalFilter()});
		requestAliAccountInfo();
	}

	@Override
	public void onClick(View v) {
		if (v == btnConfirm) {
			accountNo = etAliAccount.getText().toString().trim();
			name = etAliName.getText().toString().trim();
			depositMoney = etDepositMoney.getText().toString().trim();

			if (TextUtils.isEmpty(accountNo)) {
				showShortToast("请输入支付宝账号");
				return;
			}
			if (TextUtils.isEmpty(name)) {
				showShortToast("请输入收款人姓名");
				return;
			}
			if (TextUtils.isEmpty(depositMoney)) {
				showShortToast("请输入提现金额");
				return;
			}
			if (Float.valueOf(depositMoney) == 0.0f) {
				showShortToast("提现金额不能为0");
				return;
			}
			if (Float.valueOf(depositMoney) > balance) {
				showShortToast("本次最多可提现" + balance + "元");
				return;
			}
			depositRequest();
		}
	}


	private void setAccountInfo(DepositAccountRespVo respVo) {
		if (respVo == null) {
			return;
		}
		etAliAccount.setText(respVo.getAccountNo());
		etAliName.setText(respVo.getName());
	}

	private void requestAliAccountInfo() {
		String url = Config.WEB_API_SERVER + "/user/deposit/account/1";
		OkHttpClientManager.getInstance(getActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<DepositAccountRespVo>() {
			@Override
			public void onResponse(DepositAccountRespVo result) {
				if (result.getReturncode().equals("SUCCESS")) {
					setAccountInfo(result);
				} else {
					showShortToast(result.getReturnmsg());
				}
			}
			@Override
			public void onError(Request paramRequest, Exception e) {
				super.onError(paramRequest, e);
			}

			@Override
			public void onFail(String errorMsg) {
				super.onFail(errorMsg);
			}
		});
	}

	private void depositRequest() {

		String url = Config.WEB_API_SERVER + "/user/deposit/do";
		FormEncodingBuilder builder = new FormEncodingBuilder();
		builder.add("aType", "alipay");
		builder.add("accountNo", accountNo);
		builder.add("name", name);
		builder.add("bankName", "");
		builder.add("balance", depositMoney);

		OkHttpClientManager.getInstance(getActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
			@Override
			public void onError(Request paramRequest, Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(BaseRespVo result) {
				if (result.getReturncode().equals("SUCCESS")) {
					balance = balance - Float.valueOf(depositMoney);
					LogUtil.d("zptest","response balance: "+ balance);
					if (successDlg == null) {
						successDlg = new DepositSuccessDlg(getActivity());
					}
					successDlg.initDlg(R.drawable.reward_answers_note,"提现申请成功",getResources().getString(R.string.deposit_success));
					successDlg.setOnClickListener(new BaseDialog.DialogClickListener() {
						@Override
						public void onOK(String s) {
							if (s != null) {
								if (s.equals("Confirm")) {
									Intent intent = new Intent();
									intent.putExtra(IntentKey.INTENT_KEY_BALANCE, balance);
									finishActivity(intent);
								} else if (s.equals("Close")) {
									etDepositMoney.getText().clear();
								}
							}
							successDlg.dismiss();
						}
					});
					successDlg.showDialog();

				} else {
					showShortToast(result.getReturnmsg());
				}
			}

			@Override
			public void onFail(String errorMsg) {
				LogUtil.d("ZLOVE", "depositRequest---onFail---" + errorMsg);
			}
		}, builder);
	}

	public float getBalance(){
		return balance;
	}

}
