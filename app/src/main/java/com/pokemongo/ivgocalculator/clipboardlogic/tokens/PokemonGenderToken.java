package com.pokemongo.ivgocalculator.clipboardlogic.tokens;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pokemongo.ivgocalculator.R;
import com.pokemongo.ivgocalculator.clipboardlogic.ClipboardToken;
import com.pokemongo.ivgocalculator.scanlogic.PokeInfoCalculator;
import com.pokemongo.ivgocalculator.scanlogic.Pokemon;
import com.pokemongo.ivgocalculator.scanlogic.ScanResult;

/**
 * Created by Mattia on 2017-12-15.
 * A token which returns the gender of the scanned pokemon
 */

public class PokemonGenderToken extends ClipboardToken {

    public enum Type {
        SYMBOL(""), // Empty for backwards compatibility
        LETTER("_letter");

        private String representationSuffix;

        Type(@NonNull String representationSuffix) {
            this.representationSuffix = representationSuffix;
        }
    }

    private Type type;

    /**
     * Create a clipboard token.
     * The boolean in the constructor can be set to false if pokemon evolution is not applicable.
     */
    public PokemonGenderToken(boolean maxEv, Type type) {
        super(maxEv);
        this.type = type;
    }

    @Override
    public int getMaxLength() {
        return 1;
    }

    @Override
    public String getValue(ScanResult scanResult, PokeInfoCalculator pokeInfoCalculator) {
        switch (type) {
            default:
            case SYMBOL:
                return scanResult.gender.getSymbol();
            case LETTER:
                if (scanResult.gender == Pokemon.Gender.N) {
                    return "";
                } else {
                    return scanResult.gender.getLetter();
                }
        }
    }

    @Override
    public String getPreview() {
        switch (type) {
            default:
            case SYMBOL:
                return "⚤";

            case LETTER:
                return "M";
        }
    }

    @Override
    public String getTokenName(Context context) {
        switch (type) {
            default:
            case SYMBOL:
                return context.getString(R.string.token_pokemon_gender_symbol);
            case LETTER:
                return context.getString(R.string.token_pokemon_gender_letter);
        }
    }

    @Override
    public String getLongDescription(Context context) {
        String[] outputs = new String[2];
        switch (type) {
            default:
            case SYMBOL:
                outputs[0] = Pokemon.Gender.M.getSymbol();
                outputs[1] = Pokemon.Gender.F.getSymbol();
                break;
            case LETTER:
                outputs[0] = Pokemon.Gender.M.getLetter();
                outputs[1] = Pokemon.Gender.F.getLetter();
                break;
        }
        return context.getString(R.string.token_msg_gender, outputs[0], outputs[1]);
    }

    @Override
    public Category getCategory() {
        return Category.BASIC_STATS;
    }

    @Override
    public boolean changesOnEvolutionMax() {
        return false;
    }

    @Override
    public String getStringRepresentation() {
        return super.getStringRepresentation() + type.representationSuffix;
    }
}
