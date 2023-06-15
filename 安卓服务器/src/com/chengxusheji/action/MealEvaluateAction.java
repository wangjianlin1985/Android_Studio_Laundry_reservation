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

    /*界面层需要查询的属性: 被评套餐*/
    private WashMeal washMealObj;
    public void setWashMealObj(WashMeal washMealObj) {
        this.washMealObj = washMealObj;
    }
    public WashMeal getWashMealObj() {
        return this.washMealObj;
    }

    /*界面层需要查询的属性: 评价用户*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*界面层需要查询的属性: 评价时间*/
    private String evaluateTime;
    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }
    public String getEvaluateTime() {
        return this.evaluateTime;
    }

    /*当前第几页*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*一共多少页*/
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

    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource WashMealDAO washMealDAO;
    @Resource UserInfoDAO userInfoDAO;
    @Resource MealEvaluateDAO mealEvaluateDAO;

    /*待操作的MealEvaluate对象*/
    private MealEvaluate mealEvaluate;
    public void setMealEvaluate(MealEvaluate mealEvaluate) {
        this.mealEvaluate = mealEvaluate;
    }
    public MealEvaluate getMealEvaluate() {
        return this.mealEvaluate;
    }

    /*跳转到添加MealEvaluate视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的WashMeal信息*/
        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        /*查询所有的UserInfo信息*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*添加MealEvaluate信息*/
    @SuppressWarnings("deprecation")
    public String AddMealEvaluate() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashMeal washMealObj = washMealDAO.GetWashMealByMealId(mealEvaluate.getWashMealObj().getMealId());
            mealEvaluate.setWashMealObj(washMealObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(mealEvaluate.getUserObj().getUser_name());
            mealEvaluate.setUserObj(userObj);
            mealEvaluateDAO.AddMealEvaluate(mealEvaluate);
            ctx.put("message",  java.net.URLEncoder.encode("MealEvaluate添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("MealEvaluate添加失败!"));
            return "error";
        }
    }

    /*查询MealEvaluate信息*/
    public String QueryMealEvaluate() {
        if(currentPage == 0) currentPage = 1;
        if(evaluateTime == null) evaluateTime = "";
        List<MealEvaluate> mealEvaluateList = mealEvaluateDAO.QueryMealEvaluateInfo(washMealObj, userObj, evaluateTime, currentPage);
        /*计算总的页数和总的记录数*/
        mealEvaluateDAO.CalculateTotalPageAndRecordNumber(washMealObj, userObj, evaluateTime);
        /*获取到总的页码数目*/
        totalPage = mealEvaluateDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryMealEvaluateOutputToExcel() { 
        if(evaluateTime == null) evaluateTime = "";
        List<MealEvaluate> mealEvaluateList = mealEvaluateDAO.QueryMealEvaluateInfo(washMealObj,userObj,evaluateTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "MealEvaluate信息记录"; 
        String[] headers = { "评价id","被评套餐","评价内容","评价用户","评价时间"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"MealEvaluate.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
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
    /*前台查询MealEvaluate信息*/
    public String FrontQueryMealEvaluate() {
        if(currentPage == 0) currentPage = 1;
        if(evaluateTime == null) evaluateTime = "";
        List<MealEvaluate> mealEvaluateList = mealEvaluateDAO.QueryMealEvaluateInfo(washMealObj, userObj, evaluateTime, currentPage);
        /*计算总的页数和总的记录数*/
        mealEvaluateDAO.CalculateTotalPageAndRecordNumber(washMealObj, userObj, evaluateTime);
        /*获取到总的页码数目*/
        totalPage = mealEvaluateDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的MealEvaluate信息*/
    public String ModifyMealEvaluateQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键evaluateId获取MealEvaluate对象*/
        MealEvaluate mealEvaluate = mealEvaluateDAO.GetMealEvaluateByEvaluateId(evaluateId);

        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("mealEvaluate",  mealEvaluate);
        return "modify_view";
    }

    /*查询要修改的MealEvaluate信息*/
    public String FrontShowMealEvaluateQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键evaluateId获取MealEvaluate对象*/
        MealEvaluate mealEvaluate = mealEvaluateDAO.GetMealEvaluateByEvaluateId(evaluateId);

        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("mealEvaluate",  mealEvaluate);
        return "front_show_view";
    }

    /*更新修改MealEvaluate信息*/
    public String ModifyMealEvaluate() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashMeal washMealObj = washMealDAO.GetWashMealByMealId(mealEvaluate.getWashMealObj().getMealId());
            mealEvaluate.setWashMealObj(washMealObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(mealEvaluate.getUserObj().getUser_name());
            mealEvaluate.setUserObj(userObj);
            mealEvaluateDAO.UpdateMealEvaluate(mealEvaluate);
            ctx.put("message",  java.net.URLEncoder.encode("MealEvaluate信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("MealEvaluate信息更新失败!"));
            return "error";
       }
   }

    /*删除MealEvaluate信息*/
    public String DeleteMealEvaluate() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            mealEvaluateDAO.DeleteMealEvaluate(evaluateId);
            ctx.put("message",  java.net.URLEncoder.encode("MealEvaluate删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("MealEvaluate删除失败!"));
            return "error";
        }
    }

}
