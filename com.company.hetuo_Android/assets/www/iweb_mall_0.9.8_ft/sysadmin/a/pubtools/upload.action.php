<?php

//	语言包引入
$a_langpackage=new adminlp;

	$up = new upload();
	$up->set_dir($webRoot.'uploadfiles/news/','{y}/{m}/{d}');
	$fs=$up->execute();

	$realtxt=$fs[0];

	$dbo=new dbex();
	dbtarget('r',$dbServs);

	if($realtxt['flag']==1){
		dbtarget('w',$dbServs);

		$fileSrcStr=str_replace($webRoot,"",$realtxt['dir']).$realtxt['name'];

		echo "<script type='text/javascript'>parent.AddContentImg('$fileSrcStr','1');</script>";
		action_return(1,"",'m.php?app=upload_form');
	}else if($realtxt[flag]==-1){
		action_return(0,$a_langpackage->a_fail,'-1');
	}else if($realtxt[flag]==-2){
		action_return(0,$a_langpackage->a_fail,'-1');
	}
?>

