package com.beem.project.btf.ui.fragment;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.network.BDLocator;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity;
import com.beem.project.btf.ui.activity.ImageGalleryActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.views.BottomPopupWindow;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.butterfly.vv.vv.utils.JsonParseUtils.FindParam;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

/**
 * 图片列表展示，独立组件 目前只是用gridview展示，以后可能会搞的很炫
 */
public class ImageListFragment extends Fragment implements IEventBusAction {
	private Context mContext;
	private View view;
	private GridView gridView;
	private SelectAdapter adpater;
	private BottomPopupWindow filterPopupWindow;
	public static final String IMAGELIST_FOLDERITEM = "imagelist_folderItem";
	private ImageFolderItem folderItem;
	private ExecutorService executor;
	private Uri photoUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		if (getArguments().containsKey(IMAGELIST_FOLDERITEM)) {
			folderItem = getArguments().getParcelable(IMAGELIST_FOLDERITEM);
		}
		executor = Executors.newCachedThreadPool();
		EventBus.getDefault().register(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
			return view;
		}
		view = inflater.inflate(R.layout.album_edit, container, false);
		adpater = new SelectAdapter(mContext, folderItem.getVVImages(),
				folderItem.getContact().getJid());
		gridView = (GridView) view.findViewById(R.id.gridview_ii);
		gridView.setAdapter(adpater);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/*if (arg2 != (adpater.getCount() - 1)) {
					if (folderItem.getVVImages().size() >= Constants.uploadpicMaxNum) {
						CToast.showToast(mContext, "该相册已经满15张，请删除部分图片再上传！", Toast.LENGTH_SHORT);
					} else {
						ImageGalleryActivity.launch(mContext, arg2, folderItem);
					}
				} else {
					// 添加图片
					filterPopupWindow = new BottomPopupWindow((Activity) mContext, itemsOnClick,
							PopupActionType.TAKEPHOTO);
					filterPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				}*/
				ImageGalleryActivity.launch(mContext, arg2, folderItem);
			}
		});
		return view;
	}

	/*	private PopupActionListener itemsOnClick = new PopupActionListener() {
			@Override
			public void itemsClick(PopupActionType type, int i) {
				// TODO Auto-generated method stub
				filterPopupWindow.dismiss();
				if (type == PopupActionType.TAKEPHOTO) {
					if (i == 0) {
						photoUri = BBSUtils.takePhoto(ImageListFragment.this, Constants.TAKEPHOTO);
					} else if (i == 1) {
						// 跳转到相册库
						Intent intent = new Intent();
						intent.setAction("android.intent.action.vv.camera.photo.main");
						startActivity(intent);
					}
				}
			}
		};*/
	// gridview Adapter网格控件适配器
	class SelectAdapter extends BaseAdapter {
		private ArrayList<VVImage> list;
		private Context mContext;
		private String myjid;
		private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.deafult_imgloading)
				.showImageForEmptyUri(R.drawable.deafult_imgloading)
				.showImageOnFail(R.drawable.deafult_imgloading)
				.cacheInMemory(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).delayBeforeLoading(100)
				.build();

		public SelectAdapter(Context context, ArrayList<VVImage> list,
				String myjid) {
			this.mContext = context;
			this.list = list;
			this.myjid = myjid;
		}
		@Override
		public int getCount() {
			return list.size();
		}
		public void setData(ArrayList<VVImage> list) {
			this.list = list;
		}
		@Override
		public Object getItem(int position) {
			return position;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.small_label_photos_image = (ImageView) convertView
						.findViewById(R.id.icon);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			bindview(position, convertView, myjid, viewHolder);
			return convertView;
		}
		private void bindview(int position, View v, String myjid, ViewHolder vh) {
			if (position == (getCount() - 1)) {
				//vh.small_label_photos_image.setImageResource(R.drawable.addcontacts_selector);
			} else {
			}
			ImageLoader.getInstance().displayImage(
					list.get(position).getPathThumb(),
					vh.small_label_photos_image, options);
		}

		class ViewHolder {
			public ImageView small_label_photos_image;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.TAKEPHOTO) {
			// 拍照上传
			String takePhotoPath = BBSUtils.getTakePhotoPath(getActivity());
			if (!TextUtils.isEmpty(takePhotoPath)) {
				ArrayList<String> paths = new ArrayList<String>();
				paths.add(takePhotoPath);
				ShareChangeAlbumAuthorityActivity.launch(mContext, folderItem,
						paths);
			} else {
				CToast.showToast(mContext, "拍照失败，请重新操作", Toast.LENGTH_SHORT);
			}
		}
	}

	public class ShareHandler extends
			AsyncTask<String, String, Map<String, Object>> {
		private Context context;
		private ProgressDialog progressDialog;
		private String path;

		public ShareHandler(Context context, String pathToImage) {
			progressDialog = new ProgressDialog(context);
			this.context = context;
			this.path = pathToImage;
		}
		@Override
		public void onPreExecute() {
			progressDialog.setMessage(context
					.getString(R.string.please_wait_picture_is_uploading));
			progressDialog.setCancelable(false);
			progressDialog.show();
			//			ContactService.getInstance().synGeoInfo();
		}
		@Override
		protected Map<String, Object> doInBackground(String... strings) {
			return handlePostButtonClick();
		}
		@Override
		protected void onPostExecute(Map<String, Object> result) {
			EventBusData data = new EventBusData();
			data.setAction(EventAction.UploadTimeflyPhotoAdd);
			if (JsonParseUtils.getResult(result)) {
				progressDialog
						.setMessage(getString(R.string.image_uploaded_successfully));
				VVImage image = new VVImage();
				Map<String, String> dataMap = JsonParseUtils.getParseValue(
						result, Map.class, new FindParam("data", 0));
				image.setPath(dataMap.get("photo_big"));
				image.setPathThumb(dataMap.get("photo_small"));
				image.setPid(dataMap.get("pid"));
				image.setGid(dataMap.get("gid"));
				image.setCreateTime(dataMap.get("gid_create_time"));
				image.setJid(LoginManager.getInstance().getJidParsed());
				folderItem.getVVImages().add(image);
				data.setMsg(folderItem);
				EventBus.getDefault().post(data);// folderItem
			} else {
				Toast.makeText(BeemApplication.getContext(),
						getActivity().getString(R.string.image_upload_failed),
						Toast.LENGTH_LONG).show();
			}
			progressDialog.cancel();
		}
		public Map<String, Object> handlePostButtonClick() {
			return TimeflyService.uploadPicture(folderItem.getImageFolder()
					.getAuthority(), String.valueOf(BDLocator.getInstance()
					.getLat()), String
					.valueOf(BDLocator.getInstance().getLon()), path,
					folderItem.getImageFolder().getGid(), folderItem
							.getImageFolder().getCreateTime());
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		executor.shutdownNow();
		executor = null;
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		EventAction action = data.getAction();
		if (action.equals(EventAction.UploadTimeflyPhotoAdd)) {
			folderItem = (ImageFolderItem) data.getMsg();
			//LogUtils.i("uploadTimeflyPhotoAdd vvimage size:" + folderItem.getVVImages().size());
			adpater.setData(folderItem.getVVImages());
			adpater.notifyDataSetChanged();
		} else if (action.equals(EventAction.TimeflyImageDelete)) {
			String[] imageid = (String[]) data.getMsg();
			folderItem.getVVImages().remove(Integer.parseInt(imageid[1]));
			adpater.notifyDataSetChanged();
		}
	}
}
