﻿<html>
<head>
<TITLE><?php echo $this->__muant["__Meta"]["Title"] ?></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=<?php echo $this->__muant["__CHARSET"] ?>">
<META HTTP-EQUIV="Content-Language" content="<?php echo $this->__muant["__LANGUAGE"] ?>">
<META NAME="description" CONTENT="<?php echo $this->__muant["__Meta"]["Description"] ?>">
<META NAME="keywords" CONTENT="<?php echo $this->__muant["__Meta"]["Keywords"] ?>">
<META name="copyright" CONTENT="<?php echo $this->__muant["__Meta"]["Content"] ?>">
<link href="./css/admin/css.css" rel="stylesheet" type="text/css" />
<link href="../../css/admin/css.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="./jscript/admin/js.js"></script>
<script type="text/javascript" src="jscript/comm/divwindows.js"></script>
<!-- calendar -->
<link rel="stylesheet" type="text/css" media="all" href="./jscript/calendar/themes/winxp.css" title="Calendar Theme - system.css">

<!-- import the calendar script -->
<script type="text/javascript" src="./jscript/calendar/src/utils.js"></script>
<script type="text/javascript" src="./jscript/calendar/src/calendar.js"></script>

<!-- import the language module -->
<script type="text/javascript" src="./jscript/calendar/lang/calendar-zh.js"></script>
<script type="text/javascript" src="./jscript/calendar/src/calendar-setup.js"></script>

<style type="text/css">
 /*
 Define elements to show start/end dates and the dates in between
 */

 /*
 * for start/end dates
 */
 .edges {
	border : 1px solid;
	border-color: #adaa9c #fff #fff #adaa9c;
	background-color: #fffbee;
 }

 /*
 * for dates between start and end dates
 */
 .between {
	background-color: #dccdb9;
 }

 .calendar tbody .disabled { text-decoration: line-through; color:#000}
</style>
<!-- calendar -->
</head>
<body>
<div id="divMainBodyTop">
    <div class="fLeft"><img src="./images/default/admin/edit.gif" width="15" height="13"></div> 
    <div><?php echo $this->__muant["fun_name"] ?></div><div class="cls"></div>
</div>
<form method="post" action="admincp.php" name="form1" id="form1" target="adminMain">
<div id="divMainBodyContent">
	<div class="fLeft pad5">日期搜索:</div>
	<div class="fLeft">
		<input name="date8b" value="<?php echo $this->__muant["date8b"] ?>" class="txInput" type="text" id="arrivalDate" size="10" maxlength="10" /> 
		<input type="reset" class="txInput" value=" ... " id='button8b' />
	</div>
	<div class="fLeft">
		&nbsp;<input name="date8a" value="<?php echo $this->__muant["date8a"] ?>" class="txInput" type="text" id="departure_date" size="10" maxlength="10" /> 
		<input type="reset" class="txInput" value=" ... " id='button8a' />
		<input type="reset"  name="Submit22" value="Reset" class="txInput" onFocus="if(this.blur)this.blur()" />
		<input name="billno" type="text" class="txInput" value="<?php echo $this->__muant["billno"] ?>" size="8" maxlength="10">
		订单号
		<input name="keyword" type="text" class="txInput" size="8" <?php echo $this->__muant["keyword"] ?>>
		关键字(电话邮编地址等)
		<input name="submitdata" class="txInput" type="submit" value="提交">
	</div>
	<div class="fLeft pad5"><a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&state=0" target="adminMain">处理中订单</a></div><div></div>
	<div class="fLeft pad5"><a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&state=1" target="adminMain">已确认订单</a></div><div></div>
	<div class="fLeft pad5"><a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&state=2" target="adminMain">已出货订单</a></div><div></div>
	<div class="pad5"><a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>" target="adminMain">所有订单</a></div><div></div>
	<div class="cls"></div>
</div>
<div id="divMainBodyContent">
	<?php $checkinum = count($this->__muant["check"]); for($checki = 0; $checki<$checkinum; $checki++) { ?><?php if($this->__muant["check"]["$checki"]["billno"]!=$this->__muant["check"]["$checki"-1]["billno"]) { ?>
	<table bordercolor="#88ACD9" border="0" cellspacing="1" cellpadding="5">
	<tr bgcolor="#F2FAFE">
	<td width="150"><a href="#check" onClick="myshow('usershop', false, 2);callLoad('loadingiframe');setUserShopFrame('<?php echo $this->__muant["check"]["$checki"]["billno"] ?>');return false;">订单号:<?php echo $this->__muant["check"]["$checki"]["billno"] ?></a></td>
	<td width="40"><?php if($this->__muant["check"]["$checki"]["uid"]<1) { ?>非会员<?php } else { ?>会员<?php } ?></td>
	<td><?php if($this->__muant["check"]["$checki"]["state"]=='0') { ?>处理中<?php } elseif($this->__muant["check"]["$checki"]["state"]=='1') { ?>已确认<?php } elseif($this->__muant["check"]["$checki"]["state"]=='2') { ?>已出货<?php } ?></td>
	<td>订购日期:<?php echo $this->__muant["check"]["$checki"]["add_date"] ?> </td>
	<td width="170">出货日期:<?php if($this->__muant["check"]["$checki"]["shipment_date"]=='') { ?>未出货<?php } else { ?><?php echo $this->__muant["check"]["$checki"]["shipment_date"] ?><?php } ?></td>
	<td width="120">姓名:<?php echo $this->__muant["check"]["$checki"]["uname"] ?></td>
	<td width="110">支付价格:￥<?php echo $this->__muant["check"]["$checki"]["pay_price"] ?></td>
	<td><a href="#check" onClick="myshow('usershop', false, 2);callLoad('loadingiframe');setUserShopFrame('<?php echo $this->__muant["check"]["$checki"]["billno"] ?>', '<?php echo $this->__muant["check"]["$checki"]["md5id"] ?>');return false;">处理订单</a> </td>
	</tr>
	</table>
	<table border="0" cellspacing="1" cellpadding="5">
	<tr bordercolor="#666666" bgcolor="#CCCCCC">
	<td width="250">产品名称</td>
	<td width="110">厂编</td>
	<td width="110">产品编号</td>
	<td>数量</td>
	<td>价格</td>
	<td>市场价</td>
	<td>会员价</td>
	<td>特价</td>
	<td>进价</td>
	<td>售价</td>
	<td>总价</td>
	<!--<td>库存</td>-->
	<td>查看产品</td>
	</tr>
	<tr>
	<td><?php echo $this->__muant["check"]["$checki"]["name"] ?></td>
	<td><?php echo $this->__muant["check"]["$checki"]["factory_number"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["number"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["num"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["price"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["price_market"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["price_member"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["price_special"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["price_mill"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["sell_price"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["all_price"] ?>&nbsp;</td>
	<!--<td><?php echo $this->__muant["check"]["$checki"]["store"] ?>&nbsp;</td>-->
	<td><a href="<?php echo $this->__muant["check"]["$checki"]["url"] ?>" target="_blank">查看产品</a></td>
	</tr>
	<?php } else { ?>
	<tr>
	<td><?php echo $this->__muant["check"]["$checki"]["name"] ?></td>
	<td><?php echo $this->__muant["check"]["$checki"]["factory_number"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["number"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["num"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["price"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["price_market"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["price_member"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["price_special"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["price_mill"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["sell_price"] ?>&nbsp;</td>
	<td><?php echo $this->__muant["check"]["$checki"]["all_price"] ?>&nbsp;</td>
	<!--<td><?php echo $this->__muant["check"]["$checki"]["store"] ?>&nbsp;</td>-->
	<td><a href="<?php echo $this->__muant["check"]["$checki"]["url"] ?>" target="_blank">查看产品</a></td>
	</tr>
	<?php } ?><?php if($this->__muant["check"]["$checki"]["billno"]!=$this->__muant["check"]["$checki"+1]["billno"]) { ?>
	</table>
	<?php } ?>
	<?php } ?>
	<div class="cls"></div>
	<div id="divMainBodyContent">
		<div id="divMainBodyContentTitle" class="fb"></div>
		<div>
			    共<?php echo $this->__muant["allpage"] ?>页
				<span id="up">
				<a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&pn=1&date8b=<?php echo $this->__muant["date8b"] ?>&date8a=<?php echo $this->__muant["date8a"] ?>&billno=<?php echo $this->__muant["billno"] ?>&keyword=<?php echo $this->__muant["keyword"] ?>&state=<?php echo $this->__muant["state"] ?>" target="_self">首页</a>
				</span>
				<?php if($this->__muant["allpage"]>1) { ?>
				<a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&pn=<?php echo $this->__muant["uppn"] ?>&date8b=<?php echo $this->__muant["date8b"] ?>&date8a=<?php echo $this->__muant["date8a"] ?>&billno=<?php echo $this->__muant["billno"] ?>&keyword=<?php echo $this->__muant["keyword"] ?>&state=<?php echo $this->__muant["state"] ?>" target="_self">上一页</a>
				<?php $pageinum = count($this->__muant["page"]); for($pagei = 0; $pagei<$pageinum; $pagei++) { ?>
				<span <?php if($this->__muant["page"]["$pagei"]==$this->__muant["pn"]) { ?>class="f14c fb"<?php } ?>> 
				<?php if($this->__muant["page"]["$pagei"]!='') { ?>
				<a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&pn=<?php echo $this->__muant["page"]["$pagei"] ?>&date8b=<?php echo $this->__muant["date8b"] ?>&date8a=<?php echo $this->__muant["date8a"] ?>&billno=<?php echo $this->__muant["billno"] ?>&keyword=<?php echo $this->__muant["keyword"] ?>&state=<?php echo $this->__muant["state"] ?>" target="_self"><?php echo $this->__muant["page"]["$pagei"] ?></a>
				<?php } else { ?>... <?php } ?>
				</span>
				<?php } ?>
				<a href="admincp.php?mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&pn=<?php echo $this->__muant["downpn"] ?>&date8b=<?php echo $this->__muant["date8b"] ?>&date8a=<?php echo $this->__muant["date8a"] ?>&billno=<?php echo $this->__muant["billno"] ?>&keyword=<?php echo $this->__muant["keyword"] ?>&state=<?php echo $this->__muant["state"] ?>" target="_self">下一页</a>
				<?php } ?>
		</div>
		<div class="cls"></div>
	</div>
</div>
<div id="divMainBodyContentSumbit">
<input name="ok" type="submit" value="  o  k  " class="txInput">
<input name="reset" type="reset" value="reset" class="txInput">
<input type="hidden" name="lid" value="<?php echo $this->__muant["lid"] ?>">
<input type="hidden" name="mid" value="<?php echo $this->__muant["mid"] ?>">
</div>
</form>
<script type="text/javascript">
<!--  to hide script contents from old browsers
	var startDate;
	var endDate;
	var callbacks = 0;

	function resetDates() {
		startDate = endDate = null;
	}

	/*
	* Given two dates (in seconds) find out if date1 is bigger, date2 is bigger or
	 * they're the same, taking only the dates, not the time into account.
	 * In other words, different times on the same date returns equal.
	 * returns -1 for date1 bigger, 1 for date2 is bigger 0 for equal
	 */

	function compareDatesOnly(date1, date2) {
		var year1 = date1.getYear();
		var year2 = date2.getYear();
		var month1 = date1.getMonth();
		var month2 = date2.getMonth();
		var day1 = date1.getDate();
		var day2 = date2.getDate();

		if (year1 > year2) {
			return 1;
		}
		if (year2 > year1) {
			return -1;
		}

		//years are equal
		if (month1 > month2) {
			return 1;
		}
		if (month2 > month1) {
			return -1;
		}

		//years and months are equal
		if (day1 > day2) {
			return 1;
		}
		if (day2 > day1) {
			return -1;
		}

		//days are equal
		return 0;

		/* Can't do this because of timezone issues
		var days1 = Math.floor(date1.getTime()/Date.DAY);
		var days2 = Math.floor(date2.getTime()/Date.DAY);
		return (days1 - days2);
		*/
	}

	function filterDates1(cal) {
		startDate = cal.date;
		/* If they haven't chosen an 
		end date before we'll set it to the same date as the start date This
		way if the user scrolls in the start date 5 months forward, they don't
		need to do it again for the end date.
		*/

		if (endDate == null) { 
			Zapatec.Calendar.setup({
				inputField     :    "arrivalDate",
				button         :    "button8b",  // What will trigger the popup of the calendar
				ifFormat       :    "%Y-%m-%d ",
				timeFormat     :    "24",
				date           :     startDate,
				electric       :     false,
				showsTime      :     false,          //no time
				disableFunc    :    dateInRange2, //the function to call
				onUpdate       :    filterDates2
			});
		}
	}

	function filterDates2(cal) {
		endDate = cal.date;
	}

	/*
	* Both functions disable and hilight dates.
	*/
	
	/* 
	* Can't choose days after the
	* end date if it is choosen, hilights start and end dates with one style and dates between them with another
	*/
	function dateInRange1(date) {

		if (endDate != null) {

			// Disable dates after end date
			var compareEnd = compareDatesOnly(date, endDate);
			if  (compareEnd < 0) {
				return (true);
			}

			// Hilight end date with "edges" style
			if  (compareEnd == 0) {
				{return "edges";}
			}


			// Hilight inner dates with "between" style
			if (startDate != null){
				var compareStart = compareDatesOnly(date, startDate);
				if  (compareStart < 0) {
					return "between";
				} 
			} 
		}

		//disable days prior to today
		var today = new Date();
		var compareToday = compareDatesOnly(date, today);
		if (compareToday > 0) {
			return(true);
		}


		//all other days are enabled
		return false;
		//alert(ret + " " + today + ":" + date + ":" + compareToday + ":" + days1 + ":" + days2);
		return(ret);
	}

	/* 
	* Can't choose days before the
	* start date if it is choosen, hilights start and end dates with one style and dates between them with another
	*/

	function dateInRange2(date) {
		if (startDate != null) {
			// Disable dates before start date
			var compareDays = compareDatesOnly(startDate, date);
			if  (compareDays < 0) {
				return (true);
			}

			// Hilight end date with "edges" style
			if  (compareDays == 0) {
				{return "edges";}
			}

			// Hilight inner dates with "between" style
			if ((endDate != null) && (date > startDate) && (date < endDate)) {
				return "between";
			} 
		} 

		var now = new Date();
		if (compareDatesOnly(now, date) < 0) {
			return (true);
		}

		//all other days are enabled
		return false;
	}
	// end hiding contents from old browsers  -->
</script>

<script type="text/javascript">
var cal = new Zapatec.Calendar.setup({

		inputField     :    "departure_date",   // id of the input field
		button         :    "button8a",  // What will trigger the popup of the calendar
		ifFormat       :    "%Y-%m-%d ",       //  of the input field
		timeFormat     :    "24",
		showsTime      :     false,          //no time
		electric       :     false,
		dateStatusFunc :    dateInRange1, //the function to call
		onUpdate       :    filterDates1
	
});

	Zapatec.Calendar.setup({
		inputField     :    "arrivalDate",
		button         :    "button8b",  // What will trigger the popup of the calendar
		ifFormat       :    "%Y-%m-%d ",
		timeFormat     :    "24",
		showsTime      :     false,          //no time
		electric       :     false,
		dateStatusFunc :    dateInRange2, //the function to call
		onUpdate       :    filterDates2
	});
	
</script>
<script language="javascript">
function setUserShopFrame(val, md) {
	var frame = "<iframe src='admin/adminajaxset.php?switch=iframecheck&mid=<?php echo $this->__muant["mid"] ?>&lid=<?php echo $this->__muant["lid"] ?>&billno="
	           +val+"&act=&md="+md+"' frameborder='0' scrolling='yes' "
			   +" id='UserShop_iframe' name='UserShop_iframe' allowTransparency='true' width='660' height='378'>"
			   +" </iframe>";
	document.getElementById("iframeshop").innerHTML = frame;
}
</script>
<div id="usershop" style="display:none;position:absolute; width:682px; height:474px; margin:0px; padding:0px;">
	<div id="s_content">
		<div id="s_nav">
			<ul>
				<li id="n1" class="s_nav_cur">处理订单</li>
				<li id="n2"></li>
			</ul>
			<div id="close"><img src="images/default/close_win.gif" border="0" alt="关闭窗口" onClick="dispalymyshow('usershop', false)" /></div>
		</div>
		<div id="c3" class="s_div">
			<div class="decss tags_css">
				<div id="loadingiframe"></div>
				<div id="iframeshop" style="width:660px; height:378px; overflow:hidden; margin:0px; padding:0px;"></div>
			</div>
		</div>
		<div id="check_out" style="height:2px;"></div>
	</div>
</div>
</body></html>