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
import com.chengxusheji.dao.WashClassDAO;
import com.chengxusheji.domain.WashClass;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class WashClassAction extends BaseAction {

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

    private int classId;
    public void setClassId(int classId) {
        this.classId = classId;
    }
    public int getClassId() {
        return classId;
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
    @Resource WashClassDAO washClassDAO;

    /*待操作的WashClass对象*/
    private WashClass washClass;
    public void setWashClass(WashClass washClass) {
        this.washClass = washClass;
    }
    public WashClass getWashClass() {
        return this.washClass;
    }

    /*跳转到添加WashClass视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*添加WashClass信息*/
    @SuppressWarnings("deprecation")
    public String AddWashClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            washClassDAO.AddWashClass(washClass);
            ctx.put("message",  java.net.URLEncoder.encode("WashClass添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashClass添加失败!"));
            return "error";
        }
    }

    /*查询WashClass信息*/
    public String QueryWashClass() {
        if(currentPage == 0) currentPage = 1;
        List<WashClass> washClassList = washClassDAO.QueryWashClassInfo(currentPage);
        /*计算总的页数和总的记录数*/
        washClassDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = washClassDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = washClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("washClassList",  washClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryWashClassOutputToExcel() { 
        List<WashClass> washClassList = washClassDAO.QueryWashClassInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "WashClass信息记录"; 
        String[] headers = { "种类id","种类名称","种类描述"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<washClassList.size();i++) {
        	WashClass washClass = washClassList.get(i); 
        	dataset.add(new String[]{washClass.getClassId() + "",washClass.getClassName(),washClass.getClassDesc()});
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
			response.setHeader("Content-disposition","attachment; filename="+"WashClass.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询WashClass信息*/
    public String FrontQueryWashClass() {
        if(currentPage == 0) currentPage = 1;
        List<WashClass> washClassList = washClassDAO.QueryWashClassInfo(currentPage);
        /*计算总的页数和总的记录数*/
        washClassDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = washClassDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = washClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("washClassList",  washClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*查询要修改的WashClass信息*/
    public String ModifyWashClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键classId获取WashClass对象*/
        WashClass washClass = washClassDAO.GetWashClassByClassId(classId);

        ctx.put("washClass",  washClass);
        return "modify_view";
    }

    /*查询要修改的WashClass信息*/
    public String FrontShowWashClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键classId获取WashClass对象*/
        WashClass washClass = washClassDAO.GetWashClassByClassId(classId);

        ctx.put("washClass",  washClass);
        return "front_show_view";
    }

    /*更新修改WashClass信息*/
    public String ModifyWashClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            washClassDAO.UpdateWashClass(washClass);
            ctx.put("message",  java.net.URLEncoder.encode("WashClass信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashClass信息更新失败!"));
            return "error";
       }
   }

    /*删除WashClass信息*/
    public String DeleteWashClass() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            washClassDAO.DeleteWashClass(classId);
            ctx.put("message",  java.net.URLEncoder.encode("WashClass删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashClass删除失败!"));
            return "error";
        }
    }

}
