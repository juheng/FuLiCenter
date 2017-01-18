package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
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
    @BindView(R.id.bt_exit_login)
    Button btExitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ButterKnife.bind(this);
        initData();
        DisplayUtils.initBackWithTitle(this, "个人资料");
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

    @OnClick(R.id.bt_exit_login)
    public void onClick() {
        FuLiCenterApplication.setUser(null);
        SharePreferenceUtils.getInstance(this).removeUser();
        MFGT.gotoLoginActivity(this);
        finish();
    }

    @OnClick(R.id.tv_personal_nick)
    public void updateNick() {
        MFGT.gotoUpdateNickActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_NICK) {
            tvPersonalNick.setText(FuLiCenterApplication.getUser().getMuserNick());
        }
    }

}