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
</head>
<body>
<form method="post" action="admincp.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"></div>
		<div id="hiddenselect">
			会员id: <input name="uid" type="text" size="4"> 
			会员名: <input name="keyword" type="text"> <input name="keywordsubmit" type="submit" value="搜索">
		</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
<?php if($this->__muant["act"]=='edit') { ?><?php $userinum = count($this->__muant["user"]); for($useri = 0; $useri<$userinum; $useri++) { ?>
  <table width="90%" border="1">
    <tr>
      <td width="9%" scope="row">姓名</thd>
      <td width="91%"><input name="name" value="<?php echo $this->__muant["user"]["$useri"]["name"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">Email</td>
      <td><input name="email" value="<?php echo $this->__muant["user"]["$useri"]["email"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">联系电话</td>
      <td><input name="phone" value="<?php echo $this->__muant["user"]["$useri"]["phone"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">移动电话</td>
      <td><input name="mobile" value="<?php echo $this->__muant["user"]["$useri"]["mobile"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">邮寄地址</td>
      <td><input name="address" value="<?php echo $this->__muant["user"]["$useri"]["address"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">邮编</td>
      <td><input name="postcode" value="<?php echo $this->__muant["user"]["$useri"]["postcode"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">性别</td>
      <td><input name="sex" value="<?php echo $this->__muant["user"]["$useri"]["sex"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">生日</td>
      <td><input name="birthday" value="<?php echo $this->__muant["user"]["$useri"]["birthday"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">QQ</td>
      <td><input name="qq" value="<?php echo $this->__muant["user"]["$useri"]["qq"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">MSN</td>
      <td><input name="msn" value="<?php echo $this->__muant["user"]["$useri"]["msn"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">taobao旺旺</td>
      <td><input name="taobao" value="<?php echo $this->__muant["user"]["$useri"]["taobao"] ?>" type="text"></td>
    </tr>
    <tr>
      <td scope="row">Skype</td>
      <td><input name="skype" value="<?php echo $this->__muant["user"]["$useri"]["skype"] ?>" type="text"></td>
    </tr>
  </table><?php } ?>
<?php } else { ?>
<table width="90%" >
  <tr>
    <td>id</td>
    <td>姓名</td>
    <td>Email</td>
    <td>联系电话</td>
    <td>移动电话</td>
    <td>邮寄地址</td>
    <td>邮编</td>
    <td>性别</td>
    <td>生日</td>
    <td>QQ</td>
    <td>MSN</td>
    <td>taobao旺旺</td>
    <td>Skype</td>
    <td>修改</td>
    <td>删除</td>
  </tr>
<?php $userinum = count($this->__muant["user"]); for($useri = 0; $useri<$userinum; $useri++) { ?>
  <tr bordercolor="#CCCCCC">
    <td><input name="id[]" type='hidden' value="<?php echo $this->__muant["user"]["$useri"]["id"] ?>"><?php echo $this->__muant["user"]["$useri"]["id"] ?></td>
    <td><?php echo $this->__muant["user"]["$useri"]["name"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["email"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["phone"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["mobile"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["address"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["postcode"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["sex"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["birthday"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["qq"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["msn"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["taobao"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["user"]["$useri"]["skype"] ?>&nbsp;</td>
    <td><a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&uid=<?php echo $this->__muant["user"]["$useri"]["id"] ?>&act=edit" target="_self">修改</a></td>
    <td><a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&deluid=<?php echo $this->__muant["user"]["$useri"]["id"] ?>" target="_self">删除</a></td>
  </tr>
<?php } ?>
</table>
<?php } ?>
	  <div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
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
</div>
<div id="divMainBodyContentSumbit">
		  <input name="ok" type="submit" value="<?php if($this->__muant["act"]=='edit') { ?>修　改<?php } else { ?>  o  k  <?php } ?>" class="txInput">
		  <input name="reset" type="reset" value="reset" class="txInput">
		  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
		  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
          <input type="hidden" name="uid" value="<?php echo $this->__muant["uid"] ?>">
          <?php if($this->__muant["act"]=='edit') { ?><input type="hidden" name="act" value="edituser"><?php } ?>
</div>
</form>
</body></html>