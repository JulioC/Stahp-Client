package br.com.thejcs.stahp;

import android.app.Application;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import br.com.thejcs.stahp.api.StahpAPI;
import br.com.thejcs.stahp.api.URLBuilder;

public class StahpApplication extends Application {

    private static StahpApplication instance;

    public static StahpApplication getInstance() {
        return instance;
    }

    private StahpAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        setupAPI();
    }

    private void setupAPI() {
        URLBuilder.setBaseURL(getString(R.string.base_url));

        RequestQueue queue = Volley.newRequestQueue(this);
        api = new StahpAPI(queue);
        api.setAuthKey(getPlayerId());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        api.cancelAll();
    }

    public static final String PREF_NAME = "stahp_preferences";
    public static final String PREF_PLAYER_ID = "player_id";

    public String getPlayerId() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return settings.getString(PREF_PLAYER_ID, null);
    }

    public void setPlayerId(String playerId) {
        api.setAuthKey(getPlayerId());

        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        settings.edit()
                .putString(PREF_PLAYER_ID, playerId)
                .commit();
    }

    public void clearPlayerId() {
        api.setAuthKey(null);

        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        settings.edit()
                .remove(PREF_PLAYER_ID)
                .commit();
    }

    public StahpAPI getAPI() {
        return api;
    }

}
