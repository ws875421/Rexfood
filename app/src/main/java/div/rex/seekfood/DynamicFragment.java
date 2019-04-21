package div.rex.seekfood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DynamicFragment extends Fragment {
    private final static String TAG2 = "fragment2";


    public DynamicFragment() {
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
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);


        //
        //

        //



        //

        // 找到view裡面的TextView子元件
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        //設定每個List是否為固定尺寸
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));

        final List<Team> teamList = new ArrayList<>();
        teamList.add(new Team(1, R.drawable.p1, "巴爾的摩金鶯"));
        teamList.add(new Team(2, R.drawable.p2, "芝加哥白襪"));
        teamList.add(new Team(3, R.drawable.p3, "洛杉磯天使"));
        teamList.add(new Team(4, R.drawable.p4, "波士頓紅襪"));
        teamList.add(new Team(5, R.drawable.p5, "克里夫蘭印地安人"));
        teamList.add(new Team(6, R.drawable.p6, "奧克蘭運動家"));
        teamList.add(new Team(7, R.drawable.p7, "紐約洋基"));
        teamList.add(new Team(8, R.drawable.p8, "底特律老虎"));
        teamList.add(new Team(9, R.drawable.p9, "西雅圖水手"));
        teamList.add(new Team(1, R.drawable.p1, "巴爾的摩金鶯"));
        teamList.add(new Team(2, R.drawable.p2, "芝加哥白襪"));
        teamList.add(new Team(3, R.drawable.p3, "洛杉磯天使"));
        teamList.add(new Team(4, R.drawable.p4, "波士頓紅襪"));
        teamList.add(new Team(5, R.drawable.p5, "克里夫蘭印地安人"));
        teamList.add(new Team(6, R.drawable.p6, "奧克蘭運動家"));
        teamList.add(new Team(7, R.drawable.p7, "紐約洋基"));
        teamList.add(new Team(8, R.drawable.p8, "底特律老虎"));
        teamList.add(new Team(9, R.drawable.p9, "西雅圖水手"));
        teamList.add(new Team(10, R.drawable.p10, "坦帕灣光芒"));
        recyclerView.setAdapter(new TeamAdapter(teamList));


        return view;
    }
    private class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
        private List<Team> teamList;

        private TeamAdapter(List<Team> teamList) {
            this.teamList = teamList;
        }

        //建立ViewHolder，藉由ViewHolder做元件綁定
        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivLogo;
            private TextView tvName;

            private ViewHolder(View view) {
                super(view);
                ivLogo = view.findViewById(R.id.ivLogo);
                tvName = view.findViewById(R.id.tvName);
            }
        }

        @Override
        public int getItemCount() {
            return teamList.size();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_team, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //將資料注入到View裡
            final Team team = teamList.get(position);
            holder.ivLogo.setImageResource(team.getLogo());
            holder.tvName.setText(team.getName());
            // itemView為ViewHolder內建屬性(指的就是每一列)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), team.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
