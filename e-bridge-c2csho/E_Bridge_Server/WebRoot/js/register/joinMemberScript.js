initForm();
init_style();
//document.getElementById("loginid_info").innerHTML.classname= 'noteawoke';
function setBusinessStyle(){
business_info.className = 'notetrue';
}
function tr_display(type){
business_info.className = 'notetrue';
business_info.innerHTML = 'Ϊ�˸����ṩƥ��Ĳ�Ʒ��Ϣ������д��˾��Ӫ�Ĳ�Ʒ������񣩹ؼ��֡�<br>���ж�������ö��ŷָ����磺���ϣ�����';
if(type == 'buy'){
buykeywords.style.display = 'block';
sellkeywords.style.display = 'none';
}
if(type == 'sell'){
buykeywords.style.display = 'none';
sellkeywords.style.display = 'block';
}
if(type == 'both'){
buykeywords.style.display = 'block';
sellkeywords.style.display = 'block';
}
}
function goinitstyle(obj){
//alert(obj.id);
document.getElementById("business_info").className = 'note';
document.getElementById("business_info").innerHTML = 'Ϊ�˸����ṩƥ��Ĳ�Ʒ��Ϣ������д��˾��Ӫ�Ĳ�Ʒ������񣩹ؼ��֡�<br>���ж�������ö��ŷָ����磺���ϣ�����';
var pass = true;
var keyselected ='';
for(i=0;i<document.form.business_role.length;i++){
if(document.form.business_role[i].checked){
keyselected = document.form.business_role[i].value;
break;
}
}
if(keyselected == 'buyer'){
if(document.getElementById("buykeyword").value != ''){
if(document.getElementById("keywords_info_check").innerHTML.indexOf("img") < 0 ){
document.getElementById("keywords_info_check").innerHTML = "&nbsp;<img src=\""+IMG_SERVER_URL+"/images/cn/member/icon_right_19x19.gif\" width=\"19\" height=\"16\" align=\"absmiddle\"> " + document.getElementById("keywords_info_check").innerHTML;
}
document.getElementById("business_info").className = 'note';
document.getElementById("business_info").innerHTML = '��д��ȷ��'
}else{
if(document.getElementById("keywords_info_check").innerHTML.indexOf("img") > 0 ){
var start =  document.getElementById("keywords_info_check").innerHTML.indexOf('>');
var end = document.getElementById("keywords_info_check").innerHTML.length;
document.getElementById("keywords_info_check").innerHTML = document.getElementById("keywords_info_check").innerHTML.substring(start +1,end);
}
}
}else if(keyselected == 'seller'){
if(document.getElementById("salekeyword").value != ''){
if(document.getElementById("keywords_info_check").innerHTML.indexOf("img") < 0 ){
document.getElementById("keywords_info_check").innerHTML = "&nbsp;<img src=\""+IMG_SERVER_URL+"/images/cn/member/icon_right_19x19.gif\" width=\"19\" height=\"16\" align=\"absmiddle\"> " + document.getElementById("keywords_info_check").innerHTML;
}
document.getElementById("business_info").className = 'note';
document.getElementById("business_info").innerHTML = '��д��ȷ��'
}else{
if(document.getElementById("keywords_info_check").innerHTML.indexOf("img") > 0 ){
var start =  document.getElementById("keywords_info_check").innerHTML.indexOf('>');
var end = document.getElementById("keywords_info_check").innerHTML.length;
document.getElementById("keywords_info_check").innerHTML = document.getElementById("keywords_info_check").innerHTML.substring(start +1,end);
}
}
}else if(keyselected == 'both'){
if(document.getElementById("salekeyword").value != '' && document.getElementById("buykeyword").value != ''){
if(document.getElementById("keywords_info_check").innerHTML.indexOf("img") < 0 ){
document.getElementById("keywords_info_check").innerHTML = "&nbsp;<img src=\""+IMG_SERVER_URL+"/images/cn/member/icon_right_19x19.gif\" width=\"19\" height=\"16\" align=\"absmiddle\"> " + document.getElementById("keywords_info_check").innerHTML;
}
document.getElementById("business_info").className = 'note';
document.getElementById("business_info").innerHTML = '��д��ȷ��'
}else{
if(document.getElementById("keywords_info_check").innerHTML.indexOf("img") > 0 ){
var start =  document.getElementById("keywords_info_check").innerHTML.indexOf('>');
var end = document.getElementById("keywords_info_check").innerHTML.length;
document.getElementById("keywords_info_check").innerHTML = document.getElementById("keywords_info_check").innerHTML.substring(start +1,end);
}
}
}
}
//��ʼ����Ӫ�������ʾ����Ҫ��Ϊ������֤�������ύʧ�ܵ�����³�ʼ��
function init_style(){
var keyselected ='';
for(i=0;i<document.form.business_role.length;i++){
if(document.form.business_role[i].checked){
keyselected = document.form.business_role[i].value;
break;
}
}
if(keyselected == 'buyer'){
buykeywords.style.display = 'block';
sellkeywords.style.display = 'none';
}
if(keyselected == 'seller'){
buykeywords.style.display = 'none';
sellkeywords.style.display = 'block';
}
if(keyselected == 'both'){
buykeywords.style.display = 'block';
sellkeywords.style.display = 'block';
}
}
function info_check(obj){
if(document.getElementById(obj).innerHTML.indexOf("img") < 0 ){
if(obj == 'title_info_check'){
var titlecheck = false;
var titleObjs=document.getElementById("memberTitle").getElementsByTagName("input");
for(var i=0;i<titleObjs.length;i++){
if(titleObjs[i].checked){
titlecheck=true;
break;
}
}
if(titlecheck){
document.getElementById(obj).innerHTML = "&nbsp;<img src=\""+IMG_SERVER_URL+"/images/cn/member/icon_right_19x19.gif\" width=\"19\" height=\"16\" align=\"absmiddle\"> " + document.getElementById(obj).innerHTML;
}
}
else{
document.getElementById(obj).innerHTML = "&nbsp;<img src=\""+IMG_SERVER_URL+"/images/cn/member/icon_right_19x19.gif\" width=\"19\" height=\"16\" align=\"absmiddle\"> " + document.getElementById(obj).innerHTML;
}
}
}
function warning_check(obj,message){
document.getElementById(obj).innerHTML = message;
document.getElementById(obj).className= 'noteawoke';
}
function clean_check(obj){
document.getElementById(obj).innerHTML = "";
document.getElementById(obj).className= 'note';
}
/********************************** chinese ***************************************/
/**
*У���ַ����Ƿ�Ϊ����
*����ֵ��
*���Ϊ�գ�����У��ͨ����           ����true
*����ִ�Ϊ���ģ�У��ͨ����         ����true
*����ִ�Ϊ�����ģ�             ����false    �ο���ʾ��Ϣ������Ϊ���ģ�
*/
function checkIsChinese(str)
{
//���ֵΪ�գ�ͨ��У��
if (str == "")
return true;
var pattern = /^([\u4E00-\u9FA5]|[\uFE30-\uFFA0])*$/gi;
if (pattern.test(str))
return true;
else
return false;
}//~~~
