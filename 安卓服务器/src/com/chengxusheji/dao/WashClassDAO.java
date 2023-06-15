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

@Service @Transactional
public class WashClassDAO {

	@Resource SessionFactory factory;
    /*ÿҳ��ʾ��¼��Ŀ*/
    private final int PAGE_SIZE = 10;

    /*�����ѯ���ܵ�ҳ��*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*�����ѯ�����ܼ�¼��*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*���ͼ����Ϣ*/
    public void AddWashClass(WashClass washClass) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(washClass);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WashClass> QueryWashClassInfo(int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From WashClass washClass where 1=1";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List washClassList = q.list();
    	return (ArrayList<WashClass>) washClassList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WashClass> QueryWashClassInfo() { 
    	Session s = factory.getCurrentSession();
    	String hql = "From WashClass washClass where 1=1";
    	Query q = s.createQuery(hql);
    	List washClassList = q.list();
    	return (ArrayList<WashClass>) washClassList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<WashClass> QueryAllWashClassInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From WashClass";
        Query q = s.createQuery(hql);
        List washClassList = q.list();
        return (ArrayList<WashClass>) washClassList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber() {
        Session s = factory.getCurrentSession();
        String hql = "From WashClass washClass where 1=1";
        Query q = s.createQuery(hql);
        List washClassList = q.list();
        recordNumber = washClassList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public WashClass GetWashClassByClassId(int classId) {
        Session s = factory.getCurrentSession();
        WashClass washClass = (WashClass)s.get(WashClass.class, classId);
        return washClass;
    }

    /*����WashClass��Ϣ*/
    public void UpdateWashClass(WashClass washClass) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(washClass);
    }

    /*ɾ��WashClass��Ϣ*/
    public void DeleteWashClass (int classId) throws Exception {
        Session s = factory.getCurrentSession();
        Object washClass = s.load(WashClass.class, classId);
        s.delete(washClass);
    }

}
