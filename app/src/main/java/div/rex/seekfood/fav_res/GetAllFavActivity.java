package div.rex.seekfood.fav_res;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import div.rex.seekfood.R;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.CommonTask;
import div.rex.seekfood.vendor.VendorVO;


public class GetAllFavActivity extends AppCompatActivity {

    private CommonTask getallfavTask;
    private static final String TAG = "aaaaaaa";
    private TextView tvgetallfav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getallfav);


    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String mem_no = preferences.getString("mem_no", "");

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        StringBuffer s = new StringBuffer();
        List<VendorVO> favlist = null;
        String url = Util.URL + "fav_res/fav_res.do";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getallfav");
        jsonObject.addProperty("mem_no", mem_no);

        String jsonOut = jsonObject.toString();
        getallfavTask = new CommonTask(url, jsonOut);
        try {
            String jsonIn = getallfavTask.execute().get();

            Type listType = new TypeToken<List<Fav_ResVo>>() {
            }.getType();
//            favlist = new Gson().fromJson(jsonIn, listType);

            List<Fav_ResVo> favList = gson.fromJson(jsonIn, listType);
            for (Fav_ResVo fav_resVo : favList) {
                s.append(fav_resVo.getVendor_no() + "\n");
            }


//            showToast(this, favlist.toString());
            tvgetallfav = findViewById(R.id.tvgetallfav);

            tvgetallfav.setText(s);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


    }


}
