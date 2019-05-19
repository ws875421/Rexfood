package div.rex.seekfood.scanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import div.rex.seekfood.R;
import div.rex.seekfood.main.Holder;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.CommonTask;

import static div.rex.seekfood.main.Util.showToast;


public class scannerActivity extends AppCompatActivity {

    private final static String TAG = "scannerActivity";
    private TextView tvscanner;
    private String message, contents;

    private CommonTask isOrdTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        tvscanner = findViewById(R.id.tvscanner);

        contents = getIntent().getStringExtra("contents");

        String result = null;
        //
        if (Util.networkConnected(this)) {
            String url = Util.URL + "ord/ord.ad";

            isOrdTask = new CommonTask(url, contents);
            try {

                result = isOrdTask.execute().get();

                if (result.equals("false")) {
                    showToast(Holder.getContext(), "驗證失敗");
                } else {
                    showToast(Holder.getContext(), "驗證成功");
                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }


        tvscanner.setText("contents: " + result);
    }


}
