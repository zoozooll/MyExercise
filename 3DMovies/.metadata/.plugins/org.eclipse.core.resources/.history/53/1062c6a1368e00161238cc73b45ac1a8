package com.oregonscientific.meep.opengl;

import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.opengl.OpenGlRender.OspadRenderListener;


public class StateManager {
	
	private static SystemState mSystemState;
	
	public enum SystemState
	{
		HOME,
		MSG_GROUP,
		FUNC_SELECT,
		
	}
	
	public StateManager()
	{
		mSystemState = SystemState.HOME;
	}
	
	public static SystemState getSystemState() {
		return mSystemState;
	}

	public void setmSystemState(SystemState systemState) {
		if(mSystemState!=systemState)
		{
			StateManager.mSystemState = systemState;
			if(onSystemStateListener!=null)
			{
				onSystemStateListener.OnSystemStateChanged(systemState);
			}
		}
	}
	
	public interface SystemStateListener
    {
    	public abstract void OnSystemStateChanged(SystemState state);
    }
	
	SystemStateListener onSystemStateListener = null;
	
	public void setOnOspadRenderListener(SystemStateListener listener)
	{
		onSystemStateListener = listener;
	}

}
