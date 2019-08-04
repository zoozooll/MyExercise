package com.oregonscientific.meep.store2;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class CategoryDialogFragment extends DialogFragment {
	public static final int CATEGORY_GAME_DIALOG_ID = 1;
	public static final int CATEGORY_APP_DIALOG_ID = 2;
	public static final int PRICE_DIALOG_ID = 3;
	
//	private int mPriceButtonIds [] = {R.id.priceAll, R.id.priceFree, R.id.pricePaid, R.id.priceOthers};
	private int mPriceButtonIds [] = {R.id.priceAll, R.id.priceFree, R.id.pricePaid};
	
	private int mAppCategoryButtonIds [] = {R.id.categoryAll,R.id.categoryBooks,R.id.categoryEducation,R.id.categoryEntertainment,R.id.categoryMusic,R.id.categoryPhoto,R.id.categoryTools,R.id.categoryVideo};
	private int mGameCategoryButtonIds [] = {R.id.categoryAll,R.id.categoryArcade,R.id.categoryBrain,R.id.categoryCards,R.id.categoryCasual,R.id.categoryEducation,R.id.categoryEntertainment,R.id.categoryMusic,R.id.categoryRacing,R.id.categorySport};
	private int mCategoryButtonIds [] = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	}

	public static CategoryDialogFragment newInstance(int title) {
		CategoryDialogFragment myDialogFragment = new CategoryDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("title", title);
		myDialogFragment.setArguments(bundle);
		return myDialogFragment;
	}

	// 
	// public Dialog onCreateDialog(Bundle savedInstanceState) {
	// Dialog dialog = null;
	// int args = getArguments().getInt("title");
	// switch (args) {
	// case CATEGORY_DIALOG_ID:
	// dialog = new Dialog(getActivity());
	// dialog.show();
	// dialog.setContentView(R.layout.sort_by_category);
	// break;
	// case PRICE_DIALOG_ID:
	// }
	// return dialog;
	// }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	int args = getArguments().getInt("title");  
    	View v = null;
    	
    	switch (args) {  
			case CATEGORY_GAME_DIALOG_ID:
				v = inflater.inflate(R.layout.sort_by_category_game, container, false);
				mCategoryButtonIds = mGameCategoryButtonIds;
				break;
			case CATEGORY_APP_DIALOG_ID: 
				v = inflater.inflate(R.layout.sort_by_category_app, container, false);
				mCategoryButtonIds = mAppCategoryButtonIds;
				break;
			case PRICE_DIALOG_ID:  
				v = inflater.inflate(R.layout.sort_by_price, container, false);
				
				for (int i=0; i<mPriceButtonIds.length; i++) {
					ImageButton button = (ImageButton) v.findViewById(mPriceButtonIds[i]);
					button.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							String tag = v.getTag().toString();
							((GenericStoreActivity)getActivity()).setSelectedPriceId(Integer.parseInt(tag));
							CategoryDialogFragment.this.dismiss();
						}
					});
				}
				
				break;
		}
    	
    	if (mCategoryButtonIds != null) {
    		for (int i=0; i<mCategoryButtonIds.length; i++) {
				ImageButton button = (ImageButton) v.findViewById(mCategoryButtonIds[i]);
				button.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						String tag = v.getTag().toString();
						((GenericStoreActivity)getActivity()).setSelectedCategoryId(Integer.parseInt(tag));
						CategoryDialogFragment.this.dismiss();
					}
				});
			}
    	}
    	
    	ImageButton btnClose = (ImageButton) v.findViewById(R.id.btnClose);
    	btnClose.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				CategoryDialogFragment.this.dismiss();
			}
		});
    	
        return v;
    }
	
    @Override
	public void dismiss() {
		// TODO Auto-generated method stub
		try
		{	
			if(getActivity() instanceof GenericStoreActivity){
				//enable the sorting buttons if the container activity is instance of GenericStoreActivity
				((GenericStoreActivity) getActivity()).enableSortingButtons(true);
			}
			super.dismiss();
		}
		catch(Exception e)
		{
			Log.d("test","current activity has been closed");
		}
	}
	
	@Override
	public void onDismiss(DialogInterface dialog){
		if(getActivity() instanceof GenericStoreActivity){
			//enable the sorting buttons if the container activity is instance of GenericStoreActivity
			((GenericStoreActivity) getActivity()).enableSortingButtons(true);
		}
		super.onDismiss(dialog);
	}
}