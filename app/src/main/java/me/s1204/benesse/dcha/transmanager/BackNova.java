package me.s1204.benesse.dcha.transmanager;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.BenesseExtension;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import jp.co.benesse.dcha.dchaservice.IDchaService;
import jp.co.benesse.dcha.dchautilservice.IDchaUtilService;

import static me.s1204.benesse.dcha.transmanager.InitDcha.*;

public class BackNova extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitDcha.checkPermission(this)) return;

        try {
            BenesseExtension.putInt(BC_COMPATSCREEN, 0);
        } catch (Exception ignored) {
            if (!bindService(new Intent(UTIL_SRV).setPackage(UTIL_PKG), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    mUtilService = IDchaUtilService.Stub.asInterface(iBinder);
                    try {
                        mUtilService.setForcedDisplaySize(1280, 800);
                    } catch (RemoteException ignored) {
                    }
                    unbindService(this);
                }
                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    unbindService(this);
                }
            }, Context.BIND_AUTO_CREATE)) {
                makeText(this, R.string.fail_util_connect);
                finish();
                return;
            }
        }

        if (!bindService(new Intent(DCHA_SRV).setPackage(DCHA_PKG), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mDchaService = IDchaService.Stub.asInterface(iBinder);
                try {
                    mDchaService.setSetupStatus(0);
                    mDchaService.hideNavigationBar(false);
                    mDchaService.clearDefaultPreferredApp(TOUCH_HOME_SHO_PKG);
                    mDchaService.setDefaultPreferredHomeApp(NOVA_PKG);
                    mDchaService.removeTask(TOUCH_HOME_SHO_PKG);
                } catch (RemoteException ignored) {
                }
                unbindService(this);
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                unbindService(this);
            }
        }, Context.BIND_AUTO_CREATE)) {
            makeText(this, R.string.fail_dcha_connect);
            finish();
            return;
        }

        try {
            startActivity(new Intent(Intent.ACTION_MAIN).setClassName(NOVA_PKG, NOVA_HOME).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            startActivity(new Intent(Intent.ACTION_MAIN).setClassName(NOVA_PKG, NOVA_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            startActivity(new Intent(Intent.ACTION_MAIN).setClassName(NOVA_PKG, NOVA_HOME).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            makeText(this, R.string.start_nova);
        } catch (ActivityNotFoundException ignored) {
            makeText(this, R.string.fail_nova);
        }
        finish();
    }

}
