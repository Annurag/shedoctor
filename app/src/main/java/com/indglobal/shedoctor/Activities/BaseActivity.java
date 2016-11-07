package com.indglobal.shedoctor.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.Fragments.CalendarApointFragment;
import com.indglobal.shedoctor.Fragments.NotificationFragment;
import com.indglobal.shedoctor.Fragments.PastApointFragment;
import com.indglobal.shedoctor.Fragments.UpcomingApointFragment;
import com.indglobal.shedoctor.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Android on 8/29/16.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener,RippleView.OnRippleCompleteListener,FragmentManager.OnBackStackChangedListener {

    Toolbar mtoolbar;
    ProgressBar prgLoading;

    RippleView rlTopNotification,docsignature,addreceptionist,healthtips,ledger,payment,bankdetails,feedback,settings,rpllogout;
    private NotificationFragment m_topFragment;
    private FragmentManager manager;
    int i = 0;

    Context context = BaseActivity.this;
    RippleView ivNavCross,navConsulTime;

    //Navigation Drawer
    Boolean isDrawerOpened = false;
    DrawerLayout drawer_layout;
    NavigationView navigation_view;
    RelativeLayout rlprofile;
    ImageView navPrflImg;
    TextView tvNavDocName,tvNavDocId,tvNavOnline;
    ToggleButton tbNavOnline;
    String DoctrPref,DoctrName,DoctrId,DoctrImg,DoctrOnline,DoctrReg;
    String tbCheck = "0";
    LinearLayout Navlltoggle;
    ProgressBar NavprgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.base_main);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.nav_icon);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setLogo(R.drawable.splash_logo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigation_view = (NavigationView)findViewById(R.id.navigation_view);
        rlprofile = (RelativeLayout)findViewById(R.id.rlprofile);
        navPrflImg = (ImageView)findViewById(R.id.navPrflImg);
        tbNavOnline = (ToggleButton)findViewById(R.id.tbNavOnline);
        tvNavOnline = (TextView)findViewById(R.id.tvNavOnline);
        ivNavCross = (RippleView)findViewById(R.id.ivNavCross);
        tvNavDocName = (TextView)findViewById(R.id.tvNavDocName);
        tvNavDocId = (TextView)findViewById(R.id.tvNavDocId);
        Navlltoggle = (LinearLayout)findViewById(R.id.Navlltoggle);
        NavprgLoading = (ProgressBar)findViewById(R.id.NavprgLoading);

        tbNavOnline.setEnabled(false);
        tbNavOnline.setFocusable(true);

        DoctrPref = Comman.getPreferences(BaseActivity.this, "PREF");
        DoctrName = Comman.getPreferences(BaseActivity.this, "NAME");
        DoctrId = Comman.getPreferences(BaseActivity.this, "SDOCID");
        DoctrImg = Comman.getPreferences(BaseActivity.this, "PRFLIMG");
        DoctrOnline = Comman.getPreferences(BaseActivity.this, "ONLINE");
        DoctrReg = Comman.getPreferences(BaseActivity.this, "REGNO");

        tvNavDocName.setText(DoctrPref+" "+DoctrName);
        tvNavDocId.setText(DoctrId);

        if (DoctrOnline.equalsIgnoreCase("1")){
            tbCheck = "1";
            tbNavOnline.setChecked(true);
            tvNavOnline.setText("You are Online");

        }else {
            tbCheck = "0";
            tbNavOnline.setChecked(false);
            tvNavOnline.setText("You are Offline");
        }

        if (!DoctrImg.equalsIgnoreCase("")&&!DoctrImg.equalsIgnoreCase("null")){

            Picasso.with(BaseActivity.this).load(DoctrImg).resize(100, 100).error(R.drawable.doc)
                    .placeholder(R.drawable.doc).into(navPrflImg);

        }

        rlTopNotification = (RippleView)findViewById(R.id.rlTopNotification);
        docsignature = (RippleView)findViewById(R.id.docsignature);
        addreceptionist = (RippleView)findViewById(R.id.addreceptionist);
        healthtips = (RippleView)findViewById(R.id.healthtips);

        ledger = (RippleView)findViewById(R.id.ledger);
        payment = (RippleView)findViewById(R.id.payment);
        bankdetails = (RippleView)findViewById(R.id.bankdetails);
        feedback = (RippleView)findViewById(R.id.feedback);
        settings = (RippleView)findViewById(R.id.settings);
        rpllogout = (RippleView)findViewById(R.id.rpllogout);

        navConsulTime = (RippleView)findViewById(R.id.navConsulTime);

        manager = getSupportFragmentManager();
        manager.addOnBackStackChangedListener(this);

        rlTopNotification.setOnRippleCompleteListener(this);
        ivNavCross.setOnRippleCompleteListener(this);
        navConsulTime.setOnRippleCompleteListener(this);
        docsignature.setOnRippleCompleteListener(this);
        addreceptionist.setOnRippleCompleteListener(this);
        healthtips.setOnRippleCompleteListener(this);

        ledger.setOnRippleCompleteListener(this);
        payment.setOnRippleCompleteListener(this);
        bankdetails.setOnRippleCompleteListener(this);
        feedback.setOnRippleCompleteListener(this);
        settings.setOnRippleCompleteListener(this);
        rlprofile.setOnClickListener(this);
        rpllogout.setOnRippleCompleteListener(this);

        setupViewPager();

        Navlltoggle.setOnClickListener(this);

    }

    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

        RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.upcmg_tab_title,null);
        tabOne.setSelected(true);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        RelativeLayout tabsecond = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.past_tab_title,null);
        tabLayout.getTabAt(1).setCustomView(tabsecond);

        RelativeLayout tabthree = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.calendar_tab_title,null);
        tabLayout.getTabAt(2).setCustomView(tabthree);

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Intent mIntent = getIntent();
        int pstn = mIntent.getIntExtra("postion", 0);
        adapter.addFrag(new UpcomingApointFragment(), "Upcoming");
        adapter.addFrag(new PastApointFragment(), "Past");
        adapter.addFrag(new CalendarApointFragment(),"Calendar");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pstn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                drawer_layout.openDrawer(GravityCompat.START);
                isDrawerOpened = true;
                break;

            case R.id.action_calendr:
                Intent ii = new Intent(BaseActivity.this,UpcmngCalendarActivity.class);
                startActivity(ii);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rlprofile:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }
                Intent j = new Intent(BaseActivity.this,MyProfileActivity.class);
                startActivity(j);
                break;

            case R.id.Navlltoggle:
                NavprgLoading.setVisibility(View.VISIBLE);
                new DrStatusToggleBtnTask().execute();
                break;
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rlTopNotification:
                if (i == 0) {
                    notification();
                    i=1;
                } else {
                    i=0;
                    closeNotification();
                }

                break;

            case R.id.ivNavCross:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }
                break;

            case R.id.navConsulTime:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }

                Intent ii = new Intent(BaseActivity.this,ConsultTimeFeeActivity.class);
                startActivity(ii);

                break;

            case R.id.docsignature:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }

                Intent i = new Intent(BaseActivity.this,DoctorsignatureActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_in_left, R.anim.stay);

                break;

            case R.id.addreceptionist:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }

                Intent a = new Intent(BaseActivity.this,ReceptionistsActivity.class);
                startActivity(a);
                overridePendingTransition(R.anim.trans_in_left, R.anim.stay);

                break;

            case R.id.healthtips:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }

                Intent b = new Intent(BaseActivity.this,HealthtipsActivity.class);
                startActivity(b);
                overridePendingTransition(R.anim.trans_in_left, R.anim.stay);

                break;

            case R.id.ledger:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }

                Intent c = new Intent(BaseActivity.this,LedgerActivity.class);
                startActivity(c);
                overridePendingTransition(R.anim.trans_in_left, R.anim.stay);

                break;

            case R.id.payment:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }

                Intent d = new Intent(BaseActivity.this,PaymentActivity.class);
                startActivity(d);
                overridePendingTransition(R.anim.trans_in_left, R.anim.stay);

                break;

            case R.id.bankdetails:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }

                Intent e = new Intent(BaseActivity.this,BankdetailsActivity.class);
                startActivity(e);
                overridePendingTransition(R.anim.trans_in_left, R.anim.stay);

                break;

            case R.id.feedback:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }

                overridePendingTransition(R.anim.trans_in_left, R.anim.stay);
                Intent f = new Intent(BaseActivity.this,FeedbackActivity.class);
                startActivity(f);

                break;

            case R.id.settings:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }
                Intent g = new Intent(BaseActivity.this,SettingsActivity.class);
                startActivity(g);

                break;

            case R.id.rpllogout:
                if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawers();
                    isDrawerOpened = false;
                }
                AlertDialog.Builder builder1 = new AlertDialog.Builder(BaseActivity.this);
                builder1.setTitle("Log Out");
                builder1.setMessage("Are you sure you want to LogOut?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Comman.delPrefences(BaseActivity.this);
                                Intent ii = new Intent(BaseActivity.this,AdminActivity.class);
                                Comman.setPreferences(BaseActivity.this, "userLogin", "0");
                                startActivity(ii);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                break;
        }
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

    private void notification() {
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_in_top,
                R.anim.abc_slide_out_top, R.anim.abc_slide_out_top);
        transaction.add(R.id.fragment_top, new NotificationFragment(), "NotiPage");
        transaction.addToBackStack("Notification");
        transaction.commit();
    }

    private void closeNotification() {
        i = 0;
        manager.popBackStack();
    }

    @Override
    public void onBackStackChanged() {
        int count = manager.getBackStackEntryCount();

        for (int i=count-1; i>=0; i--) {
            manager.getBackStackEntryAt(i);
        }
    }

    private class DrStatusToggleBtnTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {

                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                DefaultHttpClient client = new DefaultHttpClient();

                SchemeRegistry registry = new SchemeRegistry();
                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);

                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                String productListUrl = getResources().getString(R.string.statusToggleUrl);
                HttpClient httpclient = new DefaultHttpClient();

                String token = Comman.getPreferences(BaseActivity.this, "TOKEN");
                String status;
                if (tbCheck.equalsIgnoreCase("1")){
                    status = "0";
                }else {
                    status = "1";
                }

                HttpGet httppost = new HttpGet(productListUrl + "?token=" + token + "&status=" + status);

                HttpResponse res = httpclient.execute(httppost);
                HttpEntity resEntity = res.getEntity();
                final String response_str = EntityUtils.toString(resEntity);
                return response_str;
                //------------------>>
            } catch (ParseException | IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.has("success")) {
                        boolean success = jsonObject.getBoolean("success");

                        String message = jsonObject.getString("message");
                        message = message.replace("<br />", System.getProperty("line.separator"));

                        if (success) {

                            if (tbCheck.equalsIgnoreCase("1")){
                                tbNavOnline.setChecked(false);
                                tbCheck = "0";
                                Comman.setPreferences(BaseActivity.this, "ONLINE", "0");
                                tvNavOnline.setText("You are Offline");
                            }else {
                                tbNavOnline.setChecked(true);
                                tbCheck = "1";
                                Comman.setPreferences(BaseActivity.this, "ONLINE", "1");
                                tvNavOnline.setText("You are Online");
                            }

                            NavprgLoading.setVisibility(View.GONE);


                        } else {

                            Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(BaseActivity.this, "Something going wrong please try again later!", Toast.LENGTH_SHORT).show();

            }

            NavprgLoading.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onResume() {
        String sf = Comman.getPreferences(BaseActivity.this, "PicChang");
        if (sf.equalsIgnoreCase("1")){
            String DoctrImg = Comman.getPreferences(BaseActivity.this, "PRFLIMG");

            if (!DoctrImg.equalsIgnoreCase("")&&!DoctrImg.equalsIgnoreCase("null")){

                Picasso.with(BaseActivity.this).load(DoctrImg).resize(100, 100).error(R.drawable.doc)
                        .placeholder(R.drawable.doc).into(navPrflImg);

                Comman.setPreferences(BaseActivity.this, "PicChang","0");
            }
        }


        super.onResume();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(BaseActivity.this);
        builder1.setTitle("Exit");
        builder1.setMessage("Are you sure you want to exit?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent setIntent = new Intent(Intent.ACTION_MAIN);
                        setIntent.addCategory(Intent.CATEGORY_HOME);
                        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(setIntent);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}