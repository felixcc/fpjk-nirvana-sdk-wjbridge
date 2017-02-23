package fpjk.nirvana.sdk;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fpjk.nirvana.sdk.wjbridge.business.FpjkView;
import fpjk.nirvana.sdk.wjbridge.business.FpjkWJSDKMgr;
import fpjk.nirvana.sdk.wjbridge.business.IReceiveLogoutAction;
import fpjk.nirvana.sdk.wjbridge.logger.L;

public class MainActivity extends Activity {

    FpjkView mFpjkView;

    String url = "http://10.10.232.242:9527/";//首页
    //    String url = "http://10.10.191.154:9000/";//测试
    String cookie = "session=Fe26.2**1f37371601fa6b359b162bac2a33c9716c3687b76813182d68c1f26ee6fd17bd*nIrCqGQf02fWc8CPOefnpw*Z7JJ1og7tGX32tHBMjV1WpVsvbkpNnFMDe8NSVqECqw**bab0966f76d0a6d653f0aa90ab13306c4b81845f6b49b671bc617ddf2d1e9140*COwRICQcZL93DHcoqpcxAco32t6qXz-Mw17vhAq85e4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFpjkView = (FpjkView) findViewById(R.id.fpjkView);

        FpjkWJSDKMgr.get()
                .setActivity(this)
                .setFpjkView(mFpjkView)
                .setShownBackButton(false)
                .execute();

//        FpjkWJSDKMgr.get().loadUrl(url);
        FpjkWJSDKMgr.get().insertCookie(url, cookie);

        FpjkWJSDKMgr.get().logout(new IReceiveLogoutAction() {
            @Override
            public void onReceive() {
                L.d("Logout=====================================");
            }
        });
    }

    public void debugEnabled() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入新地址...");
        //通过布局填充器获login_layout
        View view = getLayoutInflater().inflate(R.layout.layout_debug_dialog, null);

        //获取两个文本编辑框（密码这里不做登陆实现，仅演示）
        final TextView tvCurrentUrl = (TextView) view.findViewById(R.id.tvCurrentUrl);
        tvCurrentUrl.setText("当前地址是：" + url);
        final EditText etUrl = (EditText) view.findViewById(R.id.etUrl);
        etUrl.setText(url);

        builder.setView(view);//设置login_layout为对话提示框
        builder.setCancelable(false);//设置为不可取消
        //设置正面按钮，并做事件处理
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                url = etUrl.getText().toString().trim();
                FpjkWJSDKMgr.get().loadUrl("about:blank");
                FpjkWJSDKMgr.get().loadUrl(url);
                Toast.makeText(MainActivity.this, "切换了新地址" + url, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();//显示Dialog对话框
    }

}
