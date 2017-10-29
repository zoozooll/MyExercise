<?php


$partner        = "";       //������ID
$security_code  = "";       //��ȫ������
$seller_email   = "";       //���֧�����ʻ�
$_input_charset = "GBK";  //�ַ�����ʽ  Ŀǰ֧�� GBK �� utf-8
$sign_type      = "MD5";    //���ܷ�ʽ  ϵͳĬ��(��Ҫ�޸�)
$transport      = "https";  //����ģʽ,����Ը���Լ��ķ������Ƿ�֧��ssl���ʶ�ѡ��http�Լ�https����ģʽ(ϵͳĬ��,��Ҫ�޸�)
$notify_url     = "http://localhost/php/notify_url.php"; //���׹���з�����֪ͨ��ҳ�� Ҫ�� http://��ʽ������·��
$return_url     = "http://localhost/php/return_url.php"; //��������ת��ҳ�� Ҫ�� http://��ʽ������·��
$show_url       = ""        //����վ��Ʒ��չʾ��ַ

/** ��ʾ����λ�ȡ��ȫУ����ͺ���ID
1.���� www.alipay.com��Ȼ���½����ʻ�($seller_email).
2.���̼ҷ���.����8��������Կ���
*/
?>