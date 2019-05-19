package div.rex.seekfood.vendor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import div.rex.seekfood.R;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.CommonTask;

import static div.rex.seekfood.main.Util.showToast;


public class VendorLogin extends AppCompatActivity {
    private static final String TAG = "VendorLogin";
    private TextView tvMessage;
    private EditText logtvid, logtvpwd;
    private String v_account, v_pwd;
    private CommonTask isVendorTask;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private VendorVO vendorVO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findview();
    }

    private void findview() {
        logtvid = findViewById(R.id.logtvid);
        logtvpwd = findViewById(R.id.logtvpwd);
        tvMessage = findViewById(R.id.tvMessage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        if (login) {
            String v_account = preferences.getString("v_account", "");
            String v_pwd = preferences.getString("v_pwd", "");
            if (isVendor(v_account, v_pwd)) {
                setResult(RESULT_OK);
                finish();
                //已登入過的畫面可以切換至這裡
            }
        }
    }


    public void logincheck(View view) {

        v_account = logtvid.getText().toString();
        v_pwd = logtvpwd.getText().toString();

        if (v_account.length() <= 0 || v_pwd.length() <= 0) {
            showMessage(R.string.msg_InvalidUserOrPassword);
            return;
        }

        if (!isVendor(v_account, v_pwd)) {
            showMessage(R.string.msg_InvalidUserOrPassword);

        } else {
            SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
            preferences.edit()
                    .putBoolean("login", true)
                    .putString("vendor_no", vendorVO.getVendor_no())
                    .putString("v_account", vendorVO.getV_account())
                    .putString("v_name", vendorVO.getV_name())
                    .putString("v_type", vendorVO.getV_type())
                    .apply();

            setResult(RESULT_OK);
            finish();
        }

    }

    private boolean isVendor(final String v_account, final String v_pwd) {
        boolean isVendor = false;
        if (Util.networkConnected(this)) {
            String url = Util.URL + "vendor/vendor.ad";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isVendor");
            jsonObject.addProperty("v_account", v_account);
            jsonObject.addProperty("v_pwd", v_pwd);
            String jsonOut = jsonObject.toString();
            isVendorTask = new CommonTask(url, jsonOut);
            try {
                String result = isVendorTask.execute().get();

                if (result.equals("false")) {
                    isVendor = Boolean.valueOf(result);
                } else {
                    vendorVO = gson.fromJson(result, VendorVO.class);
                    isVendor = Boolean.valueOf("true");
                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
                isVendor = false;
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }
        return isVendor;
    }

    private void showMessage(int msgResId) {
        tvMessage.setText(msgResId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isVendorTask != null) {
            isVendorTask.cancel(true);
        }
    }

}
