<%@ page language="java" import="java.sql.*" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>��������</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
		<link rel="stylesheet" href="css/common.css" type="text/css" />
		<title>�û���Ϣ</title>
	</head>

	<body>
		<div id="man_zone">
			<table width="99%" border="0" align="center" cellpadding="3"
				cellspacing="1" class="table_style">
				<tr>
					<td class="left_title_2">
						�û�����
					</td>
					<td>
						<c:if test="${purchaser.purIsvendot=='no'}">���</c:if>
						<c:if test="${purchaser.purIsvendot=='yes'}">����</c:if>
					</td>
				</tr>
				<tr>
					<td class="left_title_1">
						��˾����
					</td>
					<td>
						${purchaser.purName }
					</td>
				</tr>
				<tr>
					<td class="left_title_2">
						��˾�绰
					</td>
					<td>
						${purchaser.purTelephone }
					</td>
				</tr>
				<tr>
					<td class="left_title_1">
						��˾���ڵ�
					</td>
					<td>
						${purchaser.purProvince }ʡ &nbsp;&nbsp;&nbsp;${purchaser.purCity }
						��
					</td>
				</tr>
				<tr>
					<td class="left_title_2">
						��Ӫ��ַ
					</td>
					<td>
						${purchaser.purAddress}
					</td>
				</tr>
				<tr>
					<td class="left_title_1">
						��������
					</td>
					<td>
						${purchaser.purPostalcode}
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
							${purchaser.vender.venShopcard}
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class="left_title_2">
							��˾����
						</td>
						<td>
							${purchaser.vender.venFax}
						</td>
					</tr>					
					<tr>
						<td class="left_title_1">
							��˾��ϵ��
						</td>
						<td>
							${purchaser.vender.venLinkman}
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							��ϵ�˵绰
						</td>
						<td>
							${purchaser.vender.venLinkmanphone}
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							��˾�ʼ���ַ
						</td>
						<td>
							${purchaser.vender.venEmail}
						</td>
					</tr>
					<tr>
						<td class="left_title_1">
							�������
						</td>
						<td>
							<c:choose>
								<c:when test="${purchaser.vender.venStatus==-1}">
    				���δͨ��
    			</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${purchaser.vender.venStatus==0}">
    				δ���
    			</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${purchaser.vender.venStatus==1}">
    				�����ͨ��
    			</c:when>
							</c:choose>
						</td>
					</tr>
				</c:if>
				<tr>
					<td  colspan="2" align="center" style="text-align: center;">
						<input onclick="window.location='adminFindVenById.action?purId=${purchaser.purId}'"
							type="button" value="�޸���Ϣ">
					</td>
				</tr>
			</table>
		</div>

	</body>
</html>