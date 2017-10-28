// 购物车JS 2008.07 刘长炯 beansoft@126.com

// 使用AJAX方式从后台更新商品购买数量
function ajaxAddOrderItem(product_id) {
		// 异步执行更改购买数量操作
		jQuery.get("cart/addItem.action?product_id=" + product_id + "&timestamp=" + 
		new Date().getTime(), function(data){
				alert(data);
				ajaxUpdateCartItemCount();
		});
}

// 即时更新购物车物品数, 只更新一次
function ajaxUpdateCartItemCount() {
	if($("cartItemCount")) {
		window.setTimeout(function () {
             jQuery("#cartItemCount").load("cart/count.action?timestamp=" + new Date().getTime());
         }, 100);
	}
}

// 缓慢定时更新购物车物品数
function ajaxSlowUpdateCartItemCount() {
	// 确保存在页面元素块
	if($("cartItemCount")) {
		window.setInterval(function () {
             jQuery("#cartItemCount").load("cart/count.action?timestamp=" + new Date().getTime());
         }, 20000);// 10秒更新一次购物车物品数
	}
}