<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE>增加买家|卖家</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=gb2312">
		<!-- 表单验证 -->
		<script src="../js/prototype.js" type="text/javascript"></script>
		<script src="../js/validation_cn.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="../js/style_min.css" />
		<script language="javascript" src="../js/province.js"></script>
		<script language="javascript" src="../js/city.js"></script>
		<script language="javascript" src="../js/global.js"></script>
		<script type="text/javascript" src="../dwr/engine.js"></script>
		<script type="text/javascript" src="../dwr/util.js"></script>
		<script type="text/javascript" src="../dwr/interface/userDao.js"></script>
		<link rel="stylesheet" href="css/common.css" type="text/css" />
		<script type="text/javascript">
		var flag=false;
			function checkNameExist(){
				var userName=dwr.util.getValue("purName");
		        userDao.checkUserName(userName,function(data){
		              dwr.util.setValue("mes",data)}); 	
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
		        	flag=false;
		        }else{
		        	flag = true;
		        }
		        
			}
			
		function getflag(){
			checkNum()
			return flag;
		}
	</script>
	</HEAD>
<BODY onload="getn()">
		<center>
		<h3>增加买家|卖家</h3>
		<div id="man_zone">
		<form action="adminaddUser.action" method="post" name="reg" onsubmit="return getflag();"
					class='required-validate'>
  <table width="99%" border="0" align="center"  cellpadding="3" cellspacing="1" class="table_style">   
    <tr>
      <td class="left_title_2">公司名称:</td>
      <td><INPUT maxLength=20 size=32 name="purName"
									class="required min-length-4 max-length-20"	 onblur="checkNameExist()">
								<span id="mes"></span></td>
    </tr>
    <tr>
      <td class="left_title_1">公司密码</td>
      <td><INPUT type="password" maxLength=20 size=32
										name="purPassword" class="required min-length-6 max-length-20"></td>
    </tr>
    <tr>
      <td class="left_title_2">确认密码</td>
      <td><INPUT type="password" maxLength=20 size=32
										name="purPassword2" class="required equals-purPassword"></td>
    </tr>
    <tr>
      <td class="left_title_1">公司电话</td>
      <td><INPUT maxLength=20 size=32 name="purTelephone"
										class="required validate-phone"></td>
    </tr>
    <tr>
      <td class="left_title_2">公司地址</td>
      <td><select name="purProvince"
									onChange="initCity(getElementByName('purCity'), '', this.value, '1')"
									style="width:115" class="button">
									<option value="-1">
										-请选择(省)-
									</option>
								</select>
								<select name="purCity" style="width:115" class="button">
									<option value="-1">
										-请选择(市)-
									</option>
								</select></td>
    </tr>
    <tr>
      <td class="left_title_1">经营地址</td>
      <td><INPUT maxLength=100 size=32 name="purAddress"
										class="required"> </td>
    </tr>
    <tr>
      <td class="left_title_2">邮政编码</td>
      <td><INPUT maxLength=100 size=32 name="purPostalcode"
										class="required validate-zip"></td>
    </tr>
    <tr>
      <td class="left_title_1">公司备注</td>
      <td><INPUT maxLength=100 size=32 name="purRemark"
										class="required"></td>
    </tr>
    <tr>
      <td class="left_title_2">验证字码</td>
      <td><INPUT maxLength=6 size=12 name="purNum" id="Num"
										class="required min-length-6 max-length-6" onblur="checkNum()"><span id="span1"
									style="background-image:url(../image/checkcode.jpg); font-size:19px; color:#FFCC99; width:90px;"></span> <a href="javascript:void(0);"
									style="margin-top:5px; color:#FF0000; float:left;"
									onclick="getn()">看不清图片？ 立刻刷新</a></td>
    </tr>
     <tr>
      <td class="left_title_1">申请为经销商</td>
      <td><input name="purIsvendot" type="radio" value="no"
										onClick="document.getElementById('show').style.display='none';"
										checked>暂不申请
								<input name="purIsvendot" type="radio" value="yes"
										onClick="document.getElementById('show').style.display='block';">立刻申请</td>
    </tr> 
    </table>
    <table id="show" width="99%" border="0" align="center"  cellpadding="3" cellspacing="1" class="table_style" style="display: none;" >
	    <tr>
	      <td class="left_title_2">公司简称</td>
	      <td><INPUT maxLength=100 size=32 name="venShortname"
												class='required'></td>
	    </tr>
	    <tr>
	      <td class="left_title_1">营业执照</td>
	      <td><INPUT maxLength=100 size=32 name="venShopcard"
												class='required'> </td>
	    </tr>
	     <tr>
	      <td class="left_title_2">公司传真</td>
	      <td><INPUT maxLength=100 size=32 name="venFax"
												class="required validate-phone"> </td>
	    </tr>
	     <tr>
	      <td class="left_title_1">公司联系人</td>
	      <td><INPUT maxLength=100 size=32 name="venLinkman"
												class="required"></td>
	    </tr>
	     <tr>
	      <td class="left_title_2">联系人电话</td>
	      <td><INPUT maxLength=100 size=32
												name="venLinkmanphone" class='required validate-mobile-phone'></td>
	    </tr>
	     <tr>
	      <td class="left_title_1">公司邮件地址</td>
	      <td><INPUT maxLength=100 size=32 name="venEmail"
												class='required validate-email'></td>
	    </tr>
	     </table>
	       <table width="99%" border="0" align="center"  cellpadding="3" cellspacing="1" class="table_style" >
	    	<tr>
	      <td colspan="2" style="text-align: center;"><INPUT style="FONT-SIZE: 14px; HEIGHT: 30px ;width: 60px;" type="submit"
								class="button" value="提交"></td>
	    </tr>
	    </table> 
  
  </form>
</div>
				<script language="javascript">
				<!--
				    initProvince(getElementByName("purProvince"), "");
				    initCity(getElementByName("purCity"), "", getElementByName("purProvince").value, "1");
				-->
				</script>						
		</center>
	</BODY>
</HTML>