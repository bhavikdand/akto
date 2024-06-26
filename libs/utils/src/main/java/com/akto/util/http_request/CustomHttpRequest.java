package com.akto.util.http_request;

import com.akto.log.LoggerMaker;
import com.akto.log.LoggerMaker.LogDb;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class CustomHttpRequest {
    private static final HttpClient httpclient = HttpClients.createDefault();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final LoggerMaker loggerMaker = new LoggerMaker(CustomHttpRequest.class);
    private static final Logger logger = LoggerFactory.getLogger(CustomHttpRequest.class);


    public static final String FORM_URL_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";

    public static Map<String,Object> getRequest(String url, String authHeader) throws HttpResponseException {
        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        httpGet.setHeader(HttpHeaders.AUTHORIZATION,authHeader);

        return s(httpGet);
    }

    public static Map<String,Object> postRequest(String url, List<NameValuePair> params) throws HttpResponseException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return s(httpPost);

    }

    public static Map<String,Object> postRequestEncodedType(String url, List<NameValuePair> params) throws HttpResponseException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", FORM_URL_ENCODED_CONTENT_TYPE);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }

        return s(httpPost);

    }

    public static Map<String,Object> s(HttpUriRequest request) throws HttpResponseException {
        //Execute and get the response.
        HttpResponse response = null;
        try {
            response = httpclient.execute(request);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        if (response == null) {
            logger.info("RESPONSE NULL");
            return null;
        }

        HttpEntity entity = response.getEntity();
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            throw new HttpResponseException(statusLine.getStatusCode(),statusLine.getReasonPhrase());
        }

        InputStream inputStream = null;
        Map<String,Object> jsonMap = null;
        if (entity != null) {
            try {
                inputStream = entity.getContent();
                jsonMap = mapper.readValue(inputStream, Map.class);
            } catch (IOException ioException) {
                loggerMaker.errorAndAddToDb("Exception in parsing entity of sso request \n " + ioException.getMessage(), LogDb.DASHBOARD);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ioException) {
                        loggerMaker.errorAndAddToDb("Exception in parsing entity of sso request \n " + ioException.getMessage(), LogDb.DASHBOARD);
                    }
                }
            }
        }

        return jsonMap;
    }
}
