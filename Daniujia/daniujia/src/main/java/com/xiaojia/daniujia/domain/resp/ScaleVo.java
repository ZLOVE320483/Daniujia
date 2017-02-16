package com.xiaojia.daniujia.domain.resp;

import java.util.List;

public class ScaleVo {
	public int pagenum;
	public int perpagenum;
	public int totalnum;
	public List<ScaleItem> scales;
	
	public class ScaleItem {
		public int id;
		public String title;
	}

}
