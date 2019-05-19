package div.rex.seekfood.vendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import div.rex.seekfood.R;
import div.rex.seekfood.main.Util;
import div.rex.seekfood.task.CommonTask;
import div.rex.seekfood.task.ImageTask;

import static div.rex.seekfood.main.Util.showToast;


public class VendorlistFragment extends Fragment {
    private final static String TAG = "VendorlistFragment";
    private RecyclerView vendorlist;
    private CommonTask getVendorTask;
    private ImageTask VendorImageTask;
    private ImageView ivAddHeart;


    public VendorlistFragment() {
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
        View view = inflater.inflate(R.layout.fragment_vendrolist, container, false);

//        FragmentManager manager2 = getChildFragmentManager();
//        FragmentTransaction transaction2 = manager2.beginTransaction();
//        VendorlistFragment VendorSearch = new VendorlistFragment();
//        Bundle bundle2 = new Bundle();
//        VendorSearch.setArguments(bundle2);
//        transaction2.replace(R.id.frameHome, VendorSearch, TAG);
//        transaction2.commit();



        vendorlist = view.findViewById(R.id.vendorlist);
        vendorlist.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.networkConnected(this.getActivity())) {
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getAll");
                String jsonOut = jsonObject.toString();
                updateUI(jsonOut);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else {
            showToast(this.getActivity(), R.string.msg_NoNetwork);
        }
    }

    private void updateUI(String jsonOut) {
        getVendorTask = new CommonTask(Util.URL + "vendor/vendor.ad", jsonOut);
        List<VendorVO> vendorList = null;
        try {
            String jsonIn = getVendorTask.execute().get();

            Type listType = new TypeToken<List<VendorVO>>() {
            }.getType();
            vendorList = new Gson().fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (vendorList == null || vendorList.isEmpty()) {
            showToast(this.getActivity(), "查無餐聽資料");
        } else {
            vendorlist.setAdapter(new VendorListAdapter(this.getActivity(), vendorList));
        }
    }

    private class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.MyViewHolder> {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<VendorVO> vendorList;
        private int imageSize;

        VendorListAdapter(Context context, List<VendorVO> vendorList) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.vendorList = vendorList;
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivPicture, ivAddHeart;
            TextView tvName, tvPath;

            MyViewHolder(View itemView) {
                super(itemView);
                ivPicture = itemView.findViewById(R.id.ivPicture);
                ivAddHeart = itemView.findViewById(R.id.ivAddHeart);
                tvName = itemView.findViewById(R.id.tvName);
                tvPath = itemView.findViewById(R.id.tvPath);

                ivAddHeart = itemView.findViewById(R.id.ivAddHeart);

            }
        }

        @Override
        public int getItemCount() {
            return vendorList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.cardview_vendor, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final VendorVO vendorVO = vendorList.get(position);
            String url = Util.URL + "vendor/vendor.ad";

            String vendor_no = vendorVO.getVendor_no();
            VendorImageTask = new ImageTask(url, "vendor_no", vendor_no, imageSize, holder.ivPicture);
            VendorImageTask.execute();

//            SharedPreferences preferences = context.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
//            boolean islogin = preferences.getBoolean("login", false);
//
//            if (islogin) {
//                holder.ivAddHeart.setImageResource(R.drawable.heart_red);
//            } else {
//
//            }

            holder.tvName.setText(vendorVO.getV_name());
            holder.tvPath.setText(vendorVO.getV_address1() + "" + vendorVO.getV_address2());
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

//                    showToast(view.getContext(), "$$");

                    Intent intent = new Intent(getActivity(),
                            VendorDetailActivity.class);
                    intent.putExtra("vendorVO", vendorVO);
                    startActivity(intent);
                }
            });
        }
    }


}
