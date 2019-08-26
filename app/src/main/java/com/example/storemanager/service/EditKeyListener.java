package com.example.storemanager.service;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.storemanager.R;

public class EditKeyListener implements View.OnKeyListener {
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        EditText editText = (EditText) view;
        int index = editText.getSelectionStart();
        Editable editable = editText.getText();
        String editableString = editable.toString();
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (editableString.equals("") || index == 0) {
                    return true;
                } else {
                    editable.delete(index - 1, index);
                    return true;
                }
            }
        }
        return false;
    }
}