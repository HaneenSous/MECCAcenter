package com.mecca_center.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mecca_center.app.meccacenter.R;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;

import java.io.File;

/**
 * Created by The_Dev on 2/15/2015.
 */

@EViewGroup
public class UploadImage extends FrameLayout {

    private static final int GET_IMAGE = 2001;
    private OnCancelListener onCancelListener = null;
    private ImageView imageView;

    private ImageView IconView;
    private OnClickListener onClickListener;
    private Activity activity;
    private boolean flag_failed = false;

    public UploadImage(Context context) {
        super(context);
        init(context);


    }

    public UploadImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public UploadImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        activity = (Activity) context;
        imageView = new ImageView(context);

        addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        imageView.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_image).color(Color.WHITE).sizeDp(100));

        LayoutParams params = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;


        params.gravity = Gravity.TOP | Gravity.RIGHT;
        IconView = new ImageView(context);
        IconView.setBackgroundColor(Color.BLACK);
        addView(IconView, params);
        IconView.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_close).color(getResources().getColor(R.color.accent)).sizeDp(30));
        IconView.setVisibility(GONE);
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image//*");
                activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_IMAGE);



            }
        };
        IconView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                   imageView.setImageDrawable(new IconDrawable(activity, Iconify.IconValue.fa_image).color(Color.WHITE).sizeDp(75));
                    }
                });
                IconView.setVisibility(GONE);
                onCancelListener.onCancel();
            }
        });

        setOnClickListener(onClickListener);
    }

    public void CancelButtonVisibility(int visibility){
        IconView.setVisibility(visibility);
    }

    public void setImageSource(File file) {
        Picasso.with(activity).load(file).into(imageView);
        IconView.setVisibility(VISIBLE);

    }


    public void setOnCancelListener(OnCancelListener listener) {
        onCancelListener = listener;
    }

    public interface OnCancelListener {
        void onCancel();
    }

}
