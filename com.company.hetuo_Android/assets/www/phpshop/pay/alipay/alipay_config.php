<?php
$partner = "";//合作伙伴ID
$security_code = "";//安全检验码
$seller_email = "";//卖家邮箱
$_input_charset = "utf-8"; //字符编码格式  目前支持 GBK 或 utf-8
$sign_type = "MD5"; //加密方式  系统默认(不要修改)
$transport= "https";//访问模式,你可以根据自己的服务器是否支持ssl访问而选择http以及https访问模式(系统默认,不要修改)
$notify_url = "http://www.1buluo.com/lshop/pay/alipay/notify_url.php";// 异步返回地址 需要填写完整的路径
$return_url = "http://www.1buluo.com/lshop/pay/alipay/return_url.php"; //同步返回地址  需要填写完整大额路径
$show_url   =""  //你网站商品的展示地址,可以为空

/** 提示：如何获取安全校验码和合作ID
登陆 www.alipay.com 后,点商户后台 ,导航栏的下面可以看到
*/
?>