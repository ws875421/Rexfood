package div.rex.seekfood.vendor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import div.rex.seekfood.R;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.ImageTask;
import div.rex.seekfood.task.ImageTask2;


public class VendorDetailActivity extends AppCompatActivity {

    private static final String TAG = "aaaaaaa";
    private List<vendorImages> vendorImagesList;
    private ImageTask vendorImageTask;
    private ImageTask2 vendorImageTask2;
    private VendorVO vendorVO;
    private TextView tv_vName, tv_detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendordetail);


    }

    @Override
    protected void onResume() {
        super.onResume();

        vendorVO = (VendorVO) this.getIntent().getSerializableExtra("vendorVO");
        if (vendorVO == null) {
            Util.showToast(this, "查無餐廳資訊");
        }

        vendorImagesList = getvendorImagesList();

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new vendorImagesAdapter(getSupportFragmentManager(), vendorImagesList));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        tv_vName = findViewById(R.id.tv_vName);
        tv_detail = findViewById(R.id.tv_detail);

        tv_vName.setText(vendorVO.getV_name());
        tv_detail.setText("地址: " + vendorVO.getV_address1() + "" + vendorVO.getV_address2() + "" + vendorVO.getV_address3() + "\n"
                + "電話: (" + vendorVO.getV_n_code() + ")" + vendorVO.getV_tel() + "\n"
                + "餐廳類型: " + vendorVO.getV_type() + "\n"
                + "營業時間: " + vendorVO.getV_start_time() + " - " + vendorVO.getV_end_time() + "\n"
                + "店家特色: " + vendorVO.getV_text()
        );


    }

    private List<vendorImages> getvendorImagesList() {

        Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        String url = Util.URL + "vendor/vendor.do";
        String url2 = Util.URL + "vendor/vendor2.do";
        int imageSize = getResources().getDisplayMetrics().widthPixels / 4;

        vendorImageTask = new ImageTask(url, "vendor_no", vendorVO.getVendor_no(), imageSize);
        vendorImageTask2 = new ImageTask2(url2, "vendor_no", vendorVO.getVendor_no(), imageSize);

        try {
            bitmap = vendorImageTask.execute().get();
            bitmap2 = vendorImageTask2.execute().get();
        } catch (ExecutionException e) {
            Log.e(TAG, e.toString());
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        }


        List<vendorImages> vendorImagesList = new ArrayList<>();
        vendorImagesList.add(new vendorImages(bitmap, "餐廳圖片"));
        vendorImagesList.add(new vendorImages(bitmap2, "餐廳活動"));

        return vendorImagesList;
    }

    private class vendorImagesAdapter extends FragmentPagerAdapter {
        private List<vendorImages> vendorImagesList;

        public vendorImagesAdapter(FragmentManager fm, List<vendorImages> vendorImagesList) {
            super(fm);

            this.vendorImagesList = vendorImagesList;
        }

        @Override
        public Fragment getItem(int i) {
            return vendorImagesFragment.newInstance(vendorImagesList.get(i));
        }

        @Override
        public int getCount() {
            return vendorImagesList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return vendorImagesList.get(position).getTitle();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();

        if (vendorImageTask2 != null) {
            vendorImageTask2.cancel(true);
        }
        if (vendorImageTask != null) {
            vendorImageTask.cancel(true);
        }
    }

}
