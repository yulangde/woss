package com.briup.client.impl;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.client.Client;
import com.briup.common.Configuration;
import com.briup.common.ConfigurationAWare;
import com.briup.common.Log;
import com.briup.model.BIDR;

/**
 * DBStore提供了入库模块的规范。
 * 该接口的实现类将BIDR集合持久化。
 * @author briup
 * @version 1.0 2010-9-14
 */
public class ClientImpl implements Client,ConfigurationAWare{
	Socket socket =null;  //定义socket对象
	private Log log; //定义日志
	private String serverip; //定义ip
	private int port; //定义端口
	
	@Override
	public void init(Properties properties) {
		serverip=properties.getProperty("ip"); //获取ip
		port=Integer.parseInt(properties.getProperty("port")); //获取端口
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		log=configuration.getLogger(); //获取日志
	} 

	/**
	 * send方法用于与服务器端进行交互，发送BIDR集合至服务器端。
	 * @param collection
	 * 
	 */
	@Override
	public void send(Collection<BIDR> collection) throws Exception {
		log.getClientLogger().info("开始入库："+"客户端发送端：");
		System.out.println("Client ......");
	//	System.out.print("Please input ServerIP:");
	//	Scanner scan=new Scanner(System.in);
	//	String serverIp=scan.next();
	//	System.out.print("Please input ServerPort:");
	//	int port=scan.nextInt();
		socket =new Socket(serverip,port);//构建socket
		log.getClientLogger().info(serverip+"   "+port);
		ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(collection);
		if(oos!=null) oos.close();
		if(socket!=null) socket.close();
		log.getClientLogger().info("入库完成");
	}

	
}
