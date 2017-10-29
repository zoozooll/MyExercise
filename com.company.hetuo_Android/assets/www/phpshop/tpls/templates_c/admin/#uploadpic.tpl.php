<html>
<head>
<TITLE><?php echo $this->__muant["__Meta"]["Title"] ?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META NAME="description" CONTENT="<?php echo $this->__muant["__Meta"]["Description"] ?>">
<META NAME="keywords" CONTENT="<?php echo $this->__muant["__Meta"]["Keywords"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
<link href="../css/admin/css.css" rel="stylesheet" type="text/css" />
</head>
<body class="bgcolor11" topmargin="0" leftmargin="0">
<form action="adminajaxset.php?switch=<?php echo $this->__muant["upload"] ?>&lid=<?php echo $this->__muant["lid"] ?>" method="post" enctype="multipart/form-data" name="form1" target="uploadpic" id="form1">
<input type="file" name="userfile" class="txInput"> <input name="uploadpic" class="txInput" type="submit" value="上传"> <?php echo $this->__muant["upload_success_script"] ?>
</form>

</body></html>