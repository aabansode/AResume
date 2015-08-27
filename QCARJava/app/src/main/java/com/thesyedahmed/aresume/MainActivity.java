package com.thesyedahmed.aresume;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;

import com.qualcomm.QCARUnityPlayer.DebugLog;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerNativeActivity;

import java.net.URI;


public class MainActivity extends AppCompatActivity {
    protected UnityPlayer mUnityPlayer;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Context mContext = this.getApplicationContext();
        mPrefs = mContext.getSharedPreferences("myAppPrefs", 0);

        if(getFirstRun()){
            setRunned();
            Intent intent = new Intent(this, AppIntroduction.class);
            startActivity(intent);
            getWindow().takeSurface(null);
            getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

            mUnityPlayer = new UnityPlayer(this);
            if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
                getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(mUnityPlayer);
            mUnityPlayer.requestFocus();
        }
        else{
            getWindow().takeSurface(null);
            getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

            mUnityPlayer = new UnityPlayer(this);
            if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
                getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(mUnityPlayer);
            mUnityPlayer.requestFocus();
        }
    }

    public boolean getFirstRun() {
        return mPrefs.getBoolean("firstRun", true);
    }

    public void setRunned() {
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putBoolean("firstRun", false);
        edit.commit();
    }

    public void callPhone(final String number){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Uri call = Uri.parse("tel:" + number);
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, call);
                startActivity(dialIntent);
            }
        });
    }

    public void openWebsite(final String url){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent i = WebPageActivity
                        .newIntent(MainActivity.this, Uri.parse(url));
                startActivity(i);
            }
        });

    }

    public void sendEmail(final String address){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",address, null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
    }

    public void openProjects(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override protected void onDestroy (){
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause(){
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume(){
        super.onResume();
        mUnityPlayer.resume();

    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event){
        return mUnityPlayer.injectEvent(event);
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event){
        return mUnityPlayer.injectEvent(event);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    public boolean onGenericMotionEvent(MotionEvent event){
        return mUnityPlayer.injectEvent(event);
    }

}
