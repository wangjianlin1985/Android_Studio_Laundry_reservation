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
import com.chengxusheji.dao.WashShopDAO;
import com.chengxusheji.domain.WashShop;
import com.chengxusheji.dao.WashClassDAO;
import com.chengxusheji.domain.WashClass;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class WashShopAction extends BaseAction {

	/*ͼƬ���ļ��ֶ�shopPhoto��������*/
	private File shopPhotoFile;
	private String shopPhotoFileFileName;
	private String shopPhotoFileContentType;
	public File getShopPhotoFile() {
		return shopPhotoFile;
	}
	public void setShopPhotoFile(File shopPhotoFile) {
		this.shopPhotoFile = shopPhotoFile;
	}
	public String getShopPhotoFileFileName() {
		return shopPhotoFileFileName;
	}
	public void setShopPhotoFileFileName(String shopPhotoFileFileName) {
		this.shopPhotoFileFileName = shopPhotoFileFileName;
	}
	public String getShopPhotoFileContentType() {
		return shopPhotoFileContentType;
	}
	public void setShopPhotoFileContentType(String shopPhotoFileContentType) {
		this.shopPhotoFileContentType = shopPhotoFileContentType;
	}
    /*�������Ҫ��ѯ������: ϴ�µ��˺�*/
    private String shopUserName;
    public void setShopUserName(String shopUserName) {
        this.shopUserName = shopUserName;
    }
    public String getShopUserName() {
        return this.shopUserName;
    }

    /*�������Ҫ��ѯ������: ϴ�µ�����*/
    private String shopName;
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getShopName() {
        return this.shopName;
    }

    /*�������Ҫ��ѯ������: ϴ�µ�����*/
    private WashClass washClassObj;
    public void setWashClassObj(WashClass washClassObj) {
        this.washClassObj = washClassObj;
    }
    public WashClass getWashClassObj() {
        return this.washClassObj;
    }

    /*�������Ҫ��ѯ������: ��ҵ绰*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
    }

    /*�������Ҫ��ѯ������: ��פ����*/
    private String addDate;
    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }
    public String getAddDate() {
        return this.addDate;
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
    @Resource WashShopDAO washShopDAO;

    /*��������WashShop����*/
    private WashShop washShop;
    public void setWashShop(WashShop washShop) {
        this.washShop = washShop;
    }
    public WashShop getWashShop() {
        return this.washShop;
    }

    /*��ת�����WashShop��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�WashClass��Ϣ*/
        List<WashClass> washClassList = washClassDAO.QueryAllWashClassInfo();
        ctx.put("washClassList", washClassList);
        return "add_view";
    }

    /*���WashShop��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddWashShop() {
        ActionContext ctx = ActionContext.getContext();
        /*��֤ϴ�µ��˺��Ƿ��Ѿ�����*/
        String shopUserName = washShop.getShopUserName();
        WashShop db_washShop = washShopDAO.GetWashShopByShopUserName(shopUserName);
        if(null != db_washShop) {
            ctx.put("error",  java.net.URLEncoder.encode("��ϴ�µ��˺��Ѿ�����!"));
            return "error";
        }
        try {
            WashClass washClassObj = washClassDAO.GetWashClassByClassId(washShop.getWashClassObj().getClassId());
            washShop.setWashClassObj(washClassObj);
            /*����ϴ�µ���Ƭ�ϴ�*/
            String shopPhotoPath = "upload/noimage.jpg"; 
       	 	if(shopPhotoFile != null)
       	 		shopPhotoPath = photoUpload(shopPhotoFile,shopPhotoFileContentType);
       	 	washShop.setShopPhoto(shopPhotoPath);
            washShopDAO.AddWashShop(washShop);
            ctx.put("message",  java.net.URLEncoder.encode("WashShop��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashShop���ʧ��!"));
            return "error";
        }
    }

    /*��ѯWashShop��Ϣ*/
    public String QueryWashShop() {
        if(currentPage == 0) currentPage = 1;
        if(shopUserName == null) shopUserName = "";
        if(shopName == null) shopName = "";
        if(telephone == null) telephone = "";
        if(addDate == null) addDate = "";
        List<WashShop> washShopList = washShopDAO.QueryWashShopInfo(shopUserName, shopName, washClassObj, telephone, addDate, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        washShopDAO.CalculateTotalPageAndRecordNumber(shopUserName, shopName, washClassObj, telephone, addDate);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = washShopDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = washShopDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("washShopList",  washShopList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("shopUserName", shopUserName);
        ctx.put("shopName", shopName);
        ctx.put("washClassObj", washClassObj);
        List<WashClass> washClassList = washClassDAO.QueryAllWashClassInfo();
        ctx.put("washClassList", washClassList);
        ctx.put("telephone", telephone);
        ctx.put("addDate", addDate);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryWashShopOutputToExcel() { 
        if(shopUserName == null) shopUserName = "";
        if(shopName == null) shopName = "";
        if(telephone == null) telephone = "";
        if(addDate == null) addDate = "";
        List<WashShop> washShopList = washShopDAO.QueryWashShopInfo(shopUserName,shopName,washClassObj,telephone,addDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "WashShop��Ϣ��¼"; 
        String[] headers = { "ϴ�µ��˺�","ϴ�µ�����","ϴ�µ�����","ϴ�µ���Ƭ","��ҵ绰","��פ����"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<washShopList.size();i++) {
        	WashShop washShop = washShopList.get(i); 
        	dataset.add(new String[]{washShop.getShopUserName(),washShop.getShopName(),washShop.getWashClassObj().getClassName(),
washShop.getShopPhoto(),washShop.getTelephone(),new SimpleDateFormat("yyyy-MM-dd").format(washShop.getAddDate())});
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
			response.setHeader("Content-disposition","attachment; filename="+"WashShop.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯWashShop��Ϣ*/
    public String FrontQueryWashShop() {
        if(currentPage == 0) currentPage = 1;
        if(shopUserName == null) shopUserName = "";
        if(shopName == null) shopName = "";
        if(telephone == null) telephone = "";
        if(addDate == null) addDate = "";
        List<WashShop> washShopList = washShopDAO.QueryWashShopInfo(shopUserName, shopName, washClassObj, telephone, addDate, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        washShopDAO.CalculateTotalPageAndRecordNumber(shopUserName, shopName, washClassObj, telephone, addDate);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = washShopDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = washShopDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("washShopList",  washShopList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("shopUserName", shopUserName);
        ctx.put("shopName", shopName);
        ctx.put("washClassObj", washClassObj);
        List<WashClass> washClassList = washClassDAO.QueryAllWashClassInfo();
        ctx.put("washClassList", washClassList);
        ctx.put("telephone", telephone);
        ctx.put("addDate", addDate);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�WashShop��Ϣ*/
    public String ModifyWashShopQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������shopUserName��ȡWashShop����*/
        WashShop washShop = washShopDAO.GetWashShopByShopUserName(shopUserName);

        List<WashClass> washClassList = washClassDAO.QueryAllWashClassInfo();
        ctx.put("washClassList", washClassList);
        ctx.put("washShop",  washShop);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�WashShop��Ϣ*/
    public String FrontShowWashShopQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������shopUserName��ȡWashShop����*/
        WashShop washShop = washShopDAO.GetWashShopByShopUserName(shopUserName);

        List<WashClass> washClassList = washClassDAO.QueryAllWashClassInfo();
        ctx.put("washClassList", washClassList);
        ctx.put("washShop",  washShop);
        return "front_show_view";
    }

    /*�����޸�WashShop��Ϣ*/
    public String ModifyWashShop() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashClass washClassObj = washClassDAO.GetWashClassByClassId(washShop.getWashClassObj().getClassId());
            washShop.setWashClassObj(washClassObj);
            /*����ϴ�µ���Ƭ�ϴ�*/
            if(shopPhotoFile != null) {
            	String shopPhotoPath = photoUpload(shopPhotoFile,shopPhotoFileContentType);
            	washShop.setShopPhoto(shopPhotoPath);
            }
            washShopDAO.UpdateWashShop(washShop);
            ctx.put("message",  java.net.URLEncoder.encode("WashShop��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashShop��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��WashShop��Ϣ*/
    public String DeleteWashShop() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            washShopDAO.DeleteWashShop(shopUserName);
            ctx.put("message",  java.net.URLEncoder.encode("WashShopɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashShopɾ��ʧ��!"));
            return "error";
        }
    }

}
