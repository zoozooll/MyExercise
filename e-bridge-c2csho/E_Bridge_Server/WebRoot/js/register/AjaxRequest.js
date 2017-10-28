///////////////////////////////////////////////////////////////////////////////////////////
//               主要实现iframe模拟xmlhttprequest，实现无刷新提交
///////////////////////////////////////////////////////////////////////////////////////////
var currentRequest = null;
/**
*页面onload时绑定隐藏iframe
*返回值：无
*/
function initAjaxRequestBox(){
var div = null;
div = document.createElement("div");
div.id = "myAjaxResultBox";
div.innerHTML = "<iframe name='myAjaxResultFrame' init=false style='display:none' onload='myAjaxResultFrame_onResult();'></iframe>";
document.body.appendChild(div);
}
if(window.document.all){
try{
window.attachEvent("onload" ,
function(e){
initAjaxRequestBox()
}
);
}
catch(e){}
}else{
window.addEventListener("load",
function(e){
initAjaxRequestBox()
},
false
);
}
/**
*提交请求后，结果返回事件触发
*返回值：无
*/
function myAjaxResultFrame_onResult(){
var myFrame = document.getElementsByName('myAjaxResultFrame')[0];
if(myFrame.inited==true){
if(myFrame.contentWindow.document.getElementById("XMLHttpResultDiv")){
var text = myFrame.contentWindow.document.getElementById("XMLHttpResultDiv").innerHTML;
}else{
var text = myFrame.contentWindow.document.body.innerHTML;
}
if(currentRequest)
{
currentRequest.setText(text);
currentRequest.onresult();
}
}else{
myFrame.inited = true;
}
}
/**
*AjaxRequest对象
*_method: 值post/get    和form使用方法一致
*_url:    值string      和form的action一致
*_async:  值false/true同步/异步  由于是iframe提交，没有异步
*/
function AjaxRequest(_method,_url,_async){
var oThis = this;
var value = null;
var text = null;
var _form = document.getElementById("myAjaxRequestForm");
if(_form){
_form.removeNode(true);
}
_form = document.createElement("form");
document.body.appendChild(_form);
_form.method = _method;
_form.action = _url;
_form.target = "myAjaxResultFrame";
currentRequest = oThis;
//input参数传递
this.setParameter = function(name,value) {
var _input = document.createElement("input");
_input.name=name;
_input.value=value;
_input.type="hidden";
_form.appendChild(_input);
}
//url参数传递
this.send = function(v){
if(v==null || v==""){
_form.submit();
}else{
var parts = v.split('&');
for(var i=0;i<parts.length;i++){
var part = parts[i].split('=');
var name = part[0];
var value = part[1];
var _input = document.createElement("input");
_input.name=name;
_input.value=value;
_input.type="hidden";
_form.appendChild(_input);
}
_form.submit();
}
}
//提取返回字段
this.getText = function(){
return text;
}
this.setText = function(t){
text = t;
}
//ajax请求完成
this.onresult = function(){
}
}
