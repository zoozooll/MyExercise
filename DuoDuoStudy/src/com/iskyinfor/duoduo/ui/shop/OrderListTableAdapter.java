package com.iskyinfor.duoduo.ui.shop;

import java.util.ArrayList;
import java.util.List;

import com.iskinfor.servicedata.pojo.Order;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.custom.page.PageListAdapter;
import com.iskyinfor.duoduo.ui.shop.task.OrderDetailTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 用于订单列表Adapter
 * @author zhoushidong
 */
public class OrderListTableAdapter extends PageListAdapter<Order> {
	//private List<TableRow> table;
	private LayoutInflater inflater;
	
	/*public OrderListTableAdapter(Context context, List<TableRow> table) {
		this.context = context;
		this.table = table;
	}*/
	
	public OrderListTableAdapter(Context context, ArrayList<Order> orders) {
		super(context, orders);
		inflater = LayoutInflater.from(context);
	}


	public int getCount() {
		if (arrayList!=null){
			
			return arrayList.size();
		}else {
			return 0;
		}
	}
	public long getItemId(int position) {
		return position;
	}
	public Order getItem(int position) {
		if (arrayList!=null){
			
			return arrayList.get(position);
		} else {
			return null;
		}
	}
	
	
	private class TableRowView{
		TextView textView1;
		TextView textView2;
		TextView textView3;
		TextView textView4;
		Button btnDetail;
		Button btnPayfor;
		Button btnCancel;
	}


	@Override
	public View initItemView(View convertView, Object object, int position) {
		TableRowView tableRowView = null;
		Order o = (Order) object;
		if (convertView==null){
			convertView = inflater.inflate(R.layout.shop_orderlist_listitem, null);
			convertView.setBackgroundColor(Color.TRANSPARENT);
			tableRowView = new TableRowView();
			tableRowView.textView1 = (TextView) convertView.findViewById(R.id.textView1);
			tableRowView.textView2 = (TextView) convertView.findViewById(R.id.textView2);
			tableRowView.textView3 = (TextView) convertView.findViewById(R.id.textView3);
			tableRowView.textView4 = (TextView) convertView.findViewById(R.id.textView4);
			tableRowView.btnDetail = (Button) convertView.findViewById(R.id.btnDetail);
			tableRowView.btnPayfor = (Button) convertView.findViewById(R.id.btnPayfor);
			tableRowView.btnCancel = (Button) convertView.findViewById(R.id.btnCancel);
			
			convertView.setTag(tableRowView);
		}else {
			tableRowView = (TableRowView) convertView.getTag();
		}
		tableRowView.textView1.setText(o.getOrderId());
		tableRowView.textView2.setText(o.getOrderAccount());
		tableRowView.textView3.setText(o.getOrderDate());
		tableRowView.textView4.setText(OrderDetailTask.getRealState(o.getOrdeState(), o.getTranState()));
		OnClickListener listener = new OrderListListener(o);
		tableRowView.btnDetail.setOnClickListener(listener);
		return convertView;
	}
	
	class OrderListListener implements OnClickListener{
		Order order;
		
		public OrderListListener(Order order) {
			super();
			this.order = order;
		}


		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnDetail:
				new OrderDetailTask((Activity) context).execute(Integer.valueOf(order.getOrderId()));
				break;
			case R.id.btnPayfor:
				
				break;
			case R.id.btnCancel:
				
				break;
			}
		}} 
	
}