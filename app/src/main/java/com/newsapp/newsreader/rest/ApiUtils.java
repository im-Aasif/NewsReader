package com.newsapp.newsreader.rest;

/**
 * Created by GLaDOS on 9/19/2017.
 */

public class ApiUtils {

    public static String apiKey = "f0eb288473a548cd9d21c015d882ab0e";
    private static String BASE_URL = "https://newsapi.org/v1/";

    public static RestService getRestService() {
        return RestClient.getClient(BASE_URL).create(RestService.class);
    }
}
