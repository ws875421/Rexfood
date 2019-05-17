package div.rex.seekfood;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NotiActivity extends AppCompatActivity {
    private TextView tvTitle, tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findViews();
        showInfo();
    }

    private void findViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
    }

    private void showInfo() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        String title = bundle.getString("title");
        String content = bundle.getString("content");
        tvTitle.setText(title);
        tvContent.setText(content);
    }
}
