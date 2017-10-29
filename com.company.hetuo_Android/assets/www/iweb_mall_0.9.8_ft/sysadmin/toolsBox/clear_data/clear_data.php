<?php
	//数据表定义区
	$t_users=$tablePreStr."users";
	$t_user_rank=$tablePreStr."user_rank";
	$t_user_info=$tablePreStr."user_info";
	$t_user_favorite=$tablePreStr."user_favorite";
	$t_user_address=$tablePreStr."user_address";
	$t_shop_request=$tablePreStr."shop_request";
	$t_shop_payment=$tablePreStr."shop_payment";
	$t_shop_inquiry=$tablePreStr."shop_inquiry";
	$t_shop_info=$tablePreStr."shop_info";
	$t_shop_honor=$tablePreStr."shop_honor";
	$t_shop_guestbook=$tablePreStr."shop_guestbook";
	$t_shop_category=$tablePreStr."shop_category";
	$t_shop_article=$tablePreStr."shop_article";
	$t_shipping=$tablePreStr."shipping";
	$t_remind_user=$tablePreStr."remind_user";
	$t_remind_info=$tablePreStr."remind_info";
//	$t_remind=$tablePreStr."remind";
//	$t_privilege=$tablePreStr."privilege";
//	$t_payment=$tablePreStr."payment";
	$t_order_info=$tablePreStr."order_info";
//	$t_mailtpl=$tablePreStr."mailtpl";
	$t_keywords_count=$tablePreStr."keywords_count";
//	$t_index_images=$tablePreStr."index_images";
	$t_goods_types=$tablePreStr."goods_types";
	$t_goods_shipping=$tablePreStr."goods_shipping";
	$t_goods_gallery=$tablePreStr."goods_gallery";
	$t_goods_comment=$tablePreStr."goods_comment";
	$t_goods_attr=$tablePreStr."goods_attr";
	$t_goods=$tablePreStr."goods";
//	$t_crons=$tablePreStr."crons";
	$t_credit=$tablePreStr."credit";
//	$t_category=$tablePreStr."category";
	$t_cart=$tablePreStr."cart";
	$t_brand=$tablePreStr."brand";
	$t_attribute=$tablePreStr."attribute";
//	$t_asd_position=$tablePreStr."asd_position";
	$t_asd_content=$tablePreStr."asd_content";
	$t_article_cat=$tablePreStr."article_cat";
	$t_article=$tablePreStr."article";
	
	$tablearray=array(
		$t_users,
		$t_user_rank,
		$t_user_info,
		$t_user_favorite,
		$t_user_address,
		$t_shop_request,
		$t_shop_payment,
		$t_shop_inquiry,
		$t_shop_info,
		$t_shop_honor,
		$t_shop_guestbook,
		$t_shop_category,
		$t_shop_article,
		$t_shipping,
		$t_remind_user,
		$t_remind_info,
		$t_order_info,
		$t_keywords_count,
		$t_goods_types,
		$t_goods_shipping,
		$t_goods_gallery,
		$t_goods_comment,
		$t_goods_attr,
		$t_goods,
		$t_credit,
		$t_cart,
		$t_brand,
		$t_attribute,
		$t_asd_content,
		$t_article_cat,
		$t_article
	);
	
	dbtarget('w',$dbServs);
	$dbo=new dbex;
	
	$show_str='';
	
	foreach($tablearray as $val){
		$sql="TRUNCATE TABLE `$val`;";
		if($dbo->exeUpdate($sql)){
			$show_str.="清理".$val."成功！<br>";
		}else{
			$show_str.="<font color=red>清理".$val."失败！</font><br>";
		}
	}
	echo $show_str;
	
//	$sql="select photo_src , photo_thumb_src from $t_photo ";
//	$photo_array=$dbo->getRs($sql);
//	foreach($photo_array as $val){
//		unlink("../".$val['photo_src']);
//		unlink("../".$val['photo_thumb_src']);
//	}
?>