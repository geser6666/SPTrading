package com.trading.activity;

import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.trading.activity.MainActivity;
import com.trading.utils.Funcs;

import java.util.Map;

/**
 * Created by Сергей on 19.02.2016.
 */
public class MainActivityTests extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTests() {
        super(MainActivity.class);
    }
    public void testActivityExists() {
        MainActivity activity = getActivity();
        assertNotNull(activity);
    }
    public void testActivityExists2() {
        MainActivity activity = getActivity();
        assertNotNull(activity);
    }
//проверяем
    public  void  testGetServersList()
    {
         MainActivity activity = getActivity();
        Map<String, ?>  allprefs= Funcs.getServersList(activity);
        assertNotNull(activity);
        assertEquals("Нет серверов",true, allprefs.size()>0);
    }


}
