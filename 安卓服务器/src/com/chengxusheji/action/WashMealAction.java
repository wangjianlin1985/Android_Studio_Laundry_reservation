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
import com.chengxusheji.dao.WashMealDAO;
import com.chengxusheji.domain.WashMeal;
import com.chengxusheji.dao.WashShopDAO;
import com.chengxusheji.domain.WashShop;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class WashMealAction extends BaseAction {

	/*ͼƬ���ļ��ֶ�mealPhoto��������*/
	private File mealPhotoFile;
	private String mealPhotoFileFileName;
	private String mealPhotoFileContentType;
	public File getMealPhotoFile() {
		return mealPhotoFile;
	}
	public void setMealPhotoFile(File mealPhotoFile) {
		this.mealPhotoFile = mealPhotoFile;
	}
	public String getMealPhotoFileFileName() {
		return mealPhotoFileFileName;
	}
	public void setMealPhotoFileFileName(String mealPhotoFileFileName) {
		this.mealPhotoFileFileName = mealPhotoFileFileName;
	}
	public String getMealPhotoFileContentType() {
		return mealPhotoFileContentType;
	}
	public void setMealPhotoFileContentType(String mealPhotoFileContentType) {
		this.mealPhotoFileContentType = mealPhotoFileContentType;
	}
    /*�������Ҫ��ѯ������: ϴ���ײ�*/
    private String mealName;
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }
    public String getMealName() {
        return this.mealName;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String publishDate;
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
    public String getPublishDate() {
        return this.publishDate;
    }

    /*�������Ҫ��ѯ������: ϴ�µ�*/
    private WashShop washShopObj;
    public void setWashShopObj(WashShop washShopObj) {
        this.washShopObj = washShopObj;
    }
    public WashShop getWashShopObj() {
        return this.washShopObj;
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

    private int mealId;
    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
    public int getMealId() {
        return mealId;
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
    @Resource WashShopDAO washShopDAO;
    @Resource WashMealDAO washMealDAO;

    /*��������WashMeal����*/
    private WashMeal washMeal;
    public void setWashMeal(WashMeal washMeal) {
        this.washMeal = washMeal;
    }
    public WashMeal getWashMeal() {
        return this.washMeal;
    }

    /*��ת�����WashMeal��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�WashShop��Ϣ*/
        List<WashShop> washShopList = washShopDAO.QueryAllWashShopInfo();
        ctx.put("washShopList", washShopList);
        return "add_view";
    }

    /*���WashMeal��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddWashMeal() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashShop washShopObj = washShopDAO.GetWashShopByShopUserName(washMeal.getWashShopObj().getShopUserName());
            washMeal.setWashShopObj(washShopObj);
            /*�����ײ�ͼƬ�ϴ�*/
            String mealPhotoPath = "upload/noimage.jpg"; 
       	 	if(mealPhotoFile != null)
       	 		mealPhotoPath = photoUpload(mealPhotoFile,mealPhotoFileContentType);
       	 	washMeal.setMealPhoto(mealPhotoPath);
            washMealDAO.AddWashMeal(washMeal);
            ctx.put("message",  java.net.URLEncoder.encode("WashMeal��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashMeal���ʧ��!"));
            return "error";
        }
    }

    /*��ѯWashMeal��Ϣ*/
    public String QueryWashMeal() {
        if(currentPage == 0) currentPage = 1;
        if(mealName == null) mealName = "";
        if(publishDate == null) publishDate = "";
        List<WashMeal> washMealList = washMealDAO.QueryWashMealInfo(mealName, publishDate, washShopObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        washMealDAO.CalculateTotalPageAndRecordNumber(mealName, publishDate, washShopObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = washMealDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = washMealDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("washMealList",  washMealList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("mealName", mealName);
        ctx.put("publishDate", publishDate);
        ctx.put("washShopObj", washShopObj);
        List<WashShop> washShopList = washShopDAO.QueryAllWashShopInfo();
        ctx.put("washShopList", washShopList);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryWashMealOutputToExcel() { 
        if(mealName == null) mealName = "";
        if(publishDate == null) publishDate = "";
        List<WashMeal> washMealList = washMealDAO.QueryWashMealInfo(mealName,publishDate,washShopObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "WashMeal��Ϣ��¼"; 
        String[] headers = { "�ײ�id","ϴ���ײ�","�ײͼ۸�","�ײ�ͼƬ","��������","ϴ�µ�"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<washMealList.size();i++) {
        	WashMeal washMeal = washMealList.get(i); 
        	dataset.add(new String[]{washMeal.getMealId() + "",washMeal.getMealName(),washMeal.getPrice() + "",washMeal.getMealPhoto(),new SimpleDateFormat("yyyy-MM-dd").format(washMeal.getPublishDate()),washMeal.getWashShopObj().getShopName()
});
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
			response.setHeader("Content-disposition","attachment; filename="+"WashMeal.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯWashMeal��Ϣ*/
    public String FrontQueryWashMeal() {
        if(currentPage == 0) currentPage = 1;
        if(mealName == null) mealName = "";
        if(publishDate == null) publishDate = "";
        List<WashMeal> washMealList = washMealDAO.QueryWashMealInfo(mealName, publishDate, washShopObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        washMealDAO.CalculateTotalPageAndRecordNumber(mealName, publishDate, washShopObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = washMealDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = washMealDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("washMealList",  washMealList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("mealName", mealName);
        ctx.put("publishDate", publishDate);
        ctx.put("washShopObj", washShopObj);
        List<WashShop> washShopList = washShopDAO.QueryAllWashShopInfo();
        ctx.put("washShopList", washShopList);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�WashMeal��Ϣ*/
    public String ModifyWashMealQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������mealId��ȡWashMeal����*/
        WashMeal washMeal = washMealDAO.GetWashMealByMealId(mealId);

        List<WashShop> washShopList = washShopDAO.QueryAllWashShopInfo();
        ctx.put("washShopList", washShopList);
        ctx.put("washMeal",  washMeal);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�WashMeal��Ϣ*/
    public String FrontShowWashMealQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������mealId��ȡWashMeal����*/
        WashMeal washMeal = washMealDAO.GetWashMealByMealId(mealId);

        List<WashShop> washShopList = washShopDAO.QueryAllWashShopInfo();
        ctx.put("washShopList", washShopList);
        ctx.put("washMeal",  washMeal);
        return "front_show_view";
    }

    /*�����޸�WashMeal��Ϣ*/
    public String ModifyWashMeal() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashShop washShopObj = washShopDAO.GetWashShopByShopUserName(washMeal.getWashShopObj().getShopUserName());
            washMeal.setWashShopObj(washShopObj);
            /*�����ײ�ͼƬ�ϴ�*/
            if(mealPhotoFile != null) {
            	String mealPhotoPath = photoUpload(mealPhotoFile,mealPhotoFileContentType);
            	washMeal.setMealPhoto(mealPhotoPath);
            }
            washMealDAO.UpdateWashMeal(washMeal);
            ctx.put("message",  java.net.URLEncoder.encode("WashMeal��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashMeal��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��WashMeal��Ϣ*/
    public String DeleteWashMeal() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            washMealDAO.DeleteWashMeal(mealId);
            ctx.put("message",  java.net.URLEncoder.encode("WashMealɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashMealɾ��ʧ��!"));
            return "error";
        }
    }

}
