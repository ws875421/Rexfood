package div.rex.seekfood.member;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import div.rex.seekfood.R;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.CommonTask;

import static div.rex.seekfood.main.Util.showToast;


public class MemberLogin extends AppCompatActivity {
    private static final String TAG = "MemberLogin";
    private TextView tvMessage;
    private EditText logtvid, logtvpwd;
    private String member_accout, member_pwd;
    private CommonTask isMemberTask;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private MemberVo memberVo = null;
    private Button button3,button5,bmag4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findview();

        button3.setVisibility(View.VISIBLE);
        button5.setVisibility(View.VISIBLE);
        bmag4.setVisibility(View.VISIBLE);
    }

    private void findview() {
        logtvid = findViewById(R.id.logtvid);
        logtvpwd = findViewById(R.id.logtvpwd);
        tvMessage = findViewById(R.id.tvMessage);

        button3=findViewById(R.id.button3);
        button5=findViewById(R.id.button5);
        bmag4=findViewById(R.id.bmag4);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        if (login) {
            String member_accout = preferences.getString("member_accout", "");
            String member_pwd = preferences.getString("member_pwd", "");
            if (isMember(member_accout, member_pwd)) {
                setResult(RESULT_OK);
                finish();
                //已登入過的畫面可以切換至這裡
            }
        }
    }


    public void logincheck(View view) {

        member_accout = logtvid.getText().toString();
        member_pwd = logtvpwd.getText().toString();
//        showToast(this, member_accout + " & " + member_pwd);

        if (member_accout.length() <= 0 || member_pwd.length() <= 0) {
            showMessage(R.string.msg_InvalidUserOrPassword);
            return;
        }

        if (!isMember(member_accout, member_pwd)) {
            showMessage(R.string.msg_InvalidUserOrPassword);
//            showToast(this, "74");

        } else {
            SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
            preferences.edit()
                    .putBoolean("login", true)
                    .putString("mem_no", memberVo.getMem_no())
                    .putString("mem_name", memberVo.getMem_name())
                    .putString("member_accout", memberVo.getMem_account())
                    .putString("mem_gender", memberVo.getMem_gender().equals("M") ? "男" : "女")
                    .putString("mem_mail", memberVo.getMem_mail())
                    .putString("mem_id", memberVo.getMem_id())
                    .putString("mem_tel", memberVo.getMem_tel())
                    .putString("mem_status", memberVo.getMem_status())
                    .putString("mem_balance", memberVo.getMem_balance().toString())
                    .putString("mem_nickname", memberVo.getMem_nickname())
                    .apply();

//            String t = preferences.getString("mem_gender", "無資料");
//            boolean login = preferences.getBoolean("login", false);
//            showToast(this, t);

            setResult(RESULT_OK);
            finish();
        }

    }

    private boolean isMember(final String userId, final String password) {
        boolean isMember = false;
        if (Util.networkConnected(this)) {
            String url = Util.URL + "member/member.ad";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isMember");
            jsonObject.addProperty("mem_account", userId);
            jsonObject.addProperty("mem_pwd", password);
            String jsonOut = jsonObject.toString();
            isMemberTask = new CommonTask(url, jsonOut);
            try {
                String result = isMemberTask.execute().get();

                if (result.equals("false")) {
                    isMember = Boolean.valueOf(result);
                } else {

                    memberVo = gson.fromJson(result, MemberVo.class);
//                    showToast(this, memberVo.toString());
                    isMember = Boolean.valueOf("true");

                }
//                isMember = Boolean.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                isMember = false;
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }
//        showToast(this, String.valueOf(isMember));
        return isMember;
    }

    private void showMessage(int msgResId) {
        tvMessage.setText(msgResId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isMemberTask != null) {
            isMemberTask.cancel(true);
        }
    }

    public void Onmag1(View v) {
        logtvid.setText("c123456");
        logtvpwd.setText("c1234567");

    }

    public void Onmag2(View v) {
        logtvid.setText("f1234562");
        logtvpwd.setText("f1234562");

    }
    public void Onmag3(View v) {
        logtvid.setText("qq1234");
        logtvpwd.setText("aa1234");

    }


}
