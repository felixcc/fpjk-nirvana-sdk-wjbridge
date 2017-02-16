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

import android.webkit.WebView;
import android.webkit.WebViewClient;

import fpjk.nirvana.sdk.wjbridge.data.RxBus;
import fpjk.nirvana.sdk.wjbridge.data.event.EventPageFinished;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeProvider;

public class WJBridgeWebViewClient extends WebViewClient {

    private WJBridgeProvider mProvider;

    public WJBridgeWebViewClient(WJBridgeProvider provider) {
        this.mProvider = provider;
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
        RxBus.get().send(new EventPageFinished().setCurrentUrl(url));
    }
}
