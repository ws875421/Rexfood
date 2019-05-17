package div.rex.seekfood.wait_pos;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

import div.rex.seekfood.R;
import div.rex.seekfood.main.Holder;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.CommonTask;
import div.rex.seekfood.task.ImageTask;
import div.rex.seekfood.vendor.VendorVO;

import static div.rex.seekfood.main.Util.showToast;


public class ShowPos extends AppCompatActivity {

    private static final String TAG = "ShowPos";
    private VendorVO vendorVO;
    private ImageView ivshowLogo2;
    private ImageTask vendorImageTask;
    private TextView tvshow1, tvshow2, tvshow3, tvshow4;
    private Button cancelPos;
    private CommonTask canclePosTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpos);
        findView();


    }

    private void findView() {
        ivshowLogo2 = findViewById(R.id.ivshowLogo2);
        tvshow1 = findViewById(R.id.tvshow1);
        tvshow2 = findViewById(R.id.tvshow2);
        tvshow3 = findViewById(R.id.tvshow3);
        tvshow4 = findViewById(R.id.tvshow4);
        cancelPos = findViewById(R.id.cancelPos);

    }

    @Override
    public void onResume() {
        super.onResume();

        //圖片
        vendorVO = (VendorVO) this.getIntent().getSerializableExtra("vendorVO");

        String url = Util.URL + "vendor/vendor.do";
        int imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        int party_size = 0;
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);

        String posVendor = preferences.getString("posVendor", "");

        if (posVendor.equals("")) {
            showToast(Holder.getContext(), "尚未候位");
            finish();
            return;
        }

        if (vendorVO == null) {


//            String posVendor = preferences.getString("posVendor", "");
            String V_name = preferences.getString("V_name", "");
            party_size = preferences.getInt("party_size", 0);

            vendorImageTask = new ImageTask(url, "vendor_no", posVendor, imageSize);

            tvshow1.setText(V_name);
        } else {
            vendorImageTask = new ImageTask(url, "vendor_no", vendorVO.getVendor_no(), imageSize);
            tvshow1.setText(vendorVO.getV_name());
            party_size = (Integer) this.getIntent().getSerializableExtra("party_size");
        }


        //圖片

        Bitmap bitmap = null;


        ivshowLogo2 = findViewById(R.id.ivshowLogo2);
        try {
            bitmap = vendorImageTask.execute().get();
            ivshowLogo2.setImageBitmap(bitmap);
        } catch (ExecutionException e) {
            Log.e(TAG, e.toString());
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        }

        //店名


        //候位序號
//        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String PosNum = preferences.getString("PosNum", "");
//        String PosNum = (String) this.getIntent().getSerializableExtra("PosNum");
        tvshow2.setText("候位序號:" + PosNum);


        //幾人桌
//        int party_size = (Integer) this.getIntent().getSerializableExtra("party_size");
        if (party_size % 2 == 0) {
            tvshow3.setText("幾人桌:" + String.valueOf(party_size) + "\n" + "前面還有幾組");
        } else {
            tvshow3.setText("幾人桌:" + String.valueOf(party_size + 1 + "\n" + "前面還有幾組"));
        }

        //還有幾組
//        String WGroup = (String) this.getIntent().getSerializableExtra("WGroup");
        String WGroup = preferences.getString("WGroup", "");
        tvshow4.setText(WGroup);

    }

    public void Oncancel(View view) {
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        int party_size = preferences.getInt("party_size", 0);
        String mem_no = preferences.getString("mem_no", "");
        String vendor_no = preferences.getString("posVendor", "");

        String url = Util.URL + "wait_pos/wait_pos2.do";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "cancelPhone");
        jsonObject.addProperty("party_size", party_size);
        jsonObject.addProperty("mem_no", mem_no);
        jsonObject.addProperty("vendor_no", vendor_no);
        String jsonOut = jsonObject.toString();
        canclePosTask = new CommonTask(url, jsonOut);
        canclePosTask.execute();


        preferences.edit()
                .putString("posVendor", "")   //候位餐廳
                .putString("PosNum","")       //候位號碼牌
                .putString("WGroup", "")      //前方還有幾組
                .putInt("party_size", 0)      //候位人數
                .putString("pushstatus", "")
                .putString("notic", "")
                .apply();
        showToast(Holder.getContext(),"已取消候位");
        finish();

    }
}