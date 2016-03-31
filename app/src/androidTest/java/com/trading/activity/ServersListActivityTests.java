package com.trading.activity;

import android.app.Instrumentation;
import android.content.SyncRequest;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.text.method.Touch;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.trading.R;
import com.trading.activity.ServersListActivity;
import com.trading.utils.ServerDialog;





/**
 * Created by Сергей on 12.03.2016.
 */
public class ServersListActivityTests extends ActivityInstrumentationTestCase2<ServersListActivity> {
  //  ServersListActivity act;
 //   Instrumentation.ActivityMonitor am;

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
       // getInstrumentation().removeMonitor(am);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
     //   ServersListActivity act = (ServersListActivity) getActivity();


    }

    private Solo solo;
    public ServersListActivityTests() {
        super(ServersListActivity.class);
    }


    public void testActivityExists() {
        ServersListActivity act = (ServersListActivity) getActivity();
        assertNotNull(act);
    }
    public void testSelectedItemExist()
    {
        ServersListActivity act = (ServersListActivity) getActivity();
        assertFalse("радио буттон Не выбирается по-умолчанию", ((RadioGroup) act.findViewById(R.id.rg_servers)).getCheckedRadioButtonId() == -1);
    }

    public void testAddServer()  {
        final ServersListActivity  act = (ServersListActivity) getActivity();
        Instrumentation.ActivityMonitor am= getInstrumentation().addMonitor(ServersListActivity.class.getName(), null /* result */, true /* block */);
         getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        boolean ok = getInstrumentation().invokeMenuActionSync(act, R.id.new_server, 0 /* flags */);
        getInstrumentation().waitForIdleSync();

        assertTrue("проверьте создание диалога нового сервера", ok);
        Button mi=(Button)act.getDialog().findViewById(R.id.btn_ok);
        assertNotNull("Диалог не создался", mi);

        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) act.getDialog().findViewById(R.id.e_servname)).setText("dddd");
                ((EditText) act.getDialog().findViewById(R.id.e_servurl)).setText("dddd_url");

            }
        });
        getInstrumentation().waitForIdleSync();
        TouchUtils.clickView(this, mi);
        assertEquals("Сервер не добавился", PreferenceManager.getDefaultSharedPreferences(act).getString("serv_dddd", "Сервер ddd не добавился").toString(), "dddd_url");
        getInstrumentation().removeMonitor(am);
    }

    public void testEditServer2() throws Exception {
       final  ServersListActivity  act = (ServersListActivity) getActivity();
        Instrumentation.ActivityMonitor am= getInstrumentation().addMonitor(ServersListActivity.class.getName(), null /* result */, true /* block */);

        final boolean[] f={false};
       ////////////////выбираем нужный сервер
        final RadioGroup rg=(RadioGroup)act.findViewById(R.id.rg_servers);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    if (((RadioButton) rg.getChildAt(i)).getText().equals("dddd")) {
                        ((RadioButton) rg.getChildAt(i)).setChecked(true);
                        //  ((RadioButton)rg.getChildAt(i)).performClick();
                        //   getInstrumentation().waitForIdleSync();
                        f[0] = true;
                        break;
                    }
                }
            }
        });

        if (!f[0])
        {
            testAddServer();
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < rg.getChildCount(); i++) {
                        if (((RadioButton) rg.getChildAt(i)).getText().equals("dddd")) {
                            ((RadioButton) rg.getChildAt(i)).setChecked(true);
                            //  ((RadioButton)rg.getChildAt(i)).performClick();
                            //   getInstrumentation().waitForIdleSync();
                            f[0] = true;
                            break;
                        }
                    }
                }
            });
        }

        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        boolean ok = getInstrumentation().invokeMenuActionSync(act, R.id.edit_server, 0 /* flags */);
        getInstrumentation().waitForIdleSync();
        assertTrue("проверьте создание диалога нового сервера", ok);
        Button mi=(Button)act.getDialog().findViewById(R.id.btn_ok);
        assertNotNull("Диалог не создался", mi);

        final EditText et=(EditText)act.getDialog().findViewById(R.id.e_servurl);
        assertNotNull("Диалог не создался. net Edittext", et);


        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et.setText("xxx");

                //  getInstrumentation().waitForIdle(this);
            }
        });
        getInstrumentation().waitForIdleSync();
        TouchUtils.clickView(this, mi);
        assertEquals("Сервер не обновился", PreferenceManager.getDefaultSharedPreferences(act).getString("serv_dddd", "Сервер ddd не добавился").toString(), "xxx");
        getInstrumentation().removeMonitor(am);
        testDelServer();
    }

    public void testDelServer() throws Exception {
        ServersListActivity  act = (ServersListActivity) getActivity();
        Instrumentation.ActivityMonitor am= getInstrumentation().addMonitor(ServersListActivity.class.getName(), null /* result */, true /* block */);


        final boolean[] f={false};
        ////////////////выбираем нужный сервер
        final RadioGroup rg=(RadioGroup)act.findViewById(R.id.rg_servers);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    if (((RadioButton) rg.getChildAt(i)).getText().equals("dddd")) {
                        ((RadioButton) rg.getChildAt(i)).setChecked(true);
                        //  ((RadioButton)rg.getChildAt(i)).performClick();
                        //   getInstrumentation().waitForIdleSync();
                        f[0]=true;
                        break;
                    }
                }
            }
        });
        if (!f[0])
        {
            testAddServer();
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < rg.getChildCount(); i++) {
                        if (((RadioButton) rg.getChildAt(i)).getText().equals("dddd")) {
                            ((RadioButton) rg.getChildAt(i)).setChecked(true);
                                                //  ((RadioButton)rg.getChildAt(i)).performClick();
                                                //   getInstrumentation().waitForIdleSync();
                            f[0] = true;
                            break;
                        }
                    }
                }
            });
        }
        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        boolean ok = getInstrumentation().invokeMenuActionSync(act, R.id.del_server, 0 /* flags */);
        getInstrumentation().waitForIdleSync();

        assertEquals("Сервер не udalilsa", PreferenceManager.getDefaultSharedPreferences(act).getString("serv_dddd", "Сервер ddd не добавился").toString(), "Сервер ddd не добавился");
        getInstrumentation().removeMonitor(am);
    }

public void testViewUpdateAddress() throws Exception
    {

    }
}
