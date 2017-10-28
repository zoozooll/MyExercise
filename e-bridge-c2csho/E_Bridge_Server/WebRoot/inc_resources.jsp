<%-- 包含在页面的 head 标签里 --%>
<%@ page  pageEncoding="UTF-8"%>

<%--
BASE 标记处理, 解决相对路径问题.
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

<base href="<%=basePath%>" />
--%>

<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<META content="text/html; charset=GBK" http-equiv=Content-Type>

<!--  jquery, 注意加载顺序 -->
<script src="js/jquery-1[1].2.1.pack.js"></script>
<script type=”text/javascript”>
      var jQuery=$;
</script>

<!-- 表单验证 -->
<script src="js/prototype.js" type="text/javascript"></script>
<script src="js/validation_cn.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="js/style_min.css" />

<!-- 缩略图标 -->
<LINK rel="shortcut icon" type=image/x-icon href="images/favicon.ico">



<!-- 样式表 -->
<LINK rel=stylesheet 
type=text/css href="css/stylesheet.css">

 