package universe.constellation.orion.viewer.device.texet;

import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import universe.constellation.orion.viewer.Common;
import universe.constellation.orion.viewer.OperationHolder;

/**
 * Created by mike on 9/16/14.
 */
public class TexetTB576HDDevice extends TexetDevice {

    private static Method texetBacklight0;

    private static Method texetBacklight1;

    private static Method invalidateScreen;

    private static boolean isMethodFound = false;

    static {
        try {
            texetBacklight0 = PowerManager.class.getMethod("setBacklight", int.class, Context.class);
        } catch (Exception e) {
            Common.d(e);
        }

        try {
            texetBacklight1 = PowerManager.class.getMethod("setBacklight", int.class);
        } catch (Exception e) {
            Common.d(e);
        }

        try {
            invalidateScreen = View.class.getMethod("postInvalidate", int.class);
            isMethodFound = true;
        } catch (Exception e) {
            Common.d(e);
        }
    }

    @Override
    public boolean isLightingSupported() {
        return true;
    }

    @Override
    public int doLighting(int delta) throws Exception {
        int i = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);

        i += delta;

        if (i < 0)
            i = 0;
        if (i > 255)
            i = 255;

        PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);

        if (texetBacklight0 != null) {
            texetBacklight0.invoke(pm, i, activity);
        } else if (texetBacklight1 != null){
            texetBacklight1.invoke(pm, i);
        } else {
            Log.e("texet backlight",  "not found");
        }

        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, i);

        return i;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event, OperationHolder holder) {
        if (keyCode == KeyEvent.KEYCODE_PAGE_UP || keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            holder.value = event.isLongPress() ? PREV : NEXT;
            return true;
        }
        return super.onKeyUp(keyCode, event, holder);
    }

    @Override
    public String getIconFileName(String simpleFileName, long fileSize) {
        int i = simpleFileName.lastIndexOf(".");
        if (i > 0) {
            simpleFileName = simpleFileName.substring(0, i);
        }
        return "/mnt/storage/BookCover/" + simpleFileName + "." + fileSize + ".png.bnv";
    }

    @Override
    public void doFullUpdate(View view) {
        if (isMethodFound) {
            try {
                invalidateScreen.invoke(view, 98);
            } catch (Exception e) {
                Common.d(e);
                super.doFullUpdate(view);
            }
        } else {
            super.doFullUpdate(view);
        }
    }

    @Override
    public void doDefaultUpdate(View view) {
        doFullUpdate(view);
    }
}
