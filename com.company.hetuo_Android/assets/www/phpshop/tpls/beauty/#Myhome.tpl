<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:spry="http://ns.adobe.com/spry" xmlns="http://www.w3.org/1999/xhtml">
<head>
{inc file=inc/#head_common.tpl}
<script type="text/javascript" src="jscript/comm/simpleajax.js"></script>
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<link href="css/beauty/myhome.css" rel="stylesheet" type="text/css">
</head>
<body id="index">
<div id="wrapper">
    <!--head -->
    {inc file=inc/#body_top.tpl}
    <!-- <div style="border:#9AD5F2 1px solid; border-top:#FFF 1px solid;">
	   <div style="height:700px; overflow:auto;">
        <div id="s_nav" class="register">会员中心</div> 
        <div id="chanr" style="width:200px; overflow:hidden; float:left;">
           <div id="divClassUB" class="viewState"><a href="#me" onclick="callLoad('divUserHomeR');callServer('myhome.php?switch=me&p='+Math.random(),'divUserHomeR')">我的信息</a></div>
           <div id="divClassUB" class="viewState"><a href="#me" onclick="callLoad('divUserHomeR');callServer('myhome.php?switch=showmybillno&p='+Math.random(),'divUserHomeR')">历史购买记录</a></div>
           <div id="divClassUB" class="viewState"><a href="#shop" onclick="setUserShopFrame('');myshow('usershop');callLoad('loadingiframe');">我的购物车</a></div>
           <div id="divClassUB" class="viewState"><a href="./logout.php">退出登陆</a></div>
           <div id="chanb"> 
                <div id="divClassul" class="chanp">
                    <div>
                    </div>
                    <div class="cl"></div>
                </div>
           </div>
       </div>
        <div id="chanl" style="float:left;">
                <div id="divUserHomeR">
                
                </div>
                <div class="f"></div>
        </div>
      </div>
    </div> -->
    <div class="cl"></div>
<script language="javascript">
function checkdata() {
	var formmyhomevalue = '';
	var form_reg = document.formmyhome;

	if(form_reg.username.value == '') {
		alert('您忘了填姓名！');
		form_reg.username.focus();
		return false;
	}
	formmyhomevalue += '&username='+form_reg.username.value;

	if(form_reg.email.value == '') {
		alert('您忘了填email！');
		form_reg.email.focus();
		return false;
	} 
	if(form_reg.email.value != '') {
		var patrn=/^(.+?)@([a-z0-9\.]+)\.([a-z]){2,5}$/i; 
		if (!patrn.exec(form_reg.email.value)) {
			alert('填入的email不正确！');
			form_reg.email.focus();
			return false;
		}
	} 
	formmyhomevalue += '&email='+form_reg.email.value;

	if(form_reg.oldpassword.value == '') {
		alert('您忘了填密码，未安全起见，修改资料必须填入密码！');
		form_reg.oldpassword.focus();
		return false;
	}
	formmyhomevalue += '&oldpassword='+form_reg.oldpassword.value;
	if(form_reg.password.value != '') {
		if(form_reg.password.value != form_reg.repassword.value) {
			alert('两次填入的密码不相等！');
			form_reg.password.focus();
			return false;
		} 
		formmyhomevalue += '&password='+form_reg.password.value;
		formmyhomevalue += '&repassword='+form_reg.repassword.value;
	}
	
	if(form_reg.phone.value == '') {
		alert('您忘了填电话！');
		form_reg.phone.focus();
		return false;
	} 
	if(form_reg.phone.value != '') {
		var patrn=/^([0-9\-\.\s\(\)]+){5,22}$/i; 
		if (!patrn.exec(form_reg.phone.value)) {
			alert('填入的联系电话不正确！');
			form_reg.phone.focus();
			return false;
		}
	} 
	formmyhomevalue += '&phone='+form_reg.phone.value;
	
	if(form_reg.mobile.value == '') {
		alert('您忘了填移动电话！');
		form_reg.mobile.focus();
		return false;
	}
	if(form_reg.mobile.value != '') {
		var patrn=/^([0-9\-\.\s\(\)]+){5,22}$/i; 
		if (!patrn.exec(form_reg.mobile.value)) {
			alert('填入的移动电话不正确！');
			form_reg.mobile.focus();
			return false;
		}
	} 
	formmyhomevalue += '&mobile='+form_reg.mobile.value;

	if(form_reg.address.value == '') {
		alert('您忘了填邮寄地址！');
		form_reg.address.focus();
		return false;
	}
	formmyhomevalue += '&address='+form_reg.address.value;
	
	if(form_reg.postcode.value == '') {
		alert('您忘了填邮寄地址邮编！');
		form_reg.postcode.focus();
		return false;
	}
	formmyhomevalue += '&postcode='+form_reg.postcode.value+'&p='+Math.random();
	callLoad('divUserHomeR');
	postServer('myhome.php?switch=modify', formmyhomevalue, 'divUserHomeR');
}
</script>
<div id="page">
	<div class="sys-phpshop" id="header">
    	<div id="topnav">
            <ul class="guid" id="guid">
                <li class="selected" id="guid1"><a href="myhome.php"><span>用户中心</span></a></li>
                <li id="guid2"><a href="#sho" onclick="callLoad('divUserHomeR');callServer('myhome.php?switch=me&p='+Math.random(),'divUserHomeR');checmenu('guid',2,'selected')"><span>账号管理</span></a></li>
                <li id="guid3"><a href=""><span></span></a></li>
                <li id="guid4"><a href=""><span></span></a></li>
            </ul>
            <script language="javascript">
				function checmenu(id,v,classname){
					for (i = 1; i < 10; i++) {
						var n = id+i;
						if(document.getElementById(n)) {
							document.getElementById(n).className="";
						}
					}
					document.getElementById(id+v).className=classname;
				}
            </script>
    	</div>
    </div>
    <div id="content">
    	<div class="grid-c2" id="phpshop-panel">
        <div class="col-sub">
             	<div class="box-diamond" id="phpshop-menu">
                	<div class="phpshop-bd">
                    	<div class="menu-box" id="100">
                        <h3 class="phpshop-bar"><span> 我的信息 </span>
                        <button class="menu-close">-</button>
                        </h3>
                        <ul class="group">
                                <li data-mid="101"><span> <a target="_blank" href="myhome.php">我的订单</a></span></li>
                                <!-- <li data-mid="103"><span> <a href="">我的购物车</a></span></li>	-->	
                                <li data-mid="104"><span> <a href="hotproduct.php">产品推荐</a></span></li>		
                                <!--<li data-mid="105"><span> <a href="">仓库中的宝贝</a></span></li>		
                                <li data-mid="106"><span> <a href="">提问客服/回复</a></span></li>-->
                                <li data-mid="107"><span> <a href="./logout.php">注销</a></span></li>
                        </ul>
                        </div>
                    </div>
                </div>
             </div>
        	<div class="col-main">
            	<div class="main-wrap">
                	<div id="main-content">
                        <div class="item-list" id="divUserHomeR">
                           <div class="item-list-hd">
                               <ul class="item-list-tabs item-list-tabs-flexible clearfix">
                                    <li id="order1" class="current"><a hidefocus="true" onclick="callLoad('item-list-bd-order');callServer('myhome.php?switch=showmybillno&act=month&p='+Math.random(),'item-list-bd-order');checmenu('order',1,'current')" href="javascript:void(0)">近一个月订单</a></li>
                                    <li id="order2"><a hidefocus="true" onclick="callLoad('item-list-bd-order');callServer('myhome.php?switch=showmybillno&act=nopay&p='+Math.random(),'item-list-bd-order');checmenu('order',2,'current')" href="javascript:void(0)">未付款订单</a></li>
                                    <li id="order3"><a hidefocus="true" onclick="callLoad('item-list-bd-order');callServer('myhome.php?switch=showmybillno&act=wait&p='+Math.random(),'item-list-bd-order');checmenu('order',3,'current')" href="javascript:void(0)">等待发货</a></li>
                                    <li id="order4"><a hidefocus="true" onclick="callLoad('item-list-bd-order');callServer('myhome.php?switch=showmybillno&act=issend&p='+Math.random(),'item-list-bd-order');checmenu('order',4,'current')" href="javascript:void(0)">已发货</a></li>
                                    <!--<li id="order5"><a hidefocus="true" onclick="callLoad('item-list-bd-order');callServer('myhome.php?switch=showmybillno&act=month&p='+Math.random(),'item-list-bd-order');checmenu('order',1,'current')" href="javascript:void(0)">退款中</a></li>
                                    <li id="order6"><a hidefocus="true" onclick="callLoad('item-list-bd-order');callServer('myhome.php?switch=showmybillno&act=month&p='+Math.random(),'item-list-bd-order');checmenu('order',1,'current')" href="javascript:void(0)">需要评价</a></li>-->
                                    <li id="order7"><a hidefocus="true" onclick="callLoad('item-list-bd-order');callServer('myhome.php?switch=showmybillno&act=success&p='+Math.random(),'item-list-bd-order');checmenu('order',7,'current')" href="javascript:void(0)">成功的订单</a></li>
                                    <li id="order8"><a hidefocus="true" onclick="callLoad('item-list-bd-order');callServer('myhome.php?switch=showmybillno&act=history&p='+Math.random(),'item-list-bd-order');checmenu('order',8,'current')" href="javascript:void(0)">历史订单</a></li>
                                </ul>	
                            </div>
                            <div class="item-list-bd" id="item-list-bd-order">
                                {inc file=#MeBillInfo.tpl} 
                            </div>   
                        </div>
                    </div>
                 </div>
             </div>
             
		</div>
	</div>
</div>
{inc file=inc/#body_bottom.tpl}
</div>
</body>
</html>
