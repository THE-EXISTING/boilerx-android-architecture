package com.existing.nextwork.cookie;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.webkit.CookieSyncManager;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import timber.log.Timber;

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

public class NextworkWebkitCookieJar extends CookieManager implements CookieJar {
    private static final String TAG = NextworkWebkitCookieJar.class.getSimpleName();

    private static NextworkWebkitCookieJar instance;

    public static NextworkWebkitCookieJar getInstance() {
        if (instance == null) {
            instance = new NextworkWebkitCookieJar();
        }
        return instance;
    }

    private android.webkit.CookieManager webkitCookieManager;

    private NextworkWebkitCookieJar() {
        this(null, null);
    }

    private NextworkWebkitCookieJar(CookieStore store, CookiePolicy cookiePolicy) {
        super(null, cookiePolicy);
        this.webkitCookieManager = android.webkit.CookieManager.getInstance();
        this.webkitCookieManager.setAcceptCookie(true);

    }

    public static void setMockInstance(NextworkWebkitCookieJar webkitCookieJar) {
        instance = webkitCookieJar;
    }

    /**
     * {@link #NextworkWebkitCookieJar(CookieStore, CookiePolicy)} didn't suffix for some older devices. Old devices required initialization through {@link CookieSyncManager#createInstance(Context)}  }
     * We didn't init it here because we don't want android logic here.
     *
     * @param context Context
     */
    public void init(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //this is for some specific devices below lollipop
            //http://stackoverflow.com/questions/16107892/cookie-manager-causing-fatal-signal-11

            CookieSyncManager.createInstance(context);
        }
    }

    public boolean isInitialized() {
        try {
            return CookieSyncManager.getInstance() != null;
        } catch (IllegalStateException ignored) {
        }
        return false;
    }

    public void setCookie(Uri uri, List<NextworkCookie> cookieList) {
        if ((uri == null) || cookieList == null || cookieList.isEmpty())
            return;

        String url = uri.toString();
        for (NextworkCookie cookie : cookieList) {
            webkitCookieManager.setCookie(url, cookie.toString());
        }
    }

    public List<String> getCookie(Uri uri) {
        // make sure our args are valid
        if (uri == null)
            throw new IllegalArgumentException("Argument is null");

        // save our url once
        String url = uri.toString();

        // get the cookie
        String cookie = webkitCookieManager.getCookie(url);

        if (cookie != null) {
            return Collections.singletonList(cookie);
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        // make sure our args are valid
        if ((uri == null) || (responseHeaders == null))
            return;

        // save our url once
        String url = uri.toString();

        // go over the headers
        for (String headerKey : responseHeaders.keySet()) {
            // ignore headers which aren't cookie related
            if ((headerKey == null)
                    || !(headerKey.equalsIgnoreCase(NextworkCookie.COOKIE_FORMAT)
                    || headerKey.equalsIgnoreCase(NextworkCookie.COOKIE_V2_FORMAT)))
                continue;

            // process each of the headers
            for (String headerValue : responseHeaders.get(headerKey)) {
                webkitCookieManager.setCookie(url, headerValue);
            }
        }
    }

    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        // make sure our args are valid
        if ((uri == null) || (requestHeaders == null))
            throw new IllegalArgumentException("Argument is null");

        // save our url once
        String url = uri.toString();

        // prepare our response
        Map<String, List<String>> res = new HashMap<>();

        // get the cookie
        String cookie = webkitCookieManager.getCookie(url);

        // return it
        if (cookie != null) {
            res.put("Cookie", Collections.singletonList(cookie));
        }
        return res;
    }

    @Override
    public CookieStore getCookieStore() {
        // we don't want anyone to work with this cookie store directly
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        HashMap<String, List<String>> generatedResponseHeaders = new HashMap<>();
        ArrayList<String> cookiesList = new ArrayList<>();
        for (Cookie c : cookies) {
            // toString correctly generates a normal cookie string
            cookiesList.add(c.toString());
        }
        generatedResponseHeaders.put(NextworkCookie.COOKIE_FORMAT, cookiesList);
        try {
            put(url.uri(), generatedResponseHeaders);
        } catch (IOException e) {
            Timber.e(e, TAG, "Error adding cookies through okhttp");
        }
    }


    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        ArrayList<Cookie> cookieArrayList = new ArrayList<>();
        try {
            Map<String, List<String>> cookieList = get(url.uri(), new HashMap<>());
            // Format here looks like: "Cookie":["cookie1=val1;cookie2=val2;"]
            for (List<String> ls : cookieList.values()) {
                for (String s : ls) {
                    String[] cookies = s.split(";");
                    for (String cookie : cookies) {
                        Cookie c = Cookie.parse(url, cookie);
                        cookieArrayList.add(c);
                    }
                }
            }
        } catch (IOException e) {
            Timber.e(e, TAG, "error making cookie!");
        }
        return cookieArrayList;
    }

    @SuppressWarnings("deprecation")
    public void clearCookie(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Timber.d(getClass().getSimpleName(), "Using clearCookies code for API >=%s", String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            android.webkit.CookieManager.getInstance().removeAllCookies(null);
            android.webkit.CookieManager.getInstance().flush();
        } else {
            Timber.d(getClass().getSimpleName(), "Using clearCookies code for API <%s", String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            cookieSyncManager.startSync();
            android.webkit.CookieManager.getInstance().removeAllCookie();
            android.webkit.CookieManager.getInstance().removeSessionCookie();
            cookieSyncManager.stopSync();
            cookieSyncManager.sync();
        }
    }
}
