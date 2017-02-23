package fpjk.nirvana.sdk.wjbridge;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 纵向刻度
 */
public class WebViewScaleProgressBar extends View {

    private float barHeight;

    /**
     * 正常字体颜色
     */
    private int normalColor;

    /**
     * 当前选中的字体颜色
     */
    private int progressColor;

    private Paint normalPaint;

    private Paint gradientPaint;

    private Paint progressPaint;

    private int min;

    private int max;

    private float progress;

    private float finishAnimProgress;

    private ObjectAnimator finishAnim;

    private ObjectAnimator smoothAnim;

    public WebViewScaleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.webViewScaleProgressBar);
            barHeight = array.getDimension(R.styleable.webViewScaleProgressBar_barHeight, 10f);
            normalColor = array.getColor(R.styleable.webViewScaleProgressBar_barNormalColor, Color.BLACK);
            progressColor = array.getColor(R.styleable.webViewScaleProgressBar_barProgressColor, Color.BLUE);
            min = array.getInteger(R.styleable.webViewScaleProgressBar_barMin, 0);
            max = array.getInteger(R.styleable.webViewScaleProgressBar_barMax, 100);
            array.recycle();
        }
        init();
    }

    private void init() {
        normalPaint = new Paint();
        normalPaint.setColor(normalColor);
        normalPaint.setAntiAlias(true);
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setAntiAlias(true);

        gradientPaint = new Paint();
        gradientPaint.setAntiAlias(true);

        finishAnim = ObjectAnimator.ofFloat(this, "finishAnimProgress", -2, 0, 1f);
        finishAnim.setDuration(600);
        finishAnim.setInterpolator(new LinearInterpolator());
        finishAnim.addListener(new AnimatorListener() {
            boolean isCancel;

            @Override
            public void onAnimationStart(Animator animation) {
                isCancel = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isCancel)
                    WebViewScaleProgressBar.this.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isCancel = true;
            }
        });

    }

    RectF rectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        float width = getWidth();
        int height = getHeight();
        float top = (height - barHeight) / 2;
        if (progress == max) {//执行完成动画
            if (finishAnimProgress < 0) {//执行完预留的距离
//                RectF rectF = new RectF(0f, top, width, top + barHeight);
                rectF.left = 0f;
                rectF.right = width;
                rectF.top = top;
                rectF.bottom = top + barHeight;
                canvas.drawRect(rectF, normalPaint);
                float right = getRatio(max + finishAnimProgress * max / 40f) * width;
                rectF.right = right;
                canvas.drawRect(rectF, progressPaint);
            } else {//执行加载完成后的动画
                float left = finishAnimProgress * width;
//                RectF animRectF = new RectF(left, top, width, top + barHeight);
                rectF.left = left;
                rectF.right = width;
                rectF.top = top;
                rectF.bottom = top + barHeight;
                int r = Color.red(progressColor);
                int g = Color.green(progressColor);
                int b = Color.blue(progressColor);
                LinearGradient gradient = new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.top, new int[]{Color.argb(80, r, g, b), progressColor, progressColor}, new float[]{0, 0.2f, 1f}, Shader.TileMode.MIRROR);
                gradientPaint.setShader(gradient);
                canvas.drawOval(rectF, gradientPaint);
                rectF.left = rectF.centerX();
                canvas.drawRect(rectF, progressPaint);
            }
        } else {//制作假进度
//            RectF rectF = new RectF(0f, top, width, top + barHeight);
            rectF.left = 0f;
            rectF.right = width;
            rectF.top = top;
            rectF.bottom = top + barHeight;
            canvas.drawRect(rectF, normalPaint);
            if (smoothAnim == null || !smoothAnim.isRunning()) {
                if (progress < max - max / 20f) {
                    float right = getRatio(progress) * width;
                    rectF.right = right;
                    postInvalidateDelayed(20);
                    progress += 0.2f;
                } else {
                    float right = getRatio(max - max / 20f) * width;
                    rectF.right = right;
                }
                canvas.drawRect(rectF, progressPaint);
            }

        }
        canvas.restore();
    }

    public void playFinishAnim() {
//        finishAnim.setStartDelay(300);
        finishAnim.start();
        finishAnimProgress = -2;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(final float progress) {
        setProgressSmooth(progress, false);
    }

    public void setProgressSmooth(float progress, boolean isSmooth) {
        if (finishAnim != null && finishAnim.isRunning()) {
            finishAnim.cancel();
        }
        if (smoothAnim != null && smoothAnim.isRunning()) {
            smoothAnim.cancel();
        }
        if (isSmooth) {
            progress = Math.min(progress, max - max / 20f);
            if (progress > this.progress) {
                smoothAnim = ObjectAnimator.ofFloat(this, "progress", this.progress, progress);
                smoothAnim.setDuration(200);
                smoothAnim.addListener(new AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        postInvalidate();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                smoothAnim.start();
            }
        } else {
            WebViewScaleProgressBar.this.progress = progress;
            postInvalidate();
        }
    }

    private float getRatio(float progress) {
        if (min >= max) {
            return 0;
        }
        return Math.min(1f, progress / (max - min));
    }

    public float getBarHeight() {
        return barHeight;
    }

    public void setBarHeight(float barHeight) {
        this.barHeight = barHeight;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;

    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setFinishAnimProgress(float finishAnimProgress) {
        this.finishAnimProgress = finishAnimProgress;
        postInvalidate();
    }

}