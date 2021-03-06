package br.com.thejcs.stahp.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.thejcs.stahp.api.entity.ChallengeEntity;
import br.com.thejcs.stahp.api.entity.EntryEntity;
import br.com.thejcs.stahp.api.entity.MatchEntity;
import br.com.thejcs.stahp.api.entity.PlayerEntity;
import br.com.thejcs.stahp.util.GsonRequest;
import br.com.thejcs.stahp.util.StringUtils;

public class StahpAPI {

    private final RequestQueue queue;

    private static final String tag = "SathpAPI";

    private String authKey;

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public StahpAPI(RequestQueue queue) {
        this.queue = queue;
    }

    public void getPlayer(String playerId,
                          Listener<PlayerEntity> listener,
                          ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder();
        String url = urlBuilder.player(playerId);

        addRequest(
                Request.Method.GET, url, PlayerEntity.class,
                null, null, listener, errorListener);
    }

    public void createPlayer(String playerName,
                             Listener<PlayerEntity> listener,
                             ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder();
        String url = urlBuilder.players();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", playerName);

        addRequest(
                Request.Method.POST, url, PlayerEntity.class,
                params, null, listener, errorListener);
    }

    public void getMatches(String playerId,
                           Listener<MatchEntity[]> listener,
                           ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder(authKey);
        String url = urlBuilder.matches(playerId);

        addRequest(
                Request.Method.GET, url, MatchEntity[].class,
                null, null, listener, errorListener);
    }

    public void getMatches(Listener<MatchEntity[]> listener,
                           ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder(authKey);
        String url = urlBuilder.matches();

        addRequest(
                Request.Method.GET, url, MatchEntity[].class,
                null, null, listener, errorListener);
    }

    public void getAllMatches(Listener<MatchEntity[]> listener,
                              ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder(authKey);
        String url = urlBuilder.allMatches();

        addRequest(
                Request.Method.GET, url, MatchEntity[].class,
                null, null, listener, errorListener);
    }

    public void getMatch(String matchId,
                         Listener<MatchEntity> listener,
                         ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder(authKey);
        String url = urlBuilder.match(matchId);

        addRequest(
                Request.Method.GET, url, MatchEntity.class,
                null, null, listener, errorListener);
    }

    public void createMatch(Listener<MatchEntity> listener,
                            ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder(authKey);
        String url = urlBuilder.matches();

        addRequest(
                Request.Method.POST, url, MatchEntity.class,
                null, null, listener, errorListener);
    }

    public void startMatch(String matchId,
                           Listener<MatchEntity> listener,
                           ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder(authKey);
        String url = urlBuilder.match(matchId);

        addRequest(
                Request.Method.POST, url, MatchEntity.class,
                null, null, listener, errorListener);
    }

    public void joinMatch(String matchId,
                          Listener<MatchEntity> listener,
                          ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder(authKey);
        String url = urlBuilder.match(matchId);

        addRequest(
                Request.Method.POST, url, MatchEntity.class,
                null, null, listener, errorListener);
    }

    public void getChallenges(String matchId,
                              Listener<ChallengeEntity[]> listener,
                              ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder(authKey);
        String url = urlBuilder.challenges(matchId);

        addRequest(
                Request.Method.GET, url, ChallengeEntity[].class,
                null, null, listener, errorListener);
    }

    public void sendResult(String matchId, List<String> words,
                           Listener<EntryEntity> listener,
                           ErrorListener errorListener) {
        URLBuilder urlBuilder = new URLBuilder(authKey);
        String url = urlBuilder.words(matchId);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("words", StringUtils.join(words, ","));

        addRequest(
                Request.Method.POST, url, EntryEntity.class,
                params, null, listener, errorListener);
    }

    public void cancelAll() {
        queue.cancelAll(tag);
    }

    protected <T> void addRequest(int method, String url, Class<T> clazz,
                                  Map<String, String> params, Map<String, String> headers,
                                  Listener<T> listener, ErrorListener errorListener) {
        if(headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put("Accept", "application/json");

        Request<T> request = new GsonRequest<T>(
                method, url, clazz,
                params, headers, listener, errorListener);

        request.setTag(tag);
        queue.add(request);
    }
}
