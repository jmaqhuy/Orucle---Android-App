package com.example.oruclejava.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;

public class ClearFocus {

    public static void clearFocus(Activity activity, EditText... editTexts) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null && imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        for (EditText editText : editTexts) {
            editText.clearFocus();
        }
    }
}
