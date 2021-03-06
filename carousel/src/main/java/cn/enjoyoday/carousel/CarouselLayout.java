package cn.enjoyoday.carousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 轮播view
 * @author hfcai
 */
public class CarouselLayout extends RelativeLayout {

    private static final String TAG = "CarouselLayout";
    private static final int LOCAL = 0;
    private static final int ONLINE = 1;

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private Context context;

    private int type;
    private int resId;
    private int urls;
    private int pos;
    private int size;
    private int loadFailed;
    private int preLoad;
    private AdapterView.OnItemClickListener onItemClickListener;

    public CarouselLayout(Context context) {
        this(context, null, 0);
    }

    public CarouselLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        inflater();
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CarouselLayout);
            type = typedArray.getInt(R.styleable.CarouselLayout_type, 0);
            resId = typedArray.getResourceId(R.styleable.CarouselLayout_resIds, -1);
            urls = typedArray.getResourceId(R.styleable.CarouselLayout_urls, -1);
            loadFailed = typedArray.getResourceId(R.styleable.CarouselLayout_loadFailed,-1);
            preLoad = typedArray.getResourceId(R.styleable.CarouselLayout_preLoad,-1);
            if (type == 0) {
                //local
                setResId(resId);
            } else {
                //online
                setUrls(urls);
            }
            typedArray.recycle();
        }
    }



    public void setLoadFailed(int drawableId){
        if (drawableId>0){
            this.loadFailed = drawableId;

        }
    }


    public void setPreLoad(int preLoad){
        if (preLoad>0){
            this.preLoad = preLoad;
        }
    }


    class CarouselPagerAdapter extends PagerAdapter {

        Context context;
        List<CarouselModel> carouselModels;
        int type;
        CarouselPagerAdapter(Context context, List<CarouselModel> carouselModels, int type) {
            this.context = context;
            this.carouselModels = carouselModels;
            this.type = type;

        }


        @Override
        public int getCount() {
            return carouselModels.size()>1?carouselModels.size()+2:carouselModels.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            CarouselModel carouselModel;
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            int innerPosition = carouselModels.size()>1?position == 0 ? carouselModels.size() - 1 :
                    position == carouselModels.size() + 1 ? 0 :
                            position - 1:
                    position ;
            carouselModel = carouselModels.get(innerPosition);
            if (type == LOCAL) {
                //本地
                imageView.setImageDrawable(context.getResources().getDrawable(carouselModel.getResId()));
            } else if (type == ONLINE) {
                //在线
                Glide.with(context)
                        .load(carouselModel.getUrl())
                        .apply(new RequestOptions()
                        .error(loadFailed!=-1?loadFailed:R.drawable.default_failed)
                        .placeholder(preLoad!=-1?preLoad:R.drawable.default_bg))
                        .into(imageView);
            }
            final int p = innerPosition;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(null,v,p,0);
                    }
                }
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


    float mPreviousOffset = 0f;


    /**
     * 加载图片列表
     *
     * @param resId
     * @param urls
     */
    private void loadImages(final Integer[] resId, String[] urls) {

        List<CarouselModel> carouselModels = new CopyOnWriteArrayList<>();
        if (type == LOCAL) {
            // 加载本地缓存图片
            size = resId.length;
            for (Integer integer:resId){
                CarouselModel carouselModel = new CarouselModel();
                carouselModel.setResId(integer);
                carouselModels.add(carouselModel);
            }

        } else if (type == ONLINE) {
            size = urls.length;

            for (String url:urls){
                CarouselModel carouselModel = new CarouselModel();
                carouselModel.setUrl(url);
                carouselModels.add(carouselModel);

            }

        }

        viewPager.setClickable(false);
        viewPager.setAdapter(new CarouselPagerAdapter(context,carouselModels,type));
        int count = radioGroup.getChildCount();
        if (count > 0) {
            radioGroup.removeAllViews();
        }

        for (int i = 0; i < size; i++) {
            RadioButton radioButton = new RadioButton(context);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginStart(ViewUtils.dp2px(context, 5));
            radioButton.setLayoutParams(layoutParams);
            radioButton.setButtonDrawable(context.getResources()
                    .getDrawable(R.drawable.radio_button_style));
            radioGroup.addView(radioButton);
        }
        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (viewPager.getAdapter() != null && size>1) {
                            int realPosition = position == 0 ? size - 2 : position == size + 1 ? 1 : position - 1;
                            if (positionOffset == 0
                                    && mPreviousOffset == 0
                                    && (position == 0 || position == size + 1)) {
                                viewPager.setCurrentItem(realPosition, false);
                            }
                        }
                        mPreviousOffset = positionOffset;
                    }

                    @Override
                    public void onPageSelected(int i) {
                        pos = i;
                        try {
                            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i-1);
                            if (radioButton != null) {
                                radioButton.setChecked(true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (viewPager.getAdapter()!=null && size>1){
                            int position = viewPager.getCurrentItem();
                            int realPosition = position == 0 ? size - 2 : position == size + 1 ? 1 : position - 1;
                            if (state == ViewPager.SCROLL_STATE_IDLE
                                    && (position == 0 || position == size + 1)) {
                                Log.d(TAG,"onPageScrollStateChanged,position:"+position+",realPos:"+realPosition);
                                viewPager.setCurrentItem(realPosition, false);
                            }

                        }

                    }

                });

        requestLayout();

        if (size>0) {
            viewPager.setCurrentItem(1);
        }else {
            viewPager.setVisibility(GONE);
        }
        startAnimation();
    }
    Handler mHandler =null;



    /**
     * 启动动画
     */
    private void startAnimation(){

        if (mHandler==null){
            mHandler = new Handler();

        }

        mHandler.removeCallbacksAndMessages(null);

        if (size>1) {
            radioGroup.setVisibility(VISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (viewPager != null && radioGroup.getChildCount() > 0) {
                        int next = pos > size ? 0 : pos + 1;
                        viewPager.setCurrentItem(next, true);
                    }
                    if (mHandler != null) {
                        mHandler.postDelayed(this, 5000);
                    }
                }
            }, 3000);
        }else {
            radioGroup.setVisibility(GONE);
        }
    }


    /**
     * 设置资源类型
     *
     * @param type 0：local,1:online
     */
    public void setType(int type) {
        if (type == LOCAL || type == ONLINE) {
            this.type = type;
        }
    }


    /**
     * 设置本地resId
     *
     * @param id
     */
    public void setResId(Integer id) {
        if (type == LOCAL && id > 0) {
            //local
            try {
                int[] ids = context.getResources().getIntArray(id);
                if (ids.length > 0) {
                    // load images
                    Integer[] integers = new Integer[ids.length];
                    int i = 0;
                    for (int a : ids) {
                        integers[i] = a;
                        i++;
                    }
                    setResId(integers);

                }else {
                    this.setBackground(getResources().getDrawable(preLoad==-1?R.drawable.default_bg:preLoad));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void setResId(Integer[] resId) {
        loadImages(resId, null);
    }


    public void setResId(List<Integer> resId) {
        setResId((Integer[]) resId.toArray());
    }


    /**
     * 设置在线图片链接
     *
     * @param id
     */
    public void setUrls(Integer id) {
        if (type == ONLINE && id > 0) {
            try {
                String[] strings = context.getResources().getStringArray(id);
                if (strings.length > 0) {
                    // glide load online images
                    setUrls(strings);
                }else {
                    this.setBackground(getResources().getDrawable(preLoad==-1?R.drawable.default_bg:preLoad));
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG,"设置url失败:"+e.getMessage());
            }

        }
    }



    public void setOnItemClick(AdapterView.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }


    public void setUrls(String[] urls) {
        if (urls != null && urls.length > 0) {
            loadImages(null, urls);
        }else {
            this.setBackground(getResources().getDrawable(preLoad==-1?R.drawable.default_bg:preLoad));
        }

    }


    public void setUrls(List<String> urls) {
        setUrls((String[]) urls.toArray());
    }



    private void inflater() {
        viewPager = new ViewPager(context);
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        viewPager.setBackgroundColor(Color.parseColor("#00000000"));
        addView(viewPager);
        radioGroup = new RadioGroup(context);
        LayoutParams radioLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //  android:layout_alignParentBottom="true"
        radioLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        //  android:layout_centerHorizontal="true"
        radioLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        radioGroup.setLayoutParams(radioLayoutParams);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        addView(radioGroup);
    }


}
