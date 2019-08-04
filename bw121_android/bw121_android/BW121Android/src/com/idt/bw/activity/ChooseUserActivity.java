package com.idt.bw.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.idt.bw.bean.User;
import com.idt.bw.database.OperatingTable;

public class ChooseUserActivity extends Activity implements OnClickListener{
	private ImageView createUserIcon;
	private ListView userlist;
	private UserAdapter userAdapter;
	private ArrayList<User> usersList;
	private User user;
	private OperatingTable operTable;
	private int chooseDelete = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_user);
		
		init();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		operTable = OperatingTable.instance(this);
		usersList = operTable.query(null);
		userAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		operTable = OperatingTable.instance(this);
		usersList = operTable.query(null);		
		userAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onDestroy() {
		operTable = OperatingTable.instance(this);
		operTable.close();
		super.onDestroy();
	}
	
	public void init(){
		createUserIcon = (ImageView) findViewById(R.id.createUserIcon);
		userlist = (ListView) findViewById(R.id.userlist);
		
		operTable = OperatingTable.instance(this);
		usersList = operTable.query(null);
		
		userAdapter = new UserAdapter();
		userlist.setAdapter(userAdapter);
			
		createUserIcon.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.createUserIcon:
			//Log.e("cdf","go create user activity !!!!!!!!");
			Intent ii = new Intent(ChooseUserActivity.this, CreateUserActivity.class);
			startActivity(ii);
			break;
			
		default:
			break;
		}
	}
	class UserAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
//			Log.e("cdf","usersList.size()=="+usersList.size());
			return usersList.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return usersList.get(position);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			LayoutInflater inflater = LayoutInflater.from(ChooseUserActivity.this);  
			if (convertView == null) {
			    holder = new ViewHolder();
			    convertView = inflater.inflate(R.layout.user_list, null);  
			    holder.userlist_name = (TextView)convertView. findViewById(R.id.userlist_name);
			    holder.userlist_pic = (ImageView)convertView. findViewById(R.id.userlist_pic);
			    convertView.setTag(holder);
			} else {
			    holder = (ViewHolder)convertView.getTag();
			}
			final User user = usersList.get(position);
			holder.userlist_name.setText(user.getUserName().toString());
			String path = user.getUserPhoto();
			//Log.e("cdf","id  birth  cat::::::::::  "+user.getId()+"   "+user.getUserBirth()+"   "+user.getUserCategory());
			if(TextUtils.isEmpty(path) || !new File(path).exists()){
				holder.userlist_pic.setImageResource(R.drawable.profile_picture);
			}else{
				Bitmap bm = BitmapFactory.decodeFile(path);
				Bitmap roundBm = getRoundedCornerBitmap(bm,false);
				holder.userlist_pic.setImageBitmap(roundBm);
			}
			/*if("1".equals(user.getUserCategory())){
				convertView.setBackgroundResource(R.drawable.bg_useritem_normal);
			}else if("2".equals(user.getUserCategory())){
				convertView.setBackgroundResource(R.drawable.bg_useritem_pregnacy);
			}else if("3".equals(user.getUserCategory())){
				convertView.setBackgroundResource(R.drawable.bg_useritem_kids);
			}else if("4".equals(user.getUserCategory())){
				convertView.setBackgroundResource(R.drawable.bg_useritem_baby);
			}*/
			//convertView.setTag(0, u);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(ChooseUserActivity.this,MainActivity.class);
					Bundle b = new Bundle();
					b.putSerializable("user", user);
					i.putExtras(b);
					startActivity(i);
				}
			});
			convertView.setLongClickable(true);
			convertView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					/*operTable.deleteUserAndSettingRecords(user.getId());
					operTable.query(null);
					notifyDataSetChanged();*/
					Dialog dialog=new AlertDialog.Builder(ChooseUserActivity.this)
					.setTitle(getResources().getString(R.string.choose_whether_deleteuser))
					.setSingleChoiceItems(getResources().getStringArray(R.array.delete_user), chooseDelete, new DialogInterface.OnClickListener() {
						//上边第一个表示的是公司数组列表，第二个参数表示默认选择的，第三个监听器
						public void onClick(DialogInterface dialog, int which) {
							chooseDelete=which;
							if(chooseDelete == 0){
								operTable.deleteUserAndSettingRecords(user.getId());
								usersList = operTable.query(null);		
								userAdapter.notifyDataSetChanged();
							}else if(chooseDelete == 1){
							}
							dialog.dismiss();
						}
					}).create();
					dialog.show();
					return true;
				}
			});
			return convertView;
		}
	}
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, boolean recyclePrevious){
		int side = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap outBitmap = Bitmap.createBitmap(side, side, Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        final Paint paint = new Paint();
        final Rect square = new Rect(0,0,side,side);
        final RectF rectF = new RectF(square);
        final float roundPX = side/2;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, square, paint);
        if (recyclePrevious) {
        	bitmap.recycle();
        }
        return outBitmap;
    }
	class ViewHolder {
		ImageView userlist_pic;
		TextView userlist_name; 
	}
	
}
