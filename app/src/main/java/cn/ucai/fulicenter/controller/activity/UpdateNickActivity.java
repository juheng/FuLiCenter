package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.MFGT;

public class UpdateNickActivity extends AppCompatActivity {
    private static String TAG=UpdateNickActivity.class.getSimpleName();

    @BindView(R.id.et_updateNick_nick)
    EditText etUpdateNickNick;
    @BindView(R.id.bt_updateNick_save)
    Button btUpdateNickSave;

    IModelUser model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        model = new ModelUser();
        initData();
        DisplayUtils.initBackWithTitle(this, "更新昵称");
    }

    private void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
           etUpdateNickNick.setText(user.getMuserNick());
        } else {
            MFGT.finish(this);
        }
    }
    @OnClick(R.id.bt_updateNick_save)
    public void onClick() {
        String nick = etUpdateNickNick.getText().toString().trim();
        if(TextUtils.isEmpty(nick)){
            etUpdateNickNick.setError(getResources().getString(R.string.nick_name_connot_be_empty));
        }else if(FuLiCenterApplication.getUser().getMuserNick().equals(nick)){
            etUpdateNickNick.setError(getResources().getString(R.string.update_nick_fail_unmodify));
        }else{
            loadUserInfo(FuLiCenterApplication.getUser(),nick);
        }
    }
    private void loadUserInfo(User user,String nick) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.update_user_nick));
        dialog.show();
        model.updateNick(this, user.getMuserName(), nick, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(Object result) {
                int msg = R.string.update_fail;
                String result1 = (String) result;
                if (result1 != null) {
                    Result result2 = ResultUtils.getResultFromJson(result1, User.class);
                    if (result2 != null) {
                        if (result2.isRetMsg()) {
                            msg = R.string.update_user_nick_success;
                            User user= (User) result2.getRetData();
                            L.e(TAG,"update success,user="+user);
                            saveNewUser(user);
                            setResult(RESULT_OK);
                            finish();

                        } else {
                            if (result2.getRetCode() == I.MSG_USER_SAME_NICK ||
                                    result2.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL) {
                                msg = R.string.update_nick_fail_unmodify;
                            }
                        }
                    }
                }
                CommonUtils.showLongToast(msg);
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(R.string.update_fail);
                dialog.dismiss();
                L.e(TAG,"error="+error);
            }
        });
    }

    private void saveNewUser(User user) {
        FuLiCenterApplication.user=user;
    }

}
