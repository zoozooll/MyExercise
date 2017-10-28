/**
* @author:aliued-wd junbiao.zhujb
* @version 1.0
*/
var userAgent = navigator.userAgent.toLowerCase();
/**
* 判断浏览器
*/
Browser = {
version: (userAgent.match( /.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/ ) || [])[1],
isSafari: /webkit/.test( userAgent ),
isOpera: /opera/.test( userAgent ),
isMsie: /msie/.test( userAgent ) && !/opera/.test( userAgent ),
isMozilla: /mozilla/.test( userAgent ) && !/(compatible|webkit)/.test( userAgent )
}
/**
* 异步跨域请求类的封装实现
*/
AsyncScript = function(){
var nidx = 0;//script对象的序列ID
/**
* 根据接点类型和节点拥有的属性及作用域来创建一个节点
* @param {String} nodeType 接点类型
* @param {Object} attributes 节点拥有的属性
* @param {Object} win 作用域
* @return 返回一个DOM节点
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
* 根据URL，作用域和编码来创建一个javascript节点
* @param {String} url URL
* @param {Object} win 作用域
* @param {String} charset 编码
* @return 返回一个javascript节点
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
* 发起异步跨域请求的方法
* @param {Object} url 异步跨域请求的链接
* @param {Object} fn 请求成功后触发的方法
* @param {Object} scope 作用域
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
* 常用方法
*/
Lang = {
/**
* 判断是否是方法
* @param {Object} o
*/
isFunction: function(o) {
return typeof o === 'function';
},
/**
* 好像是IE不支持什么的，用来解决的
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
* 合并对象的方法，把属性收集到一个对象里面
* @param {Object} r 合并后的对象
* @param {Object} s 需要合并到R中的对象
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
* 合并对象
*/
merge: function() {
var o={}, a=arguments;
for (var i=0, l=a.length; i<l; i=i+1) {
Lang.augmentObject(o, a[i], true);
}
return o;
}
}
