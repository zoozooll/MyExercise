package com.oregonscientific.meep.communicator.view.conversation;

import java.util.Iterator;
import java.util.Set;

import org.arabidopsis.ahocorasick.AhoCorasick;
import org.arabidopsis.ahocorasick.SearchResult;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Text view of conversation message which support emoticons
 */
public class ConversationTextView extends TextView {
	
	private final String TAG = getClass().getSimpleName();
	private final int TEXT_HEIGHT = 30;
	
	/**
	 * Constructor for a text bar
	 * @param context current context
	 * @param attrs current attribute set
	 */
	public ConversationTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		setWillNotDraw(false);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		if (!replaceEmoticonCodes(canvas, getText().toString())) {
			super.onDraw(canvas);
		}
	}
	
	/**
	 * Replace emoticon codes in message with emoticons
	 * @param canvas current canvas
	 * @param str the message
	 * @return true if emoticon code is found in message, false otherwise
	 */
	@SuppressWarnings("unchecked")
	public boolean replaceEmoticonCodes(Canvas canvas, String str) {
		boolean hasEmoticon = false;
		
		// set paint
		TextPaint paint = new TextPaint();
		paint.setTextSize(16);
		
		// set variables
		final int textMarginTop = 20;
		int textViewWidth = getWidth();
		int textViewHeight = getHeight();
		int index = 0;
		float previoustTextWidth = 0;
		float messageHeight = 0;
		
		// search for emoticon codes
		AhoCorasick tree = Emoticon.getTree(getContext());
		if (str != null && tree != null) {
			Iterator<?> itr = tree.search(str.toLowerCase().getBytes());
			while (itr.hasNext()) {
				hasEmoticon = true;
				
				// get the string content in front of emoticon found
				SearchResult rst = (SearchResult) itr.next();
				String currentTextBeforeEmoticon = str.substring(index, rst.getLastIndex() - Emoticon.EMOTICON_CODE_LENGTH);
				float currentTextWidth = paint.measureText(currentTextBeforeEmoticon);
				
				// set new line if text goes beyond message window
				if (previoustTextWidth + currentTextWidth > textViewWidth) {
					previoustTextWidth = 0;
					if (messageHeight != 0) {
						messageHeight += TEXT_HEIGHT;
					}
				}
				while (currentTextWidth > textViewWidth) {
					int charCount = paint.breakText(currentTextBeforeEmoticon, true, textViewWidth, null);
					String textBeforeBreak = str.substring(index, index + charCount);
					currentTextBeforeEmoticon = str.substring(index + charCount, rst.getLastIndex() - Emoticon.EMOTICON_CODE_LENGTH);
					
					// draw text
					canvas.drawText(textBeforeBreak, 0, messageHeight + textMarginTop, paint);
					messageHeight += TEXT_HEIGHT;
					
					currentTextWidth = paint.measureText(currentTextBeforeEmoticon);
					previoustTextWidth = 0;
					index += charCount;
				}
				canvas.drawText(currentTextBeforeEmoticon, previoustTextWidth, messageHeight + textMarginTop, paint);
				Set<String> outputs = rst.getOutputs();
				
				// set new line if emoticon goes beyond message window
				if (previoustTextWidth + currentTextWidth + Emoticon.EMOTICON_MAX_LENGTH > textViewWidth) {
					previoustTextWidth = 0;
					currentTextWidth = 0;
					messageHeight += TEXT_HEIGHT;
				}
				
				// draw emoticon
				for (String output : outputs) {
					Bitmap bitmap = Emoticon.getEmoticon(getContext(), output);
					if (bitmap != null) {
						canvas.drawBitmap(bitmap, previoustTextWidth + currentTextWidth, messageHeight, null);
					}
				}
				previoustTextWidth += currentTextWidth + Emoticon.EMOTICON_MAX_LENGTH;
				index = rst.getLastIndex();
			}
			
			// draw remaining text
			if (hasEmoticon && index < str.length()) {
				if (previoustTextWidth >= textViewWidth - 30) {
					previoustTextWidth = 0;
					messageHeight += TEXT_HEIGHT;
				}
				String remainingText = str.substring(index, str.length());
				float reamainingTextWidth = paint.measureText(remainingText);
				while (reamainingTextWidth + previoustTextWidth > textViewWidth) {
					int charCount = paint.breakText(remainingText, true, textViewWidth - previoustTextWidth, null);
					String textBeforeBreak = str.substring(index, index + charCount);
					remainingText = str.substring(index + charCount, str.length());
					
					// draw text
					canvas.drawText(textBeforeBreak, previoustTextWidth, messageHeight + textMarginTop, paint);
					messageHeight += TEXT_HEIGHT;
					
					reamainingTextWidth = paint.measureText(remainingText);
					previoustTextWidth = 0;
					index += charCount;
				}
				canvas.drawText(remainingText, previoustTextWidth, messageHeight + textMarginTop, paint);
			}
		}
		
		// extend text view's height if message's height exceeded text view's height
		if (hasEmoticon && messageHeight >= textViewHeight - 15) {
			setHeight((int) (messageHeight + 30));
		}
		
		return hasEmoticon;
	}
	
}