package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.WashShop;
public class WashShopListHandler extends DefaultHandler {
	private List<WashShop> washShopList = null;
	private WashShop washShop;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (washShop != null) { 
            String valueString = new String(ch, start, length); 
            if ("shopUserName".equals(tempString)) 
            	washShop.setShopUserName(valueString); 
            else if ("password".equals(tempString)) 
            	washShop.setPassword(valueString); 
            else if ("shopName".equals(tempString)) 
            	washShop.setShopName(valueString); 
            else if ("washClassObj".equals(tempString)) 
            	washShop.setWashClassObj(new Integer(valueString).intValue());
            else if ("shopPhoto".equals(tempString)) 
            	washShop.setShopPhoto(valueString); 
            else if ("telephone".equals(tempString)) 
            	washShop.setTelephone(valueString); 
            else if ("addDate".equals(tempString)) 
            	washShop.setAddDate(Timestamp.valueOf(valueString));
            else if ("address".equals(tempString)) 
            	washShop.setAddress(valueString); 
            else if ("latitude".equals(tempString)) 
            	washShop.setLatitude(new Float(valueString).floatValue());
            else if ("longitude".equals(tempString)) 
            	washShop.setLongitude(new Float(valueString).floatValue());
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("WashShop".equals(localName)&&washShop!=null){
			washShopList.add(washShop);
			washShop = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		washShopList = new ArrayList<WashShop>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("WashShop".equals(localName)) {
            washShop = new WashShop(); 
        }
        tempString = localName; 
	}

	public List<WashShop> getWashShopList() {
		return this.washShopList;
	}
}
