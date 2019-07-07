package Functionality_Class;

import android.app.Activity;
import android.content.Context;

import com.StudentFaclity.Stulity.R;


/**
 * Created by Abhay dhiman
 */

//Class for Animation
public class Animationall {

    //For forward animation
    public void Animallforward(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_exit);;
    }

    //For backward animation
    public void Animallbackward(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_exit);
    }


    //Slide Up animation
    public void Animallslide_up(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.slide_in_top, R.anim.no_animation2);
    }

    //Slide down animation
    public void Animallslide_down(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.no_animation, R.anim.slide_in_bottom);
    }

}
