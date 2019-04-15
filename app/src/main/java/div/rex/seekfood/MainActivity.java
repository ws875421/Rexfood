package div.rex.seekfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, FirseActivity.class);
        startActivity(intent);
    }

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
