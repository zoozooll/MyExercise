<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE><?php echo $this->__muant["__Meta"]["Title"] ?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META NAME="description" CONTENT="<?php echo $this->__muant["__Meta"]["Description"] ?>">
<META NAME="keywords" CONTENT="<?php echo $this->__muant["__Meta"]["Keywords"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
<link href="css/admin/css.css" rel="stylesheet" type="text/css" />
<link href="../../css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="jscript/admin/js.js"></script>
<script language="JavaScript" src="jscript/comm/divwindows.js"></script>

</head>
<body>
<div id="dvTop">
 <div id="divTopMsg">
     <a href="http://www.phpshop.cn/" target="_blank" id="aLogo">
	 <img class="logo" src="images/default/admin/logo.gif" alt="logo" border="0"></a>
	<!--用户信息-->
	<div id="divUsrMsg" class="fLeft">
	  您好，<b><?php echo $this->__muant["admin_name"] ?></b> [ <a href="admin/checklogin.php?logout=yes" class="toplink" target="_top">退出</a>，
	  <a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=39" target="adminMain">我的帐户</a>，
	  <a onclick="checmenu(1)" href="admincp.php?mid=&lid=" target="adminMain">欢迎页</a>，<a href="http://<?php echo $this->__muant["web_url"] ?>" target="_blank">网站首页</a> ]
	</div>
	<!--右上小连接-->
	<div id="divToplink" class="fRig">
	   <a href="#"></a>
	   <span id="spnNewFunc">
	   <a href="#"  target="_blank"></a>
	   <a href="#"></a>
	   <a href="#" target="_blank"></a>
	   </span>	
	</div>
  </div>
  <div id="divTopNav">
    <!--主导航按钮onClick="checmenu('<?php echo $this->__muant["admin_menu"]["$admin_menui"]["id"] ?>');return false;" -->
	<ul id="ulTopNav">
	<?php $admin_menuinum = count($this->__muant["admin_menu"]); for($admin_menui = 0; $admin_menui<$admin_menuinum; $admin_menui++) { ?>
		<?php if($this->__muant["admin_menu"]["$admin_menui"]["orderid"]==0) { ?>
		<li id="admin_menu">
		<a href="javascript:void(0)" id="amenu<?php echo $this->__muant["admin_menu"]["$admin_menui"]["id"] ?>" class="<?php if($this->__muant["admin_menu"]["$admin_menui"]["id"]==$this->__muant["mid"]) { ?>navOn f14e<?php } elseif($this->__muant["admin_menu"]["$admin_menui"]["id"]=='1') { ?>navOn f14e<?php } elseif($this->__muant["admin_menu"]["$admin_menui"+1]["id"]>'0') { ?>navNml f14c<?php } else { ?>last f14c<?php } ?>" ><?php echo $this->__muant["admin_menu"]["$admin_menui"]["name"] ?></a>
			<ul><?php } ?><?php if($this->__muant["admin_menu"]["$admin_menui"]["orderid"]>0) { ?>
				<li> <a href="javascript:void(0)" onClick="show('','<?php echo $this->__muant["admin_menu"]["$admin_menui"]["fsid"] ?>','<?php echo $this->__muant["admin_menu"]["$admin_menui"]["id"] ?>'); return false" id="aMenuInbox"><?php echo $this->__muant["admin_menu"]["$admin_menui"]["name"] ?></a></li><?php } ?><?php if($this->__muant["admin_menu"]["$admin_menui"+1]["orderid"]==0) { ?>
			</ul>
		</li>
		<?php } ?>
	<?php } ?>
    </ul>
    <div id="divSearch">
	  <div class="fLeft"></div>
	  <div class="bgcolor9 f12w" id="tbMsg">
	  	<span id="msg"></span>	  
	  </div>
    </div>
  </div>
</div>
<!--页面顶部结束-->

<script language="javascript">
function checmenu(v){
	for (i = 1; i < 40; i++) {
		var n = 'amenu'+i;
		if(document.getElementById(n) != null) {
			document.getElementById(n).className="navNml f14c";
		}
	}
	document.getElementById('amenu'+v).className='navOn f14e';
}
function show(name,mid,id) {
	document.getElementById("adminMain").src = "admincp.php?mid="+mid+"&lid="+id;
	//SetWinHeight(document.getElementById("adminMain"));
	checmenu(mid);
}

window.status = "Welcome to <?php echo $this->__muant["site"] ?>";
window.onload=menuFix;
</script>
</body>
</html>