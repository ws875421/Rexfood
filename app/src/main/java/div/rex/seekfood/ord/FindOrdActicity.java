package div.rex.seekfood.ord;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.lang.reflect.Type;
import java.util.List;

import div.rex.seekfood.R;
import div.rex.seekfood.main.Holder;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.CommonTask;

import static div.rex.seekfood.main.Util.showToast;


public class FindOrdActicity extends AppCompatActivity {

    private static final String TAG = "FindOrdActicity";
    private static final String LOG_TAG = "QRCodeGenerator";

    private TextView tvord;
    private CommonTask getordTask;
    private OrdVO OrdVO = null;
    private ImageView ivCode;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private String ord_no = null;
    private String mem_no;
    private String vendor_no = null;
    private String verif_code = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ord);

        tvord = findViewById(R.id.tvord);
        ivCode = findViewById(R.id.ivCode);
        Button btnQRCode = findViewById(R.id.btnQRCode);
        getord();
    }


    public void getord() {

        if (Util.networkConnected(this)) {

            SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
            mem_no = preferences.getString("mem_no", "");
            String url = Util.URL + "ord/ord2.ad";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getord");
            jsonObject.addProperty("mem_no", mem_no);

            String jsonOut = jsonObject.toString();
            getordTask = new CommonTask(url, jsonOut);

            List<OrdVO> ordList = null;
            String jsonStr = "";

            try {
                String result = getordTask.execute().get();

//                OrdVO = gson.fromJson(result, OrdVO.class);
//                showToast(Holder.getContext(),String.valueOf(result.length()));
                if (result.length() <= 2) {
                    showToast(Holder.getContext(), "查無訂單資料");
                    finish();
                }

                Type collectionType = new TypeToken<List<OrdVO>>() {
                }.getType();
                List<OrdVO> List = gson.fromJson(result, collectionType);
//                jsonStr = gson.toJson(List);


                for (OrdVO vo : List) {

                    ord_no = vo.getOrd_no();
                    vendor_no = vo.getVendor_no();
                    verif_code = vo.getVerif_code();
                }


//                preferences.edit()
//                        .putString("ord_no", ord_no)
//                        .putString("ord_vendor_no", vendor_no)
//                        .apply();


//                showToast(this, gson.toJson(List));

                tvord = findViewById(R.id.tvord);
                tvord.setText("訂單編號:" + ord_no + "\n" + "會員:" + mem_no + "\n" + "餐廳:" + vendor_no + "\n" + "驗證碼:" + verif_code);


            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }


    }

    public void showQR(View v) {

//        EditText etQRCodeText = findViewById(R.id.etQRCodeText);
//        String qrCodeText = etQRCodeText.getText().toString();
        String jsonStr = "";
        QRord ord = new QRord(ord_no, mem_no, vendor_no, verif_code);
        jsonStr = gson.toJson(ord);

        int smallerDimension = getDimension();
        switch (v.getId()) {
            case R.id.btnQRCode:
                Log.e(LOG_TAG, jsonStr);
                // Encode with a QR Code image
                QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(jsonStr, null,
                        Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                        smallerDimension);
                try {
                    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                    ivCode.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
        }


    }

    private int getDimension() {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 取得螢幕尺寸
        Display display = manager.getDefaultDisplay();
        // API 13列為deprecated，但為了支援舊版手機仍採用
        int width = display.getWidth();
        int height = display.getHeight();

        // 產生的QR code圖形尺寸(正方形)為螢幕較短一邊的1/2長度
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension / 2;

        // API 13開始支援
//                Display display = manager.getDefaultDisplay();
//                Point point = new Point();
//                display.getSize(point);
//                int width = point.x;
//                int height = point.y;
//                int smallerDimension = width < height ? width : height;
//                smallerDimension = smallerDimension / 2;
        return smallerDimension;
    }

}
