package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.MealEvaluate;
public class MealEvaluateListHandler extends DefaultHandler {
	private List<MealEvaluate> mealEvaluateList = null;
	private MealEvaluate mealEvaluate;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (mealEvaluate != null) { 
            String valueString = new String(ch, start, length); 
            if ("evaluateId".equals(tempString)) 
            	mealEvaluate.setEvaluateId(new Integer(valueString).intValue());
            else if ("washMealObj".equals(tempString)) 
            	mealEvaluate.setWashMealObj(new Integer(valueString).intValue());
            else if ("evaluateContent".equals(tempString)) 
            	mealEvaluate.setEvaluateContent(valueString); 
            else if ("userObj".equals(tempString)) 
            	mealEvaluate.setUserObj(valueString); 
            else if ("evaluateTime".equals(tempString)) 
            	mealEvaluate.setEvaluateTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("MealEvaluate".equals(localName)&&mealEvaluate!=null){
			mealEvaluateList.add(mealEvaluate);
			mealEvaluate = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		mealEvaluateList = new ArrayList<MealEvaluate>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("MealEvaluate".equals(localName)) {
            mealEvaluate = new MealEvaluate(); 
        }
        tempString = localName; 
	}

	public List<MealEvaluate> getMealEvaluateList() {
		return this.mealEvaluateList;
	}
}
