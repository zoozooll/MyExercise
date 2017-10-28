<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE>�������|����</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=gb2312">
		<link href="../css/register.css" rel="stylesheet" type="text/css">
		<!-- ����֤ -->
		<script src="../js/prototype.js" type="text/javascript"></script>
		<script src="../js/validation_cn.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="../js/style_min.css" />
		<script language="javascript" src="../js/province.js"></script>
		<script language="javascript" src="../js/city.js"></script>
		<script language="javascript" src="../js/global.js"></script>
		<link rel="stylesheet" href="css/common.css" type="text/css" />
	</HEAD>
	<BODY>
		<h4 align="center">
			�޸��û�
		</h4>
		<div id="man_zone">
			<form action="adminmodifyUser.action?purId=${purchaser.purId}" method="post" name="reg"
				class='required-validate'>
				<table width="99%" border="0" align="center" cellpadding="3"
					cellspacing="1" class="table_style">
					<tr>
						<td class="left_title_1">
							��˾����
						</td>
						<td>
							<INPUT maxLength=20 size=32 name="purName"
								value="${purchaser.purName}"
								class="required min-length-4 max-length-20" readonly="readonly">							
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							�û�����:
						</td>
						<td>
							<INPUT type="text" maxLength=20 size=32
								value="${purchaser.purPassword}" name="purPassword"
								class="required min-length-6 max-length-20">
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							��˾�绰
						</td>
						<td>
							<INPUT maxLength=20 size=32 name="purTelephone"
								value="${purchaser.purTelephone}"
								class="required validate-phone">
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							��˾���ڵ�
						</td>
						<td>
							<input type="text" name="purProvince"
								value="${purchaser.purProvince}" class="required" />
							ʡ

							<input type="text" name="purCity" value="${purchaser.purCity}"
								class="required" />
							��
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							��Ӫ��ַ
						</td>
						<td>
							<INPUT maxLength=100 size=32 name="purAddress"
								value="${purchaser.purAddress}" class="required">
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							��������
						</td>
						<td>
							<INPUT maxLength=100 size=32 name="purPostalcode"
								value="${purchaser.purPostalcode}" class="required validate-zip">
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							��˾��ע
						</td>
						<td>
							<INPUT maxLength=100 size=32 name="purRemark"
								value="${purchaser.purRemark}" class="required">
						</td>
					</tr>

					<c:if test="${purchaser.purIsvendot=='yes'}">
						<tr>
							<td class="left_title_2">
								��˾���
							</td>
							<td>
								${purchaser.vender.venShortname}
							</td>
						</tr>
						<tr>
							<td class="left_title_1">
								Ӫҵִ��
							</td>
							<td>
								<INPUT maxLength=100 size=32 name="venShopcard"
									value="${purchaser.vender.venShopcard}" class='required'
									readonly="readonly">
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="left_title_2">
								��˾����
							</td>
							<td>
								<INPUT maxLength=100 size=32 name="venFax"
									value="${purchaser.vender.venFax}"
									class="required validate-phone">
							</td>
						</tr>
						<tr>
							<td class="left_title_1">
								��˾��ϵ��
							</td>
							<td>
								<INPUT maxLength=100 size=32 name="venLinkman"
									value="${purchaser.vender.venLinkman}" class="required">
							</td>
						</tr>
						<tr>
							<td class="left_title_2">
								��ϵ�˵绰
							</td>
							<td>
								<input maxLength=100 size=32 name="venLinkmanphone"
									value="${purchaser.vender.venLinkmanphone}"
									class='required validate-mobile-phone'>
							</td>
						</tr>
						<tr>
							<td class="left_title_1">
								��˾�ʼ���ַ
							</td>
							<td>
								<INPUT maxLength=100 size=32 name="venEmail"
									value="${purchaser.vender.venEmail}"
									class='required validate-email'>
							</td>
						</tr>

					</c:if>
					<tr>
						<td colspan="2" align="center" style="text-align: center;">
							<input style="FONT-SIZE: 14px; HEIGHT: 30px" type="submit"
								class="button" value="�޸��û�">
						</td>
					</tr>
				</table>
			</form>
		</div>

	</BODY>
</HTML>