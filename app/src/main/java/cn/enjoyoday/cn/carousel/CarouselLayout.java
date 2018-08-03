package cn.enjoyoday.cn.carousel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 轮播view
 */
public class CarouselLayout extends RelativeLayout {


    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private Context context;
    //数据源
    private List<CarouselModel> carouselModels = new CopyOnWriteArrayList<>();

    public CarouselLayout(Context context) {
        this(context,null,0);
    }

    public CarouselLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CarouselLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        if (attrs!=null) {
            @SuppressLint("Recycle")
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CarouselLayout);



            typedArray.recycle();



        }
        inflater();
    }


    private void inflater(){
        viewPager = new ViewPager(context);
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        addView(viewPager);
        radioGroup = new RadioGroup(context);
        LayoutParams radioLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        //  android:layout_alignParentBottom="true"
        radioLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        //  android:layout_centerHorizontal="true"
        radioLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        radioGroup.setLayoutParams(radioLayoutParams);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        addView(radioGroup);
    }





    public void setData(){




    }




















}
