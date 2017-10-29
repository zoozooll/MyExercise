package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;
import java.util.Map;

import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskinfor.servicedata.pojo.User;
import com.iskinfor.servicedata.study.service.IManagerStudyOperater0100030001;
import com.iskinfor.servicedata.study.serviceimpl.ManagerStudyOperater010003000Impl;
import com.iskinfor.servicedata.usercenter.service.IQuerryUserInfor0300020001;
import com.iskinfor.servicedata.usercenter.serviceimpl.QuerryUserInfor0300020001Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.ReadBookResource;
import com.iskyinfor.duoduo.ui.UiHelp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BookShelfGiftEditActivity extends Activity implements OnClickListener {
	private ImageView img_results=null;
	private TextView bookshelfgifteditlist,bookshelfgift_booknum,bookshelfgift_bookname; 
	private IQuerryUserInfor0300020001 queryuserinfo;
	private IManagerStudyOperater0100030001 managerstudy;
	private BookShelfqq  bookshelfqq;
	private ArrayList<User> classesList,teacherList,homeList,friendList;
	private Map<String, Object> CLASSNAMTESmap;
	private String[] userIDs;
	private ImageView bookshelfgiftsend;
	private EditText bookshelfgiftcontext;
	private String strBookID,strBookNum,strBookName;
	private String giftName;
	private TextView bookshelfgiftgiftname;
	private CheckBox bookshelfgiftchk;
	private ProgressDialog myLoadingDialog;
	private GiftEditTask giftedittask;
	private GiftSendTask giftsendtask;
	
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_gift_edit);
		InitView();
	}
	
  	public void InitView()
  	{
         strBookID= getIntent().getStringExtra("BookID"); 
   	     strBookNum=getIntent().getStringExtra("BookNum"); 
   	     strBookName=getIntent().getStringExtra("BookName"); 
   	     
  		 
  		bookshelfgiftsend =(ImageView)findViewById(R.id.bookshelfgift_send);
  		bookshelfgiftsend.setOnClickListener(this);
  		
  		bookshelfgift_booknum =(TextView)findViewById(R.id.bookshelfgift_booknum); 
  		bookshelfgift_booknum.setText(strBookNum);
  		
  		bookshelfgift_bookname =(TextView)findViewById(R.id.bookshelfgift_bookname); 
  		bookshelfgift_bookname.setText(strBookName);
  		bookshelfgiftgiftname = (TextView)findViewById(R.id.bookshelfgift_giftname);
  		bookshelfgifteditlist=(TextView)findViewById(R.id.bookshelfgift_editlist);
		bookshelfgifteditlist.setText(Html.fromHtml("<u><a>"+bookshelfgifteditlist.getText()+"</u></a>"));
		bookshelfgifteditlist.setOnClickListener(this);
		img_results=(ImageView)findViewById(R.id.bookshelfgift_edit_bottomfh);
		img_results.setOnClickListener(this);   
		
  	}
  	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	    switch(v.getId())
	    {
	        case R.id.bookshelfgift_edit_bottomfh:
	    	finish();
	    	break;
	        case R.id.bookshelfgift_editlist:
	        	
	        	myLoadingDialog = new ProgressDialog(this);
	    		myLoadingDialog.setMessage("好友列表加载中...");
	    		myLoadingDialog.show();
	    		
				try {
					giftedittask=new GiftEditTask(BookShelfGiftEditActivity.this, myLoadingDialog, "BookShelfGiftEditActivity");
					giftedittask.execute();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	break;
	        case R.id.bookshelfgift_send:
				try {
					bookshelfgiftcontext=(EditText) findViewById(R.id.bookshelfgift_context); 
		        	managerstudy=new ManagerStudyOperater010003000Impl();
					bookshelfgiftchk=(CheckBox)findViewById(R.id.bookshelfgift_chk); 
					if(bookshelfgiftgiftname.getText().toString().equals(""))
					{Toast.makeText(BookShelfGiftEditActivity.this, "赠送人不能为空！", Toast.LENGTH_LONG).show();return;}
					if(bookshelfgiftcontext.getText().toString().equals(""))
					{Toast.makeText(BookShelfGiftEditActivity.this, "赠送内容不能为空！", Toast.LENGTH_LONG).show();return;}
					
					myLoadingDialog = new ProgressDialog(this);
		    		myLoadingDialog.setMessage("赠送中,请稍候...");
		    		myLoadingDialog.show();
					giftsendtask=new GiftSendTask(BookShelfGiftEditActivity.this, myLoadingDialog, "gift",UiHelp.getUserShareID(BookShelfGiftEditActivity.this), strBookID,bookshelfgiftchk.isChecked()?"01":"00", bookshelfgiftcontext.getText().toString(), userIDs, "");
					giftsendtask.execute();
                    //String userId,String proId,String operateType,String reason,String[] object,String ispublic
					//String reinfo= managerstudy.giveBookToOther(UiHelp.getUserShareID(BookShelfGiftEditActivity.this), strBookID,bookshelfgiftchk.isChecked()?"01":"00", bookshelfgiftcontext.getText().toString(), userIDs, "");
				
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	break;
	    }
	}
	
	private String[] listToStrings(ArrayList<User> listuser)
	{
		String[] Str=new String[listuser.size()];
		for(int i=0;i<listuser.size();i++)
		{
//			Str[i] = listuser.get(i).getUserId()+"/"+listuser.get(i).getUserName();
			Str[i] = listuser.get(i).getUserName();
		}
		return Str;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			UiHelp.turnHome(this);
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	public void setUserId(String[] IDs,String giftname)
	{
		userIDs=IDs;
		giftName = giftname;
        bookshelfgiftgiftname.setText(giftname);
        
        String IDS="";
		for(int i=0;i<userIDs.length;i++)
		{IDS+=userIDs[i]+" ";}
		Log.i("PLJ", "GiftIDS==>"+IDS);
        
	}
	public void setGiftFriendMap(Map<String, Object> nameMap)
	{
		CLASSNAMTESmap= nameMap;
		bookshelfqq=new BookShelfqq(BookShelfGiftEditActivity.this,bookshelfgifteditlist,CLASSNAMTESmap,"BookShelfGiftEditActivity",myLoadingDialog,userIDs);
	  	bookshelfqq.showQQScren();
	}
	
	public void setGiftSendInfo(String reinfo)
	{
		if(reinfo=="")
		{Toast.makeText(BookShelfGiftEditActivity.this, "赠送成功！", Toast.LENGTH_LONG).show(); StaticData.boolSended=true; finish();}
		else{Toast.makeText(BookShelfGiftEditActivity.this, "赠送失败："+reinfo, Toast.LENGTH_LONG).show();}
		
	}
	

	private void loadDataProgress(){
		myLoadingDialog = new ProgressDialog(this);
		myLoadingDialog.setMessage("好友列表加载中...");
		myLoadingDialog.show();
	}
	private void cancelDialog(){
		if(myLoadingDialog!=null && myLoadingDialog.isShowing()){
			myLoadingDialog.dismiss();
		} 
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		cancelDialog();
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		cancelDialog();
		super.onDestroy();
	}
	
}
