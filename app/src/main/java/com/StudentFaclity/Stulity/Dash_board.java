package com.StudentFaclity.Stulity;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter_all.ViewPagerAdapter_home;
import Call_Back.DialogCallBack;
import Controllers.Config;
import Controllers.Controller;
import Fragement_all.MyPostedAd;
import Fragement_all.TransitionHistory;
import Fragement_all.Home;
import Fragement_all.Profile;

public class Dash_board extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Controller controller;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter_home view_pager_home;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton sellFab,postAddFab,parentFab;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Boolean isFabOpen = false;
    private View transparentView;
    private ImageView menu_btn,appIcon,searchDashboard,closeSearch;
    private LinearLayout parentSearch;
    private EditText searchEt;
    private Handler handler;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_board);

        context = Dash_board.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        controller = new Controller();
        set_action_bar();

        initialise();
    }

    private void initialise() {
        sellFab = (FloatingActionButton) findViewById(R.id.sell_fab);
        postAddFab = (FloatingActionButton) findViewById(R.id.post_add_fab);
        parentFab = (FloatingActionButton) findViewById(R.id.parent_fab);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);

        transparentView = (View) findViewById(R.id.transparent_view);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        parentFab.setOnClickListener(this);
        postAddFab.setOnClickListener(this);
        sellFab.setOnClickListener(this);
        transparentView.setOnClickListener(this);

        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressWarnings("ConstantConditions")
            @Override
            public void onPageSelected(int position) {
                switch (position) {

                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home);
                        tabLayout.getTabAt(1).setIcon(R.drawable.posted_add_unselect);
                        break;

                    case 1:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home_unselected);
                        tabLayout.getTabAt(1).setIcon(R.drawable.posted_add);
                        break;

                    case 2:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home_unselected);
                        tabLayout.getTabAt(1).setIcon(R.drawable.posted_add_unselect);
                        break;


                    default:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home);
                        tabLayout.getTabAt(1).setIcon(R.drawable.posted_add_unselect);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.home);
        tabLayout.getTabAt(1).setIcon(R.drawable.posted_add_unselect);
    }

    private void setupViewPager(ViewPager viewPager) {
        view_pager_home = new ViewPagerAdapter_home(getSupportFragmentManager());
        view_pager_home.addFrag(new Home(), getString(R.string.home));
        view_pager_home.addFrag(new MyPostedAd(), getString(R.string.my_posted_ad));

        viewPager.setAdapter(view_pager_home);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            finish();
            controller.Animation_backward(context);
        }
    }

    private void set_action_bar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menu_btn = (ImageView) toolbar.findViewById(R.id.menu_btn);
        appIcon = (ImageView) toolbar.findViewById(R.id.app_icon);
        searchDashboard = (ImageView) toolbar.findViewById(R.id.search_dashboard);
        parentSearch = (LinearLayout) toolbar.findViewById(R.id.parent_search);
        searchEt = (EditText) toolbar.findViewById(R.id.search_et);
        closeSearch = (ImageView) toolbar.findViewById(R.id.close_search);

        menu_btn.setOnClickListener(this);
        searchDashboard.setOnClickListener(this);
        closeSearch.setOnClickListener(this);

        replace_fragment();
    }

    private void searchVisible() {
        menu_btn.setVisibility(View.GONE);
        appIcon.setVisibility(View.GONE);
        searchDashboard.setVisibility(View.GONE);
        parentSearch.setVisibility(View.VISIBLE);
    }

    private void searchInvisible() {
        menu_btn.setVisibility(View.VISIBLE);
        appIcon.setVisibility(View.VISIBLE);
        searchDashboard.setVisibility(View.VISIBLE);
        parentSearch.setVisibility(View.GONE);
    }

    private void replace_fragment() {
        Profile profile = new Profile();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.replace_fragment, profile);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_btn:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;

            case R.id.parent_fab:
                animateFAB();
                break;

            case R.id.post_add_fab:
                if (controller.InternetCheck(context)) {
                    intent = new Intent(context, CentralizedWebView.class);
                    intent.putExtra("url", Config.POST_AD);
                    startActivity(intent);
                } else {
                    controller.Toast_show(context, getString(R.string.enable_internet));
                }
                break;

            case R.id.sell_fab:
                break;

            case R.id.transparent_view:
                animateFAB();
                break;

            case R.id.search_dashboard:
                intent = new Intent(context,SearchData.class);
                intent.putExtra("class_type",getString(R.string.search));
                startActivity(intent);
                break;

            case R.id.close_search:
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Hide_keyboard();
                    }
                }, 100);
                searchInvisible();
                break;
        }
    }


    public void animateFAB(){

        if(isFabOpen){
            parentFab.startAnimation(rotate_backward);
            postAddFab.startAnimation(fab_close);
            sellFab.setVisibility(View.GONE);
            postAddFab.setVisibility(View.GONE);
            sellFab.startAnimation(fab_close);
            postAddFab.setClickable(false);
            sellFab.setClickable(false);
            isFabOpen = false;
            transparentView.setVisibility(View.GONE);
        } else {
            parentFab.startAnimation(rotate_forward);
            postAddFab.startAnimation(fab_open);

            sellFab.setVisibility(View.GONE);
            postAddFab.setVisibility(View.VISIBLE);
            sellFab.startAnimation(fab_open);
            postAddFab.setClickable(true);
            sellFab.setClickable(true);
            isFabOpen = true;
            transparentView.setVisibility(View.VISIBLE);
        }
    }


    public void Hide_keyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
    }

    public void Show_key_board() {
        InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(0, 0);
    }

}
