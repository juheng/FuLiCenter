package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.view.MFGT;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_setting_back, R.id.bt_returnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting_back:
                MFGT.finish(this);
                break;
            case R.id.bt_returnLogin:
                MFGT.gotoLoginActivity(this);
                break;
        }
    }
}
