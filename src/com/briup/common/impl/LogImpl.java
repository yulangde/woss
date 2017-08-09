package com.briup.common.impl;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.briup.common.Configuration;
import com.briup.common.ConfigurationAWare;
import com.briup.common.Log;

public class LogImpl implements Log,ConfigurationAWare{
	private String log_properties;
	
	@Override
	public void init(Properties properties) {
		// TODO Auto-generated method stub
		log_properties=properties.getProperty("log-properties");
	}

	@Override
	public Logger getClientLogger() {
		//
		PropertyConfigurator.configure(log_properties);
		Logger client=Logger.getLogger("clientLogger");
		return client;
	}

	@Override
	public Logger getServerLogger() {
		// TODO Auto-generated method stub
		PropertyConfigurator.configure(log_properties);
		Logger server=Logger.getLogger("serverLogger");
		return server;
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		// TODO Auto-generated method stub
		
	}

}
