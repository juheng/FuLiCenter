package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.CenterFragment;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.SharePreferenceUtils;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.MFGT;

public class PersonalDataActivity extends AppCompatActivity {
    private static final String TAG = CenterFragment.class.getSimpleName();

    @BindView(R.id.iv_personal_avatar)
    ImageView ivPersonalAvatar;
    @BindView(R.id.tv_personal_name)
    TextView tvPersonalName;
    @BindView(R.id.tv_personal_nick)
    TextView tvPersonalNick;
    @BindView(R.id.bt_exit_login)
    Button btExitLogin;

    User user;
    OnSetAvatarListener onSetAvatarListener;

    IModelUser model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ButterKnife.bind(this);
        initData();
        DisplayUtils.initBackWithTitle(this, "个人资料");
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
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

    @OnClick(R.id.tv_personal_name)
    public void onNameClick() {
        CommonUtils.showLongToast(R.string.username_connot_be_modify);
    }

    @OnClick(R.id.iv_personal_avatar)
    public void updateAvatar() {
        L.e(TAG, "onClick..........");
        onSetAvatarListener = new OnSetAvatarListener(this, R.id.iv_personal_avatar, user.getMuserName()
                , I.AVATAR_TYPE_USER_PATH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e(TAG, "requestCode===========" + requestCode + ",resultCode=" + resultCode + ",data=" + data);
        if (resultCode != RESULT_OK) {
            return;
        }else
        if (requestCode == I.REQUEST_CODE_NICK) {
            tvPersonalNick.setText(FuLiCenterApplication.getUser().getMuserNick());
        } else if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            uploadAvatar();
        }else{

            onSetAvatarListener.setAvatar(requestCode, data, ivPersonalAvatar);
        }

    }

    private void uploadAvatar() {
        final User user = FuLiCenterApplication.getUser();
        L.e(TAG, "uploadAvatar....user==========" + user);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.update_user_avatar));
        dialog.show();
        model = new ModelUser();
        File file = null;
        file = new File(String.valueOf(OnSetAvatarListener.getAvatarFile(this,
                I.AVATAR_TYPE_USER_PATH + "/" + user.getMuserName()
                        + user.getMavatarSuffix())));
        L.e(TAG, "file==========" + file.getAbsolutePath());
        model.updateAvatar(this, user.getMuserName()
                , file, new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(Object result) {
                        String result1 = (String) result;
                        L.e(TAG, "result===========" + result);
                        int msg = R.string.update_user_avatar_fail;
                        if (result1 != null) {
                            Result result2 = ResultUtils.getResultFromJson(result1, User.class);
                            if (result2 != null) {
                                if (result2 != null) {
                                    if (result2.isRetMsg()) {
                                        User user= (User) result2.getRetData();
                                        saveNewUser(user);
                                        msg = R.string.update_user_avatar_success;
                                    }
                                }
                            }
                            CommonUtils.showLongToast(msg);
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showLongToast(error);
                        dialog.dismiss();
                        L.e(TAG, "error===========" + error);
                    }
                });

    }

    private void saveNewUser(User user) {
        FuLiCenterApplication.user=user;
    }
}