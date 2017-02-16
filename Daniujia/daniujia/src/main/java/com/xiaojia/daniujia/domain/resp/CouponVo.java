package com.xiaojia.daniujia.domain.resp;

import java.util.List;

public class CouponVo {
	public int totalnum;
	public int perpagenum;
	public int pagenum;
	public List<GiftItem> gifts;

	@Override
	public String toString() {
		return "CouponVo{" +
				"totalnum=" + totalnum +
				", perpagenum=" + perpagenum +
				", pagenum=" + pagenum +
				", gifts=" + gifts +
				'}';
	}
}
