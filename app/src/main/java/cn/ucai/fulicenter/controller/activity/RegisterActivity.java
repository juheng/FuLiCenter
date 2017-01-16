package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

public class RegisterActivity extends AppCompatActivity {
    static final String TAG=RegisterActivity.class.getSimpleName();

    @BindView(R.id.et_register_username)
    EditText etRegisterUsername;
    @BindView(R.id.et_register_nick)
    EditText etRegisterNick;
    @BindView(R.id.et_register_password)
    EditText etRegisterPassword;
    @BindView(R.id.et_ConfirmPassword)
    EditText etConfirmPassword;

    IModelUser model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_register_return, R.id.bt_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_register_return:
                MFGT.finish(this);
                break;
            case R.id.bt_register:
                checkInput();
                break;
        }
    }

    private void checkInput() {
        String username = etRegisterUsername.getText().toString().trim();
        String usernick = etRegisterNick.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String confim = etConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            etRegisterUsername.setError(getResources().getString(R.string.user_name_connot_be_empty));
            etRegisterUsername.requestFocus();
        } else if (!username.matches("[a-zA-Z]\\w{5,15}")) {
            etRegisterUsername.setError(getResources().getString(R.string.illegal_user_name));
            etRegisterUsername.requestFocus();
        } else if (TextUtils.isEmpty(usernick)) {
            etRegisterNick.setError(getResources().getString(R.string.nick_name_connot_be_empty));
            etRegisterUsername.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etRegisterPassword.setError(getResources().getString(R.string.password_connot_be_empty));
            etRegisterPassword.requestFocus();
        } else if (TextUtils.isEmpty(confim)) {
            etConfirmPassword.setError(getResources().getString(R.string.confirm_password_connot_be_empty));
            etConfirmPassword.requestFocus();
        } else if (!password.equals(confim)) {
            etConfirmPassword.setError(getResources().getString(R.string.two_input_password));
            etConfirmPassword.requestFocus();
        } else {
            register(username, usernick, password);
        }
    }

    private void register(String username, String usernick, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.registering));
        model = new ModelUser();
        model.register(this, username, usernick, password, new OnCompleteListener<String>() {

            @Override
            public void onSuccess(Object result) {
                if (result != null) {
                    String result1= (String) result;
                    Result result2 = ResultUtils.getListResultFromJson(result1, User.class);
                    L.e(TAG,"result="+result2);
                    if (result2 != null) {
                        if (result2.isRetMsg()) {
                            CommonUtils.showLongToast(R.string.register_success);
                            L.e(result2.toString());
                            MFGT.finish(RegisterActivity.this);
                        } else {
                            CommonUtils.showLongToast(R.string.register_fail_exists);
                        }
                    } else {
                        CommonUtils.showLongToast(R.string.register_fail);
                    }
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                CommonUtils.showLongToast(error);
            }
        });
    }
}
