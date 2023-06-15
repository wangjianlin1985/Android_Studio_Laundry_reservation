package com.chengxusheji.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionContext;
import com.chengxusheji.dao.MealEvaluateDAO;
import com.chengxusheji.domain.MealEvaluate;
import com.chengxusheji.dao.WashMealDAO;
import com.chengxusheji.domain.WashMeal;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class MealEvaluateAction extends BaseAction {

    /*�������Ҫ��ѯ������: �����ײ�*/
    private WashMeal washMealObj;
    public void setWashMealObj(WashMeal washMealObj) {
        this.washMealObj = washMealObj;
    }
    public WashMeal getWashMealObj() {
        return this.washMealObj;
    }

    /*�������Ҫ��ѯ������: �����û�*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*�������Ҫ��ѯ������: ����ʱ��*/
    private String evaluateTime;
    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }
    public String getEvaluateTime() {
        return this.evaluateTime;
    }

    /*��ǰ�ڼ�ҳ*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*һ������ҳ*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    private int evaluateId;
    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
    }
    public int getEvaluateId() {
        return evaluateId;
    }

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource WashMealDAO washMealDAO;
    @Resource UserInfoDAO userInfoDAO;
    @Resource MealEvaluateDAO mealEvaluateDAO;

    /*��������MealEvaluate����*/
    private MealEvaluate mealEvaluate;
    public void setMealEvaluate(MealEvaluate mealEvaluate) {
        this.mealEvaluate = mealEvaluate;
    }
    public MealEvaluate getMealEvaluate() {
        return this.mealEvaluate;
    }

    /*��ת�����MealEvaluate��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�WashMeal��Ϣ*/
        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        /*��ѯ���е�UserInfo��Ϣ*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*���MealEvaluate��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddMealEvaluate() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashMeal washMealObj = washMealDAO.GetWashMealByMealId(mealEvaluate.getWashMealObj().getMealId());
            mealEvaluate.setWashMealObj(washMealObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(mealEvaluate.getUserObj().getUser_name());
            mealEvaluate.setUserObj(userObj);
            mealEvaluateDAO.AddMealEvaluate(mealEvaluate);
            ctx.put("message",  java.net.URLEncoder.encode("MealEvaluate��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("MealEvaluate���ʧ��!"));
            return "error";
        }
    }

    /*��ѯMealEvaluate��Ϣ*/
    public String QueryMealEvaluate() {
        if(currentPage == 0) currentPage = 1;
        if(evaluateTime == null) evaluateTime = "";
        List<MealEvaluate> mealEvaluateList = mealEvaluateDAO.QueryMealEvaluateInfo(washMealObj, userObj, evaluateTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        mealEvaluateDAO.CalculateTotalPageAndRecordNumber(washMealObj, userObj, evaluateTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = mealEvaluateDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = mealEvaluateDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("mealEvaluateList",  mealEvaluateList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("washMealObj", washMealObj);
        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("evaluateTime", evaluateTime);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryMealEvaluateOutputToExcel() { 
        if(evaluateTime == null) evaluateTime = "";
        List<MealEvaluate> mealEvaluateList = mealEvaluateDAO.QueryMealEvaluateInfo(washMealObj,userObj,evaluateTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "MealEvaluate��Ϣ��¼"; 
        String[] headers = { "����id","�����ײ�","��������","�����û�","����ʱ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<mealEvaluateList.size();i++) {
        	MealEvaluate mealEvaluate = mealEvaluateList.get(i); 
        	dataset.add(new String[]{mealEvaluate.getEvaluateId() + "",mealEvaluate.getWashMealObj().getMealName(),
mealEvaluate.getEvaluateContent(),mealEvaluate.getUserObj().getName(),
mealEvaluate.getEvaluateTime()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"MealEvaluate.xls");//filename�����ص�xls���������������Ӣ�� 
			response.setContentType("application/msexcel;charset=UTF-8");//�������� 
			response.setHeader("Pragma","No-cache");//����ͷ 
			response.setHeader("Cache-Control","no-cache");//����ͷ 
			response.setDateHeader("Expires", 0);//��������ͷ  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
    /*ǰ̨��ѯMealEvaluate��Ϣ*/
    public String FrontQueryMealEvaluate() {
        if(currentPage == 0) currentPage = 1;
        if(evaluateTime == null) evaluateTime = "";
        List<MealEvaluate> mealEvaluateList = mealEvaluateDAO.QueryMealEvaluateInfo(washMealObj, userObj, evaluateTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        mealEvaluateDAO.CalculateTotalPageAndRecordNumber(washMealObj, userObj, evaluateTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = mealEvaluateDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = mealEvaluateDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("mealEvaluateList",  mealEvaluateList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("washMealObj", washMealObj);
        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("evaluateTime", evaluateTime);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�MealEvaluate��Ϣ*/
    public String ModifyMealEvaluateQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������evaluateId��ȡMealEvaluate����*/
        MealEvaluate mealEvaluate = mealEvaluateDAO.GetMealEvaluateByEvaluateId(evaluateId);

        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("mealEvaluate",  mealEvaluate);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�MealEvaluate��Ϣ*/
    public String FrontShowMealEvaluateQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������evaluateId��ȡMealEvaluate����*/
        MealEvaluate mealEvaluate = mealEvaluateDAO.GetMealEvaluateByEvaluateId(evaluateId);

        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("mealEvaluate",  mealEvaluate);
        return "front_show_view";
    }

    /*�����޸�MealEvaluate��Ϣ*/
    public String ModifyMealEvaluate() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashMeal washMealObj = washMealDAO.GetWashMealByMealId(mealEvaluate.getWashMealObj().getMealId());
            mealEvaluate.setWashMealObj(washMealObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(mealEvaluate.getUserObj().getUser_name());
            mealEvaluate.setUserObj(userObj);
            mealEvaluateDAO.UpdateMealEvaluate(mealEvaluate);
            ctx.put("message",  java.net.URLEncoder.encode("MealEvaluate��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("MealEvaluate��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��MealEvaluate��Ϣ*/
    public String DeleteMealEvaluate() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            mealEvaluateDAO.DeleteMealEvaluate(evaluateId);
            ctx.put("message",  java.net.URLEncoder.encode("MealEvaluateɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("MealEvaluateɾ��ʧ��!"));
            return "error";
        }
    }

}
