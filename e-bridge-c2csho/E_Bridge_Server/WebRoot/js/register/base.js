/*
* 函数说明：判断搜索内容是否为空
* 参数：	字符串
* 返回值：	是/否
* 时间：2006-5-12
*/
function checkform(str){
searchFormObj = document.getElementById(str);
var v = trim(searchFormObj.keywords.value);
if(v.length > 100){
alert("您输入的关键字过长！");
return false;
}
if(v == ""  || v.substring(0,3) =="请输入") {
alert("请输入关键字！");
return false;
}
}
/*
* 函数说明：去除头尾空格
* 参数：	字符串
* 返回值：	无
* 时间：2006-5-12
*/
function trim(inputString) {
return inputString.replace(/^ +/,"").replace(/ +$/,"");
}
/*
* 函数说明：取cookie值
* 参数：	cookie字段名
* 返回值：	cookie值
* 时间：2006-5-12
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
* 函数说明：取出对象，等于document.getElementById()
* 参数：	 对象名，参数可以多个，用逗号隔开
* 返回值：	对象
* 时间：2006-6-27
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
