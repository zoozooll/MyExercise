<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ include file="head.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE>ע��</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=gb2312">
		<link href="css/register.css" rel="stylesheet" type="text/css">
		<!-- ����֤ -->
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
		        	  if(data=="�û�����ע��")
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
		        	alert("��֤�����");
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
							���ע��
						</LI>
					</ul>
				</DIV>
				<DIV id="top">
					<UL>
						<LI>
							ע�Ჽ�裺
						</LI>
						<LI>
							��дע����Ϣ
						</LI>
						<LI>
							&gt;
						</LI>
						<LI>
							ѡ���Ա����(���|����)
						</LI>
						<LI>
							&gt;
						</LI>
						<LI>
							������֤
						</LI>
						<LI>
							&gt;
						</LI>
						<LI>
							ע��ɹ�
						</LI>
						<LI style="float:right;padding-right: 20px;">
							<span style="color:#FF0000;">* </span>Ϊ������
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
								���������ʻ���Ϣ
							</li>
						</ul>
					</div>
					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder;">
								<span>&nbsp;&nbsp;��˾����</span>
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
								<span>&nbsp;&nbsp;��˾����</span>
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
								<span>&nbsp; ȷ������</span>
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
								<span>&nbsp;&nbsp;��˾�绰</span>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=20 size=32 name="purTelephone"
										class="validate-phone"> </span>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;��˾��ַ</span>
								&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="purProvince"
									onChange="initCity(getElementByName('purCity'), '', this.value, '1')"
									style="width:115" class="button">
									<option value="-1">
										-��ѡ��(ʡ)-
									</option>
								</select>
								<select name="purCity" style="width:115" class="button">
									<option value="-1">
										-��ѡ��(��)-
									</option>
								</select>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;��Ӫ��ַ</span>
								&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purAddress"> </span>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;��������</span>
								&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purPostalcode"
										class="validate-zip"> </span>
							</li>
						</ul>
					</div>
					<div style="float: left; width: 950px">
						<ul>
							<li style="float: left; font: bolder">
								<span>&nbsp;&nbsp;��˾��ע</span>
								&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span><INPUT maxLength=100 size=32 name="purRemark"> </span>
							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder;">
								<span>&nbsp;&nbsp;��֤����</span>
								<span class="red">* </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;								
								<span><INPUT maxLength=6 size=12 name="purNum" id="Num"
										class="required min-length-6 max-length-6" onblur="checkNum()"> </span>
								<span id="span1"
									style="background-image:url(image/checkcode.jpg); font-size:19px; color:#FFCC99; width:90px;"></span>
							</li>
							<li>
								<a href="javascript:void(0);"
									style="margin-top:5px; color:#FF0000; float:left;"
									onclick="getn()">������ͼƬ�� ����ˢ��</a>

							</li>
						</ul>
					</div>

					<div style="float:left; width:950px">
						<ul>
							<li style="float:left; font:bolder">
								<span>&nbsp;&nbsp;����Ϊ������</span> &nbsp;&nbsp;&nbsp;
								<span><input name="purIsvendot" type="radio" value="no"
										onClick="javascript:getElementById('show').style.display='none';"
										checked>�ݲ�����</span>
								<span><input name="purIsvendot" type="radio" value="yes"
										onClick="javascript:getElementById('show').style.display='block';">��������</span>
							</li>
						</ul>
					</div>

					<div style="width:950px; height:10px; float:left"></div>

					<div id="show" style="display:none">
						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;��˾���</span>
									<span class="red">* </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venShortname"
											class='required'> </span>
								</li>
							</ul>
						</div>

						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;Ӫҵִ��</span>
									<span class="red">* </span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venShopcard"
											class='required'> </span>
								</li>
							</ul>
						</div>

						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;��˾����</span>
									&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venFax"
											class="validate-phone"> </span>
								</li>
							</ul>
						</div>

						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;��ϵ��Ա</span>
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span><INPUT maxLength=100 size=32 name="venLinkman"> </span>
								</li>
							</ul>
						</div>

						<div style="float:left; width:950px">
							<ul>
								<li style="float:left; font:bolder">
									<span>&nbsp;&nbsp;��ϵ�绰</span>
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
									<span>&nbsp;&nbsp;��˾����</span>
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
							class="button" value=ͬ���������ύע����Ϣ name=Submit>
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
<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->
<%@ include file="footer.jsp"%>