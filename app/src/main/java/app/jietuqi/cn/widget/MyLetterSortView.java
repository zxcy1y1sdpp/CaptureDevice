package app.jietuqi.cn.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import app.jietuqi.cn.R;


public class MyLetterSortView extends View {
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	public static String[] b = { "↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	private int choose = -1;
	private Paint paint = new Paint();

	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public MyLetterSortView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyLetterSortView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLetterSortView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / b.length;

		for (int i = 0; i < b.length; i++) {
			paint.setColor(Color.parseColor("#545454"));
//			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
	      //	paint.setTextSize(PixelUtil.sp2px(12));
	    	paint.setTextSize(35);
	

			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}

			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight - paint.measureText(b[i]) / 4;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:

			setBackgroundResource(R.drawable.letter_sort_background);
			if (oldChoose != c) {
				if (c >= 0 && c < b.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null) {
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					
					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}


	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	
	public interface OnTouchingLetterChangedListener {
		void onTouchingLetterChanged(String s);
	}

}
