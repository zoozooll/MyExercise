/**
* @author:aliued-wd junbiao.zhujb
* @version 1.0
*/
var userAgent = navigator.userAgent.toLowerCase();
/**
* �ж������
*/
Browser = {
version: (userAgent.match( /.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/ ) || [])[1],
isSafari: /webkit/.test( userAgent ),
isOpera: /opera/.test( userAgent ),
isMsie: /msie/.test( userAgent ) && !/opera/.test( userAgent ),
isMozilla: /mozilla/.test( userAgent ) && !/(compatible|webkit)/.test( userAgent )
}
/**
* �첽����������ķ�װʵ��
*/
AsyncScript = function(){
var nidx = 0;//script���������ID
/**
* ���ݽӵ����ͺͽڵ�ӵ�е����Լ�������������һ���ڵ�
* @param {String} nodeType �ӵ�����
* @param {Object} attributes �ڵ�ӵ�е�����
* @param {Object} win ������
* @return ����һ��DOM�ڵ�
*/
var _node = function(nodeType,attributes,win){
var w = win || window, d=w.document, n=d.createElement(nodeType);
for (var i in attributes) {
if (attributes[i]) {
n.setAttribute(i, attributes[i]);
}
}
return n;
};
/**
* ����URL��������ͱ���������һ��javascript�ڵ�
* @param {String} url URL
* @param {Object} win ������
* @param {String} charset ����
* @return ����һ��javascript�ڵ�
*/
var _scriptNode = function(url,win,charset){
var c = charset || "gbk";
return _node("script",{
"id":"alicn"+(nidx++),
"type":"text/javascript",
"charset": c,
"src":url
},win);
};
return {
/**
* �����첽��������ķ���
* @param {Object} url �첽�������������
* @param {Object} fn ����ɹ��󴥷��ķ���
* @param {Object} scope ������
*/
script:function(url,fn,scope){
var w=scope||window, d=w.document, h=d.getElementsByTagName("head")[0], n;
n = _scriptNode(url,w,"gbk");
h.appendChild(n);
if(Browser.isMsie){
n.onreadystatechange = function(){
var rs = this.readyState;
if("loaded" === rs || "complete" === rs){
fn();
}
}
}else{
n.onload = function(){
fn();
}
}
}
}
}();
/**
* ���÷���
*/
Lang = {
/**
* �ж��Ƿ��Ƿ���
* @param {Object} o
*/
isFunction: function(o) {
return typeof o === 'function';
},
/**
* ������IE��֧��ʲô�ģ����������
* @param {Object} r
* @param {Object} s
*/
_IEEnumFix: function(r, s) {
if (Browser.isMsie) {
var add=["toString", "valueOf"], i;
for (i=0;i<add.length;i=i+1) {
var fname=add[i],f=s[fname];
if (Lang.isFunction(f) && f!=Object.prototype[fname]) {
r[fname]=f;
}
}
}
},
/**
* �ϲ�����ķ������������ռ���һ����������
* @param {Object} r �ϲ���Ķ���
* @param {Object} s ��Ҫ�ϲ���R�еĶ���
*/
augmentObject: function(r, s) {
if (!s||!r) {
throw new Error("Absorb failed, verify dependencies.");
}
var a=arguments, i, p, override=a[2];
if (override && override!==true) { // only absorb the specified properties
for (i=2; i<a.length; i=i+1) {
r[a[i]] = s[a[i]];
}
} else { // take everything, overwriting only if the third parameter is true
for (p in s) {
if (override || !r[p]) {
r[p] = s[p];
}
}
Lang._IEEnumFix(r, s);
}
},
/**
* �ϲ�����
*/
merge: function() {
var o={}, a=arguments;
for (var i=0, l=a.length; i<l; i=i+1) {
Lang.augmentObject(o, a[i], true);
}
return o;
}
}
