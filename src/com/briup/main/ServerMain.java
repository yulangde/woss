package com.briup.main;

import java.util.Scanner;

import com.briup.common.Configuration;
import com.briup.common.impl.ConfigurationImpl;
import com.briup.server.Server;


/**
 * @author 
 * 服务器启动
 * */
public class ServerMain {
	public static void main(String[] args) throws Exception {
		Configuration configuration = new ConfigurationImpl();
		Server server=configuration.getServer();
		((Thread) server).start();
		System.out.println("是否关闭服务器");
		if((new Scanner(System.in).next()).equals("yes")){
			server.shutdown();
		}
	}	
}
