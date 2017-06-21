package com.example.okhttpfinal;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/6/12.
 */

public class HttpsCerManager {
    private OkHttpClient.Builder builder;

    public HttpsCerManager(OkHttpClient.Builder builder){
        this.builder=builder;
    }

    public void setCertificates(List<InputStream> certificates) {
        setCertificates(certificates.toArray(new InputStream[]{}), null, null);
    }

    public void setCertificates(InputStream... certificates) {
        setCertificates(certificates, null, null);
    }

    public void setCertificates(List<InputStream> certificates,InputStream bksFile, String password){
        setCertificates(certificates,bksFile,password);
    }

    private TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) {
            return null;
        }
        /*
        * 从InputStream 中获取证书，写入Keystore
         */
        try {
            //返回证书必须是java.security.cert.X509Certificate 的实例
            CertificateFactory certificateFactory=CertificateFactory.getInstance("X.509");
            KeyStore keyStore=KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index=0;
            for(InputStream inputStream:certificates){
                String certificateAlias=Integer.toString(index);
                keyStore.setCertificateEntry(certificateAlias,
                        certificateFactory.generateCertificate(inputStream));
                if(inputStream!=null)
                    inputStream.close();
            }
            TrustManagerFactory trustManagerFactory =null;
            trustManagerFactory =TrustManagerFactory.getInstance
                    (TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers=trustManagerFactory.getTrustManagers();

            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyManager[] prepareKeyManager (InputStream bksFile, String password){
        try {
            if(bksFile ==null || password == null){
                return null;
            }
            KeyStore keyStore=KeyStore.getInstance("BKS");
            keyStore.load(bksFile,password.toCharArray());

            KeyManagerFactory keyManagerFactory=KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password.toCharArray());

            if(bksFile != null){
                bksFile.close();
            }
            return keyManagerFactory.getKeyManagers();
        }catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private X509TrustManager chooseTrustManager (TrustManager[] trustManagers){
        for(TrustManager trustManager:trustManagers){
            if(trustManager instanceof  X509TrustManager){
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private void setCertificates(InputStream[] certificates,InputStream bksFile, String password){
        TrustManager[] trustManagers =prepareTrustManager(certificates);
        KeyManager[] keyManagers=prepareKeyManager(bksFile,password);

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers,
                    new TrustManager[] { new OkHttpTrustManager(chooseTrustManager(trustManagers)) },
                    new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory());
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }
    private class OkHttpTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public OkHttpTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
