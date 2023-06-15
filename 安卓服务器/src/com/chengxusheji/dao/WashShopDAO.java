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
import com.chengxusheji.domain.WashClass;
import com.chengxusheji.domain.WashShop;

@Service @Transactional
public class WashShopDAO {

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

    /*添加图书信息*/
    public void AddWashShop(WashShop washShop) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(washShop);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WashShop> QueryWashShopInfo(String shopUserName,String shopName,WashClass washClassObj,String telephone,String addDate,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From WashShop washShop where 1=1";
    	if(!shopUserName.equals("")) hql = hql + " and washShop.shopUserName like '%" + shopUserName + "%'";
    	if(!shopName.equals("")) hql = hql + " and washShop.shopName like '%" + shopName + "%'";
    	if(null != washClassObj && washClassObj.getClassId()!=0) hql += " and washShop.washClassObj.classId=" + washClassObj.getClassId();
    	if(!telephone.equals("")) hql = hql + " and washShop.telephone like '%" + telephone + "%'";
    	if(!addDate.equals("")) hql = hql + " and washShop.addDate like '%" + addDate + "%'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List washShopList = q.list();
    	return (ArrayList<WashShop>) washShopList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WashShop> QueryWashShopInfo(String shopUserName,String shopName,WashClass washClassObj,String telephone,String addDate) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From WashShop washShop where 1=1";
    	if(!shopUserName.equals("")) hql = hql + " and washShop.shopUserName like '%" + shopUserName + "%'";
    	if(!shopName.equals("")) hql = hql + " and washShop.shopName like '%" + shopName + "%'";
    	if(null != washClassObj && washClassObj.getClassId()!=0) hql += " and washShop.washClassObj.classId=" + washClassObj.getClassId();
    	if(!telephone.equals("")) hql = hql + " and washShop.telephone like '%" + telephone + "%'";
    	if(!addDate.equals("")) hql = hql + " and washShop.addDate like '%" + addDate + "%'";
    	Query q = s.createQuery(hql);
    	List washShopList = q.list();
    	return (ArrayList<WashShop>) washShopList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WashShop> QueryAllWashShopInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From WashShop";
        Query q = s.createQuery(hql);
        List washShopList = q.list();
        return (ArrayList<WashShop>) washShopList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String shopUserName,String shopName,WashClass washClassObj,String telephone,String addDate) {
        Session s = factory.getCurrentSession();
        String hql = "From WashShop washShop where 1=1";
        if(!shopUserName.equals("")) hql = hql + " and washShop.shopUserName like '%" + shopUserName + "%'";
        if(!shopName.equals("")) hql = hql + " and washShop.shopName like '%" + shopName + "%'";
        if(null != washClassObj && washClassObj.getClassId()!=0) hql += " and washShop.washClassObj.classId=" + washClassObj.getClassId();
        if(!telephone.equals("")) hql = hql + " and washShop.telephone like '%" + telephone + "%'";
        if(!addDate.equals("")) hql = hql + " and washShop.addDate like '%" + addDate + "%'";
        Query q = s.createQuery(hql);
        List washShopList = q.list();
        recordNumber = washShopList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public WashShop GetWashShopByShopUserName(String shopUserName) {
        Session s = factory.getCurrentSession();
        WashShop washShop = (WashShop)s.get(WashShop.class, shopUserName);
        return washShop;
    }

    /*更新WashShop信息*/
    public void UpdateWashShop(WashShop washShop) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(washShop);
    }

    /*删除WashShop信息*/
    public void DeleteWashShop (String shopUserName) throws Exception {
        Session s = factory.getCurrentSession();
        Object washShop = s.load(WashShop.class, shopUserName);
        s.delete(washShop);
    }

}
