//Gaurab Dahal

package com.gaurabdahal.bugsmasher;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

/**
 * Created by gaurabdahal on 7/11/17.
 */
public class PrefsActivity extends PreferenceActivity {


    /*___________________________________________________________________
	|
	| Function: onCreate
	|__________________________________________________________________*/
    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isValidFragment(String fragmentName){
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.HONEYCOMB){
            return true;
        }else if(PrefsFragmentSettings.class.getName().equals(fragmentName)){
            return true;
        }else{
                return false;
        }
    }

    public void onBuildHeaders(List<Header> target){
        getFragmentManager().beginTransaction().replace(android.R.id.content,new PrefsFragmentSettings()).commit();
    }
}
