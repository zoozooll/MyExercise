<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

function shop_url($shop_id,$app='index',$page=''){
	global $url_rewrite;
	if($url_rewrite) {
		$url_rewrite=='1' ? $url = "shop/$shop_id" : $url = "shop.php/$shop_id";
		if($app) {
			$url .= "/$app";
		}
		if($page) {
			$url .= "/$page";
		}
		$url .= '.html';
	} else {
		$url = "shop.php?shopid=$shop_id";
		if($app) {
			$url .= "&app=$app";
		}
		if($page) {
			$url .= "&page=$page";
		}
	}
	return $url;
}

function goods_url($goods_id){
	global $url_rewrite;
	if($url_rewrite) {
		$url_rewrite=='1' ? $url = "goods/$goods_id" : $url = "goods.php/$goods_id";
		$url .= '.html';
	} else {
		$url = "goods.php?id=$goods_id";
	}
	return $url;
}

function article_url($article_id){
	global $url_rewrite;
	if($url_rewrite) {
		$url_rewrite=='1' ? $url = "article/$article_id" : $url = "article.php/$article_id";
		$url .= '.html';
	} else {
		$url = "article.php?id=$article_id";
	}
	return $url;
}

function article_list_url($id,$page=''){
	global $url_rewrite;
	if($url_rewrite) {
		$url_rewrite=='1' ? $url = "article_list/$id" : $url = "article_list.php/$id";
		if($page) {
			$url .= "/$page";
		}
		$url .= '.html';
	} else {
		$url = "article_list.php?id=$id";
		if($page) {
			$url .= "&page=$page";
		}
	}
	return $url;
}


function ucategory_url($id,$page=''){
	global $url_rewrite;
	if($url_rewrite) {
		$url_rewrite=='1' ? $url = "ucategory/$id" : $url = "ucategory.php/$id";
		if($page) {
			$url .= "/$page";
		}
		$url .= '.html';
	} else {
		$url = "ucategory.php?id=$id";
		if($page) {
			$url .= "&page=$page";
		}
	}
	return $url;
}

function category_url($id,$page=''){
	global $url_rewrite;
	if($url_rewrite) {
		$url_rewrite=='1' ? $url = "category/$id" : $url = "category.php/$id";
		if($page) {
			$url .= "/$page";
		}
		$url .= '.html';
	} else {
		$url = "category.php?id=$id";
		if($page) {
			$url .= "&page=$page";
		}
	}
	return $url;
}

function urlRewrite(){
	$script_name = basename($_SERVER['SCRIPT_NAME']);
	$request_str = strstr($_SERVER['REQUEST_URI'],'.php');
	$request_str=ereg_replace("\.(html|htm|php)$",'',$request_str);
	$request_arr=explode('/',$request_str);
	array_shift($request_arr);
	if($script_name == 'goods.php' || $script_name == 'article.php') {
		isset($request_arr[0]) && $_GET['id'] = $request_arr[0];
	} elseif ($script_name == 'shop.php') {
		isset($request_arr[0]) && $_GET['shopid'] = $request_arr[0];
		isset($request_arr[1]) && $_GET['app'] = $request_arr[1];
		isset($request_arr[2]) && $_GET['page'] = $request_arr[2];
	} elseif ($script_name == 'category.php' || $script_name == 'ucategory.php' || $script_name == 'article_list.php') {
		isset($request_arr[0]) && $_GET['id'] = $request_arr[0];
		isset($request_arr[1]) && $_GET['page'] = $request_arr[1];
	}
}

if($url_rewrite && $url_rewrite=='2') {
	urlRewrite();
}
?>