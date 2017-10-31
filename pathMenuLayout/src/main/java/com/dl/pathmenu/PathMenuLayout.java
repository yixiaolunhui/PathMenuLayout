package com.dl.pathmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dl on 2017/10/31.
 */

public class PathMenuLayout extends RelativeLayout {

    private Context mContext;
    //每个菜单之间的角度
    private int mPathMenuItemIntervalAngle;
    //菜单弹出时间
    private int mPathMenuPopupTime;
    //半径
    private float mPathMenuRadius;
    //是否旋转扩散
    private  boolean mPathMenuIsRotate;
    //菜单子布局集合
    private List<View> mPathMenus = new ArrayList<>();//菜单
    //菜单item数量
    private int menuItemCount;
    //菜单弹出动画
    private List<ViewPropertyAnimator> popupAnimators = new ArrayList<>();
    //每个菜单的旋转动画
    private List<ViewPropertyAnimator> rotateAnimators = new ArrayList<>();
    //菜单回调接口
    private PathMenuListener mPathMenuListener;
    //菜单是否打开
    private boolean isOpen = false;
    private int left, top;
    //菜单打开的总角度
    private int mSectorAngle;


    public PathMenuLayout(Context context) {
        this(context, null);
    }

    public PathMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PathMenuLayout);
        mPathMenuRadius = typedArray.getDimension(R.styleable.PathMenuLayout_PathMenuRadius, dp2px(mContext, 100));
        mPathMenuPopupTime = typedArray.getInt(R.styleable.PathMenuLayout_PathMenuPopupTime, 400);
        mPathMenuItemIntervalAngle = typedArray.getInt(R.styleable.PathMenuLayout_PathMenuItemIntervalAngle, 24);
        mPathMenuIsRotate = typedArray.getBoolean(R.styleable.PathMenuLayout_PathMenuIsRotate, true);
        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
        setVisibility(INVISIBLE);
    }

    private void init() {
        int count = getChildCount();
        if (count == 0) return;
        this.mPathMenus.clear();
        for (int i = 0; i < count; i++) {
            if (getChildAt(i).getVisibility() == VISIBLE) {
                this.mPathMenus.add(getChildAt(i));
            }
        }
        menuItemCount = mPathMenus.size();
        initMenuItem();
    }

    /**
     * 设置菜单item
     * @param menus
     */
    public void setMenuViews(List<View> menus) {
        this.mPathMenus = menus;
        menuItemCount = menus.size();
        initMenuItem();
    }

    /**
     * 初始化菜单item
     */
    private void initMenuItem() {
        this.removeAllViews(); //删除所有子控件
        if (menuItemCount == 0) return;
        LayoutParams params = getMenuParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < menuItemCount; i++) {
            View menuItem = (View) mPathMenus.get(i);
            FrameLayout viewContainer = new FrameLayout(mContext);
            ViewGroup parent = (ViewGroup) menuItem.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            viewContainer.addView(menuItem);
            viewContainer.setLayoutParams(params);
            this.addView(viewContainer);
        }
        popupAnimators.clear();
        rotateAnimators.clear();
        //初始化动画，每个view对应一个animator
        for (int i = 0; i < menuItemCount; i++) {
            ViewGroup childAt = (ViewGroup) this.getChildAt(i);
            ViewPropertyAnimator anim = ViewPropertyAnimator.animate(childAt);
            anim.setDuration(mPathMenuPopupTime);
            popupAnimators.add(anim);
            View image = childAt.getChildAt(0);
            ViewPropertyAnimator anim1 = ViewPropertyAnimator.animate(image);
            anim.setDuration(mPathMenuPopupTime);
            rotateAnimators.add(anim1);
            final int finalI = i;
            childAt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                    if (mPathMenuListener != null)
                        mPathMenuListener.menuClick(v, finalI);
                }
            });
        }
    }


    private LayoutParams getMenuParams(int width, int margin) {
        int w = dp2px(mContext, width);
        int h = w;
        LayoutParams params = new LayoutParams(w, h);
        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(CENTER_HORIZONTAL);
        params.bottomMargin = dp2px(mContext, margin);
        return params;
    }

    /**
     * 设置菜单接口回调
     *
     * @param pathListener
     */
    public void setPathMenuListener(PathMenuListener pathListener) {
        this.mPathMenuListener = pathListener;
    }

    /**
     * 切换菜单
     */
    public void toggle() {
        if (isOpen) {
            close();
        } else {
            open();
        }
    }

    /**
     * 打开菜单
     */
    private void open() {
        setVisibility(VISIBLE);
        if (isOpen == true) {
            return;
        }
        if (menuItemCount == 1) {
            mSectorAngle = 0;
        } else {
            mSectorAngle = mPathMenuItemIntervalAngle * menuItemCount;
        }
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen)
                    close();
            }
        });
        isOpen = true;
        if (mPathMenuListener != null)
            mPathMenuListener.onOpen();
        this.setBackgroundColor(Color.parseColor("#66000000"));
        //小按钮扇面飞出同时旋转
        int startAngle = (180 - mSectorAngle) / 2;
        //各个菜单项按钮间隔
        int distanceAngle;
        if (menuItemCount == 1) {
            distanceAngle = mSectorAngle;
        } else {
            distanceAngle = mSectorAngle / (menuItemCount - 1);
        }
        left = getChildAt(0).getLeft();
        top = getChildAt(0).getTop();
        for (int i = 0; i < menuItemCount; i++) {
            //角度
            int angle = startAngle + i * distanceAngle;
            //弧度  1角度=π/180弧度
            double radians = angle * Math.PI / 180;
            double deltaX = -Math.cos(radians) * mPathMenuRadius;
            double deltaY = -Math.sin(radians) * mPathMenuRadius;
            ViewPropertyAnimator popupAnimator = popupAnimators.get(i);
            popupAnimator.setInterpolator(new OvershootInterpolator()).x((float) (left + deltaX)).y((float) (top + deltaY));
            popupAnimator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    setVisibility(VISIBLE);
                }
            });
            if(mPathMenuIsRotate){
                ViewPropertyAnimator rotateAnimator = rotateAnimators.get(i);
                rotateAnimator.rotation(-360);
            }

        }
    }

    /**
     * 关闭菜单
     */
    private void close() {
        this.setOnClickListener(null);
        this.setClickable(false);
        isOpen = false;
        if (mPathMenuListener != null)
            mPathMenuListener.onClose();
        for (int i = 0; i < menuItemCount; i++) {
            ViewPropertyAnimator popupAnimator = popupAnimators.get(i);
            popupAnimator.setInterpolator(new AnticipateInterpolator()).x((float) (left)).y((float) (top));
            popupAnimator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setBackgroundResource(android.R.color.transparent);
                    setVisibility(GONE);
                }
            });
            if(mPathMenuIsRotate){
                ViewPropertyAnimator rotateAnimator = rotateAnimators.get(i);
                rotateAnimator.rotation(0);
            }
        }
    }


    /**
     * dp转px
     */
    public int dp2px(Context context, float dpValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
        return 0;
    }


    /**
     * 菜单接口回调
     */
    public interface PathMenuListener {
        /**
         * 菜单打开
         */
        void onOpen();

        /**
         * 菜单关闭
         */
        void onClose();

        /**
         * 点击菜单中的按钮
         *
         * @param view
         * @param position
         */
        void menuClick(View view, int position);
    }
}
