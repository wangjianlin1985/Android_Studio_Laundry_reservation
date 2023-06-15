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
import com.chengxusheji.dao.OrderInfoDAO;
import com.chengxusheji.domain.OrderInfo;
import com.chengxusheji.dao.WashMealDAO;
import com.chengxusheji.domain.WashMeal;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.dao.OrderStateDAO;
import com.chengxusheji.domain.OrderState;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class OrderInfoAction extends BaseAction {

    /*�������Ҫ��ѯ������: ϴ���ײ�*/
    private WashMeal washMealObj;
    public void setWashMealObj(WashMeal washMealObj) {
        this.washMealObj = washMealObj;
    }
    public WashMeal getWashMealObj() {
        return this.washMealObj;
    }

    /*�������Ҫ��ѯ������: �µ��û�*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*�������Ҫ��ѯ������: ��ϵ�绰*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
    }

    /*�������Ҫ��ѯ������: �µ�ʱ��*/
    private String orderTime;
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
    public String getOrderTime() {
        return this.orderTime;
    }

    /*�������Ҫ��ѯ������: ����״̬*/
    private OrderState orderState;
    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
    public OrderState getOrderState() {
        return this.orderState;
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

    private int orderId;
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getOrderId() {
        return orderId;
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
    @Resource OrderStateDAO orderStateDAO;
    @Resource OrderInfoDAO orderInfoDAO;

    /*��������OrderInfo����*/
    private OrderInfo orderInfo;
    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
    public OrderInfo getOrderInfo() {
        return this.orderInfo;
    }

    /*��ת�����OrderInfo��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�WashMeal��Ϣ*/
        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        /*��ѯ���е�UserInfo��Ϣ*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        /*��ѯ���е�OrderState��Ϣ*/
        List<OrderState> orderStateList = orderStateDAO.QueryAllOrderStateInfo();
        ctx.put("orderStateList", orderStateList);
        return "add_view";
    }

    /*���OrderInfo��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddOrderInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashMeal washMealObj = washMealDAO.GetWashMealByMealId(orderInfo.getWashMealObj().getMealId());
            orderInfo.setWashMealObj(washMealObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(orderInfo.getUserObj().getUser_name());
            orderInfo.setUserObj(userObj);
            OrderState orderState = orderStateDAO.GetOrderStateByOrderStateId(orderInfo.getOrderState().getOrderStateId());
            orderInfo.setOrderState(orderState);
            orderInfoDAO.AddOrderInfo(orderInfo);
            ctx.put("message",  java.net.URLEncoder.encode("OrderInfo��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("OrderInfo���ʧ��!"));
            return "error";
        }
    }

    /*��ѯOrderInfo��Ϣ*/
    public String QueryOrderInfo() {
        if(currentPage == 0) currentPage = 1;
        if(telephone == null) telephone = "";
        if(orderTime == null) orderTime = "";
        List<OrderInfo> orderInfoList = orderInfoDAO.QueryOrderInfoInfo(washMealObj, userObj, telephone, orderTime, orderState, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        orderInfoDAO.CalculateTotalPageAndRecordNumber(washMealObj, userObj, telephone, orderTime, orderState);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = orderInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = orderInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("orderInfoList",  orderInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("washMealObj", washMealObj);
        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("telephone", telephone);
        ctx.put("orderTime", orderTime);
        ctx.put("orderState", orderState);
        List<OrderState> orderStateList = orderStateDAO.QueryAllOrderStateInfo();
        ctx.put("orderStateList", orderStateList);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryOrderInfoOutputToExcel() { 
        if(telephone == null) telephone = "";
        if(orderTime == null) orderTime = "";
        List<OrderInfo> orderInfoList = orderInfoDAO.QueryOrderInfoInfo(washMealObj,userObj,telephone,orderTime,orderState);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "OrderInfo��Ϣ��¼"; 
        String[] headers = { "�������","ϴ���ײ�","Ԥ������","�µ��û�","��ϵ�绰","�µ�ʱ��","����״̬","��ע��Ϣ"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<orderInfoList.size();i++) {
        	OrderInfo orderInfo = orderInfoList.get(i); 
        	dataset.add(new String[]{orderInfo.getOrderId() + "",orderInfo.getWashMealObj().getMealName(),
orderInfo.getOrderCount() + "",orderInfo.getUserObj().getName(),
orderInfo.getTelephone(),orderInfo.getOrderTime(),orderInfo.getOrderState().getStateName(),
orderInfo.getMemo()});
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
			response.setHeader("Content-disposition","attachment; filename="+"OrderInfo.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯOrderInfo��Ϣ*/
    public String FrontQueryOrderInfo() {
        if(currentPage == 0) currentPage = 1;
        if(telephone == null) telephone = "";
        if(orderTime == null) orderTime = "";
        List<OrderInfo> orderInfoList = orderInfoDAO.QueryOrderInfoInfo(washMealObj, userObj, telephone, orderTime, orderState, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        orderInfoDAO.CalculateTotalPageAndRecordNumber(washMealObj, userObj, telephone, orderTime, orderState);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = orderInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = orderInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("orderInfoList",  orderInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("washMealObj", washMealObj);
        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("telephone", telephone);
        ctx.put("orderTime", orderTime);
        ctx.put("orderState", orderState);
        List<OrderState> orderStateList = orderStateDAO.QueryAllOrderStateInfo();
        ctx.put("orderStateList", orderStateList);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�OrderInfo��Ϣ*/
    public String ModifyOrderInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������orderId��ȡOrderInfo����*/
        OrderInfo orderInfo = orderInfoDAO.GetOrderInfoByOrderId(orderId);

        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        List<OrderState> orderStateList = orderStateDAO.QueryAllOrderStateInfo();
        ctx.put("orderStateList", orderStateList);
        ctx.put("orderInfo",  orderInfo);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�OrderInfo��Ϣ*/
    public String FrontShowOrderInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������orderId��ȡOrderInfo����*/
        OrderInfo orderInfo = orderInfoDAO.GetOrderInfoByOrderId(orderId);

        List<WashMeal> washMealList = washMealDAO.QueryAllWashMealInfo();
        ctx.put("washMealList", washMealList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        List<OrderState> orderStateList = orderStateDAO.QueryAllOrderStateInfo();
        ctx.put("orderStateList", orderStateList);
        ctx.put("orderInfo",  orderInfo);
        return "front_show_view";
    }

    /*�����޸�OrderInfo��Ϣ*/
    public String ModifyOrderInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            WashMeal washMealObj = washMealDAO.GetWashMealByMealId(orderInfo.getWashMealObj().getMealId());
            orderInfo.setWashMealObj(washMealObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(orderInfo.getUserObj().getUser_name());
            orderInfo.setUserObj(userObj);
            OrderState orderState = orderStateDAO.GetOrderStateByOrderStateId(orderInfo.getOrderState().getOrderStateId());
            orderInfo.setOrderState(orderState);
            orderInfoDAO.UpdateOrderInfo(orderInfo);
            ctx.put("message",  java.net.URLEncoder.encode("OrderInfo��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("OrderInfo��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��OrderInfo��Ϣ*/
    public String DeleteOrderInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            orderInfoDAO.DeleteOrderInfo(orderId);
            ctx.put("message",  java.net.URLEncoder.encode("OrderInfoɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("OrderInfoɾ��ʧ��!"));
            return "error";
        }
    }

}
