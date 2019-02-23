package com.nanangrustianto.suara2019.app;



        import android.app.Application;
        import android.content.Context;
        import android.text.TextUtils;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.toolbox.Volley;


/**
 * Created by Philipus on 20/02/2016.
 */
public class AppController2 extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;

    Context context;

    private static AppController2 mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public AppController2() {
    }

    public static synchronized AppController2 getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }


    public AppController2(Context context) {
        this.context = context;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}