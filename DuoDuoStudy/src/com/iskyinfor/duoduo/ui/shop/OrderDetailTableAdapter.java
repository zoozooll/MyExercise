package com.iskyinfor.duoduo.ui.shop;

import java.util.List;

import com.iskyinfor.duoduo.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 订单详情Adapter
 * @author zhoushidong
 *
 */
public class OrderDetailTableAdapter extends BaseAdapter {
	private Context context;
	private List<TableRow> table;
	public OrderDetailTableAdapter(Context context, List<TableRow> table) {
		this.context = context;
		this.table = table;
	}
	public int getCount() {
		return table.size();
	}
	public long getItemId(int position) {
		return position;
	}
	public TableRow getItem(int position) {
		return table.get(position);
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		TableRow tableRow = table.get(position);
		return new TableRowView(this.context, tableRow , position);
	}
	/**
	 * TableRowView
	 * 
	 * @author hellogv
	 */
	class TableRowView extends LinearLayout {
		public TableRowView(Context context, TableRow tableRow, int position) {
			super(context);
			
			this.setOrientation(LinearLayout.HORIZONTAL);
			this.setPadding(0, 6, 0, 6);
			
			TableCell tableCell = tableRow.getCellValue(0);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					tableCell.width, tableCell.height);//���ո�Ԫָ���Ĵ�С���ÿռ�
			TextView textCell = new TextView(context);
			textCell.setGravity(Gravity.CENTER);
			//textCell.setBackgroundColor(Color.WHITE);	//������ɫ
			textCell.setText(String.valueOf(tableCell.value));
			addView(textCell, layoutParams);
			
			tableCell = tableRow.getCellValue(1);
			layoutParams = new LinearLayout.LayoutParams(
					tableCell.width, tableCell.height);//���ո�Ԫָ���Ĵ�С���ÿռ�
			if (position == 0) {
				textCell = new TextView(context);
				textCell.setGravity(Gravity.CENTER);
				textCell.setText(String.valueOf(tableCell.value));
				addView(textCell , layoutParams);
			} else {
				LinearLayout ll = new LinearLayout(context);
				LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.CENTER_VERTICAL;
				lp.rightMargin = 3;
				ImageView bookImage = new ImageView(context);
				bookImage.setImageResource(R.drawable.bookone);
				textCell.setTextColor(Color.BLUE);
				ll.addView(bookImage , lp);
				textCell = new TextView(context);
				textCell.setText("������������");
				textCell.setTextColor(Color.BLUE);
				ll.addView(textCell , lp);
				
				addView(ll, layoutParams);
			}/*
			textCell = new TextView(context);
			textCell.setGravity(Gravity.CENTER);
			textCell.setText(String.valueOf(tableCell.value));
			addView(textCell, layoutParams);*/
			
			tableCell = tableRow.getCellValue(2);
			layoutParams = new LinearLayout.LayoutParams(
					tableCell.width, tableCell.height);//���ո�Ԫָ���Ĵ�С���ÿռ�
			textCell = new TextView(context);
			textCell.setGravity(Gravity.CENTER);
			textCell.setText(String.valueOf(tableCell.value));
			addView(textCell, layoutParams);
			
			tableCell = tableRow.getCellValue(3);
			layoutParams = new LinearLayout.LayoutParams(
					tableCell.width, tableCell.height);//���ո�Ԫָ���Ĵ�С���ÿռ�
			textCell = new TextView(context);
			textCell.setGravity(Gravity.CENTER);
			textCell.setText(String.valueOf(tableCell.value));
			addView(textCell, layoutParams);
			
			tableCell = tableRow.getCellValue(4);
			layoutParams = new LinearLayout.LayoutParams(
					tableCell.width, tableCell.height);//���ո�Ԫָ���Ĵ�С���ÿռ�
			// 0 ���ʾ��ͷ
			if (position == 0) {
				textCell = new TextView(context);
				textCell.setGravity(Gravity.CENTER);
				textCell.setText(String.valueOf(tableCell.value));
				addView(textCell , layoutParams);
			} else {
				LinearLayout ll = new LinearLayout(context);
				LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.rightMargin = 3;
				lp.gravity = Gravity.CENTER_VERTICAL;
				textCell = new TextView(context);
				textCell.setText(Html.fromHtml("<u>����</u>"));
				textCell.setTextColor(Color.BLUE);
				ll.addView(textCell , lp);
				textCell = new TextView(context);
				textCell.setText(Html.fromHtml("<u>�����ղ�</u>"));
				textCell.setTextColor(Color.BLUE);
				ll.addView(textCell , lp);
				textCell = new TextView(context);
				textCell.setText(Html.fromHtml("<u>����</u>"));
				textCell.setTextColor(Color.BLUE);
				ll.addView(textCell , lp);
				
				addView(ll, layoutParams);
			}
			
			
		}
	}
	/**
	 * TableRow ʵ�ֱ�����
	 * @author hellogv
	 */
	static public class TableRow {
		private TableCell[] cell;
		public TableRow(TableCell[] cell) {
			this.cell = cell;
		}
		public int getSize() {
			return cell.length;
		}
		public TableCell getCellValue(int index) {
			if (index >= cell.length)
				return null;
			return cell[index];
		}
	}
	/**
	 * TableCell ʵ�ֱ��ĸ�Ԫ
	 * @author hellogv
	 */
	static public class TableCell {
		static public final int STRING = 0;
		static public final int IMAGE = 1;
		public Object value;
		public int width;
		public int height;
		int type;
		public TableCell(Object value, int width, int height, int type) {
			this.value = value;
			this.width = width;
			this.height = height;
			this.type = type;
		}
	}
	
	
}