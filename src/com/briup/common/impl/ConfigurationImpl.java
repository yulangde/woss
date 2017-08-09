package com.briup.common.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.client.Client;
import com.briup.client.Gather;
import com.briup.common.Backup;
import com.briup.common.Configuration;
import com.briup.common.ConfigurationAWare;
import com.briup.common.Log;
import com.briup.common.WossModule;
import com.briup.server.DBStore;
import com.briup.server.Server;

/**
* Configuration接口提供了配置模块的规范。
* 配置模块通过某种配置方式将Logger、BackUP、Gather、Client、Server、DBStore等模块的实现类进行实例化，
* 并且将其所需要配置信息予以传递。通过配置模块可以获得各个模块的实例。
* 
* @author briup
* @version 1.0 2010-9-14
*/
public class ConfigurationImpl implements Configuration {
	private Backup backup;
	private Log log;
	private Client client;
	private Server server;
	private Gather gather;
	private DBStore dbstore;

	public ConfigurationImpl() {
		// TODO Auto-generated constructor stub
		this("src/config.xml");
	}
	
	public ConfigurationImpl(String path){
		SAXReader saxReader = new SAXReader();
		try {
			Document document=saxReader.read(path);
			Element rootElement =document.getRootElement();
			List<Element> chileElement =rootElement.elements();
			Map<String, WossModule> map= new HashMap<String, WossModule>();
			for (Element element : chileElement) {
				String classValue = element.attribute("class").getValue();
				WossModule wossModule = (WossModule)Class.forName(classValue).newInstance();//获取实例
				map.put(element.getName(), wossModule);
				List<Element> childchildElement = element.elements();
				Properties properties = new Properties();
				for (Element childelement : childchildElement) {
					String tagName = childelement.getName();
					String tagText = childelement.getTextTrim();
					properties.setProperty(tagName, tagText);
				}
				wossModule.init(properties);
			}
			if(map.containsKey("server")){
				server=(Server)map.get("server");
			}
			if(map.containsKey("client")){
				client=(Client)map.get("client");
			}
			if(map.containsKey("logger")){
				log=(Log)map.get("logger");
			}
			if(map.containsKey("dbStore")){
				dbstore=(DBStore)map.get("dbStore");
			}
			if(map.containsKey("backups")){
				backup=(Backup)map.get("backups");
			}
			if(map.containsKey("gather")){
				gather=(Gather)map.get("gather");
			}
			
			for (Object object : map.values()) {
				((ConfigurationAWare)object).setConfiguration(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public Log getLogger() {
		// TODO Auto-generated method stub
		return log;
	}

	@Override
	public Backup getBackup() {
		// TODO Auto-generated method stub
		return backup;
	}
	
	@Override
	public Gather getGather() {
		// TODO Auto-generated method stub
		return gather;
	}

	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return client;
	}

	@Override
	public Server getServer() {
		// TODO Auto-generated method stub
		return server;
	}

	@Override
	public DBStore getDBStore() {
		// TODO Auto-generated method stub
		return dbstore;
	}

/*	public static void main(String [] args){
		//new ConfigurationImpl();
		System.out.println(new ConfigurationImpl().getBackup());
	}*/
}
