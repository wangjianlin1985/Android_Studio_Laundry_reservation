<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%> <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>160基于Android洗衣店预约系统的设计与实现-首页</title>
<link href="<%=basePath %>css/index.css" rel="stylesheet" type="text/css" />
 </head>
<body>
<div id="container">
	<div id="banner"><img src="<%=basePath %>images/logo.gif" /></div>
	<div id="globallink">
		<ul>
			<li><a href="<%=basePath %>index.jsp">首页</a></li>
			<li><a href="<%=basePath %>UserInfo/UserInfo_FrontQueryUserInfo.action" target="OfficeMain">用户</a></li> 
			<li><a href="<%=basePath %>WashClass/WashClass_FrontQueryWashClass.action" target="OfficeMain">洗衣店种类</a></li> 
			<li><a href="<%=basePath %>WashMeal/WashMeal_FrontQueryWashMeal.action" target="OfficeMain">洗衣套餐</a></li> 
			<li><a href="<%=basePath %>MealEvaluate/MealEvaluate_FrontQueryMealEvaluate.action" target="OfficeMain">套餐评价</a></li> 
			<li><a href="<%=basePath %>OrderInfo/OrderInfo_FrontQueryOrderInfo.action" target="OfficeMain">订单</a></li> 
			<li><a href="<%=basePath %>WashShop/WashShop_FrontQueryWashShop.action" target="OfficeMain">洗衣店</a></li> 
		</ul>
		<br />
	</div> 
	<div id="main">
	 <iframe id="frame1" src="<%=basePath %>desk.jsp" name="OfficeMain" width="100%" height="100%" scrolling="yes" marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0 >
	 </iframe>
	</div>
	<div id="footer">
		<p>双鱼林设计 QQ:287307421或254540457 &copy;版权所有 <a href="http://www.shuangyulin.com" target="_blank">双鱼林设计网</a>&nbsp;&nbsp;<a href="<%=basePath%>login/login_view.action"><font color=red>后台登陆</font></a></p>
	</div>
</div>
</body>
</html>
