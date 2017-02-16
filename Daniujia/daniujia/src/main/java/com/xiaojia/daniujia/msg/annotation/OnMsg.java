package com.xiaojia.daniujia.msg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnMsg {
	/**
	 * run on main thread
	 * @return
	 */
	boolean ui() default true;
	/**
	 * listening messages
	 * @return
	 */
	int[] msg();
	/**
	 * use last message
	 * @return
	 */
	boolean useLastMsg() default false;
}
