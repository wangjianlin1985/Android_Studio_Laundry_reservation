package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.WashClass;
public class WashClassListHandler extends DefaultHandler {
	private List<WashClass> washClassList = null;
	private WashClass washClass;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (washClass != null) { 
            String valueString = new String(ch, start, length); 
            if ("classId".equals(tempString)) 
            	washClass.setClassId(new Integer(valueString).intValue());
            else if ("className".equals(tempString)) 
            	washClass.setClassName(valueString); 
            else if ("classDesc".equals(tempString)) 
            	washClass.setClassDesc(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("WashClass".equals(localName)&&washClass!=null){
			washClassList.add(washClass);
			washClass = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		washClassList = new ArrayList<WashClass>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("WashClass".equals(localName)) {
            washClass = new WashClass(); 
        }
        tempString = localName; 
	}

	public List<WashClass> getWashClassList() {
		return this.washClassList;
	}
}
