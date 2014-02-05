package br.com.thejcs.stahp.api;

import java.util.HashMap;
import java.util.Map;

public class URLBuilder {

    private static String BASE_URL = "http://lambdahq.com:8080/stahp/";

    public static void setBaseURL(String baseURL) {
        URLBuilder.BASE_URL = baseURL;
    }

    private String authKey;

    public URLBuilder() {
    }

    public URLBuilder(String authKey) {
        this.authKey = authKey;
    }

    public String players() {
        return buildURL("players");
    }

    public String player(String playerId) {
        return buildURL("players/" + playerId);
    }

    public String matches() {
        return buildURL("matches", true);
    }

    public String matches(String playerId) {
        // TODO: implement this query on server
        return buildURL("matches", true);
    }

    public String match(String matchId) {
        return buildURL("matches/" + matchId, true);
    }

    public String allMatches() {
        return buildURL("matches/all", true);
    }

    public String challenges(String matchId) {
        return buildURL("matches/" + matchId + "/words", true);
    }

    public String words(String matchId) {
        return buildURL("matches/" + matchId + "/words", true);
    }

    public String words(String matchId, String playerId) {
        return buildURL("matches/" + matchId + "/words/" + playerId, true);
    }

    private String buildURL(String path) {
        return buildURL(path, null, false);
    }

    private String buildURL(String path, boolean needAuthKey) {
        return buildURL(path, null, needAuthKey);
    }

    private String buildURL(String path, Map<String, String> params) {
        return buildURL(path, params, false);
    }

    private String buildURL(String path, Map<String, String> params, boolean needAuthKey) {
        if(params == null) {
            params = new HashMap<String, String>();
        }

        if(needAuthKey) {
            params.put("key", authKey);
        }

        return BASE_URL + path + buildQuery(params);
    }

    private String buildQuery(Map<String, String> params) {
        StringBuilder query = new StringBuilder();

        if(!params.isEmpty()) {
            query.append('?');
            boolean first = true;
            for(String key: params.keySet()) {
                if(first) {
                    first = false;
                    query.append('&');
                }

                query.append(key);
                query.append('=');
                query.append(params.get(key));
            }
        }

        return query.toString();
    }

}
