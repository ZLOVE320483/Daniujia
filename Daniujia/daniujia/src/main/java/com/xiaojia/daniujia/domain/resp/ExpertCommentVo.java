package com.xiaojia.daniujia.domain.resp;

import java.util.List;

public class ExpertCommentVo {
	public int totalnum;
	public int perpagenum;
	public int pagenum;
	public List<CommentItem> assessments;

	public class CommentItem {
		public int userid;
		public String name;
		public String username;
		public String imgurl;
		public int identity;
		public int assesstime;
		public String totalprice;
		public int servcnt;
		public int servicetime;
		public int score;
		public String assessment;
		
		@Override
		public String toString() {
			return "CommentItem [userid=" + userid + ", name=" + name + ", username=" + username + ", imgurl=" + imgurl
					+ ", identity=" + identity + ", assesstime=" + assesstime + ", totalprice=" + totalprice
					+ ", servcnt=" + servcnt + ", servicetime=" + servicetime + ", score=" + score + ", assessment="
					+ assessment + "]";
		}
	}

}
