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

	/*图片或文件字段shopPhoto参数接收*/
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
    /*界面层需要查询的属性: 洗衣店账号*/
    private String shopUserName;
    public void setShopUserName(String shopUserName) {
        this.shopUserName = shopUserName;
    }
    public String getShopUserName() {
        return this.shopUserName;
    }

    /*界面层需要查询的属性: 洗衣店名称*/
    private String shopName;
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getShopName() {
        return this.shopName;
    }

    /*界面层需要查询的属性: 洗衣店种类*/
    private WashClass washClassObj;
    public void setWashClassObj(WashClass washClassObj) {
        this.washClassObj = washClassObj;
    }
    public WashClass getWashClassObj() {
        return this.washClassObj;
    }

    /*界面层需要查询的属性: 店家电话*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
    }

    /*界面层需要查询的属性: 入驻日期*/
    private String addDate;
    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }
    public String getAddDate() {
        return this.addDate;
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
    @Resource WashShopDAO washShopDAO;

    /*待操作的WashShop对象*/
    private WashShop washShop;
    public void setWashShop(WashShop washShop) {
        this.washShop = washShop;
    }
    public WashShop getWashShop() {
        return this.washShop;
    }

    /*跳转到添加WashShop视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的WashClass信息*/
        List<WashClass> washClassList = washClassDAO.QueryAllWashClassInfo();
        ctx.put("washClassList", washClassList);
        return "add_view";
    }

    /*添加WashShop信息*/
    @SuppressWarnings("deprecation")
    public String AddWashShop() {
        ActionContext ctx = ActionContext.getContext();
        /*验证洗衣店账号是否已经存在*/
        String shopUserName = washShop.getShopUserName();
        WashShop db_washShop = washShopDAO.GetWashShopByShopUserName(shopUserName);
        if(null != db_washShop) {
            ctx.put("error",  java.net.URLEncoder.encode("该洗衣店账号已经存在!"));
            return "error";
        }
        try {
            WashClass washClassObj = washClassDAO.GetWashClassByClassId(washShop.getWashClassObj().getClassId());
            washShop.setWashClassObj(washClassObj);
            /*处理洗衣店照片上传*/
            String shopPhotoPath = "upload/noimage.jpg"; 
       	 	if(shopPhotoFile != null)
       	 		shopPhotoPath = photoUpload(shopPhotoFile,shopPhotoFileContentType);
       	 	washShop.setShopPhoto(shopPhotoPath);
            washShopDAO.AddWashShop(washShop);
            ctx.put("message",  java.net.URLEncoder.encode("WashShop添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashShop添加失败!"));
            return "error";
        }
    }

    /*查询WashShop信息*/
    public String QueryWashShop() {
        if(currentPage == 0) currentPage = 1;
        if(shopUserName == null) shopUserName = "";
        if(shopName == null) shopName = "";
        if(telephone == null) telephone = "";
        if(addDate == null) addDate = "";
        List<WashShop> washShopList = washShopDAO.QueryWashShopInfo(shopUserName, shopName, washClassObj, telephone, addDate, currentPage);
        /*计算总的页数和总的记录数*/
        washShopDAO.CalculateTotalPageAndRecordNumber(shopUserName, shopName, washClassObj, telephone, addDate);
        /*获取到总的页码数目*/
        totalPage = washShopDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryWashShopOutputToExcel() { 
        if(shopUserName == null) shopUserName = "";
        if(shopName == null) shopName = "";
        if(telephone == null) telephone = "";
        if(addDate == null) addDate = "";
        List<WashShop> washShopList = washShopDAO.QueryWashShopInfo(shopUserName,shopName,washClassObj,telephone,addDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "WashShop信息记录"; 
        String[] headers = { "洗衣店账号","洗衣店名称","洗衣店种类","洗衣店照片","店家电话","入驻日期"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"WashShop.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询WashShop信息*/
    public String FrontQueryWashShop() {
        if(currentPage == 0) currentPage = 1;
        if(shopUserName == null) shopUserName = "";
        if(shopName == null) shopName = "";
        if(telephone == null) telephone = "";
        if(addDate == null) addDate = "";
        List<WashShop> washShopList = washShopDAO.QueryWashShopInfo(shopUserName, shopName, washClassObj, telephone, addDate, currentPage);
        /*计算总的页数和总的记录数*/
        washShopDAO.CalculateTotalPageAndRecordNumber(shopUserName, shopName, washClassObj, telephone, addDate);
        /*获取到总的页码数目*/
        totalPage = washShopDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的WashShop信息*/
    public String ModifyWashShopQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键shopUserName获取WashShop对象*/
        WashShop washShop = washShopDAO.GetWashShopByShopUserName(shopUserName);

        List<WashClass> washClassList = washClassDAO.QueryAllWashClassInfo();
        ctx.put("washClassList", washClassList);
        ctx.put("washShop",  washShop);
        return "modify_view";
    }

    /*查询要修改的WashShop信息*/
    public String FrontShowWashShopQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键shopUserName获取WashShop对象*/
        WashShop washShop = washShopDAO.GetWashShopByShopUserName(shopUserName);

        List<WashClass> washClassList = washClassDAO.QueryAllWashClassInfo();
        ctx.put("washClassList", washClassList);
        ctx.put("washShop",  washShop);
        return "front_show_view";
    }

    /*更新修改WashShop信息*/
    public String ModifyWashShop() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashClass washClassObj = washClassDAO.GetWashClassByClassId(washShop.getWashClassObj().getClassId());
            washShop.setWashClassObj(washClassObj);
            /*处理洗衣店照片上传*/
            if(shopPhotoFile != null) {
            	String shopPhotoPath = photoUpload(shopPhotoFile,shopPhotoFileContentType);
            	washShop.setShopPhoto(shopPhotoPath);
            }
            washShopDAO.UpdateWashShop(washShop);
            ctx.put("message",  java.net.URLEncoder.encode("WashShop信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashShop信息更新失败!"));
            return "error";
       }
   }

    /*删除WashShop信息*/
    public String DeleteWashShop() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            washShopDAO.DeleteWashShop(shopUserName);
            ctx.put("message",  java.net.URLEncoder.encode("WashShop删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashShop删除失败!"));
            return "error";
        }
    }

}
