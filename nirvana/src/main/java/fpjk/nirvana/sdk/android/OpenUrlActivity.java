package fpjk.nirvana.sdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import fpjk.nirvana.sdk.android.business.DataTransferEntity;
import fpjk.nirvana.sdk.wjbridge.R;

/**
 * Summary:
 * Created by FelixChen
 * Created 2016-12-13 01:04
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */

public class OpenUrlActivity extends Activity {
    public static final String EXTRA_KEY = "EXTRA_KEY";

    private DataTransferEntity mDataTransferEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_open_url);

        mDataTransferEntity = getIntent().getParcelableExtra(EXTRA_KEY);
        if (null == mDataTransferEntity) {
            finish();
        }
    }

    private void build(DataTransferEntity dataTransferEntity) {
    }

}
