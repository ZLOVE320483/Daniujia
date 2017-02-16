package com.xiaojia.daniujia.ui.frag;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.AlertMsgDlg;
import com.xiaojia.daniujia.ui.control.QuestionControl;
import com.xiaojia.daniujia.utils.EditUtils;

/**
 * Created by Administrator on 2016/4/27
 */
public class QuestionFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QuestionControl control = new QuestionControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
       return R.layout.fg_question;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        TextView title= (TextView) view.findViewById(R.id.id_title);
        title.setText(R.string.question);
        TextView title_right = (TextView) view.findViewById(R.id.title_right);
        title_right.setText(R.string.history);
        EditText etQuestionContent = (EditText) view.findViewById(R.id.et_question_content);
        TextView textInput = (TextView) view.findViewById(R.id.input_num);

        EditText personContent = (EditText) view.findViewById(R.id.person_edit);
        TextView personInputNum = (TextView) view.findViewById(R.id.person_input_num);
        EditUtils utils = new EditUtils(0);
        utils.init(etQuestionContent, textInput);
        utils.init(personContent, personInputNum);

        TextView questionExample = (TextView) view.findViewById(R.id.question_example);
        questionExample.setOnClickListener(this);
        TextView accountExample = (TextView) view.findViewById(R.id.account_example);
        accountExample.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        AlertMsgDlg dlg;
        switch (v.getId()) {
            case R.id.question_example:
                dlg = new AlertMsgDlg(getActivity());
                dlg.initDlg(R.string.consult_title,R.string.test);
                dlg.show();
                break;
            case R.id.account_example:
                dlg = new AlertMsgDlg(getActivity());
                dlg.initDlg(R.string.consult_title,R.string.test);
                dlg.show();
                break;
        }
    }
}
