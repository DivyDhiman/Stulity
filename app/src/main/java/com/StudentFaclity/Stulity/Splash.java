package com.StudentFaclity.Stulity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import Controllers.Config;
import Controllers.Controller;

/**
 * Created by Abhay dhiman
 */


public class Splash extends Activity implements Animation.AnimationListener {

    private Context context;
    private Controller controller;
    private Intent intent;
    private TextView splash_title,welcome_txt;
    private Animation mAnim = null;
    private String status;
    private boolean checksplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        context = Splash.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        controller = new Controller();
        controller.initSharedPreference(context);

        status = controller.getString(getString(R.string.session_id));

        splash_title = (TextView) findViewById(R.id.splash_title);
        welcome_txt = (TextView) findViewById(R.id.welcome_txt);
        welcome_txt.setVisibility(View.GONE);
        mAnim = AnimationUtils.loadAnimation(this, R.anim.translate);
        mAnim.setAnimationListener(this);
        splash_title.clearAnimation();
        splash_title.setAnimation(mAnim);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (!checksplash) {
            welcome_txt.setVisibility(View.VISIBLE);

            mAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            mAnim.setAnimationListener(this);

            welcome_txt.startAnimation(mAnim);
            checksplash = true;

        } else {
            if (!status.equals(Config.SHARED_PREFRENCE_NO_DATA_KEY_STRING)) {
                intent = new Intent(context, Dash_board.class);
                startActivity(intent);
                finish();
            } else {
                intent = new Intent(context, Login_activity.class);
                startActivity(intent);
                finish();

            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
