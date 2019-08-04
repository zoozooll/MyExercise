package com.beem.project.btf.ui.fragment;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.R.drawable;
import com.beem.project.btf.bbs.bean.DraftInfo;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.DraftDao;
import com.beem.project.btf.ui.activity.ChatActivity;
import com.beem.project.btf.ui.entity.EmotionInfo;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.views.HandlerConstant;
import com.beem.project.btf.ui.views.MediaRecordHandler;
import com.beem.project.btf.ui.views.VolumeWaveView;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.ExpressionUtil;
import com.beem.project.btf.utils.ExpressionUtil.ExpressionSizeType;
//import com.butterfly.vv.camera.CameraActivity;
import com.butterfly.vv.camera.GalleryActivity;

import de.greenrobot.event.EventBus;

/**
 * @author le yang
 * @category 此fragment在聊天界面显示为普通的fragment，而在评论时作为一个弹出框显示(dialogFragment)
 */
@SuppressLint("ValidFragment")
public class TalkFragement extends DialogFragment implements IEventBusAction {
	private static final String TAG = TalkFragement.class.getSimpleName();
	private ViewPager emotion_viewpager;
	private ArrayList<View> listViews = new ArrayList<View>();
	private LinearLayout emotion_selectedllt;
	// 匹配表情的正则表达式数组
	private String regularExpression[] = new String[] { "zem[0-9]{1,2}" };
	private Context mContext;
	private EditText msg_edit;
	private ImageButton sendmsg_btn, face_btn, del_btn, voice_input_btn;
	private LinearLayout face_wraper, face_wraper_emotion, face_wraper_btn;
	private Button mTakePhotoBtn, mTakeCameraBtn;
	private FrameLayout msg_edit_wraper;
	private Button voice_btn;
	private int numColumn = 6;
	private boolean longClicked = false;
	private static final int MSG = 1;
	/** 新加的start */
	private Dialog soundVolumeDialog = null;
	private FrameLayout volume_dialog_wraper;
	private RelativeLayout volume_dialog_anim_wraper;
	private TextView volume_dialog_time, volume_dialog_note;
	private TextView volume_dialog_icon;
	private Animation fadeout, fadein;
	private VolumeWaveView waveView;
	private Thread audioRecorderThread = null;
	private MediaRecordHandler audioRecorderInstance = null;
	private IActionCallback actionCallBack;
	public static final String PATTERN = "[a-z]em[0-9]{1,2}";
	private DraftDao draftDao;
	private DraftInfo draftInfoTDialog;
	private String content;
	private String id;
	private String suitName;
	private List<TextWatcher> watchers = Collections
			.synchronizedList(new ArrayList<TextWatcher>());;
	//用于存放TalkFragment在onCreateView后执行的任务
	private List<Runnable> postRunnable = Collections
			.synchronizedList(new ArrayList<Runnable>());
	
	private DialogInterface.OnCancelListener onCancelListener;

	/**
	 * VOICEINPUT语音输入 NORMALINPUT普通输入法输入 FACEINPUT表情输入 IMAGEINPUT图片输入
	 */
	private enum ToggleType {
		VOICEINPUT, NORMALINPUT, FACEINPUT, IMAGEINPUT
	}

	// 发送消息接口
	private SendmsgListenter sendmsgListenter;

	public enum FragmentType {
		normal, dialog
	}

	private FragmentType ftype;
	
	public TalkFragement() {
		
	}

	public static  TalkFragement newFragment(FragmentType type) {
		TalkFragement f = new TalkFragement();
		Bundle data = new Bundle();
		data.putSerializable("ftype", type);
		f.setArguments(data);
		return f;
	}
	public void setSendmsgListenter(SendmsgListenter sendmsgListenter) {
		this.sendmsgListenter = sendmsgListenter;
	}

	public interface SendmsgListenter {
		public void sendmsg(TalkFragement fragment, String str);
	}

	// 操作回调类
	public interface IActionCallback {
		// 返回拍照后,选择相册照片后的Uri
		public void onTakePhoto(Uri photoUri);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG: {
					if (msg_edit != null) {
						String editStr = msg_edit.getText().toString().trim();
						if (editStr.length() > 0) {
							// 模拟删除
							msg_edit.onKeyDown(KeyEvent.KEYCODE_DEL,
									new KeyEvent(KeyEvent.ACTION_DOWN,
											KeyEvent.KEYCODE_DEL));
						}
					}
					break;
				}
				case HandlerConstant.RECEIVE_MAX_VOLUME: {
					// 更新对话框视图
					float[] Tmsg = (float[]) msg.obj;
					waveView.updataAM(0, Tmsg[0]);
					volume_dialog_time.setText(((int) Tmsg[1]) + "＂");
					break;
				}
				case HandlerConstant.RECORD_AUDIO_TOO_LONG: {
					// 录音超时处理
					int recordSecond = msg.getData().getInt("recordTime");
					String fileName = msg.getData().getString("fileName");
					doFinishRecordAudio();
					onRecordVoiceEnd(fileName, recordSecond);
					break;
				}
				case HandlerConstant.HANDLER_RECORD_FINISHED: {
					// 录音结束处理
					int recordSecond = msg.getData().getInt("recordTime");
					String fileName = msg.getData().getString("fileName");
					onRecordVoiceEnd(fileName, recordSecond);
					break;
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			ftype = (FragmentType) args.get("ftype");
		}
		if (ftype == FragmentType.dialog) {
			setStyle(STYLE_NO_FRAME, 0);
		}
		EventBus.getDefault().register(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//		Log.i(TAG, "onCreateView");
		mContext = getActivity();
		View rootview = inflater.inflate(R.layout.talkfragement_layout,
				container, false);
		sendmsg_btn = (ImageButton) rootview.findViewById(R.id.sendmsg_btn);
		emotion_selectedllt = (LinearLayout) rootview
				.findViewById(R.id.emotion_selectedllt);
		msg_edit = (EditText) rootview.findViewById(R.id.msg_edit);
		face_wraper = (LinearLayout) rootview.findViewById(R.id.face_wraper);
		face_wraper_emotion = (LinearLayout) rootview
				.findViewById(R.id.face_wraper_emotion);
		face_wraper_btn = (LinearLayout) rootview
				.findViewById(R.id.face_wraper_btn);
		mTakePhotoBtn = (Button) rootview.findViewById(R.id.takephoto_btn);
		mTakeCameraBtn = (Button) rootview.findViewById(R.id.takecamera_btn);
		voice_input_btn = (ImageButton) rootview
				.findViewById(R.id.voice_input_btn);
		voice_btn = (Button) rootview.findViewById(R.id.voice_btn);
		msg_edit_wraper = (FrameLayout) rootview
				.findViewById(R.id.msg_edit_wraper);
		face_btn = (ImageButton) rootview.findViewById(R.id.face_btn);
		del_btn = (ImageButton) rootview.findViewById(R.id.del_btn);
		emotion_viewpager = (ViewPager) rootview
				.findViewById(R.id.emotion_viewpager);
		detectKeyboardShow(mContext);
		setContent(content);
		if (ftype == FragmentType.dialog) {
			// 作为弹出框使用
			sendmsg_btn.setTag(ToggleType.NORMALINPUT.toString());
			sendmsg_btn.setImageResource(R.drawable.send_msg);
			voice_input_btn.setImageBitmap(null);
			voice_input_btn
					.setPadding(DimenUtils.dip2px(mContext, 15), 0, 0, 0);
			getDialog().setCanceledOnTouchOutside(true);
			getDialog().setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					setFaceVisible(ToggleType.NORMALINPUT);
				}
			});
			Window window = getDialog().getWindow();
			WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
			int[] wh = BBSUtils.getDeviceWH(mContext);
			windowParams.x = 0;
			windowParams.y = wh[1];
			window.setAttributes(windowParams);
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			if (!TextUtils.isEmpty(suitName)) {
				msg_edit.setHint("@" + suitName + ":");
			}
			if (!TextUtils.isEmpty(id)) {
				draftDao = new DraftDao(mContext);
				draftInfoTDialog = draftDao.query(id, "dialog");
				if (draftInfoTDialog != null) {
					setContent(draftInfoTDialog.getContent());
				}
			}
		} else {
			// 作为普通fragment使用
			mTakeCameraBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 部分手机调用Uri takePhoto(Activity activity, int requestCode)出现问题
					BBSUtils.takePhoto(getActivity());
				}
			});
			mTakePhotoBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 跳转到相册库
					//					Log.i(TAG, "onTakePhotoBtnClick btn");
					/*Intent intent = new Intent();
					intent.setAction("android.intent.action.vv.camera.photo.main");
					intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, CameraActivity.GALLERY_TYPE_CHAT);
					startActivity(intent);*/
					Intent intent = new Intent(mContext, GalleryActivity.class);
					intent.putExtra(GalleryActivity.GALLERY_PICKABLE, true);
					intent.putExtra(GalleryActivity.GALLERY_CHOOSE_MAX, 6);
					intent.putExtra(GalleryActivity.GALLERY_FROM_CAMERA, true);
					intent.putExtra(GalleryActivity.GALLERY_CROP, false);
					getActivity().startActivityForResult(intent,
							Constants.PICKPHOTO);
				}
			});
			voice_input_btn.setOnClickListener(new OnClickListener() {
				// 用于切换到语音输入
				@Override
				public void onClick(View paramView) {
					String tag = (String) voice_input_btn.getTag();
					if (tag.equals(ToggleType.NORMALINPUT.toString())) {
						setFaceVisible(ToggleType.VOICEINPUT);
					} else {
						setFaceVisible(ToggleType.NORMALINPUT);
					}
				}
			});
			// 录音监听
			voice_btn.setOnTouchListener(new voiceListener());
			// 初始化音量框
			initSoundVolumeDlg();
		}
		// 表情按钮监听
		face_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				// 填充表情列表GridView
				if (face_btn.isSelected()) {
					setFaceVisible(ToggleType.NORMALINPUT);
				} else {
					setFaceVisible(ToggleType.FACEINPUT);
				}
			}
		});
		// 发送信息
		sendmsg_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				String tag = (String) sendmsg_btn.getTag();
				if (tag.equals(ToggleType.NORMALINPUT.toString())) {
					if (sendmsgListenter != null) {
						sendmsgListenter.sendmsg(TalkFragement.this, msg_edit
								.getText().toString().trim());
						msg_edit.setText("");
					}
					if (ftype == FragmentType.dialog) {
						closeInput();
						dismiss();
					}
				} else {
					// 图片输入布局
					if (sendmsg_btn.isSelected()) {
						setFaceVisible(ToggleType.NORMALINPUT);
					} else {
						setFaceVisible(ToggleType.IMAGEINPUT);
					}
				}
			}
		});
		// 循环删除字符
		del_btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
				if (paramMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
					longClicked = true;
					Thread t = new Thread() {
						@Override
						public void run() {
							super.run();
							while (longClicked) {
								mHandler.sendEmptyMessage(MSG);
								try {
									Thread.sleep(250);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						};
					};
					t.start();
				} else if (paramMotionEvent.getAction() == MotionEvent.ACTION_UP) {
					longClicked = false;
				}
				return true;
			}
		});
		// 添加两个页面内容(根据分组数量添加)
		for (int i = 0; i < emotion_selectedllt.getChildCount(); i++) {
			listViews.add(inflater.inflate(R.layout.bbs_addemotion_item, null));
			emotion_selectedllt.getChildAt(i).setOnClickListener(
					new MyOnClickListener(i));
		}
		// 设置表情viewpager
		emotion_viewpager.setAdapter(pagerAdapter);
		emotion_viewpager.setCurrentItem(0);
		emotion_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int paramInt) {
				setselected(paramInt);
			}
			@Override
			public void onPageScrolled(int paramInt1, float paramFloat,
					int paramInt2) {
			}
			@Override
			public void onPageScrollStateChanged(int paramInt) {
			}
		});
		setEditListener();
		setselected(0);
		setGridViewAdapter();
		executePostRunnable();
		return rootview;
	}
	public void setActionCallBack(IActionCallback actionCallBack) {
		this.actionCallBack = actionCallBack;
	}

	/**
	 * ########################################处理表情相关方法start######################################
	 */
	// 表情viewpager 适配器
	PagerAdapter pagerAdapter = new PagerAdapter() {
		@Override
		public boolean isViewFromObject(View paramView, Object paramObject) {
			return paramView == paramObject;
		}
		@Override
		public int getCount() {
			return listViews.size();
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (listViews.get(position).getParent() == null) {
				container.addView(listViews.get(position));
			}
			return listViews.get(position);
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(listViews.get(position));
		}
	};

	@Override
	public void onCancel(DialogInterface dialog) {
		if (onCancelListener != null) {
			onCancelListener.onCancel(dialog);
		}
	}
	
	

	// 填充表情列表GridView方法
	private void setGridViewAdapter() {
		for (int i = 0; i < listViews.size(); i++) {
			// 根据不同的规则匹配不同表情，并填充到列表中
			final int Index = i;
			final GridView emotiongridview = (GridView) listViews.get(Index)
					.findViewById(R.id.sendpost_expressions);
			final ProgressBar progressBar = (ProgressBar) listViews.get(Index)
					.findViewById(R.id.emotion_Loadingbar);
			emotiongridview.setNumColumns(numColumn);
			new AsyncTask<Void, Integer, ArrayList<EmotionInfo>>() {
				@Override
				protected void onPreExecute() {
					progressBar.setVisibility(View.VISIBLE);
				};
				@Override
				protected ArrayList<EmotionInfo> doInBackground(Void... params) {
					ArrayList<EmotionInfo> emotionInfos = new ArrayList<EmotionInfo>();
					emotionInfos = loadExpression(regularExpression[Index],
							emotionInfos);
					return emotionInfos;
				}
				@Override
				protected void onPostExecute(final ArrayList<EmotionInfo> result) {
					progressBar.setVisibility(View.GONE);
					// 在第一行最后一列插入空白
					if (result.size() >= numColumn) {
						EmotionInfo emInfo = new EmotionInfo();
						emInfo.setResid(R.drawable.transparent);
						result.add(numColumn - 1, emInfo);
					}
					emotiongridview.setAdapter(new ExpressionItemAdapter(
							mContext, result, Index));
					emotiongridview
							.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(
										AdapterView<?> paramAdapterView,
										View paramView, int paramInt,
										long paramLong) {
									if (paramInt != numColumn - 1) {
										if (msg_edit != null) {
											// 插入表情
											EmotionInfo emInfo = result
													.get(paramInt);
											String rename = emInfo.getResname();
											Integer reid = emInfo.getResid();
											SpannableString spannableString = ExpressionUtil
													.getExpressionString(
															mContext, rename,
															reid);
											int index = msg_edit
													.getSelectionStart();
											if (index >= 0
													&& index < msg_edit
															.getText()
															.toString()
															.length()) {
												msg_edit.getText().insert(
														index, spannableString);
											} else {
												msg_edit.getText().append(
														spannableString);
											}
											char[] chs = new char[index];
											msg_edit.getText().getChars(0,
													index, chs, 0);
											String inserPrevStr = String
													.valueOf(chs);
											Pattern pattern = Pattern
													.compile(new StringBuffer()
															.append(".*")
															.append(ChatActivity.PATTERN)
															.append("$")
															.toString());
											Matcher matcher = pattern
													.matcher(inserPrevStr);
											boolean isEmotionEnd = matcher
													.matches();
										}
									}
								}
							});
				};
			}.execute();
		}
	}
	// 加载表情的方法
	public ArrayList<EmotionInfo> loadExpression(String regularExpression,
			ArrayList<EmotionInfo> ids) {
		Class<drawable> drawable = R.drawable.class;
		Field[] fields = drawable.getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			if (field.getName().matches(regularExpression)) {
				try {
					int id = field.getInt(null);
					EmotionInfo emoinfo = new EmotionInfo();
					emoinfo.setResid(id);
					emoinfo.setResname(name);
					ids.add(emoinfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ids;
	}

	// 表情列表GridView适配器
	public class ExpressionItemAdapter extends BaseAdapter {
		private Context c;
		private ArrayList<EmotionInfo> ids;
		private int pageTag = 0;

		public ExpressionItemAdapter(Context c, ArrayList<EmotionInfo> ids,
				int index) {
			this.c = c;
			this.ids = ids;
			pageTag = index;
		}
		@Override
		public int getCount() {
			return ids.size();
		}
		@Override
		public EmotionInfo getItem(int position) {
			return ids.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) c
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.message_emote_item,
						null);
				holder = new ViewHolder();
				holder.expression_image = (ImageView) convertView
						.findViewById(R.id.emote_imageview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.expression_image.setImageResource(getItem(position)
					.getResid());
			return convertView;
		}

		class ViewHolder {
			public ImageView expression_image;
		}
	}

	// 底部标签选择器
	private void setselected(int selectItems) {
		for (int i = 0; i < listViews.size(); i++) {
			if (i == selectItems) {
				emotion_selectedllt.getChildAt(i).setSelected(true);
			} else {
				emotion_selectedllt.getChildAt(i).setSelected(false);
			}
		}
	}

	// 标签页点击监听
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			emotion_viewpager.setCurrentItem(index);
		}
	}

	/**
	 * ########################################处理表情相关方法end##########################################
	 */
	// 设置输入框相关监听器
	private void setEditListener() {
		if (msg_edit != null) {
			msg_edit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setFaceVisible(ToggleType.NORMALINPUT);
				}
			});
			// 弹出框使用时不设置文本内容监听
			msg_edit.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence paramCharSequence,
						int paramInt1, int paramInt2, int paramInt3) {
					for (TextWatcher watcherOne : watchers) {
						watcherOne.onTextChanged(paramCharSequence, paramInt1,
								paramInt2, paramInt3);
					}
					if (ftype == FragmentType.dialog) {
					}
				}
				@Override
				public void beforeTextChanged(CharSequence paramCharSequence,
						int paramInt1, int paramInt2, int paramInt3) {
					for (TextWatcher watcher : watchers) {
						watcher.beforeTextChanged(paramCharSequence, paramInt1,
								paramInt2, paramInt3);
					}
				}
				@Override
				public void afterTextChanged(Editable paramEditable) {
					for (TextWatcher watcher : watchers) {
						watcher.afterTextChanged(paramEditable);
					}
					if (ftype == FragmentType.normal) {
						if (msg_edit.length() > 0) {
							sendmsg_btn.setTag(ToggleType.NORMALINPUT
									.toString());
							sendmsg_btn.setImageResource(R.drawable.send_msg);
						} else {
							sendmsg_btn.setTag(ToggleType.IMAGEINPUT.toString());
							sendmsg_btn
									.setImageResource(R.drawable.open_more_selector);
						}
					}
				}
			});
		}
	}
	public void addTextChangedListener(TextWatcher watcher) {
		this.watchers.add(watcher);
	}
	public void removeTextChangedListener(TextWatcher watcher) {
		watchers.remove(watcher);
	}
	/**
	 * 设置布局和输入法的切换 VOICEINPUT语音输入 NORMALINPUT普通输入法输入 FACEINPUT表情输入 IMAGEINPUT图片输入
	 */
	private void setFaceVisible(ToggleType type) {
		switch (type) {
			case FACEINPUT: {
				closeSoftInput();
				ToggleImageInput(false);
				ToggleFaceInput(true);
				break;
			}
			case NORMALINPUT: {
				ToggleVoiceInput(false);
				ToggleFaceInput(false);
				ToggleImageInput(false);
				break;
			}
			case VOICEINPUT: {
				closeSoftInput();
				ToggleFaceInput(false);
				ToggleImageInput(false);
				ToggleVoiceInput(true);
				msg_edit.setText("");
				break;
			}
			case IMAGEINPUT: {
				closeSoftInput();
				msg_edit.setText("");
				ToggleVoiceInput(false);
				ToggleFaceInput(false);
				ToggleImageInput(true);
				break;
			}
		}
	}
	public void closeInput() {
		closeSoftInput();
		setFaceVisible(ToggleType.NORMALINPUT);
	}
	/** 切换表情输入 */
	private void ToggleFaceInput(boolean isopen) {
		if (isopen) {
			face_btn.setSelected(true);
			face_wraper.setVisibility(View.VISIBLE);
			face_wraper_emotion.setVisibility(View.VISIBLE);
			face_wraper_btn.setVisibility(View.GONE);
		} else {
			face_btn.setSelected(false);
			face_wraper.setVisibility(View.GONE);
		}
	}
	/** 切换语音输入 */
	private void ToggleVoiceInput(boolean isopen) {
		if (ftype == FragmentType.normal) {
			if (isopen) {
				voice_input_btn.setTag(ToggleType.VOICEINPUT.toString());
				voice_input_btn.setImageResource(R.drawable.input_pressed);
				voice_btn.setVisibility(View.VISIBLE);
				msg_edit_wraper.setVisibility(View.GONE);
			} else {
				voice_input_btn.setTag(ToggleType.NORMALINPUT.toString());
				voice_input_btn
						.setImageResource(R.drawable.voice_input_selector);
				voice_btn.setVisibility(View.GONE);
				msg_edit_wraper.setVisibility(View.VISIBLE);
			}
		}
	}
	/** 切换图片输入 */
	private void ToggleImageInput(boolean isopen) {
		if (isopen) {
			sendmsg_btn.setSelected(true);
			face_wraper.setVisibility(View.VISIBLE);
			face_wraper_emotion.setVisibility(View.GONE);
			face_wraper_btn.setVisibility(View.VISIBLE);
		} else {
			sendmsg_btn.setSelected(false);
			face_wraper.setVisibility(View.GONE);
		}
	}
	private void closeSoftInput() {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(msg_edit.getWindowToken(), 0);
	}
	private void openSoftInput() {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(msg_edit, InputMethodManager.SHOW_FORCED);
	}
	private void detectKeyboardShow(Context mContext) {
		// 监听输入法是否打开
	}
	/**
	 * 设置表情的高度
	 * @param keyboardHeight
	 */
	public void setbiaoqingViewHeight(int keyboardHeight) {
		LayoutParams biaoqingParams = face_wraper_emotion.getLayoutParams();
		biaoqingParams.height = keyboardHeight;
		face_wraper_emotion.setLayoutParams(biaoqingParams);
	}

	/**
	 * 录音功能部分///////////////////////////////////////////////////////////////////////////////////
	 */
	public class voiceListener implements OnTouchListener {
		private String fileName = null;
		private String audioSavePath = null;
		private float y1, y2 = 0;

		@Override
		public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
			if (paramMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				// 按下时
				y1 = paramMotionEvent.getY();
				voice_btn.setSelected(true);
				soundVolumeDialog.show();
				volume_dialog_icon.setText("开始说话");
				volume_dialog_icon.startAnimation(fadeout);
				volume_dialog_anim_wraper.startAnimation(fadein);
				volume_dialog_note.setText("手指上滑  取消发送");
				voice_btn.setText("松开 发送");
				// 存放路径
				fileName = String.valueOf(System.currentTimeMillis()) + ".amr";
				audioSavePath = BBSUtils.getAudioSavePath(fileName);
				//LogUtils.i("fileName:" + fileName + " audioSavePath:" + audioSavePath);
				audioRecorderInstance = new MediaRecordHandler(fileName,
						audioSavePath, mHandler);
				audioRecorderInstance.setRecording(true);
				// 启动线程开始录音
				audioRecorderThread = new Thread(audioRecorderInstance);
				audioRecorderThread.start();
			} else if (paramMotionEvent.getAction() == MotionEvent.ACTION_UP) {
				y2 = paramMotionEvent.getY();
				// 松开时
				voice_btn.setSelected(false);
				voice_btn.setBackgroundResource(R.drawable.voice_btn_selector);
				voice_btn.setText("按下 说话");
				volume_dialog_wraper
						.setBackgroundResource(R.drawable.volume_dialog_narmal);
				if (audioRecorderInstance != null
						&& audioRecorderThread != null) {
					audioRecorderInstance.setRecording(false);
					audioRecorderThread.isInterrupted();
				}
				soundVolumeDialog.dismiss();
				if (y1 - y2 <= 50) {
					if (audioRecorderInstance.getRecordTime() >= 1) {
						if (audioRecorderInstance.getRecordTime() < HandlerConstant.MAX_SOUND_RECORD_TIME) {
							// 声音时长在范围内就发出去,否则不发
							audioRecorderInstance.stop();
							audioRecorderInstance
									.sendAudioMessage(HandlerConstant.HANDLER_RECORD_FINISHED);
						}
					} else {
						audioRecorderInstance.stop();
						soundVolumeDialog.show();
						volume_dialog_icon.setText("录音时间太短 !");
						volume_dialog_icon.startAnimation(fadein);
						volume_dialog_anim_wraper.startAnimation(fadeout);
						fadeout.setAnimationListener(new AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
							}
							@Override
							public void onAnimationRepeat(Animation animation) {
							}
							@Override
							public void onAnimationEnd(Animation animation) {
								if (soundVolumeDialog.isShowing()) {
									soundVolumeDialog.dismiss();
								}
								fadeout.setAnimationListener(null);
							}
						});
					}
				}
			} else if (paramMotionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				// 移动时
				y2 = paramMotionEvent.getY();
				if (y1 - y2 > 50) {
					voice_btn.setText("松开取消发送");
					voice_btn
							.setBackgroundResource(R.drawable.voice_btn_middle);
					volume_dialog_wraper
							.setBackgroundResource(R.drawable.volume_dialog_pause);
					volume_dialog_note.setText("手指松开  取消发送");
				} else {
					voice_btn.setText("松开 发送");
					voice_btn
							.setBackgroundResource(R.drawable.voice_btn_selector);
					volume_dialog_wraper
							.setBackgroundResource(R.drawable.volume_dialog_narmal);
					volume_dialog_note.setText("手指上滑  取消发送");
				}
			}
			return false;
		}
	}

	/**
	 * 初始化音量对话框
	 */
	private void initSoundVolumeDlg() {
		soundVolumeDialog = new Dialog(mContext, R.style.SoundVolumeStyle);
		soundVolumeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		soundVolumeDialog.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		soundVolumeDialog.setCanceledOnTouchOutside(false);
		soundVolumeDialog.setContentView(R.layout.tt_sound_volume_dialog);
		volume_dialog_wraper = (FrameLayout) soundVolumeDialog
				.findViewById(R.id.volume_dialog_wraper);
		volume_dialog_icon = (TextView) soundVolumeDialog
				.findViewById(R.id.volume_dialog_icon);
		volume_dialog_icon.setText("开始说话");
		volume_dialog_anim_wraper = (RelativeLayout) soundVolumeDialog
				.findViewById(R.id.volume_dialog_anim_wraper);
		volume_dialog_time = (TextView) soundVolumeDialog
				.findViewById(R.id.volume_dialog_time);
		volume_dialog_note = (TextView) soundVolumeDialog
				.findViewById(R.id.volume_dialog_note);
		volume_dialog_note.setText("手指上滑  取消发送");
		waveView = (VolumeWaveView) soundVolumeDialog
				.findViewById(R.id.volumeWaveView1);
		// 初始化淡入淡出动画
		fadein = AnimationUtils.loadAnimation(mContext, R.anim.recordfadein);
		fadeout = AnimationUtils.loadAnimation(mContext, R.anim.recordfadeout);
		fadeout.setFillAfter(true);
	}
	/**
	 * @Description 录音结束后处理录音数据
	 * @param fileName
	 */
	private void onRecordVoiceEnd(String fileName, int recordSecond) {
		sendmsgListenter.sendmsg(TalkFragement.this,
				Constants.MESSAGE_AUDIO_LINK_START + fileName
						+ Constants.MESSAGE_AUDIO_LINK_SPLIT + recordSecond
						+ Constants.MESSAGE_AUDIO_LINK_END);
	}
	/**
	 * @Description 录音超时(60s)，发消息调用该方法
	 */
	public void doFinishRecordAudio() {
		audioRecorderInstance.setRecording(false);
		voice_btn.setBackgroundResource(R.drawable.voice_btn_selector);
		voice_btn.setSelected(false);
		voice_btn.setText("按下 说话");
		volume_dialog_wraper
				.setBackgroundResource(R.drawable.volume_dialog_narmal);
		if (soundVolumeDialog.isShowing()) {
			soundVolumeDialog.dismiss();
		}
		soundVolumeDialog.show();
		volume_dialog_icon.setText("超时 !");
		volume_dialog_icon.startAnimation(fadein);
		volume_dialog_anim_wraper.startAnimation(fadeout);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (soundVolumeDialog.isShowing())
					soundVolumeDialog.dismiss();
				this.cancel();
			}
		}, 1500);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void onEventMainThread(EventBusData data) {
	}
	public String getContent() {
		if (msg_edit != null)
			return msg_edit.getText().toString().trim();
		else
			return "";
	}
	public void setContent(final String content) {
		if (msg_edit != null) {
			TalkFragement.this.content = content;
			ExpressionUtil.showEmoteInListview(mContext, msg_edit,
					ExpressionSizeType.middle, content);
			msg_edit.setSelection(msg_edit.getText().length());
		} else {
			postRunable(new Runnable() {
				@Override
				public void run() {
					TalkFragement.this.content = content;
					ExpressionUtil.showEmoteInListview(mContext, msg_edit,
							ExpressionSizeType.middle, content);
					msg_edit.setSelection(msg_edit.getText().length());
				}
			});
		}
	}
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (FragmentType.dialog.equals(ftype)) {
			fillContent(draftInfoTDialog);
		}
	}
	private void fillContent(DraftInfo draftInfo) {
		if (draftInfo == null) {
			String id = this.id;
			if (!TextUtils.isEmpty(id)) {
				String type = "dialog";
				String content = msg_edit.getText().toString().trim();
				if (!"".equals(content)) {
					draftInfo = new DraftInfo(id, type, content);
					draftDao.addDraftInfo(draftInfo.getId(),
							draftInfo.getType(), draftInfo.getContent());
				}
			}
		} else {
			String content = msg_edit.getText().toString().trim();
			draftInfo.setContent(content);
			int update = draftDao.update(draftInfo.getId(),
					draftInfo.getType(), draftInfo.getContent());
		}
	}
	public void setTypeTDialog(String str) {
		id = str;
	}
	//停止录音
	public void stopRecord() {
		voice_btn.setSelected(false);
		voice_btn.setBackgroundResource(R.drawable.voice_btn_selector);
		voice_btn.setText("按下 说话");
		volume_dialog_wraper
				.setBackgroundResource(R.drawable.volume_dialog_narmal);
		soundVolumeDialog.dismiss();
		if (audioRecorderThread != null && audioRecorderInstance != null) {
			audioRecorderInstance.setRecording(false);
			audioRecorderThread.isInterrupted();
			audioRecorderInstance.stop();
		}
	}
	public void setCommentedSuitableName(String name) {
		suitName = name;
	}
	//设置娱乐相机头条新闻输入框配置
	public void setNewsTopsetting(final int maxInputLen) {
		postRunable(new Runnable() {
			@Override
			public void run() {
				msg_edit_wraper.removeView(face_btn);
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
				lp.leftMargin = DimenUtils.dip2px(mContext, 10);
				final TextView inputMaxLenTextView = new TextView(getActivity());
				try {
					inputMaxLenTextView.setText(msg_edit.getText().toString()
							.getBytes("gbk").length
							+ "/" + maxInputLen);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				msg_edit.setSingleLine();
				msg_edit.setSelection(msg_edit.getText().length());
				msg_edit_wraper.addView(inputMaxLenTextView, lp);
				addTextChangedListener(new TextWatcher() {
					private String getLimitSubstring(String inputStr,
							int allowMaxBytes) {
						int orignLen = inputStr.length();
						int resultLen = 0;
						String temp = null;
						for (int i = 0; i < orignLen; i++) {
							temp = inputStr.substring(i, i + 1);
							try {// 3 bytes to indicate chinese word,1 byte to indicate english
									// word ,in utf-8 encode
								if (temp.getBytes("utf-8").length == 3) {
									resultLen += 2;
								} else {
									resultLen++;
								}
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							if (resultLen > allowMaxBytes) {
								return inputStr.substring(0, i);
							}
						}
						return inputStr;
					}
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}
					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}
					@Override
					public void afterTextChanged(Editable s) {
						int editEnd = msg_edit.getText().length();
						String limitStr = getLimitSubstring(msg_edit.getText()
								.toString(), maxInputLen);
						s.delete(limitStr.length(), editEnd);
						if (inputMaxLenTextView != null) {
							try {
								String str = msg_edit.getText().toString();
								int nowInputLen = str.getBytes("gbk").length;
								inputMaxLenTextView.setText(nowInputLen + "/"
										+ maxInputLen);
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		});
	}
	private void postRunable(Runnable runnable) {
		postRunnable.add(runnable);
	}
	private void executePostRunnable() {
		for (Iterator<Runnable> it = postRunnable.iterator(); it.hasNext();) {
			Runnable runnable = it.next();
			if (Looper.getMainLooper() == Looper.myLooper()) {
				runnable.run();
			} else {
				getActivity().runOnUiThread(runnable);
			}
			it.remove();
		}
	}

	public void setOnCancelListener(
			DialogInterface.OnCancelListener onCancelListener) {
		this.onCancelListener = onCancelListener;
	}
}
