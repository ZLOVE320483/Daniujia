package com.xiaojia.daniujia.domain.resp;

import java.util.List;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.utils.SysUtil;

public class ServeVo {
	public int servprod_id;
	public int servicetype;// 咨询类型（1：图文 2：电话 3：线下）
	public String imgurl;
	public String name;
	public String position;
	public String company;
	public float price;
	public int assessmentcnt;
	public int servcnt;
	public float score;
	public List<GiftItem> gifts;
	public float balance;
	public int ques_id;
	public String quesdesc;
	
	public class GiftItem {
		public int id;
		public int gtype;
		public String title;
		public int value;
		public int deadtime;
		public float balance;
	}

	public String getServeTitle() {
		switch (servicetype) {
		case 1:
			return SysUtil.getString(R.string.graphics_consult);
		case 2:
			return SysUtil.getString(R.string.network_chat);
		case 3:
			return SysUtil.getString(R.string.offline_consult);
		}
		return null;
	}
	
	public String getServeUnit() {
		switch (servicetype) {
		case 1:
			return SysUtil.getString(R.string.per_cnt);
		case 2:
			return SysUtil.getString(R.string.per_h);
		case 3:
			return SysUtil.getString(R.string.per_m);
		}
		return null;
	}

}
