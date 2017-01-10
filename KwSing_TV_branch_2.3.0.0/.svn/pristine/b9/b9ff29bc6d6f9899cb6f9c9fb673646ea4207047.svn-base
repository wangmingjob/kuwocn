/**
 * Copyright (c) 2013, FightingTime, Inc. All rights reserved. 
 */
package cn.kuwo.sing.tv.socket;

import java.io.Serializable;

import cn.kuwo.sing.tv.bean.Mtv;
import cn.kuwo.sing.tv.bean.UserMtv;

/**
 * @Package cn.kuwo.sing.tv.bean
 * 
 * @Date Mar 18, 2014 9:40:45 AM
 *
 * @Author wangming
 *
 */
public class RequestMessage implements Serializable {
	public int cmd; //add,play,toPlay,toPause,toPreMtv,toNextMtv,toSwitchOriginal,toSwitchAccomp
	public String extraMessage;//
	public Mtv data; //add,play(data!=null) 
	public UserMtv userData;//play
	public long time;
	@Override
	public String toString() {
		return "RequestMessage [cmd=" + cmd + ", extraMessage=" + extraMessage
				+ ", data=" + data + ", userData=" + userData + ", time="
				+ time + "]";
	}
}
