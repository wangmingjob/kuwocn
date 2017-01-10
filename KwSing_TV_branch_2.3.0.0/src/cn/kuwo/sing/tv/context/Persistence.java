package cn.kuwo.sing.tv.context;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Stack;

import cn.kuwo.sing.tv.bean.Mtv;

/**
 * 持久化参数
 * 
 */
public class Persistence implements Serializable {
	private static final long serialVersionUID = -3631106448346902629L;
	
	public LinkedList<Mtv> orderLinkedList;
	
	/**
	 * 搜索历史记录
	 */
	public Stack<String> searchHistory = null;
	
	/**
	 * 设置快捷方式
	 */
	public boolean hasCreatedShortcut = false;
}
