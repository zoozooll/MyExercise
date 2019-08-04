package com.butterfly.vv.camera;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity;
import com.beem.project.btf.ui.activity.ClipPictureActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.butterfly.vv.GalleryNavigation;
import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.base.PhotoChoiceChangeListener;
import com.butterfly.vv.camera.base.PhotosChooseManager;
import com.butterfly.vv.camera.photo.ChildAdapter;
import com.butterfly.vv.camera.photo.PhotoFolderGetData;
import com.butterfly.vv.camera.renew.RenewDetailBaseActivity;
import com.butterfly.vv.db.ormhelper.bean.VVImage;

import de.greenrobot.event.EventBus;

/**
 * 显示相册缩略图内容
 * @ClassName: ShowImageActivity
 * @Description: TODO
 * @author: zhenggen xie
 * @date: 2015年3月5日 下午2:45:05
 */
public class ShowImageActivity extends Activity implements IEventBusAction, PhotoChoiceChangeListener {
	private GridView mGridView;
//	private List<String> list;
	private ChildAdapter adapter;
	private ImageFolderInfo imageFolderInfo;
	public GalleryNavigation mMyNavigationView;
	private PhotosChooseManager chooser;
	public static final String IMAGE_VIEW_HOLDER_EXTRA = "image_view_holder_extra";
	public static final String IMAGE_DETAIL_TITLE_STRING = "image_detail_title_string";
	public static final String IMAGE_DETAIL_TYPE = "image_detail_type";
	public static final String IMAGE_DETAIL_CURR_INDEX = "image_detail_curr_index";
	public static final String IMAGE_DETAIL_COUNT = "image_detail_count";
	public static final String IMAGE_DETAIL_IMAGE_LIST = "image_detail_image_list";
	public static final String PREF_NAME_HIDE_PHOTO_FOLDER = "pref_name_hide_photo_folder";
	public static final String PREF_ITEM_FOLDER = "pref_item_folder";
	public static final int TO_HIDE_ACT_REQUEST_CODE = 62;
	public static final int TO_SYSTEM_CAMERA_REQUST_CODE = 63;
	public static final int TO_HIDE_ACT_RESULT_CODE = 82;
	public static final String TRANSFER_PHOTO_FOLDER_STRING = "transfer_photo_folder_string";
	public boolean dopick;
	private Intent lastIntent;
	private Uri photoUri;
	private String picPath;
	/** 从Intent获取图片路径的KEY */
	public static final String KEY_PHOTO_PATH = "photo_path";
	public static final int SELECT_PIC_BY_PICK_PHOTO_SHOW = 3;
	private ArrayList<VVImage> tempVvImages = new ArrayList<VVImage>();
	// 上传图片返回的结果
	private static final int CLIP_PICTURE_B = 30;
	private static final String tag = ShowImageActivity.class.getSimpleName();
	public static final String TAG = ShowImageActivity.class.getSimpleName();
	private int type;
	private static final int CLIP_PICTURE_A = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showimage);
		chooser = PhotosChooseManager.getInstance();
		mGridView = (GridView) findViewById(R.id.photo_gv);
		dopick = getIntent().getBooleanExtra("dopick", false);
		type = getIntent().getIntExtra(CameraActivity.CAMERA_GALLERY_TYPE, -1);
		imageFolderInfo = (ImageFolderInfo) getIntent().getSerializableExtra(TRANSFER_PHOTO_FOLDER_STRING);
		String folderName = imageFolderInfo.getmFolderPath();
		List<String> list = PhotoFolderGetData.getInstance().getImageList(folderName);
		mMyNavigationView = (GalleryNavigation) findViewById(R.id.title_layout);
		//		mMyNavigationView.setStringTitle(getTitleString(), "(" + imageFolderInfo.mFolderImgCount + getString(R.string.str_img_piece) + ")");
		mMyNavigationView.setStringTitle(imageFolderInfo.getmFolderName(), "(" + imageFolderInfo.getmFolderImgCount()
				+ getString(R.string.str_img_piece) + ")");
		NavigationViewRightBtnListener rBtnListener = new NavigationViewRightBtnListener();
		mMyNavigationView.setBtnUploadListener(rBtnListener);
		mMyNavigationView.setCameraBtnHide(true);
		//		mMyNavigationView.setCameraListener(rBtnListener);
		//		mMyNavigationView.setBtnRightListener(rBtnListener);
		mMyNavigationView.setBtnLeftListener(mNaviLeftBtnListener);
		adapter = new ChildAdapter(this, imageFolderInfo, list, mGridView, type);
		adapter.setPhotoCheckedListener(this);
		adapter.getFolderImageFile();
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//				if (!dopick) {
				int count = parent.getCount();
				ChildAdapter tempAdapter = (ChildAdapter) parent.getAdapter();
				ImageInfoHolder imageInfo = tempAdapter.mImageInfoList.get(position);
				int currentCount = position + 1;
				String titleString = imageFolderInfo.getmFolderName();
				RenewDetailBaseActivity.setBigImageInfoList(tempAdapter.mImageInfoList);
				Intent intent = new Intent(ShowImageActivity.this, RenewDetailBaseActivity.class);
				intent.putExtra(IMAGE_VIEW_HOLDER_EXTRA, imageInfo);
				intent.putExtra(IMAGE_DETAIL_TITLE_STRING, titleString);
				intent.putExtra(CameraActivity.IMAGE_DETAIL_TYPE, RenewDetailBaseActivity.PHOTO_GRID_BIG_IMAGE_TYPE);
				intent.putExtra(CameraActivity.IMAGE_DETAIL_CURR_INDEX, currentCount);
				intent.putExtra(CameraActivity.IMAGE_DETAIL_COUNT, count);
				intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, type);
				startActivityForResult(intent, CameraActivity.REQUESTCODE_ENTER);
				//				} else {
				/*				ChildAdapter tempAdapter = (ChildAdapter) parent.getAdapter();
								ImageInfoHolder imageInfo = tempAdapter.mImageInfoList.get(position);
								lastIntent = new Intent();
								lastIntent.putExtra(KEY_PHOTO_PATH, imageInfo.mImagePath);
								lastIntent.setClass(ShowImageActivity.this, ClipPictureActivity.class);
								startActivity(lastIntent);*/
				//				}
			}
		});
		EventBus.getDefault().register(this);
	}
	/**
	 * 获取标题名称
	 * @Title: getTitleString
	 * @Description: TODO
	 * @return
	 * @return: String
	 */
	private String getTitleString() {
		String titleString = imageFolderInfo.getmFolderName();
		String strPiece = getResources().getString(R.string.str_img_piece);
		titleString = titleString + "(" + imageFolderInfo.getmFolderImgCount() + strPiece + ")";
		return titleString;
	}

	private OnClickListener mNaviLeftBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	private int msg;

	public void naviBtnGoBack() {
		adapter.removeAllSelectedItems();
		updateTitleTextImage();
	}

	public class NavigationViewRightBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (type) {
				case CameraActivity.GALLERY_TYPE_CONTACTCARD: {
					Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
					if (checkListimagespath != null) {
						String path = checkListimagespath.iterator().next().mImagePath;
						ClipPictureActivity.launch(ShowImageActivity.this, path, CLIP_PICTURE_A);
						setResult(CameraActivity.RESULTCODE_ENTER_CONFIRM);
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
					}
				}
					break;
				case CameraActivity.GALLERY_TYPE_NEWSTV:
				case CameraActivity.GALLERY_TYPE_NEWSTOPTITLE: {
					Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
					if (checkListimagespath != null) {
						//						String path = checkListimagespath.iterator().next().mImagePath;
						//						photoUri=Uri.fromFile(new File(path));
						//						PictureUtil.photoClip(ShowImageActivity.this,photoUri,900,602);
						//						PictureUtil.photoClipInner(ShowImageActivity.this,path, 0.6666667f);
						setResult(CameraActivity.RESULTCODE_ENTER_CONFIRM);
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
					}
					break;
				}
				case CameraActivity.GALLERY_TYPE_TIME: {
					setResult(CameraActivity.RESULTCODE_ENTER_CONFIRM);
					finish();
				}
					break;
				case CameraActivity.GALLERY_TYPE_REGISTER: {
					Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
					if (checkListimagespath != null) {
						String path = checkListimagespath.iterator().next().mImagePath;
						ClipPictureActivity.launch(ShowImageActivity.this, path, CLIP_PICTURE_A, true);
						setResult(CameraActivity.RESULTCODE_ENTER_CONFIRM);
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
					}
				}
					break;
				case CameraActivity.GALLERY_TYPE_CHAT: {
					Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
					if (checkListimagespath != null) {
						String imgepath = checkListimagespath.iterator().next().mImagePath;
						EventBus.getDefault().post(new EventBusData(EventAction.SendPathTChat, imgepath));
						setResult(CameraActivity.RESULTCODE_ENTER_CONFIRM);
						finish();
					}
				}
					break;
				default: {
					Intent intent2 = new Intent(ShowImageActivity.this, ShareChangeAlbumAuthorityActivity.class);
					startActivity(intent2);
				}
					break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	private void updateTitleTextImage() {
		int selectedCount = chooser.getChoosedCount();
		if (selectedCount > 0) {
			String choiseText;
			if (type == -1) {
				choiseText = getResources().getString(R.string.str_had_selected, chooser.getChoosedCount(),
						chooser.getChooseCountMax());
			} else {
				choiseText = getString(R.string.login_ok);
			}
			mMyNavigationView.setChoiseMode(choiseText);
		} else {
			//			mMyNavigationView.setChoiceMode(false, getTitleString());
			//			mMyNavigationView.setRight_drawable(R.drawable.vv_navi_add_selector_new);
			mMyNavigationView.cancelChoiseMode();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		if (EventAction.ShowImageTitleChange.equals(data.getAction())) {
			msg = (Integer) data.getMsg();
			if (msg == 0) {
				naviBtnGoBack();
				clearSelectedInfo();
			} else {
				updateTitleTextImage();
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*if (requestCode == Constants.takephoto && resultCode == Activity.RESULT_OK) {
			picPath = BBSUtils.getTakePhotoPath(ShowImageActivity.this, photoUri, data);
			if (picPath != null) {
				ClipPictureActivity.launch(ShowImageActivity.this, picPath, requestCode);
			} else {
				Toast.makeText(ShowImageActivity.this, "选择图片文件不正确", Toast.LENGTH_SHORT).show();
			}
		} else*/ 
		if (requestCode == CLIP_PICTURE_B && resultCode == Activity.RESULT_OK) {
			String[] savePhoto = data.getStringArrayExtra("savePhoto");
			if (savePhoto != null) {
				int size = tempVvImages.size();
				VVImage vvImage = new VVImage();
				vvImage.setPath(savePhoto[0]);
				vvImage.setPathThumb(savePhoto[1]);
				if (size == 8) {
					tempVvImages.set(size - 1, vvImage);
				} else if (size < 8) {
					tempVvImages.add(vvImage);
				}
				adapter.notifyDataSetChanged();
			}
		} else if (requestCode == CameraActivity.REQUESTCODE_ENTER) {
			// 从更深层图库界面，例如相册详情，照片详情，返回的信息。
			// 当返回数据的为确认图片状态时，退出本界面，且返回一个数据给调用者
			if (resultCode == CameraActivity.RESULTCODE_ENTER_CONFIRM) {
				setResult(resultCode);
				finish();
			}
		} else if (requestCode == Constants.CLIPPHOTO) {
			//调用系统剪切功能的返回接收
			EventBus.getDefault().post(new EventBusData(EventAction.SendClipPhoto, photoUri));
			EventBus.getDefault().post(new EventBusData(EventAction.FinishActivity, null));
			finish();
		}
		if (resultCode == 0 && requestCode == 100) {
			clearSelectedInfo();
		}
	}
	private void clearSelectedInfo() {
		adapter.removeAllSelectedItems();// 清除被选中的图片记录
		adapter.notifyDataSetChanged();
	}
	@Override
	protected void onStart() {
		super.onStart();
		updateTitleTextImage();
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onPhotoChoiceChange() {
		int selectedCount = PhotosChooseManager.getInstance().getChoosedCount();
		if (selectedCount == 0) {
			naviBtnGoBack();
			clearSelectedInfo();
		} else {
			updateTitleTextImage();
		}
	}
}
