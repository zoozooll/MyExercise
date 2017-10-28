<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ include file="head.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE>注册</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=gb2312">
		<link href="css/register.css" rel="stylesheet" type="text/css">
		<!-- 表单验证 -->
		<script src="js/prototype.js" type="text/javascript"></script>
		<script src="js/validation_cn.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="js/style_min.css" />
		<script language="javascript" src="js/global.js"></script>
		<script language="javascript" src="js/province.js"></script>
		<script language="javascript" src="js/city.js"></script>		
		<script type="text/javascript" src="dwr/engine.js"></script>
		<script type="text/javascript" src="dwr/util.js"></script>
		<script type="text/javascript" src="dwr/interface/userDao.js"></script>
		<script type="text/javascript">
			var flag=false;
			function checkNameExist(){
				var userName=dwr.util.getValue("purName");
		        userDao.checkUserName(userName,function(data){
		        	  if(data=="用户名已注册")
		        	  {
		        	  	dwr.util.setValue("mes2","");
		        	  	dwr.util.setValue("mes1",data)
		        	  }
		        	  else
		        	  {
		        	  	dwr.util.setValue("mes1","");
		        	  	dwr.util.setValue("mes2",data)
		        	  } }); 	
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
			checkNum();
			return flag;
		}
	</script>
<style type="text/css">
	body center div{margin: 5px 1px 1px 1px;;	
	}	
</style>

	</HEAD>
<BODY onload="getn()">

		<center>
			<!--head-->
			<DIV id="container">
				<DIV id="title">
					<ul>
						<LI style="text-align: center;clear: none;">
							免费注册
						</LI>
					</ul>
				</DIV>
				<DIV id="top">
					<UL>
						<LI>
							注册步骤：
						</LI>
						<LI>
							填写注册信息
						</LI>
						<LI>
							&gt;
						</LI>
						<LI>
							选择会员类型(买家|卖家)
						</LI>
						<LI>
							&gt;
						</LI>
						<LI>
							邮箱验证
						</LI>
						<LI>
							&gt;
						</LI>
						<LI>
							注册成功
						</LI>
						<LI style="float:right;padding-right: 20px;">
							<span style="color:#FF0000;">* </span>为必填项
						</LI>
					</UL>
					<div style="margin-top:15px; ">
					<hr width="95%"/>
					</div>
				</DIV>
				<div style="left: 200px;" >
				<form action="registerAction.action" method="post" name="reg" onsubmit="return getflag()"
					class='required-validate'>
					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder;font-size: 14px;">
								设置您的帐户信息
							</li>
						</ul>
					</div>
					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder;">
								<span>&nbsp;&nbsp;公司名称</span>
								<span class="red">*</span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=20 size=32 name="purName"
									class="required min-length-4 max-length-20"	 onblur="checkNameExist()"> </span>
								<span id="mes1" style="color: red"></span>
								<span id="mes2" style="color: green"></span>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;公司密码</span>
								<span class="red">* </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT type="password" maxLength=20 size=35
										name="purPassword" class="required min-length-6 max-length-20">
								</span>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp; 确认密码</span>
								<span class="red">* </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT type="password" maxLength=20 size=35
										name="purPassword2" class="required equals-purPassword">
								</span>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;公司电话</span>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=20 size=32 name="purTelephone"
										class="validate-phone"> </span>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;公司地址</span>
								&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="purProvince"
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
								</select>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;经营地址</span>
								&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purAddress"> </span>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;邮政编码</span>
								&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purPostalcode"
										class="validate-zip"> </span>
							</li>
						</ul>
					</div>
					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;公司备注</span>
								&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purRemark"> </span>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder;">
								<span>&nbsp;&nbsp;验证字码</span>
								<span class="red">* </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;								
								<span><INPUT maxLength=6 size=12 name="purNum" id="Num"
										class="required min-length-6 max-length-6" onblur="checkNum()"> </span>
								<span id="span1"
									style="background-image:url(image/checkcode.jpg); font-size:19px; color:#FFCC99; width:90px;"></span>
							</li>
							<li>
								<a href="javascript:void(0);"
									style="margin-top:5px; color:#FF0000; float:left;"
									onclick="getn()">看不清图片？ 立刻刷新</a>

							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;申请为经销商</span> &nbsp;&nbsp;&nbsp;
								<span><input name="purIsvendot" type="radio" value="no"
										onClick="javascript:getElementById('show').style.display='none';"
										checked>暂不申请</span>
								<span><input name="purIsvendot" type="radio" value="yes"
										onClick="javascript:getElementById('show').style.display='block';">立刻申请</span>
							</li>
						</ul>
					</div>

					<div style="width:950px; height:10px; float:left"></div>

					<div id="show" style="display:none">
						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;公司简称</span>
									<span class="red">* </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venShortname"
											class='required'> </span>
								</li>
							</ul>
						</div>

						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;营业执照</span>
									<span class="red">* </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venShopcard"
											class='required'> </span>
								</li>
							</ul>
						</div>

						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;公司传真</span>
									&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venFax"
											class="validate-phone"> </span>
								</li>
							</ul>
						</div>

						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;联系人员</span>
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venLinkman"> </span>
								</li>
							</ul>
						</div>

						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;联系电话</span>
									&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32
											name="venLinkmanphone" class='validate-mobile-phone'>
									</span>
								</li>
							</ul>
						</div>

						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;公司邮箱</span>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
									<span><INPUT maxLength=100 size=32 name="venEmail"
											class='validate-email'> </span>
								</li>
							</ul>
						</div>
					</div>



					<div style="width:950px; height:50px;">
					</div>

					<DIV>
						<INPUT style="FONT-SIZE: 14px; HEIGHT: 30px" type="submit"
							class="button" value=同意服务条款，提交注册信息 name=Submit>
					</DIV>
				</form>
				</div>
				<script language="javascript">
				<!--
				    initProvince(getElementByName("purProvince"), "");
				    initCity(getElementByName("purCity"), "", getElementByName("purProvince").value, "1");
				-->
				</script>
			</DIV>
		</center>
	</BODY>
</HTML>
<!-- 底部页面开始 -->
<%@ include file="footer.jsp" %>
<!-- 底部页面结束 -->
<%@ include file="footer.jsp"%>