<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.WashShop" %>
<%@ page import="com.chengxusheji.domain.WashClass" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //��ȡ���е�WashClass��Ϣ
    List<WashClass> washClassList = (List<WashClass>)request.getAttribute("washClassList");
    WashShop washShop = (WashShop)request.getAttribute("washShop");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>�޸�ϴ�µ�</TITLE>
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
/*��֤��*/
function checkForm() {
    var shopUserName = document.getElementById("washShop.shopUserName").value;
    if(shopUserName=="") {
        alert('������ϴ�µ��˺�!');
        return false;
    }
    var password = document.getElementById("washShop.password").value;
    if(password=="") {
        alert('�������¼����!');
        return false;
    }
    var shopName = document.getElementById("washShop.shopName").value;
    if(shopName=="") {
        alert('������ϴ�µ�����!');
        return false;
    }
    var telephone = document.getElementById("washShop.telephone").value;
    if(telephone=="") {
        alert('�������ҵ绰!');
        return false;
    }
    var address = document.getElementById("washShop.address").value;
    if(address=="") {
        alert('��������̵�ַ!');
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
    <TD align="left" vAlign=top ><s:form action="WashShop/WashShop_ModifyWashShop.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>ϴ�µ��˺�:</td>
    <td width=70%><input id="washShop.shopUserName" name="washShop.shopUserName" type="text" value="<%=washShop.getShopUserName() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>��¼����:</td>
    <td width=70%><input id="washShop.password" name="washShop.password" type="text" size="20" value='<%=washShop.getPassword() %>'/></td>
  </tr>

  <tr>
    <td width=30%>ϴ�µ�����:</td>
    <td width=70%><input id="washShop.shopName" name="washShop.shopName" type="text" size="50" value='<%=washShop.getShopName() %>'/></td>
  </tr>

  <tr>
    <td width=30%>ϴ�µ�����:</td>
    <td width=70%>
      <select name="washShop.washClassObj.classId">
      <%
        for(WashClass washClass:washClassList) {
          String selected = "";
          if(washClass.getClassId() == washShop.getWashClassObj().getClassId())
            selected = "selected";
      %>
          <option value='<%=washClass.getClassId() %>' <%=selected %>><%=washClass.getClassName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>ϴ�µ���Ƭ:</td>
    <td width=70%><img src="<%=basePath %><%=washShop.getShopPhoto() %>" width="200px" border="0px"/><br/>
    <input type=hidden name="washShop.shopPhoto" value="<%=washShop.getShopPhoto() %>" />
    <input id="shopPhotoFile" name="shopPhotoFile" type="file" size="50" /></td>
  </tr>
  <tr>
    <td width=30%>��ҵ绰:</td>
    <td width=70%><input id="washShop.telephone" name="washShop.telephone" type="text" size="20" value='<%=washShop.getTelephone() %>'/></td>
  </tr>

  <tr>
    <td width=30%>��פ����:</td>
    <% DateFormat addDateSDF = new SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><input type="text" readonly  id="washShop.addDate"  name="washShop.addDate" onclick="setDay(this);" value='<%=addDateSDF.format(washShop.getAddDate()) %>'/></td>
  </tr>

  <tr>
    <td width=30%>���̵�ַ:</td>
    <td width=70%><input id="washShop.address" name="washShop.address" type="text" size="80" value='<%=washShop.getAddress() %>'/></td>
  </tr>

  <tr>
    <td width=30%>γ��:</td>
    <td width=70%><input id="washShop.latitude" name="washShop.latitude" type="text" size="8" value='<%=washShop.getLatitude() %>'/></td>
  </tr>

  <tr>
    <td width=30%>����:</td>
    <td width=70%><input id="washShop.longitude" name="washShop.longitude" type="text" size="8" value='<%=washShop.getLongitude() %>'/></td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='����' >
        &nbsp;&nbsp;
        <input type="reset" value='��д' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
