<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.WashShop" %>
<%@ page import="com.chengxusheji.domain.WashClass" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的WashClass信息
    List<WashClass> washClassList = (List<WashClass>)request.getAttribute("washClassList");
    WashShop washShop = (WashShop)request.getAttribute("washShop");

%>
<HTML><HEAD><TITLE>查看洗衣店</TITLE>
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
    <td width=30%>洗衣店账号:</td>
    <td width=70%><%=washShop.getShopUserName() %></td>
  </tr>

  <tr>
    <td width=30%>登录密码:</td>
    <td width=70%><%=washShop.getPassword() %></td>
  </tr>

  <tr>
    <td width=30%>洗衣店名称:</td>
    <td width=70%><%=washShop.getShopName() %></td>
  </tr>

  <tr>
    <td width=30%>洗衣店种类:</td>
    <td width=70%>
      <%=washShop.getWashClassObj().getClassName() %>
    </td>
  </tr>

  <tr>
    <td width=30%>洗衣店照片:</td>
    <td width=70%><img src="<%=basePath %><%=washShop.getShopPhoto() %>" width="200px" border="0px"/></td>
  </tr>
  <tr>
    <td width=30%>店家电话:</td>
    <td width=70%><%=washShop.getTelephone() %></td>
  </tr>

  <tr>
    <td width=30%>入驻日期:</td>
        <% java.text.DateFormat addDateSDF = new java.text.SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><%=addDateSDF.format(washShop.getAddDate()) %></td>
  </tr>

  <tr>
    <td width=30%>店铺地址:</td>
    <td width=70%><%=washShop.getAddress() %></td>
  </tr>

  <tr>
    <td width=30%>纬度:</td>
    <td width=70%><%=washShop.getLatitude() %></td>
  </tr>

  <tr>
    <td width=30%>经度:</td>
    <td width=70%><%=washShop.getLongitude() %></td>
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
