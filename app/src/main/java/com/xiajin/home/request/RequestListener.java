package com.xiajin.home.request;

import com.xiajin.home.request.RequestData.Action;




public interface RequestListener {
	/**
     * The CALLBACK for success aMarket API HTTP response
     * 
     * @param response
     *        the HTTP response
     */
    void onSuccess(Action method, Object obj);

    /**
     * The CALLBACK for failure aMarket API HTTP response
     * 
     * @param statusCode
     *            the HTTP response status code
     */
    void onError(Action method, Object statusCode);
}
