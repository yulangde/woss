package com.briup.main;

import com.briup.client.Client;
import com.briup.client.Gather;
import com.briup.common.Configuration;
import com.briup.common.impl.ConfigurationImpl;


/**
 * @author Chen
 * 客户端启动
 * */
public class ClientMain {
	public static void main(String[] args) throws Exception {
		Configuration configuration = new ConfigurationImpl();
		Client client = configuration.getClient();
		Gather gather = configuration.getGather();
		client.send(gather.gather());
	}
}
