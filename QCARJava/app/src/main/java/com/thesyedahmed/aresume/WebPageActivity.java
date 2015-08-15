package com.thesyedahmed.aresume;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;

/**
 * Created by steakpizza on 8/14/2015.
 */
public class WebPageActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, Uri webPageUri){
        Intent i = new Intent(context, WebPageActivity.class);
        i.setData(webPageUri);
        return i;
    }

    @Override
    protected Fragment createFragment(){
        return WebPageFragment.newInstance(getIntent().getData());
    }

    @Override
    public void onBackPressed(){
        if(WebPageFragment.getWebView().canGoBack()){
            WebPageFragment.getWebView().goBack();
        }else{
            super.onBackPressed();
        }
    }

}
