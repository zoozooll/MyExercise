<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="head.jsp"%>

<%-- �۸񱣻�ҳ�� --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>�޽�����</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/css.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery-1[1].2.1.pack.js" type="text/javascript"></script>
		<script src="js/slide.js" type="text/javascript"></script>
		<style type="text/css">
.addproductpage table input {
	border: 1px solid black;
	width: 200px;
	margin: 10px 5px;
}
</style>
	</head>

	<body>
		<div id="container" style="height: auto">
			<span style="text-align: left;">���������ڵ�λ����:Today&gt;&gt;<a
				href="index.jsp">��ҳ</a>&gt;&gt;<a
				href="findAllPurchasersAction.action">��Ʒ����</a>&gt;&gt;<a
				style="color: red; font-weight: bold;">ɾ����Ʒ</a>&gt;&gt;</span>
		<c:choose>
					<c:when test="${purchaser.purIsvendot=='yes'&&purchaser.vender.venStatus==1}">
			<jsp:include page="mycompanyleft.jsp"></jsp:include>
			</c:when>
			<c:when test="${purchaser.purIsvendot=='no'}"><jsp:include page="purleft.jsp"></jsp:include></c:when>
			<c:otherwise><jsp:include page="indexleft.jsp"></jsp:include></c:otherwise>
			</c:choose>		
			<div id="main"
				style="border: 1px solid red; border-top: none; padding-bottom: 50px;">
<div id="latest" style="height: 240px; top: 67px; left: 152px;">
				<div class="right">
					<!--[if !ie]>���� ��ʼ<![endif]-->
					<h3 style="background-color: #AACDED;">
						�۸񱣻���
					</h3>
					<div class="help_box">
						<p>
							�����̳ǵ���Ʒ�۸����г��۸�Ĳ���ÿ�ն������Ǽۡ����ۻ����Żݵȱ仯��������궩����۸����˱仯��ʵ�����¼۱�ԭ��
						</p>
						<ul class="List_Number">
							<li>
								�ȿ�����������������Ʒ���ֽ�����Ϊ��
								<br />
								����Ʒ��δ����ʱ�����¼۸���㣬�����Ǯ���Ի��ֵ���ʽ��1Ԫ=10�����֣��ڶ�����ɺ󷵻������ľ����˻��С�
								<ul class="List_Letter">
									<li>
										�����֧����ϵ�����δ���⣬���ɽ����ҵľ���-
										<a href="http://jd2008.360buy.com/User_jiabao.aspx"
											target="_blank">�۸񱣻�</a>ҳ���Զ����룬����ɹ���ึ����Ի�����ʽ���������ľ����˻��У�
									</li>
									<li>
										һ����Ʒ���⣬��Ʒ���������ܼ۱�����ȡ������������ȡ��Ӧ�˷Ѽ������ѡ�
									</li>
								</ul>
							</li>
							<li>
								�������������������Ʒ���ֽ�����Ϊ��
								<ul class="List_Letter">
									<li>
										�ڻ������֮ǰ���ɽ����ҵľ���-
										<a href="http://jd2008.360buy.com/User_jiabao.aspx"
											target="_blank">�۸񱣻�</a>ҳ���Զ����룻
									</li>
									<li>
										����Ʒ�ѳ����������յ�����ĵ����µ�ͷ�ȷ�ϼ۱����ˣ��Կͻ������۸���յ���Ʒʱ�̳Ǽ۸���������ͼ۸���㣨������Ա�뿪�󽫲����ܼ۱�����
									</li>
								</ul>
							</li>
							<li>
								�������ᶩ������������Ʒ���ֽ�����Ϊ��
								<ul class="List_Letter">
									<li>
										�ڻ������֮ǰ���ɽ����ҵľ���-
										<a href="http://jd2008.360buy.com/User_jiabao.aspx"
											target="_blank">�۸񱣻�</a>ҳ���Զ����룻
									</li>
									<li>
										����Ʒ�ѳ������������ʱ�����Ṥ����Ա����۱����룬����Ϊ����ʵ��������������ߺ󽫲������ܼ۱���
									</li>
								</ul>
							</li>
							<li>
								�����ύ��������Ʒ�����Ǽ���Ϊ�������Ż��ڽ���������Ʒ�۸��ǰ������µ�ʱ�ļ۸���㣬�������Ƿ��� ������Ҫ��ͷ�ȷ�ϡ�
							</li>
							<li>
								�ؼ���Ʒ�����ڸ�����Ʒ����У�����������ܼ۸񱣻���
							</li>
						</ul>
					</div>
				</div>
				</div>
			</div>
		</div>


	</body>
</html>
<!-- �ײ�ҳ�濪ʼ -->
<%@ include file="footer.jsp" %>
<!-- �ײ�ҳ����� -->