package com.pokemongo.ivgocalculator.clipboardlogic.tokens;

import android.content.Context;

import com.pokemongo.ivgocalculator.R;

public class CustomNameToken extends PokemonNameToken {

    public CustomNameToken(boolean maxEv) {
        super(maxEv, 5);
    }

    @Override public String getTokenName(Context context) {
        return "PokeName";
    }

    @Override
    public String getLongDescription(Context context) {
        return "This token gives the name of the pokemon, you can "
                + "pick how long the name can be, or if it should be cut off.";
    }

}
