package com.butterfly.vv.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.ui.fragment.TalkFragement;
import com.beem.project.btf.ui.fragment.TalkFragement.FragmentType;
import com.beem.project.btf.ui.fragment.TalkFragement.SendmsgListenter;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.ExpressionUtil;
import com.beem.project.btf.utils.SortedList;
import com.beem.project.btf.utils.ExpressionUtil.ExpressionSizeType;
import com.beem.project.btf.utils.SortedList.Equalable;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.service.TimeflyService;

/**
 * @func VV项目中评论适配器
 * @author yuedong bao
 * @date 2015-2-11 下午5:07:08
 */
public class SharerankingCommentAdapter extends BaseAdapter {
	private static final String TAG = "CommentAdapter";
	private Context mContext;
	private final CommentShow data;
	private ItemChangedListener itemChangedListener;
	//最多显示多少条
	private int maxShowNum = Integer.MAX_VALUE;

	//数据项更新监听器
	public interface ItemChangedListener {
		public void ItemChanged(int count);
	}

	public void setItemChangedListener(ItemChangedListener itemChangedListener) {
		this.itemChangedListener = itemChangedListener;
	}

	private class CommentShow {
		// 一级评论map
		private List<CommentItem> dataMap = new SortedList<CommentItem>(
				new ArrayList<CommentItem>(), new Comparator<CommentItem>() {
					@Override
					public int compare(CommentItem lhs, CommentItem rhs) {
						if (lhs.getComent().getId()
								.equals(rhs.getComent().getId())) {
							return 0;
						}
						String time_l = lhs.getComent().getCommentTime();
						String time_r = rhs.getComent().getCommentTime();
						if (time_r == null) {
							return 1;
						} else if (time_l == null) {
							return -1;
						}
						//						Log.i(TAG, "~time_l~"+time_l+"~time_r~"+time_r);
						return time_r.compareTo(time_l);
					}
				}, new Equalable<CommentItem>() {
					@Override
					public boolean equal(CommentItem t1, CommentItem t2) {
						return t1.getComent().getId()
								.equals(t2.getComent().getId());
					}
				});

		public void clear() {
			dataMap.clear();
			list.clear();
		}

		private List<CommentItem> list = new LinkedList<CommentItem>();

		public void addCommentItem(CommentItem item) {
			if (item.getComent().isCommentLayFirst()) {
				dataMap.add(item);
			} else {
				String to_cid = item.getComent().getToCid();
				for (CommentItem commentItem : dataMap) {
					if (commentItem != null
							&& commentItem.getComent().getCid() != null) {
						if (commentItem.getComent().getCid().equals(to_cid)) {
							commentItem.addChildItem(item);
							break;
						}
					}
				}
			}
			updateList();
		}
		public void addCommentItems(Collection<CommentItem> items) {
			for (CommentItem itemOne : items) {
				addCommentItem(itemOne);
			}
			updateList();
		}
		private void updateList() {
			list.clear();
			for (CommentItem commentItem : dataMap) {
				list.add(commentItem);
				for (CommentItem commentItemChild : commentItem.getChildItems()) {
					list.add(commentItemChild);
				}
			}
		}
		@Override
		public String toString() {
			return "CommentShow [dataMap=" + dataMap + ", list=" + list + "]";
		}
	}

	public SharerankingCommentAdapter(Context ctx) {
		super();
		this.mContext = ctx;
		this.data = new CommentShow();
	}
	public SharerankingCommentAdapter(Context ctx, int maxShowNum) {
		this(ctx);
		this.maxShowNum = maxShowNum;
	}
	@Override
	public long getItemId(int pos) {
		return pos;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.shareranking_detail_item1,
					parent, false);
			vh = new ViewHolder();
			vh.head = (ImageView) convertView.findViewById(R.id.head);
			vh.content = (TextView) convertView.findViewById(R.id.content);
			vh.reply_wraper = convertView.findViewById(R.id.reply_wraper);
			vh.commentitem_name = (TextView) convertView
					.findViewById(R.id.commentitem_name);
			vh.replyTimePoint_tv = (TextView) convertView
					.findViewById(R.id.replyTimePoint_tv);
			convertView.setTag(R.layout.shareranking_detail_item1, vh);
		} else {
			vh = (ViewHolder) convertView
					.getTag(R.layout.shareranking_detail_item1);
		}
		bindView(convertView, position, vh);
		return convertView;
	}
	private void bindView(View convertView, int pos, ViewHolder vh) {
		final CommentItem item = getItem(pos);
		// 根据性别加载头像
		final Contact commentContac = item.getCommentContact();
		if (commentContac != null) {
			commentContac.displayPhoto(vh.head);
			vh.head.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View paramView) {
					ContactInfoActivity.launch(mContext, commentContac);
				}
			});
			vh.commentitem_name.setText(commentContac.getSuitableName());
		}
		// 处理@某人颜色特效
		ExpressionUtil.showEmoteInListview(mContext, vh.content,
				ExpressionSizeType.middle, new String[] {
						item.getComent().getContent(),
						item.getComent().isCommentLayFirst() ? null : item
								.getCommentedContact().getSuitableName() });
		vh.replyTimePoint_tv.setText(BBSUtils.toHours(item.getComent()
				.getCommentTime(), true));
		vh.reply_wraper.setOnClickListener(new AdapterClickLis(pos, vh));
		vh.reply_wraper.setPadding(DimenUtils.dip2px(mContext, item.getComent()
				.isCommentLayFirst() ? 10 : 25),
				DimenUtils.dip2px(mContext, 5), 0, DimenUtils.dip2px(mContext,
						5));
	}

	private class AdapterClickLis implements OnClickListener {
		private int pos;
		private ViewHolder vh;

		public AdapterClickLis(int pos, ViewHolder vh) {
			super();
			this.pos = pos;
			this.vh = vh;
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.reply_wraper: {
					CommentItem comentItem = getItem(pos);
					TalkFragement talkFragement = TalkFragement.newFragment(FragmentType.dialog);
					talkFragement.show(((FragmentActivity) mContext)
							.getSupportFragmentManager(), "");
					String id = comentItem.getComent().getJidPG()
							+ comentItem.getComent().getGid()
							+ comentItem.getComent().getGidCreateTime()
							+ comentItem.getComent().getCid();
					id = id.trim().replace("-", "");
					talkFragement.setTypeTDialog(id);
					talkFragement.setCommentedSuitableName(comentItem
							.getCommentContact().getSuitableName());
					talkFragement.setSendmsgListenter(new SendmsgListenter() {
						@Override
						public void sendmsg(TalkFragement fragment, String str) {
							Log.i("CommentAdapter", "~str~" + str);
							CommentItem comentitem = getItem(pos);
							String toCid = null;
							if (comentitem.getComent().isCommentLayFirst()) {
								toCid = comentitem.getComent().getCid();
							} else {
								toCid = comentitem.getComent().getToCid();
							}
							TimeflyService.executeCommentPG(mContext,
									comentitem.getComent().getJidPG(),
									comentitem.getComent().getGid(), comentitem
											.getComent().getGidCreateTime(),
									str, toCid, comentitem.getCommentContact(),
									null);
						}
					});
				}
			}
		}
	}

	private static class ViewHolder {
		private ImageView head;
		private TextView content;
		private View reply_wraper;
		private TextView replyTimePoint_tv;
		private TextView commentitem_name;
	}

	public void addItem(CommentItem input) {
		data.addCommentItem(input);
	}
	@Override
	public int getCount() {
		return Math.min(data.list.size(), maxShowNum);
	}
	@Override
	public CommentItem getItem(int position) {
		return data.list.get(position);
	}
	public void addItems(ArrayList<CommentItem> comments) {
		data.addCommentItems(comments);
	}
	public void clearItems() {
		data.clear();
	}
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (itemChangedListener != null) {
			itemChangedListener.ItemChanged(getCount());
		}
	}
}
