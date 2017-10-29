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
<script language="JavaScript" src="./jscript/comm/divwindows.js"></script>
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

	<ul id="ClassesMenu" onMouseOut="setSelectElement(document)" onMouseOver="setSelectElement(document, false)">
		<li id="menutitle"><a href="javascript:void(0)">选择 → 商品类别列表</a>
			<ul id="title" class="scroll">
			<?php $classesinum = count($this->__muant["classes"]); for($classesi = 0; $classesi<$classesinum; $classesi++) { ?>
				<li class="<?php if($this->__muant["classes"]["$classesi"]["cpid"]=='0') { ?>fb <?php } ?>l">
				<span class="fLeft"><?php echo $this->__muant["classes"]["$classesi"]["_name"] ?></span> <?php if($this->__muant["classes"]["$classesi"]["ccidnum"]==0) { ?><a title="类别商品" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["classes"]["$classesi"]["id"] ?>&act=add" class="fRig" target="adminMain"> 查看商品</a><?php } ?>
				</li>
			<?php } ?>
			</ul>
		</li>
	</ul>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb">类别属于:</div>
		<div id="hiddenselect">
			<select name="cid"  class="txInput" id="cid">
			  <option value="">....请选择</option>
			  <?php $classesinum = count($this->__muant["classes"]); for($classesi = 0; $classesi<$classesinum; $classesi++) { ?>
			  <option value="<?php echo $this->__muant["classes"]["$classesi"]["id"] ?>"<?php if($this->__muant["classes"]["$classesi"]["id"]==$this->__muant["cid"]) { ?> selected<?php } ?>>
			  <?php echo $this->__muant["classes"]["$classesi"]["_name"] ?></option>
			  <?php } ?>
			</select>
			
			&nbsp;按商品名称搜索: <input name="keyword" type="text" value="<?php echo $this->__muant["keyword"] ?>"> <input name="keywordsubmit" type="submit" value="搜索"> (共<?php echo $this->__muant["productnum"] ?>个商品)
		</div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div class="fLeft" style="width:80px;">商品id</div>
		<div class="fLeft" style="width:250px;">品名</div>
		<div><ul id="productlist"><li style="width:100px;">编号</li><li style="width:100px;">厂编</li><li>进价</li><li>售价</li><li>特价</li><li>市场价</li><li>库存</li><li>出货量</li><li>货源</li><li>修改</li><li>删除</li></ul></div>
		<div class="cls"></div>
	</div>
	<?php $productsinum = count($this->__muant["products"]); for($productsi = 0; $productsi<$productsinum; $productsi++) { ?>
	<div id="divMainBodyContent">
		<div class="fLeft" style="width:50px;">
		<input name="id[]" type="checkbox" value="<?php echo $this->__muant["products"]["$productsi"]["id"] ?>"><?php echo $this->__muant["products"]["$productsi"]["id"] ?>
		</div>
		<div class="fLeft" style="width:280px;"><a href="product.php?pid=<?php echo $this->__muant["products"]["$productsi"]["id"] ?>" target="_blank"><?php echo $this->__muant["products"]["$productsi"]["name"] ?></a></div>
		<div>
		<ul id="productlist">
		<li style="width:100px;"><?php echo $this->__muant["products"]["$productsi"]["number"] ?></li>
		<li style="width:100px;"><?php echo $this->__muant["products"]["$productsi"]["factory_number"] ?></li>
		<li><?php echo $this->__muant["products"]["$productsi"]["price_mill"] ?></li>
		<li><?php echo $this->__muant["products"]["$productsi"]["price"] ?></li>
		<li><?php echo $this->__muant["products"]["$productsi"]["price_special"] ?></li>
		<li><?php echo $this->__muant["products"]["$productsi"]["price_market"] ?></li>
		<li><?php echo $this->__muant["products"]["$productsi"]["store"] ?></li>
		<li></li>
		<li><?php echo $this->__muant["products"]["$productsi"]["psid"] ?></li>
	    <li><a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["products"]["$productsi"]["cid"] ?>&pid=<?php echo $this->__muant["products"]["$productsi"]["id"] ?>" target="_self">修改</a></li>
		<li><a onClick="return delYesOrNo()" title="删除" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["products"]["$productsi"]["cid"] ?>&delpid=<?php echo $this->__muant["products"]["$productsi"]["id"] ?>" target="_self">删除</a></li>
	   </ul>
		</div>
		<div class="cls"></div>
	</div>
	<?php } ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"></div>
		<div class="cls"></div>
	</div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"></div>
		<div>
			    共<?php echo $this->__muant["allpage"] ?>页
				<span id="up">
				<a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["cid"] ?>&pn=1" target="_self">首页</a>
				</span>
				<?php if($this->__muant["allpage"]>1) { ?>
				<?php $pageinum = count($this->__muant["page"]); for($pagei = 0; $pagei<$pageinum; $pagei++) { ?>
				<span<?php if($this->__muant["page"]["$pagei"]==$this->__muant["pn"]) { ?> class="f14c fb"<?php } ?>> 
				<?php if($this->__muant["page"]["$pagei"]!='') { ?>
				<a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["cid"] ?>&pn=<?php echo $this->__muant["page"]["$pagei"] ?>" target="_self"><?php echo $this->__muant["page"]["$pagei"] ?></a>
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
<script laguage="javascript">classesFix();</script>
</body></html>