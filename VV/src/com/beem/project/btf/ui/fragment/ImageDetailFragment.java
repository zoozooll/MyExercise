package com.beem.project.btf.ui.fragment;

import java.io.File;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.BottomPopupWindow;
import com.beem.project.btf.ui.views.BottomPopupWindow.PopupActionListener;
import com.beem.project.btf.ui.views.BottomPopupWindow.PopupActionType;
import com.beem.project.btf.ui.views.SimpleDilaogView;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.teleca.jamendo.api.WSError;

import de.greenrobot.event.EventBus;

public class ImageDetailFragment extends Fragment {
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private static final String TAG = "ImageDetailFragment";
	private Context mContext;
	private ImageFolderItem myimageFolderItem;
	private int mposition;
	private Contact mContact;
	private BottomPopupWindow filterPopupWindow;
	private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.transparent)
			.showImageOnFail(R.drawable.transparent).cacheInMemory(true)
			.cacheOnDisk(true).build();
	private String sex = "2";
	private AnimationDrawable animationDrawable;
	private boolean isContactInfoEdit;

	public static ImageDetailFragment newInstance(
			ImageFolderItem MyimageFolderItem, int position, String sex,
			boolean isContactInfoEdit) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putParcelable("shareTrancItem", MyimageFolderItem);
		args.putInt("position", position);
		args.putString("sex", sex);
		args.putBoolean("isContactInfoEdit", isContactInfoEdit);
		f.setArguments(args);
		return f;
	}
	public static ImageDetailFragment newInstance(
			ImageFolderItem MyimageFolderItem, int position, String sex) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putParcelable("shareTrancItem", MyimageFolderItem);
		args.putInt("position", position);
		args.putString("sex", sex);
		f.setArguments(args);
		return f;
	}
	public static ImageDetailFragment newInstance(
			ImageFolderItem MyimageFolderItem, int position) {
		return newInstance(MyimageFolderItem, position, "2");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// onCreate方法里获取传入的数据包
		super.onCreate(savedInstanceState);
		myimageFolderItem = getArguments().getParcelable("shareTrancItem");
		mposition = getArguments().getInt("position");
		sex = getArguments().getString("sex");
		isContactInfoEdit = getArguments().getBoolean("isContactInfoEdit");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment,
				container, false);
		mContext = getActivity();
		mContact = myimageFolderItem.getContact();
		mImageView = (ImageView) v.findViewById(R.id.pagerimage);
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (filterPopupWindow != null && filterPopupWindow.isShowing()) {
					filterPopupWindow.dismiss();
				}
				if (mContact == null) {
					// 联系人为空的情况
					filterPopupWindow = new BottomPopupWindow(
							(Activity) mContext, new MenuIMGsrcOnclickLT(
									mposition), PopupActionType.SAVE);
					filterPopupWindow.showAtLocation(v, Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
				} else {
					String photo_big = mContact.getPhoto_big();
					StringBuilder builder = new StringBuilder();
					if (!TextUtils.isEmpty(photo_big)) {
						String[] splitPhotos = BBSUtils.splitPhotos(
								DBKey.photo_big, photo_big);
						builder.append(splitPhotos);
					}
					if ((isContactInfoEdit && builder.length() == 0)) {
						if (filterPopupWindow != null) {
							filterPopupWindow.dismiss();
							filterPopupWindow = null;
						}
					} else {
						if (LoginManager.getInstance().isMyJid(
								mContact.getJid())) {
							filterPopupWindow = new BottomPopupWindow(
									(Activity) mContext,
									new MenuIMGsrcOnclickLT(mposition),
									PopupActionType.SAVE_DEL);
							filterPopupWindow.showAtLocation(v, Gravity.BOTTOM
									| Gravity.CENTER_HORIZONTAL, 0, 0);
						} else {
							filterPopupWindow = new BottomPopupWindow(
									(Activity) mContext,
									new MenuIMGsrcOnclickLT(mposition),
									PopupActionType.SAVE);
							filterPopupWindow.showAtLocation(v, Gravity.BOTTOM
									| Gravity.CENTER_HORIZONTAL, 0, 0);
						}
					}
				}
				return false;
			}
		});
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				if (getActivity() != null) {
					getActivity().finish();
				}
			}
		});
		progressBar = (ProgressBar) v.findViewById(R.id.pagerloading);
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final String path = myimageFolderItem.getVVImages().get(mposition)
				.getPath();
		final String paththumb = myimageFolderItem.getVVImages().get(mposition)
				.getPathThumb();
		if (Integer.parseInt(sex) == 0 && TextUtils.isEmpty(path)) {
			mImageView.setImageResource(R.drawable.default_headw);
		} else if (Integer.parseInt(sex) == 1 && TextUtils.isEmpty(path)) {
			mImageView.setImageResource(R.drawable.default_head);
		} else {
			ImageLoader.getInstance().displayImage(paththumb, mImageView,
					defaultOptions, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
							// 缩略图下载好了再去下载大图
							mAttacher.update();
							ImageLoader.getInstance().displayImage(path,
									mImageView, defaultOptions,
									new SimpleImageLoadingListener() {
										@Override
										public void onLoadingStarted(
												String imageUri, View view) {
											//TODO
											progressBar
													.setVisibility(View.VISIBLE);
										}
										@Override
										public void onLoadingFailed(
												String imageUri, View view,
												FailReason failReason) {
											String message = null;
											switch (failReason.getType()) {
												case IO_ERROR:
													message = "下载错误";
													break;
												case DECODING_ERROR:
													message = "图片无法显示";
													break;
												case NETWORK_DENIED:
													message = "网络有问题，无法下载";
													break;
												case OUT_OF_MEMORY:
													message = "图片太大无法显示";
													break;
												case UNKNOWN:
													message = "未知的错误";
													break;
											}
											Log.i(TAG, "~~message~~" + message
													+ "mContext" + mContext);
											Toast.makeText(mContext, message,
													Toast.LENGTH_SHORT).show();
											progressBar
													.setVisibility(View.GONE);
											// 大图记载失败就加载缩略图
											// ImageLoader.getInstance().displayImage(paththumb, mImageView);
										}
										@Override
										public void onLoadingComplete(
												String imageUri, View view,
												Bitmap loadedImage) {
											progressBar
													.setVisibility(View.GONE);
											mAttacher.update();
										}
									});
						}
					});
		}
	}
	/* 保存本地 */
	private void SaveToSDCard(int position) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			CToast.showToast(BeemApplication.getContext(), "存储卡不可用，不能下载",
					Toast.LENGTH_SHORT);
			return;
		}
		VVImage operationImage = myimageFolderItem.getVVImages().get(position);
		ImageLoader.getInstance().loadImage(operationImage.getPath(),
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						super.onLoadingStarted(imageUri, view);
						UIHelper.showDialogForLoading(mContext, "正在下载,请稍后...",
								false);
					}
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						super.onLoadingFailed(imageUri, view, failReason);
						UIHelper.hideDialogForLoading();
						CToast.showToast(BeemApplication.getContext(), "下载失败，"
								+ failReason.getCause().toString(),
								Toast.LENGTH_SHORT);
					}
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						UIHelper.hideDialogForLoading();
						String saveName = imageUri;
						String[] splitPaths = imageUri.split("/");
						if (splitPaths != null && splitPaths.length > 0) {
							saveName = splitPaths[splitPaths.length - 1];
						}
						if (!saveName.contains("jpg")
								&& !saveName.contains(".bmp")
								&& !saveName.contains(".png")
								&& !saveName.contains("gif")) {
							saveName += ".jpg";
						}
						String assemblePath = BBSUtils
								.getSavedBitmapAbsulutePath(mContext, saveName);
						if (!new File(assemblePath).exists()) {
							BBSUtils.SaveBitmapToSDCard(mContext, loadedImage,
									assemblePath);
							CToast.showToast(BeemApplication.getContext(),
									"下载成功，图片路径：" + assemblePath,
									Toast.LENGTH_SHORT);
							PictureUtil.galleryAddPic(mContext, assemblePath);
						} else {
							CToast.showToast(BeemApplication.getContext(),
									"图片已下载，图片路径：" + assemblePath,
									Toast.LENGTH_SHORT);
						}
					}
				});
	}
	/* 删除图片 */
	private void DeleteImage(final int position) {
		final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
				mContext, R.style.blurdialog);
		SimpleDilaogView simpleDilaogView = new SimpleDilaogView(mContext);
		// 设置标题
		simpleDilaogView.setTitle("确定要删除这张图片吗?");
		// 分别设置两个按钮及定义操作
		simpleDilaogView.setPositiveButton("删除", new OnClickListener() {
			@Override
			public void onClick(View v) {
				blurDlg.dismiss();
				if (isContactInfoEdit) {
					EventBus.getDefault().post(
							new EventBusData(EventAction.SendBroadCastDel,
									position));
					getActivity().finish();
				} else {
					new VVBaseLoadingDlg<Map<String, Object>>(
							new VVBaseLoadingDlgCfg(mContext)
									.setShowWaitingView(true)) {
						@Override
						protected Map<String, Object> doInBackground() {
							VVImage operationImage = myimageFolderItem
									.getVVImages().get(position);
							String jid = LoginManager.getInstance()
									.getJidParsed();
							String pid = operationImage.getPid();
							String createTime = operationImage.getCreateTime();
							String gid = myimageFolderItem.getImageFolder()
									.getGid();
							Map<String, Object> result = null;;
							try {
								result = TimeflyService
										.deletePhoto(jid, pid, createTime, gid);
								if (JsonParseUtils.getResult(result)) {
									// 删除图片
									VVImage.delete(jid, gid, createTime, pid);
									myimageFolderItem.getVVImages().remove(
											operationImage);
									ImageFolder folder = myimageFolderItem
											.getImageFolder();
									int newFolderCount = folder.getPhotoCount() - 1;
									if (newFolderCount == 0) {
										// 删除文件夹
										ImageFolder.delete(jid, gid, createTime);
									} else {
										ImageFolder dbEditor = new ImageFolder();
										dbEditor.setField(DBKey.jid, jid);
										dbEditor.setField(DBKey.gid, gid);
										dbEditor.setField(DBKey.createTime,
												createTime);
										dbEditor.setField(DBKey.photoCount,
												newFolderCount);
										dbEditor.saveToDatabaseAsync();
									}
									// 通知时光界面刷新图片
									String[] imageid = { createTime,
											String.valueOf(position) };
									EventBus.getDefault().post(
											new EventBusData(
													EventAction.TimeflyImageDelete,
													imageid));
								}
							} catch (WSError e) {
								setManulaTimeOut(true);
								e.printStackTrace();
							}
							return result;
						}
						@Override
						protected void onPostExecute(Map<String, Object> result) {
							if (JsonParseUtils.getResult(result)) {
								CToast.showToast(mContext, "删除成功",
										Toast.LENGTH_SHORT);
								getActivity().finish();
							} else {
								CToast.showToast(
										mContext,
										"删除失败"
												+ JsonParseUtils
														.getDescription(result),
										Toast.LENGTH_SHORT);
							}
						}
					}.execute();
				}
			}
		});
		simpleDilaogView.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				blurDlg.dismiss();
			}
		});
		blurDlg.setContentView(simpleDilaogView.getmView());
		blurDlg.show();
	}

	class MenuIMGsrcOnclickLT implements PopupActionListener {
		private int position;

		public MenuIMGsrcOnclickLT(int position) {
			this.position = position;
		}
		@Override
		public void itemsClick(PopupActionType type, int i) {
			filterPopupWindow.dismiss();
			switch (type) {
				case SAVE: {
					if (i == 0) {
						SaveToSDCard(position);
					}
					break;
				}
				case SAVE_DEL: {
					if (i == 0) {
						SaveToSDCard(position);
					} else if (i == 1) {
						DeleteImage(position);
					}
					break;
				}
			}
		}
	}
}
