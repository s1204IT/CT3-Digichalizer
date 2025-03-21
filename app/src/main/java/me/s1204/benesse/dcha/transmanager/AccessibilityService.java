package me.s1204.benesse.dcha.transmanager;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {

    private static boolean isHomeButtonPressed = false;
    private static boolean isVolumeDownPressed = false;
    private static boolean isVolumeUpPressed = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                isHomeButtonPressed = true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                isHomeButtonPressed = false;
            }
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                isVolumeDownPressed = true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                isVolumeDownPressed = false;
            }
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                isVolumeUpPressed = true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                isVolumeUpPressed = false;
            }
        }

        if (isHomeButtonPressed && isVolumeDownPressed) {
            startActivity(new Intent(this, BackNova.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }

        if (isHomeButtonPressed && isVolumeUpPressed) {
            startActivity(new Intent(this, InitDcha.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }

        return super.onKeyEvent(event);
    }
}
