package com.briup.test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

public class WriteDom4J {
	/**
	 * 用dom4j写一个xml文档
	 * @throws IOException 
	 */
	@Test
	public void write() throws IOException{
		//1.构建document
		Document document =DocumentFactory.getInstance().createDocument();
		//2.添加根标签
		Element rootElement=document.addElement("student");
		for(int i=0;i<5;i++){
			//
			Element childElement=rootElement.addElement("student"+i);
			childElement.addAttribute("name","蒙奇.D.路飞"+i);
			childElement.addText("和道一文字"+i);
			//
			Element childchildElement=childElement.addElement("螺旋丸");
			childchildElement.addText("超大玉螺旋丸");
		
		}
		OutputFormat outputFormat=new OutputFormat();
		outputFormat.setNewlines(true);
		outputFormat.setIndent("\t");
		
		FileWriter writer =new FileWriter("src/student.xml");
		XMLWriter xmlWriter =new XMLWriter(writer,outputFormat);
		xmlWriter.write(document);
		xmlWriter.flush();
		xmlWriter.close();
		System.out.println("写完成");
	}
}
