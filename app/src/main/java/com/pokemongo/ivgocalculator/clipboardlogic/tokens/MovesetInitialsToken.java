package com.pokemongo.ivgocalculator.clipboardlogic.tokens;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;
import com.pokemongo.ivgocalculator.R;
import com.pokemongo.ivgocalculator.clipboardlogic.ClipboardToken;
import com.pokemongo.ivgocalculator.pokeflycomponents.MovesetsManager;
import com.pokemongo.ivgocalculator.scanlogic.MovesetData;
import com.pokemongo.ivgocalculator.scanlogic.PokeInfoCalculator;
import com.pokemongo.ivgocalculator.scanlogic.ScanResult;

import java.util.LinkedHashSet;

public class MovesetInitialsToken extends ClipboardToken {

    private static final MovesetData BACKUP_MOVESET = new MovesetData("Razor Leaf", "Frenzy Pla");


    private int maxInitials;


    /**
     * Create a new MovesetInitialsToken given the max initials to use for each move.
     *
     * @param maxInitials How many move name words initials use for the output.
     */
    public MovesetInitialsToken(int maxInitials) {
        super(false);
        this.maxInitials = maxInitials;
    }

    @Override
    public int getMaxLength() {
        return maxInitials * 2;
    }

    @Override
    public String getValue(ScanResult scanResult, PokeInfoCalculator pokeInfoCalculator) {
        return computeInitials(scanResult.selectedMoveset);
    }

    @Override
    public @NonNull String getPreview() {
        LinkedHashSet<MovesetData> movesets = MovesetsManager.getMovesetsForDexNumber(2);
        if (movesets != null) {
            return computeInitials(movesets.iterator().next());
        } else {
            return computeInitials(BACKUP_MOVESET);
        }
    }

    @Override
    public String getStringRepresentation() {
        return super.getStringRepresentation() + String.valueOf(maxInitials);
    }

    @Override
    public String getTokenName(Context context) {
        return context.getString(R.string.token_moveset_initials) + maxInitials;
    }

    @Override
    public String getLongDescription(Context context) {
        LinkedHashSet<MovesetData> movesets = MovesetsManager.getMovesetsForDexNumber(2);

        final MovesetData moveset;
        if (movesets != null) {
            moveset = movesets.iterator().next();
        } else {
            moveset = BACKUP_MOVESET;
        }

        String result = computeInitials(moveset);

        return context.getString(R.string.token_msg_moveset_initials,
                maxInitials, moveset.getFast(), moveset.getCharge(), result);
    }

    @Override
    public Category getCategory() {
        return Category.MOVESET;
    }

    @Override
    public boolean changesOnEvolutionMax() {
        return false;
    }

    private @NonNull String computeInitials(@Nullable MovesetData moveset) {
        if (moveset == null
                || Strings.isNullOrEmpty(moveset.getFast())
                || Strings.isNullOrEmpty(moveset.getCharge())) {
            return "";
        }

        StringBuilder resultBuilder = new StringBuilder(maxInitials * 2);

        String[] fastSplit = moveset.getFast().split(" ");
        for (int i = 0; i < maxInitials; i++) {
            if (i < fastSplit.length) {
                resultBuilder.append(Character.toUpperCase(fastSplit[i].charAt(0)));
            } else {
                resultBuilder.append('_');
            }
        }

        String[] chargeSplit = moveset.getCharge().split(" ");
        for (int i = 0; i < maxInitials; i++) {
            if (i < chargeSplit.length) {
                resultBuilder.append(Character.toUpperCase(chargeSplit[i].charAt(0)));
            } else {
                resultBuilder.append('_');
            }
        }

        return resultBuilder.toString();
    }

}
