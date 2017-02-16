package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.act.ActDeposit;
import com.xiaojia.daniujia.ui.act.IncomeExpensesActivity;

import java.text.DecimalFormat;

/**
 * Created by ZLOVE on 2016/6/2.
 */
public class BalanceFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvBalance;
    private View balanceDetailView;
    private View depositView;

    private float balance = 0.0f;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_balance;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_BALANCE)) {
                balance = intent.getFloatExtra(IntentKey.INTENT_KEY_BALANCE, 0.0f);
            }
        }

        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("余额");

        tvBalance = (TextView) view.findViewById(R.id.balance);
        tvBalance.setText(String.valueOf(balance));

        balanceDetailView = view.findViewById(R.id.acc_balance);
        balanceDetailView.setOnClickListener(this);
        depositView = view.findViewById(R.id.deposit);
        depositView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == balanceDetailView) {
            startActivity(new Intent(getActivity(), IncomeExpensesActivity.class));
        } else if (v == depositView) {
            Intent intent = new Intent(getActivity(), ActDeposit.class);
            intent.putExtra(IntentKey.INTENT_KEY_BALANCE, balance);
            getActivity().startActivityForResult(intent, IntentKey.REQ_CODE_DEPOSIT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_DEPOSIT) {
            if (data != null && data.hasExtra(IntentKey.INTENT_KEY_BALANCE)) {
                float temp = data.getFloatExtra(IntentKey.INTENT_KEY_BALANCE, 0.0f);
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                balance = Float.parseFloat(decimalFormat.format(temp));
                String balanceStr = decimalFormat.format(temp);
                tvBalance.setText(TextUtils.isEmpty(balanceStr) ? "0.00" : balanceStr);
            }
        }
    }
}
