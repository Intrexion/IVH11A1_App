package edu.avans.hartigehap.a1.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import me.denley.preferenceinjector.InjectPreference;

public class RestClient {
    @InjectPreference(value = "pref_api_host", listen = true)
    public String apiHost;
    @InjectPreference(value = "pref_api_port", listen = true)
    public int apiPort;
    public String apiPath = "/hh/rest/v1/";

    private static RestClient instance;
    private AsyncHttpClient client;

    private RestClient() {
        client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 3000);
    }

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return "http://" + apiHost + ":" + apiPort + apiPath + relativeUrl;
    }

    public static RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }

        return instance;
    }
}
