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

	/*图片或文件字段mealPhoto参数接收*/
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
    /*界面层需要查询的属性: 洗衣套餐*/
    private String mealName;
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }
    public String getMealName() {
        return this.mealName;
    }

    /*界面层需要查询的属性: 发布日期*/
    private String publishDate;
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
    public String getPublishDate() {
        return this.publishDate;
    }

    /*界面层需要查询的属性: 洗衣店*/
    private WashShop washShopObj;
    public void setWashShopObj(WashShop washShopObj) {
        this.washShopObj = washShopObj;
    }
    public WashShop getWashShopObj() {
        return this.washShopObj;
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

    private int mealId;
    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
    public int getMealId() {
        return mealId;
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
    @Resource WashShopDAO washShopDAO;
    @Resource WashMealDAO washMealDAO;

    /*待操作的WashMeal对象*/
    private WashMeal washMeal;
    public void setWashMeal(WashMeal washMeal) {
        this.washMeal = washMeal;
    }
    public WashMeal getWashMeal() {
        return this.washMeal;
    }

    /*跳转到添加WashMeal视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的WashShop信息*/
        List<WashShop> washShopList = washShopDAO.QueryAllWashShopInfo();
        ctx.put("washShopList", washShopList);
        return "add_view";
    }

    /*添加WashMeal信息*/
    @SuppressWarnings("deprecation")
    public String AddWashMeal() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashShop washShopObj = washShopDAO.GetWashShopByShopUserName(washMeal.getWashShopObj().getShopUserName());
            washMeal.setWashShopObj(washShopObj);
            /*处理套餐图片上传*/
            String mealPhotoPath = "upload/noimage.jpg"; 
       	 	if(mealPhotoFile != null)
       	 		mealPhotoPath = photoUpload(mealPhotoFile,mealPhotoFileContentType);
       	 	washMeal.setMealPhoto(mealPhotoPath);
            washMealDAO.AddWashMeal(washMeal);
            ctx.put("message",  java.net.URLEncoder.encode("WashMeal添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashMeal添加失败!"));
            return "error";
        }
    }

    /*查询WashMeal信息*/
    public String QueryWashMeal() {
        if(currentPage == 0) currentPage = 1;
        if(mealName == null) mealName = "";
        if(publishDate == null) publishDate = "";
        List<WashMeal> washMealList = washMealDAO.QueryWashMealInfo(mealName, publishDate, washShopObj, currentPage);
        /*计算总的页数和总的记录数*/
        washMealDAO.CalculateTotalPageAndRecordNumber(mealName, publishDate, washShopObj);
        /*获取到总的页码数目*/
        totalPage = washMealDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryWashMealOutputToExcel() { 
        if(mealName == null) mealName = "";
        if(publishDate == null) publishDate = "";
        List<WashMeal> washMealList = washMealDAO.QueryWashMealInfo(mealName,publishDate,washShopObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "WashMeal信息记录"; 
        String[] headers = { "套餐id","洗衣套餐","套餐价格","套餐图片","发布日期","洗衣店"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"WashMeal.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询WashMeal信息*/
    public String FrontQueryWashMeal() {
        if(currentPage == 0) currentPage = 1;
        if(mealName == null) mealName = "";
        if(publishDate == null) publishDate = "";
        List<WashMeal> washMealList = washMealDAO.QueryWashMealInfo(mealName, publishDate, washShopObj, currentPage);
        /*计算总的页数和总的记录数*/
        washMealDAO.CalculateTotalPageAndRecordNumber(mealName, publishDate, washShopObj);
        /*获取到总的页码数目*/
        totalPage = washMealDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的WashMeal信息*/
    public String ModifyWashMealQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键mealId获取WashMeal对象*/
        WashMeal washMeal = washMealDAO.GetWashMealByMealId(mealId);

        List<WashShop> washShopList = washShopDAO.QueryAllWashShopInfo();
        ctx.put("washShopList", washShopList);
        ctx.put("washMeal",  washMeal);
        return "modify_view";
    }

    /*查询要修改的WashMeal信息*/
    public String FrontShowWashMealQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键mealId获取WashMeal对象*/
        WashMeal washMeal = washMealDAO.GetWashMealByMealId(mealId);

        List<WashShop> washShopList = washShopDAO.QueryAllWashShopInfo();
        ctx.put("washShopList", washShopList);
        ctx.put("washMeal",  washMeal);
        return "front_show_view";
    }

    /*更新修改WashMeal信息*/
    public String ModifyWashMeal() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashShop washShopObj = washShopDAO.GetWashShopByShopUserName(washMeal.getWashShopObj().getShopUserName());
            washMeal.setWashShopObj(washShopObj);
            /*处理套餐图片上传*/
            if(mealPhotoFile != null) {
            	String mealPhotoPath = photoUpload(mealPhotoFile,mealPhotoFileContentType);
            	washMeal.setMealPhoto(mealPhotoPath);
            }
            washMealDAO.UpdateWashMeal(washMeal);
            ctx.put("message",  java.net.URLEncoder.encode("WashMeal信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashMeal信息更新失败!"));
            return "error";
       }
   }

    /*删除WashMeal信息*/
    public String DeleteWashMeal() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            washMealDAO.DeleteWashMeal(mealId);
            ctx.put("message",  java.net.URLEncoder.encode("WashMeal删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("WashMeal删除失败!"));
            return "error";
        }
    }

}
