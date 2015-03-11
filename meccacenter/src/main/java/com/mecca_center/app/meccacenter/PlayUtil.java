package com.mecca_center.app.meccacenter;

import android.app.Activity;
import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mecca_center.app.utils.ServiceUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;

/**
 * Created by The_Dev on 2/12/2015.
 */

@EBean
public class PlayUtil {

    @RootContext
    Context context;

    @RootContext
     Activity activity;
@Bean
ServiceUtils serviceUtils;

    GoogleCloudMessaging GCM;

    String GCM_Register_Id;


    PlayUtilCallBacks callBacks;

    public void setCallBacks(PlayUtilCallBacks callBacks) {
        this.callBacks = callBacks;
    }

    public     boolean CheckPlayService() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (result != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
                GooglePlayServicesUtil.getErrorDialog(result, activity, 0).show();
            } else {
                new MaterialDialog.Builder(activity).title("MECCA Center")
                        .content("This device is not supported.")
                        .positiveText("OK")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {

                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .show();
            }
            return false;
        }
        return true;
    }

    @Background
  public  void RegisterDevice() {
        GCM = GoogleCloudMessaging.getInstance(context);

        try {
            GCM_Register_Id = GCM.register(context.getResources().getString(R.string.SenderId));

        } catch (IOException e) {
            GCM_Register_Id = "";
        }

        getRegisterId(GCM_Register_Id);
    }

    @UiThread
  public  void getRegisterId(String RegisteredId){
        callBacks.getRegisterId(RegisteredId);
    }


    interface PlayUtilCallBacks {
        void getRegisterId(String RegisteredId);
    }
}
