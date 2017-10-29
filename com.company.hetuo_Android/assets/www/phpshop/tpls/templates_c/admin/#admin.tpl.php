<html>
<head>
<TITLE><?php echo $this->__muant["__Meta"]["Title"] ?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META NAME="description" CONTENT="<?php echo $this->__muant["__Meta"]["Description"] ?>">
<META NAME="keywords" CONTENT="<?php echo $this->__muant["__Meta"]["Keywords"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
<script language="javascript">
function addAdmin() {
	document.getElementById('add_admin').style.display = 'block';
	document.getElementById('show_admin').style.display = 'none';
	document.form1.addadmin.checked = true;
}
function setAddAdmin(obj) {
	if(obj.checked == true) {
		document.getElementById('add_admin').style.display = 'block';
		document.getElementById('show_admin').style.display = 'none';
	} else {
		document.getElementById('add_admin').style.display = 'none';
		document.getElementById('show_admin').style.display = 'block';
	}
}
</script>
</head>
<body>
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"></div>
		<div id="hiddenselect">
			<?php if($this->__muant["aid"]=='') { ?><a onClick="addAdmin();return false;" href="admin#add">增加新管理员</a><input name="addadmin" type="checkbox" onClick="setAddAdmin(this)" value="1" class="txInput" ><?php } ?> 用户名: <input name="keyword" type="text"> <input name="keywordsubmit" type="submit" onClick="document.form1.action='admincp.php'" value="搜索" class="txInput" >
		</div>
		<div class="cls"></div>
	</div>
<div id="show_admin">
<?php if($this->__muant["aid"]=='') { ?>
	<div id="divMainBodyContent">
<table width="90%" border="1">
  <tr>
    <td>id</td>
    <td>用户名</td>
    <td>email</td>
    <td>添加时间</td>
    <td>登录IP</td>
    <td>登录时间</td>
    <td>修改</td>
    <td>删除</td>
  </tr>
<?php $admininum = count($this->__muant["admin"]); for($admini = 0; $admini<$admininum; $admini++) { ?>
  <tr bordercolor="#CCCCCC">
    <td><input name="id[]" type="checkbox" value="<?php echo $this->__muant["admin"]["$admini"]["id"] ?>" class="txInput" ></td>
    <td><?php echo $this->__muant["admin"]["$admini"]["name"] ?></td>
    <td><?php echo $this->__muant["admin"]["$admini"]["email"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["admin"]["$admini"]["add_date"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["admin"]["$admini"]["lastloginip"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["admin"]["$admini"]["lastlogintime"] ?>&nbsp;</td>
    <td><?php if($this->__muant["admin"]["$admini"]["id"]!=1) { ?><?php if($this->__muant["admin"]["$admini"]["id"]!=$this->__muant["loginaid"]) { ?><a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&nid=<?php echo $this->__muant["admin"]["$admini"]["id"] ?>&modify=yes&aid=<?php echo $this->__muant["admin"]["$admini"]["id"] ?>" target="_self">修改</a><?php } ?><?php } ?>&nbsp;</td>
    <td><?php if($this->__muant["admin"]["$admini"]["id"]!=1) { ?><?php if($this->__muant["admin"]["$admini"]["id"]!=$this->__muant["loginaid"]) { ?><a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&delaid=<?php echo $this->__muant["admin"]["$admini"]["id"] ?>" target="_self">删除</a><?php } ?><?php } ?>&nbsp;</td>
  </tr>
<?php } ?>
</table>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"></div>
		<div>
			    共<?php echo $this->__muant["allpage"] ?>页
				<span id="up">
				<a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&pn=1" target="_self">首页</a>
				</span>
				<?php if($this->__muant["allpage"]>1) { ?>
				<?php $pageinum = count($this->__muant["page"]); for($pagei = 0; $pagei<$pageinum; $pagei++) { ?>
				<span<?php if($this->__muant["page"]["$pagei"]==$this->__muant["pn"]) { ?> class="f14c fb"<?php } ?>> 
				<?php if($this->__muant["page"]["$pagei"]!='') { ?>
				<a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&pn=<?php echo $this->__muant["page"]["$pagei"] ?>" target="_self"><?php echo $this->__muant["page"]["$pagei"] ?></a>
				<?php } else { ?>... <?php } ?>
				</span>
				<?php } ?>
				<?php } ?>
		</div>
		<div class="cls"></div>
	</div>
<?php } ?>
</div>
<div id="add_admin"<?php if($this->__muant["aid"]=='') { ?> style="display:none"<?php } ?>>
	<?php if($this->__muant["aid"]!='') { ?><div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 修改用户:<b><?php echo $this->__muant["admin"]["name"] ?></b> <input name="cache" value="取消修改" type="button" onClick="location.href='admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>'" class="txInput" ></div>
		<div class="cls"></div><input name="addadmin" type="hidden" value="1">
	</div><?php } ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 用户名:</div>
		<div><input name="name" type="text" class="txInput" id="name" value="<?php echo $this->__muant["admin"]["name"] ?>"<?php if($this->__muant["aid"]!='') { ?> readonly<?php } ?> >
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">Email:</div>
	    <div><input name="email" type="text" class="txInput" id="email" value="<?php echo $this->__muant["admin"]["email"] ?>"<?php if($this->__muant["aid"]!='') { ?> readonly<?php } ?> ></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">密码:</div>
		<div><input name="password" type="password" class="txInput" id="password" value="" > 
		</div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">再输入一次密码:</div>
		<div><input name="pass" type="password" class="txInput" id="pass" value="" ></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">管理员权限设置:</div>
		<div class="fLeft"><?php $adminsetinfoinum = count($this->__muant["adminsetinfo"]); for($adminsetinfoi = 0; $adminsetinfoi<$adminsetinfoinum; $adminsetinfoi++) { ?><?php if($this->__muant["adminsetinfo"]["$adminsetinfoi"]["orderid"]==0) { ?><br /><b><?php } ?><?php echo $this->__muant["adminsetinfo"]["$adminsetinfoi"]["name"] ?><?php if($this->__muant["adminsetinfo"]["$adminsetinfoi"]["orderid"]=='0') { ?></b><?php } ?><input name="power[]" type="checkbox" value="<?php echo $this->__muant["adminsetinfo"]["$adminsetinfoi"]["id"] ?>"<?php if($this->__muant["adminsetinfo"]["$adminsetinfoi"]["id"]==$this->__muant["adminsetinfo"]["$adminsetinfoi"]["fid"]) { ?> checked<?php } ?>><?php } ?><br /><br /></div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContentSumbit">
			  <input type="hidden" name="aid" value="<?php echo $this->__muant["aid"] ?>">
			  <input name="ok" type="submit" value="  o  k  " class="txInput">
			  <input name="reset" type="reset" value="reset" class="txInput">
			  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
			  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
	</div>
</div>
</div>
</form>
</body></html>