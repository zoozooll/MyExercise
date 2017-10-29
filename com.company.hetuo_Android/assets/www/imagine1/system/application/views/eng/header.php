<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<?=base_url()?>"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="system/css/style.css" rel="stylesheet" type="text/css" />
<div id="header">
  <div class="hsbcEntity"> </div>
  	<div style="float:left">
  		<img src="system/images/logo.jpg" alt="" />
    
    </div>
    <style >
    	.heade_right	{float:right}
		.heade_right li	{float:right;margin-left:40px;}
		/* TABMENU DIVLET
---------------------------------------------------------------------------------------------- */
div.hsbcDivletTabMenu
{
	background:url("system/images/tab_bg1.jpg") repeat-x left bottom;
	height:44px;
	width:100%;
	clear:left;
}
div.hsbcDivletTabMenu ul
{
	margin:0px;
	padding:0px;
}
div.hsbcDivletTabMenu ul li
{
	margin:0px;
	padding:0px 0px 0px 6px;
	list-style:none;
	float:left;
	height:44px;
	background:url("system/images/tab_off_left.gif") no-repeat top left;
	text-align:center;
}
div.hsbcDivletTabMenu ul li a,
div.hsbcDivletTabMenu ul li span.extNoLink
{
	padding:0px 9px 0px 2px;
	margin:0px;
	height:44px;
	line-height:36px;
	float:left;
	display:block;
	background:url("system/images/tab_off_right.gif") no-repeat top right;
	text-decoration:none;
	color:#666;
	font-weight:bold;
	vertical-align:50%;
}
div.hsbcDivletTabMenu ul li a:hover
{
	color:#f00 !important;
	text-decoration:none;
}
div.hsbcDivletTabMenu ul li.hsbcDivletTabMenuSelected
{
	background:url("system/images/tab_on_left.gif") no-repeat top left;
}
div.hsbcDivletTabMenu ul li.hsbcDivletTabMenuSelected a
{
	background:url("system/images/tab_on_right.gif") no-repeat top right;
	color:#f00;
}
div.hsbcDivletTabMenu ul li.extFirstTab
{
	background:url("system/images/first_tab_off_left.gif") no-repeat top left;
	padding-left:15px;
}
div.hsbcDivletTabMenu ul li.extFirstTabSelected
{
	background:url("system/images/first_tab_on_left.gif") no-repeat top left;
	padding-left:15px;
}
div.hsbcDivletTabMenu ul li.extFirstTabSelected a
{
	background:url("system/images/tab_on_right.gif") no-repeat top right;
	color:#f00;
}
div.hsbcDivletTabMenu ul li.extSelectedTabRight
{
	background:url("system/images/tab_off_left_selected_right.gif") no-repeat top left;
}
div.hsbcDivletTabMenu ul li.extSelectedTabRight a
{
	background:url("system/images/tab_off_right_selected_right.gif") no-repeat top right;
}
div.hsbcDivletTabMenu ul li.extSelectedTabLeft
{}
div.hsbcDivletTabMenu ul li.extSelectedTabLeft a
{
	background:url("system/images/tab_off_right_selected_left.gif") no-repeat top right;
}
div.hsbcDivletTabMenu ul li.extLastTabSelected
{
	background:  url("system/images/tab_on_left.gif") no-repeat top left;
}
div.hsbcDivletTabMenu ul li.extLastTabSelected a
{
	background: url("system/images/last_tab_on_right.gif") no-repeat top right;
	padding-right:34px;
	color:#f00;
}
div.hsbcDivletTabMenu ul li.extRightAlignTab
{
	float:right;
	padding-right:10px;
}
div.hsbcDivletTabMenu ul li.extRightAlignTabSelected
{
	background: url("mages/first_tab_on_left.gif") no-repeat top left;
	float:right;
	padding-left:15px;
	padding-right:0px;
}
div.hsbcDivletTabMenu ul li.extRightAlignTabSelected a
{
	background:url("mages/right_align_tab_on_right.gif") no-repeat top right;
	padding-right:20px;
	padding-left:3px;
	color:#f00;
}
div.hsbcDivletTabMenu ul li.extSelectedTabRightEndBlock
{
	background:url("system/images/tab_off_left_selected_right_end_block.gif") no-repeat top left;
}
div.hsbcDivletTabMenu ul li a.extDoubleLine
{
	line-height:13px;
	padding-top:4px;
	/* FIX FOR:PC IE5.5 and PC IE5.01 */
	height:44px;
	hei\ght:40px;
}
div.hsbcDivletTabMenu ul li a .extMacIE
{
	float:left;
	clear:left;
	padding-top:0px;
	padding-bottom:-10px;
	/*FIX FOR:MAC IE \*/
	float:none;
	clear:none;
	padding-top:0px;
	/* */
}



div.hsbcDivletTabMenu ul li.extNoTab
{
	background:none !important;
}
div.hsbcDivletTabMenu ul li.extNoTab a
{
	background:none !important;
	padding:0px !important;
	padding-right:3px !important;
	font-weight:normal;
}
div.hsbcDivletTabMenu ul li span.extNoLink
{
	background:none !important;
	padding:0px !important;
	padding-right:3px !important;
}

    </style>
    <ul class="heade_right">
  		<li><img src="system/images/email.jpg" alt="" /></li>
    	<li><img src="system/images/about_us.jpg" alt="" /></li>
    </ul>
    <div class="containerTabMenu">
<div class="hsbcDivletTabMenu">
<div class="hsbcDivletTabMenuInner">

<ul>

<li class="extFirstTab"><a title="Personal" 
href="<?=site_url('globals/index/eng');?>"> Home </a></li>
<li><a title="Banking" 
href="<?=site_url('globals/company/eng');?>">Company Profile</a></li>
<li><a title="Investments" 
href="<?=site_url('globals/prolist/eng');?>">Products</a></li>
<li ><a title="Cards" 
href="<?=site_url('globals/infolist/eng');?>">Technical Data</a></li>
<li><a title="Mortgages" 
href="<?=site_url('globals/news/eng');?>">News</a></li>
<li><a title="Mortgages" 
href="<?=site_url('globals/contact/eng');?>">Contact Us</a></li>
<li class="extRightAlignTab extNoTab">
</li></ul>
</div>
</div>
</div>
</div>
      <!--<table class="menuRight">
        <tr>
          <td align="right"><form>
              <select name="fastSearch" id="fastSearch">
                <option value="<?=site_url('globals/index/chi');?>">首页</option>
                <option value="<?=site_url('product/product_type/chi/corporate');?>">激光/光学测量仪器</option>
                <option value="<?=site_url('product/product_type/chi/tv');?>">固体/半导体激光器</option>
                <option value="<?=site_url('product/product_type/chi/print');?>">光纤激光器</option>
                <option value="<?=site_url('product/product_type/chi/advertising');?>">电光器件</option>
                <option value="<?=site_url('product/product_type/chi/promotion');?>">法拉第隔离器</option>
                <option value="<?=site_url('product/product_type/chi/event');?>">光子晶体光纤/激光玻璃</option>
                <option value="<?=site_url('product/product_type/chi/premeium');?>">NESLAB ThermoFlex 冷却水循环器</option>
                <option value="<?=site_url('product/product_type/chi/video');?>">光机械及光学元器件</option>
              </select>
            </form></td> -->
          <td width="30px" align="right" valign="top"><a href="<?=site_url('globals/index/chi');?>" ><img src="system/images/iop_flag_cn.gif" height="16px" width="24px" /></a></td>
          <td width="24px" align="right" valign="top"><a href="<?=site_url('globals/index/eng');?>" ><img src="system/images/iop_flag_en.gif" height="16px" width="24px"/></a></td>
        </tr>
      </table>
    </div>
  </div>