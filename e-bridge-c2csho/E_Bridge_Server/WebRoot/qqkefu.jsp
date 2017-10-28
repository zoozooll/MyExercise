<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%	
		boolean flag=true;
	if(flag=true){ 
		String[] QQ_name={"网站客服","网站客服","网站客服"};
		String[] QQ={"406897011","515961153","781803766"};
		System.out.println("QQ======>"+QQ[0]+QQ[1]+QQ[2]);		
 %>
<script type="text/javascript">
var online= new Array();
if (!document.layers)
document.write('<div id="divStayTopLeft" style="position:absolute;z-index:10;">')
</script>
<layer id="divStayTopLeft">
<table border="0" cellspacing="0" cellpadding="0">
<tr><td><img border=0 src=images/qq/up1.gif></td></tr>
<tr><td>
<table width=118 border=0 bgcolor=#FFFFFF background=images/qq/mid1.gif>
<%
for(int i=0;i<QQ.length;i++){
if("1".equals("0")){
out.println("<tr><td align='right'><a class='c' href='tencent://message/?uin="+QQ[i]+"&Site=Today电子商务网站&Menu=yes'><img alt='已下线' src='images/qq/qqoff1.gif' border='0' align='center' style='width: auto;height: auto;border: none;'></a></td><td><a class='c' target=blank href='tencent://message/?uin="+QQ[i]+"&Site=Today电子商务网站&Menu=yes' alt='不在线上' style='font-size:12px;'>"+QQ_name[i]+"</a></td></tr>");
}else{
out.println("<tr><td align='right'><a class='b' href='tencent://message/?uin="+QQ[i]+"&Site=Today电子商务网站&Menu=yes'><img alt='有问题找我们呀' src='images/qq/qqon4.gif' border='0' align='center' style='width: auto;height: auto;border: none;'></a></td><td><a class='b' target=blank href='tencent://message/?uin="+QQ[i]+"&Site=Today电子商务网站&Menu=yes' alt='有事Q我们啊' style='font-size:12px;'>"+QQ_name[i]+"</a></td></tr>");
}
}
%>
</table></td></tr>
<tr><td><img border="0" src="images/qq/down1.gif" style="width: auto;height: auto;border: none;"></td></tr>
</table>
</layer>
<%
if( "1".equals("1")) 
{
%>
<script type="text/javascript">
var verticalpos="frombottom"
if (!document.layers)
document.write('</div>')
function JSFX_FloatTopDiv()
{
	var startX =2,
	startY = 220;
	var ns = (navigator.appName.indexOf("Netscape") != -1);
	var d = document;
	function ml(id)
	{
		var el=d.getElementById?d.getElementById(id):d.all?d.all[id]:d.layers[id];
		if(d.layers)el.style=el;
		el.sP=function(x,y){this.style.left=x;this.style.top=y;};
		el.x = startX;
		if (verticalpos=="fromtop")
		el.y = startY;
		else{
		el.y = ns ? pageYOffset + innerHeight : document.body.scrollTop + document.body.clientHeight;
		el.y -= startY;
		}
		return el;
	}
	window.stayTopLeft=function()
	{
		if (verticalpos=="fromtop"){
		var pY = ns ? pageYOffset : document.body.scrollTop;
		ftlObj.y += (pY + startY - ftlObj.y)/8;
		}
		else{
		var pY = ns ? pageYOffset + innerHeight : document.body.scrollTop + document.body.clientHeight;
		ftlObj.y += (pY - startY - ftlObj.y)/8;
		}
		ftlObj.sP(ftlObj.x, ftlObj.y);
		setTimeout("stayTopLeft()", 10);
	}
	ftlObj = ml("divStayTopLeft");
	stayTopLeft();
}
JSFX_FloatTopDiv();
</script>
<%}else{%>
<script type="text/javascript">
var verticalpos="frombottom"
if (!document.layers)
document.write('</div>')
function JSFX_FloatTopDiv()
{
	var startX =screen.width-140,
	startY = 220;
	var ns = (navigator.appName.indexOf("Netscape") != -1);
	var d = document;
	function ml(id)
	{
		var el=d.getElementById?d.getElementById(id):d.all?d.all[id]:d.layers[id];
		if(d.layers)el.style=el;
		el.sP=function(x,y){this.style.left=x;this.style.top=y;};
		el.x = startX;
		if (verticalpos=="fromtop")
		el.y = startY;
		else{
		el.y = ns ? pageYOffset + innerHeight : document.body.scrollTop + document.body.clientHeight;
		el.y -= startY;
		}
		return el;
	}
	window.stayTopLeft=function()
	{
		if (verticalpos=="fromtop"){
		var pY = ns ? pageYOffset : document.body.scrollTop;
		ftlObj.y += (pY + startY - ftlObj.y)/8;
		}
		else{
		var pY = ns ? pageYOffset + innerHeight : document.body.scrollTop + document.body.clientHeight;
		ftlObj.y += (pY - startY - ftlObj.y)/8;
		}
		ftlObj.sP(ftlObj.x, ftlObj.y);
		setTimeout("stayTopLeft()", 10);
	}
	ftlObj = ml("divStayTopLeft");
	stayTopLeft();
}
JSFX_FloatTopDiv();
</script>

<%
}
}
%>

