<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.WashMeal" %>
<%@ page import="com.chengxusheji.domain.WashShop" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的WashShop信息
    List<WashShop> washShopList = (List<WashShop>)request.getAttribute("washShopList");
    WashMeal washMeal = (WashMeal)request.getAttribute("washMeal");

%>
<HTML><HEAD><TITLE>查看洗衣套餐</TITLE>
<STYLE type=text/css>
body{margin:0px; font-size:12px; background-image:url(<%=basePath%>images/bg.jpg); background-position:bottom; background-repeat:repeat-x; background-color:#A2D5F0;}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
</HEAD>
<BODY><br/><br/>
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3'  class="tablewidth">
  <tr>
    <td width=30%>套餐id:</td>
    <td width=70%><%=washMeal.getMealId() %></td>
  </tr>

  <tr>
    <td width=30%>洗衣套餐:</td>
    <td width=70%><%=washMeal.getMealName() %></td>
  </tr>

  <tr>
    <td width=30%>套餐说明:</td>
    <td width=70%><%=washMeal.getIntroduce() %></td>
  </tr>

  <tr>
    <td width=30%>套餐价格:</td>
    <td width=70%><%=washMeal.getPrice() %></td>
  </tr>

  <tr>
    <td width=30%>套餐图片:</td>
    <td width=70%><img src="<%=basePath %><%=washMeal.getMealPhoto() %>" width="200px" border="0px"/></td>
  </tr>
  <tr>
    <td width=30%>发布日期:</td>
        <% java.text.DateFormat publishDateSDF = new java.text.SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><%=publishDateSDF.format(washMeal.getPublishDate()) %></td>
  </tr>

  <tr>
    <td width=30%>洗衣店:</td>
    <td width=70%>
      <%=washMeal.getWashShopObj().getShopName() %>
    </td>
  </tr>

  <tr>
      <td colspan="4" align="center">
        <input type="button" value="返回" onclick="history.back();"/>
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
