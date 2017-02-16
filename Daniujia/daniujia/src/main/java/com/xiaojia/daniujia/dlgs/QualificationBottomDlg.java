package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.QualificationItem;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.List;

public class QualificationBottomDlg extends BaseBottomDialog implements OnClickListener, OnItemClickListener {
	private ListView mListView;

	public QualificationBottomDlg(Activity context) {
		super(context, LayoutInflater.from(context).inflate(R.layout.dlg_bottom_scale, null));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView.findViewById(R.id.cancel).setOnClickListener(this);
		mListView = (ListView) mView.findViewById(R.id.list);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		TextView tv = (TextView) view;
		String scale = tv.getText().toString();
		int scaleId = (Integer) tv.getTag();
		if (mClickListener != null) {
			Bundle b = new Bundle();
			b.putString(ExtraConst.EXTRA_PROFILE_CMP_SCALE, scale);
			b.putInt(ExtraConst.EXTRA_PROFILE_CMP_SCALE_ID, scaleId);
			mClickListener.onOK(b);
		}
		dismiss();
	}

	public void show(List<QualificationItem> list) {
		final MyListAdapter adapter = new MyListAdapter(list);
		show();
		mListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		dismiss();
	}

	private class MyListAdapter extends BaseAdapter {
		private List<QualificationItem> list;

		private MyListAdapter(List<QualificationItem> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(mActivity);
			int padding = ScreenUtils.dip2px(8);
			tv.setPadding(padding, padding, padding, padding);
			tv.setTextColor(SysUtil.getColor(R.color.grey_darkest));
			QualificationItem item = list.get(position);
			tv.setText(item.name);
			tv.setTag(item.tagId);
			tv.setBackgroundResource(R.drawable.selector_white_grey);
			return tv;
		}
	}

}
