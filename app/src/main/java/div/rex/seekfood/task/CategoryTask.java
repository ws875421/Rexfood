package div.rex.seekfood.task;


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import div.rex.seekfood.category.Category;
import div.rex.seekfood.main.Util;


public class CategoryTask extends AsyncTask<String, Integer, List<Category>> {
    private static final String TAG = "CategoryTask";

    @Override
    protected List<Category> doInBackground(String... strings) {
        String urlString = Util.URL + "CategoryServlet";
        DataOutputStream dos = null;
        HttpURLConnection connection = null;
        StringBuilder sb = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true); // allow inputs
            connection.setDoOutput(true); // allow outputs
            // 不知道請求內容大小時可以呼叫此方法將請求內容分段傳輸，設定0代表使用預設大小
            // 參考HttpURLConnection API的Posting Content部分
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false); // do not use a cached copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            connection.connect();

            dos = new DataOutputStream(connection.getOutputStream());
            // 使用此方式則可以在servlet使用req.getParameter方法取得請求參數 (可跟web端servlet對接)
            String content = "action=getAll";
            dos.writeBytes(content);
            dos.flush();

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    sb.append(strLine);
                }
                br.close();
            } else {
                Log.e(TAG, "statusCode = " + statusCode);
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        if (sb != null) {
            // decode JSON to List<Category>
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Category>>() {
            }.getType();
            return gson.fromJson(sb.toString(), listType);
        }
        return null;
    }
}
