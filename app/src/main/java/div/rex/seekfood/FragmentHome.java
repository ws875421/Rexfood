package div.rex.seekfood;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import div.rex.seekfood.vendor.VendorlistFragment;


public class FragmentHome extends Fragment {

    private final static String TAG = "fragment";
    private Banner mBanner;
    private ArrayList<String> images;
    private ArrayList<String> imageTitle;


    public FragmentHome() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
            為了產生Fragment的畫面，透過inflater物件呼叫inflate()取得指定layout檔的內容，3個參數：
            1. R.layout.dynamic_fragment所代表的layout檔將成為Fragment的畫面
            2. container是Activity所設定的ViewGroup
                (對DynamicFragment來說，container即是FrameLayout)
            3. false代表不要將產生的畫面附加在container上，因為在Activity已經附加過了，就不再多此一舉
        */
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // 找到view裡面的TextView子元件

        mBanner = view.findViewById(R.id.banner);
        //初始化数据
        initData();
        //初始化view
        initView();

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        VendorlistFragment vendorlist = new VendorlistFragment();
        Bundle bundle = new Bundle();
        vendorlist.setArguments(bundle);

        transaction.replace(R.id.frameLayout, vendorlist, TAG);
        transaction.commit();


        return view;
    }


    /**
     * 网络加载图片
     * 使用了Glide图片加载框架
     */
    private void initView() {
//        mBanner = findViewById(R.id.banner);
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        //可选样式如下:
        //1. Banner.CIRCLE_INDICATOR    显示圆形指示器
        //2. Banner.NUM_INDICATOR   显示数字指示器
        //3. Banner.NUM_INDICATOR_TITLE 显示数字指示器和标题
        //4. Banner.CIRCLE_INDICATOR_TITLE  显示圆形指示器和标题
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(imageTitle);
        //设置轮播样式（没有标题默认为右边,有标题时默认左边）
        //可选样式:
        //Banner.LEFT   指示器居左
        //Banner.CENTER 指示器居中
        //Banner.RIGHT  指示器居右
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置是否允许手动滑动轮播图
        mBanner.setViewPagerIsScroll(true);
        //设置是否自动轮播（不设置则默认自动）
        mBanner.isAutoPlay(true);
        //设置轮播图片间隔时间（不设置默认为2000）
        mBanner.setDelayTime(1500);
        //设置图片资源:可选图片网址/资源文件，默认用Glide加载,也可自定义图片的加载框架
        //所有设置参数方法都放在此方法之前执行
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setImages(images)
//                .setOnBannerListener(new OnBannerListener() {
//                    @Override
//                    public void OnBannerClick(int position) {
//                        Toast.makeText(getActivity(), "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
//                    }
//                })
                .start();

    }

    private void initData() {
        //设置图片资源:url或本地资源
        images = new ArrayList<>();
        images.add("https://i.imgur.com/g7JhnfE.jpg");
        images.add("http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg");
        images.add("http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg");
        images.add("http://img.zcool.cn/community/01fda356640b706ac725b2c8b99b08.jpg");
        images.add("https://i.imgur.com/FgR2Oh8.jpg");
        images.add("https://i.imgur.com/M9lA0Rk.jpg");
        //设置图片标题:自动对应
        imageTitle = new ArrayList<>();
        imageTitle.add("101跳樓大拍賣");
        imageTitle.add("蜂蜜不甜砍頭");
        imageTitle.add("帝王蟹季");
        imageTitle.add("下麵給你吃");
        imageTitle.add("過年大特價");
        imageTitle.add("蜜汁大鮑魚");

    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load((String) path)
                    .into(imageView);
        }

    }

    /**
     * End
     */


}
