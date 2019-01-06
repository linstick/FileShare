package com.luoruiyong.fileshare.bean;

public class PacketContent {

    public static final String FUNCTION_REQUEST_SEARCH_SHARE_HOST = "request_search_share_host";
    public static final String FUNCTION_REQUEST_SEARCH_SHARE_FILES = "request_search_share_files";
    public static final String FUNCTION_RESPONSE_SEARCH_SHARE_HOST = "response_search_share_host";
    public static final String FUNCTION_RESPONSE_SEARCH_SHARE_FILES = "response_search_share_files";

    private String mFunction;
    private String mMessage;

    public String getFunction() {
        return mFunction;
    }

    public void setFunction(String function) {
        this.mFunction = function;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }
}
