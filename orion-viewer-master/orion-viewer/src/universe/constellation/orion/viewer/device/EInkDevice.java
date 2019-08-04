package universe.constellation.orion.viewer.device;

import android.view.View;

import universe.constellation.orion.viewer.OrionViewerActivity;
import universe.constellation.orion.viewer.prefs.GlobalOptions;

/**
 * Created by mike on 9/9/14.
 */
public abstract class EInkDevice extends AndroidDevice {

    protected int counter;

    @Override
    public void flushBitmap() {
        GlobalOptions options = ((OrionViewerActivity)activity).getGlobalOptions();
        View view = activity.getView();
        if (options.isEinkOptimization()) {
            if (counter < options.getEinkRefreshAfter()) {
                doPartialUpdate(view);
                counter++;
            } else {
                counter = 0;
                doFullUpdate(view);

            }
        } else {
            doDefaultUpdate(view);
        }
    }

    public void doPartialUpdate(View view) {
        super.flushBitmap();
    }

    public void doFullUpdate(View view) {
        super.flushBitmap();
    }

    public void doDefaultUpdate(View view) {
        super.flushBitmap();
    }

    public boolean isLightingSupported() {
        return false;
    }

    public int doLighting(int delta) throws Exception {
        return -1;
    }
}
