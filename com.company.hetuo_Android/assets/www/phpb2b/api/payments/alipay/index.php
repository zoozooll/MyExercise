<?php

require_once("alipay_service.php");
require_once("alipay_config.php");
$parameter = array(
	"service"        => "trade_create_by_buyer",  //��������
	"partner"        => $partner,         //�����̻���
	"return_url"     => $return_url,      //ͬ������
	"notify_url"     => $notify_url,      //�첽����
	"_input_charset" => $_input_charset,  //�ַ�Ĭ��ΪGBK
	"subject"        => "��Ʒ���",       //��Ʒ��ƣ�����
	"body"           => "��Ʒ����",       //��Ʒ�������
	"out_trade_no"   => date(Ymdhms),     //��Ʒ�ⲿ���׺ţ������֤Ψһ�ԣ�
	"price"          => "0.01",           //��Ʒ���ۣ�����۸���Ϊ0��
	"payment_type"   => "1",              //Ĭ��Ϊ1,����Ҫ�޸�
	"quantity"       => "1",              //��Ʒ��������
		
	"logistics_fee"      =>'0.00',        //�������ͷ���
	"logistics_payment"  =>'BUYER_PAY',   //������ø��ʽ��SELLER_PAY(���֧��)��BUYER_PAY(���֧��)��BUYER_PAY_AFTER_RECEIVE(�����)
	"logistics_type"     =>'EXPRESS',     //�������ͷ�ʽ��POST(ƽ��)��EMS(EMS)��EXPRESS(������)

	"show_url"       => $show_url,        //��Ʒ�����վ
	"seller_email"   => $seller_email     //������䣬����
);
$alipay = new alipay_service($parameter,$security_code,$sign_type);
$link=$alipay->create_url();
echo "<script>window.location =\"$link\";</script>"; 
?>

