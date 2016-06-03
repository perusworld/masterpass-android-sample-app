package com.mastercard.masterpass.test.merchant.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.PrivateKey;

import javax.ws.rs.core.Response.Status;

import com.mastercard.masterpass.test.merchant.ServiceName;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gdata.client.authn.oauth.OAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthRsaSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthUtil;
import com.google.gdata.util.common.util.Base64;
import com.mastercard.masterpass.test.merchant.exceptions.MasterPassError;
import com.mastercard.masterpass.test.merchant.exceptions.MasterPassException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * RestOpenAPIService class responsible for creating connection with Master Pass
 * Open API services. Rest Template API has been used to call RESTful services.
 */
public class RestOpenAPIService {

    // OAuth header params
    private static final String OAUTH_VERSION_NUMBER = "1.0";
    private static final String OAUTH_VERSION = "oauth_version";
    private static final String SIGNATURE_METHOD = "RSA-SHA1";
    private static final String KEY_CONSTANTS_CALLBACK_URL = "constants.callbackurl";
    private static final String KEY_CONSTANTS_REALM_KEY = "constants.realmkey";

    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger(RestOpenAPIService.class);

    /**
     * Standard HTTP 200 OK code
     */
    private static final int HTTP_OK = 200;

    // Keys for reading config.properties file
    private static final String KEY_P12_FILENAME = ".p12.file.name";
    private static final String KEY_P12_KEYSTORE_TYPE = ".p12.keystore.type";
    private static final String KEY_P12_PASSWORD = ".p12.password";
    private static final String KEY_P12_KEY_ALIAS = ".p12.key.alias";
    public static final String KEY_CONSUMER_KEY = ".consumer.key";

    // Internal temp variables
    private String requestToken;
    private String verifierToken;
    private String accessToken;

    // Setters for variables

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public void setVerifierToken(String verifierToken) {
        this.verifierToken = verifierToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    /**
     * Make a POST request to Switch server at given endpoint with given request
     *
     * @param requestHeader http header
     * @param requestBody   http body
     * @param endpoint      full url of server
     * @param contentType   mime type
     * @param clazz         response class
     * @param <T>           response class
     * @return response casted to T
     */
    public <T> T postService(String requestHeader, Object requestBody, String endpoint, String contentType, Class<T> clazz) {
        ClientResponse clientResponse;

        Client client = Client.create();
        WebResource webResource = client.resource(endpoint);

        logger.debug("make service call to switch: " + endpoint);
        logger.debug("request header: " + requestHeader);
        logger.debug("request body: " + requestBody);

        clientResponse = webResource
                .header("Content-Type", contentType + ";charset=UTF-8")
                .header("Authorization", requestHeader)
                .post(ClientResponse.class, requestBody);

        if (clientResponse.getStatus() != HTTP_OK) {
            String serverError = "{ 'errorCode' : '" + clientResponse.getStatus() + "',"
                    + "'errorMessage' : '" + convertInputStreamToString(clientResponse.getEntityInputStream()) + "'}";

            logger.error("postService server error: " + serverError);
            throw new MasterPassException(Status.INTERNAL_SERVER_ERROR,
                    MasterPassError.ERROR_CODE_SYSTEM_ERROR,
                    MasterPassError.ERROR_MESSAGE_SYSTEM_ERROR + ": " + serverError);
        }
        return clazz.cast(clientResponse.getEntity(clazz));
    }

    /**
     * Make a GET request to Switch server at given endpoint with given request
     *
     * @param requestHeader http header
     * @param requestBody   http body
     * @param endpoint      full url of server
     * @param contentType   mime type
     * @param clazz         response class
     * @param <T>           response class
     * @return response casted to T
     */
    public <T> T getService(String requestHeader, Object requestBody, String endpoint, String contentType, Class<T> clazz) {
        ClientResponse clientResponse;

        Client client = Client.create();
        WebResource webResource = client.resource(endpoint);

        logger.debug("make service call to switch: " + endpoint);
        logger.debug("request header: " + requestHeader);
        logger.debug("request body: " + requestBody);
        clientResponse = webResource
                .header("Content-Type", contentType + ";charset=UTF-8")
                .header("Authorization", requestHeader)
                .get(ClientResponse.class);
        if (clientResponse.getStatus() != HTTP_OK) {
            String serverError = clientResponse.getStatus() + clientResponse.getEntity(String.class);
            logger.error("getService server error:  " + serverError);
            throw new MasterPassException(Status.INTERNAL_SERVER_ERROR,
                    MasterPassError.ERROR_CODE_SYSTEM_ERROR,
                    MasterPassError.ERROR_MESSAGE_SYSTEM_ERROR + ": " + serverError);
        }
        return clazz.cast(clientResponse.getEntity(clazz));
    }

    /**
     * Create OAuth http header based on body, url and service
     *
     * @param requestBody http body
     * @param serviceURL  target url
     * @param serviceName current service name
     * @return OAuth header
     */
    public String createOAuthHeader(String requestBody, String serviceURL, ServiceName serviceName) {
        return createOAuthHeader(new Config(), requestBody, serviceURL, serviceName);
    }

    /**
     * Create OAuth http header based on body, url and service
     *
     * @param config config helper
     * @param requestBody http body
     * @param serviceURL  target url
     * @param serviceName current service name
     * @return OAuth header
     */
    public String createOAuthHeader(Config config, String requestBody, String serviceURL, ServiceName serviceName) {
        String header = "";
        try {
            PrivateKey servicePrivateKey = OauthUtil.getPrivateKeyFromP12(
                    config.readProperties().getProperty(Config.activeProfile + KEY_P12_FILENAME),
                    config.readProperties().getProperty(Config.activeProfile + KEY_P12_KEYSTORE_TYPE),
                    config.readProperties().getProperty(Config.activeProfile + KEY_P12_PASSWORD),
                    config.readProperties().getProperty(Config.activeProfile + KEY_P12_KEY_ALIAS));
            String consumerKey = config.readProperties().getProperty(Config.activeProfile + KEY_CONSUMER_KEY);
            String realmKey = config.readProperties().getProperty(KEY_CONSTANTS_REALM_KEY);
            String callbackUrl = config.readProperties().getProperty(KEY_CONSTANTS_CALLBACK_URL);
            header = createOAuthHeader(servicePrivateKey, requestBody, serviceURL, consumerKey, serviceName, realmKey, callbackUrl);
        } catch (Exception e) {
            logger.error("Create oauth header error: " + e.getMessage());
            e.printStackTrace();
        }
        return header;
    }

    /**
     * Create OAuthHeader as per guideline.
     *
     * @param servicePrivateKey private key object
     * @param requestBody       http body
     * @param serviceURL        full Switch endpoint url
     * @param consumerKey       consumer key used for authentication
     * @param serviceName       name of the service to call
     * @param realmKey          realm key from config.properties
     * @param callbackUrl       callback url from config.properties
     * @return oauth header string
     * @throws Exception
     */
    public String createOAuthHeader(PrivateKey servicePrivateKey,
                                    String requestBody, String serviceURL, String consumerKey,
                                    ServiceName serviceName, String realmKey, String callbackUrl) throws Exception {

        OAuthRsaSha1Signer rsaSigner = new OAuthRsaSha1Signer();
        rsaSigner.setPrivateKey(servicePrivateKey);

        OAuthParameters params = new OAuthParameters();
        params.setOAuthConsumerKey(consumerKey);
        params.setOAuthNonce(OAuthUtil.getNonce());

        String ts = OAuthUtil.getTimestamp();

        params.setOAuthTimestamp(ts);
        params.setOAuthSignatureMethod(SIGNATURE_METHOD);
        params.setOAuthType(OAuthParameters.OAuthType.THREE_LEGGED_OAUTH);
        params.addCustomBaseParameter(OAUTH_VERSION, OAUTH_VERSION_NUMBER);

        if (serviceName == ServiceName.REQUEST_TOKEN || serviceName == ServiceName.ACCESS_TOKEN) {
            params.addCustomBaseParameter(OAuthParameters.OAUTH_CALLBACK_KEY, callbackUrl);
        }
        if (serviceName == ServiceName.ACCESS_TOKEN) {
            params.addCustomBaseParameter(OAuthParameters.OAUTH_VERIFIER_KEY, verifierToken);
            params.addCustomBaseParameter(OAuthParameters.OAUTH_TOKEN_KEY, requestToken);
        }
        if (serviceName == ServiceName.CHECKOUT_RESOURCE) {
            params.addCustomBaseParameter(OAuthParameters.OAUTH_TOKEN_KEY, accessToken);
        }
        String method = "POST";
        if (serviceName == ServiceName.CHECKOUT_RESOURCE) {
            method = "GET";
        }
        if (requestBody != null) {

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            byte[] hash = digest.digest(requestBody.getBytes("UTF-8"));
            String encodedHash = Base64.encode(hash);

            params.addCustomBaseParameter("oauth_body_hash", encodedHash);
        }

        String baseString = OAuthUtil.getSignatureBaseString(serviceURL, method, params.getBaseParameters());

        String signature = rsaSigner.getSignature(baseString, params);

        params.addCustomBaseParameter("oauth_signature", signature);

        if (serviceName == ServiceName.REQUEST_TOKEN || serviceName == ServiceName.ACCESS_TOKEN) {
            params.addCustomBaseParameter(OAuthParameters.REALM_KEY, realmKey);
        }

        return OauthUtil.buildAuthHeaderString(params);
    }

    /**
     * Converts input stream to string to be able to pass it forward
     *
     * @param in input stream
     * @return string
     */
    private String convertInputStreamToString(InputStream in) {
        InputStreamReader is = new InputStreamReader(in);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        try {
            String read = br.readLine();

            while (read != null) {
                sb.append(read);
                read = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return sb.toString();
    }

}
