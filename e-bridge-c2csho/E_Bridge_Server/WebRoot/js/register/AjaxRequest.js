///////////////////////////////////////////////////////////////////////////////////////////
//               ��Ҫʵ��iframeģ��xmlhttprequest��ʵ����ˢ���ύ
///////////////////////////////////////////////////////////////////////////////////////////
var currentRequest = null;
/**
*ҳ��onloadʱ������iframe
*����ֵ����
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
*�ύ����󣬽�������¼�����
*����ֵ����
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
*AjaxRequest����
*_method: ֵpost/get    ��formʹ�÷���һ��
*_url:    ֵstring      ��form��actionһ��
*_async:  ֵfalse/trueͬ��/�첽  ������iframe�ύ��û���첽
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
//input��������
this.setParameter = function(name,value) {
var _input = document.createElement("input");
_input.name=name;
_input.value=value;
_input.type="hidden";
_form.appendChild(_input);
}
//url��������
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
//��ȡ�����ֶ�
this.getText = function(){
return text;
}
this.setText = function(t){
text = t;
}
//ajax�������
this.onresult = function(){
}
}
