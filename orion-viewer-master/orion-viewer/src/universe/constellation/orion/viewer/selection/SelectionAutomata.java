/*
 * Orion Viewer - pdf, djvu, xps and cbz file viewer for android devices
 *
 * Copyright (C) 2011-2013  Michael Bogdanov & Co
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package universe.constellation.orion.viewer.selection;

import android.view.*;
import universe.constellation.orion.viewer.OrionViewerActivity;
import universe.constellation.orion.viewer.dialog.DialogOverView;

/**
 * User: mike
 * Date: 11.08.12
 * Time: 14:37
 */
public class SelectionAutomata extends DialogOverView {

    private enum STATE {START, MOVING, END, CANCELED};

    private STATE state = STATE.CANCELED;

    private int startX, startY, width, height;

    private SelectionView selectionView;

    public SelectionAutomata(final OrionViewerActivity activity) {
        super(activity, universe.constellation.orion.viewer.R.layout.text_selector, android.R.style.Theme_Translucent_NoTitleBar);

        selectionView = (SelectionView) dialog.findViewById(universe.constellation.orion.viewer.R.id.text_selector);

        selectionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return SelectionAutomata.this.onTouch(event);
            }
        });
    }



    public boolean onTouch(MotionEvent event) {
        int action = event.getAction();

        //System.out.println("aaaction " + action + " " + event.getX() + ", " + event.getY());
        STATE oldState = state;
        boolean result = true;
        switch (state) {
            case START:
                if (action == MotionEvent.ACTION_DOWN) {
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    width = 0;
                    height = 0;
                    state = STATE.MOVING;
                    selectionView.reset();
                } else {
                    state = STATE.CANCELED;
                }
                break;

            case MOVING:
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                width = endX - startX;
                height = endY - startY;
                if (action == MotionEvent.ACTION_UP) {
                    state = STATE.END;
                } else {
                    selectionView.updateView(Math.min(startX, endX), Math.min(startY, endY), Math.max(startX, endX), Math.max(startY, endY));
                }
                break;

            default: result = false;
        }

        if (oldState != state) {
            switch (state) {
                case CANCELED: dialog.dismiss(); break;

                case END:
                    dialog.dismiss();

                    String text = activity.getController().selectText(getStartX(), getStartY(), getWidth(), getHeight());
                    if (text != null && !"".equals(text)) {
                        new SelectedTextActions(activity).show(text);
                    } else {
                        activity.showFastMessage(universe.constellation.orion.viewer.R.string.warn_no_text_in_selection);
                    }
                    break;
            }
        }
        return result;
    }

    public void startSelection() {
        selectionView.reset();
        initDialogSize();
        dialog.show();
        String msg = activity.getResources().getString(universe.constellation.orion.viewer.R.string.msg_select_text);
        activity.showFastMessage(msg);
        state = STATE.START;
    }



    public boolean inSelection() {
        return state == STATE.START || state == STATE.MOVING;
    }

    public boolean isSuccessful() {
        return state == STATE.END;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
