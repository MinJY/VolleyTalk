package com.teamnexters.volleytalk.team;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamnexters.volleytalk.R;
import com.teamnexters.volleytalk.WriteActivity;
import com.teamnexters.volleytalk.album.AlbumFragment;
import com.teamnexters.volleytalk.cheering.CheeringFragment;
import com.teamnexters.volleytalk.player.Player;
import com.teamnexters.volleytalk.post.PostFragment;
import com.teamnexters.volleytalk.team.fragment.TeamDetailAllFragment;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by MIN on 2017. 8. 11..
 */

public class DetailTeamActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Toolbar
     **/
    private Toolbar toolbar_detail_team;

    /**
     * Resources
     **/
    private ImageView iv_back_detail_team;
    private ImageView iv_follow_detail_team;
    private ImageView iv_heart_detail_team;
    private TextView tv_name_detail_team;
    private TextView tv_back_detail_team;
    private TextView tv_physical_detail_team;
    private TextView tv_num_heart_detail_team;
    private TextView tv_num_feed_detail_team;

    /**
     * TabLayout, ViewPager
     **/
    private TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    /**
     * Floating
     **/
    private FloatingActionButton fab_team;

    private Context context;
    private Player who;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailteam);

//        Intent intent = getIntent();
//        who = (Player) intent.getSerializableExtra("who");
//
//        context = getApplicationContext();

        initResources();


        changeTabsFont();

    }

    private void initResources() {
        initToolbar();
        initTopLayout();
        initViewPager();
        initTabLayout();
    }

    private void initToolbar() {
        toolbar_detail_team = (Toolbar) findViewById(R.id.toolbar_detail_team);
    }

    private void initTopLayout() {
        /** 뒤로가기 버튼 **/
        iv_back_detail_team = (ImageView) toolbar_detail_team.findViewById(R.id.iv_back_detail_team);
        iv_back_detail_team.setOnClickListener(this);

        iv_follow_detail_team = (ImageView) findViewById(R.id.iv_follow_detail_team);
        iv_heart_detail_team = (ImageView) findViewById(R.id.iv_heart_detail_team);

        tv_name_detail_team = (TextView) findViewById(R.id.tv_name_detail_team);
        tv_back_detail_team = (TextView) findViewById(R.id.tv_back_detail_team);
        tv_physical_detail_team = (TextView) findViewById(R.id.tv_physical_detail_team);
        tv_num_heart_detail_team = (TextView) findViewById(R.id.tv_num_heart_detail_team);
        tv_num_feed_detail_team = (TextView) findViewById(R.id.tv_num_feed_detail_team);

        /** Floating 버튼 **/
        fab_team = (FloatingActionButton) findViewById(R.id.fab_detail_team);
        fab_team.setOnClickListener(this);

//        tv_name_detail_team.setText(who.getName());
//        tv_back_detail_team.setText("No." + who.getBacknumber());
//        tv_physical_detail_team.setText(who.getPhysical());
//        tv_num_heart_detail_team.setText(who.getLikecount());
//        tv_num_feed_detail_team.setText(who.getPostcount());
//
//        if (!who.getFollow().isEmpty()) {
//            Glide.with(this)
//                    .load(R.mipmap.ico_raw_star_on)
//                    .into(iv_follow_detail_team);
//        }

    }

    private void initViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container_detail_team);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tabs_detail_team);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.iv_back_detail_team:
                finish();
                break;
            case R.id.iv_follow_detail_team:
                //follow 하기, 취소하기.
                break;

            case R.id.iv_heart_detail_team:
                //like 하기, 취소하기
                break;

            case R.id.fab_detail_team:
                intent = new Intent(getApplicationContext(), WriteActivity.class);
                intent.putExtra("type", "player");
                intent.putExtra("seq", who.getSeq());
                startActivity(intent);
                break;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public Fragment fragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            fragment = null;

            switch (position) {
                case 0:
//                    return PostFragment.newInstance("player", String.valueOf(who.getSeq()));
                    return new TeamDetailAllFragment();
                case 1:
                    return AlbumFragment.newInstance("player");
                case 2:
                case 3:
                case 4:
                    return AlbumFragment.newInstance("player");
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "전체글";
                case 1:
                    return "앨범";
                case 2:
                    return "뉴스";
                case 3:
                    return "일정 및 결과";
                case 4:
                    return "선수";
            }
            return null;
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                case 1:
                    fab_team.show();
                    break;
                case 2:
                case 3:
                case 4:
                    fab_team.hide();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "NotoSans-Regular.ttf"));
                }
            }
        }
    }


}
