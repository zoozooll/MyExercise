<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0066)http://china.alibaba.com/member/join.htm?tracelog=main_toolbar_reg -->
<html>
	<head>
		<title>������ϸ�˽�</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="css/style_min.css" />
		<style type="text/css">
		.shophelp{
			font-size: 14px;
		}
			.shophelp table{
			text-align: left;border: 1px solid #AACDED;
			width: 100%;
			}
			.shophelp table td{
			text-align: left;border: 1px solid #AACDED;
			}
			
		</style>
</head>
	<body>
		<div id="container" style="height: auto">
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
				href="index.jsp">��ҳ</a>&gt;&gt;<a href="myshopcartproductlist.jsp">�ҵ�����</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">��Ʒ����</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">���Ӳ�Ʒ</a>&gt;&gt;</span>
	<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus==1}">
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			</c:when>
			<c:when test="${purchaser.purIsvendot=='no'}"><jsp:include page="purleft.jsp"></jsp:include></c:when>
			<c:otherwise><jsp:include page="indexleft.jsp"></jsp:include></c:otherwise>
			</c:choose>
			<div id="main">				
		<div class="shophelp">
		<!--[if !ie]>���� ��ʼ<![endif]-->
		<h3 style="background-color: #AACDED;text-align: left;">������䣺</h3>
			<table>
				<tr>
					<th height="30">���򻮷�</th>
					<th height="30">��������</th>
					<th width="32%">�˷ѱ�׼��Ԫ��</th>
				</tr>
				<tr>
					<td width="21%" rowspan="3" align="center">һ��</td>
					<td width="47%" height="20">����</td>
					<td rowspan="3" align="center">5</td>
				</tr>
				<tr>
					<td height="20">�Ϻ�</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td rowspan="17" align="center">����</td>
					<td height="20">����</td>
					<td rowspan="17" align="center">6</td>
				</tr>
				<tr>
					<td height="20">�㽭</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">���</td>
				</tr>
				<tr>
					<td height="20">ɽ��</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">�㶫���������⣩</td>
				</tr>
				<tr>
					<td height="20">�ӱ�</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">ɽ��</td>
				</tr>
				<tr>
					<td height="20">������</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td rowspan="12" align="center">����</td>
					<td height="20">����</td>
					<td rowspan="12" align="center">15</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">�Ĵ�</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">�½�</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
				<tr>
					<td height="20">�ຣ</td>
				</tr>
				<tr>
					<td height="20">����</td>
				</tr>
		</table>		
		<h4 class="margin_t20"  id="sfbz">���������ȡ��׼һ����</h4>
	<table width="98%" class="tablecss">
				<tr>
					<th height="30" colspan="2">����</th>
					<th width="24%">��ʯ��˫�꣩��Ա</th>
					<th width="24%">���ƻ�Ա</th>
					<th width="26%">������Ա</th>
				</tr>
				<tr>
					<td width="10%" align="center">һ��</td>
					<td width="16%" height="70">�����������أ����Ϻ��������⻷����Ľ�������������������������</td>
					<td align="left">�������������Ŷ�����50Ԫ���������Ͽ�������˷�ȫ��</td>
					<td align="left">�������������Ŷ��������200Ԫ���������Ͽ�������˷�ȫ�⣻���Ŷ�������200Ԫ��ȡ��������˷�5Ԫ</td>
					<td align="left">�������������Ŷ��������400Ԫ���������Ͽ�������˷�ȫ�⣻���Ŷ�������400Ԫ��ȡ��������˷�5Ԫ</td>
				</tr>
				<tr>
					<td align="center">����</td>
					<td height="70">���ա��㽭�����ա����ɽ�������������ϡ����������ϡ����ϡ��㶫���������⣩���ӱ���������������ɽ����������������</td>
					<td align="left">�������������Ŷ�����100Ԫ���������Ͽ�������˷�ȫ��</td>
					<td align="left">�������������Ŷ�����200Ԫ���������Ͽ�������˷�ȫ�⣻���Ŷ�������200Ԫ��ȡ��������˷�6Ԫ</td>
					<td align="left">�������������Ŷ��������400Ԫ���������Ͽ�������˷�ȫ�⣻���Ŷ�������400Ԫ��ȡ��������˷�6Ԫ</td>
				</tr>
				<tr>
					<td align="center">����</td>
					<td height="70">���ࡢ�������Ĵ������졢�½������������ϡ����ɡ����ݡ����ġ����ء��ຣ</td>
					<td align="left">�������������Ŷ�����400���������Ͽ�������˷�ȫ�⣻���Ŷ�������400Ԫ��ȡ��������˷�15Ԫ</td>
					<td align="left">�������������Ŷ��������800���������Ͽ�������˷�ȫ�⣻���Ŷ�������800Ԫ����ȡ��������˷�15Ԫ</td>
					<td align="left">�������������ƽ�ÿ�Ŷ����̶���ȡ��������˷�15Ԫ</td>
				</tr>
			</table>
				<ul class="margin_t20 margin_b20">
					<li>����ע�⣺</li>
					<li>1.�����ѡ�����ָ����Բͨ����ͨ����˳���ݣ���û�����˷��Żݣ��������ָ������ݻ򾩶�ѡ�õļ۸�ϵ����Ŀ�ݹ�˾���ʾֿ�����������˵ȣ�
</li>
					<li>2.�������ز��ṩ������������ͷ�ʽ��</li>
					<li>3.�綩���ջ���ַ������ѡ��ݵ����ͷ�Χ�������̳��п���ֱ��ת���ʾְ�����ȫ�����ؿɵ�������䷽ʽ��</li>
					<li>4.��ۡ����š�̨�塢���㵺�����Ŀ�ݷ�һ�ɰ�ʵ���˷���ȡ��</li>
					<li>5.��֧�ֿ���������������ǵĳ��з������������ջ���ַ��������ܴ��Ϻ��������Ļ�����������ķ�����</li>
					<li>6.�¶�����ϵͳ�Զ������˷ѣ�������������ʵ���˷ѵ���ϵͳ�Զ�������˷ѣ���˾�Ὣ������˷��Ի��ֵ���ʽ���������ľ����˻��С���1Ԫ�����=10�����֣������֡����������롣���磺ʵ���˷Ѷ���ȡ1.58Ԫ���򷵻�����16�֣�ʵ���˷Ѷ���ȡ��1.32Ԫ���򷵻�����13�֣���</li>
		</ul>
				<table width="98%" class="tablecss">
				<tr>
					<th width="18%" height="30">��������</th>
					<th width="82%">����ʡ��</th>
				</tr>
				<tr>
					<td height="30" align="center">����</td>
					<td align="left">��������򡢺ӱ���ɽ�������ϡ����������֡������������ɹš�ɽ�����������Ĵ������졢���ݡ����ϡ����ء����������ࡢ�ຣ�����ġ��½�</td>
				</tr>
				<tr>
					<td height="30" align="center">�Ϻ�</td>
					<td align="left">���ա��㽭���Ϻ������ա����㵺</td>
				</tr>
				<tr>
					<td height="30" align="center">����</td>
					<td align="left">�㶫�����������������ϡ����������ϡ�̨�塢��ۡ�����</td>
				</tr>
		</table>
		<div class="align_Right margin_t5"><a href="#">���ض���</a></div>
				<!--[if !ie]>���� ����<![endif]-->
				<!--[if !ie]>help_tips ��ʼ<![endif]-->
		
	</div>
	</div>
</div>
</body>
</html>

<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->