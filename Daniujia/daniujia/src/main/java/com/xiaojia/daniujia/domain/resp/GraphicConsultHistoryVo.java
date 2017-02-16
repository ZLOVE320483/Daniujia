package com.xiaojia.daniujia.domain.resp;

import java.util.List;

public class GraphicConsultHistoryVo {
	public int totalnum;
	public int pagenum;
	public int perpagenum;
	public List<GraphicConsultHistItem> messageinfos;

	public class GraphicConsultHistItem {
		public int id;
		public int sender_id;
		public String username;
		public String name;
		public String imgurl;
		public String content;
		public int sendtime;
	}
}
