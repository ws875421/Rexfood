package div.rex.seekfood.wait_pos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;


import div.rex.seekfood.R;
import div.rex.seekfood.main.Holder;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.CommonTask;
import div.rex.seekfood.task.ImageTask;
import div.rex.seekfood.vendor.VendorVO;

import static android.view.View.VISIBLE;
import static div.rex.seekfood.main.Util.showToast;


public class WaitPosActivity extends AppCompatActivity {

    private static final String TAG = "WaitPosActivity";
    private SeekBar waitNum;
    private int party_size;
    private VendorVO vendorVO;
    private TextView tvWnum, tvNoticeMsg;
    private ImageTask vendorImageTask;
    private ImageView ivPosLogo;
    private Button btnPush, btnNoPush, btnNotice1, btnNotice2, btnNotice3, button4;
    private CommonTask getPosTask;
    private String mem_no, pushstatus, notic;
    private TextView tvVName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wops);
        findView();


        waitNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                button4.setVisibility(VISIBLE);
                party_size = progress + 1;
                tvWnum.setText(party_size + " 人 ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                tvWnum.setText(party_size + " 人 ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                tvWnum.setText(party_size + " 人 ");
            }
        });


        vendorVO = (VendorVO) this.getIntent().getSerializableExtra("vendorVO");
        mem_no = (String) this.getIntent().getSerializableExtra("mem_no");

        if (vendorVO == null) {
            showToast(this, "查無餐廳資訊");
        }


        //圖片
        Bitmap bitmap = null;
        String url = Util.URL + "vendor/vendor.do";
        int imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        vendorImageTask = new ImageTask(url, "vendor_no", vendorVO.getVendor_no(), imageSize);
        ivPosLogo = findViewById(R.id.ivPosLogo);
        try {
            bitmap = vendorImageTask.execute().get();
            ivPosLogo.setImageBitmap(bitmap);
        } catch (ExecutionException e) {
            Log.e(TAG, e.toString());
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        }

        tvVName.setText(vendorVO.getV_name());

    }

    private void findView() {
        waitNum = findViewById(R.id.waitNum);
        tvWnum = findViewById(R.id.tvWnum);
        tvNoticeMsg = findViewById(R.id.tvNoticeMsg);
        btnPush = findViewById(R.id.btnPush);
        btnNoPush = findViewById(R.id.btnNoPush);
        btnNotice1 = findViewById(R.id.btnNotice1);
        btnNotice2 = findViewById(R.id.btnNotice2);
        btnNotice3 = findViewById(R.id.btnNotice3);
        tvVName = findViewById(R.id.tvVName);
        button4 = findViewById(R.id.button4);
    }

    //推播訊息
    public void OnPush(View view) {
        pushstatus = "yes";
        notic = null;

        btnPush.setBackgroundColor(Color.rgb(233, 30, 99));
        btnNoPush.setBackgroundColor(Color.rgb(192, 192, 192));
        tvNoticeMsg.setVisibility(VISIBLE);
        btnNotice1.setVisibility(VISIBLE);
        btnNotice2.setVisibility(VISIBLE);
        btnNotice3.setVisibility(VISIBLE);
    }

    //無推播
    public void OnPushNot(View view) {
        pushstatus = "no";
        notic = null;

        btnNoPush.setBackgroundColor(Color.rgb(233, 30, 99));
        btnPush.setBackgroundColor(Color.rgb(192, 192, 192));
        tvNoticeMsg.setVisibility(view.GONE);
        btnNotice1.setVisibility(view.GONE);
        btnNotice2.setVisibility(view.GONE);
        btnNotice3.setVisibility(view.GONE);
    }

    //提醒票數
    public void OnNotice1(View view) {
        notic = (String) btnNotice1.getText();
        btnNotice1.setBackgroundColor(Color.rgb(233, 30, 99));
        btnNotice2.setBackgroundColor(Color.rgb(192, 192, 192));
        btnNotice3.setBackgroundColor(Color.rgb(192, 192, 192));
    }

    public void OnNotice2(View view) {
        notic = (String) btnNotice2.getText();
        btnNotice2.setBackgroundColor(Color.rgb(233, 30, 99));
        btnNotice1.setBackgroundColor(Color.rgb(192, 192, 192));
        btnNotice3.setBackgroundColor(Color.rgb(192, 192, 192));
    }

    public void OnNotice3(View view) {
        notic = (String) btnNotice3.getText();
        btnNotice3.setBackgroundColor(Color.rgb(233, 30, 99));
        btnNotice2.setBackgroundColor(Color.rgb(192, 192, 192));
        btnNotice1.setBackgroundColor(Color.rgb(192, 192, 192));
    }


    public void getWaitPos(View view) {

        if (pushstatus == null) {
            showToast(Holder.getContext(), "請選擇提醒模式");
            return;
        } else if (pushstatus.equals("yes") && notic == null) {
            showToast(Holder.getContext(), "請選擇訊息提示");
            return;
        }


        String vendor_no = vendorVO.getVendor_no();
//        MemberVo memberVo = new MemberVo();
//        memberVo.setMem_no("M000001");
//        String mem_no = memberVo.getMem_no();

        String url = Util.URL + "wait_pos/wait_pos2.do";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "insertPhone");
        jsonObject.addProperty("party_size", party_size);
        jsonObject.addProperty("mem_no", mem_no);
        jsonObject.addProperty("vendor_no", vendor_no);
        String jsonOut = jsonObject.toString();
        getPosTask = new CommonTask(url, jsonOut);

        String result = null;
        try {
            result = getPosTask.execute().get();
//            showToast(this, result);
        } catch (Exception e) {
            Log.e(TAG, e.toString());

        }

        if (result.equals("\"目前不開放候位\"")) {
            showToast(Holder.getContext(),"目前不開放候位");
            return;
        } else if (result.equals("\"不得重複抽號\"")) {
            Intent intent = new Intent(WaitPosActivity.this, ShowPos.class);
            intent.putExtra("vendorVO", vendorVO);
            intent.putExtra("party_size", party_size);
            finish();
            startActivity(intent);
        } else {

            //            {"號碼牌":4,"前面還有幾組人":0}
            Gson gson = new Gson();
            JsonObject jsonObject2 = gson.fromJson(result, JsonObject.class);
            String PosNum = jsonObject2.get("號碼牌").getAsString();
            String WGroup = jsonObject2.get("前面還有幾組人").getAsString();


            SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
            preferences.edit()
                    .putString("posVendor", vendorVO.getVendor_no())    //候位餐廳
                    .putString("PosNum", PosNum)                        //候位號碼牌
                    .putString("WGroup", WGroup)                        //前方還有幾組
                    .putInt("party_size", party_size)                   //候位人數
                    .putString("V_name", vendorVO.getV_name())
                    .apply();

            if (pushstatus.equals("yes")) {
                preferences.edit()
                        .putString("pushstatus", pushstatus)            //開啟推播
                        .putString("notic", notic)                      //推播提醒號碼
                        .apply();
            }


            Intent intent = new Intent(WaitPosActivity.this, ShowPos.class);
            intent.putExtra("vendorVO", vendorVO);
            intent.putExtra("party_size", party_size);


            finish();
            startActivity(intent);
        }


    }
}

