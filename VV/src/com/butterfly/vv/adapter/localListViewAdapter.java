package com.butterfly.vv.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.ChatActivity;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.utils.BBSUtils;
import com.btf.push.Item;
import com.btf.push.Item.MsgType;
import com.btf.push.Item.MsgTypeSub;
import com.butterfly.vv.service.dialog.ContactServiceDlg;
import com.butterfly.vv.vv.utils.CToast;

public class localListViewAdapter extends CommonPhotoAdapter<Item> {
	private Context mContext;

	public localListViewAdapter(Context c, AdapterView<?> listView) {
		super(listView);
		mContext = c;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.loadnewfrienditem, parent,
					false);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.avatar);
			holder.name = (TextView) convertView
					.findViewById(R.id.contactlistpseudo);
			holder.profile_layout_sex_sign = (LinearLayout) convertView
					.findViewById(R.id.profile_layout_sex_sign);
			holder.sexage = (TextView) convertView
					.findViewById(R.id.profile_tv_agenew);
			holder.profile_tv_distance = (TextView) convertView
					.findViewById(R.id.profile_tv_distance);
			holder.profile_tv_time = (TextView) convertView
					.findViewById(R.id.profile_tv_time);
			holder.signature = (TextView) convertView
					.findViewById(R.id.contactlistmsgperso);
			holder.zone = (Button) convertView.findViewById(R.id.vvzone);
			holder.list = convertView.findViewById(R.id.list);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.zone.setOnClickListener(new VHClickLis(holder, position));
		holder.list.setOnClickListener(new VHClickLis(holder, position));
		holder.img.setOnClickListener(new VHClickLis(holder, position));
		bindView(convertView, position, holder);
		return convertView;
	}

	private class VHClickLis implements OnClickListener {
		private ViewHolder vh;
		private int position;

		public VHClickLis(ViewHolder vh, int postion) {
			super();
			this.vh = vh;
			this.position = postion;
		}
		@Override
		public void onClick(View v) {
			MsgTypeSub type = getItem(position).getMsgTypeSub();
			if (v == vh.zone) {
				if (type == MsgTypeSub.zone) {
					// 跳到个人时光界面
				} else if (type == MsgTypeSub.unregister) {
					// 发送邀请
					CharSequence charSequence = Html.fromHtml("www.evieo.com");
					BBSUtils.sendSMS(mContext, getItem(position).getPhoneNum(),
							"快来注册时光机吧！" + charSequence);
				} else if (type == MsgTypeSub.unadded) {
					ContactServiceDlg.showAddContactDlg(mContext, new Contact(
							getItem(position).getJid(), getItem(position)
									.getNickname()), v);
				}
			} else if (v == vh.list) {
				if (LoginManager.getInstance().isMyJid(
						getItem(position).getJid())) {
					Toast.makeText(mContext, "请不要和自己聊天", Toast.LENGTH_SHORT)
							.show();
				} else if (type != MsgTypeSub.unregister) {
					ChatActivity
							.launch(mContext, getItem(position).toContact());
				}
			} else if (v == vh.img) {
				if (type == MsgTypeSub.unregister) {
					CToast.showToast(mContext, "该手机联系人没有注册不能查看个人信息",
							Toast.LENGTH_SHORT);
				} else {
					Contact c = new Contact();
					c.setJid(getItem(position).getJid());
					c.setNickName(getItem(position).getNickname());
					// 点击头像进入资料界面
					ContactInfoActivity.launch(mContext, c);
				}
			}
		}
	}

	protected void bindView(View view, int position, ViewHolder holder) {
		Item item = getItem(position);
		item.displayPhoto(holder.img);
		switch (getItem(position).getMsgtype()) {
			case msg_city:
			case search_rst:
			case msg_school:
				holder.profile_tv_time.setText(BBSUtils
						.getTimeDurationString(item.getLoginTime()));
				holder.profile_tv_distance.setText(LoginManager.getInstance()
						.latlon2Distance(item.getLat(), item.getLon()));
				holder.name.setText(item.getNickname());
				holder.signature.setText(item.getSignature());
				holder.sexage.setText(BBSUtils.getAgeByBithday(item.getBday()));
				holder.sexage.setSelected(item.getSexInt() == 0);
				break;
			case msg_phone:
				holder.name.setText(item.getNickname());
				holder.signature.setText(item.getSignature());
				break;
			default:
				break;
		}
		setZoneStyle(item.getMsgTypeSub(), holder, item);
	}
	private void setZoneStyle(MsgTypeSub zoneStyle, ViewHolder vh, Item item) {
		vh.profile_layout_sex_sign.setVisibility(View.VISIBLE);
		vh.name.setVisibility(View.VISIBLE);
		switch (zoneStyle) {
			case added: {
				vh.zone.setBackgroundResource(R.drawable.transparent);
				vh.zone.setText("已添加");
				vh.zone.setTextColor(Color.parseColor("#888888"));
				if (item.getMsgtype() == MsgType.msg_phone) {
					vh.signature.setText("手机通讯录（" + item.getPhoneNum() + ") "
							+ item.getPhoneName());
				} else if (item.getMsgtype() == MsgType.search_rst) {
					vh.signature.setText(item.getSignature());
				}
				break;
			}
			case unregister: {
				vh.zone.setBackgroundResource(R.drawable.contact_phonecontacts_invite_sel);
				vh.zone.setText("邀请");
				vh.zone.setTextColor(Color.parseColor("#ffffff"));
				vh.profile_layout_sex_sign.setVisibility(View.GONE);
				vh.name.setVisibility(View.GONE);
				if (item.getMsgtype() == MsgType.msg_phone) {
					vh.signature.setText("手机通讯录（" + item.getPhoneNum() + ") "
							+ item.getPhoneName());
				} else if (item.getMsgtype() == MsgType.search_rst) {
					vh.signature.setText(item.getSignature());
				}
				break;
			}
			case unadded: {
				vh.zone.setBackgroundResource(R.drawable.transparent);
				vh.zone.setText("+ 添加");
				vh.zone.setTextColor(Color.parseColor("#ff2EB5F0"));
				if (item.getMsgtype() == MsgType.msg_phone) {
					vh.signature.setText("手机通讯录（" + item.getPhoneNum() + ") "
							+ item.getPhoneName());
				} else if (item.getMsgtype() == MsgType.search_rst) {
					vh.signature.setText(item.getSignature());
				}
				break;
			}
			case zone: {
				vh.zone.setBackgroundResource(R.drawable.btn_vv_im_zone);
				break;
			}
			default:
				break;
		}
	}

	private final class ViewHolder {
		public ImageView img;
		public TextView name;
		public LinearLayout profile_layout_sex_sign;
		public TextView sexage;
		public TextView profile_tv_distance;
		public TextView profile_tv_time;
		public TextView signature;
		public Button zone;
		public View list;
	}

	@Override
	public List<Item> initItems() {
		return new LinkedList<Item>();
	}
}
