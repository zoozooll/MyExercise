<?php

/**
 *@author Billy
 */
//Factory auto include Class
function factoryLoader($classname){
    $classList = array('../vo/' . $classname . '.php' , '../Dao/' . $classname . '.php');
    foreach ($classList as $value) {
        is_file($value) && require_once ($value);
    }
}

function factoryLoaderDir($classname){
    $classList = array('vo/' . $classname . '.php' , 'Dao/' . $classname . '.php');
    foreach ($classList as $value) {
        is_file($value) && require_once ($value);
    }
}

//User add
function InitUserInfo(&$firstName, &$lastName, &$sex, &$email, &$tel, &$address, &$introducer = ''){
    global $_POST;
    $firstName = daddcslashes($_POST['firstName']);
    $lastName = daddcslashes($_POST['lastName']);
    $sex = daddcslashes($_POST['sex']);
    $email = daddcslashes($_POST['email']);
    $tel = daddcslashes($_POST['tel']);
    $address = daddcslashes($_POST['address']);
    $introducer = daddcslashes($_POST['introducer']);
}

function InitLoginInfo(&$username, &$password){
    global $_POST;
    $username = daddcslashes($_POST['username']);
    $password = daddcslashes($_POST['password']);
}

function getMethod(&$method){
    global $_GET;
    $method = $_GET['method'];
}

//转义数据
function daddcslashes($input){
    if (get_magic_quotes_gpc() == '0') {
        return addslashes((htmlspecialchars(trim($input))));
    } else {
        return (htmlspecialchars(trim($input)));
    }
}

function initAdminPost(&$username, &$password){
    global $_POST;
    $username = daddcslashes($_POST['username']);
    $password = daddcslashes($_POST['password']);
}

function href($url){
    echo "<script>window.location.href = '$url';</script>";
}

function jsAlert($string){
    require_js();
    echo "<script>$.messager.alert('warning','$string','info');;</script>";
}

function script($code){
    echo "<script>$code</script>";
}

// rand number
function randNum($length){
    $list = array('a' , 'b' , 'c' , 'd' , 'e' , 'f' , 'g' , 'h' , 'i' , 'j' , 'k' , 'l' , 'm' , 'n' , 'o' , 'p' , 'r' , 's' , 't' , 'u' , 'v' , 'w' , 'x' , 'y' , 'z' , 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 0);
    $str = '';
    for ($i = 0; $i < $length; $i ++) {
        $str .= $list[rand(0, count($list))];
    }
    return $str;
}

function initProductPost(&$code, &$product_name_en, &$product_name_tw, &$action_en, &$action_tw, &$clinical_en, &$clinical_tw, &$directions_en, &$directions_tw, &$price, &$display, &$sort, &$brand, &$discount, &$aust, &$product_type){
    $code = daddcslashes($_POST['code']);
    $product_name_en = daddcslashes($_POST['product_name_en']);
    $product_name_tw = daddcslashes($_POST['product_name_tw']);
    $action_en = daddcslashes($_POST['action_en']);
    $action_tw = daddcslashes($_POST['action_tw']);
    $clinical_en = daddcslashes($_POST['clinical_en']);
    $clinical_tw = daddcslashes($_POST['clinical_tw']);
    $directions_en = daddcslashes($_POST['directions_en']);
    $directions_tw = daddcslashes($_POST['directions_tw']);
    $price = daddcslashes($_POST['price']);
    $display = daddcslashes($_POST['display']);
    $sort = daddcslashes($_POST['sort']);
    $brand = daddcslashes($_POST['brand']);
    $discount = daddcslashes($_POST['discount'] ? $_POST['discount'] : '100');
    $aust = daddcslashes($_POST['aust']);
    $product_type = daddcslashes($_POST['product_type']);
}

function initOrderPost(&$name2, &$sex, &$dz, &$yb, &$tel, &$email, &$ly, &$province){
    $name2 = daddcslashes($_POST['name2']);
    $sex = daddcslashes($_POST['sex']);
    $dz = daddcslashes($_POST['dz']);
    $yb = daddcslashes($_POST['yb']);
    $tel = daddcslashes($_POST['tel']);
    $email = daddcslashes($_POST['email']);
    $ly = daddcslashes($_POST['ly']);
    $province = daddcslashes($_POST['province']);
}

function initAddAdminPost(&$username, &$password, &$usercms, &$productcms, &$ordercms, &$admincms, &$id = 0){
    $username = daddcslashes($_POST['username']);
    $password = daddcslashes($_POST['password']);
    $id = daddcslashes($_POST['id']);
    $usercms = daddcslashes($_POST['cms']['user'] ? $_POST['cms']['user'] : 0);
    $productcms = daddcslashes($_POST['cms']['product'] ? $_POST['cms']['product'] : 0);
    $ordercms = daddcslashes($_POST['cms']['order'] ? $_POST['cms']['order'] : 0);
    $admincms = daddcslashes($_POST['cms']['admin'] ? $_POST['cms']['admin'] : 0);
}

function initUpdateUser(&$firstName, &$lastName, &$password, &$tel, &$address, &$introducer){
    $firstName = daddcslashes($_POST['firstName']);
    $lastName = daddcslashes($_POST['lastName']);
    $password = daddcslashes($_POST['password']);
    $tel = daddcslashes($_POST['tel']);
    $address = daddcslashes($_POST['address']);
    $introducer = daddcslashes($_POST['introducer']);
}

function initForgetPassword(&$email){
    $email = daddcslashes($_POST['email']);
}

function dayTime($value){
    return intval($value / (60 * 60 * 24));
}

function require_js( ){
    echo "<script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>
		  <script type=\"text/javascript\" src=\"js/jquery.tools.min.js\"></script>
		  <script type=\"text/javascript\" src=\"js/jquery.easyui.min.js\"></script>
		  <script type=\"text/javascript\" src=\"js/jquery.easing.1.3.js\" ></script>";
}

function initUpdateProduct(&$code, &$product_name_en, &$product_name_tw, &$action_en, &$action_tw, &$clinical_en, &$clinical_tw, &$directions_en, &$directions_tw, &$price, &$display, &$aust, &$discount, &$brand, &$product_type, &$sort, &$id){
    $P = $_POST;
    $code = daddcslashes($P['code']);
    $product_name_en = daddcslashes($P['product_name_en']);
    $product_name_tw = daddcslashes($P['product_name_tw']);
    $action_en = daddcslashes($P['action_en']);
    $action_tw = daddcslashes($P['action_tw']);
    $directions_tw = daddcslashes($P['directions_tw']);
    $clinical_en = daddcslashes($P['clinical_en']);
    $clinical_tw = daddcslashes($P['clinical_tw']);
    $directions_en = daddcslashes($P['directions_en']);
    $price = daddcslashes($P['price']);
    $display = daddcslashes($P['display']);
    $aust = daddcslashes($P['aust']);
    $discount = daddcslashes($P['discount']);
    $brand = daddcslashes($P['brand']);
    $product_type = daddcslashes($P['product_type']);
    $sort = daddcslashes($P['sort']);
    $id = daddcslashes($P['id']);
}

function cartCount( ){
    foreach (explode("@", $_SESSION[producelist]) as $value) {
        $value != "" && $count[] = $value;
    }
    return count($count);
}

function check_is_chinese($s){
    return preg_match('/[\x80-\xff]./', $s);
}

function product_element_Count($array){
    $count = 0;
    foreach ($array as $key => $value) {
        if (preg_match('/' . $_SESSION['lang'] . '/i', $key)) {
            $count ++;
        }
    }
    return $count;
}

function cnSubstr($str, $start, $len){
    $str_tmp = $len - $start;
    if (strlen($str) < $str_tmp) {
        $tmpstr = $str;
    } else {
        $tmpstr = "";
        $strlen = $start + $len;
        for ($i = 0; $i < $strlen; $i ++) {
            if (ord(substr($str, $i, 1)) > 0xa0) {
                $tmpstr .= substr($str, $i, 2);
                $i ++;
            } else {
                $tmpstr .= substr($str, $i, 1);
            }
        }
        //$tmpstr .= "..";
    }
    return $tmpstr;
}

function strstrstr($value, $sign = '.'){
    $result = '';
    for ($i = 0; $i < strlen($value); $i ++) {
        if ($value[$i] != $sign) {
            $result .= $value[$i];
        } else {
            break;
        }
    }
    return $result;
}
//分頁
function page($page,$total,$phpfile, $query_string = NULL, $pagesize=10,$pagelen=7){
    $pagecode = '';//定义变量，存放分页生成的HTML
    $page = intval($page);//避免非数字页码
    $total = intval($total);//保证总记录数值类型正确
    if(!$total) return array();//总记录数为零返回空数组
    $pages = ceil($total/$pagesize);//计算总分页
    //处理页码合法性
    if($page<1) $page = 1;
    if($page>$pages) $page = $pages;
    //计算查询偏移量
    $offset = $pagesize*($page-1);
    //页码范围计算
    $init = 1;//起始页码数
    $max = $pages;//结束页码数
    $pagelen = ($pagelen%2)?$pagelen:$pagelen+1;//页码个数
    $pageoffset = ($pagelen-1)/2;//页码个数左右偏移量
    
    //生成html
    $pagecode='<div class="page">';
    $pagecode.="<span>Page $page of $pages </span>";//第几页,共几页
    //如果是第一页，则不显示第一页和上一页的连接
    if($page!=1){
        $pagecode.="<a style='text-decoration:none;' href=\"{$phpfile}?page=1{$query_string}\">First| </a>";//第一页
        $pagecode.="<a style='text-decoration:none;' href=\"{$phpfile}?page=".($page-1)."{$query_string}\">Prev</a>";//上一页
    }
    //分页数大于页码个数时可以偏移
    if($pages>$pagelen){
        //如果当前页小于等于左偏移
        if($page<=$pageoffset){
            $init=1;
            $max = $pagelen;
        }else{//如果当前页大于左偏移
            //如果当前页码右偏移超出最大分页数
            if($page+$pageoffset>=$pages+1){
                $init = $pages-$pagelen+1;
            }else{
                //左右偏移都存在时的计算
                $init = $page-$pageoffset;
                $max = $page+$pageoffset;
            }
        }
    }
    //生成html
    for($i=$init;$i<=$max;$i++){
        if($i==$page){
            $pagecode.='<span> '.$i.' </span>';
        } else {
            $pagecode.="<a style='text-decoration:none;' href=\"{$phpfile}?page={$i}{$query_string}\"> $i </a>";
        }
    }
    if($page!=$pages){
        $pagecode.=" "."<a style='text-decoration:none;' href=\"{$phpfile}?page=".($page+1)."{$query_string}\">Next</a>"." | ";//下一页
        $pagecode.="<a style='text-decoration:none;' href=\"{$phpfile}?page={$pages}{$query_string}\">Last</a>"."  ";//最后一页
    }
    $pagecode.="$total Record(s)</div>";
    return array('pagecode'=>$pagecode,'sqllimit'=>' limit '.$offset.','.$pagesize);
}


	function p($value){
		print_r($value);
		die();
	}
	
	function count_array_num($array) {
		$count = count($array);
		$num = 0;
		for ($i = 0; $i < $count; $i++) {
				if ($array[$i] != NULL) {
					$num++;	
				}
		}	
		return $num;
	}
	function type(){
		$type = array(
			'video' => array (
			'tv' => 'tv',
			'web' => 'web',
			'video' => 'video'
		),
		'client' => array ('No', 'Yes')
	);
	return $type;
}
function strcut($str,$start,$len){  
    if($start < 0)  
        $start = strlen($str)+$start;  
      
    $retstart = $start+getOfFirstIndex($str,$start);  
    $retend = $start + $len -1 + getOfFirstIndex($str,$start + $len);   
    return substr($str,$retstart,$retend-$retstart+1);  
}  
//判断字符开始的位置  
function getOfFirstIndex($str,$start){  
    $char_aci = ord(substr($str,$start-1,1));  
    if(223<$char_aci && $char_aci<240)  
        return -1;  
    $char_aci = ord(substr($str,$start-2,1));  
    if(223<$char_aci && $char_aci<240)  
        return -2;  
    return 0;  
} 

function product_title($value){
	$title = NULL;
	switch ($value['language'])	{
		case 'chi' : {
			switch ($value['string']) {
				case  'corporate': {
					$title = '激光/光学测量仪器';
					break;	
				}
				case 'tv': {
					$title = '固体/半导体激光器';
					break;	
				}
				case 'print': {
					$title = '光纤激光器';
					break;
				}
				case 'advertising': {
					$title = '电光器件';
					break;	
				}
				case 'promotion': {
					$title = '法拉第隔离器';
					break;	
				}
				case 'event' : {
					$title = '光子晶体光纤/激光玻璃';
					break;	
				}
				case 'premeium': {
					$title = 'NESLAB ThermoFlex 冷却水循环器';
					break;	
				}
				case 'video': {
					$title = '光机械及光学元器件';
					break;	
				}
				default: {
						$title = '所有产品';
						break;
				}
			}	
			break;
		}	
		case 'eng': {
			switch ($value['string']) {
				case  'corporate': {
					$title = 'Laser instrument';
					break;	
				}
				case 'tv': {
					$title = 'Semiconductor lasers';
					break;	
				}
				case 'print': {
					$title = 'Optical fiber laser';
					break;
				}
				case 'advertising': {
					$title = 'Lightning device';
					break;	
				}
				case 'promotion': {
					$title = 'Faraday isolator';
					break;	
				}
				case 'event' : {
					$title = 'Photonic crystal fiber';
					break;	
				}
				case 'premeium': {
					$title = 'NESLAB ThermoFlex Cooling water circulation is';
					break;	
				}
				case 'video': {
					$title = 'Light machinery';
					break;	
				}
				default: {
						$title = 'All Product';
						break;
				}
			}	
			break;
		}
	}
	return $title;
}
?>