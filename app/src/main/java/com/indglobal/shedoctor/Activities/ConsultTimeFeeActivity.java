package com.indglobal.shedoctor.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import com.indglobal.shedoctor.Fragments.ConsultFeeFragment;
import com.indglobal.shedoctor.Fragments.ConsultTimeFragment;
import com.indglobal.shedoctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 9/13/16.
 */
public class ConsultTimeFeeActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    Toolbar mtoolbar;

    private FragmentManager manager;
    Context context = ConsultTimeFeeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.consult_time_fee_main);



        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        manager = getSupportFragmentManager();
        manager.addOnBackStackChangedListener(this);

        setupViewPager();

    }

    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerConsult);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabConsult);
        tabLayout.setupWithViewPager(viewPager);

        RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.fee_tab_title,null);
        tabOne.setSelected(true);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        RelativeLayout tabsecond = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.time_tab_title,null);
        tabLayout.getTabAt(1).setCustomView(tabsecond);

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Intent mIntent = getIntent();
        int pstn = mIntent.getIntExtra("postion", 0);
        adapter.addFrag(new ConsultFeeFragment(), "Fee");
        adapter.addFrag(new ConsultTimeFragment(), "Time");

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pstn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public void onBackStackChanged() {
        int count = manager.getBackStackEntryCount();

        for (int i=count-1; i>=0; i--) {
            manager.getBackStackEntryAt(i);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}