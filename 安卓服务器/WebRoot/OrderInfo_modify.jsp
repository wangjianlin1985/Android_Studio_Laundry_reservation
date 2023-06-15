<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.OrderInfo" %>
<%@ page import="com.chengxusheji.domain.WashMeal" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ page import="com.chengxusheji.domain.OrderState" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的WashMeal信息
    List<WashMeal> washMealList = (List<WashMeal>)request.getAttribute("washMealList");
    //获取所有的UserInfo信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    //获取所有的OrderState信息
    List<OrderState> orderStateList = (List<OrderState>)request.getAttribute("orderStateList");
    OrderInfo orderInfo = (OrderInfo)request.getAttribute("orderInfo");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改订单</TITLE>
<STYLE type=text/css>
BODY {
	MARGIN-LEFT: 0px; BACKGROUND-COLOR: #ffffff
}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
<script language="javascript">
/*验证表单*/
function checkForm() {
    var telephone = document.getElementById("orderInfo.telephone").value;
    if(telephone=="") {
        alert('请输入联系电话!');
        return false;
    }
    return true; 
}
 </script>
</HEAD>
<BODY background="<%=basePath %>images/adminBg.jpg">
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="OrderInfo/OrderInfo_ModifyOrderInfo.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>订单编号:</td>
    <td width=70%><input id="orderInfo.orderId" name="orderInfo.orderId" type="text" value="<%=orderInfo.getOrderId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>洗衣套餐:</td>
    <td width=70%>
      <select name="orderInfo.washMealObj.mealId">
      <%
        for(WashMeal washMeal:washMealList) {
          String selected = "";
          if(washMeal.getMealId() == orderInfo.getWashMealObj().getMealId())
            selected = "selected";
      %>
          <option value='<%=washMeal.getMealId() %>' <%=selected %>><%=washMeal.getMealName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>预订数量:</td>
    <td width=70%><input id="orderInfo.orderCount" name="orderInfo.orderCount" type="text" size="8" value='<%=orderInfo.getOrderCount() %>'/></td>
  </tr>

  <tr>
    <td width=30%>下单用户:</td>
    <td width=70%>
      <select name="orderInfo.userObj.user_name">
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(orderInfo.getUserObj().getUser_name()))
            selected = "selected";
      %>
          <option value='<%=userInfo.getUser_name() %>' <%=selected %>><%=userInfo.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>联系电话:</td>
    <td width=70%><input id="orderInfo.telephone" name="orderInfo.telephone" type="text" size="20" value='<%=orderInfo.getTelephone() %>'/></td>
  </tr>

  <tr>
    <td width=30%>下单时间:</td>
    <td width=70%><input id="orderInfo.orderTime" name="orderInfo.orderTime" type="text" size="20" value='<%=orderInfo.getOrderTime() %>'/></td>
  </tr>

  <tr>
    <td width=30%>订单状态:</td>
    <td width=70%>
      <select name="orderInfo.orderState.orderStateId">
      <%
        for(OrderState orderState:orderStateList) {
          String selected = "";
          if(orderState.getOrderStateId() == orderInfo.getOrderState().getOrderStateId())
            selected = "selected";
      %>
          <option value='<%=orderState.getOrderStateId() %>' <%=selected %>><%=orderState.getStateName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>备注信息:</td>
    <td width=70%><textarea id="orderInfo.memo" name="orderInfo.memo" rows=5 cols=50><%=orderInfo.getMemo() %></textarea></td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='保存' >
        &nbsp;&nbsp;
        <input type="reset" value='重写' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
