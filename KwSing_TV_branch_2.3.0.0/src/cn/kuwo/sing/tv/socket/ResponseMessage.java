/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.socket;

import java.io.Serializable;

/**
 * @Package cn.kuwo.sing.tv.bean
 * 
 * @Date Mar 18, 2014 9:41:01 AM
 *
 * @Author wangming
 *
 */
public class ResponseMessage implements Serializable {
	public int cmd;
	public int result;
	public long time;
	public Object data;
	@Override
	public String toString() {
		return "ResponseMessage [cmd=" + cmd + ", result=" + result + ", time="
				+ time + ", data=" + data + "]";
	}
}
