package div.rex.seekfood.vendor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import div.rex.seekfood.R;


public class VendorSearch extends Fragment {

    public VendorSearch() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
            為了產生Fragment的畫面，透過inflater物件呼叫inflate()取得指定layout檔的內容，3個參數：
            1. R.layout.dynamic_fragment所代表的layout檔將成為Fragment的畫面
            2. container是Activity所設定的ViewGroup
                (對DynamicFragment來說，container即是FrameLayout)
            3. false代表不要將產生的畫面附加在container上，因為在Activity已經附加過了，就不再多此一舉
        */
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        // 找到view裡面的TextView子元件
//        TextView textView = view.findViewById(R.id.textView);
//        String title = getArguments().getString("title");
//        textView.setText(title);
        return view;
    }
}
