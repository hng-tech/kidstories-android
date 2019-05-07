package com.dragonlegend.kidstories.Utils;

import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import java.lang.reflect.Field;

public class BottomNavigationViewHelper {
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView bottomNavigationView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = bottomNavigationView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(bottomNavigationView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < bottomNavigationView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) bottomNavigationView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
}