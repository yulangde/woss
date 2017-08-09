package com.briup.test;

import java.beans.Transient;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

public class HelloWorld {
	@Test
	public void simple() throws Exception{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String string=format.format(date);
		//2016_11_17 00:00:00
	//	System.out.println(string);
	//	Date parse=format.parse(string);
	//	System.out.println(parse);
		//System.out.println(new Date());
		//2016-11-17 00:00:00
		string = string +" 00:00:00";
	//	System.out.println(string);
		Timestamp timestamp = Timestamp.valueOf(string);
		System.out.println(timestamp);
		long i=Long.parseLong("1285376771000");
		System.out.println(new Date(i));
		//yyyy-mm-dd
	}
	
	@Test
	public void logger(){
		//加载日志配置文件
		PropertyConfigurator.configure("src/log4j.properties");
		Logger logger=Logger.getLogger("clientLogger");
		logger.info("开始入库");
		//java-code
	//	logger.info("异常信息e.");
		logger.info("入库完成"); 
		
	}
}
