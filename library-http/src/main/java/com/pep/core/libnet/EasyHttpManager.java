package com.pep.core.libnet;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.pep.core.libnet.EasyHttpCoinfig.BASE_URL;
import static com.pep.core.libnet.EasyHttpCoinfig.IS_DEBUG;


/**
 * The type Http manager.
 *
 * @author sunbaixin
 */
public class EasyHttpManager {

    private Retrofit retrofit;

    private EasyHttpManager() {
    }


    private static class InnerObject {
        private static EasyHttpManager single = new EasyHttpManager();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static EasyHttpManager getInstance() {
        return InnerObject.single;
    }

    /**
     * interceptors.
     */
    private ArrayList<Interceptor> interceptors = new ArrayList<>();

    /**
     * networkInterceptor.
     */
    private ArrayList<Interceptor> networkInterceptor = new ArrayList<>();


    /**
     * Init.
     *
     * @param baseUrl the base url
     * @throws RuntimeException the runtime exception
     */
    public void init(String baseUrl) throws RuntimeException {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new RuntimeException("baseUrl is null");
        }
        EasyHttpCoinfig.BASE_URL = baseUrl;
        OkHttpClient.Builder builder = getUnsafeOkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        if (IS_DEBUG) {
            // Log Interceptor print
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

            //Log pring level
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            addInterceptor(loggingInterceptor);
        }

        // set interceptors in okhttp builder
        setInterceptors(builder);


        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build();
    }

    private void setInterceptors(OkHttpClient.Builder builder) {

        for (int i = 0; i < interceptors.size(); i++) {
            builder.addInterceptor(interceptors.get(i));
        }

        for (int i = 0; i < networkInterceptor.size(); i++) {
            builder.addNetworkInterceptor(networkInterceptor.get(i));
        }
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add interceptor.
     *
     * @param interceptor the interceptor
     */
    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    /**
     * Gets retrofit.
     *
     * @return the retrofit
     */
    public Retrofit getRetrofit() {
        if (retrofit == null) {
            return null;
        }
        return retrofit;
    }

    /**
     * Gets service.
     *
     * @param <T>     the type parameter
     * @param service the service
     * @return the service
     */
    public <T> T getService(final Class<T> service) {
        return retrofit.create(service);
    }

}
