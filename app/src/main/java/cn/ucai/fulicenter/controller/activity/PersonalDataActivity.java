package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.SharePreferenceUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.MFGT;

public class PersonalDataActivity extends AppCompatActivity {

    @BindView(R.id.iv_personal_avatar)
    ImageView ivPersonalAvatar;
    @BindView(R.id.tv_personal_name)
    TextView tvPersonalName;
    @BindView(R.id.tv_personal_nick)
    TextView tvPersonalNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ButterKnife.bind(this);
        initData();
        DisplayUtils.initBackWithTitle(this,"个人资料");
    }

    private void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            loadUserInfo(user);
        } else {
            MFGT.gotoLoginActivity(this);
        }
    }

    private void loadUserInfo(User user) {
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), this, ivPersonalAvatar);
        tvPersonalName.setText(user.getMuserName());
        tvPersonalNick.setText(user.getMuserNick());
    }

    @OnClick({R.id.iv_setting_back, R.id.bt_exit_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting_back:
                MFGT.finish(this);
                break;
            case R.id.bt_exit_login:
                FuLiCenterApplication.setUser(null);
                SharePreferenceUtils.getInstance(this).removeUser();
                MFGT.gotoLoginActivity(this);
                finish();
                break;
        }
    }
    @OnClick(R.id.tv_personal_nick)
    public void updateNick(){
        String nick=tvPersonalNick.getText().toString().trim();
        if(TextUtils.isEmpty(nick)){

        }
    }
}
