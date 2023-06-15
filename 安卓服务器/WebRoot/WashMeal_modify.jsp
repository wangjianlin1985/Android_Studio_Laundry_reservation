<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.WashMeal" %>
<%@ page import="com.chengxusheji.domain.WashShop" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的WashShop信息
    List<WashShop> washShopList = (List<WashShop>)request.getAttribute("washShopList");
    WashMeal washMeal = (WashMeal)request.getAttribute("washMeal");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改洗衣套餐</TITLE>
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
    var mealName = document.getElementById("washMeal.mealName").value;
    if(mealName=="") {
        alert('请输入洗衣套餐!');
        return false;
    }
    var introduce = document.getElementById("washMeal.introduce").value;
    if(introduce=="") {
        alert('请输入套餐说明!');
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
    <TD align="left" vAlign=top ><s:form action="WashMeal/WashMeal_ModifyWashMeal.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>套餐id:</td>
    <td width=70%><input id="washMeal.mealId" name="washMeal.mealId" type="text" value="<%=washMeal.getMealId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>洗衣套餐:</td>
    <td width=70%><input id="washMeal.mealName" name="washMeal.mealName" type="text" size="60" value='<%=washMeal.getMealName() %>'/></td>
  </tr>

  <tr>
    <td width=30%>套餐说明:</td>
    <td width=70%><textarea id="washMeal.introduce" name="washMeal.introduce" rows=5 cols=50><%=washMeal.getIntroduce() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>套餐价格:</td>
    <td width=70%><input id="washMeal.price" name="washMeal.price" type="text" size="8" value='<%=washMeal.getPrice() %>'/></td>
  </tr>

  <tr>
    <td width=30%>套餐图片:</td>
    <td width=70%><img src="<%=basePath %><%=washMeal.getMealPhoto() %>" width="200px" border="0px"/><br/>
    <input type=hidden name="washMeal.mealPhoto" value="<%=washMeal.getMealPhoto() %>" />
    <input id="mealPhotoFile" name="mealPhotoFile" type="file" size="50" /></td>
  </tr>
  <tr>
    <td width=30%>发布日期:</td>
    <% DateFormat publishDateSDF = new SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><input type="text" readonly  id="washMeal.publishDate"  name="washMeal.publishDate" onclick="setDay(this);" value='<%=publishDateSDF.format(washMeal.getPublishDate()) %>'/></td>
  </tr>

  <tr>
    <td width=30%>洗衣店:</td>
    <td width=70%>
      <select name="washMeal.washShopObj.shopUserName">
      <%
        for(WashShop washShop:washShopList) {
          String selected = "";
          if(washShop.getShopUserName().equals(washMeal.getWashShopObj().getShopUserName()))
            selected = "selected";
      %>
          <option value='<%=washShop.getShopUserName() %>' <%=selected %>><%=washShop.getShopName() %></option>
      <%
        }
      %>
    </td>
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
