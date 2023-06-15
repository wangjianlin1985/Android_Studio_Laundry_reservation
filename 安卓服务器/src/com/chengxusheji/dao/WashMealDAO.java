package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.WashShop;
import com.chengxusheji.domain.WashMeal;

@Service @Transactional
public class WashMealDAO {

	@Resource SessionFactory factory;
    /*每页显示记录数目*/
    private final int PAGE_SIZE = 10;

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加洗衣套餐信息*/
    public void AddWashMeal(WashMeal washMeal) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(washMeal);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WashMeal> QueryWashMealInfo(String mealName,String publishDate,WashShop washShopObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From WashMeal washMeal where 1=1";
    	if(!mealName.equals("")) hql = hql + " and washMeal.mealName like '%" + mealName + "%'";
    	if(!publishDate.equals("")) hql = hql + " and washMeal.publishDate like '%" + publishDate + "%'";
    	if(null != washShopObj && !washShopObj.getShopUserName().equals("")) hql += " and washMeal.washShopObj.shopUserName='" + washShopObj.getShopUserName() + "'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List washMealList = q.list();
    	return (ArrayList<WashMeal>) washMealList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WashMeal> QueryWashMealInfo(String mealName,String publishDate,WashShop washShopObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From WashMeal washMeal where 1=1";
    	if(!mealName.equals("")) hql = hql + " and washMeal.mealName like '%" + mealName + "%'";
    	if(!publishDate.equals("")) hql = hql + " and washMeal.publishDate like '%" + publishDate + "%'";
    	if(null != washShopObj && !washShopObj.getShopUserName().equals("")) hql += " and washMeal.washShopObj.shopUserName='" + washShopObj.getShopUserName() + "'";
    	Query q = s.createQuery(hql);
    	List washMealList = q.list();
    	return (ArrayList<WashMeal>) washMealList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WashMeal> QueryAllWashMealInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From WashMeal";
        Query q = s.createQuery(hql);
        List washMealList = q.list();
        return (ArrayList<WashMeal>) washMealList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String mealName,String publishDate,WashShop washShopObj) {
        Session s = factory.getCurrentSession();
        String hql = "From WashMeal washMeal where 1=1";
        if(!mealName.equals("")) hql = hql + " and washMeal.mealName like '%" + mealName + "%'";
        if(!publishDate.equals("")) hql = hql + " and washMeal.publishDate like '%" + publishDate + "%'";
        if(null != washShopObj && !washShopObj.getShopUserName().equals("")) hql += " and washMeal.washShopObj.shopUserName='" + washShopObj.getShopUserName() + "'";
        Query q = s.createQuery(hql);
        List washMealList = q.list();
        recordNumber = washMealList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public WashMeal GetWashMealByMealId(int mealId) {
        Session s = factory.getCurrentSession();
        WashMeal washMeal = (WashMeal)s.get(WashMeal.class, mealId);
        return washMeal;
    }

    /*更新WashMeal信息*/
    public void UpdateWashMeal(WashMeal washMeal) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(washMeal);
    }

    /*删除WashMeal信息*/
    public void DeleteWashMeal (int mealId) throws Exception {
        Session s = factory.getCurrentSession();
        Object washMeal = s.load(WashMeal.class, mealId);
        s.delete(washMeal);
    }

}
