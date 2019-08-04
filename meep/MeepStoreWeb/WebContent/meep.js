// home
// var movie_caption_txt = $.t("store.movie");
// var music_caption_txt = $.t("store.music");
// var apps_caption_txt = $.t("store.apps");
// var game_caption_txt = $.t("store.game");
// var ebook_caption_txt = $.t("store.e_book");
// var more_left_txt = $.t("store.more");
// var more_right_txt = $.t("store.more");
// var searchbar_txt = $.t("store.search");
var coins_total = (sessionStorage.coins) ? sessionStorage.coins : 0;
// var coins_total_txt1 = $.t("store.space_coins");
// var coins_total_txt2 = $.t("store.space_coin");
// var comingsoon_txt = $.t("store.coming_soon");
// var error_txt = $.t("store.not_authorized_to_store1") + "<br/>" + $.t("store.not_authorized_to_store2") + "<br/><br/>" + $.t("store.not_authorized_to_store3");

// store
var store = "";
var storelabel = "";
var count = 0;
var array_length = 0;
var loaded_count = 0;
var loaded_shelf_count = 0;
var loaded_container_count = 0;
// var loaded_array_count = 0;
var gametitle = new Array();
var price = new Array();
var installed = new Array();
var shelf_slot = 12;
// var shelf_count = Math.ceil(games.length / shelf_slot);
var webStorePrefix = "https://static.oregonscientific.com/meepuser/";
// var goolabeltxt = $.t("store.get_it");
// var freelabeltxt = $.t("store.free");
// var noimagepng = "category/" + $.i18n.lng().split('-')[0] +"meepstore_noimage.png";

// dsc (description popup)
// var bestseller_txt = "&nbsp&nbsp" + $.t("store.bestseller_txt1") + "<br/>" + $.t("store.bestseller_txt2");
// var hotitem_txt = $.t("store.hotitem_txt1") + "<br/>" + $.t("store.hotitem_txt2");
// var sale_txt = $.t("store.sale_txt");
// var accessory_txt = $.t("store.accessory_txt1") + "<br/>" + $.t("store.accessory_txt2");
// var install_txt = $.t("store.install");
// var gooinstall_txt = $.t("store.get_it");
// var awaiting_txt = $.t("store.awaiting_txt");

// var dsc_MB_txt = $.t("store.mb");
var dsc_storeid = new Array();
var dsc_title_txt = new Array();
var dsc_developer_txt = new Array();
var dsc_size_txt = new Array();
var dsc_price_num = new Array();
var dsc_word_txt = new Array();
var dsc_content_txt = new Array();
var dsc_screenshots = new Array();
var dsc_badge = new Array();
var dsc_purchase_status = new Array();
var dsc_recommends = new Array();
var dsc_type = new Array();
var dsc_icon = new Array();
var result = "";

//balance
var balance = "";
// var balance_msg_txt1 = $.t("store.confirm_purchase");
// var balance_msg_txt1 = "Do you want to buy this app?";
// var balance_msg_txt2 = $.t("store.not_enough_coins");
// var balance_msg_txt3 = $.t("store.confirm_download");
// var balance_coins_txt = $.t("store.coins");
var balance_price_num = 0;
var balance_minus_sign = "-";
var balance_result_num = 0;
// var balance_btn_back_txt = $.t("store.back");
// var balance_btn_confirm_txt = $.t("store.confirm");
// var balance_btn_request_txt = $.t("store.request_coins1") + "<br/>" + $.t("store.request_coins2");

var blocked = "";
// var blocked_txt1 = $.t("store.not_directly_on_meep_store1") + "<br/>" + $.t("store.not_directly_on_meep_store2");
// var blocked_txt2 = $.t("store.no_permission_meep_store");
// var blocked_txt3 = $.t("store.storage_full");
// var blocked_btn_back_txt = $.t("store.back");
// var blocked_btn_request_txt = $.t("store.send_request1") + "<br/>" + $.t("store.send_request2");
// var blocked_btn_ok_txt = $.t("store.ok");

// popup redeem code
// var popup_txt_type1 = $.t("store.enter_coupon_code");
// var popup_txt_type2 = $.t("store.wrong_code1") + "<br/>" + $.t("store.wrong_code2");
// var popup_txt_type2 = "Your coupon code is invalid!";
// var popup_txt_type3 = $.t("store.used_code");
// var popup_txt_type4 = $.t("store.can_redeem");
// var popup_txt_type5 = $.t("store.can_download_num_times") + app_avail_num + $.t("store.more_times");
// var popup_txt_type6 = $.t("store.code_accepted_num_times") + "<br/><br/>" + app_avail_num + $.t("store.more_times");
// var popup_txt_type7 = $.t("store.code_accepted");
var popup_txt_type8 = "";
var popup_txt_type9 = "";
var popup_txt_type10 = "";

// var popup_s_title_txt = $.t("store.enter_coupon_code");
var popup_s_content1_txt = "";
// var popup_s_content2_txt = $.t("store.wrong_code1") + "<br/>" + $.t("store.wrong_code2");
// var popup_s_content3_txt = popup_txt_type3;
// var popup_s_btn1_txt = $.t("store.back");
// var popup_s_btn2_txt = $.t("store.confirm");
// var popup_s_btn3_txt = $.t("store.ok");

var popup_b_content1_txt = "";
var popup_b_content2_txt = "";
var popup_b_content3_txt = "";
// var popup_b_title_txt = popup_txt_type4;
// var popup_b_btn1_txt = $.t("store.back");
// var popup_b_btn2_txt = $.t("store.confirm");
// var popup_b_btn3_txt = $.t("store.ok");

// var coins_txt = $.t("store.space_coins");
var app_avail_num = 0;
var app_avail_title = new Array();
var app_avail_icon = new Array();
var coins_avail = 0;
var couponType = "";

var hasNextPage = true;
var firstPageLoaded = false;
var nextPageNum = 0;
var canLoadNextPage = true;
var newMethod = true;

function loadMovie(){
	$.i18n.init({
		ns: { namespaces: ["mt.shared"], defaultNs: "mt.shared"},
		useLocalStorage: false,
		debug: false
	}, function () {
		store = "movie";
		storelabel = "movie";
		newMethod = false;
		getArray(24);
		loadStore();
	});
}
function loadMusic(){
	$.i18n.init({
		ns: { namespaces: ["mt.shared"], defaultNs: "mt.shared"},
		useLocalStorage: false,
		debug: false
	}, function () {
		store = "music";
		storelabel = "music";
		newMethod = false;
		getArray(24);
		loadStore();
	});
}
function loadApps(){
	$.i18n.init({
		ns: { namespaces: ["mt.shared"], defaultNs: "mt.shared"},
		useLocalStorage: false,
		debug: false
	}, function () {
			store = "apps";
			storelabel = "apps";
			getDataFromDatabase(0);
		//	getArray(24);
		//	loadStore();
	});
}
function loadGame(){
	$.i18n.init({
		ns: { namespaces: ["mt.shared"], defaultNs: "mt.shared"},
		useLocalStorage: false,
		debug: false
	}, function () {
			store = "game";
			storelabel = "game";
			getDataFromDatabase(0);
	});
}
function loadEbook(){
	$.i18n.init({
		ns: { namespaces: ["mt.shared"], defaultNs: "mt.shared"},
		useLocalStorage: false,
		debug: false
	}, function () {
			store = "ebook";
			storelabel = "e_book";
			getDataFromDatabase(0);
	});
}

function getHttpHeader() {
	var header = {};
	if (sessionStorage.authToken) {
		header.Authorization = sessionStorage.authToken;
	}
	if (sessionStorage.visitor) {
		header['X-MEEPSTORE-VISITOR'] = sessionStorage.visitor;
	}
	return header;
}

function getDataFromDatabase(page) {
	//if (true) {
	if (hasNextPage && canLoadNextPage) {
		canLoadNextPage = false;

		var resource = store;

		if (resource == "apps"){
			resource = "app";
		}

	    $.ajax({type: 'GET',
	            url: "https://portal.meeptablet.com/1/store/items/" + resource + "/" + page,
	            contentType: 'application/json',
	            dataType: "json",
	            async: true,
	            cache: false,
	            headers: getHttpHeader(),
	            crossDomain: true,
	            data: "",
	            success: function(data) {
	            	canLoadNextPage = true;
	                if (data.code === 200) {
						console.log(data);
                		if(data["results"].length > 0) {							
							array_length = data["results"].length;
                			fillLocalArray(data["results"], true);
							if (nextPageNum == 0) {
                				// First page is loaded
                				loadStore();
                				getDataFromDatabase(1);
                			}

                			if (data["results"].length < 12) {
		                		hasNextPage = false;
		                	}

                			nextPageNum = page + 1;
                		} else {
                			hasNextPage = false;
                		}
	                }
	            },
	            error: function(httpres) {
	            	canLoadNextPage = true;

	                switch (httpres.status) {
	                    case 400:
	                    case 403:
	                    case 404:
	                    case 500:
	                    case 503:
	                        data = JSON.parse(httpres.responseText);

	                        break;
	                }
	            }
	    });
	}
}

function getArray(num){

	// get array from db
	// test = ......

	// Dummy
	var temp = [];
	for (var i=0; i<num; i++) {
		var j = i % games.length;
		temp.push(games[j]);
	}

	fillLocalArray(temp, true);
}

function fillSingleObject(ds){
	
	dsc_storeid[0] = ds._id;
	dsc_title_txt[0]= ds.name;
	dsc_developer_txt[0] = ds.developer;
	dsc_size_txt[0] = ds.size;
	if(ds.coins == -2 || ds.name == "Coming Soon" || ds.developer == "Coming Soon"){
		dsc_price_num[0] = $.t("store.coming_soon");}
	else if(ds.coins == -1){
		dsc_price_num[0] = $.t("store.get_it");}
	else if(ds.coins == 0){
		dsc_price_num[0] = $.t("store.free");}
	else{
		dsc_price_num[0] = ds.coins;
	}	
	
	dsc_badge[0] = ds.badge;
// dsc_badge[0] = "sdcard";
	
	
	dsc_word_txt[0] = $.t("store.description");
	// dsc_content_txt[0] = ds.description;
	dsc_content_txt[0] = ds.description.replace(/(\r\n|\n|\r)/gm, "<br/>");
	dsc_screenshots[0] = ds.screenshots;
	dsc_type[0]	= ds.type;
	dsc_icon[0] = ds.icon;
	dsc_purchase_status[0] = ds.purchase_status;
	dsc_recommends[0] = ds.recommends;



	// dsc_storeid[0] = sessionStorage._id;
	// dsc_title_txt[0]= sessionStorage.name;
	// dsc_developer_txt[0] = sessionStorage.developer;
	// dsc_size_txt[0] = sessionStorage.size;
	// if(sessionStorage.coins == -1){
		// dsc_price_num[0] = $.t("store.get_it");}
	// else if(sessionStorage.coins == 0){
		// dsc_price_num[0] = $.t("store.free");}
	// else{
		// dsc_price_num[0] = sessionStorage.coins;
	// }
	// dsc_badge[0] = sessionStorage.badge;
	// dsc_word_txt[0] = "Description";
	// dsc_content_txt[0] = sessionStorage.description.replace(/(\r\n|\n|\r)/gm, "<br>");
	// dsc_screenshots[0] = sessionStorage.screenshots;
	// dsc_type[0]	= sessionStorage.type;
	// dsc_icon[0] = sessionStorage.icon;
	// dsc_purchase_status[0] = sessionStorage.purchase_status;
	// dsc_recommends[0] = sessionStorage.recommends;

}


function fillLocalArray(ds, willLoadNextShelf){

	for (var i=0; i<ds.length; i++) {
			var j = dsc_title_txt.length;
			dsc_storeid[j] = ds[i]._id;
			dsc_title_txt[j] = ds[i].name;
			dsc_developer_txt[j] = ds[i].developer;
			dsc_size_txt[j] = ds[i].size;
			if(ds[i].coins == -2 || ds[i].name == "Coming Soon" || ds[i].developer == "Coming Soon"){
				dsc_price_num[j] = $.t("store.coming_soon");}
			else if(ds[i].coins == -1){
				dsc_price_num[j] = $.t("store.get_it");}
			else if(ds[i].coins == 0){
				dsc_price_num[j] = $.t("store.free");}
			else{
				dsc_price_num[j] = ds[i].coins;}
				
// dsc_price_num[j] = $.t("store.coming_soon");
				
				dsc_word_txt[j] = "Description";
				// dsc_content_txt[j] = ds[i].description;
				dsc_content_txt[j] = ds[i].description.replace(/(\r\n|\n|\r)/gm, "<br>");
				dsc_screenshots[j] = ds[i].screenshots;
				dsc_type[j]	= ds[i].type;
				dsc_icon[j] = ds[i].icon;
				dsc_badge[j] = ds[i].badge;
				
// dsc_badge[j] = "sdcard";


				dsc_purchase_status[j] = ds[i].purchase_status;
				dsc_recommends[j] = ds[i].recommends;
				// count++;
		}

	// }
	// loaded_array_count += count;

	if (willLoadNextShelf) {
		loadNextShelf();
	}
}



function loadCoins(){
	$("#coins_icon").html("<div id='coins_icon_txt' data-lang='" + $.i18n.lng().split('-')[0] + "'></div>");
	if(coins_total <= 1){
		$("#coins_icon_txt").html(coins_total + $.t("store.space_coin"));
	}
	else{
		$("#coins_icon_txt").html(coins_total + $.t("store.space_coins"));
	}
}

function getAwardUrl(string){
	if(string == "likes"){
		return "category/" + $.i18n.lng().split('-')[0] + "/meep_store_bronze.png";
	}
	else if (string == "friendly"){
		return "category/" + $.i18n.lng().split('-')[0] + "/meep_store_silver.png";
	}
	else if (string == "recommends"){
		return "category/" + $.i18n.lng().split('-')[0] + "/meep_store_gold.png";
	}
	else {
		return "";
	}
}
function requestCoins(id){
	// request coins
	 $.ajax({type: 'GET',
	            url: "https://portal.meeptablet.com/1/store/coinrequest",
	            contentType: 'application/json',
	            dataType: "json",
	            async: true,
	            cache: false,
	            headers: getHttpHeader(),
	            crossDomain: true,
	            data: "",
	            success: function(data) {
	                if (data.code === 200) {

					backToDsc(id);
					$("#dsc_pricebtn").hide();
					$("#dsc_installbtn").hide();
					$("#dsc_freebtn").hide();
					$("#dsc_gooinstallbtn").hide();
					$("#dsc_approval").show();
					}
	            },
	            error: function(httpres) {
	                switch (httpres.status) {
	                    case 400:
	                    case 403:
	                    case 404:
	                    case 500:
	                    case 503:
	                }
	            }
	    });
}



function getSingleObject(e){
	$.i18n.init({
		ns: { namespaces: ["mt.shared"], defaultNs: "mt.shared"},
		useLocalStorage: false,
		debug: false
	}, function () {
		if (window.location.hash.length === 41){
			sessionStorage.authToken = "OST " + window.location.hash.substring(1);
			$("#error").hide();
		}
		else if (sessionStorage.authToken && sessionStorage.authToken.length === 44) {
			$("#error").hide();
		}
		else {
				// show error page
				$("#error").html("<div>" + $.t("store.not_authorized_to_store1") + "<br/>" + $.t("store.not_authorized_to_store2") + "<br/><br/>" + $.t("store.not_authorized_to_store3") + "</div>");
				$("#error").css("background-image", "url(/main_menu/meepstore_cannotaccess.png)");
				$("#error").show();
		}

		var storeid = e.window.location.search.substring(1);
		var directPurchase = false;
		if (storeid.substring(0, 7) === "DIRECT:") {
			directPurchase = true;
			storeid = storeid.substring(7);
		}

		// pass ObjectId to server and get object back
		 $.ajax({type: 'GET',
					url: "https://portal.meeptablet.com/1/store/item/" + storeid,
					contentType: 'application/json',
					dataType: "json",
					async: true,
					cache: false,
					headers: getHttpHeader(),
					crossDomain: true,
					data: "",
					success: function(data) {
						if (data.code === 200) {
							fillSingleObject(data.result);
							loadSingleDsc(data.result);

							if(directPurchase){
								switch(result){
								case "goo":
									$("#dsc_gooinstallbtn").click();
									break;
								case "install":
									// $("#dsc_installbtn").click();
									break;
								case "no":
									$("#dsc_pricebtn").click();
									break;
								case "free":
									$("#dsc_freebtn").click();
									break;
								case "blocked":
									if(dsc_price_num[id] == $.t("store.get_it")){
										$("#dsc_gooinstallbtn").click();
									}
									else if(dsc_price_num[id] == $.t("store.free")){
										$("#dsc_freebtn").click();
									}
									else{
										$("#dsc_pricebtn").click();
									}
									break;
								case "awaiting":
									break;
								default:{}
								}
							}
						}
					},
					error: function(httpres) {
						switch (httpres.status) {
							case 400:
							case 403:
							case 404:
							case 500:
							case 503:
						}
					}
			});
	});
}

// function getSingleObject(item){

	// var url = e.window.location.href.split('?')[1];

	// pass ObjectId to server and get object back

	// fillSingleObject(item);

	// dsc_title_txt[0]= url;

	// return item;
// }


function requestUpdateStatus(id){

	var storeid = dsc_storeid[id];

	$("#dsc_balance_btn_confirm").hide();
	$("#dsc_balance_btn_request").hide();
	$("#dsc_balance_btn_back").hide();
	$("#dsc_installbtn").hide();

	$.ajax({type: 'GET',
			url: "https://portal.meeptablet.com/1/store/purchase/" + storeid,
			contentType: 'application/json',
			dataType: "json",
			async: true,
			cache: false,
			headers: getHttpHeader(),
			crossDomain: true,
			data: "",
			success: function(data) {
				if (data.code === 200 || data.code === 202) {
					if(data.code === 200){
						// dsc_purchase_status[id] = "PURCHASED";
						$("#dsc_approval").html($.t("store.installed_txt"));
						$("#dsc_approval").show();
						// $("#dsc_installbtn").hide();
					}
					if(data.code === 202){
						if(dsc_price_num[id] != $.t("store.get_it")){
							dsc_purchase_status[id] = "PENDING";
						}
					}
					if($("#dsc_blocked").is(":visible")){
						requestApproval();
					}
					else{
						confirmPayment(storeid);
					}
				}
			},
			error: function(httpres) {

				switch (httpres.status) {
					case 400:
					case 403:
					case 404:
					case 500:
					case 503:
						break;
				}
			}
	});
}

function checkout(id){

		switch(result){
			case "blocked":{
				$("#dsc_balance").hide();
				$("#dsc_blocked").show();
				$("#dsc_blocked_btn_request").hide();
				$("#dsc_blocked_btn_back").hide();
				$("#dsc_blocked_btn_ok").show();
				$("#dsc_bottom_row").css("overflow-x", "");
				$("#dsc_bottom_row").css("overflow-y", "");
				$("#dsc_screenshot_frame").css("overflow-x", "");
				$("#dsc_screenshot_frame").css("overflow-y", "");
				$("#dsc_bottom_row").hide();
				break;
			}
			case "free":{
				// $("#dsc_balance").hide();
				// $("#dsc_blocked").hide();
				// $("#dsc_approval").show();

				balance_price_num = 0;
				balance_result_num = coins_total;
				
				if(dsc_badge[id] == "sdcard"){
					$("#dsc_balance_msg").html($.t("store.confirm_download"));
					$("#dsc_balance_msg").append("<br />" + "<div style=\"font-size:12px;margin-top:15px;\">" + $.t("store.need_sd_to_play1") + "<br />" + $.t("store.need_sd_to_play2") + "</div>");
				}
				else{
					$("#dsc_balance_msg").html("<br /><br />" + $.t("store.confirm_download"));
				}
				
				$("#dsc_balance_price").html(balance_price_num);
				$("#dsc_balance_result").html(balance_result_num);

				$("#dsc_balance_btn_confirm").show();
				$("#dsc_balance_btn_request").hide();

				$("#dsc_balance").show();
				$("#dsc_blocked").hide();

				if(dsc_price_num[id] != $.t("store.get_it") && dsc_price_num[id] != $.t("store.free")){
					$("#dsc_coins").show();
					$("#dsc_coins_txt").show();
				}

				$("#dsc_bottom_row").css("overflow-x", "");
				$("#dsc_bottom_row").css("overflow-y", "");
				$("#dsc_screenshot_frame").css("overflow-x", "");
				$("#dsc_screenshot_frame").css("overflow-y", "");
				$("#dsc_bottom_row").hide();
				break;
			}
			case "no":{
				if(balance_result_num < 0){
					$("#dsc_balance_msg").html($.t("store.not_enough_coins"));
					$("#dsc_balance_msg").css("color", "red");
					$("#dsc_balance_btn_confirm").hide();
					$("#dsc_balance_btn_request").show();
				}
				else{
					$("#dsc_balance_btn_confirm").show();
					$("#dsc_balance_btn_request").hide();
				}
				
				if(dsc_badge[id] == "sdcard"){
					$("#dsc_balance_msg").append("<br />" + "<div style=\"font-size:12px;margin-top:15px;\">" + $.t("store.need_sd_to_play1") + "<br />" + $.t("store.need_sd_to_play2") + "</div>");
				}
				else{
					$("#dsc_balance_msg").prepend("<br />");	
				}
				$("#dsc_balance").show();
				$("#dsc_blocked").hide();

				if(dsc_price_num[id] != $.t("store.get_it") && dsc_price_num[id] != $.t("store.free")){
					$("#dsc_coins").show();
					$("#dsc_coins_txt").show();
				}

				$("#dsc_bottom_row").css("overflow-x", "");
				$("#dsc_bottom_row").css("overflow-y", "");
				$("#dsc_screenshot_frame").css("overflow-x", "");
				$("#dsc_screenshot_frame").css("overflow-y", "");
				$("#dsc_bottom_row").hide();
				break;
			}
			case "goo":{
				$("#dsc_blocked").show();
				$("#dsc_balance").hide();
				$("#dsc_bottom_row").css("overflow-x", "");
				$("#dsc_bottom_row").css("overflow-y", "");
				$("#dsc_screenshot_frame").css("overflow-x", "");
				$("#dsc_screenshot_frame").css("overflow-y", "");
				$("#dsc_bottom_row").hide();

				$("#dsc_pricebtn").hide();
				$("#dsc_installbtn").hide();
				$("#dsc_freebtn").hide();
				$("#dsc_gooinstallbtn").hide();
				$("#dsc_blocked_btn_ok").hide();
			}
			default:{}
		}

			$("#dsc_pricebtn").hide();
			$("#dsc_installbtn").hide();
			$("#dsc_gooinstallbtn").hide();
			$("#dsc_freebtn").hide();
}

function directCheckout(e){
	var itemid = $(e).attr("id");

	itemid = itemid.split("_")[1];
	window.location.href = "/store.html?DIRECT:" + dsc_storeid[itemid];
	return;

	$("#dsc_id").show();
	$(".button").hide();

	$("#dsc_bottom_row").hide();
	// $("#dsc_balance").show();

	loadDsc(itemid);
	var id = itemid.split("_")[1];

	result = getStatus(id);


	switch(result){
		case "goo":{
			$("#dsc_gooinstallbtn").click();
			break;
		}
		case "install":{
			// $("#dsc_installbtn").click();
			break;
		}
		case "no":{
			$("#dsc_pricebtn").click();
			break;
		}
		case "free":{
			$("#dsc_freebtn").click();
			break;
		}
		case "blocked":{
			if(dsc_price_num[id] == $.t("store.get_it")){
				$("#dsc_gooinstallbtn").click();
			}
			else if(dsc_price_num[id] == $.t("store.free")){
				$("#dsc_freebtn").click();
			}
			else{
				$("#dsc_pricebtn").click();
			}
			break;
		}
		case "awaiting":{
			break;
		}
		default:{}
	}

}

function backToDsc(id){
	$("#dsc_balance").hide();
	$("#dsc_bottom_row").css("overflow-x", "hidden");
	$("#dsc_bottom_row").css("overflow-y", "scroll");
	$("#dsc_screenshot_frame").css("overflow-x", "scroll");
	$("#dsc_screenshot_frame").css("overflow-y", "hidden");
	$("#dsc_bottom_row").show();
	$("#dsc_coins").hide();
	$("#dsc_coins_txt").hide();
	$("#dsc_blocked").hide();

	if(dsc_price_num[id] == $.t("store.get_it")){
		$("#dsc_pricebtn").hide();
		$("#dsc_installbtn").hide();
		$("#dsc_freebtn").hide();
		$("#dsc_gooinstallbtn").show();
		if($("#dsc_approval").is(":visible")){
			$("#dsc_gooinstallbtn").hide();
		}
	}
	else if(dsc_price_num[id] == $.t("store.free")){
		$("#dsc_pricebtn").hide();
		$("#dsc_installbtn").hide();
		$("#dsc_freebtn").show();
		$("#dsc_gooinstallbtn").hide();
	}
	else{
		$("#dsc_pricebtn").show();
		$("#dsc_installbtn").hide();
		$("#dsc_freebtn").hide();
		$("#dsc_gooinstallbtn").hide();
	}
}

function confirmPayment(storeid){

	coins_total = balance_result_num;

	loadCoins();

	$("#dsc_balance").hide();
	$("#dsc_bottom_row").css("overflow-x", "hidden");
	$("#dsc_bottom_row").css("overflow-y", "scroll");
	$("#dsc_screenshot_frame").css("overflow-x", "scroll");
	$("#dsc_screenshot_frame").css("overflow-y", "hidden");
	$("#dsc_bottom_row").show();
	$("#dsc_coins").hide();
	$("#dsc_coins_txt").hide();

	requestApproval();
}

function requestApproval(){
	// send request and change status to "awaiting approval"
	// if blocked, keep "blocked" until parent changes it

	if($("#dsc_blocked").is(":visible")){
		if(result != "blocked"){
			$("#dsc_blocked_btn_back").hide();
			$("#dsc_blocked_btn_request").hide();
			$("#dsc_blocked_btn_ok").show();
			$("#dsc_approval").show();
		}
	}
	else{
		$("#dsc_approval").show();
	}

}

function getStatus(id){

	result = "";

	switch(dsc_purchase_status[id]){
		case "NORMAL":{
			if(dsc_price_num[id] == $.t("store.get_it")){
				result = "goo"; // case 1
			}
			else if(dsc_price_num[id] == $.t("store.free")){
				result = "free"; // case 4
			}
			else{
			result = "no"; // case 2, 3
			}
			break;
		}
		case "PURCHASED":{
			result = "install";
			break;
		}
		case "PENDING":{
			result = "awaiting";
			break;
		}
		case "BLOCKED":{
			result = "blocked";
			break;
		}
		default: {}
	}

	switch (result){
		case "goo":{
			$("#dsc_pricebtn").hide();
			$("#dsc_approval").hide();
			$("#dsc_gpLogo").show();
			$("#dsc_installbtn").hide();
			$("#dsc_freebtn").hide();
			$("#dsc_gooinstallbtn").show();
			break;
		}
		case "install":{
			$("#dsc_pricebtn").hide();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").show();
			$("#dsc_freebtn").hide();
			$("#dsc_gooinstallbtn").hide();
			break;
		}
		case "no":{
			$("#dsc_pricebtn").show();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").hide();
			$("#dsc_freebtn").hide();
			$("#dsc_gooinstallbtn").hide();
			break;
		}
		case "free":{
			$("#dsc_pricebtn").hide();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").hide();
			$("#dsc_freebtn").show();
			$("#dsc_gooinstallbtn").hide();
			break;
		}
		case "awaiting":{
			$("#dsc_pricebtn").hide();
			$("#dsc_approval").show();
			$("#dsc_installbtn").hide();
			$("#dsc_freebtn").hide();
			$("#dsc_gooinstallbtn").hide();
			break;
		}
		case "blocked":{
			$("#dsc_pricebtn").show();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").hide();
			$("#dsc_freebtn").hide();
			$("#dsc_gooinstallbtn").hide();
			$("#dsc_blocked_txt").html($.t("store.no_permission_meep_store"));
			if(dsc_price_num[id] == $.t("store.get_it")){
				$("#dsc_pricebtn").hide();
				$("#dsc_gooinstallbtn").show();
			}
			else if(dsc_price_num[id] == $.t("store.free")){
				$("#dsc_pricebtn").hide();
				$("#dsc_freebtn").show();
			}
			break;
		}
		default: {
			$("#dsc_pricebtn").hide();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").hide();
			$("#dsc_freebtn").hide();
			$("#dsc_gooinstallbtn").hide();
		}
	}
	return result;
}

function getSingleStatus(ObjectId){

	result = "";
	result = "goo";
	// result = "install";
	// result = "no";
	// result = "free";
	// result = "awaiting";
	// result = "blocked";

	switch (result){
		case "goo":{
			$("#dsc_pricebtn").hide();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").hide();
			$("#dsc_gooinstallbtn").show();
			break;
		}
		case "install":{
			$("#dsc_pricebtn").hide();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").show();
			$("#dsc_gooinstallbtn").hide();
			break;
		}
		case "no":{
			$("#dsc_pricebtn").show();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").hide();
			$("#dsc_gooinstallbtn").hide();
			break;
		}
		case "free":{
			$("#dsc_pricebtn").hide();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").show();
			$("#dsc_gooinstallbtn").hide();
			break;
		}
		case "awaiting":{
			$("#dsc_pricebtn").hide();
			$("#dsc_approval").show();
			$("#dsc_installbtn").hide();
			$("#dsc_gooinstallbtn").hide();
			break;
		}
		case "blocked":{
			$("#dsc_pricebtn").show();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").hide();
			$("#dsc_gooinstallbtn").hide();
			if(dsc_price_num[id] == $.t("store.get_it") || dsc_price_num[id] == $.t("store.free")){
				$("#dsc_pricebtn").hide();
				$("#dsc_installbtn").show();
			}
			break;
		}
		default: {
			$("#dsc_pricebtn").hide();
			$("#dsc_approval").hide();
			$("#dsc_installbtn").hide();
			$("#dsc_gooinstallbtn").hide();
		}
	}
	return result;
}

function loadScreenshots(touch_no){

	var screenshotTag = "";

	for (var id=0; id<dsc_screenshots[touch_no].length; id++){
		screenshotTag += "<div class='screenshot' id='dsc_screenshot" + (id) + "'><a href='" + webStorePrefix + dsc_screenshots[touch_no][id] + "' rel='lightbox[" + dsc_title_txt[touch_no] + "]'><img border='0' src='" + webStorePrefix + dsc_screenshots[touch_no][id] + "' alt='screenshot' height='110' onload='expandContainer(this);'/></a></div>";				
	}

	if(dsc_screenshots[touch_no].length == 0){
		screenshotTag += "<div class='screenshot' id='dsc_screenshot" + (id) + "'><a href='" + webStorePrefix + dsc_screenshots[touch_no][id] + "' rel='lightbox[" + dsc_title_txt[touch_no] + "]'><img border='0' src='" + "category/" + $.i18n.lng().split('-')[0] +"/meepstore_noimage.png" + "' alt='screenshot' height='110' onload='expandContainer(this);'/></a></div>";
	}

	return screenshotTag;
}

function prevSS(){
	// var screenshotLeft = $("#dsc_block_screenshot").position().left;
	var screenshotLeft = $("#dsc_screenshot_frame").scrollLeft();

	$("#dsc_screenshot_frame").animate({scrollLeft: screenshotLeft - 220}, 500);
}

function nextSS(){
	var screenshotLeft = $("#dsc_screenshot_frame").scrollLeft();

	$("#dsc_screenshot_frame").animate({scrollLeft: screenshotLeft + 220}, 500);
}

function loadHome(){
	$.i18n.init({
		ns: { namespaces: ["mt.shared"], defaultNs: "mt.shared"},
		useLocalStorage: false,
		debug: false
	}, function () {
		if (window.location.hash.length === 41){
			sessionStorage.authToken = "OST " + window.location.hash.substring(1);
			$("#error").hide();
		}
		else if (sessionStorage.authToken && sessionStorage.authToken.length === 44) {
			$("#error").hide();
		}
		else {
			// show error page
			$("#error").html("<div>" + $.t("store.not_authorized_to_store1") + "<br/>" + $.t("store.not_authorized_to_store2") + "<br/><br/>" + $.t("store.not_authorized_to_store3") + "</div>");
			$("#error").css("background-image", "url(/main_menu/meepstore_cannotaccess.png)");
			$("#error").show();
		}


		$("#movie_caption").html($.t("store.movie"));
		$("#movie_caption").attr("data-lang", $.i18n.lng().split('-')[0]);
		$("#music_caption").html($.t("store.music"));
		$("#apps_caption").html($.t("store.apps"));
		$("#game_caption").html($.t("store.game"));
		$("#ebook_caption").html($.t("store.e_book"));
		$("#more_left").html($.t("store.more"));
		$("#more_right").html($.t("store.more"));
		$("#searchbar_txt").html($.t("store.search"));
		$("#coins_logo_id").css("background-image", "url(/category/" + $.i18n.lng().split('-')[0] + "/meep_market_code.png)");
		
		loadCoins();

		$.ajax({type: 'GET',
				url: "https://portal.meeptablet.com/1/store/welcome",
				contentType: 'application/json',
				dataType: "json",
				async: true,
				cache: false,
				headers: getHttpHeader(),
				crossDomain: true,
				data: "",
				success: function(data) {
					if (data.code === 200) {
						console.log(data);
						coins_total = data.coins;
						sessionStorage.coins = data.coins;
						sessionStorage.visitor = data.visitor;
						loadTV(data.banner);

						$('#tv1').cycle({
							fx:     'scrollHorz',
							speed:  350,
							timeout: 10000,
							prev:   '#arrow_left',
							next:   '#arrow_right'
						});

						loadCoins();
					}
				},
				error: function(httpres) {
					switch (httpres.status) {
						case 400:
						case 403:
						case 404:
						case 406:
							$("#error").html("<div>" + $.t("store.not_authorized_to_store1") + "<br/>" + $.t("store.not_authorized_to_store2") + "<br/><br/>" + $.t("store.not_authorized_to_store3") + "</div>");
							$("#error").css("background-image", "url(/main_menu/meepstore_cannotaccess.png)");
							$("#error").show();
							break;
						case 500:
						case 503:
							// $("#movie_caption").html(httpres.status);
							break;
					}
				}
		});

		$("#popup_id").hide();

		// home (movie, music not available)
		$(".comingsoon").html($.t("store.coming_soon"));

		$('img').each(function() {
			image_height = $(this).height();
			image_width = $(this).width();
			scale = image_width/image_height;
			newScale = (218/158);

			if(scale > newScale){
				new_h = 218/scale;
				$(this).css('width', '218px');
				$(this).css('height', new_h + 'px');
				 margin_top = (158 - new_h) / 2;
				$(this).css('margin-top', margin_top + 'px');
			}
			else{
				new_w = 158*scale;
				$(this).css('width', new_w + 'px');
				$(this).css('height', '158px');
				margin_left = (218 - new_w) / 2;
				$(this).css('margin-left', margin_left + 'px');
			}
		});
	});
}


function loadTV(ds){
	for(var i=0; i<ds.length; i++){
		$('#tv1').append("<a href='" + "store.html?" + ds[i]['id'] + "'><img src='" + ds[i]['image'] + "'/></a>");
		// $('#tv1').append("<a href='" + "store.html?503d7fc08500fa128000021f'><img src='" + ds[i]['image'] + "'/></a>");
		// $('#tv1').append("<img src='" + ds[i].image + "'/>");
	}
}

function loadCoupon1(){

	var popup = "";

	popup += "<div class='popup' id='popup_bg' onclick=''></div>";

	popup += "<div id='popup_s'><div id='popup_s_title'><div id='popup_s_title_txt'>" + $.t("store.enter_coupon_code") + "</div></div><div id='popup_s_content1'><input type='text' id='redeemCode' maxlength='19'/></div><div id='popup_s_content2'><div id='popup_s_content2_txt'></div></div><div id='popup_s_btn1' onclick='backToHome()'><div id='popup_s_btn1_txt' onclick='backToHome()'>" + $.t("store.back") +"</div></div><div id='popup_s_btn2' onclick='checkRedeemCode()'><div id='popup_s_btn2_txt'>" + $.t("store.confirm") + "</div></div>";

	$("#popup_id").html(popup);
	$("#popup_id").show();
}

function loadCoupon2(couponCode, couponType, couponResult){
	var popup2 = "";

	if(couponType == "contents"){

		for (var i=0; i<couponResult.length; i++){
			app_avail_title[i] = couponResult[i].name.Left(11);
			app_avail_icon[i] = couponResult[i].icon;
		}

		// coupon with 3 apps
		if(couponResult.length == 3){
			popup_b_app2_txt = app_avail_title[0].Left(11);
			popup_b_app1_txt = app_avail_title[1].Left(11);
			popup_b_app3_txt = app_avail_title[2].Left(11);

			popup2 += "<div class='popup' id='popup_bg' onclick=''></div>";

			popup2 += "<div id='popup_b'><div id='popup_b_title'><div id='popup_b_title_txt'>" + $.t("store.can_redeem") + "</div></div><div id='popup_content_box'><div id='popup_b_container1'><div id='popup_app_icon_bg'></div><div id='popup_b_app1' style='background-image:url(" + webStorePrefix + app_avail_icon[1] + ")'></div><div id='popup_b_app1_txt'>" + popup_b_app1_txt + "</div></div>";

			popup2 += "<div id='popup_plus_sign1'>+</div>";

			popup2 += "<div id='popup_b_container2'><div id='popup_app_icon_bg'></div><div id='popup_b_app2' style='background-image:url(" + webStorePrefix + app_avail_icon[0] + ")'></div><div id='popup_b_app2_txt'>" + popup_b_app2_txt + "</div></div>";

			popup2 += "<div id='popup_plus_sign2'>+</div>";

			popup2 += "<div id='popup_b_container3'><div id='popup_app_icon_bg'></div><div id='popup_b_app3' style='background-image:url(" + webStorePrefix + app_avail_icon[2] + ")'></div><div id='popup_b_app3_txt'>" + popup_b_app3_txt + "</div></div>";

			popup2 += "</div>";

			// popup2 += "<div id='popup_b_content2'><div id='popup_b_content2_txt'>" + $.t("store.code_accepted_num_times") + "<br/><br/>" + app_avail_num + $.t("store.more_times") + "</div></div>";

			popup2 += "<div id='popup_b_btn_box'><div id='popup_b_btn1' onclick='backToHome()'><div id='popup_b_btn1_txt'>" + $.t("store.back") + "</div></div><div id='popup_b_btn2' onclick='confirmCoupon(\"" + couponCode + "\")'><div id='popup_b_btn2_txt'>" + $.t("store.confirm") + "</div></div><div id='popup_b_btn3' onclick='confirmReceived()'><div id='popup_b_btn3_txt'>" + $.t("store.ok") + "</div></div></div>";
		}
		// coupon with 1 app
		else{
			popup_b_app2_txt = app_avail_title[0].Left(11);

			popup2 += "<div class='popup' id='popup_bg' onclick=''></div>";

			popup2 += "<div id='popup_b'><div id='popup_b_title'><div id='popup_b_title_txt'>" + $.t("store.can_redeem") + "</div></div><div id='popup_content_box'><div id='popup_b_container1' style='visibility:hidden;'></div>";

			popup2 += "<div id='popup_plus_sign1' style='visibility:hidden;'>+</div>";

			popup2 += "<div id='popup_b_container2'><div id='popup_app_icon_bg'></div><div id='popup_b_app2' style='background-image:url(" + webStorePrefix + app_avail_icon[0] + ")'></div><div id='popup_b_app2_txt'>" + popup_b_app2_txt + "</div></div>";

			popup2 += "<div id='popup_plus_sign2' style='visibility:hidden;'>+</div>";

			popup2 += "<div id='popup_b_container3' style='visibility:hidden;'></div>";

			popup2 += "</div>";

			// popup2 += "<div id='popup_b_content2'><div id='popup_b_content2_txt'>" + $.t("store.code_accepted_num_times") + "<br/><br/>" + app_avail_num + $.t("store.more_times") + "</div></div>";

			popup2 += "<div id='popup_b_btn_box'><div id='popup_b_btn1' onclick='backToHome()'><div id='popup_b_btn1_txt'>" + $.t("store.back") + "</div></div><div id='popup_b_btn2' onclick='confirmCoupon(\"" + couponCode + "\")'><div id='popup_b_btn2_txt'>" + $.t("store.confirm") + "</div></div><div id='popup_b_btn3' onclick='confirmReceived()'><div id='popup_b_btn3_txt'>" + $.t("store.ok") + "</div></div></div>";
		}
	}
	else if(couponType == "coins"){
		popup_b_content1_txt = couponResult + $.t("store.space_coins");
		popup_b_content2_txt = "";
		coins_avail = couponResult;

		popup2 += "<div class='popup' id='popup_bg' onclick=''></div>";

		popup2 += "<div id='popup_b'><div id='popup_b_title'><div id='popup_b_title_txt'>" + $.t("store.can_redeem") + "</div></div><div id='popup_b_content2'><div id='popup_b_content2_txt'></div></div><div id='popup_content_box'><div id='popup_b_content_icon'></div><div id='popup_b_content1'><div id='popup_b_content1_txt'>" + popup_b_content1_txt + "</div></div></div><div id='popup_b_btn_box'><div id='popup_b_btn1' onclick='backToHome()'><div id='popup_b_btn1_txt'>" + $.t("store.back") + "</div></div><div id='popup_b_btn2' onclick='confirmCoupon(\"" + couponCode + "\")'><div id='popup_b_btn2_txt'>" + $.t("store.confirm") + "</div></div><div id='popup_b_btn3' onclick='confirmReceived()'><div id='popup_b_btn3_txt'>" + $.t("store.ok") + "</div></div></div>";
	}

	$("#popup_id").html(popup2);
	if(couponType == "coins"){
		$("#popup_b_content2").css("margin-top", "22px");
		$("#popup_b_content2").css("visibility", "hidden");
		$("#popup_b_btn_box").css("margin-top", "136px");
	}
	$("#popup_b_btn3").hide();
	$("#popup_id").show();
}

function confirmCoupon(couponCode){
	$.ajax({type: 'GET',
			url: "https://portal.meeptablet.com/1/store/coupon/confirmredeem/" + couponCode,
			contentType: 'application/json',
			dataType: "json",
			async: true,
			cache: false,
			headers: getHttpHeader(),
			crossDomain: true,
			data: "",
			success: function(data) {
				if (data.type === "contents") {		
					confirmCouponApp(data.remaining);
				} else if (data.type === "coins") {
					confirmCouponCoins();
				}
			},
			error: function(httpres) {
				switch (httpres.status) {
					case 400:
					case 403:
					case 404:
					case 410:
					case 500:
					case 503:
						data = JSON.parse(httpres.responseText);
						break;
				}
			}
	});
}

function confirmCouponApp(app_avail_num){

	// popup_b_content2_txt = popup_txt_type6;

	var popup2 = "";
	// popup_txt_type6 = "The code is accepted. You can now redeem:<br/><br/>" + app_avail_num + " more times.";
	// popup_b_content2_txt = popup_txt_type6;

	popup2 += "<div class='popup' id='popup_bg' onclick=''></div>";

	popup2 += "<div id='popup_b'><div id='popup_b_title'><div id='popup_b_title_txt'>" + $.t("store.can_redeem") + "</div></div><div id='popup_b_content2'><div id='popup_b_content2_txt'>" + $.t("store.code_accepted_num_times") + "<br/><br/>" + app_avail_num + $.t("store.more_times") + "</div></div><div id='popup_b_btn_box'><div id='popup_b_btn1' onclick='backToHome()'><div id='popup_b_btn1_txt'>" + $.t("store.back") + "</div></div><div id='popup_b_btn2' onclick='confirmCoupon()'><div id='popup_b_btn2_txt'>" + $.t("store.confirm") + "</div></div><div id='popup_b_btn3' onclick='confirmReceived()'><div id='popup_b_btn3_txt'>" + $.t("store.ok") + "</div></div>";

	// popup2 += "<div id='popup_b'><div id='popup_b_title'><div id='popup_b_title_txt'>" + $.t("store.can_redeem")t + "</div></div><div id='popup_b_content2'><div id='popup_b_content2_txt'>" + $.t("store.code_accepted_num_times") + "<br/><br/>" + app_avail_num + $.t("store.more_times") + "</div></div><div id='popup_content_box'><div id='popup_app_icon_bg'><div id='popup_app1_icon'></div></div><div id='popup_b_app1'><div id='popup_b_app1_txt'>" + popup_b_content1_txt + "</div></div></div><div id='popup_b_btn_box'><div id='popup_b_btn1' onclick='backToHome()'><div id='popup_b_btn1_txt'>" + $.t("store.back") + "</div></div><div id='popup_b_btn2' onclick='confirmCoupon()'><div id='popup_b_btn2_txt'>" + $.t("store.confirm") + "</div></div><div id='popup_b_btn3' onclick='confirmReceived()'><div id='popup_b_btn3_txt'>" + $.t("store.ok") + "</div></div>";

	$("#popup_id").html(popup2);
	$("#popup_b_btn3").css("margin-top", "-140px");
	$("#popup_b_btn1").hide();
	$("#popup_b_btn2").hide();
	$("#popup_b_btn3").show();
}

function confirmCouponCoins(){

	// popup_b_content2_txt = popup_txt_type7;
	$("#popup_b_content2_txt").html($.t("store.code_accepted"));
	$("#popup_b_content2").css("visibility", "visible");

	$("#popup_b_btn1").hide();
	$("#popup_b_btn2").hide();
	$("#popup_b_btn3").show();

	coins_total += coins_avail;
	loadCoins();
}

function confirmReceived(){
	$("#popup_id").hide();
}

function backToHome(){
	$("#popup_id").hide();
}

function checkRedeemCode() {
	var code = $("#redeemCode").val().toUpperCase();

	if (code.length !== 19) {
		$("#popup_s_content2_txt").html($.t("store.wrong_code1") + "<br/>" + $.t("store.wrong_code2"));
		return;
	}

	$.ajax({type: 'GET',
			url: "https://portal.meeptablet.com/1/store/coupon/redeem/" + code,
			contentType: 'application/json',
			dataType: "json",
			async: true,
			cache: false,
			headers: getHttpHeader(),
			crossDomain: true,
			data: "",
			success: function(data) {
				loadCoupon2(code, data.type, data.results);
			},
			error: function(httpres) {
				switch (httpres.status) {
					case 404:
						$("#popup_s_content2_txt").html($.t("store.wrong_code1") + "<br/>" + $.t("store.wrong_code2"));
						break;
					case 409:
						$("#popup_s_content2_txt").html($.t("store.used_code"));
						break;
					case 410:
						$("#popup_s_content2_txt").html($.t("store.used_code"));
						break;
					case 400:
					case 403:
					case 500:
					case 503:
						data = JSON.parse(httpres.responseText);
						break;
				}
			}
	});
}

function loadStore(){
	$("#dsc_id").hide();

	$("#store_id").after("<div id='store_id_txt'><a href='store_" + store + ".html'><span></span></a>" + $.t("store." + storelabel) + "</div>");
	$(".shelf").append($("<div class='label'><div id='" + store + "'>" + $.t("store." + storelabel) + "</div></div>"));

	loadCoins();

	$.ajax({type: 'GET',
			url: "https://portal.meeptablet.com/1/store/welcome",
			contentType: 'application/json',
			dataType: "json",
			async: true,
			cache: false,
			headers: getHttpHeader(),
			crossDomain: true,
			data: "",
			success: function(data) {
				if (data.code === 200) {
					coins_total = data.coins;
					sessionStorage.coins = data.coins;
					sessionStorage.visitor = data.visitor;
					loadCoins();
				}
			},
			error: function(httpres) {
				switch (httpres.status) {
					case 400:
					case 403:
					case 404:
					case 406:
					case 500:
					case 503:
						break;
				}
			}
	});
}


function loadNextShelf(){

	for (var i=0; i<((dsc_title_txt.length - loaded_container_count) / shelf_slot); i++) {
		loadShelf(loaded_shelf_count++);
	}

	for (;loaded_container_count < dsc_title_txt.length; loaded_container_count++) {
		loadContainer(loaded_container_count);
	}

	loaded_count++;
}


function loadShelf(i){

		$(".shelf").css("width", "+=122px");
		$(".shelf").append($("<div class='shelf_start' id='shelf_start_" + (i) + "'></div>"));

		// if ( i == shelf_count-1 && (Math.ceil(array_length%shelf_slot) != 0)){

		if(array_length < 12){
			var shelf_middle_add_count = Math.ceil((array_length % shelf_slot) / 2);
			$(".shelf").css("width", "+=" + (shelf_middle_add_count * 146) + "px");

		    var shelf_end_shift = shelf_middle_add_count * 146 - 121;
			$(".shelf").append($("<div class='shelf_end' id='shelf_end_" + (i) + "' style='margin-left:" + shelf_end_shift + "px'></div>"));

			for (var j=0; j<shelf_middle_add_count; j++ ) {
					var backshift = (shelf_middle_add_count - j ) * 146 + 60;
					$(".shelf").append($("<div class='shelf_middle' id='shelf_middle_" + count++ +"' style='margin-left:" + (-backshift) + "px'></div>"));
			}
		} else {
			var shelf_end_shift = shelf_slot / 2 * 146 - 121;
			$(".shelf").css("width", "+=" + (146 * 6) + "px");
			$(".shelf").append($("<div class='shelf_end' id='shelf_end_" + (i) + "' style='margin-left:" + shelf_end_shift + "px'></div>"));

			for (var k=0; k<(shelf_slot/2); k++ ) {
					var backshift = (shelf_slot/2 - k) * 146 + 60;
					$(".shelf").append($("<div class='shelf_middle' id='shelf_middle_" + count++ +"' style='margin-left:" + (-backshift) + "px'></div>"));
			}
		}
		$(".shelf").css("width", "+=20px");
}

function loadContainer(id){

		var content = "<div class='container_" + store + "' id='container_" + store + "_" + (id) + "'><div class='" + store + "label' id='" + store + "label_" + (id) + "' style='background-image:url(" + webStorePrefix + dsc_icon[id] + ")' onclick='showDsc(this);' ></div>";
		// var content = "<div class='container_" + store + "' id='container_" + store + "_" + (id) + "'><div class='" + store + "label' id='" + store + "label_" + (id) + "' style='background-image:url(" + webStorePrefix + dsc_icon[id] + ")' onclick='goDsc(id);' ></div><div class='" + store + "labelshadow' id='" + store + "labelshadow_" + (id) + "' onclick='goDsc(id);' >";<a href="store.html?+  dsc_storeid[id]">  store.html?"
				
		content += "<div class='" + store + "labelshadow' id='" + store + "labelshadow_" + (id) + "' onclick='showDsc(this);'></div>";

		content += "<div class='badge' id='badge_" + (id) + "' onclick='showDsc(this);'></div>";
		
		content += "<div class='namelabel' id='namelabel_" + (id) + "' onclick='showDsc(this);' ><div id='namelabel_txt" + (id) +"'>" + dsc_title_txt[id].Left(12) + "</div></div><div class='pricelabel' id='pricelabel_" + (id) + "' onclick='directCheckout(this);'><div id='pricetxt_" + id + "'>" + dsc_price_num[id] + "</div></div><div class='goolabel' id='goolabel_" + (id) + "' data-lang='" + $.i18n.lng().split('-')[0] + "' onclick='directCheckout(this);'><div id='goolabeltxt_" + id + "'>" + $.t("store.get_it") + "</div></div><div class='freelabel' id='freelabel_" + (id) + "' onclick='directCheckout(this);'><div id='freelabeltxt_" + id + "'>" + $.t("store.free") + "</div></div><div class='blanklabel' id='blanklabel_" + id + "'><div id='blanklabeltxt_" + id + "'>" + $.t("store.coming_soon")  + "</div></div>";
		// content += "</div><div class='namelabel' id='namelabel_" + (id) + "' onclick='goDsc(id);' ><div id='namelabel_txt" + (id) +"'>" + dsc_title_txt[id].Left(12) + "</div></div><div class='pricelabel' id='pricelabel_" + (id) + "' onclick='directCheckout(this);'><div id='pricetxt_" + id + "'>" + dsc_price_num[id] + "</div></div><div class='goolabel' id='goolabel_" + (id) + "' onclick='directCheckout(this);'><div id='goolabeltxt_" + id + "'>" + $.t("store.get_it") + "</div></div><div class='freelabel' id='freelabel_" + (id) + "' onclick='directCheckout(this);'><div id='freelabeltxt_" + id + "'>" + $.t("store.free") + "</div></div>";

		// if (dsc_badge[id] == "bestseller"){
			// content += "<div class='bestseller' id='bestseller_" + (id) + "' onclick='showDsc(this);'><div>" + "&nbsp&nbsp" + $.t("store.bestseller_txt1") + "<br/>" + $.t("store.bestseller_txt2") + "</div></div>";
			// content += "<div class='bestseller' id='bestseller_" + (id) + "' onclick='showDsc(this);'></div>";
		// }
		// if (dsc_badge[id] == "hotitem"){
			// content += "<div class='hotitem' id='hotitem_" + (id) + "' onclick='showDsc(this);'><div>" + $.t("store.hotitem_txt1") + "<br/>" + $.t("store.hotitem_txt2") + "</div></div>";
			// content += "<div class='hotitem' id='hotitem_" + (id) + "' onclick='showDsc(this);'></div>";
		// }
		// if (dsc_badge[id] == "sale"){
			// content += "<div class='sale' id='sale_" + (id) + "' onclick='showDsc(this);'><div>" + $.t("store.sale_txt") + "</div></div>";
			// content += "<div class='sale' id='sale_" + (id) + "' onclick='showDsc(this);'></div>";
		// }
		// if (dsc_badge[id] == "accessory"){
			// content += "<div class='accessory' id='accessory_" + (id) + "' onclick='showDsc(this);'><div>" + $.t("store.accessory_txt1") + "<br/>" + $.t("store.accessory_txt2") + "</div></div>";
			// content += "<div class='accessory' id='accessory_" + (id) + "' onclick='showDsc(this);'></div>";
		// }

		content += "</div>";

		var slot = "#shelf_middle_" + Math.floor((id)/2 );
		$(slot).append($(content));

		if (dsc_badge[id] == "bestseller"){
			$("#badge_" + id).css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_bestseller.png)");
		}
		if (dsc_badge[id] == "hotitem"){
			$("#badge_" + id).css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_hotitem.png)");
		}
		if (dsc_badge[id] == "sale"){
			$("#badge_" + id).css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_sale.png)");
		}
		if (dsc_badge[id] == "accessory"){
			$("#badge_" + id).css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_accessory.png)");
		}
		if (dsc_badge[id] == "sdcard"){
			$("#badge_" + id).css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_sdcard.png)");
		}

		swapPriceLabel(id);
}


function swapPriceLabel(id){
		//show different labels depending on price
		
		if(dsc_price_num[id] == $.t("store.coming_soon")){
			$("#goolabel_" + (id)).hide();
			$("#freelabel_" + (id)).hide();
			$("#pricelabel_" + (id)).hide();
			$("#blanklabel_" + (id)).show();
			$("#" + store + "label_" + id).attr("onclick", "");
			$("#" + store + "labelshadow_" + id).attr("onclick", "");
			$("#namelabel_" + id).attr("onclick", "");
			$("#badge_" + id).attr("onclick", "");
		}
		else if(dsc_price_num[id] == $.t("store.get_it")){
			$("#goolabel_" + (id)).show();
			$("#freelabel_" + (id)).hide();
			$("#pricelabel_" + (id)).hide();
			$("#blanklabel_" + (id)).hide();
		}
		else if(dsc_price_num[id] == $.t("store.free")){
			$("#goolabel_" + (id)).hide();
			$("#freelabel_" + (id)).show();
			$("#pricelabel_" + (id)).hide();
			$("#blanklabel_" + (id)).hide();
		}
		else{
			$("#goolabel_" + (id)).hide();
			$("#freelabel_" + (id)).hide();
			$("#pricelabel_" + (id)).show();
			$("#blanklabel_" + (id)).hide();
		}
}

function loadDsc(itemid){
	var app_shelf_id = new Array();

	app_shelf_id = itemid.split("_");

	var touch_type = app_shelf_id[0];
	var touch_no = app_shelf_id[1];

	var screenshotTag = loadScreenshots(touch_no);

	var dsc = "";
	var string = "";

	dsc = "<div id='dsc_transition'><div id='dsc_popup'></div>";

	dsc += "<div id='dsc_top_row'>"

	// dsc += "<div id='dsc_block_icon' ><div id='dsc_codeblock_" + store.toLowerCase() + "_bg'></div><div id='dsc_codeblock_" + store.toLowerCase() + "' style='background-image:url(" + webStorePrefix + dsc_icon[touch_no] + ")'  onclick='closeDsc()'></div>";
	dsc += "<div id='dsc_block_icon' ><div id='dsc_codeblock_" + store.toLowerCase() + "_bg'></div><div id='dsc_codeblock_" + store.toLowerCase() + "' style='background-image:url(" + webStorePrefix + dsc_icon[touch_no] + ")'></div>";

	if (dsc_badge[touch_no] == "bestseller"){
			// dsc += "<div class='bestseller' id='dsc_bestseller'><div>" + "&nbsp&nbsp" + $.t("store.bestseller_txt1") + "<br/>" + $.t("store.bestseller_txt2") + "</div></div>";
			dsc += "<div class='bestseller' id='dsc_bestseller'></div>";
	}
	if (dsc_badge[touch_no] == "hotitem"){
			// dsc += "<div class='hotitem' id='dsc_hotitem'><div>" + $.t("store.hotitem_txt1") + "<br/>" + $.t("store.hotitem_txt2") + "</div></div>";
			dsc += "<div class='hotitem' id='dsc_hotitem'></div>";
	}
	if (dsc_badge[touch_no] == "sale"){
			// dsc += "<div class='sale' id='dsc_sale'><div>" + $.t("store.sale_txt") + "</div></div>";
			dsc += "<div class='sale' id='dsc_sale'></div>";
	}
	if (dsc_badge[touch_no] == "accessory"){
			// dsc += "<div class='accessory' id='dsc_accessory'><div>" + $.t("store.accessory_txt1") + "<br/>" + $.t("store.accessory_txt2") + "</div></div>";
			dsc += "<div class='accessory' id='dsc_accessory'></div>";
	}
	if (dsc_badge[touch_no] == "sdcard"){		
			dsc += "<div class='sdcard' id='dsc_sdcard'></div>";
	}

	dsc += "</div>";

	dsc += "<div id='dsc_block_info'><div id='dsc_title'>" + dsc_title_txt[touch_no] + "</div>	<div id='dsc_developer'>" + dsc_developer_txt[touch_no] + "</div><div id='dsc_size'>" + dsc_size_txt[touch_no] + $.t("store.mb") + "</div>";

	dsc += "<div id='dsc_pricebtn' onclick='checkout(" + touch_no + ")'><div id='dsc_price'>" + dsc_price_num[touch_no] + "</div></div><div id='dsc_installbtn' onclick='requestUpdateStatus(" + touch_no +")'><div id='dsc_install_txt'>" + $.t("store.install") + "</div></div><div id='dsc_freebtn' onclick='checkout(" + touch_no +")'><div id='dsc_free_txt'>" + $.t("store.free") + "</div></div><div id='dsc_gooinstallbtn' onclick='checkout(" + touch_no + ")'><div id='dsc_gooinstall_txt' data-lang='" + $.i18n.lng().split('-')[0] + "'>" + $.t("store.get_it") + "</div></div>";

	dsc += "<div id='dsc_approval'>" + $.t("store.awaiting_txt") + "</div><div id='dsc_coins'></div><div id='dsc_coins_txt'>" + dsc_price_num[touch_no] + "</div></div>";

	dsc += "<div id='dsc_block_award'><div id='dsc_award' style='background-image:url(" + getAwardUrl(dsc_recommends[touch_no]) + ")'></div></div>";

	dsc += "</div></div>";

	dsc += "<div id='dsc_bottom_row' style='overflow-x: hidden; overflow-y: scroll' ontouchmove='event.stopPropagation()'>";

	dsc += "<div id='dsc_screenshot_bg'><div id='dsc_arrow_left' onclick='prevSS()'></div><div id='dsc_screenshot_frame' style='overflow-x: scroll; overflow-y: hidden'>";

	dsc += "<div id='dsc_block_screenshot' style='width: 0px'>" + screenshotTag;

	dsc += "</div></div>";

	dsc += "<div id='dsc_arrow_right' onclick='nextSS()'></div></div>";

	dsc += "<div id='dsc_word' >" + dsc_word_txt[touch_no] + "</div>";

	dsc += "<div id='dsc_content'>" + dsc_content_txt[touch_no] + "</div>";

	dsc += "</div>";

	dsc += "</div>";

	dsc += "<div id='dsc_balance'></div>";

	dsc += "<div id='dsc_blocked'></div>";

	$(".dsc").html(dsc);

	if (dsc_badge[touch_no] == "bestseller"){
			$(".bestseller").css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_bestseller.png)");
		}
	if (dsc_badge[touch_no] == "hotitem"){
		$(".hotitem").css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_hotitem.png)");
	}
	if (dsc_badge[touch_no] == "sale"){
		$(".sale").css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_sale.png)");
	}
	if (dsc_badge[touch_no] == "accessory"){
		$(".accessory").css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_accessory.png)");
	}
	if (dsc_badge[touch_no] == "sdcard"){
		$(".sdcard").css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_sdcard.png)");
	}

	// var totalWidth = 0;
	// var img = $("#dsc_block_screenshot").find("img");
	// for (var i=0; i<img.length; i++) {
		// totalWidth += img[i].width;
	// }
	// totalWidth += (img.length - 1) * 30;
	// if( totalWidth >600){
	// $("#dsc_block_screenshot").css('width', totalWidth);}

	balance = "";
	balance_price_num = dsc_price_num[touch_no];
	balance_result_num = coins_total - balance_price_num;

	balance += "<div id='dsc_balance_msg'>" + $.t("store.confirm_purchase") + "</div>"

	balance += "<div id='dsc_calculation'><div id='dsc_balance_coins'>" + $.t("store.coins") + "</div><div id='dsc_balance_total'>" + coins_total + "</div><div id='dsc_balance_minus_sign'>" + balance_minus_sign + "</div><div id='dsc_balance_price'>" + balance_price_num + "</div><div id='dsc_balance_line'></div><div id='dsc_balance_result'>" + balance_result_num + "</div></div>";

	balance += "<div id='dsc_balance_btn_back' onclick='backToDsc(" + touch_no + ")'><div id='dsc_balance_btn_back_txt' onclick='backToDsc(" + touch_no + ")'>" + $.t("store.back") + "</div></div><div id='dsc_balance_btn_confirm' onclick='requestUpdateStatus(" + touch_no + ")'><div id='dsc_balance_btn_confirm_txt'>" + $.t("store.confirm") + "</div></div><div id='dsc_balance_btn_request' onclick='requestCoins()'><div id='dsc_balance_btn_request_txt'>" + $.t("store.request_coins1") + "<br/>" + $.t("store.request_coins2") + "</div></div>";

	balance +="</div>";

	$("#dsc_balance").html(balance);
	$("#dsc_balance").hide();



	blocked = "";

	blocked += "<div id='dsc_blocked_txt'>" + $.t("store.not_directly_on_meep_store1") + "<br/>" + $.t("store.not_directly_on_meep_store2") + "</div>";

	// blocked += "<div id='dsc_blocked_btn_ok' onclick=requestbackToDsc(" + touch_no + ")'><div id='dsc_blocked_btn_ok_txt'>" + $.t("store.ok") + "</div></div>";

	blocked += "<div id='dsc_blocked_btn_box'><div id='dsc_blocked_btn_back' onclick='backToDsc(" + touch_no + ")'><div id='dsc_blocked_btn_back_txt' onclick='backToDsc(" + touch_no + ")'>" + $.t("store.back") + "</div></div><div id='dsc_blocked_btn_request' onclick='requestUpdateStatus(" + touch_no + ")'><div id='dsc_blocked_btn_request_txt'>" + $.t("store.send_request1") + "<br/>" + $.t("store.send_request2") + "</div></div><div id='dsc_blocked_btn_ok' onclick='backToDsc(" + touch_no + ")'><div id='dsc_blocked_btn_ok_txt'>" + $.t("store.ok") + "</div></div></div>";

	$("#dsc_blocked").html(blocked);
	$("#dsc_blocked").hide();

	$("#dsc_coins").hide();
	$("#dsc_coins_txt").hide();

	result = getStatus(touch_no);

	return touch_no;
}

function loadSingleDsc(object){

	touch_no = 0;

	var screenshotTag = loadScreenshots(touch_no);

	var dsc = "";
	var string = "";

	var store = object.type;

	if(store == "app"){
		store = "apps";
	}

	dsc = "<div id='dsc_transition'><div id='dsc_popup'></div>";

	dsc += "<div id='dsc_top_row'>"

	// dsc += "<div id='dsc_block_icon' ><div id='dsc_codeblock_" + store.toLowerCase() + "_bg'></div><div id='dsc_codeblock_" + store.toLowerCase() + "' style='background-image:url(" + webStorePrefix + dsc_icon[touch_no] + ")'  onclick='closeDsc()'></div>";
	dsc += "<div id='dsc_block_icon' ><div id='dsc_codeblock_" + store.toLowerCase() + "_bg'></div><div id='dsc_codeblock_" + store.toLowerCase() + "' style='background-image:url(" + webStorePrefix + dsc_icon[touch_no] + ")' ></div>";

	// if (dsc_badge[touch_no] == "bestseller"){
			// // dsc += "<div class='bestseller' id='dsc_bestseller'><div>" + "&nbsp&nbsp" + $.t("store.bestseller_txt1") + "<br/>" + $.t("store.bestseller_txt2") + "</div></div>";
			// dsc += "<div class='bestseller' id='dsc_bestseller'></div>";
	// }
	// if (dsc_badge[touch_no] == "hotitem"){
			// // dsc += "<div class='hotitem' id='dsc_hotitem'><div>" + $.t("store.hotitem_txt1") + "<br/>" + $.t("store.hotitem_txt2") + "</div></div>";
			// dsc += "<div class='hotitem' id='dsc_hotitem'></div>";
	// }
	// if (dsc_badge[touch_no] == "sale"){
			// // dsc += "<div class='sale' id='dsc_sale'><div>" + $..t("store.sale_txt") + "</div></div>";
			// dsc += "<div class='sale' id='dsc_sale'></div>";
	// }
	// if (dsc_badge[touch_no] == "accessory"){
			// // dsc += "<div class='accessory' id='dsc_accessory'><div>" + $.t("store.accessory_txt1") + "<br/>" + $.t("store.accessory_txt2") + "</div></div>";
			// dsc += "<div class='accessory' id='dsc_accessory'></div>";
	// }
	if(dsc_badge[touch_no] != ""){
		dsc += "<div class='badge' id='dsc_badge'></div>";
	}	
	
	dsc += "</div>";

	dsc += "<div id='dsc_block_info'><div id='dsc_title'>" + dsc_title_txt[touch_no] + "</div>	<div id='dsc_developer'>" + dsc_developer_txt[touch_no] + "</div><div id='dsc_size'>" + dsc_size_txt[touch_no] + $.t("store.mb") + "</div>";

	dsc += "<div id='dsc_pricebtn' onclick='checkout(" + touch_no + ")'><div id='dsc_price'>" + dsc_price_num[touch_no] + "</div></div><div id='dsc_installbtn' onclick='requestUpdateStatus(" + touch_no +")'><div id='dsc_install_txt'>" + $.t("store.install") + "</div></div><div id='dsc_freebtn' onclick='checkout(" + touch_no +")'><div id='dsc_free_txt'>" + $.t("store.free") + "</div></div><div id='dsc_gooinstallbtn' onclick='checkout(" + touch_no + ")'><div id='dsc_gooinstall_txt' data-lang='" + $.i18n.lng().split('-')[0] + "'>" + $.t("store.get_it") + "</div></div>";

	dsc += "<div id='dsc_approval'>" + $.t("store.awaiting_txt") + "</div><div id='dsc_coins'></div><div id='dsc_coins_txt'>" + dsc_price_num[touch_no] + "</div></div>";

	dsc += "<div id='dsc_block_award'><div id='dsc_award' style='background-image:url(" + getAwardUrl(dsc_recommends[touch_no]) + ")'></div></div>";

	dsc += "</div></div>";

	dsc += "<div id='dsc_bottom_row' style='overflow-x: hidden; overflow-y: scroll' ontouchmove='event.stopPropagation()'>";

	dsc += "<div id='dsc_screenshot_bg'><div id='dsc_arrow_left' onclick='prevSS()'></div><div id='dsc_screenshot_frame' style='overflow-x: scroll; overflow-y: hidden'>";

	dsc += "<div id='dsc_block_screenshot' style='width: 0px'>" + screenshotTag;

	dsc += "</div></div>";

	dsc += "<div id='dsc_arrow_right' onclick='nextSS()'></div></div>";

	dsc += "<div id='dsc_word' >" + dsc_word_txt[touch_no] + "</div>";

	dsc += "<div id='dsc_content'>" + dsc_content_txt[touch_no] + "</div>";

	dsc += "</div>";

	dsc += "</div>";

	dsc += "<div id='dsc_balance'></div>";

	dsc += "<div id='dsc_blocked'></div>";

	$(".dsc").html(dsc);

	if (dsc_badge[touch_no] == "bestseller"){
			$("#dsc_badge").css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_bestseller.png)");
		}
	if (dsc_badge[touch_no] == "hotitem"){
		$("#dsc_badge").css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_hotitem.png)");
	}
	if (dsc_badge[touch_no] == "sale"){
		$("#dsc_badge").css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_sale.png)");
	}
	if (dsc_badge[touch_no] == "accessory"){
		$("#dsc_badge").css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_accessory.png)");
	}
	if (dsc_badge[touch_no] == "sdcard"){
		$("#dsc_badge").css("background-image", "url(../category/" + $.i18n.lng().split('-')[0] + "/meep_sdcard.png)");
	}



	// var totalWidth = 0;
	// var img = $("#dsc_block_screenshot").find("img");
	// for (var i=0; i<img.length; i++) {
		// totalWidth += img[i].width;
	// }
	// totalWidth += (img.length - 1) * 30;
	// if( totalWidth >600){
	// $("#dsc_block_screenshot").css('width', totalWidth);}

	balance = "";
	balance_price_num = dsc_price_num[touch_no];
	balance_result_num = coins_total - balance_price_num;

	balance += "<div id='dsc_balance_msg'>" + $.t("store.confirm_purchase") + "</div>"

	balance += "<div id='dsc_calculation'><div id='dsc_balance_coins'>" + $.t("store.coins") + "</div><div id='dsc_balance_total'>" + coins_total + "</div><div id='dsc_balance_minus_sign'>" + balance_minus_sign + "</div><div id='dsc_balance_price'>" + balance_price_num + "</div><div id='dsc_balance_line'></div><div id='dsc_balance_result'>" + balance_result_num + "</div></div>";

	balance += "<div id='dsc_balance_btn_back' onclick='backToDsc(" + touch_no + ")'><div id='dsc_balance_btn_back_txt' onclick='backToDsc(" + touch_no + ")'>" + $.t("store.back") + "</div></div><div id='dsc_balance_btn_confirm' onclick='requestUpdateStatus(" + touch_no + ")'><div id='dsc_balance_btn_confirm_txt'>" + $.t("store.confirm") + "</div></div><div id='dsc_balance_btn_request' onclick='requestCoins()'><div id='dsc_balance_btn_request_txt'>" + $.t("store.request_coins1") + "<br/>" + $.t("store.request_coins2") + "</div></div>";

	balance +="</div>";

	$("#dsc_balance").html($(balance));
	$("#dsc_balance").hide();



	blocked = "";

	blocked += "<div id='dsc_blocked_txt'>" + $.t("store.not_directly_on_meep_store1") + "<br/>" + $.t("store.not_directly_on_meep_store2") + "</div>";

	// blocked += "<div id='dsc_blocked_btn_ok' onclick='backToDsc(" + touch_no + ")'><div id='dsc_blocked_btn_ok_txt'>" + $.t("store.ok") + "</div></div></div>";

	blocked += "<div id='dsc_blocked_btn_box'><div id='dsc_blocked_btn_back' onclick='backToDsc(" + touch_no + ")'><div id='dsc_blocked_btn_back_txt' onclick='backToDsc(" + touch_no + ")'>" + $.t("store.back") + "</div></div><div id='dsc_blocked_btn_request' onclick='requestUpdateStatus(" + touch_no + ")'><div id='dsc_blocked_btn_request_txt'>" + $.t("store.send_request1") + "<br/>" + $.t("store.send_request2") + "</div></div><div id='dsc_blocked_btn_ok' onclick='backToDsc(" + touch_no + ")'><div id='dsc_blocked_btn_ok_txt'>" + $.t("store.ok") + "</div></div></div>";

	$("#dsc_blocked").html($(blocked));
	$("#dsc_blocked").hide();

	$("#dsc_coins").hide();
	$("#dsc_coins_txt").hide();

	result = getStatus(touch_no);

    setTimeout(marquee_title, 8000);

	return touch_no;
}

function marquee_title(repeat) {
    var el = document.getElementById("dsc_title");
    if (el.scrollWidth > el.offsetWidth || repeat) {
        el = $("#dsc_title");
        el.html("<marquee behavior='alternate' loop='2' scrollamount='3'>" + el.text() + " &nbsp; &nbsp; &nbsp;</marquee>");
        setTimeout(function() { marquee_title(true); }, 30000);
    }
}


function expandContainer(e) {
	var p = $(e).parent().parent().parent();
	var origWidth = parseInt(p.css("width"));
	if (origWidth == 0) {
		origWidth = -30;
	}
	p.css("width", origWidth + e.width + 30 + "px");
}


function showDsc(e) {
	var itemid = $(e).attr("id");

	itemid = itemid.split("_")[1];
	window.location.href = "/store.html?" + dsc_storeid[itemid];
	return;

	//$("#dsc_id").show();
	// $(".button").hide();

//	loadDsc(itemid);
}

function closeDsc(){
$("#dsc_id").hide();
// $(".button").show();
// $('iframe').css({'overflow':'scroll'});
}


		$("#button_left_id").click(function() {
			var frameLeft = $("#iframe").scrollLeft();
			$("#iframe").animate({scrollLeft: frameLeft-800}, 500);
		});
		$("#button_right_id").click(function() {
			var frameLeft = $("#iframe").scrollLeft();
			$("#iframe").animate({scrollLeft: frameLeft+800}, 500);
		});

		function prevShelf(){
			// var shelfLeft = parseInt($(".shelf").css("left"));
			// var shelfLeft2 = $(".shelf").position().left;

			var frameLeft = $("#iframe").scrollLeft();
			// if( shelfLeft > -300){
				// $(".shelf").css("-webkit-transform", "translate3d(200px, 0, 0)");
				// setTimeout("$('.shelf').css('-webkit-transform', 'translate3d(0px, 0, 0)');", 500);
				// $(".shelf").animate({"left": "+=200px"});
				// $(".shelf").animate({"left": "-=200px"});
			// }
			// else{
				$("#iframe").animate({scrollLeft: frameLeft-800}, 500);

				//$(".shelf").animate({"left": "-=800px"});
			 // }
		}

function nextShelf(){
	var frameLeft = $("#iframe").scrollLeft();
	// if( shelfLeft < -shelfWidth + 800) {


	// if( frameLeft < 800) {

	// }
	// else{
		$("#iframe").animate({scrollLeft: frameLeft+800}, 500);
		//$(".shelf").animate({"left": "-=800px"});
	// }
}

function getScroll(){
	var frameLeft = $("#iframe").scrollLeft();
	var shift_count = 1 + Math.floor(frameLeft / 650);

	if(shift_count >= loaded_count){
		if (newMethod) {
			getDataFromDatabase(nextPageNum);
		} else {
			// TODO: Delete
			getArray(12);
		}
	}
}


String.prototype.Left = function (n){
	if (n <= 0)
		return "";
	else if (n > String(this).length)
		return this;
	else
		return String(this).substring(0,n) + "..";
}

function goBack()  {
	window.history.back()
}


// function goDsc(id){

	// sessionStorage.item = createDsc(dsc_storeid[id], dsc_title_txt[id], dsc_developer_txt[id], dsc_size_txt[id], dsc_price_num[id], dsc_badge[id], dsc_word_txt[id], dsc_content_txt[id], dsc_screenshots[id], dsc_type[id], dsc_icon[id], dsc_purchase_status[id], dsc_recommends[id]);


	// fillSingleObject(sessionStorage.item);

	// dsc_title_txt[0]= url;

	// window.open("store.html");

	// return item;

// }

// function createDsc(storeid, title, developer, size, price, badge, word, content, screenshots, type, icon, purchase_status, recommends){
	// item ={};
	// item = {
	// "_id":storeid,
	// "name":title,
	// "developer":developer,
	// "size":size,
	// "coins":price,
	// "badge":badge,
	// "word":word,
	// "description":content,
	// "screenshots":screenshots,
	// "type":type,
	// "icon":icon,
	// "purchase_status":purchase_status,
	// "recommends":recommends};
	// return item;
// }
