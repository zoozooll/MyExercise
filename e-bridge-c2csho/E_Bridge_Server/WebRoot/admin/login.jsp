<%@ page language="java" pageEncoding="gbk"%>
<html>
<head> <title>Today电子商务网站,后台管理登陆</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />

<LINK href="css/style.css" rel=stylesheet type=text/css>
<link href="images/images/style.CSS" rel="stylesheet" type="text/css">
<script language="JavaScript" src="js/Keyboard.js"></script>
<STYLE type=text/css>
.style3 {
	FONT-SIZE: 9pt; COLOR: #dadeed; TEXT-DECORATION: none
}
.style4 {
	FONT-SIZE: 9pt
}
.ipt {
	BORDER-RIGHT: #a8b1d2 1px solid; BORDER-TOP: #a8b1d2 1px solid; FONT-SIZE: 9pt; BORDER-LEFT: #a8b1d2 1px solid; WIDTH: 120px; COLOR: #7b8ac3; BORDER-BOTTOM: #a8b1d2 1px solid; HEIGHT: 18px
}
.copyright {
	PADDING-RIGHT: 1px; BORDER-TOP: #6595d6 1px dashed; PADDING-LEFT: 1px; PADDING-BOTTOM: 1px; FONT: 11px verdana,arial,helvetica,sans-serif; COLOR: #4455aa; PADDING-TOP: 1px; TEXT-DECORATION: none
}
</STYLE>

<script type="text/javascript">

function killErrors() { 
return true; 
} 
window.onerror = killErrors; 

</script>
<script type="text/javascript">
var flag=false;
function entityTextfocus(obj){		
		var con=document.getElementById(obj);
		//alert(con.value);
		con.value='';
		if(obj=='adminPassword') con.type='password';
		con.style.color='black';
	}	
	function getn(){
			    var a1=parseInt(Math.random()*10);
				var a2=parseInt(Math.random()*10);
				var a3=parseInt(Math.random()*10);
				var a4=parseInt(Math.random()*10);
				var a5=parseInt(Math.random()*10);
				var a6=parseInt(Math.random()*10);
				var num=document.getElementById(span1);		
				if(a1<=0 || a2<=0 || a3<=0 || a4<=0||a5<=0||a6<=0){
					getn();		
				}				
				var b=a1;
				var c=a2;
				var d=a3;
				var e=a4;
				var f=a5;
				var g=a6;	
				span1.innerHTML=b+""+c+""+d+""+e+""+f+""+g;
			 }
	function checkNum()
			{
				var nn=document.getElementById("span1").innerHTML;
				var num=""+document.getElementById("Num").value;
		        if(num!=nn){
		        	alert("验证码错误！");
		        	getn();
		        	flag=false;
		        }else{
		        	flag = true;
		        }
		        
			}
	
	function HOPE_return()
	{
		 HOPE_check=document.HOPE_form;
		if (HOPE_check.userid.value==""){
			HOPE_check.userid.focus();
			alert("请输入您的用户名");
			flag=false;
			return flag;
		}	
		if (HOPE_check.password.value==""){
				HOPE_check.password.focus();
				alert("您的密码不能为空");
				flag=false;
				return flag;
		}	
		if (HOPE_check.password.value.length>16){
				HOPE_check.password.focus();
				alert("您的密码长度最多只能输入16位!");
				flag=false;
				return flag;
		}
		checkNum();
		getn();
		return flag;
	}
	getn();
</script>

<style type="text/css">
<!--
.STYLE43 {
	font-size: medium;
	font-weight: bold;
	font-family: "新宋体";
}
-->
</style>
<body onload="getn()" topMargin="0" leftmargin="0" marginheight="0" bgColor="#FAFAFA" style="background-image: url('images/images/bg.jpg');background-repeat: repeat-x;">
<div style="left:120px;top:120px;position: absolute;">
<FORM name="HOPE_form" action="adminLogin.action" method="post" onSubmit="return HOPE_return()">
          <table width="1003" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="500" valign="top" background="images/images/logins_02.jpg"><table width="499" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="35">　</td>
                </tr>
                <tr>
                  <td><img src="images/images/logins_09.png" width="498" height="75" alt="" /></td>
                </tr>
                <tr>
                  <td><img src="images/images/logins_13.gif" width="500" height="126" alt="" /></td>
                </tr>
                <tr>
                  <td height="300" valign="top" background="images/images/logins_18.jpg"><table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
                      <tr>
                        <td height="2">&nbsp;　欢迎使用Today电子商务门户网站后台管理系统，如果您对我们有建议，或对本系统有什么不懂之处；请进入 <a href="http://www.today.com/bbs"><span class="STYLE9">Today电子商务论坛</span></a> 发贴交流</td>
                      </tr>
                      <tr>
                        <td height="74" valign="top">
						<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
                            <tr>
                              <td valign="top">
								<table width="100%" height="68%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td>
      
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td ></td>
              <td ></td>
              <td valign="top" ><table border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td ></td>
                  <td ><table width="100%"   border="0" cellpadding="0" cellspacing="0">
                    <tr></tr>
                    <tr>
                      <td >&nbsp;</td>
                      <td width="85" valign="bottom" ></td>
                    </tr>
                  </table></td>
                </tr>
                <tr>
                  <td  colspan="2"><table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="20"  rowspan="2"><img src="images/images/User_Login_0_15.png" width="20" height="30" alt=""></td>
                      <td width="70"  height="20"><span class="STYLE6">用户名称：</span></td>
                      <td width="20"  rowspan="2" align="center" valign="middle"><img src="images/images/User_Login_0_19.png" alt="" width="20" height="30"></td>
                      <td width="76"><span class="STYLE6">用户密码：</span></td>
                      <td width="29" rowspan="2" align="center"><img src="images/images/User_Login_0_23.png" alt="" width="29" height="30"></td>
                      <td ><span class="STYLE6">验证码：</span></td>
                      <td width="75"><span style="color:red;font-weight: bold;">${message}</span></td>

                      <td width="32" rowspan="2" align="center">　</td>
                     
                    </tr>
                    <tr>
                      <td><input name="adminName" type=text id="userid" size="12" value="请输入用户名" style="color:gray;" onfocus="entityTextfocus('adminName')"></td>
                      <td><label>
                        <input type="password" size=12 name="adminPassword" onFocus="this.select();" readOnly onKeyDown="Calc.password.value=this.value" onChange="Calc.password.value=this.value" onclick= "password1=this;showkeyboard();this.readOnly=1;Calc.password.value=''">
                      </label>
					  </td>
                      <td width="71"><INPUT name="verifycode" id="Num" type=text size=9 maxlength="6" onblur="checkNum()"></td>  
                      <td width="185" valign="bottom" align="right"><span id="span1"
									style="background-image:url('images/images/checkcode.jpg'); font-size:23px; color:#FFCC99; width:60px;font-family:'华文行楷';text-align: center;"></span><a style="margin-top:5px; color:#FF0000; float:left;" href="javascript:void(0);"
									onclick="getn()">看不清图片？ 立刻刷新</a>
									<script type="text/javascript">
										getn();
									</script>
									
									<label title="登录">
                       <INPUT type=hidden value=Login name=Action> <input type="image" name="imageField" src="images/images/User_Login_0_13.png" style=" width:40px; height:40px;"/>
                      </label></td>                    
                    </tr>
                  </table></td>
                </tr>
                
              </table></td>
            </tr>
          </table>
    
        <script language="JavaScript" type="text/JavaScript">
        SetFocus();
        </script></td>
                            </tr>
                        </table></td>
                      </tr>
                      <tr>
                        <td height="13"><table width="96%" border="0" align="left" cellpadding="0" cellspacing="3">
                          <tr>
                            <td>默认用户名称:admin 用户密码:123456</td>
                          </tr>
                          <tr>
                            <td><span class="STYLE8">服务时间：周一至周日：8:00--12:00，13:00--23:00</span></td>
                          </tr>
                          <tr>
                            <td><span class="STYLE8">售前电话：020-34506590 / 13760746489</span></td>
                          </tr>
                          <tr>
                            <td><span class="STYLE8">QQ在线：406897011 | 781803766 
							| 657248708 |</span></td>
                          </tr>
                        </table></td>
                      </tr>
                  </table></td>
                </tr>
            </table>                  </td>
                </tr>
        </table></td>
  </tr>
</table>
</form>
</div>
</BODY>
</html>
