<%@ page language="java" import="java.sql.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>个人资料</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
		<link rel="stylesheet" href="css/common.css" type="text/css" />
		<title>用户信息</title>
	</head>

	<body>
		<div id="man_zone">
			<table width="99%" border="0" align="center" cellpadding="3"
				cellspacing="1" class="table_style">
				<tr>
					<td class="left_title_2">
						用户类型
					</td>
					<td>
						<c:if test="${purchaser.purIsvendot=='no'}">买家</c:if>
						<c:if test="${purchaser.purIsvendot=='yes'}">卖家</c:if>
					</td>
				</tr>
				<tr>
					<td class="left_title_1">
						公司名称
					</td>
					<td>
						${purchaser.purName }
					</td>
				</tr>
				<tr>
					<td class="left_title_2">
						公司电话
					</td>
					<td>
						${purchaser.purTelephone }
					</td>
				</tr>
				<tr>
					<td class="left_title_1">
						公司所在地
					</td>
					<td>
						${purchaser.purProvince }省 &nbsp;&nbsp;&nbsp;${purchaser.purCity }
						市
					</td>
				</tr>
				<tr>
					<td class="left_title_2">
						经营地址
					</td>
					<td>
						${purchaser.purAddress}
					</td>
				</tr>
				<tr>
					<td class="left_title_1">
						邮政编码
					</td>
					<td>
						${purchaser.purPostalcode}
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
							${purchaser.vender.venShopcard}
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class="left_title_2">
							公司传真
						</td>
						<td>
							${purchaser.vender.venFax}
						</td>
					</tr>					
					<tr>
						<td class="left_title_1">
							公司联系人
						</td>
						<td>
							${purchaser.vender.venLinkman}
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							联系人电话
						</td>
						<td>
							${purchaser.vender.venLinkmanphone}
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							公司邮件地址
						</td>
						<td>
							${purchaser.vender.venEmail}
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							申请情况
						</td>
						<td>
							<c:choose>
								<c:when test="${purchaser.vender.venStatus==-1}">
    				审核未通过
    			</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${purchaser.vender.venStatus==0}">
    				未审核
    			</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${purchaser.vender.venStatus==1}">
    				审核已通过
    			</c:when>
							</c:choose>
						</td>
					</tr>
				</c:if>
				<tr>
					<td  colspan="2" align="center" style="text-align: center;">
						<input onclick="window.location='adminFindVenById.action?purId=${purchaser.purId}'"
							type="button" value="修改信息">
					</td>
				</tr>
			</table>
		</div>

	</body>
</html>