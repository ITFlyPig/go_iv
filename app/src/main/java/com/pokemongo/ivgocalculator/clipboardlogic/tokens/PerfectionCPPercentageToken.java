package com.pokemongo.ivgocalculator.clipboardlogic.tokens;

import android.content.Context;

import com.pokemongo.ivgocalculator.R;
import com.pokemongo.ivgocalculator.clipboardlogic.ClipboardToken;
import com.pokemongo.ivgocalculator.scanlogic.IVCombination;
import com.pokemongo.ivgocalculator.scanlogic.PokeInfoCalculator;
import com.pokemongo.ivgocalculator.scanlogic.Pokemon;
import com.pokemongo.ivgocalculator.scanlogic.ScanResult;

/**
 * Created by Johan on 2016-09-25.
 * Token representing how close your pokemon is in max CP compared to if the pokemon had perfect IVs.
 */

public class PerfectionCPPercentageToken extends ClipboardToken {
    /**
     * Create a clipboard token.
     * The boolean in the constructor can be set to false if pokemon evolution is not applicable.
     *
     * @param maxEv true if the token should change its logic to pretending the pokemon is fully evolved.
     */
    public PerfectionCPPercentageToken(boolean maxEv) {
        super(maxEv);
    }

    @Override
    public int getMaxLength() {
        return 3;
    }

    @Override
    public String getValue(ScanResult isr, PokeInfoCalculator pokeInfoCalculator) {
        Pokemon poke = getRightPokemon(isr.pokemon, pokeInfoCalculator);
        double perfectIvCp = pokeInfoCalculator.getCpRangeAtLevel(poke,
                IVCombination.MAX, IVCombination.MAX, 40).getFloatingAvg();
        double thisCP = pokeInfoCalculator.getCpRangeAtLevel(poke,
                isr.getCombinationLowIVs(), isr.getCombinationHighIVs(), 40).getFloatingAvg();
        long roundedPerfection = Math.round(thisCP * 100.0 / perfectIvCp);
        return String.valueOf(roundedPerfection);
    }

    @Override
    public String getPreview() {
        return "97";
    }


    @Override
    public String getTokenName(Context context) {
        return "mIV%";
    }

    @Override
    public String getLongDescription(Context context) {
        return context.getString(R.string.token_msg_perfCP);
    }

    @Override
    public Category getCategory() {
        return Category.EVALUATION;
    }

    @Override
    public boolean changesOnEvolutionMax() {
        return true;
    }
}
