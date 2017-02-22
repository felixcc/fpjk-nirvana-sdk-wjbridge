/*
 * Copyright 2015 Soulwolf Ching
 * Copyright 2015 The Android Open Source Project for WebViewJavascriptBridge
 * 
 * Licens ed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fpjk.nirvana.sdk.wjbridge.presenter;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import fpjk.nirvana.sdk.wjbridge.data.RxBus;
import fpjk.nirvana.sdk.wjbridge.data.event.EventPageReceivedFinished;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeProvider;
import fpjk.nirvana.sdk.wjbridge.logger.L;

public class WJBridgeWebViewClient extends WebViewClient {

    private WJBridgeProvider mProvider;

    public WJBridgeWebViewClient(WJBridgeProvider provider) {
        this.mProvider = provider;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        L.d("onPageStarted");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return mProvider.shouldOverrideUrlLoading(mProvider.getLoader(), url)
                || super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        this.mProvider.onPageFinished();
        RxBus.get().send(new EventPageReceivedFinished().setCurrentUrl(url));
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        L.d("onReceivedError");
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        L.d("onReceivedError-deprecated");
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        L.d("onReceivedHttpError");
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        L.d("onReceivedSslError");
    }
}
