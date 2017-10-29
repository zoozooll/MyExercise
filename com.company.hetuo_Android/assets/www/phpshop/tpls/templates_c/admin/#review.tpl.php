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
<script language="JavaScript" src="./jscript/comm/simpleajax.js"></script>
</head>
<body>
<form method="post" action="admincp.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<div id="divMainBodyContent">
<table width="90%" >
  <tr>
    <td>id</td>
    <td width="50">看产品</td>
    <td width="100">标题</td>
    <td>优点</td>
    <td>不足</td>
    <td>结论</td>
    <td width="60">ip</td>
     <td width="60">客户订单号</td>
    <td width="50">审核</td>
    <td width="30">删除</td>
  </tr>
<?php $valueinum = count($this->__muant["value"]); for($valuei = 0; $valuei<$valueinum; $valuei++) { ?>
  <tr bordercolor="#CCCCCC">
    <td><input name="id[]" type='hidden' value="<?php echo $this->__muant["value"]["$valuei"]["id"] ?>"><?php echo $this->__muant["value"]["$valuei"]["id"] ?></td>
    <td><a href="../product.php?pid=<?php echo $this->__muant["value"]["$valuei"]["pid"] ?>" target="_blank">看产品</a></td>
    <td><?php echo $this->__muant["value"]["$valuei"]["title"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["value"]["$valuei"]["good"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["value"]["$valuei"]["bad"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["value"]["$valuei"]["contents"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["value"]["$valuei"]["ip"] ?>&nbsp;</td>
    <td><?php echo $this->__muant["value"]["$valuei"]["billno"] ?>&nbsp;</td>
    <td><input name="c<?php echo $this->__muant["value"]["$valuei"]["id"] ?>" onClick="var v = '&v=0';if(this.checked == true) {v = '&v=1';};postServer('admin/adminajaxset.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&switch=fun&id=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>',v,'r<?php echo $this->__muant["value"]["$valuei"]["id"] ?>')" type="checkbox" <?php if($this->__muant["value"]["$valuei"]["init"]==1) { ?>checked<?php } else { ?><?php } ?>><span id="r<?php echo $this->__muant["value"]["$valuei"]["id"] ?>"><?php if($this->__muant["value"]["$valuei"]["init"]==1) { ?>已审<?php } else { ?>未审<?php } ?></span></td>
    <td><a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&delrid=<?php echo $this->__muant["value"]["$valuei"]["id"] ?>" target="_self">删除</a></td>
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
		  <input name="ok" type="submit" value="<?php if($this->__muant["act"]=='edit') { ?>修　改<?php } else { ?>  o  k  <?php } ?>" class="txInput">
		  <input name="reset" type="reset" value="reset" class="txInput">
		  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
		  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
          <input type="hidden" name="uid" value="<?php echo $this->__muant["uid"] ?>">
          <?php if($this->__muant["act"]=='edit') { ?><input type="hidden" name="act" value="edituser"><?php } ?>
</div>
</form>
</body></html>