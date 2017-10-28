<%@ page language="java" pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link rel="stylesheet" href="css/common.css" type="text/css" />
<title>左侧导航栏</title>
</head>
<script  type="text/javascript">
eval(function(p,a,c,k,e,d){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)d[e(c)]=k[c]||e(c);k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('G N="";8 1e(M,h){9(N!=""){7(N).r="Y"}9(7(M).r=="Y"){7(M).r="1z";2.H(h);N=M}}8 7(l){9(e.15&&e.15(l)){o e.15(l)}t 9(e.14&&e.14(l)){o e.14(l)}t 9(e.13&&e.13[l]){o e.13[l]}t{o 1y}}8 1x(){d.4=x 10();d.6=x 10();d.12=12;d.I=I;d.y=y;d.H=H;d.z=z}8 Z(p,K,J,L){d.5=K;d.X=J;d.b=p;d.1f=L}8 12(p,5,L){2.6[2.4.c]=x 10();2.4[2.4.c]=x Z(p,5,0,L);o(2.4.c-1)}8 I(p,k,J){9(k>=0&&k<=2.4.c){K="1w"+k;2.6[k][2.6[k].c]=x Z(p,K,J,0);o(2.6[k].c-1)}t I=-1}8 z(5){G 3="";v(i=0;i<2.4.c;i++){9(2.4[i].1f==1&&2.4[i].5==5){3+="<f w=T g=U"+i+" u=\\"V(\'E"+i+"\')\\">";3+="<F>"+2.4[i].b+"</F>";3+="</f>";3+="<f w=1d g=E"+i+"><m>";v(j=0;j<2.6[i].c;j++){3+="<n g="+2.6[i][j].5+j+" u=\\"W(\'"+2.6[i][j].b+"\',\'"+2.4[i].b+"\',\'"+2.6[i][j].X+"\')\\"><a 1c=#>"+2.6[i][j].b+"</a></n>"}3+="</m></f>"}}7(\'1b\').D=3}8 y(5){G 3="<m>";v(i=0;i<2.4.c;i++){9(2.4[i].5==5){3+="<n g=1v"+i+" u=\\"1e(g,\'"+2.4[i].b+"\')\\" w=Y>"+2.4[i].b+"</n>"}}3+="</m>";7(\'1u\').D=3}8 H(h){G 3="";v(i=0;i<2.4.c;i++){9(2.4[i].b==h){3="<f w=T g=U"+i+" u=\\"V(\'E"+i+"\')\\">";3+="<F>"+2.4[i].b+"</F>";3+="</f>";3+="<f w=1d g=E"+i+" B=\'A:1a;\'><m>";v(j=0;j<2.6[i].c;j++){3+="<n g="+2.6[i][j].5+"1t"+j+" u=\\"W(\'"+2.6[i][j].b+"\',\'"+2.4[i].b+"\',\'"+2.6[i][j].X+"\')\\"><a 1c=#>"+2.6[i][j].b+"</a></n>"}3+="</m></f>"}}7(\'1b\').D=3}8 W(h,5,q){9(h!=""&&5!=""){R.Q.P[\'1s\'].7(\'1r\').D=5+"&C;&C;<1q q=1p/1o.1n 1m=0 />&C;&C;"+h}9(q!=""){R.Q.P[\'O\'].18=q}}8 V(s){S="U"+s.1l(11);9(7(s).B.A=="19"){7(s).B.A="1a";7(S).r="T"}t{7(s).B.A="19";7(S).r="1k"}}8 1j(5){2.z(5);2.y(5);R.Q.P[\'O\'].18="O.1i"}e.1h("<16 q=17/1g.17></"+"16>");',62,98,'||outlookbar|output|titlelist|sortname|itemlist|getObject|function|if||title|length|this|document|div|id|item|||parentid|objectId|ul|li|return|intitle|src|className|divid|else|onclick|for|class|new|getbytitle|getdefaultnav|display|style|nbsp|innerHTML|sub_detail_|span|var|getbyitem|additem|inkey|insort|inisdefault|Id|preClassName|manFrame|frames|top|window|subsortid|list_tilte|sub_sort_|hideorshow|changeframe|key|left_back|theitem|Array||addtitle|layers|all|getElementById|script|js|location|none|block|right_main_nav|href|list_detail|list_sub_detail|isdefault|nav|write|html|initinav|list_tilte_onclick|substring|broder|gif|slide|images|img|show_text|mainFrame|_|left_main_nav|left_nav_|item_|outlook|false|left_back_onclick'.split('|'),0,{}))
</script>
<body onload="initinav('系统设置')">
<div id="left_content">
     <div id="user_info">欢迎您，<strong>${admin.adminName}</strong><br />[<a href="#">系统管理员</a>，<a href="#">退出</a>]</div>
	 <div id="main_nav">
	     <div id="left_main_nav"></div>
		 <div id="right_main_nav"></div>
	 </div>
</div>
</body>
</html>