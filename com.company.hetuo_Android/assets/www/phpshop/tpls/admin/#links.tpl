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
<form method="post" action="adminset.php" name="form1" id="form1" target="adminMain">
<div style="border-left:#88ACD9 solid 1px; border-right:#88ACD9 solid 1px;">
<div id="TopTool" class="bgcolor9 line2">
	<div class="le" id="dvFolderSearchinbox">&nbsp;</div>
	<div class="ri">&nbsp;<b>请详细填下面的信息</b></div>
</div>
<div id="MailHeader">
</div>	
<div class="mp" id="MailListMaininbox">
	<div>
		<div class="bgcolor10" align="center">&nbsp;添加新用户</div>
	</div>
	<div id="dvPeriod_d_inbox3">
		<div id="dvLetterList">
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divLetter">
				<div class="f12h fLeft" style="width:180px; padding:5px;"></div>
				<div>
				<select name="ucid"  class="txInput" id="ucid">
				  <option value="0">-新类别-</option>
				{$loop urs}
				  <option value="{if urs.loop.orderid>0}{$urs.loop.id}{else}{$urs.loop.uctype}{/if}"{if urs.loop.id==uctype} selected{/if}>{if urs.loop.orderid>0}&#8212; {/if}{$urs.loop.name}</option>
				{/loop}				
				</select>
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divLetter">
				<div class="f12h fLeft" style="width:180px; padding:5px;">名称:</div>
				<div><input name="name" type="text" class="txInput" id="name" value="{$value.name}" >
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divLetter">
				<div class="f12h fLeft" style="width:180px; padding:5px;">类别名称(中文):</div>
				<div><input name="uctypename" type="text" class="txInput" id="uctypename" value="{$value.uctypename}" >
				</div>
			</div>
			<div class="m2 bgcolor11" style="position: relative; cursor: default;" id="divLetter">
				<div class="f12h fLeft" style="width:180px; padding:5px;">类别(英文字母):</div>
			    <div><input name="uctype" type="text" class="txInput" id="uctype" value="{$value.uctype}" >
			  </div>
			</div>
		</div>
	</div>
	<div>
	  <div class="bgcolor10" align="center">
		  <input name="ok" type="submit" value="  o  k  " class="txInput">&nbsp;
		  <input name="reset" type="reset" value="reset" class="txInput">
		  <input type="hidden" name="lid" value="{$lid}">
		  <input type="hidden" name="mid" value="{$mid}">
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