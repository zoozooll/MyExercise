<html>
<head>
<TITLE><?php echo $this->__muant["__Meta"]["Title"] ?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META NAME="description" CONTENT="<?php echo $this->__muant["__Meta"]["Description"] ?>">
<META NAME="keywords" CONTENT="<?php echo $this->__muant["__Meta"]["Keywords"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<link href="../../css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
<script language="javascript">
function MP_jumpMenu(val) {
	location.href = "admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid="+val;
}
</script>
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
			标题: <input name="keyword" type="text"> <input name="keywordsubmit" type="submit" value="搜索">
		</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
<table width="90%" border="1">
  <tr>
    <td>新闻id</td>
    <td>标题</td>
    <td>添加日期</td>
    <td>作者</td>
    <td>来源</td>
    <td>修改</td>
    <td>删除</td>
  </tr>
<?php $newsinum = count($this->__muant["news"]); for($newsi = 0; $newsi<$newsinum; $newsi++) { ?>
  <tr bordercolor="#CCCCCC">
    <td><input name="id[]" type="checkbox" value="<?php echo $this->__muant["news"]["$newsi"]["id"] ?>"><?php echo $this->__muant["news"]["$newsi"]["id"] ?></td>
    <td><a href="news.php?id=<?php echo $this->__muant["news"]["$newsi"]["id"] ?>" target="_blank"><?php echo $this->__muant["news"]["$newsi"]["title"] ?></a></td>
    <td><?php echo $this->__muant["news"]["$newsi"]["add_date"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["news"]["$newsi"]["author"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["news"]["$newsi"]["source_from"] ?>&nbsp;</td>
    <td><a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&nid=<?php echo $this->__muant["news"]["$newsi"]["id"] ?>&modify=yes" target="_self">修改</a>&nbsp;</td>
    <td><a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&delnid=<?php echo $this->__muant["news"]["$newsi"]["id"] ?>" target="_self">删除</a></td>
  </tr>
<?php } ?>
</table>
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
		  <input name="ok" type="submit" value="  o  k  " class="txInput">
		  <input name="reset" type="reset" value="reset" class="txInput">
		  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
		  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
</div>
</form>
</body></html>