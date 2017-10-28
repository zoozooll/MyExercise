//country methods
function Country(id,title,phone,hasProvince) {
this.id = id;
this.title=title;
this.phone=phone;
this.hasProvince=hasProvince;
this.boardlist=new Array();
this.addBoard=addBoard;
this.getOptions = getOptions;
}
function addBoard(board) {
this.boardlist = this.boardlist.concat(board);
}
function getOptions() {
var tmp = new Array();
for(var i=0; i < this.boardlist.length;i++) {
var b = this.boardlist[i];
tmp[i]= b.getOption();
}
return tmp;
}
//board methods
function Board(country,provinceId,province) {
this.country=country;
this.province=province;
this.provinceId = provinceId;
this.getOption=getOption;
}
function getOption() {
if(this.country == "CN")
return new Option(this.province,this.provinceId);
return new Option(this.province,this.province);
}
function provincesList(countryName,provinceId,provinceName){
this.countryName = countryName;
this.provinceId = provinceId;
this.provinceName = provinceName;
this.provincesArr = new Array();
this.addProvinces = addProvinces;
this.getProvinces = getProvinces;
}
function addProvinces(province){
this.provincesArr = this.provincesArr.concat(province);
}
function getProvinces(){
var tmp = new Array();
for(var i = 0;i < this.provincesArr.length;i++){
var b = this.provincesArr[i];
tmp[i] = b;
}
return tmp;
}
function provinceCN(provinceName,capitalCityId,capitalCityName) {
this.provinceName = provinceName;
this.capitalCityId = capitalCityId;
this.capitalCityName = capitalCityName;
this.capitalCityArr = new Array();
this.addCity = addCity;
this.getCapitalCitys = getCapitalCitys;
}
function addCity(cityId,cityName) {
this.capitalCityArr = this.capitalCityArr.concat(new Option(cityName,cityId));
}
function getCapitalCitys() {
var tmp = new Array();
for(var i=0; i < this.capitalCityArr.length;i++) {
var b = this.capitalCityArr[i];
tmp[i]= b;
}
return tmp;
}
//同时改变国家和国家的省份
//selectFlag = all,hot,cn
function changeCountry(list,selectFlag) {
var selectIndex = list.selectedIndex;
if(selectFlag == "cn" || selectFlag == "hot" ) {
if(selectIndex == 0){
selectFlag = "cn"
}else if( selectIndex == 4 ){
selectFlag = "all";
selectIndex = 1;
}else{
selectFlag = "hot";
}
}else if(selectFlag == "all" ){
var cn = CountryArr[selectIndex-1].id;
if("CN" == cn) {
selectFlag = "cn";
selectIndex = 0;
}else if("HK" == cn || "TW" == cn || "MO" == cn ){
selectFlag = "hot";
if("HK" == cn) {
selectIndex = 1;
}
if("TW" == cn) {
selectIndex = 2;
}
if("MO" == cn) {
selectIndex = 3;
}
}else {
selectFlag = "all";
}
}
if ( selectFlag == "all" && selectIndex<=0) {
CountryForm.options[0].selected=true;
var len = boardForm.options.length;
for (var i=len-1;i>0;i--){
boardForm.options[i]=null;
}
}
else {
var countryname;
if("cn" == selectFlag){
countryname = CountryArrCN[selectIndex].id;
}else if ("hot" == selectFlag){
countryname = CountryArrHot[selectIndex].id;
}else {
countryname = CountryArr[selectIndex-1].id;
}
selectCountryList = selectFlag;
//除了中国,其他的都显示城市输入框
tb6.style.display="block";
// 清空一些数据
if(initFlag != "true"){
document.getElementsByName('notCNCity')[0].value = "";
document.getElementsByName('province2')[0].value = "";
}
initFlag = "false";
// PreAddress.innerHTML = "";
if("CN" == countryname) {
var provincecnSelected = $('provinceCN').selectedIndex;
var boardscn = CountryArrCN[selectIndex].getOptions();
for (var i=0;i< boardscn.length;i++) {
$('provinceCN').options[i+1] = boardscn[i];
if( provincecnSelected > 0 && (provincecnSelected == i+1 ) ) {
$('provinceCN').options[i+1].selected=true;
}
}
selectCountryList = "cn";
//显示中国的省份,地级城市和县级城市
tb1.style.display="block";
tb2.style.display="none";
tb3.style.display="none";
tb4.style.display="none";
tb5.style.display="none";
tb6.style.display="none";
}else if("HK" == countryname || "TW" == countryname || "MO" == countryname ) {
selectCountryList = "hot";
tb1.style.display="none";
tb2.style.display="block";
tb3.style.display="none";
tb4.style.display="none";
tb5.style.display="none";
}else {
var len = boardForm.options.length;
var boards = CountryArr[selectIndex-1].getOptions();
for (var i=len-1;i>0;i--){
boardForm.options[i]=null;
}
for (var i=0;i<boards.length;i++) {
boardForm.options[i+1]=boards[i];
}
if(CountryArr[selectIndex-1].hasProvince=="1") {
tb1.style.display="none";
tb2.style.display="none";
tb3.style.display="block";
tb4.style.display="block";
tb5.style.display="none";
} else if (CountryArr[selectIndex-1].hasProvince=="0"){
tb1.style.display="none";
tb2.style.display="none";
tb3.style.display="block";
tb4.style.display="none";
tb5.style.display="block";
} else {
tb1.style.display="none";
tb2.style.display="none";
tb3.style.display="block";
tb4.style.display="none";
tb5.style.display="none";
}
}
if(selectCountryList == "cn" || selectCountryList == "hot") {
CountryListCN.options[selectIndex].selected=true;
CountryListHot.options[selectIndex].selected=true;
//当存在phone_country（区号）时,改变相应的区号
if(document.getElementById("phone_country")){document.getElementById("phone_country").value = CountryArrCN[selectIndex].phone;}
}else {
CountryForm.options[selectIndex].selected=true;
selectIndex=selectIndex-1;
//当存在phone_country（区号）时,改变相应的区号
if(document.getElementById("phone_country")){document.getElementById("phone_country").value = CountryArr[selectIndex].phone;}
}
changeAddress();
}
}
function selectBoard(country,bid) {
for(var i=0;i<CountryForm.length;i++) {
if (CountryForm.options[i].value==country) {
CountryForm.options[i].selected=true;
changeCountry(CountryForm);
break;
}
}
for(var i=0;i<boardForm.length;i++) {
if (boardForm.options[i].value==bid) {
boardForm.options[i].selected=true;
break;
}
}
}
function changeProvinceList(provinceList) {
var index = provinceList.selectedIndex;
var len2 = capitalCN.options.length;
for(var i = len2-1;i>0;i--){
capitalCN.options[i] = null;
}
len2 = cityCN.options.length;
for (var i = len2-1;i>0;i--){
cityCN.options[i] = null;
}
if(index <= 0) {
selectedProvinceName = "";
selectedCityName = "";
}
else {
var province = provinceList.options[index].value;
SelectProvinceId = province;
var len = provincesListArr.length;
var tmpProvincesList;
for(var i=0;i < len;i++){
tmpProvincesList = provincesListArr[i];
if(SelectProvinceId == tmpProvincesList.provinceId ) {
//设置地级城市
var len1 = capitalCN.options.length;
for(var i = len1-1;i>0;i--){
capitalCN.options[i] = null;
}
var capitals = tmpProvincesList.getProvinces();
for(var i=0;i<capitals.length;i++){
capitalCN.options[i+1] = new Option(capitals[i].capitalCityName,capitals[i].capitalCityId);
}
break;
}
}
capitalCN.options[1].selected=true;
changeCapitalCity(capitalCN);
selectedProvinceName = provinceList.options[index].text;
changeAddress();
}
}
function changeCapitalCity(list) {
if (list.selectedIndex<=0) {
$('cityCN').options[0].selected=true;
var len = $('cityCN').options.length;
for (var i=len-1;i>0;i--){
$('cityCN').options[i]=null;
}
}
else {
var tmpProvincesList;
var len = provincesListArr.length;
var capital = list.options[list.selectedIndex].value;
for(var i=0;i<len;i++){
tmpProvincesList = provincesListArr[i];
if(SelectProvinceId == tmpProvincesList.provinceId ) {
//设置地级城市
var capitals = tmpProvincesList.getProvinces();
for(var i=0;i<capitals.length;i++){
var c = capitals[i];
if(c.capitalCityId == capital){
var len1 = $('cityCN').options.length;
for (var i=len1-1;i>0;i--){
$('cityCN').options[i]=null;
}
var citys = c.getCapitalCitys();
for(var i=0;i<citys.length;i++){
$('cityCN').options[i+1] = citys[i];
}
break;
}
//capitalCN.options[i+1] = new Option(capitals[i].Name,capitals[i].Id);
}
break;
}
}
cityCN.options[1].selected=true;
selectedCityName = cityCN.options[cityCN.selectedIndex].text;
changeAddress();
}
}
function changeCity(list){
if( list.selectedIndex > 0 ) {
selectedCityName = list.options[list.selectedIndex].text;
}
changeAddress();
}
function changeAddress(){
var address = "";
if(selectCountryList == "cn"){
address += "中国";
address = address + selectedProvinceName + selectedCityName;
}
//PreAddress.innerHTML = address;
}
function putAreaInfoToInput() {
if("cn" == selectCountryList) {
document.getElementById("country").value = "CN";
var provinceSelectedIndex = $('provinceCN').selectedIndex;
if( provinceSelectedIndex < 0)
provinceSelectedIndex = 0;
document.getElementById("province").value = $('provinceCN').options[provinceSelectedIndex].value;
var capitalSelectIndex = $('capitalCN').selectedIndex;
if( capitalSelectIndex < 0)
capitalSelectIndex = 0;
$('SelectCapitalId').value = $('capitalCN').options[capitalSelectIndex].value;
var citySelectIndex = $('cityCN').selectedIndex;
if( citySelectIndex < 0)
citySelectIndex = 0;
document.getElementById("city").value = $('cityCN').options[citySelectIndex].value;
if(document.getElementById("city").value=="市、县级市、县"){
document.getElementById("city").value="";
}
}else if( "hot" == selectCountryList ) {
var country = CountryArrHot[CountryListHot.selectedIndex];
document.getElementById("country").value = country.id;
if(country.hasProvince=="0") {
document.getElementById("province").value = document.getElementsByName('province2')[0].value;
}
else {
document.getElementById("province").value = ""
}
document.getElementById("city").value = document.getElementsByName('notCNCity')[0].value;
}else {
var country = CountryArr[CountryForm.selectedIndex-1];
document.getElementById("country").value = country.id;
if(CountryArr[$('CountryList').selectedIndex-1].hasProvince=="0") {
document.getElementById("province").value = document.getElementsByName('province2')[0].value;
}
else if (CountryArr[$('CountryList').selectedIndex-1].hasProvince=="1" ) {
var selectedIndex = document.getElementsByName('province1')[0].selectedIndex;
if( selectedIndex < 0)
selectedIndex = 0;
document.getElementById("province").value = document.getElementsByName('province1')[0].options[selectedIndex].value;
}
else {
document.getElementById("province").value = ""
}
document.getElementById("city").value = document.getElementsByName('notCNCity')[0].value;
}
}
function validateArea_rightOrWrong(){
putAreaInfoToInput();
var cityValue = $('city').value;
if(checkByteLength(cityValue,1,40)){
return true;
}else{
return false;
}
}
