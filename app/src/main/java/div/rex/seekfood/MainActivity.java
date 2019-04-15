package div.rex.seekfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Banner mBanner;
    private ArrayList<String> images;
    private ArrayList<String> imageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, FirseActivity.class);
        startActivity(intent);
        mBanner = findViewById(R.id.banner);

        //初始化数据
        initData();
        //初始化view
        initView();

    }

    private void initView() {
        mBanner = findViewById(R.id.banner);
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
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Toast.makeText(MainActivity.this, "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
                    }
                })
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
        imageTitle.add("十大星级品牌联盟，全场2折起");
        imageTitle.add("嗨购5折不要停");
        imageTitle.add("双12趁现在");
        imageTitle.add("嗨购5折不要停，12.12趁现在");
        imageTitle.add("实打实大优惠");
        imageTitle.add("买到就是赚到");

    }

    /**
     * 网络加载图片
     * 使用了Glide图片加载框架
     */
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load((String) path)
                    .into(imageView);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    //##  離開上一頁設定
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertFragment alertFragment = new AlertFragment();
            FragmentManager fm = getSupportFragmentManager();
            alertFragment.show(fm, "alert");
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {
        //離開上一頁設定
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    //設定圖示
                    .setIcon(R.drawable.alert)
                    .setTitle("This is Dialog")
                    //設定訊息內容
                    .setMessage(R.string.message_alert)
                    //設定確認鍵 (positive用於確認)
                    .setPositiveButton(R.string.text_btYes, this)
                    //設定取消鍵 (negative用於取消)
                    .setNegativeButton(R.string.text_btNo, this)
                    .create();
            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    getActivity().finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
                default:
                    break;
            }
        }
    }
}
