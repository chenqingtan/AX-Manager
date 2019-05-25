package pangolin.ax.plus.XWidget;

import android.annotation.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

public class XDZScrollView extends ScrollView
{
    private View inner;
    private boolean isCount = false;
    private Rect normal = new Rect();
    private float y;

    @SuppressLint({"NewApi"})
    public XDZScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
	}

    public void animation() {
        Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) this.inner.getTop(), (float) this.normal.top);
        translateAnimation.setDuration(200);
        this.inner.startAnimation(translateAnimation);
        this.inner.layout(this.normal.left, this.normal.top, this.normal.right, this.normal.bottom);
        this.normal.setEmpty();
	}

    public void commOnTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 1:
				if (isNeedAnimation()) {
					animation();
					this.isCount = false;
					return;
                }
				return;
            case 2:
				float f = this.y;
				float y = motionEvent.getY();
				int i = (int) (f - y);
				if (!this.isCount) {
					i = 0;
                }
				this.y = y;
				if (isNeedMove()) {
					if (this.normal.isEmpty()) {
						this.normal.set(this.inner.getLeft(), this.inner.getTop(), this.inner.getRight(), this.inner.getBottom());
                    }
					this.inner.layout(this.inner.getLeft(), this.inner.getTop() - (i / 2), this.inner.getRight(), this.inner.getBottom() - (i / 2));
                }
				this.isCount = true;
				return;
            default:
				return;
		}
	}

    public boolean isNeedAnimation() {
        return !this.normal.isEmpty();
	}

    public boolean isNeedMove() {
        int measuredHeight = this.inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        return scrollY == 0 || scrollY == measuredHeight;
	}

    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            this.inner = getChildAt(0);
		}
	}

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.inner != null) {
            commOnTouchEvent(motionEvent);
		}
        return super.onTouchEvent(motionEvent);
	}
}

