<html>
<head>
<TITLE>{$__Meta.Title}</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset={$__CHARSET}">
<META HTTP-EQUIV="Content-Language" content="{$__LANGUAGE}">
<META NAME="description" CONTENT="{$__Meta.Description}">
<META NAME="keywords" CONTENT="{$__Meta.Keywords}">
<META name="copyright" CONTENT="{$__Meta.Content}">
<link href="../css/admin/css.css" rel="stylesheet" type="text/css" />
</head>
<body class="bgcolor11" topmargin="0" leftmargin="0">
<form action="adminajaxset.php?switch={$upload}&lid={$lid}" method="post" enctype="multipart/form-data" name="form1" target="uploadpic" id="form1">
<input type="file" name="userfile" class="txInput"> <input name="uploadpic" class="txInput" type="submit" value="上传"> {$upload_success_script}
</form>

</body></html>