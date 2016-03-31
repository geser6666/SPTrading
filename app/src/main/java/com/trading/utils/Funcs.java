package com.trading.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Funcs {
    public static Map<String, ?> getServersList(Context context)
    {
        Map<String, ?> allprefs=	PreferenceManager.getDefaultSharedPreferences(context).getAll();
        for (int i=allprefs.keySet().size()-1;i>0;i--)
        {
            if (!String.copyValueOf(allprefs.keySet().toArray()[i].toString().toCharArray(),0,5).equals("serv_"))
            {
               allprefs.remove(allprefs.keySet().toArray()[i].toString());
            }
        }
        return allprefs;
    }
}
