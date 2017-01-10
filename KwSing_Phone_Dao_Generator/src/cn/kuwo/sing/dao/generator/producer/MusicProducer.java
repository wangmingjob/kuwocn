/**
 * Copyright (c) 2013, Kuwo.cn, Inc. All rights reserved. 
 */
package cn.kuwo.sing.dao.generator.producer;

import cn.kuwo.sing.dao.generator.DaoGenerator;
import cn.kuwo.sing.dao.generator.Entity;
import cn.kuwo.sing.dao.generator.Property;
import cn.kuwo.sing.dao.generator.Schema;
import cn.kuwo.sing.dao.generator.ToMany;

/**
 * @Package cn.kuwo.sing.dao.generator.producer
 * 
 * @Date 2013年10月29日 下午6:55:51
 * 
 * @author wangming
 * 
 */
public class MusicProducer {
	private static final String TARGET_PROJECT_PACKAGE_DAO = "cn.kuwo.sing.dao";
	public static void main(String[] args) throws Exception {
		/**
		 * <music rid="709174" name="思思念念全是你" artist="高安" album="高安原唱精选集" duration="248" 
		 * format="wma" hot="4"res="http://music3.9t9t.com/5/3406/7894/2074080343.wma" img=""/>
		 */
		Schema schema = new Schema(1, TARGET_PROJECT_PACKAGE_DAO);
		addMusic(schema);
		new DaoGenerator().generateAll(schema, "../KwSing_Phone/src-gen");//必须先建立src-gen目录，再运行此程序
	}
	
	private static void addMusic(Schema schema) {
		Entity music = schema.addEntity("Music");
		music.addStringProperty("rid"); //rid
		music.addStringProperty("name"); //name
		music.addStringProperty("artist"); //artist
		music.addStringProperty("album"); //album
		music.addStringProperty("duration"); //duration
		music.addStringProperty("format"); //format
		music.addStringProperty("hot"); //hot
		music.addStringProperty("res"); //res
		music.addStringProperty("imageUrl"); //imageUrl
		
	}

	private static void addNote(Schema schema) {
		Entity note = schema.addEntity("Note");
		note.addIdProperty();
		note.addStringProperty("text").notNull();
		note.addStringProperty("comment");
		note.addDateProperty("date");
	}

	private static void addCustomerOrder(Schema schema) {
		Entity customer = schema.addEntity("Customer");
		customer.addIdProperty();
		customer.addStringProperty("name").notNull();

		Entity order = schema.addEntity("Order");
		order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
		order.addIdProperty();
		Property orderDate = order.addDateProperty("date").getProperty();
		Property customerId = order.addLongProperty("customerId").notNull()
				.getProperty();
		order.addToOne(customer, customerId);

		ToMany customerToOrders = customer.addToMany(order, customerId);
		customerToOrders.setName("orders");
		customerToOrders.orderAsc(orderDate);
	}
}
