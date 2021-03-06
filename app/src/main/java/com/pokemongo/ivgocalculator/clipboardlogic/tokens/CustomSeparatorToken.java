package com.pokemongo.ivgocalculator.clipboardlogic.tokens;

import android.content.Context;

import com.pokemongo.ivgocalculator.R;

public class CustomSeparatorToken extends SeparatorToken {

    public CustomSeparatorToken() {
        super("");
    }

    @Override public String getTokenName(Context context) {
        return context.getString(R.string.token_msg_custom);
    }

    @Override
    public String getLongDescription(Context context) {
        return context.getString(R.string.token_msg_customSeparator);
    }

    public void setSeparator(String s) {
        this.string = s;
    }
}
