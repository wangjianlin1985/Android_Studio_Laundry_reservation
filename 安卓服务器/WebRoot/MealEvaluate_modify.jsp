<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.MealEvaluate" %>
<%@ page import="com.chengxusheji.domain.WashMeal" %>
<%@ page import="com.chengxusheji.domain.UserInfo" %>
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
    MealEvaluate mealEvaluate = (MealEvaluate)request.getAttribute("mealEvaluate");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改套餐评价</TITLE>
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
    var evaluateContent = document.getElementById("mealEvaluate.evaluateContent").value;
    if(evaluateContent=="") {
        alert('请输入评价内容!');
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
    <TD align="left" vAlign=top ><s:form action="MealEvaluate/MealEvaluate_ModifyMealEvaluate.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>评价id:</td>
    <td width=70%><input id="mealEvaluate.evaluateId" name="mealEvaluate.evaluateId" type="text" value="<%=mealEvaluate.getEvaluateId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>被评套餐:</td>
    <td width=70%>
      <select name="mealEvaluate.washMealObj.mealId">
      <%
        for(WashMeal washMeal:washMealList) {
          String selected = "";
          if(washMeal.getMealId() == mealEvaluate.getWashMealObj().getMealId())
            selected = "selected";
      %>
          <option value='<%=washMeal.getMealId() %>' <%=selected %>><%=washMeal.getMealName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>评价内容:</td>
    <td width=70%><textarea id="mealEvaluate.evaluateContent" name="mealEvaluate.evaluateContent" rows=5 cols=50><%=mealEvaluate.getEvaluateContent() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>评价用户:</td>
    <td width=70%>
      <select name="mealEvaluate.userObj.user_name">
      <%
        for(UserInfo userInfo:userInfoList) {
          String selected = "";
          if(userInfo.getUser_name().equals(mealEvaluate.getUserObj().getUser_name()))
            selected = "selected";
      %>
          <option value='<%=userInfo.getUser_name() %>' <%=selected %>><%=userInfo.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>评价时间:</td>
    <td width=70%><input id="mealEvaluate.evaluateTime" name="mealEvaluate.evaluateTime" type="text" size="20" value='<%=mealEvaluate.getEvaluateTime() %>'/></td>
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
