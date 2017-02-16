package com.xiaojia.daniujia.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.domain.resp.HomeEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图ViewPager
 */
public class RollViewPager extends ViewPager {

    private static final int NEXT = 0;//切换下一张图片的消息
    private List<ImageView> dotlist;
    private List<HomeEntity.BannersEntity> carousels = new ArrayList<>();
    private boolean isShowBottomTest = true;
    /**
     * 布局中引入自定义控件，调用的构造
     * @param context
     * @param attrs
     */
    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    /**
     * 代码实例化，调用的构造
     * @param context
     */
    public RollViewPager(Context context) {
        super(context);
        init();
    }
    /**
     * 初始化操作
     */
    private void init() {
        addOnPageChangeListener(new MyOnPageChangeListener());
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            RollViewPager.FixedSpeedScroller scroller = new RollViewPager.FixedSpeedScroller(getContext(),
                    new AccelerateInterpolator());
            field.set(this, scroller);
            scroller.setmDuration(450);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setShowBottomTest(boolean showBottomTest) {
        isShowBottomTest = showBottomTest;
    }

    private int lastPosition = 0;

    public void initData(List<HomeEntity.BannersEntity> carousels) {
        this.carousels.clear();
        this.carousels.addAll(carousels);
        initTopImageUrls();
    }

    class MyOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener{
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (dotlist != null && dotlist.size() > 1) {
                dotlist.get(lastPosition % carousels.size()).setImageResource(R.drawable.roll_dot_unchecked);
                ImageView currentDot = dotlist.get(position % carousels.size());
                currentDot.setImageResource(R.drawable.roll_dot_checked);
            }
            lastPosition = position;
        }
    }
    /**
     * 初始化轮播图的图片url
     */
    public void initTopImageUrls() {
        //设置轮播图适配器
        setAdapter(new MyRollViewPagerAdapter());
        if (carousels != null && carousels.size() <= 1) {
            setCurrentItem(carousels.size());
        } else {
            setCurrentItem(carousels.size() * 1000);
        }

        startRoll();
    }

    private ArrayList<View> cacheView = new ArrayList<>();

    class MyRollViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (carousels != null) {
                if (carousels.size() > 1) {
                    return Integer.MAX_VALUE;
                } else {
                    return carousels.size();
                }
            }

            return 0;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            if (cacheView.size() == 0){
                view = View.inflate(getContext(), R.layout.viewpager_item, null);
            } else {
                view = cacheView.get(0);
                cacheView.clear();
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView tvName = (TextView) view.findViewById(R.id.id_name);
            TextView tvPosition = (TextView) view.findViewById(R.id.id_position);
            TextView tvCorp = (TextView) view.findViewById(R.id.id_corp);
            TextView tvCareerTime = (TextView) view.findViewById(R.id.id_career_duration);
            RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.roll_rl_bottom);

            if (isShowBottomTest) {
                layout.setVisibility(VISIBLE);
            } else {
                layout.setVisibility(GONE);
            }

            HomeEntity.BannersEntity entity = carousels.get(position % carousels.size());

            if (!TextUtils.isEmpty(entity.getImage())) {
                Picasso.with(App.get()).load(entity.getImage()).error(R.drawable.def_icon).into(imageView);
            }else{
                Picasso.with(App.get()).load(R.drawable.def_icon).into(imageView);
            }
            tvName.setText(entity.getName());
            tvPosition.setText(entity.getPosition());
            tvCorp.setText(entity.getCompany());
            tvCareerTime.setText(entity.getWorkage() + "年");
            container.addView(view);

//            View view = View.inflate(getContext(), R.layout.viewpager_item, null);
//            ImageView imageView = (ImageView) view.findViewById(R.id.image);
//
//            TextView tvName = (TextView) view.findViewById(R.id.id_name);
//            TextView tvPosition = (TextView) view.findViewById(R.id.id_position);
//            TextView tvCorp = (TextView) view.findViewById(R.id.id_corp);
//            TextView tvCareerTime = (TextView) view.findViewById(R.id.id_career_duration);
//
//            HomeBeanVo.CarouselsEntity entity = carousels.get(position % carousels.size());
//
//            if (!TextUtils.isEmpty(entity.getImage())) {
//                Picasso.with(App.get()).load(entity.getImage()).error(R.drawable.def_icon).into(imageView);
//            }else{
//                Picasso.with(App.get()).load(R.drawable.def_icon).into(imageView);
//            }
//            tvName.setText(entity.getName());
//            tvPosition.setText(entity.getPosition());
//            tvCorp.setText(entity.getCompany());
//            tvCareerTime.setText(entity.getWorkage() + "年");
//            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            View view = (View) object;
            if (view != null) {
                cacheView.add(view);
            }

        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    /**
     * 初始化轮播图指示点
     * @param dotlist
     */
    public void initDots(List<ImageView> dotlist) {
        this.dotlist = dotlist;
    }
    private boolean isStartRunning = false;
    /**
     * 开启轮播
     */
    public void startRoll(){
        if (this.carousels.size() <= 1) {
            if (carousels.size() == 1)
            return;
        }
        handler.removeMessages(NEXT);
        isStartRunning = true;
        handler.sendEmptyMessageDelayed(NEXT, 2000);
    }
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case NEXT:
                    if(isStartRunning){
                        int nextItem = getCurrentItem()+1;//当前item+1
                        setCurrentItem(nextItem);
                        handler.sendEmptyMessageDelayed(NEXT, 3000);//2s的延迟消息
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void setRunning(boolean isStartRunning){
        this.isStartRunning = isStartRunning;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStartRunning = false;
    }
    private int downX = 0;
    private OnItemClickListener onItemClickListener;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int disX = Math.abs(moveX - downX);
                int disY = Math.abs(moveY - downY);
                if(disX>disY){
                    if (getCurrentItem() == 0 && moveX > downX) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (getCurrentItem() == carousels.size() - 1 && downX > moveX) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else{
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    private int downTime = 0;
    private int downY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = (int) SystemClock.uptimeMillis();
                downX =(int) ev.getX();
                downY = (int) ev.getY();
                isStartRunning = false;
                break;
            case MotionEvent.ACTION_UP:
                startRoll();
                int upTime = (int) SystemClock.uptimeMillis();
                int upX = (int) ev.getX();
                int upY = (int) ev.getY();
                int disX = Math.abs(upX - downX);
                int disY = Math.abs(upY - downY);
                if(upTime - downTime <500 && disX<5 && disY<5){
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(getCurrentItem(),this);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                startRoll();

                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
    /**
     * 自定义，Viewpager，条目点击监听
     */
    public interface OnItemClickListener {
        void onItemClick(int position,RollViewPager viewPager);
    }
    public void setOnItemClickLstener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 700;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }

}
