package com.godyhm.test.bendtriangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TriangleView extends GLSurfaceView implements OnTouchListener{
	TriangleRender m_render = null;
	boolean bRender = false;
	public TriangleView(Context context) {
			super(context);
			m_render = new TriangleRender(context,this);
			setRenderer(m_render);		
//			setRenderMode(RENDERMODE_WHEN_DIRTY);
			setOnTouchListener(this);
			
		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			 switch (event.getAction() & MotionEvent.ACTION_MASK) 
		      {
			      case MotionEvent.ACTION_DOWN:
				      {	    	  
				    	  if(!bRender)
				    	  {
					    	  setRenderMode(RENDERMODE_CONTINUOUSLY);
							  requestRender();
				    	  }
				    	  else
				    	  {
				    		  setRenderMode(RENDERMODE_WHEN_DIRTY);
				    	  }
				    	  bRender = !bRender;
				      }
				      break;
				  default:
					  break;
		      }
			return false;
		}

}
