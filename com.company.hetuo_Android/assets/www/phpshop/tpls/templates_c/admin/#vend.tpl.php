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
function MP_jumpMenu(val) {
	location.href = "admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&cid="+val;
}
</script>
</head>
<body>
<form action="adminset.php" method="post" enctype="multipart/form-data" name="form1" target="adminMain" id="form1">
<div style="border-left:#88ACD9 solid 1px; border-right:#88ACD9 solid 1px;">
<div id="TopTool" class="bgcolor9 line2">
	<div class="le" id="dvFolderSearchinbox">&nbsp;</div>
	<div class="ri">&nbsp;<b>添加商品</b></div>
</div>
<div id="MailHeader">
</div>	
<div class="mp" id="MailListMaininbox">
	<div>
		<div class="bgcolor10" align="center">&nbsp;</div>
	</div>
	<div id="dvPeriod_d_inbox3">
		<div id="dvLetterList">
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divLetter">
				<div class="f12h fLeft" style="width:180px; padding:5px;">类别:</div>
				<div id="hiddenselect">
				<select name="cid"  class="txInput" id="cid" onChange="MP_jumpMenu(this.value)">
				  <option value="">....请选择</option>
				  <?php $classesinum = count($this->__muant["classes"]); for($classesi = 0; $classesi<$classesinum; $classesi++) { ?>
				  <option value="<?php echo $this->__muant["classes"]["$classesi"]["id"] ?>"<?php if($this->__muant["classes"]["$classesi"]["id"]==$this->__muant["cid"]) { ?> selected<?php } ?>>
				  <?php echo $this->__muant["classes"]["$classesi"]["_name"] ?></option>
				  <?php } ?>
				</select>
				</div>
			</div>
			<div id="dvPeriod_d_inbox5">
				<div id="dvLetterList">
				
				</div>
			</div>
			
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 品名:</div>
				<div>
				   <input name="product_name" type="text"> 库存<input name="product_store" type="text" value="" size="5" maxlength="5">
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 价格:</div>
				<div>
				   <input name="product_price" type="text">
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 编号:</div>
				<div>
				   <input name="product_no" type="text">
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 厂编:</div>
				<div>
				   <input name="product_factory_number" type="text">
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 品牌:</div>
			  <div>
					   <select name="product_brand">
				     <option value="0">　</option>
				   </select>
			    </div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 系列:</div>
				<div>
				   <select name="product_series">
				     <option value="0">　</option>
				   </select>
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 型号:</div>
				<div>
				   <select name="product_model">
				     <option value="0">　</option>
				   </select>
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 描述:</div>
				<div>
				   <textarea name="product_des" cols="50" rows="2"></textarea>
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 小图:</div>
			  <div>
				   <input name="product_s" type="text">
				   <input type="file" name="file"> 上传(同时生成大中小图)
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 中图:</div>
				<div>
				   <input name="product_m" type="text">
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"> 大图:</div>
				<div>
				   <input name="product_b" type="text">
				</div>
			</div>
			<div>
				<div class="bgcolor10" align="center">&nbsp;商品属性 </div>
			</div>
			<?php $atrinum = count($this->__muant["atr"]); for($atri = 0; $atri<$atrinum; $atri++) { ?>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divvalue">
				<div class="f12h fLeft" style="width:180px; padding:5px;"><?php echo $this->__muant["atr"]["$atri"]["name"] ?></div>
				<div>
				   <input name="value<?php echo $this->__muant["atr"]["$atri"]["orderid"] ?>" type="text">
				</div>
			</div>
			<?php } ?>
			
			
		</div>
	</div>
	<div>
	  <div class="bgcolor10" align="center">
		  <input name="ok" type="submit" value="  o  k  " class="txInput">&nbsp;
		  <input name="reset" type="reset" value="reset" class="txInput">
		  <input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
		  <input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
	  </div>
	</div>
	<div id="dvPeriod_d_inbox5">
		<div id="dvLetterList"></div>
	</div>
</div>
<div id="DownNav">
  <!--div class="le">&nbsp;</div>
  <div class="ri3">&nbsp;</div-->
  <div class="ri4">&nbsp;</div>
</div>
<div id="DownTool" class="bgcolor9 line2">&nbsp;</div>
</div>
</form>
</body></html>