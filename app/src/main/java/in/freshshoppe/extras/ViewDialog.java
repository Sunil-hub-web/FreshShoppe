package in.freshshoppe.extras;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.freshshoppe.R;

import androidx.core.content.ContextCompat;


public class ViewDialog {

    Activity activity;
    Dialog dialog;
    //..we need the context else we can not create the dialog so get context in constructor
    public ViewDialog(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("ResourceAsColor")
    public void showDialog() {

        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the viewquiz i told you will inflate later
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.progress_dialog);

        //...initialize the imageView form infalted viewquiz
        ImageView gifImageView = dialog.findViewById(R.id.loadingimage);

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */

            Glide.with(activity).load(R.drawable.loader_m).into(gifImageView);
            gifImageView.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

        //...finaly show it
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog(){
        dialog.dismiss();
    }

}
