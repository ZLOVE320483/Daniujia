package com.xiaojia.daniujia.domain.resp;

import java.util.List;

public class IndustryVo {
	public int pagenum;
	public int perpagenum;
	public int totalnum;
	public List<IndustryItem> industrys;
	
	public class IndustryItem {
		public int id;
		public String name;
	}

}
