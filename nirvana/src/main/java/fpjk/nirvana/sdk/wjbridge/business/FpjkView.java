package fpjk.nirvana.sdk.wjbridge.business;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import fpjk.nirvana.sdk.wjbridge.R;
import fpjk.nirvana.sdk.wjbridge.business.entity.DataTransferEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.OpenUrlResponse;
import fpjk.nirvana.sdk.wjbridge.data.FpjkEnum;
import fpjk.nirvana.sdk.wjbridge.data.GsonMgr;
import fpjk.nirvana.sdk.wjbridge.data.RxBus;
import fpjk.nirvana.sdk.wjbridge.data.event.EventPageFinished;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.wjbridge.logger.L;
import fpjk.nirvana.sdk.wjbridge.presenter.WJBridgeWebView;
import rx.functions.Action1;

import static fpjk.nirvana.sdk.wjbridge.R.id.defaultWJBridgeWebView;
import static fpjk.nirvana.sdk.wjbridge.R.id.viewFlipper;

/**
 * Summary:
 * Created by Felix
 * Date: 13/12/2016
 * Time: 16:32
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class FpjkView extends RelativeLayout implements FpjkBusiness.ITabViewSwitcher {

    private ViewFlipper mViewFlipper;

    private WJBridgeWebView mDefaultWJBridgeWebView;

    private OpenUrlView mOpenUrlView;

    private WJCallbacks mWjCallbacks;

    public FpjkView(Context context) {
        super(context);
        build(context);
    }

    public FpjkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        build(context);
    }

    public FpjkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        build(context);
    }

    private void build(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_fpjk, null);

        mViewFlipper = (ViewFlipper) v.findViewById(viewFlipper);
        mDefaultWJBridgeWebView = (WJBridgeWebView) v.findViewById(defaultWJBridgeWebView);
        mOpenUrlView = (OpenUrlView) v.findViewById(R.id.openUrlView);

        mOpenUrlView.onBack(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenUrlResponse openUrlResponse = new OpenUrlResponse();
                openUrlResponse.setSuccess(FpjkEnum.OpenUrlStatus.USER_SHUTDOWN.getValue());
                String callBack = GsonMgr.get().toJSONString(openUrlResponse);
                mWjCallbacks.onCallback(callBack);
                showDefaultTab();
            }
        });

        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(-1, -1);
        addView(v, rl);
    }

    public WJBridgeWebView getDefaultWJBridgeWebView() {
        return mDefaultWJBridgeWebView;
    }

    public void loadDefaultUrl(String url) {
        mDefaultWJBridgeWebView.loadUrl(url);
    }

    private void showDefaultTab() {
        mViewFlipper.setDisplayedChild(0);
    }

    private void showNext() {
        mViewFlipper.setDisplayedChild(1);
    }

    @Override
    public void showOpenUrlTab(DataTransferEntity dataTransferEntity, WJCallbacks wjCallbacks) {
        mWjCallbacks = wjCallbacks;
        showNext();
        mOpenUrlView.recycle();
        mOpenUrlView.loadUrl(dataTransferEntity.getUrl());
        mOpenUrlView.setTitle(dataTransferEntity.getTitle());

        processCookieEvent(dataTransferEntity);
    }

    private void processCookieEvent(final DataTransferEntity dataTransferEntity) {
        RxBus.get().toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof EventPageFinished) {
                    String matchingUrl = ((EventPageFinished) o).getCurrentUrl();
                    if (matchingUrl.startsWith(dataTransferEntity.getFinishUrl())) {
                        OpenUrlResponse openUrlResponse = new OpenUrlResponse();
                        openUrlResponse.setSuccess(FpjkEnum.OpenUrlStatus.AUTO_SHUTDOWN.getValue());
                        String callBack = GsonMgr.get().toJSONString(openUrlResponse);
                        mWjCallbacks.onCallback(callBack);
                        showDefaultTab();
                        L.d("匹配到了指定URL，即将爆炸[%s]", o);
                    }
                }
            }
        });
    }
}
