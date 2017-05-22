package github.com.slidingmenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import github.com.slidingmenu.R;


/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/5/21
 * Version  1.0
 * Description: 自定义侧滑
 */

public class SlidingMenu extends HorizontalScrollView  {

    /**
     * 滑动界面状态改变时回调接口
     * @author root
     *
     */
    public interface OnSlidingMenuStatusChangeListenter {
        /**
         * 界面关闭时调用
         */
        void  close();
        /**
         * 界面完全打开时调用
         */
        void  open();

    }
    //滑动界面状态改变时回调接口
    private OnSlidingMenuStatusChangeListenter mListenter=null;

    /**
     * 滑动界面状态改变时回调接口
     * @param listenter
     */
    public void setOnSlidingMenuStatusChangeListenter(OnSlidingMenuStatusChangeListenter listenter){
        this.mListenter=listenter;
    }

    //菜单view
    private View mMenuView;
    //内容view
    private View mContentView;
    //菜单宽度
    private int mMenuWidth;
    //菜单和内容宽度的差值
    private float right_padding=0;
    //菜单是否被打开
    private boolean isMenuOpen=false;
    //手势处理类
    private GestureDetector mGestureDetector=null;
    //最小fling速度
    private int minimumFlingVelocity=0;
    //
//    VelocityTracker

    public SlidingMenu(Context context) {
        this(context,null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //解析自定义属性
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        if(typedArray!=null){
            right_padding = typedArray.getDimension(R.styleable.SlidingMenu_right_padding, dip2px(50));

            typedArray.recycle();
        }
        //获取fling的最小速度
        ViewConfiguration configuration = ViewConfiguration.get(context);
        minimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        //初始化手势处理器
        mGestureDetector=new GestureDetector(context,new MyGestureListener());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            //布局改变，则调用
            scrollTo(mMenuWidth,0);
        }
    }
    //事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //当前点击位置
        float current = ev.getX();
        //如果菜单打开且当前点击位置>菜单宽度，则停止分发事件
        if(isMenuOpen && current>mMenuWidth){
            //关闭菜单
            closeMenu();
            //阻止事件分发
            return false;
        }

        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //拦截触摸事件
        //将触摸事件交给手势处理器
        if(mGestureDetector.onTouchEvent(ev)){
            return false;
        }

        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if(scrollX>mMenuWidth/2){
                    closeMenu();
                }else {
                    openMenu();
                }
                return false;
        }

        return super.onTouchEvent(ev);

    }

    /**
     * 关闭菜单
     */
    private void closeMenu() {
        smoothScrollTo(mMenuWidth,0);
        isMenuOpen=false;
        if(mListenter!=null){
            mListenter.close();
        }
    }
    //打开菜单
    private void openMenu() {
        smoothScrollTo(0,0);
        isMenuOpen=true;
        if(mListenter!=null){
            mListenter.open();
        }
    }


    //布局加载完毕，执行
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //1.获取菜单和内容的布局
        ViewGroup childAt = (ViewGroup) getChildAt(0);
        if(childAt==null){
            throw new IllegalArgumentException("SlidingMenu must contain child view!");
        }
        //1.1菜单
        mMenuView = childAt.getChildAt(0);
        if(mMenuView==null){
            throw new IllegalArgumentException("SlidingMenu must contain menu view!");
        }

        //1.2内容
        mContentView = childAt.getChildAt(1);
        if(mContentView==null){
            throw new IllegalArgumentException("SlidingMenu must contain content view!");
        }



        //2.设置宽高
        //2.1菜单宽度
        mMenuWidth = (int) (getScreenWidth() - right_padding);
        mMenuView.getLayoutParams().width=mMenuWidth;
        //2.2内容的宽度
        mContentView.getLayoutParams().width=getScreenWidth();


    }

    /**
     * 屏幕宽度
     * @return
     */
    private  int getScreenWidth(){
        DisplayMetrics displayMetrics =
                getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * dip转px
     * @param dp
     * @return
     */
    private int  dip2px(int dp){
        DisplayMetrics displayMetrics =
                getResources().getDisplayMetrics();
        return (int) (displayMetrics.density*dp);
    }

    //手势监听
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            //如果是纵向滑动，则直接返回，防止横向滑动
            if(Math.abs((velocityY)) > Math.abs(velocityX)){
                return super.onFling(e1,e2,velocityX,velocityY);
            }
            //向左----velocityX < 0
            if(velocityX<0 && isMenuOpen && Math.abs(velocityX) > minimumFlingVelocity){
                toggleMenu();
                return true;
            }

            //向右-----velocityX > 0
            if(velocityX > 0 && !isMenuOpen && Math.abs(velocityX) > minimumFlingVelocity){
                toggleMenu();
                return true;
            }
            return super.onFling(e1,e2,velocityX,velocityY);
        }
    }

    /**
     * 切换状态
     * 提供给用户调用
     */
    public void toggleMenu() {
        if(isMenuOpen){
            closeMenu();
        }else {
            openMenu();
        }
    }

    //位置滑动时，调用
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //滑动时，给菜单设置默认位置
        mMenuView.setTranslationX(l*0.8f);

        //l (0-->mMenuWidth)
        //percent (0--->1)
        float percent = l * 1f / mMenuWidth;
        //percent (0.4->1)
        percent=evaluate(percent,0.4,1);

        //给内容添加阴影(0.4-1)
        mContentView.getBackground().setColorFilter((Integer)evaluateColor(percent, Color.BLACK,Color.TRANSPARENT),
                android.graphics.PorterDuff.Mode.SRC_OVER);

    }

    /**
     * 评估值
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * 颜色变化过度
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
    }
}
