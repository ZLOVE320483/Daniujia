package com.xiaojia.daniujia.ui.frag;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.act.ActMain;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/10/18.
 */
public class CommitSuccessFragment extends BaseFragment {
    private TextView mTvContact;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_commit_success;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("提交成功");
        mTvContact = (TextView) view.findViewById(R.id.commit_success_tv_contact);

    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView(){
        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)){
            String blueText = "大牛家客服";
            SpannableString loginSpan = new SpannableString(blueText);
            ClickableSpan clickableSpan = new ShowClickableSpan(blueText,getActivity());
            loginSpan.setSpan(clickableSpan, 0, blueText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mTvContact.setText("您也可以直接和");
            mTvContact.append(loginSpan);
            mTvContact.append("进行沟通,或者拨打客服电话");

        } else {
            String blueText = "登录";
            SpannableString loginSpan = new SpannableString(blueText);
            ClickableSpan clickableSpan = new ShowClickableSpan(blueText,getActivity());
            loginSpan.setSpan(clickableSpan, 0, blueText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mTvContact.setText("您也可以");
            mTvContact.append(loginSpan);
            mTvContact.append("后直接和大牛家客服进行沟通,或者拨打客服电话");
        }
        String phone = "4000903022";
        SpannableString phoneSpan = new SpannableString(phone);
        ClickableSpan clickablePhone = new MyLinkSpan(phone);
        phoneSpan.setSpan(clickablePhone, 0, phone.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mTvContact.append(phoneSpan);
        mTvContact.append("与我们联系。");
        mTvContact.setHighlightColor(Color.TRANSPARENT);
        mTvContact.setMovementMethod(LinkMovementMethod.getInstance());
    }


    public class ShowClickableSpan extends ClickableSpan {
        String string;
        Context context;
        public ShowClickableSpan(String str,Context context){
            super();
            this.string = str;
            this.context = context;
        }


        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(0xff2c90de);
        }


        @Override
        public void onClick(View widget) {
            if (string != null) {
                if (string.equals("登录")) {
                    Intent intent = new Intent(getActivity(), ActLogin.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ActMain.class);
                    intent.putExtra(IntentKey.INTENT_KEY_CHAT_TOPIC, "eric");
                    startActivity(intent);
                }
            }

        }

    }

    public class MyLinkSpan extends ClickableSpan{
        private String phone;

        public MyLinkSpan(String phone) {
            this.phone = phone;
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phone);
            intent.setData(data);
            startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(0xff888888);       //设置文件颜色
            ds.setUnderlineText(true);
        }
    }
}
