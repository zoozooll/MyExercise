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
<script language="JavaScript" src="./jscript/comm/divwindows.js"></script>
<script type="text/javascript" src="./jscript/editor/tiny_mce.js"></script>
</head>
<body>
<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1" onSubmit="return checkData();">
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div>
</div>
<div id="BodyContent">
	<?php if($this->__muant["pid"]>1) { ?><div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb">修改商品</div>
		<div class="cls"></div>
	</div><?php } ?> 
	<ul id="ClassesMenu" onMouseOut="setSelectElement(document)" onMouseOver="setSelectElement(document, false)">
		<li id="menutitle"><a href="javascript:void(0)">选择 → 商品类别列表</a>
			<ul id="title" class="scroll">
			<?php $classesinum = count($this->__muant["classes"]); for($classesi = 0; $classesi<$classesinum; $classesi++) { ?>
				<li class="<?php if($this->__muant["classes"]["$classesi"]["cpid"]=='0') { ?>fb <?php } ?>l">
				<span class="fLeft"><?php echo $this->__muant["classes"]["$classesi"]["_name"] ?></span> <?php if($this->__muant["classes"]["$classesi"]["ccidnum"]==0) { ?><a title="类别商品" href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["classes"]["$classesi"]["id"] ?>&act=add" class="fRig" target="adminMain"> <?php if($this->__muant["pid"]>0) { ?>查看<?php } else { ?>添加<?php } ?>商品</a><?php } ?>
				</li>
			<?php } ?>
			</ul>
		</li>
	</ul>
	<div class="cls"></div>
<?php if($this->__muant["successproduct"]!=NULL) { ?><div class="divMsg">商品添加成功 <a href="<?php echo $this->__muant["successproduct"]["url"] ?>" target="_blank">查看 <?php echo $this->__muant["successproduct"]["name"] ?></a> <a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["cid"] ?>&pid=<?php echo $this->__muant["successproduct"]["id"] ?>" target="adminMain">修改</a> <a href="adminset.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid=<?php echo $this->__muant["cid"] ?>&d_pid=<?php echo $this->__muant["successproduct"]["id"] ?>" target="adminMain">删除</a></div><?php } ?>
<?php if($this->__muant["cid"]<1) { ?>
	<!-- div class="divMsg">请在 商品类别列表 选择要添加的商品类别</div -->
<?php } else { ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">类别:</div>
	    <div id="hiddenselect" class="fLeft">
		<select class="txInput"<?php if($this->__muant["pid"]==$this->__muant["null"]) { ?> disabled<?php } ?> name="cid" id="cid">
		  <option value="">....请选择</option>
		  <?php $classesinum = count($this->__muant["classes"]); for($classesi = 0; $classesi<$classesinum; $classesi++) { ?>
		  <option value="<?php echo $this->__muant["classes"]["$classesi"]["id"] ?>"<?php if($this->__muant["classes"]["$classesi"]["id"]==$this->__muant["cid"]) { ?> selected<?php } ?>>
		  <?php echo $this->__muant["classes"]["$classesi"]["_name"] ?></option>
		  <?php } ?>
		</select>
		</div>
		<div class="cls"></div>
    </div>	
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle">产品名称:</div>
	    <div class="fLeft">
			<input class="txInput" name="product_name" type="text" value="<?php echo $this->__muant["product"]["name"] ?>"> 
		   库存:<input class="txInput" name="product_store" type="text" size="5" maxlength="5" value="<?php echo $this->__muant["product"]["store"] ?>">
		   上架:<input class="txInput" name="online" type="checkbox" value="1" <?php if($this->__muant["pid"]>0) { ?><?php if($this->__muant["product"]["online"]==1) { ?>checked<?php } ?><?php } else { ?>checked<?php } ?>>
		   促销产品:<input class="txInput" name="special_offer" type="checkbox" value="1" <?php if($this->__muant["pid"]>0) { ?><?php if($this->__muant["product"]["special_offer"]==1) { ?>checked<?php } ?><?php } else { ?><?php } ?>>
		   显示市场价:<input class="txInput" name="market_offer" type="checkbox" value="1" <?php if($this->__muant["pid"]>0) { ?><?php if($this->__muant["product"]["market_offer"]==1) { ?>checked<?php } ?><?php } else { ?><?php } ?>>
		   显示会员价:<input class="txInput" name="member_offer" type="checkbox" value="1" <?php if($this->__muant["pid"]>0) { ?><?php if($this->__muant["product"]["member_offer"]==1) { ?>checked<?php } ?><?php } else { ?>checked<?php } ?>>
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 售价:</div>
	    <div class="fLeft"><input name="product_price" type="text" class="txInput" value="<?php echo $this->__muant["product"]["price"] ?>" size="9"> 
				   进价:<input name="product_mill_price" type="text" class="txInput" value="<?php echo $this->__muant["product"]["price_mill"] ?>" size="9"> 
				   促销特价:<input name="product_special_price" type="text" class="txInput" value="<?php echo $this->__muant["product"]["price_special"] ?>" size="9"> 
				   市场价:<input name="price_market" type="text" class="txInput" value="<?php echo $this->__muant["product"]["price_market"] ?>" size="9"> 
				   会员价:<input name="product_member_price" type="text" class="txInput" value="<?php echo $this->__muant["product"]["price_member"] ?>" size="9"> 
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 编号:</div>
	    <div class="fLeft" id="hiddenselect"><input class="txInput" name="product_no" type="text" value="<?php echo $this->__muant["product"]["number"] ?>"> 
				   厂编:<input class="txInput" name="product_factory_number" type="text" value="<?php echo $this->__muant["product"]["factory_number"] ?>">
				   货源商家:<select name="psid" id="psid">
				   <option value="">....请选择</option>
				   <?php $supplyinum = count($this->__muant["supply"]); for($supplyi = 0; $supplyi<$supplyinum; $supplyi++) { ?>
				   <option value="<?php echo $this->__muant["supply"]["$supplyi"]["id"] ?>"<?php if($this->__muant["supply"]["$supplyi"]["id"]==$this->__muant["product"]["psid"]) { ?> selected<?php } ?>><?php echo $this->__muant["supply"]["$supplyi"]["name"] ?></option>
				   <?php } ?>
				   </select>
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 品牌:</div>
	    <div class="fLeft" id="hiddenselect"><select class="txInput" name="product_brand" id="product_brand" onChange="myselect(this.value);">
				     <option value="">请选择品牌..</option>
				   </select>
				   系列:
				    <select class="txInput" name="product_series" id="product_series">
				     <option value="">请选择系列..</option>
				   </select>
				   型号:<input class="txInput" name="product_model" type="text" value="<?php echo $this->__muant["product"]["model"] ?>">
				   <script language=javascript>
						var childmenu = new Array(
						<?php $brandinum = count($this->__muant["brand"]); for($brandi = 0; $brandi<$brandinum; $brandi++) { ?>
							[0,'<?php echo $this->__muant["brand"]["$brandi"]["id"] ?>','<?php echo $this->__muant["brand"]["$brandi"]["name"] ?>'],
					    <?php } ?>
						<?php $seriesinum = count($this->__muant["series"]); for($seriesi = 0; $seriesi<$seriesinum; $seriesi++) { ?>
							['<?php echo $this->__muant["series"]["$seriesi"]["bid"] ?>','<?php echo $this->__muant["series"]["$seriesi"]["id"] ?>','<?php echo $this->__muant["series"]["$seriesi"]["name"] ?>'],
					    <?php } ?>
						[,,]
						);
						for(i=0;i<childmenu.length;i++) {
						 if (childmenu[i][0] == 0) {
						   slct=document.createElement("Option");
						   slct.value=childmenu[i][1];
						   if(slct.value == '<?php echo $this->__muant["product"]["bid"] ?>') {
							   slct.selected = true;
						   }
						   slct.innerText=childmenu[i][2];
						   document.getElementById("product_brand").appendChild(slct);
						 }
						}
						function myselect(course) { 
						  document.getElementById("product_series").length = 1;
						  for (i=0;i<childmenu.length;i++){
							  if (course == childmenu[i][0]){
								  slct=document.createElement("Option");
								  slct.value=childmenu[i][1];
								  if(slct.value== '<?php echo $this->__muant["product"]["sid"] ?>') {
									  slct.selected = true;
								  }
								  slct.innerText=childmenu[i][2];
								  document.getElementById("product_series").appendChild(slct);
							  }
						  }
					    }
						myselect(document.getElementById('product_brand').value);
					</script>   
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 相关商品:</div>
	    <div class="fLeft"><input class="txInput" name="product_related" type="text" value="<?php if($this->__muant["pid"]>0) { ?><?php echo $this->__muant["product"]["product_related"] ?><?php } else { ?>0<?php } ?>">
	    (填 0 为自动相关;手动为填 产品id号相关 以 , 号隔开: 1,2,3,4；不要相关商品请清空input框内容；默认为自动相关商品) 
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 描述:</div>
	    <div class="fLeft" style="height:300px;"><textarea id="product_des" name="product_des"  rows="15" cols="35" style="width:98%"><?php echo $this->__muant["product"]["describes"] ?></textarea>
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 小图:</div>
	    <div class="fLeft"><input class="txInput" name="product_s" type="text" value="<?php echo $this->__muant["product"]["s_pic"] ?>">
					<!--<input class="txInput" name="nopic" type="checkbox" value="1"> 不上传图片 以产品编号做图片编号(ex:5678.jpg)		-->
		</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 中图:</div>
	    <div class="fLeft">  <input class="txInput" name="product_m" type="text" value="<?php echo $this->__muant["product"]["m_pic"] ?>"></div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"> 大图:</div>
	    <div class="fLeft"><input class="txInput" name="product_b" type="text" value="<?php echo $this->__muant["product"]["b_pic"] ?>"></div>
		<div class="fLeft">&nbsp;<iframe src="./admin/adminajaxset.php?switch=uploadpic&lid=<?php echo $this->__muant["lid"] ?>" width="260" frameborder="0" height="22" allowTransparency='true' name="uploadpic"></iframe></div>
		<div>上传(生成大中小图)</div>
		<div class="cls"></div>
    </div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"> 商品属性:</div>
		<div class="cls"></div>
    </div>
	<?php $atrinum = count($this->__muant["atr"]); for($atri = 0; $atri<$atrinum; $atri++) { ?>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle"<?php if($this->__muant["atr"]["$atri"]["mainatr"]==1) { ?> class="fb fc"<?php } ?>><?php echo $this->__muant["atr"]["$atri"]["name"] ?>:</div>
	    <div class="fLeft"> 
			<input  name="atrid[]" type="hidden" value="<?php echo $this->__muant["atr"]["$atri"]["id"] ?>">
			<input class="txInput" name="atrvalue[]" type="<?php if($this->__muant["atr"]["$atri"]["mainatr"]==1) { ?>hidden<?php } else { ?>text<?php } ?>" value="<?php echo $this->__muant["atr"]["$atri"]["value"] ?>"></div>
		<div class="cls"></div>
    </div>
	<?php } ?>
	<div class="cls"></div>
<?php } ?>
</div>
<div id="divMainBodyContentSumbit">
<input name="ok" type="submit" value="  o  k  " class="txInput">
<input name="reset" type="reset" value="reset" class="txInput">
<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
<input type="hidden" name="pid" value="<?php echo $this->__muant["pid"] ?>">
<?php if($this->__muant["pid"]=='') { ?><input type="hidden" name="cid" value="<?php echo $this->__muant["cid"] ?>"><?php } ?>
</div>
</form>
<script language="javascript">
function setPic(val) {
	document.form1.product_s.value = val;
	document.form1.product_m.value = val;
	document.form1.product_b.value = val;
}
function MP_jumpMenu(val) {
	location.href = "admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid="+val;
}
<!-- TinyMCE -->
	// O2k7 skin
	tinyMCE.init({
		// General options
		mode : "exact",
		elements : "product_des",
		theme : "advanced",
		skin : "o2k7",
		plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,inlinepopups",

		// Theme options
		theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,outdent,indent,|,fontselect,fontsizeselect,|,forecolor,backcolor,|,removeformat,fullscreen,code,preview",
		theme_advanced_buttons2 : "cut,copy,paste,|,search,replace,|,undo,redo,|,link,unlink,image,cleanup,|,insertdate,inserttime,|,tablecontrols,|,sub,sup,charmap,media,advhr",
		theme_advanced_buttons3 : "",
		//theme_advanced_buttons4 : "",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_statusbar_location : "bottom",
		theme_advanced_resizing : true,

		// Example content CSS (should be your site CSS)
		content_css : "css/content.css",

		// Drop lists for link/image/media/template dialogs
		template_external_list_url : "lists/template_list.js",
		external_link_list_url : "lists/link_list.js",
		external_image_list_url : "lists/image_list.js",
		media_external_list_url : "lists/media_list.js",

		// Replace values for the template plugin
		template_replace_values : {
			username : "Some User",
			staffid : "991234"
		}
	});
<!-- /TinyMCE -->
function checkData() {
	/*if(document.form1.nopic.checked == true && document.form1.product_no.value == '') {
		alert('编号不能为空！');
		document.form1.product_no.focus;
		return false;
	}*/
	if(document.form1.product_name.value == '') {
		alert('品名不能为空！');
		document.form1.product_name.focus;
		return false;
	}
	if(document.form1.product_price.value == '') {
		alert('售价不能为空！');
		document.form1.product_price.focus;
		return false;
	}
	if(document.form1.product_store.value == '') {
		alert('库存不能为空！');
		document.form1.product_store.focus;
		return false;
	}
	if(document.form1.special_offer.checked == true && document.form1.product_special_price.value == '') {
		alert('您选择了商品为促销产品，但促销价格为空！');
		document.form1.product_special_price.focus;
		return false;
	}
	if(document.form1.market_offer.checked == true && document.form1.price_market.value == '') {
		alert('您选择了显示市场价，但市场价格为空！');
		document.form1.price_market.focus;
		return false;
	}
	if(document.form1.member_offer.checked == true && document.form1.product_member_price.value == '') {
		alert('您选择了显示会员价，但会员价格为空！');
		document.form1.product_member_price.focus;
		return false;
	}
}
classesFix();
</script>
</body>
</html>