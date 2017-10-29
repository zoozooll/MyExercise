<?php

require_once("alipay_notify.php");
require_once("alipay_config.php");
$alipay = new alipay_notify($partner,$security_code,$sign_type,$_input_charset,$transport);
$verify_result = $alipay->notify_verify();
if($verify_result) {   //��֤�ϸ�
 //��ȡ֧�����ķ�!����
    $dingdan   = $_POST['out_trade_no'];   //��ȡ֧�������ݹ�4�Ķ�����
    $total     = $_POST['total_fee'];      //��ȡ֧�������ݹ�4���ܼ۸�

    $receive_name    =$_POST['receive_name'];    //��ȡ�ջ�������
	$receive_address =$_POST['receive_address']; //��ȡ�ջ��˵�ַ
	$receive_zip     =$_POST['receive_zip'];     //��ȡ�ջ����ʱ�
	$receive_phone   =$_POST['receive_phone'];   //��ȡ�ջ��˵绰
	$receive_mobile  =$_POST['receive_mobile'];  //��ȡ�ջ����ֻ�

/*
	��ȡ֧������!��4��״̬,��ݲ�ͬ��״̬4������ݿ� 
	WAIT_BUYER_PAY(��ʾ�ȴ���Ҹ���);
	WAIT_SELLER_SEND_GOODS(��ʾ��Ҹ���ɹ�,�ȴ���ҷ���);
	WAIT_BUYER_CONFIRM_GOODS(��ʾ����Ѿ�����ȴ����ȷ��);
	TRADE_FINISHED(��ʾ�����Ѿ��ɹ�����);
*/
	if($_POST['trade_status'] == 'WAIT_BUYER_PAY') {                   //�ȴ���Ҹ���
        //����������Զ������,�����ݲ�ͬ��trade_status���в�ͬ����
		echo "success";
		//log_result("verify_success");
	}
	else if($_POST['trade_status'] == 'WAIT_SELLER_SEND_GOODS') {      //��Ҹ���ɹ�,�ȴ���ҷ���
        //����������Զ������,�����ݲ�ͬ��trade_status���в�ͬ����
		echo "success";
		//log_result("verify_success");
	}
	else if($_POST['trade_status'] == 'WAIT_BUYER_CONFIRM_GOODS') {    //����Ѿ�����ȴ����ȷ��
        //����������Զ������,�����ݲ�ͬ��trade_status���в�ͬ����
		echo "success";
		//log_result("verify_success");
	}
	else if($_POST['trade_status'] == 'TRADE_FINISHED') {              //���׳ɹ�����
        //����������Զ������,�����ݲ�ͬ��trade_status���в�ͬ����
		echo "success";
		//log_result("verify_success");
	}
	else {
		echo "fail";
		//log_result ("verify_failed");
	}
}
else  {    //��֤���ϸ�
	echo "fail";
	//log_result ("verify_failed");
}

//�����������֧�����Ĺ���?�ܣ����ڷ��ص���Ϣ���治Ҫ������жϣ���������У��ͨ������ֵ���������Ҫ��ȡ�����ʹ�ù����Ľ��,
//���ȡ������Ϣ������ֶ�discount��ֵ��ȡ���ֵ���������ʹ�ù����Ľ��� ԭ�������ܽ��=��Ҹ���صĽ��total_fee +|discount|.	

/*  ��־��Ϣ,��֧������!�Ĳ����¼��4
function  log_result($word) {
	$fp = fopen("log.txt","a");	
	flock($fp, LOCK_EX) ;
	fwrite($fp,$word."��ִ�����ڣ�".strftime("%Y%m%d%H%I%S",time())."\t\n");
	flock($fp, LOCK_UN); 
	fclose($fp);
}
*/	
?>