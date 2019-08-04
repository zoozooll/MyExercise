package com.butterfly.vv.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.ChatActivity;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.ui.activity.OtherTimeFlyMain;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.btf.push.NeighborHoodPacket.NeighborHoodType;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.Debug;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @ClassName: NearbyContactsAdapter
 * @Description: 附近的人Adapter
 * @author: yuedong bao
 * @date: 2015-3-9 下午3:10:19
 */
public class NearbyContactsAdapter extends CommonPhotoAdapter<Contact> {
	private Context mContext;
	private NeighborHoodType nbType;

	public NearbyContactsAdapter(Context c, NeighborHoodType nbType,
			AdapterView<?> listView) {
		super(listView);
		this.mContext = c;
		this.nbType = nbType;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.contactlistcontact, parent,
					false);
			vh.nickname = (TextView) convertView
					.findViewById(R.id.contactlistpseudo);
			vh.signature = (TextView) convertView
					.findViewById(R.id.contactlistmsgperso);
			vh.headPhoto = (ImageView) convertView.findViewById(R.id.avatar);
			vh.vvzone_wraper = (RelativeLayout) convertView
					.findViewById(R.id.vvzone_wraper);
			vh.distance = (TextView) convertView
					.findViewById(R.id.profile_tv_distance);
			vh.profile_tv_agenew = (TextView) convertView.findViewById(R.id.profile_tv_agenew);
			vh.logintime = (TextView) convertView
					.findViewById(R.id.profile_tv_time);
			vh.list = convertView.findViewById(R.id.list);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		bindView(convertView, position, vh);
		return convertView;
	}

	private static class ViewHolder {
		public View list;
		public TextView logintime;
		public TextView signature;
		public TextView distance;
		public TextView profile_tv_agenew;
		public TextView nickname;
		public ImageView headPhoto;
		public RelativeLayout vvzone_wraper;
	}

	protected void bindView(View view, int position, ViewHolder vh) {
		Contact curContact = getItem(position);
		vh.logintime.setText(curContact.getLoginTimeRlt());
		vh.distance.setText(curContact.getDistanceFormat());
		vh.profile_tv_agenew.setText(curContact.getAge());
		if ("1".equals(curContact.getSex())) {
			vh.profile_tv_agenew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bbs_man, 0, 0, 0);
			vh.profile_tv_agenew.setBackgroundResource(R.drawable.bbs_man_bg);
		} else {
			vh.profile_tv_agenew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bbs_woman, 0, 0, 0);
			vh.profile_tv_agenew.setBackgroundResource(R.drawable.bbs_woman_bg);
		}
		vh.nickname.setText(curContact.getNickName());
		vh.signature.setText(curContact.getSignature());
		DisplayImageOptions options = null;
		ImageLoader.getInstance().displayImage(curContact.getPhoto(),
				vh.headPhoto, options);
		vh.headPhoto.setOnClickListener(new InfoListener(curContact));
		vh.vvzone_wraper.setOnClickListener(new ZoneListener(position,
				curContact));
		vh.list.setOnClickListener(new ChatListener(curContact));
	}

	private class ZoneListener implements OnClickListener {
		private int mposition;
		com.beem.project.btf.service.Contact curContact;

		public ZoneListener(int position,
				com.beem.project.btf.service.Contact curContact) {
			this.mposition = position;
			this.curContact = curContact;
		}
		@Override
		public void onClick(View v) {
			Debug.getDebugInstance().log(
					getClass().getName() + " zone l " + mposition + " "
							+ curContact.getJid());
			OtherTimeFlyMain.launch(mContext, curContact);
		}
	}

	private class ChatListener implements OnClickListener {
		Contact c;

		public ChatListener(com.beem.project.btf.service.Contact c) {
			this.c = c;
		}
		@Override
		public void onClick(View v) {
			if (LoginManager.getInstance().isLogined()) {
				ChatActivity.launch(mContext, c);
			} else {
				CToast.showToast(mContext, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
				ActivityController.getInstance().gotoLogin();
			}
		}
	}

	private class InfoListener implements OnClickListener {
		private Contact c;

		public InfoListener(Contact c) {
			this.c = c;
		}
		@Override
		public void onClick(View v) {
			ContactInfoActivity.launch(mContext, c);
		}
	}

	@Override
	public List<Contact> initItems() {
		return new ArrayList<Contact>();
	}
	public NeighborHoodType getNbType() {
		return nbType;
	}
	public void setNbType(NeighborHoodType nbType) {
		this.nbType = nbType;
	}
	@Override
	public void addItems(Collection<Contact> inputs) {
		for (Contact input : inputs) {
			addItem(input);
		}
	}
	@Override
	public void addItem(Contact input) {
		String jid = null;
		jid = input.getJIDParsed();
		for (Iterator<Contact> it = getItems().iterator(); it.hasNext();) {
			Contact c = it.next();
			if (c.getJIDParsed().equals(jid)) {
				it.remove();
				break;
			}
		}
		super.addItem(input);
	}
}
