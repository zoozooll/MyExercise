/*
* ����˵�����ж����������Ƿ�Ϊ��
* ������	�ַ���
* ����ֵ��	��/��
* ʱ�䣺2006-5-12
*/
function checkform(str){
searchFormObj = document.getElementById(str);
var v = trim(searchFormObj.keywords.value);
if(v.length > 100){
alert("������Ĺؼ��ֹ�����");
return false;
}
if(v == ""  || v.substring(0,3) =="������") {
alert("������ؼ��֣�");
return false;
}
}
/*
* ����˵����ȥ��ͷβ�ո�
* ������	�ַ���
* ����ֵ��	��
* ʱ�䣺2006-5-12
*/
function trim(inputString) {
return inputString.replace(/^ +/,"").replace(/ +$/,"");
}
/*
* ����˵����ȡcookieֵ
* ������	cookie�ֶ���
* ����ֵ��	cookieֵ
* ʱ�䣺2006-5-12
*/
function getCookie(sName) {
var aCookie = document.cookie.split("; ");
for (var i=0; i < aCookie.length; i++)
{
var aCrumb = aCookie[i].split("=");
if (sName == aCrumb[0])
return unescape(aCrumb[1]);
}
return null;
}
/*
* ����˵����ȡ�����󣬵���document.getElementById()
* ������	 ���������������Զ�����ö��Ÿ���
* ����ֵ��	����
* ʱ�䣺2006-6-27
*/
function $() {
var elements = new Array();
for (var i = 0; i < arguments.length; i++) {
var element = arguments[i];
if (typeof element == 'string')
element = document.getElementById(element);
if (arguments.length == 1)
return element;
elements.push(element);
}
return elements;
}
String.prototype._indexOf = String.prototype.indexOf;
String.prototype.indexOf = function()
{
if(typeof(arguments[arguments.length - 1]) != 'boolean'){
return this._indexOf.apply(this.toLowerCase(),arguments);
}
else
{
var bi = arguments[arguments.length - 1];
var thisObj = this;
var idx = 0;
if(typeof(arguments[arguments.length - 2]) == 'number')
{
idx = arguments[arguments.length - 2];
thisObj = this.substr(idx);
}
var re = new RegExp(arguments[0],bi?'i':'');
var r = thisObj.match(re);
return r==null?-1:r.index + idx;
}
}
