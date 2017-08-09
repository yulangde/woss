package com.briup.server.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Properties;

import com.briup.common.Configuration;
import com.briup.common.ConfigurationAWare;
import com.briup.common.Log;
import com.briup.common.impl.LogImpl;
import com.briup.main.ServerThread;
import com.briup.model.BIDR;
import com.briup.server.DBStore;
import com.briup.server.Server;

/**
 * Server接口提供了采集系统网络模块服务器端的标准。
 * Server的实现类需要实现与采集体统客户端进行交互，并调用DBStore将接收的数据持久化。
 * 当Server的实现类执行revicer()方法时，Server开始监听端口。
 * 当调用Server的shutdown方法时，Server将安全的停止服务。
 * @author yu
 * @version 1.0 2010-9-14
 *
 */
public class ServerImpl extends ServerThread implements Server ,ConfigurationAWare{
	//LogImpl logImpl = new LogImpl();
	private static Socket socket =null;
	private static ServerSocket ssocket =null;
//	DBStoreImpl dbstoreimple=new DBStoreImpl();
	private DBStore dbstore;
	private int port;
	private Log log;
	
	@Override
	public void init(Properties properties) {
		port =Integer.parseInt(properties.getProperty("port"));
	}
	
	public void run(){
		//super.run();
		try {
			revicer();
			//System.exit(-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 该方法用于启动这个Server服务，开始接收客户端传递的信息，并且调用DBStore将接收的数据持久化。
	 */
	@Override
	public void revicer() throws Exception {
		log.getServerLogger().info("开始入库"+"服务器接收端：");
		ssocket=new ServerSocket(port);
		while(true){
		System.out.println("service start.......");
		socket = ssocket.accept();	
		SocketAddress sa = socket.getRemoteSocketAddress();
		System.out.println("client: "+sa);
		log.getServerLogger().info("Client:"+sa);
		ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
		dbstore.saveToDB((Collection<BIDR>) ois.readObject());
		if(ois!=null)ois.close();
		if(socket != null) socket.close();
		}
	}

	/**
	 * 该方法用于使Server安全的停止运行。
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		log.getServerLogger().info("服务器入库结束");
		System.out.println("服务器已关闭");
		System.exit(-1);
		try {
		//	if(socket != null) socket.close();
			if(ssocket != null)
				ssocket.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		// TODO Auto-generated method stub
		log=configuration.getLogger();
		dbstore=configuration.getDBStore();
	}

}
