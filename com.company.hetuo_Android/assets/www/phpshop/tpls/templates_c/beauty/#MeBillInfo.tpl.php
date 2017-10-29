<table id="J_ListTable">
<thead>
<tr>
    <th class="item-list-col0"> </th>
    <th class="item-list-col1 item">订单号</th>
    <th class="item-list-col2 price">订单日期</th>
    <th class="item-list-col3 num">出货日期</th>
    <th class="item-list-col4 trouble">单价</th>
    <th class="item-list-col6 contact">售价</th>
    <th class="item-list-col7 trade-status">交易状态</th>
    <th class="item-list-col8 order-price">实收款(元)</th>
    <th></th>
</tr>
</thead>
<?php if($this->__muant["mybillno"]!='') { ?>
<?php $mybillnoinum = count($this->__muant["mybillno"]); for($mybillnoi = 0; $mybillnoi<$mybillnoinum; $mybillnoi++) { ?><?php if($this->__muant["mybillno"]["$mybillnoi"]["billno"]!=$this->__muant["mybillno"]["$mybillnoi"-1]["billno"]) { ?>
<tbody id="all_order" bgcolor="#F6F6F6">
    <tr>
        <td> </td>
        <td class="item"><?php echo $this->__muant["mybillno"]["$mybillnoi"]["billno"] ?></td>
        <td class="price"><?php echo $this->__muant["mybillno"]["$mybillnoi"]["add_date"] ?></td>
        <td class="num"><?php if($this->__muant["mybillno"]["$mybillnoi"]["shipment_date"]=='') { ?>未出货<?php } else { ?><?php echo $this->__muant["mybillno"]["$mybillnoi"]["shipment_date"] ?><?php } ?></td>
        <td class="trouble"></td>
        <td class="contact"></td>
        <td class="trade-status"><?php if($this->__muant["mybillno"]["$mybillnoi"]["pay_success"]=='0') { ?>未付款 <a href="getshopinfo.php?billno=<?php echo $this->__muant["mybillno"]["$mybillnoi"]["billno"] ?>&md=<?php echo $this->__muant["mybillno"]["$mybillnoi"]["md5id"] ?>" target="_blank">付款</a><!--a href="">取消订单</a--><?php } elseif($this->__muant["mybillno"]["$mybillnoi"]["pay_success"]=='1') { ?>网上支付成功<?php } elseif($this->__muant["mybillno"]["$mybillnoi"]["pay_success"]=='2') { ?>银行转账成功<?php } elseif($this->__muant["mybillno"]["$mybillnoi"]["pay_success"]=='3') { ?>邮局电汇成功<?php } elseif($this->__muant["mybillno"]["$mybillnoi"]["pay_success"]=='4') { ?>货到付款成功<?php } elseif($this->__muant["mybillno"]["$mybillnoi"]["pay_success"]=='5') { ?>正在退款<?php } elseif($this->__muant["mybillno"]["$mybillnoi"]["pay_success"]=='6') { ?>退款成功<?php } elseif($this->__muant[" mybillno"]["$ mybillnoi"]["state"]=='3') { ?>订单已取消<?php } ?></td>
        <td class="order-price">￥<?php echo $this->__muant["mybillno"]["$mybillnoi"]["pay_price"] ?></td>
        <td></td>
    </tr>
</tbody>
<tbody>
    <tr>
        <td> </td>
        <td class="item"><a href="<?php echo $this->__muant["mybillno"]["$mybillnoi"]["url"] ?>" target="_blank"><img src="<?php echo $this->__muant["mybillno"]["$mybillnoi"]["m_pic"] ?>" width="50" onerror="this.src='product/nopic.gif'" border="0" /></a></td>
        <td class="price"><a href="<?php echo $this->__muant["mybillno"]["$mybillnoi"]["url"] ?>" target="_blank"><?php echo $this->__muant["mybillno"]["$mybillnoi"]["name"] ?></a></td>
        <td class="num">数量：<?php echo $this->__muant["mybillno"]["$mybillnoi"]["num"] ?></td>
        <td class="trouble"><?php echo $this->__muant["mybillno"]["$mybillnoi"]["price"] ?></td>
        <td class="contact"><?php echo $this->__muant["mybillno"]["$mybillnoi"]["sell_price"] ?></td>
        <td class="trade-status"></td>
        <td class="order-price"><?php echo $this->__muant["mybillno"]["$mybillnoi"]["all_price"] ?></td>
        <td></td>
    </tr>
</tbody>
<?php } else { ?>
<tbody>
    <tr>
        <td> </td>
        <td class="item"><a href="<?php echo $this->__muant["mybillno"]["$mybillnoi"]["url"] ?>" target="_blank"><img src="<?php echo $this->__muant["mybillno"]["$mybillnoi"]["m_pic"] ?>" width="50" onerror="this.src='product/nopic.gif'" border="0" /></a></td>
        <td class="price"><a href="<?php echo $this->__muant["mybillno"]["$mybillnoi"]["url"] ?>" target="_blank"><?php echo $this->__muant["mybillno"]["$mybillnoi"]["name"] ?></a></td>
        <td class="num">数量：<?php echo $this->__muant["mybillno"]["$mybillnoi"]["num"] ?></td>
        <td class="trouble"><?php echo $this->__muant["mybillno"]["$mybillnoi"]["price"] ?></td>
        <td class="contact"><?php echo $this->__muant["mybillno"]["$mybillnoi"]["sell_price"] ?></td>
        <td class="trade-status"></td>
        <td class="order-price"><?php echo $this->__muant["mybillno"]["$mybillnoi"]["all_price"] ?></td>
        <td></td>
    </tr>
</tbody>
<?php } ?>
<?php } ?>
<?php } else { ?>
<tbody><tr><td colspan="9">
   <div style="text-align: left;" class="smile-tip">近一个月中没有您需要的订单信息。30天前的订单，请到<a hidefocus="true" onclick="callLoad('item-list-bd-order');callServer('myhome.php?switch=showmybillno&act=history&p='+Math.random(),'item-list-bd-order');checmenu('order',8,'current')" href="javascript:void(0);">历史订单</a>中查询。</div>
  </td></tr>
</tbody>
<?php } ?>
</table>