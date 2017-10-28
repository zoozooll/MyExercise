<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE>增加买家|卖家</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=gb2312">
		<link href="../css/register.css" rel="stylesheet" type="text/css">
		<!-- 表单验证 -->
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
			修改用户
		</h4>
		<div id="man_zone">
			<form action="adminmodifyUser.action?purId=${purchaser.purId}" method="post" name="reg"
				class='required-validate'>
				<table width="99%" border="0" align="center" cellpadding="3"
					cellspacing="1" class="table_style">
					<tr>
						<td class="left_title_1">
							公司名称
						</td>
						<td>
							<INPUT maxLength=20 size=32 name="purName"
								value="${purchaser.purName}"
								class="required min-length-4 max-length-20" readonly="readonly">							
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							用户密码:
						</td>
						<td>
							<INPUT type="text" maxLength=20 size=32
								value="${purchaser.purPassword}" name="purPassword"
								class="required min-length-6 max-length-20">
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							公司电话
						</td>
						<td>
							<INPUT maxLength=20 size=32 name="purTelephone"
								value="${purchaser.purTelephone}"
								class="required validate-phone">
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							公司所在地
						</td>
						<td>
							<input type="text" name="purProvince"
								value="${purchaser.purProvince}" class="required" />
							省

							<input type="text" name="purCity" value="${purchaser.purCity}"
								class="required" />
							市
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							经营地址
						</td>
						<td>
							<INPUT maxLength=100 size=32 name="purAddress"
								value="${purchaser.purAddress}" class="required">
						</td>
					</tr>
					<tr>
						<td class="left_title_2">
							邮政编码
						</td>
						<td>
							<INPUT maxLength=100 size=32 name="purPostalcode"
								value="${purchaser.purPostalcode}" class="required validate-zip">
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							公司备注
						</td>
						<td>
							<INPUT maxLength=100 size=32 name="purRemark"
								value="${purchaser.purRemark}" class="required">
						</td>
					</tr>

					<c:if test="${purchaser.purIsvendot=='yes'}">
						<tr>
							<td class="left_title_2">
								公司简称
							</td>
							<td>
								${purchaser.vender.venShortname}
							</td>
						</tr>
						<tr>
							<td class="left_title_1">
								营业执照
							</td>
							<td>
								<INPUT maxLength=100 size=32 name="venShopcard"
									value="${purchaser.vender.venShopcard}" class='required'
									readonly="readonly">
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td class="left_title_2">
								公司传真
							</td>
							<td>
								<INPUT maxLength=100 size=32 name="venFax"
									value="${purchaser.vender.venFax}"
									class="required validate-phone">
							</td>
						</tr>
						<tr>
							<td class="left_title_1">
								公司联系人
							</td>
							<td>
								<INPUT maxLength=100 size=32 name="venLinkman"
									value="${purchaser.vender.venLinkman}" class="required">
							</td>
						</tr>
						<tr>
							<td class="left_title_2">
								联系人电话
							</td>
							<td>
								<input maxLength=100 size=32 name="venLinkmanphone"
									value="${purchaser.vender.venLinkmanphone}"
									class='required validate-mobile-phone'>
							</td>
						</tr>
						<tr>
							<td class="left_title_1">
								公司邮件地址
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
								class="button" value="修改用户">
						</td>
					</tr>
				</table>
			</form>
		</div>

	</BODY>
</HTML>