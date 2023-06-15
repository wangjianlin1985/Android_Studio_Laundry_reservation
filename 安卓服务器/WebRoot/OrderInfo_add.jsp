<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%>
<%@ page import="com.chengxusheji.domain.WashMeal" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
<%@ page import="com.chengxusheji.domain.OrderState" %>
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
    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>添加订单</TITLE> 
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
    <TD align="left" vAlign=top >
    <s:form action="OrderInfo/OrderInfo_AddOrderInfo.action" method="post" id="orderInfoAddForm" onsubmit="return checkForm();"  enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">

  <tr>
    <td width=30%>洗衣套餐:</td>
    <td width=70%>
      <select name="orderInfo.washMealObj.mealId">
      <%
        for(WashMeal washMeal:washMealList) {
      %>
          <option value='<%=washMeal.getMealId() %>'><%=washMeal.getMealName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>预订数量:</td>
    <td width=70%><input id="orderInfo.orderCount" name="orderInfo.orderCount" type="text" size="8" /></td>
  </tr>

  <tr>
    <td width=30%>下单用户:</td>
    <td width=70%>
      <select name="orderInfo.userObj.user_name">
      <%
        for(UserInfo userInfo:userInfoList) {
      %>
          <option value='<%=userInfo.getUser_name() %>'><%=userInfo.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>联系电话:</td>
    <td width=70%><input id="orderInfo.telephone" name="orderInfo.telephone" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>下单时间:</td>
    <td width=70%><input id="orderInfo.orderTime" name="orderInfo.orderTime" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>订单状态:</td>
    <td width=70%>
      <select name="orderInfo.orderState.orderStateId">
      <%
        for(OrderState orderState:orderStateList) {
      %>
          <option value='<%=orderState.getOrderStateId() %>'><%=orderState.getStateName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>备注信息:</td>
    <td width=70%><textarea id="orderInfo.memo" name="orderInfo.memo" rows="5" cols="50"></textarea></td>
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
