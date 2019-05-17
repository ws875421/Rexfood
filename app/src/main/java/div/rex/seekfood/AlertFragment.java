package div.rex.seekfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public  class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                //設定訊息內容
                .setMessage("aaaa")
                //設定確認鍵 (positive用於確認)
                .setPositiveButton("ccc", this)
                //設定取消鍵 (negative用於取消)
                .setNegativeButton("ffff", this)
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