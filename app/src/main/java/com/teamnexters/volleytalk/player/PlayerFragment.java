package com.teamnexters.volleytalk.player;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teamnexters.volleytalk.R;
import com.teamnexters.volleytalk.ResForm;
import com.teamnexters.volleytalk.config.Config;
import com.teamnexters.volleytalk.player.adapter.PlayerAdapter;
import com.teamnexters.volleytalk.tool.NetworkModel;
import com.teamnexters.volleytalk.ui_element.NonScrollListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MIN on 2017. 7. 13..
 */

public class PlayerFragment extends Fragment {

    public TextView foldOnOff;
    public LinearLayout wall_player;
    public List<PlayerAdapter> adapterList_team_player;
    public List<PlayerList> allPlayerList;
    public RelativeLayout lv_footer;
    public List<Integer> footerList;
    public SparseArray<TextView> foldOnOFFList;
    public String[] teamNameList;
    public int[] teamColorList;

    public int start;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView_player = inflater.inflate(R.layout.fragment_player, container, false);
        wall_player = (LinearLayout) rootView_player.findViewById(R.id.wall_player);

        footerList = new ArrayList<>();
        adapterList_team_player = new ArrayList<>();

        RadioButton rb_male_player = (RadioButton) rootView_player.findViewById(R.id.rb_male_player);
        RadioButton rb_female_player = (RadioButton) rootView_player.findViewById(R.id.rb_female_player);

        rb_male_player.setOnClickListener(clickSegmentedControl);
        rb_female_player.setOnClickListener(clickSegmentedControl);

        rb_male_player.setChecked(true);
        getplayerList("M");

        start = Config.MALE_TEAM_START_SEQ;
        teamNameList = getResources().getStringArray(R.array.team_male_list);
        teamColorList = getResources().getIntArray(R.array.team_male_color_list);
        foldOnOFFList = new SparseArray<TextView>();

        return rootView_player;
    }

    public void createListViewInTeam() {
        for( int i = 0 ; i < allPlayerList.size(); i++) {
            wall_player.addView(createPlayerList(allPlayerList.get(i), getContext()));
        }
    }


    //여기 망함. 정신차리고나서 고치기.
    public View createPlayerList(PlayerList playerList, Context context) {

        LinearLayout ll_playerList = new LinearLayout(context);
        ll_playerList.setOrientation(LinearLayout.VERTICAL);
        ll_playerList.setBackground(getResources().getDrawable(R.drawable.lv_background));
        LinearLayout.LayoutParams layoutParams_list = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        int margin_default = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        layoutParams_list.setMargins(margin_default, margin_default * 2, margin_default, margin_default);
        ll_playerList.setLayoutParams(layoutParams_list);

        //리스트뷰 헤더 추가
        int height_header = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams layoutParams_header = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height_header);
        RelativeLayout lv_header = new RelativeLayout(context);
        lv_header.setLayoutParams(layoutParams_header);
        GradientDrawable header = (GradientDrawable) getResources().getDrawable(R.drawable.lv_header);
        //팀대표색으로 색상 변경
        header.setColor(teamColorList[Integer.valueOf(playerList.getTeam())-start]);
        lv_header.setBackground(header);

        //헤더에 텍스트뷰 추가
        TextView teamname = new TextView(context);
        teamname.setTextColor(Color.WHITE);
        teamname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        teamname.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSans-Regular.ttf"));
        teamname.setText(teamNameList[Integer.valueOf(playerList.getTeam())-start].replace("\n", " "));
        teamname.setMaxLines(1);
        lv_header.addView(teamname);
        RelativeLayout.LayoutParams layoutParams_teamname = (RelativeLayout.LayoutParams)teamname.getLayoutParams();
        layoutParams_teamname.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        //리스트뷰
        NonScrollListView lv_player = new NonScrollListView(context);
        lv_player.setBackgroundColor(Color.TRANSPARENT);
        lv_player.setDivider(getResources().getDrawable(R.drawable.lv_days_match_divider));
        lv_player.setDrawSelectorOnTop(true);

        PlayerAdapter adapter_team_player = new PlayerAdapter(playerList.getPlayer().subList(0, Config.FOLDED_LIST_SIZE));
        adapterList_team_player.add(adapter_team_player);
        lv_player.setAdapter(adapter_team_player);

        View divider = new View(context);
        divider.setBackgroundColor(Color.parseColor("#eeeeee"));
        divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics())));

        ll_playerList.addView(lv_header);
        ll_playerList.addView(lv_player);
        ll_playerList.addView(divider);

        //리스트뷰 푸터 추가
        if (playerList.getPlayer().size() > Config.FOLDED_LIST_SIZE) {
            lv_footer = new RelativeLayout(context);
            lv_footer.setBackgroundColor(Color.TRANSPARENT);
            lv_footer.setLayoutParams(layoutParams_header);
            lv_footer.setOnClickListener(clickFoldListener);

            //푸터에 텍스트뷰 추가
            foldOnOff = new TextView(context);
            foldOnOff.setTextColor(Color.parseColor("#9b9b9b"));
            foldOnOff.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            foldOnOff.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSans-Regular.ttf"));
            foldOnOff.setText("펼치기");

            lv_footer.addView(foldOnOff);
            RelativeLayout.LayoutParams layoutParams_btn_fold = (RelativeLayout.LayoutParams) foldOnOff.getLayoutParams();
            layoutParams_btn_fold.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            foldOnOff.setLayoutParams(layoutParams_btn_fold);

            ll_playerList.addView(lv_footer);
            lv_footer.setId(Integer.valueOf(playerList.getTeam()));
            footerList.add(lv_footer.getId());
            foldOnOFFList.append(lv_footer.getId(), foldOnOff);
        }

        ll_playerList.setClipToOutline(true);
        ll_playerList.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        ll_playerList.setElevation(4 * context.getResources().getDisplayMetrics().density);

        return ll_playerList;
    }


    View.OnClickListener clickFoldListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = footerList.indexOf(view.getId());
            if (adapterList_team_player.get(pos).getCount() == Config.FOLDED_LIST_SIZE) {
                foldOnOFFList.get(view.getId()).setText("접기");
                adapterList_team_player.get(pos).setPlayerList(allPlayerList.get(pos).getPlayer());
                adapterList_team_player.get(pos).notifyDataSetChanged();
            } else {
                foldOnOFFList.get(view.getId()).setText("펼치기");
                adapterList_team_player.get(pos).setPlayerList(allPlayerList.get(pos).getPlayer().subList(0, Config.FOLDED_LIST_SIZE));
                adapterList_team_player.get(pos).notifyDataSetChanged();
            }
        }
    };

    View.OnClickListener clickSegmentedControl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            wall_player.removeAllViews();
            footerList.clear();
            adapterList_team_player.clear();

            switch (view.getId()) {
                case R.id.rb_male_player:
                    start = Config.MALE_TEAM_START_SEQ;
                    teamNameList = getResources().getStringArray(R.array.team_male_list);
                    teamColorList = getResources().getIntArray(R.array.team_male_color_list);
                    getplayerList("M");
                    break;
                case R.id.rb_female_player:
                    start  = Config.FEMALE_TEAM_START_SEQ;
                    teamNameList = getResources().getStringArray(R.array.team_female_list);
                    teamColorList = getResources().getIntArray(R.array.team_female_color_list);
                    getplayerList("F");
                    break;
            }
        }
    };



    public void getplayerList(String sex) {

        NetworkModel networkModel = NetworkModel.retrofit.create(NetworkModel.class);
        Call<ResForm<List<PlayerList>>> call = networkModel.getPlayerList(sex);
        call.enqueue(new Callback<ResForm<List<PlayerList>>>() {
            @Override
            public void onResponse(Call<ResForm<List<PlayerList>>> call, Response<ResForm<List<PlayerList>>> response) {
                ResForm<List<PlayerList>> list = response.body();
                if(list.getStatus().equals("true")) {
                    allPlayerList = list.getResData();
                    createListViewInTeam();
                } else {
                    Toast.makeText(getContext(), list.getErrMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResForm<List<PlayerList>>> call, Throwable t) {

            }
        });
    }

}
