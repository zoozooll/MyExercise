<?php

require("foundation/module_img_size.php");
require("foundation/module_shop.php");

//	语言包引入
$m_langpackage=new moduleslp;

	$up = new upload();
	$up->set_dir($webRoot.'uploadfiles/goods/','{y}/{m}/{d}');
	$fs=$up->execute();

	$realtxt=$fs[0];

	$dbo=new dbex();
	dbtarget('r',$dbServs);

	$t_shop_info=$tablePreStr."shop_info";
	$t_img_size=$tablePreStr."img_size";

	$row=get_shop_info($dbo,$t_shop_info,$shop_id);
	$img_size=unserialize(get_sess_privilege());
	$img_size_m=$img_size['8'];
	$img_size_k=$img_size_m*1024*1024;
	if($row['count_imgsize']>$img_size_k){
		action_return(0,'您已经上传超过'.$img_size['8'].'M的图片，请清理后继续上传','-1');
	}else if($realtxt['flag']==1){
		dbtarget('w',$dbServs);

		$fileSrcStr=str_replace($webRoot,"",$realtxt['dir']).$realtxt['name'];
		$post[0]['uid']=$user_id;
		$post[0]['img_size']=$realtxt['size'];
		$post[0]['upl_time'] = $ctime->long_time();
		$post[0]['img_url'] = $fileSrcStr;

		img_size($dbo,$t_img_size,$post,$t_shop_info,$row['count_imgsize'],$shop_id);

		echo "<script type='text/javascript'>parent.AddContentImg('$fileSrcStr','1');</script>";
		action_return(1,"",'modules.php?app=upload_form');
	}else if($realtxt[flag]==-1){
		action_return(0,$m_langpackage->m_upl_lose,'-1');
	}else if($realtxt[flag]==-2){
		action_return(0,$m_langpackage->m_upl_lose,'-1');
	}
?>

