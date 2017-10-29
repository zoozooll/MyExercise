<html>
<head>
<TITLE>{$__Meta.Title}</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset={$__CHARSET}">
<META HTTP-EQUIV="Content-Language" content="{$__LANGUAGE}">
<META NAME="description" CONTENT="{$__Meta.Description}">
<META NAME="keywords" CONTENT="{$__Meta.Keywords}">
<META name="copyright" CONTENT="{$__Meta.Content}">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
</head>
<body>
<form method="post" action="admincp.php" name="form1" id="form1" target="adminMain">
<div style="border-left:#88ACD9 solid 1px; border-right:#88ACD9 solid 1px;">
<div id="TopTool" class="bgcolor9 line2">
	<div class="le" id="dvFolderSearchinbox">&nbsp;</div>
	<div class="ri">&nbsp;<b>请详细填下面的信息</b></div>
</div>
<div id="MailHeader">
</div>	
<div class="mp" id="MailListMaininbox">
	<div>
		<div class="bgcolor10" align="center">&nbsp;界面与显示方式</div>
	</div>
	<div id="dvPeriod_d_inbox3">
		<div id="dvLetterList">
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divLetter">
				<div class="f12h fLeft" style="width:180px; padding:5px;">会员类别:</div>
				<div>
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divLetter">
				<div class="f12h fLeft" style="padding:5px;"><input name="del" type="checkbox" value="" class="txInput" onClick="checkgp();"></div>
				<div class="f12h fLeft" style="width:100px; padding:5px;">名称</div>
				<div class="f12h fLeft" style="width:50px; padding:5px;">积分</div>
				<div class="f12h fLeft" style="width:50px; padding:5px;">威望</div>
				<div class="f12h fLeft" style="width:50px; padding:5px;">金钱</div>
				<div class="f12h fLeft" style="width:50px; padding:5px;">发帖数</div>
				<div class="f12h fLeft" style="width:100px; padding:5px;">生日</div>
				<div class="f12h fLeft" style="width:80px; padding:5px;">登录次数</div>
				<div class="f12h fLeft" style="width:150px; padding:5px;">最后登录时间</div>
			</div>
			{$loop urs}
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divLetter">
				<div class="f12h fLeft" style="padding:5px;"><input name="tid[]" type="checkbox" value="{$urs.loop.id}" id="tid[]" class="txInput"{if urs.loop.orderid==0}{if urs.next!=0} disabled{/if}{/if} onClick="changeName(this)"></div>
				<div class="f12h fLeft" style="width:120px; padding:5px;">
				{if urs.loop.orderid>0}+{/if}
				<input name="name[]" id="name{$urs.loop.id}" type="text" class="txInput" value="{$urs.loop.name}" size="12" disabled>
				</div>
				<div class="f12h fLeft" style="width:50px; padding:5px;">{$value.loop.integral}</div>
				<div class="f12h fLeft" style="width:50px; padding:5px;">{$value.loop.prestige}</div>
				<div class="f12h fLeft" style="width:50px; padding:5px;">{$value.loop.money}</div>
				<div class="f12h fLeft" style="width:50px; padding:5px;">{$value.loop.postnum}</div>
				<div class="f12h fLeft" style="width:100px; padding:5px;">{$value.loop.birthday}</div>
				<div class="f12h fLeft" style="width:80px; padding:5px;">{$value.loop.vistitime}</div>
				<div class="f12h fLeft" style="width:150px; padding:5px;">{$value.loop.lastlogin}</div>
			</div>
			{/loop}
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divLetter">
				<div class="f12h fLeft" style="width:180px; padding:5px;"></div>
		        <div class="f12h fLeft" style="width:180px; padding:5px;"></div>
			</div>
		</div>
	</div>
	<div>
	  <div class="bgcolor10" align="center">
	  </div>
	</div>
	<div>
	  <div class="bgcolor10" align="center">
		  <input name="ok" type="submit" value="  update  " class="txInput">&nbsp;
		  <input name="deled" type="submit" value="  del  " class="txInput">
		  <input type="hidden" name="lid" value="{$lid}">
		  <input type="hidden" name="mid" value="{$mid}">
	      <input name="cid" type="hidden" id="cid" value="{$cid}">
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
<script language="javascript">
function checkgp(){
	var chb;
	chb = document.getElementsByName ("tid[]");
	if(chb.length){  
	  for (i = 0; i < chb.length; i++) {
		  if(chb[i].checked == false){
			  chb[i].checked = true;
			  if(chb[i].disabled == true) {
			  } else {
		  	 	 changeName(chb[i]);
			  }
		  } else {
			  chb[i].checked = false;
		  	  changeName(chb[i]);
		  }
	  }
	}
}
function changeName(id) {
	n = "name"+id.value;
	chb = document.getElementById (n);
	if(id.checked == true) {
		chb.disabled = false;
	} else {
		chb.disabled = true;
	}
	return null;
}
</script>