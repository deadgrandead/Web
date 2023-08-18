package com.deadgrandead;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final InputStream body;
    private final Map<String, String> queryParams;

    public Request(String method, String path, Map<String, String> headers, InputStream body) {
        this.method = method;
        this.headers = headers;
        this.body = body;

        int questionMarkPosition = path.indexOf("?");
        if (questionMarkPosition >= 0) {
            this.path = path.substring(0, questionMarkPosition);
            String query = path.substring(questionMarkPosition + 1);
            String decodedQuery = java.net.URLDecoder.decode(query, StandardCharsets.UTF_8);
            this.queryParams = parseQueryString(decodedQuery);
        } else {
            this.path = path;
            this.queryParams = new HashMap<>();
        }
    }

    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryMap = new HashMap<>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0 && idx < pair.length() - 1) {
                queryMap.put(pair.substring(0, idx), pair.substring(idx + 1));
            }
        }
        return queryMap;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public InputStream getBody() {
        return body;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getQueryParam(String key) {
        return queryParams.get(key);
    }
}
