package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.WashMeal;
public class WashMealListHandler extends DefaultHandler {
	private List<WashMeal> washMealList = null;
	private WashMeal washMeal;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (washMeal != null) { 
            String valueString = new String(ch, start, length); 
            if ("mealId".equals(tempString)) 
            	washMeal.setMealId(new Integer(valueString).intValue());
            else if ("mealName".equals(tempString)) 
            	washMeal.setMealName(valueString); 
            else if ("introduce".equals(tempString)) 
            	washMeal.setIntroduce(valueString); 
            else if ("price".equals(tempString)) 
            	washMeal.setPrice(new Float(valueString).floatValue());
            else if ("mealPhoto".equals(tempString)) 
            	washMeal.setMealPhoto(valueString); 
            else if ("publishDate".equals(tempString)) 
            	washMeal.setPublishDate(Timestamp.valueOf(valueString));
            else if ("washShopObj".equals(tempString)) 
            	washMeal.setWashShopObj(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("WashMeal".equals(localName)&&washMeal!=null){
			washMealList.add(washMeal);
			washMeal = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		washMealList = new ArrayList<WashMeal>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("WashMeal".equals(localName)) {
            washMeal = new WashMeal(); 
        }
        tempString = localName; 
	}

	public List<WashMeal> getWashMealList() {
		return this.washMealList;
	}
}
