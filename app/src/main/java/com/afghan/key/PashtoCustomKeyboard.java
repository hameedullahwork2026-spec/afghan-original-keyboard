package com.afghan.key;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import java.io.InputStream;

public class PashtoCustomKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboardPashto, keyboardDari, keyboardFarsi, keyboardArabic, keyboardEnglish;
    private int currentLanguage = 1; // 1:Pashto, 2:Dari, 3:Farsi, 4:Arabic, 5:English

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_layout, null);
        
        keyboardPashto = new Keyboard(this, R.xml.method);
        kv.setKeyboard(keyboardPashto);
        kv.setOnKeyboardActionListener(this);
        
        kv.setBackgroundColor(Color.parseColor("#2d2d2d"));
        return kv;
    }

    public void changeKeyboardBackground(String imagePath) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(imagePath));
            Drawable drawable = Drawable.createFromStream(inputStream, imagePath);
            if (kv != null) {
                kv.setBackground(drawable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        switch (primaryCode) {
            case -5: // Backspace
                getCurrentInputConnection().deleteSurroundingText(1, 0);
                break;
            case -2: // د ژبو د بدلولو تڼۍ
                switchLanguage();
                break;
            case 32: // Space
                getCurrentInputConnection().commitText(" ", 1);
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isDigit(code) && currentLanguage != 5) {
                    code = convertToEasternArabic(code);
                }
                getCurrentInputConnection().commitText(String.valueOf(code), 1);
        }
    }

    private void switchLanguage() {
        currentLanguage++;
        if (currentLanguage > 5) currentLanguage = 1;
        if (currentLanguage == 1) kv.setKeyboard(keyboardPashto);
    }

    private char convertToEasternArabic(char code) {
        switch (code) {
            case '0': return '۰'; case '1': return '۱'; case '2': return '۲';
            case '3': return '۳'; case '4': return '۴'; case '5': return '۵';
            case '6': return '۶'; case '7': return '۷'; case '8': return '۸';
            case '9': return '۹';
            default: return code;
        }
    }

    @Override public void onPress(int primaryCode) {}
    @Override public void onRelease(int primaryCode) {}
    @Override public void onText(CharSequence text) {}
    @Override public void swipeLeft() {}
    @Override public void swipeRight() {}
    @Override public void swipeDown() {}
    @Override public void swipeUp() {}
}
