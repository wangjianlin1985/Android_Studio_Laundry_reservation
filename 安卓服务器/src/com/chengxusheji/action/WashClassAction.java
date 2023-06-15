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

    private int classId;
    public void setClassId(int classId) {
        this.classId = classId;
    }
    public int getClassId() {
        return classId;
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
    @Resource WashClassDAO washClassDAO;

    /*��������WashClass����*/
    private WashClass washClass;
    public void setWashClass(WashClass washClass) {
        this.washClass = washClass;
    }
    public WashClass getWashClass() {
        return this.washClass;
    }

    /*��ת�����WashClass��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*���WashClass��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddWashClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            washClassDAO.AddWashClass(washClass);
            ctx.put("message",  java.net.URLEncoder.encode("WashClass��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashClass���ʧ��!"));
            return "error";
        }
    }

    /*��ѯWashClass��Ϣ*/
    public String QueryWashClass() {
        if(currentPage == 0) currentPage = 1;
        List<WashClass> washClassList = washClassDAO.QueryWashClassInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        washClassDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = washClassDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = washClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("washClassList",  washClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryWashClassOutputToExcel() { 
        List<WashClass> washClassList = washClassDAO.QueryWashClassInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "WashClass��Ϣ��¼"; 
        String[] headers = { "����id","��������","��������"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"WashClass.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯWashClass��Ϣ*/
    public String FrontQueryWashClass() {
        if(currentPage == 0) currentPage = 1;
        List<WashClass> washClassList = washClassDAO.QueryWashClassInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        washClassDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = washClassDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = washClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("washClassList",  washClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�WashClass��Ϣ*/
    public String ModifyWashClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������classId��ȡWashClass����*/
        WashClass washClass = washClassDAO.GetWashClassByClassId(classId);

        ctx.put("washClass",  washClass);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�WashClass��Ϣ*/
    public String FrontShowWashClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������classId��ȡWashClass����*/
        WashClass washClass = washClassDAO.GetWashClassByClassId(classId);

        ctx.put("washClass",  washClass);
        return "front_show_view";
    }

    /*�����޸�WashClass��Ϣ*/
    public String ModifyWashClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            washClassDAO.UpdateWashClass(washClass);
            ctx.put("message",  java.net.URLEncoder.encode("WashClass��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashClass��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��WashClass��Ϣ*/
    public String DeleteWashClass() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            washClassDAO.DeleteWashClass(classId);
            ctx.put("message",  java.net.URLEncoder.encode("WashClassɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashClassɾ��ʧ��!"));
            return "error";
        }
    }

}
