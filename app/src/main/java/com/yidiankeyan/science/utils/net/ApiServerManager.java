package com.yidiankeyan.science.utils.net;

import android.util.Log;

import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nby on 2016/12/23 0023.
 */

public class ApiServerManager {

    private static OkHttpClient mOkHttpClient;

    private static OkHttpClient mFileUploadClient;

    static {
        initOkHttpClient();
        initFileUploadClient();
    }

    private static void initFileUploadClient() {
        if (mFileUploadClient == null) {
            synchronized (ApiServerManager.class) {
                if (mFileUploadClient == null) {
                    //设置Http缓存
                    Cache cache = new Cache(new File(DemoApplication.getInstance().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
                    if (DemoApplication.DEBUG) {
                        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                Log.i("OkHttp", message);
                            }
                        });
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        mFileUploadClient = new OkHttpClient.Builder()
                                .cache(cache)
                                .retryOnConnectionFailure(true)
                                .connectTimeout(60, TimeUnit.SECONDS)
                                .writeTimeout(60, TimeUnit.SECONDS)
                                .readTimeout(60, TimeUnit.SECONDS)
                                .addInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(Chain chain) throws IOException {
                                        Request originalRequest = chain.request();
                                        Request.Builder builder = originalRequest.newBuilder();
                                        builder.header("token", SpUtils.getStringSp(DemoApplication.applicationContext, "access_token"));
                                        Request.Builder requestBuilder = builder.method(originalRequest.method(), originalRequest.body());
                                        Request request = requestBuilder.build();
                                        return chain.proceed(request);
                                    }
                                })
                                .addInterceptor(interceptor)
                                .build();
                    } else {
                        mFileUploadClient = new OkHttpClient.Builder()
                                .cache(cache)
                                .retryOnConnectionFailure(true)
                                .connectTimeout(60, TimeUnit.SECONDS)
                                .writeTimeout(60, TimeUnit.SECONDS)
                                .readTimeout(60, TimeUnit.SECONDS)
                                .addInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(Chain chain) throws IOException {
                                        Request originalRequest = chain.request();
                                        Request.Builder builder = originalRequest.newBuilder();
                                        builder.header("token", SpUtils.getStringSp(DemoApplication.applicationContext, "access_token"));
                                        Request.Builder requestBuilder = builder.method(originalRequest.method(), originalRequest.body());
                                        Request request = requestBuilder.build();
                                        return chain.proceed(request);
                                    }
                                })
                                .build();
                    }
                }
            }
        }
    }

    /**
     * 初始化OKHttpClient
     */
    private static void initOkHttpClient() {

        if (mOkHttpClient == null) {
            synchronized (ApiServerManager.class) {
                if (mOkHttpClient == null) {
                    //设置Http缓存
                    Cache cache = new Cache(new File(DemoApplication.getInstance().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
                    if (DemoApplication.DEBUG) {
                        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                Log.i("OkHttp", message);
                            }
                        });
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        mOkHttpClient = new OkHttpClient.Builder()
                                .cache(cache)
                                .retryOnConnectionFailure(true)
                                .connectTimeout(20, TimeUnit.SECONDS)
                                .writeTimeout(20, TimeUnit.SECONDS)
                                .readTimeout(20, TimeUnit.SECONDS)
                                .addInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(Chain chain) throws IOException {
                                        Request originalRequest = chain.request();
                                        Request.Builder builder = originalRequest.newBuilder();
                                        builder.header("token", SpUtils.getStringSp(DemoApplication.applicationContext, "access_token"));
                                        builder.header("Content-Type", "application/json");
                                        Request.Builder requestBuilder = builder.method(originalRequest.method(), originalRequest.body());
                                        Request request = requestBuilder.build();
                                        return chain.proceed(request);
                                    }
                                })
                                .addInterceptor(interceptor)
                                .build();
                    } else {
                        mOkHttpClient = new OkHttpClient.Builder()
                                .cache(cache)
                                .retryOnConnectionFailure(true)
                                .connectTimeout(20, TimeUnit.SECONDS)
                                .writeTimeout(20, TimeUnit.SECONDS)
                                .readTimeout(20, TimeUnit.SECONDS)
                                .addInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(Chain chain) throws IOException {
                                        Request originalRequest = chain.request();
                                        Request.Builder builder = originalRequest.newBuilder();
                                        builder.header("token", SpUtils.getStringSp(DemoApplication.applicationContext, "access_token"));
                                        builder.header("Content-Type", "application/json");
                                        Request.Builder requestBuilder = builder.method(originalRequest.method(), originalRequest.body());
                                        Request request = requestBuilder.build();
                                        return chain.proceed(request);
                                    }
                                })
                                .build();
                    }
                }
            }
        }
    }

    private volatile static ApiServerManager instance;

    private ApiServerManager() {
    }

    public static ApiServerManager getInstance() {
        if (instance == null) {
            synchronized (ApiServerManager.class) {
                if (instance == null) {
                    instance = new ApiServerManager();
                }
            }
        }
        return instance;
    }

    public ApiServer getApiServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SystemConstant.URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiServer.class);
    }

    public WebApiServer getWebApiServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SystemConstant.MYURL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WebApiServer.class);
    }

    public FileUploadService getFileUploadServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SystemConstant.MYURL)
                .client(mFileUploadClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(FileUploadService.class);
    }

    public UserApiServer getUserApiServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SystemConstant.USER_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(UserApiServer.class);
    }

    /**
     * 测试专用
     * @return
     */
    public UserApiServer getUserServer() {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.16:8066/")
                .baseUrl("http://192.168.1.56:8090/")
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(UserApiServer.class);
    }
}
