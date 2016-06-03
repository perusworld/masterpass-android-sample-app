package com.mastercard.masterpass.test.merchant.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.LinkedHashMap;
import java.util.Map;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gdata.client.authn.oauth.OAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthUtil;


/**
 * Provide utility functions for oauth authentication
 */
public class OauthUtil {
    /**
     * File/Console logger
     */
    private static Logger logger = LogManager.getLogger(OauthUtil.class);

    /**
     * Build OAuth header from OAuthParameters
     *
     * @param params input params
     * @return built header
     */
    public static String buildAuthHeaderString(OAuthParameters params) {
        StringBuilder authHeader = new StringBuilder();
        int cnt = 0;
        authHeader.append("OAuth ");
        Map<String, String> paramMap = params.getBaseParameters();
        Object[] paramNames = paramMap.keySet().toArray();
        for (Object paramName : paramNames) {
            String value = paramMap.get(paramName);
            authHeader.append(paramName + "=\"" + OAuthUtil.encode(value)
                    + "\"");
            cnt++;
            if (paramNames.length > cnt) {
                authHeader.append(",");
            }
        }
        return authHeader.toString();
    }

    /**
     * Split query parameters, and save them in a map
     * Uses UTF-8 encoding
     *
     * @param query query params
     * @return map with key-value parsed from query
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        if (query == null || query.isEmpty()) {
            return query_pairs;
        }
        int beginIdx = query.indexOf("?");
        String paramsString = query.substring(beginIdx + 1);
        String[] pairs = paramsString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx != -1) {
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                        URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
        }
        return query_pairs;
    }

    /**
     * Create PrivateKey from P12 file
     *
     * @param pathP12          path to p12 file
     * @param keyStoreInstance key store instance
     * @param password         password
     * @param alias            alias
     * @return private key
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromP12(String pathP12, String keyStoreInstance, String password, String alias) throws Exception {
        KeyStore keystore = KeyStore.getInstance(keyStoreInstance);
        InputStream inputStream = OauthUtil.class.getClassLoader().getResourceAsStream(pathP12);
        if (inputStream != null) {
            try {
                // Convert stream to keystore
                keystore.load(inputStream, password.toCharArray());
                return (PrivateKey) keystore.getKey(alias, password.toCharArray());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Tidy up
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }

    /**
     * Parse OAuth response and extract OAuth Token value.
     * The server response is in form of: key=value&...
     *
     * @param response raw response from Switch
     * @return request token value
     */
    public static String extractOAuthToken(String response) {
        String token = "";
        Map<String, String> queryPairs;
        try {
            queryPairs = OauthUtil.splitQuery(response);
            if (queryPairs.containsKey("oauth_token")) {
                token = queryPairs.get("oauth_token");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("Extract OAuth Token Error");
        }
        return token;
    }
}
