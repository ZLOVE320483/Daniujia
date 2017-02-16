package com.xiaojia.daniujia.domain.resp;

import java.util.List;

public class AccBalanceVo {
	public int totalnum;
	public int perpagenum;
	public int pagenum;
	public List<RecordItem> records;

	public class RecordItem {
		public String descrip;
		public int flag;// 1：支出 2：收入
		public float amount;// 此笔记录数额
		public float actbalance;// 当前账户总额
		public int recordtime;
	}

}
