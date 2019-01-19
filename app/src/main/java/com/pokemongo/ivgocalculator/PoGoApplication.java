package com.pokemongo.ivgocalculator;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.pokemongo.ivgocalculator.pokeflycomponents.MovesetsManager;
import com.pokemongo.ivgocalculator.utils.CrashlyticsWrapper;
import com.pokemongo.ivgocalculator.utils.FontsOverride;
import com.umeng.analytics.MobclickAgent;

import timber.log.Timber;

public class PoGoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            CrashlyticsWrapper.getInstance().init(this);
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Fonts overriding application wide
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Lato-Medium.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Lato-Medium.ttf");

        MovesetsManager.init(this);

        Log.d("wyl", "PoGoApplication 开始初始化友盟统计");
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, getString(R.string.umeng_key), "release", MobclickAgent.EScenarioType.E_UM_NORMAL, true));
    }
}
