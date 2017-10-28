/////////////////////////////////////////////////////////////
//                Setup Class Names
/////////////////////////////////////////////////////////////
//default classes for infobox
var infoboxOkClass		= "notetrue";
var infoboxWarningClass	= "notetrue";
var infoboxErrorClass	= "noteawoke";
var infoboxHintClass	= "note";
//default classes for input field
var inputWarningClass	= "note";
var inputErrorClass		= "noteawoke";
var inputOkClass		= "notetrue";
var inputNormalClass	= "note";
/////////////////////////////////////////////////////////////
//                Initialize Form
/////////////////////////////////////////////////////////////
document.onkeydown=function(evnt){
if(isIE()&&window.event.keyCode==13){
$("Submit").focus();
}
}
function initForm(){
//initialize form UI and add triggers
var infobox;
var x = document.getElementById("register");
if(!x) return;
var y = x.getElementsByTagName("input");
for (var i=0;i<y.length;i++){
if((y[i].type == 'text' || y[i].type == 'password')){
initStatus(y[i],true);
setFiledWidth(y[i]);
y[i].onfocus	= getFocus;
y[i].onblur		= lostFocus;
//y[i].onkeyup	= showMyStatus;
}
}
}
function initStatus(obj,isInput){
if(isInput){
if(isRequired(obj)) showStatus(obj,"Warning");
else showStatus(obj,"Normal");
}
var infobox = getInfobox(obj);
var errorCode = getInitStatus(obj);
if(infobox){
if(!errorCode || errorCode == 0){
infobox.className	= infoboxHintClass;
infobox.innerHTML	= getErrorMsg(obj,0);
//alert(infobox.innerHTML);
}
if(errorCode >0){
infobox.className	= infoboxErrorClass;
infobox.innerHTML	= getErrorMsg(obj,errorCode);
}
}
}
/////////////////////////////////////////////////////////////
//                Base Functions
/////////////////////////////////////////////////////////////
function isIE() {
if(document.all) return true;
return false;
}
function setFiledWidth(obj){
obj.style.width=(19/3)*obj.size+11;
}
function formEle(required,datatype,parameter,infobox,errormsg,combine,status){
this.r	= required;
this.d	= datatype;
this.p	= parameter;
this.i	= infobox;
this.e	= errormsg;
this.c = combine;
this.s = status;
}
function isRequired(obj){
//alert((obj.id).r);
if(obj.id){
if(eval(obj.id).r) return eval(obj.id).r;
}
return false;
}
function isCombine(obj){
if(obj.id){
if(eval(obj.id).c) return eval(obj.id).c;
}
return false;
}
function getDatatype(obj){
if(obj.id){
if(eval(obj.id).d) return eval(obj.id).d;
}
return false;
}
function getInfobox(obj){
//alert(obj.id);
if(obj.id){
if(eval(obj.id).i && document.getElementById(eval(obj.id).i)) return document.getElementById(eval(obj.id).i);
}
return;
}
function getErrorMsg(obj,errorCode){
if(obj.id){
if(eval(obj.id).e[errorCode]) return eval(obj.id).e[errorCode];
}
return;
}
function getHintMsg(obj){
if(obj.id){
if(eval(obj.id).e[0]) return eval(obj.id).e[0];
}
return;
}
function getInitStatus(obj){
if(obj.id){
if(eval(obj.id).s || eval(obj.id).s==0 ) return eval(obj.id).s;
}
return;
}
function getAttrName(str){
var s=str.split("=");
return s[0];
}
function getAttrValue(str){
var s=str.split("=");
return s[1];
}
function getAttrValueByName(obj,str){
var para;
if(obj.id){
if(eval(obj.id).p) para=eval(obj.id).p;
else return;
}else{
return;
}
var s = para.split(",");
for(var i=0;i<s.length;i++){
if(getAttrName(s[i]) == str){
if(getAttrValue(s[i]))
return getAttrValue(s[i]);
else
return;
}
}
return;
}
function getMailServer(str){
//be sure str is a correct email address
str = str.trim();
return str.substr(str.indexOf("@")+1);
}
String.prototype.trim = function()
{
return this.replace(/(^\s*)|(\s*$)/g, "");
}
/////////////////////////////////////////////////////////////
//                UI Functions
/////////////////////////////////////////////////////////////
function getFocus(evnt)
{
var obj;
if (isIE()) {
obj = event.srcElement;
}else {
obj = evnt.target;
}
showInfo(obj,0);
}
function lostFocus(evnt)
{
var obj;
if (isIE()) {
obj = event.srcElement;
}else {
obj = evnt.target;
}
showInfo(obj,-1);
if(obj.value == ''){
removeDraw(obj);
//������Ϊ��ʱ��ȷ������ҲΪ��
if(obj.id && eval(obj.id).c && document.getElementById(eval(obj.id).c)){
var infobox = getInfobox(obj);
var errorCode = getInitStatus(obj);
if(infobox){
if(infobox.className == infoboxErrorClass){
infobox.className	= "note";
infobox.innerHTML	= getErrorMsg(obj,0);
}
}
}
return;
}
errorCode = validateValue(obj);
if(errorCode != 0){
//��֤�������ͼ����Ϣ
if(obj.id == 'phone_country' || obj.id == 'phone_area' || obj.id == 'phone_number' ||
obj.id == 'fax_country' || obj.id == 'fax_area' || obj.id == 'fax_number' || obj.id == 'salekeyword' || obj.id == 'buykeyword' ){
if(errorCode == 1){
sendFieldclick(obj);
}
}else{
sendFieldclick(obj);
}
}
//alert(errorCode);
if(errorCode == 0){
//loginid �� email  ��������֤
if(obj.id == 'loginid'){
checkUserName(obj);
return;
}
if(obj.id == 'email'){
checkEmail(obj);
return;
}
if(obj.id == 'password'){
initStatus(document.getElementById('confirm_password'),true);
document.getElementById('confirm_password').value="";
removeDraw(document.getElementById('confirm_password'));
removeDraw(obj);
}
if(obj.id){
addDraw(obj);
document.getElementById(eval(obj.id).i).className = 'noteok';
//        		document.getElementById(eval(obj.id).i).innerHTML = (eval(obj.id).e)[3];
document.getElementById(eval(obj.id).i).innerHTML = '��д��ȷ��'
}
}
if(errorCode >= 1){
if(obj.id){
//alert(eval(obj.id).i);
if(eval(obj.id).i && document.getElementById(eval(obj.id).i))
document.getElementById(eval(obj.id).i).className = 'noteawoke';
document.getElementById(eval(
obj.id).i).innerHTML = (eval(obj.id).e)[errorCode];
}
removeDraw(obj);
}
if(errorCode < 0 && eval(obj.id)){
removeDraw(obj);
if(obj.id){
//alert(eval(obj.id).i);
if(eval(obj.id).i && document.getElementById(eval(obj.id).i) )
document.getElementById(eval(obj.id).i).className = 'note';
document.getElementById(eval(obj.id).i).innerHTML = (eval(obj.id).e)[0];
//document.getElementById(eval(obj.id).i).innerHTML = '&nbsp;';
}
//showStatus(obj,"Normal");
}
}
function showInfo(obj,errorCode,forcible)
//Show Information in Infobox
//errorCode==0 for hint message
//if forcible==true, no matter what the status of infobox now, change it,
//otherwise,if the status of infobox is "Error", do not change it forever.
{
var infobox = getInfobox(obj);
if(infobox){
if(infobox.className != infoboxErrorClass || forcible){
if(errorCode == 0 ){
//alert(infobox.innerHTML);
infobox.innerHTML	= getErrorMsg(obj,errorCode);
if(infobox.innerHTML != '&nbsp;'){
infobox.className	= infoboxWarningClass;
}
}
if(errorCode >0){
infobox.className	= infoboxErrorClass;
infobox.innerHTML	= getErrorMsg(obj,errorCode);
}
if(errorCode <0){
infobox.className	= infoboxHintClass;
}
}
}
}
function showMyStatus(evnt){
var obj,errorCode;
if (isIE()) {
obj = event.srcElement;
}else {
obj = evnt.target;
}
errorCode = validateValue(obj);
if(errorCode == 0){
showStatus(obj,"Ok");
}
if(errorCode >= 1){
showStatus(obj,"Error");
}
if(errorCode < 0){
showStatus(obj,"Normal");
}
}
function showStatus(obj,stat)
//Show the status of user currently inputting field
//3 Statuses: Warning|Error|Ok
{
switch(stat){
case "Warning":
obj.className = inputWarningClass;
break;
case "Error":
obj.className = inputErrorClass;
break;
case "Ok":
obj.className = inputOkClass;
break;
default:
obj.className = inputNormalClass;
break;
}
}
/////////////////////////////////////////////////////////////
//                Validator Functions
/////////////////////////////////////////////////////////////
function changeIdToLowerCase(obj){
var tmp=obj.value.toLowerCase();
if(obj.value!=tmp){
obj.value=tmp;
}
}
function validateValue(obj){
//trim
var patn = /(^\s)|(\s$)/;
if(patn.test(obj.value))	obj.value = obj.value.trim();
//switcher
var errorCode = -1;
switch(getDatatype(obj)){
case "loginid":
//alert(obj);
changeIdToLowerCase(obj);
errorCode = validateUsername(obj);
break;
case "password":
errorCode = validatePassword(obj);
break;
case "confirm_password":
errorCode = validateSafePassword(obj);
break;
case "email":
errorCode = validateEmail(obj);
break;
case "company":
errorCode = validateCompany(obj);
break;
case "mobile":
errorCode = validateMobile(obj);
break;
case "address":
errorCode = validateAddress(obj);
break;
case "first_name":
errorCode = validateFirstName(obj);
break;
case "job_title":
errorCode = validateJobTitle(obj);
break;
case "buykeyword":
errorCode = validateKeyword(obj);
break;
case "salekeyword":
errorCode = validateKeyword(obj);
break;
case "phone_country":
errorCode = validatePhoneArea(obj);
break;
case "phone_area":
errorCode = validatePhoneArea(obj);
break;
case "phone_number":
errorCode = validatePhoneNumber(obj);
break;
case "fax_country":
errorCode = validateFaxArea(obj);
break;
case "fax_area":
errorCode = validateFaxArea(obj);
break;
case "fax_number":
errorCode = validateFaxNumber(obj);
break;
default:
errorCode = -1;
break;
}
return errorCode;
}
var hiddenList = new Array();
function hiddenContent(obj){
var nObj = obj.parentNode;
for(var i=0;i<6;i++){
if(nObj.className=="content"){
nObj.style.display="none";
return;
}else{
nObj=nObj.parentNode;
}
}
}
function validateAll(formObj){
var obj,infobox,pass;
pass = true;
var x = formObj;
if(!x) return;
var y = x.getElementsByTagName("input");
for (var i=0;i<y.length;i++){
if(y[i].type != 'hidden' && y[i].id !='isAddCompany' && y[i].id !='isAddInfo'){
obj = y[i];
obj.value = obj.value.trim();
infobox = getInfobox(y[i]);
if(obj.type == 'text' || obj.type == 'password'){
if(!isRequired(obj) && obj.value == ""){
if((obj.id=="fax_number"||obj.id=="mobile")&&validatePhoneArea(obj)=="-1"){hiddenList[hiddenList.length]=obj;}
continue;
}
if(isRequired(obj) && obj.value == ""){
pass = false;
obj.parentNode.focus();//�ύ����ʱ��λ
showStatus(obj,"Error");
infobox.className	= infoboxErrorClass;
infobox.innerHTML	= "<h1>"+requireErrorInfo + getErrorMsg(obj,0) + "<\/h1>";
removeDraw(obj);
//if(isCombine(obj)) break;
continue;
}
if(validateValue(obj)>0){
pass = false;
obj.parentNode.focus();//�ύ����ʱ��λ
showStatus(obj,"Error");
showInfo(obj,validateValue(obj),true);
removeDraw(obj);
//if(isCombine(obj)) break;
continue;
}
if(obj.id == 'password'){
if(validatePasswordSafe() > 0 && validateValue(obj)== 0){
pass = false;
removeDraw(obj);
showInfo(obj,2,true);
document.getElementById(eval(obj.id).i).focus();
continue;
}
}
if(validateValue(obj)==0){
//showStatus(obj,"Ok");
//infobox.className	= infoboxHintClass;
//infobox.innerHTML	= validatedInfo;
//alert(obj.outerHTML);
if(obj.id != 'confirm_password'&&obj.id != 'password'){
hiddenList[hiddenList.length]=obj;
}
continue;
}
}
}
}//for
//�ж��Ա���ûѡ
var titlecheck = true;
/*var titleObjs=document.getElementById("memberTitle").getElementsByTagName("input");
for(var i=0;i<titleObjs.length;i++){
if(titleObjs[i].checked){
titlecheck=true;
break;
}
}
if(!titlecheck){
pass = false;
warning_check("title_info",'<h1>��ѡ���Ա�<\/h1>');
document.getElementById("title_info").focus();
}else{
clean_check("title_info");
hiddenList[hiddenList.length]=titleObjs[0];
}*/
//�жϹؼ�����û��ȷ��д
var keyselected ='';
for(i=0;i<document.form.business_role.length;i++){
if(document.form.business_role[i].checked){
keyselected = document.form.business_role[i].value;
break;
}
}
if(keyselected == 'buyer'){
if(document.getElementById("buykeyword").value == ''){
warning_check("business_info",'<h1>����д�ؼ���<\/h1>');
document.getElementById("business_info").focus();
pass = false;
}
}else if(keyselected == 'seller'){
if(document.getElementById("salekeyword").value == ''){
warning_check("business_info",'<h1>����д�ؼ���<\/h1>');
document.getElementById("business_info").focus();
pass = false;
}
}else if(keyselected == 'both'){
if(document.getElementById("buykeyword").value == '' || document.getElementById("salekeyword").value == ''){
warning_check("business_info",'<h1>����д�ؼ���<\/h1>');
document.getElementById("business_info").focus();
pass = false;
}
}
//�жϵ���ѡ����û��ȷ
if(document.getElementById("city").value == '' ){
warning_check("city_info",'<h1>��ѡ��ʡ�ݻ��߳���<\/h1>');
document.getElementById("city_info").focus();
pass = false;
}else{
clean_check("city_info");
hiddenContent(document.getElementById("city"));
}
if(document.getElementById("category").value == '' ){
warning_check("category_info",'<h1>��ѡ����Ӫ��ҵ<\/h1>');
document.getElementById("category_info").focus();
pass = false;
}else{
clean_check("category_info");
hiddenList[hiddenList.length]=document.getElementById("category");
}
if( document.getElementById("loginid_info").className == 'noteawoke'){
document.getElementById("loginid_info").focus();
pass= false;
}
if( document.getElementById("email_info").className == 'noteawoke'){
document.getElementById("email_info").focus();
//checkEmail();
pass= false;
}
//��˾����Ĭ���Ѿ�ѡ��
/*start �������δͨ��,��������ȷ��,ֻ��ʾ������   modify by ym 2008-1-20*/
if(!pass){
for(var i=0;i<hiddenList.length;i++){
hiddenContent(hiddenList[i]);
}
hiddenContent(document.getElementById("company_type"));
}//end
if(!pass){
sendAllFieldclick();
}
return pass;
}
//functions for each particular datatype validation
function validateUsername(obj){
var str = obj.value;
var patn =   /^[a-zA-Z]+[a-zA-Z0-9]+$/;
//var patn = /^[^\s]*$/;
if(!checkByteLength(str,4,20)) return 1;
if(!patn.test(str)){
return 1;
}
return 0;
}
function checkUserName(obj){
document.getElementById(eval(obj.id).i).innerHTML = "����У����Ե�...";
document.getElementById(eval(obj.id).i).className = "notetrue";
var url = ALIBABA_SERVER_URL+"/member/check_login_id_tmp.htm";
var r = new AjaxRequest("post",url,false);
r.setParameter("TPL_NICK",obj.value);
r.send(null);
r.onresult = function(){
showInfoHaveUsed(obj,r.getText());
}
}
function showInfoHaveUsed(obj,right){
if(right=="1"){
if(obj.id){
sendFieldclick(obj);
if(eval(obj.id).i && document.getElementById(eval(obj.id).i))
document.getElementById(eval(obj.id).i).className = 'noteawoke';
document.getElementById(eval(obj.id).i).innerHTML = (eval(obj.id).e)[2];
}
removeDraw(obj);
}else{
addDraw(obj);
document.getElementById(eval(obj.id).i).className = 'noteok';
document.getElementById(eval(obj.id).i).innerHTML = (eval(obj.id).e)[3];
}
}
//ȥ���򹴵�
function removeDraw(obj){
if(obj.id && eval(obj.id).c && document.getElementById(eval(obj.id).c)){
if(document.getElementById(eval(obj.id).c).innerHTML.indexOf("img") > 0 ){
var start =  document.getElementById(eval(obj.id).c).innerHTML.indexOf('>');
var end = document.getElementById(eval(obj.id).c).innerHTML.length;
document.getElementById(eval(obj.id).c).innerHTML = document.getElementById(eval(obj.id).c).innerHTML.substring(start +1,end);
}
}
}
//���Ϲ�
function addDraw(obj){
if(eval(obj.id).c && document.getElementById(eval(obj.id).c)){
if(document.getElementById(eval(obj.id).c).innerHTML.indexOf("img") < 0 ){
document.getElementById(eval(obj.id).c).innerHTML = "&nbsp;<img src=\""+IMG_SERVER_URL+"/images/cn/member/icon_right_19x19.gif\" width=\"19\" height=\"16\" align=\"absmiddle\"> " + document.getElementById(eval(obj.id).c).innerHTML;
}
}
}
function validatePassword(obj){
var str = obj.value;
if(!checkByteLength(str,6,20)) return 1;
var patn1 =   /^[a-zA-Z0-9]+$/;
if(!patn1.test(str) ) return 1;
return validatePasswordSafe() ? 2 : 0;
return 0;
}
function validatePasswordSafe(){
var password= document.getElementById("password").value;
var passwordLowcase = password.toLowerCase();
var loginid= document.getElementById("loginid").value;
if(loginid!=''){
var loginidLowcase=loginid.toLowerCase();
if(passwordLowcase.indexOf(loginidLowcase)!=-1||loginidLowcase.indexOf(passwordLowcase)!=-1){
return 1;
}
}
if(passwordLowcase.indexOf('password')!=-1){
return 1;
}
var firstName = document.getElementById("first_name").value;
if(firstName!=''&&passwordLowcase.indexOf(firstName.toLowerCase())!=-1){
return 1;
}
if(isNumberContinue(password) == 1){
return 1;
}
if(isSameLetter(password) == 1){
return 1;
}
var patn =   /[a-zA-Z0-9]+$/;
if(!patn.test(password)){
return 1;
}
var email = document.getElementById("email").value;
var emailIndexer = email.indexOf('@');
if(emailIndexer!=-1){
var emailHeader = email.substring(0,emailIndexer).toLowerCase();
if(passwordLowcase==emailHeader){
return 1;
}
}else{
//return 1;
}
if(isEqual("password","phone_number") == 1){
return 1;
}
if(isEqual("password","mobile") == 1){
return 1;
}
if(isEqual("password","fax_number") == 1){
return 1;
}
return 0;
}
function isNumberContinue(str){
var patn1 =   /^[0-9_]+$/;
var ascendNumber=0;
var descendNumber=0;
for (var i = 1; i < str.length; i++) {
if (str.charAt(i).charCodeAt() != (str.charAt(i-1).charCodeAt() + 1)) {
ascendNumber = 1;
break;
}
}
for (i = 0; i < (str.length - 1); i++) {
if (str.charAt(i).charCodeAt() != (str.charAt(i+1).charCodeAt() + 1)) {
descendNumber = 1;
break;
}
}
if(descendNumber == 0 || ascendNumber == 0){
return 1;
}else{
return 0;
}
}
function isSameLetter(str){
var sameNumberFlag = 1;
var patn1 =   /^[0-9]+$/;
if(patn1.test(str) ){
for (var i = 0; i < str.length; i++) {
if (str.charAt(0) != str.charAt(i)) {
sameNumberFlag = 0;
break;
}
}
} else {
for (var i = 0; i < str.length; i++) {
if (str.charAt(0) != str.charAt(i)) {
sameNumberFlag = 0;
break;
}
}
}
return sameNumberFlag;
}
function isEqual(objid1,objid2){
if(document.getElementById(objid1).value == document.getElementById(objid2).value){
return 1;
}else{
return 0;
}
}
function validateSafePassword(obj){
var str = obj.value;
if(str != document.getElementById("password").value) return 1;
return 0;
}
function validateEmail(obj){
var str = obj.value;
if(!checkByteLength(str,1,50)) return 1;
var patn = /^[_a-zA-Z0-9\-]+(\.[_a-zA-Z0-9\-]*)*@[a-zA-Z0-9\-]+([\.][a-zA-Z0-9\-]+)+$/;
if(!patn.test(str)){
return 1;//incorrect format
}
return 0;
}
function checkEmail(obj){
document.getElementById(eval(obj.id).i).innerHTML = "����У����Ե�...";
document.getElementById(eval(obj.id).i).className = "notetrue";
var url = ALIBABA_SERVER_URL+"/member/check_email_tmp.htm"
var r = new AjaxRequest("post",url,false);
r.setParameter("TPL_EMAIL",obj.value);
r.send(null);
r.onresult = function(){
showInfoEmailHaveUsed(obj,r.getText());
}
}
function showInfoEmailHaveUsed(obj,text){
var str = text.trim().split(";");
if(str[0]=="0"){
addDraw(obj);
document.getElementById(eval(obj.id).i).className = "noteok";
document.getElementById(eval(obj.id).i).innerHTML = "�����ʽ��ȷ�����ǿͻ�������ϵ����ѡ��ʽ����ȷ����д��ȷ��";
}else{
sendFieldclick(obj);
var existEmailMemberId = str[1];
var emailHaveUsed=obj.value;
var url=ALIBABA_SERVER_URL+"/member/cancelEmailValidation.htm";
url=url+"?email="+emailHaveUsed+"&loginId="+existEmailMemberId;
document.getElementById(eval(obj.id).i).className = "noteawoke";
document.getElementById(eval(obj.id).i).innerHTML ="<h1>����д�����䣬�Ѿ�����<font color=\"FF7300\">"+existEmailMemberId+"<\/font>�û������á�<br>1�����������Ƿ���ȷ����ʹ���������䣻<br>2�����������<font color=\"FF7300\">"+existEmailMemberId+"<\/font>�û�������ʹ��������䲢�����»�Ա��¼����<a href=\""+url+"\" target=\"_blank\">���˽���<\/a><\/h1>";
removeDraw(obj);
}
}
function validateNum(obj){
var str = obj.value;
var patn = new RegExp("\\d{"+getAttrValueByName(obj,"minlen")+","+getAttrValueByName(obj,"maxlen")+"}");
if(patn.test(str)) return 0;
return 1;
}
function validateMobile(obj){
var str1 = obj.value;
var str = tot(str1);
obj.value = str;
if(str.length > 16){
return 1;
}
if(str.length == 0){
return -1;
}
var patn = /^[0-9]+$/;
if(patn.test(str)) return 0;
return 2;
}
function validatePhoneArea(obj){
var str1 = obj.value;
var str = tot(str1);
obj.value = str;
if(str.length == 0){
return -1;
}
var patn = /^[0-9]+$/;
if(!patn.test(str)) return 1;
return validatePhone();
}
function validateFaxArea(obj){
var str1 = obj.value;
var str = tot(str1);
obj.value = str;
if(str.length == 0){
return -1;
return
}
var patn = /^[0-9]+$/;
if(!patn.test(str)) return 1;
return validateFax();
}
function validateArea(obj){
var str1 = obj.value;
var str = tot(str1);
obj.value = str;
if(str.length == 0){
return -1;
return
}
var patn = /^[0-9]+$/;
if(!patn.test(str)) return 1;
return 0;
}
function validatePhone(){
if(validateArea(document.getElementById("phone_country")) == 0 && validateArea(document.getElementById("phone_area")) == 0 && validateNumber(document.getElementById("phone_number")) == 0){
return 0;
}else{
return -1
}
}
function validateFax(){
if(validateArea(document.getElementById("fax_country")) == 0 && validateArea(document.getElementById("fax_area")) == 0 && validateNumber(document.getElementById("fax_number")) == 0){
return 0;
}else{
return -1
}
}
function validateNumber(obj){
var str1 = obj.value;
var str = tot(str1);
obj.value = str;
if(str.length == 0){
return -1;
}
var patn = /^[0-9-\/]+$/;
if(!patn.test(str)) return 1;
return 0;
}
function validatePhoneNumber(obj){
var str1 = obj.value;
var str = tot(str1);
obj.value = str;
if(str.length == 0){
return -1;
}
var patn = /^[0-9-\/]+$/;
if(!patn.test(str)) return 1;
//����С��7
if(str.length<7) return 2;
if(str == Array(str.length+1).join(((str||'').substring(0,1)||''))){return 3}
return validatePhone();
}
function validateFaxNumber(obj){
var str1 = obj.value;
var str = tot(str1);
obj.value = str;
if(str.length == 0){
return -1;
}
var patn = /^[0-9-\/]+$/;
if(!patn.test(str)) return 1;
//����С��6
if(str.length<6) return 2;
if(str == Array(str.length+1).join(((str||'').substring(0,1)||''))){return 3}
return validateFax();
}
function validateKeyword(obj){
var str = obj.value;
if(str.length > 40){
return 1;
}
if(str.length == 0){
return -1;
}
if(checkDenyWords(str) != ""){
return 1;
}
var pass = true;
var keyselected ='';
for(i=0;i<document.form.business_role.length;i++){
if(document.form.business_role[i].checked){
keyselected = document.form.business_role[i].value;
break;
}
}
if(keyselected == 'buyer'){
if(document.getElementById("buykeyword").value == ''){
pass = false;
}
}else if(keyselected == 'seller'){
if(document.getElementById("salekeyword").value == ''){
pass = false;
}
}else if(keyselected == 'both'){
if(document.getElementById("salekeyword").value == '' || document.getElementById("buykeyword").value == ''){
pass = false;
}
}
if(pass){
return 0;
}
return -1;
}
function validateCompany(obj){
var str = obj.value;
if(str.length > 50){
return 1;
}
if(str.length == 0){
return -1;
}
if(checkDenyWords(str) != ""){
return 2;
}
//��˾��������������ַ�
for(var i=0,a='@#$%'.split(''),len=a.length;i<len;i++){
if(str.indexOf(a[i])>-1) return 3;
}
if(str && /^\d+$/.test(str))return 4;
return 0;
}
function validateJobTitle(obj){
var str = obj.value;
if(str.length > 16){
return 1;
}
if(str.length == 0){
return -1;
}
if(checkDenyWords(str) != ""){
return 2;
}
return 0;
}
function validateFirstName(obj){
var str = obj.value;
if(str.length > 32){
return 1;
}
if(str.length == 0){
return -1;
}
if(checkDenyWords(str) != ""){
return 2;
}
//�����ﺬ�������ַ�
for(var i=0,a='@#$%'.split(''),len=a.length;i<len;i++){
if(str.indexOf(a[i])>-1) return 3;
}
return 0;
}
function validateAddress(obj){
var str = obj.value;
if(str.length > 80){
return 1;
}
if(str.length == 0){
return -1;
}
return 0;
}
function validateCheckCode(obj){
var str = obj.value;
var patn = /^[0-9a-zA-Z]{4}$/;
if(patn.test(str)) return 0;
return 1;
}
function checkByteLength(str,minlen,maxlen) {
if (str == null) return false;
var l = str.length;
var blen = 0;
for(i=0; i<l; i++) {
if ((str.charCodeAt(i) & 0xff00) != 0) {
blen ++;
}
blen ++;
}
if (blen > maxlen || blen < minlen) {
return false;
}
return true;
}
function tot(mobnumber){
while(mobnumber.indexOf("��")!=-1){
mobnumber = mobnumber.replace("��","0");
}
while(mobnumber.indexOf("��")!=-1){
mobnumber = mobnumber.replace("��","1");}
while(mobnumber.indexOf("��")!=-1){
mobnumber = mobnumber.replace("��","2");}
while(mobnumber.indexOf("��")!=-1){
mobnumber = mobnumber.replace("��","3");}
while(mobnumber.indexOf("��")!=-1){
mobnumber = mobnumber.replace("��","4");}
while(mobnumber.indexOf("��")!=-1){
mobnumber = mobnumber.replace("��","5");}
while(mobnumber.indexOf("��")!=-1){
mobnumber = mobnumber.replace("��","6");}
while(mobnumber.indexOf("��")!=-1){
mobnumber = mobnumber.replace("��","7");}
while(mobnumber.indexOf("��")!=-1){
mobnumber = mobnumber.replace("��","8");}
while(mobnumber.indexOf("��")!=-1){
mobnumber = mobnumber.replace("��","9");}
return mobnumber;
}
//������д�
function checkDenyWords(content) {
if (content == null || content.trim() == "") return "";
var wordsArrays = new Array();
content=content.toLowerCase();
wordsArrays = new Array("����������","��������","��������","5322","ע��","��ַ","��վ","www.EC21.com","����ͨ","0791 6690161","0791 6690253","0791 6690310","0791 6690356","0791 8885159","0791-6690310","0791-6690356","100data.com","2288.35007.net","28897737","3536.net","51sobu.com","5322.com","5322.net","5332.com","5822.com","5877.com","5iok.com","6640620","6640630","680.com.cn","71008.com","8848","8gem.com","9595.cn","Ecplaza.com","Ecplaza.net","Eվͨ","Post2Ali","TraCQ����������","alloy.com.cn","aweb.com.cn","booye.com","btob.com","btob.net","buffet.com.cn","bxcn.com","cappma.com","cartelcn.com","cashfiesta","cg160.com","cgy.cn","changpian.com","chinabamboonet.com","chinabidding.com.cn","chinaesteel.com.cn","chinamj.com.cn","chinaour","chinapharm.com.cn","chinascrap.com","clean-info.com","cn5000.com","cn61.net","cnbt.com.cn","cnfee.com","cnflw.com","cnfurnace.net","cnli.net","cntra.com","cpbbs.net","da001.com","dj800","e-marketing.net.cn","ec51.com","eck.com.cn","elibaba.net","fa lun","feitianlight.com","foodqs.com","gbele.com","gkw.com.cn","gs114.cn","gz001.com","hardwareol.net","hblbet.com","hc360.com","hcgroup.com","hotexport.com","huiduo.net.cn","imageengine.com.cn","jctis.com","jdztaoci.com","jxbx.com","minghui","mymai.com","nc365.com","nc365.net","nic2000","nyto.cn","okws.com","packbuy.com","pcsohu.com","pointsmoney","qm365.com","qyfw.com","ra36.com","res168.net","script","sie.cn","tearen.com","tongzhuang.net","tpage.com","tpage.net.cn","tradenet.cn","uuxx.net","video.com.cn","wltools.net","www.123trading.com","www.3536","www.51wj","www.5322","www.5322.com","www.Ecplaza","www.Globalsources.com","www.TradeEasy.com","www.btob","www.bx727.com","www.cartelcn","www.ce.net.cn","www.chinaccm.com","www.cn818","www.easyeb.com","www.hardwaretoday","www.made-in-china.com","www.mmmn.net","www.mysc.cn","www.nc365","www.sinobnet.com","www.sparkice.com.cn","www.tracq.net","www.wto1.net","xichi.net","yicou.com","yjtx.com","yujie.cn","zapbao.com","zhibei.com","zhun.net","zj555.com","zzlm.com","��������.com","��������������","��������.com","����������com","��������.com","���²ɹ���","�չ�","����������","��װ�ɹ���","��ʯ��","��Ϫ727","������","�ɹ���վ","��������","������","��������","����������","������","����","��","�󷨵���","����","������Ʊ","��¯������","��ͻ","����","����","����","���ֹ�","����","����","����","����","����","����","���ִ�","���ֹ�","����","����","����","����","������","����","�����й�","������","����","���Ʒ������","���ʾ����豸��Ϣ��","����ͯװ��","�Ͻ������","��־","��־","������ó��Դ","���������","������Ħ��Դ","������Դ","�۴�������","�۴���","�쵰","��е�豸��","�ҵ�����","����ͨ","��վ","��վͨ","���˵�","������","������","����","�������մ���̳","����","���־","���־","��å","���ϲ�","����","������","�ֹ�","������","������","��Ѳ鿴�ɹ���Ϣ","�ϲ��� �ؽ���·","������","������Ѷ��","ũ����","ƭ","�Ⱥ�","ȫ��ɹ���վ","ȫ��������","����","ɫ��","��ó��","�����ͻ�����С��","�����ͻ�������","������","������վ","��ҵ��վ","�ϵ�","ʳƷ��ҵ��","ʵ����","ʾ��","��","�Ѳ���","�Ѻ���ó����","��������","̨�����","��ɽ������","ͻ��˹̹","����(�й�)������","���̹�����","���˵�","���罻��","�����ƹ�","��վһҹ����","���빤����","����������","�����վ","�������","������ҵ��","���ض���","����ǽ","���ŷ���","��Ϣƽ̨","��ҵ��վ","��ת��ͷ��","ѹ��","һ��һ��","һ��һ̨","��óͨ","����","����","�췴","�㽭����Ʒ��","������","��ѹ","����","����","���η粨","�����˶�","֧����","�йش���Ա�����","�й���","�й��ɹ����б���","�й�������ҵ��","�й�������Ʒ��","�й�������Ϣ��","�й������","�й���ͯ��Ʒ��","�й�������","�й�����Ʒ������","�й�������","�й����ʼӹ���","�й����ʼӹ���","�й�����������","�й�������","�й�����Ʒ��","�й�������","�й�Ʒ�ƽ�����","�й�����","�й�ȫ�Զ��齫����","�й��߲���","�й�ҩ��","�й�����","�й���֯��","�й�������","�й�����","�л���ҵ��","�л�������","������","�Է�","�����˶�","���");
for(var k=0;k<wordsArrays.length;k++){
var wordLowerCase=wordsArrays[k].toLowerCase();
if (content.indexOf(wordLowerCase)!=-1){
return wordsArrays[k];
}
}
return "";
}
document.ondragstart = function(){
return false;
}
var validatedInfo		= "��д��ȷ��";
var requireErrorInfo	= "<span class=\"R\">����Ϊ�����</span><br \/>";
var msgInfo	= new Array();
//validator info
msgInfo[0]				= new Array(	'��4-20����ĸ��������ɡ���֧�����ģ����������ֿ�ͷ��ע��ɹ��󲻿��޸ġ�','<h1><span class=\"R\">������д����<\/span>��Ա��¼��ֻ����4-20��Ӣ����ĸ���������(��֧�����ġ����������ֿ�ͷ)��<\/h1>','<h1>�û�Ա���Ѿ����ڣ�����������<\/h1>','�û�Ա������ע��');
var loginid	 		= new formEle(true,"loginid",null,"loginid_info",msgInfo[0],"loginid_info_check");
msgInfo[1]				= new Array(	'��6-20����ĸ�����ִ�Сд����������ɡ�',
'<h1><span class=\"R\">�����õ���������<\/span>������6-20��Ӣ����ĸ(���ִ�Сд)���������<\/h1>','<h1>�����õ����벻����ȫ�����������������룡<a href=\"http://info.china.alibaba.com/helpcenter/tips/s5011167-d5506534.html\" target=\"_blank\" class=\"NOL\"><font color=\"#399600\">��ΰ�ȫ��������<\/font><\/a><\/h1>','',validatedInfo);
var password	 		= new formEle(true,"password",null,"password_info",msgInfo[1],"password_info_check");
msgInfo[2]				= new Array(	'��������һ����������д�����롣','<h1><span class=\"R\">������������벻һ�£�<\/span>��������һ����������д�����롣<\/h1>');
var confirm_password	= new formEle(true,"confirm_password","sameas=password","confirm_password_info",msgInfo[2],"confirm_password_info_check");
msgInfo[3]				= new Array(	'���佫�������һ��������Ҫ;�����������д��<br>û�е������䣿<a href=\"http://www.126.com\" target=\"_blank\" class=\"NOL\">ע����������<\/a> <a href=\"https://member.cn.yahoo.com/cnreg/reginfo.html?id=61001\" target=\"_blank\" class=\"NOL\">ע���Ż�����<\/a>','<h1>�����ʼ���ʽ����ȷ����������ȷ�ĵ����ʼ���ַ��<\/h1>');
var email				= new formEle(true,"email",null,"email_info",msgInfo[3],"email_info_check");
msgInfo[4]				= new Array(	'����ͻ���ʱ��ϵ����������ͰͲ����κ��շѷ���','<h1>�ֻ����볬����󳤶�16����<\/h1>','<h1>����д��ȷ���ֻ�����<\/h1>');
var mobile		= new formEle(false,"mobile",null,"mobile_info",msgInfo[4],"mobile_info_check");
msgInfo[5]				= new Array(	'ע����ҵ����д���̾�ע���ȫ�ƣ����̺ŵĸ��徭Ӫ������дִ���ϵ�����������ע���徭Ӫ����:���������徭Ӫ��','<h1>��˾������󳤶Ȳ��ܳ���50<\/h1>','<h1>���зǷ��ַ�<\/h1>','���зǷ��ַ���@��#��$��%�ȣ�����������д','��ʹ��������д��˾����');
var company			= new formEle(true,"company",null,"company_info",msgInfo[5],"company_info_check");
msgInfo[6]				= new Array(	'����ϸ��д��˾��Ӫ��ַ���磺������·�Ϲ�����1��8�㡣','<h1>��ַ���Ȳ��ܳ���80<\/h1>');
var address = new formEle(true,"address",null,"address_info",msgInfo[6],"address_info_check");
msgInfo[7]				= new Array(	'&nbsp;','<h1>����������1-32����<\/h1>','<h1>�������зǷ��ַ�<\/h1>','���зǷ��ַ���@��#��$��%�ȣ�����������д');
var first_name = new formEle(true,"first_name",null,"first_name_info",msgInfo[7],"first_name_info_check");
msgInfo[11]				= new Array('�����Ʒ�����á������ָ����磺��װ�����ϣ�������','<h1>�ؼ��ֺ��зǷ��ַ�<\/h1>');
var buykeyword = new formEle(false,"buykeyword",null,"business_info",msgInfo[11],"keywords_info_check");
var salekeyword = new formEle(false,"salekeyword",null,"business_info",msgInfo[11],"keywords_info_check");
msgInfo[12]				= new Array('&nbsp;','<h1>�������ֻ��ʹ�����ֻ�\"/\",\"-\"<\/h1>','������벻��С��6λ����������д','�벻Ҫʹ���ظ���������Ϊ�������');
msgInfo[14]				= new Array('&nbsp;','<h1>���ź͹��Һ���ֻ��ʹ������<\/h1>');
msgInfo[8]				= new Array('������룬���á�/���ָ����ֻ����룬���á�-���ָ���','<h1>���ź͹��Һ���ֻ��ʹ������<\/h1>');
msgInfo[9]				= new Array('������룬���á�/���ָ����ֻ����룬���á�-���ָ���','<h1>�绰����ֻ��ʹ�����ֻ�\"/\",\"-\"<\/h1>','�绰���벻��С��7λ����������д','�벻Ҫʹ���ظ���������Ϊ�绰����');
var phone_country = new formEle(true,"phone_country",null,"phone_info",msgInfo[8],"phone_info_check");
var phone_area = new formEle(true,"phone_area",null,"phone_info",msgInfo[8],"phone_info_check");
var phone_number = new formEle(true,"phone_number",null,"phone_info",msgInfo[9],"phone_info_check");
var fax_country = new formEle(false,"fax_country",null,"fax_info",msgInfo[14],"fax_info_check");
var fax_area = new formEle(false,"fax_area",null,"fax_info",msgInfo[14],"fax_info_check");
var fax_number = new formEle(false,"fax_number",null,"fax_info",msgInfo[12],"fax_info_check");
msgInfo[13]				= new Array(	'&nbsp;','<h1>ְλ���Ƴ�����󳤶�16����<\/h1>','<h1>ְλ���зǷ��ַ�<\/h1>');
var job_title = new formEle(true,"job_title",null,"job_title_info",msgInfo[13],"job_title_info_check");
function submitForm(obj) {
putAreaInfoToInput();
document.form.userCode.value = tot(document.form.userCode.value);
var ret = validateAll(obj);
if (ret == true) {
adjustKeywords();
if (document.form.Submit) {
document.form.Submit.disabled = true;
}
}
return ret;
}
// ����keywords
function adjustKeywords(){
var keyselected ='';
for(i=0;i<document.form.business_role.length;i++){
if(document.form.business_role[i].checked){
keyselected = document.form.business_role[i].value;
break;
}
}
if(keyselected == 'buyer'){
document.getElementById("salekeyword").value = '';
}else if(keyselected == 'seller'){
document.getElementById("buykeyword").value = '';
}
}
function afterChangeArea(){
var right = validateArea_rightOrWrong();
if(right){
info_check('city_info_check');
clean_check("city_info");
}else{
var city_info_Obj=$('city_info_check');
if(city_info_Obj.innerHTML.indexOf("img")> 0 ){
city_info_Obj.innerHTML = "��˾���ڵ�";
}
warning_check("city_info",'<h1>��ѡ��ʡ�ݻ��߳���<\/h1>');
//��֤�������ͼ����Ϣ
sendFieldclick($('city'));
document.getElementById("city_info").focus();
}
}
function afterChangeCountry(list,selectFlag,selectIndex){
afterChangeArea();
}
function afterChangeProvince(){
afterChangeArea();
}
function afterChangeCapitalCity(){
afterChangeArea();
}
function afterChangeCity(){
afterChangeArea();
}
//ע����Ϣͳ��:�ֶμ�ʱУ����
function sendFieldclick(obj){
var strsid = getSid();
if(strsid == ""){
return;
}
//trim
var patn = /(^\s)|(\s$)/;
if(patn.test(obj.value))	obj.value = obj.value.trim();
var inputid = obj.id;
if(  inputid == "loginid" || inputid == "first_name" || inputid == "job_title" || inputid == "email"
|| inputid == "phone_country" || inputid == "phone_area" || inputid == "phone_number" || inputid == "fax_country"
|| inputid == "fax_area" || inputid == "fax_number" || inputid == "mobile" || inputid == "company"
|| inputid == "address" || inputid == "salekeyword" || inputid == "buykeyword" || inputid == "city"){
regfieldclick("?sid="+strsid+"&field="+inputid+"&value="+obj.value);
}
}
function sendAllFieldclick(){
var strsid = getSid();
if(strsid == ""){
return;
}
var objAry = new Array("loginid","first_name","job_title","email","phone_country","phone_area",
"phone_number","fax_country","fax_area","fax_number","mobile","company",
"province","country","city","address","category","salekeyword","buykeyword");
var clickParm = "?sid=" + strsid;
for(var i=0; i < objAry.length ; i++){
var oldClickParm = clickParm;
var obj = document.getElementById(objAry[i]);
if(obj){
clickParm = clickParm + "&" + objAry[i] + "=" + getObjValue(obj);
}
// http://stat.china.alibaba.com/reg_submit.htm ��44���ַ��������ȥ
// time������19���ַ�ҲҪ��ȥ
// �������ƣ�����һ��
if(clickParm.length > (2048-44-19)){
regsubmitclick(oldClickParm);
clickParm = "?sid=" + strsid + "&" + objAry[i] + "=" + getObjValue(obj);
}
}
if(clickParm != ("?sid=" + strsid)){
regsubmitclick(clickParm);
}
}
//�õ�sessionId
function getSid(){
var objsid = document.getElementById("sessionId");
if(!objsid){
return "";
}else{
return objsid.value;
}
}
//�õ���ͬ�Ŀؼ���ֵradio����text
function getObjValue(obj){
if(obj.type == "radio"){
var obj2 = document.getElementsByName(obj.id);
for(var j=0;j<obj2.length;j++){
if(obj2[j].checked){
return obj2[j].value;
}
}
return "";
}else{
return obj.value;
}
}
