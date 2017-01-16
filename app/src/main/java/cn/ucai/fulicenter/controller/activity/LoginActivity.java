package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.SharePreferenceUtils;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

public class LoginActivity extends AppCompatActivity {
    static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.iv_username)
    ImageView ivUsername;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.rl_username)
    RelativeLayout rlUsername;
    @BindView(R.id.iv_password)
    ImageView ivPassword;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.rl_password)
    RelativeLayout rlPassword;
    @BindView(R.id.bt_Url)
    Button btUrl;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;

    IModelUser model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_login_Return, R.id.bt_login, R.id.bt_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_login_Return:
                break;
            case R.id.bt_login:
                checkInput();
                break;
            case R.id.bt_register:
                MFGT.startActivity(this, RegisterActivity.class);
                break;
        }
    }

    private void checkInput() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            etUsername.setError(getResources().getString(R.string.user_name_connot_be_empty));
            etUsername.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etPassword.setError(getResources().getString(R.string.password_connot_be_empty));
            etPassword.requestFocus();
        } else {
            login(username, password);
        }
    }

    private void login(String username, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.login));
        model = new ModelUser();
        model.login(this, username, password, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(Object result) {
                String result1 = (String) result;
                Result result2 = ResultUtils.getListResultFromJson(result1, User.class);
                L.e(TAG,"result="+result2);
                if (result2 != null) {
                    if (result2.isRetMsg()) {
                        CommonUtils.showLongToast(R.string.logining);
                        User user= (User) result2.getRetData();
                        SharePreferenceUtils.getInstance(LoginActivity.this).saveUser(user.getMuserName());
                        MFGT.finish(LoginActivity.this);
                    } else {
                        if (result2.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                            CommonUtils.showLongToast(getString(R.string.login_fail_unknow_user));
                        }
                        if (result2.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                            CommonUtils.showLongToast(getString(R.string.login_fail_error_password));
                        }
                    }
                } else {
                    CommonUtils.showLongToast(R.string.login_fail);
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "error" + error);
            }
        });
    }
}
