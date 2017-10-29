<?php
header("content-type:text/html;charset=utf-8");
$IWEB_SHOP_IN = true;

require_once ("./classes/PayResponseHandler.class.php");
require("../../configuration.php");
require("includes.php");
require("../../foundation/module_order.php");
require_once("../../foundation/ctime.class.php");

$dbo = new dbex;
dbtarget('r',$dbServs);
$t_order_info = $tablePreStr."order_info";
$t_shop_payment = $tablePreStr."shop_payment";

/* 创建支付应答对象 */
$resHandler = new PayResponseHandler();

$sp_billno = $resHandler->getParameter("sp_billno");

$orderinfo = get_order_info_bypayid($dbo,$t_order_info,$sp_billno);
if(!$orderinfo) {exit("非法操作");}

$shop_id=$orderinfo['shop_id'];
$pay_id = 4;

$sql="select * from $t_shop_payment where pay_id=$pay_id and shop_id=$shop_id";
$row = $dbo->getRow($sql);
$payment_info = unserialize($row['pay_config']);
//print_r($row);

/* 密钥 */
//$key = $payment_info['key'];
//
//$resHandler->setKey($key);


$fp = fsockopen ('www.paypal.com', 80, $errno, $errstr, 30);
        if (!$fp)
        {
            fclose($fp);

            return false;
        }
        else
        {
            fputs($fp, $header . $req);
            while (!feof($fp))
            {
                $res = fgets($fp, 1024);
                if (strcmp($res, 'VERIFIED') == 0)
                {

                    dbtarget('w',$dbServs);
					$pay_time = $ctime->long_time();
		
					$sql="update $t_order_info set pay_id=$pay_id,pay_status=1,pay_time='$pay_time' where payid=$sp_billno";
					$row = $dbo->exeUpdate($sql);
                    
                    fclose($fp);

                    echo "<br/>" . "支付成功" . "<br/>";
                }
                elseif (strcmp($res, 'INVALID') == 0)
                {
                    // log for manual investigation
                    fclose($fp);

                    echo "<br/>" . "支付失败" . "<br/>";
                }

            }
        }


?>