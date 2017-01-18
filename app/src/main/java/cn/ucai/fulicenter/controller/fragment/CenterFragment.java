package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.view.MFGT;

/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends Fragment {
    private static final String TAG = CenterFragment.class.getSimpleName();
    @BindView(R.id.iv_center_avatar)
    ImageView ivCenterAvatar;
    @BindView(R.id.tv_center_name)
    TextView tvCenterName;
    @BindView(R.id.tv_count)
    TextView tvCount;

    IModelNewGoods model;

    public CenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_center, container, false);
        model=new ModelNewGoods();
        ButterKnife.bind(this, layout);
        initData();
        getCount();
        return layout;
    }

    private void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            loadUserInfo(user);
            getCount();
        } else {
            MFGT.gotoLoginActivity(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       initData();
    }

    private void getCount() {
        model.CollectCount(getActivity(), FuLiCenterApplication.getUser().getMuserName(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(Object result) {
                MessageBean result1= (MessageBean) result;
                L.e(TAG,"result1======="+result1);
                L.e(TAG,"000000000000"+result1.getMsg());
                if(result1!=null&&result1.isSuccess()){
                    loadCount(result1.getMsg());
                }
            }

            @Override
            public void onError(String error) {
                loadCount("0");
            }
        });

    }

    private void loadUserInfo(User user) {
        L.e(TAG, user.toString());
        ImageLoader.downloadImg(getActivity(), ivCenterAvatar, user.getAavatarPath());
        tvCenterName.setText(user.getMuserNick());
        loadCount("0");
    }

    private void loadCount(String count) {
        tvCount.setText(count);
    }

    @OnClick({R.id.tv_center_set, R.id.tv_center_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_center_set:
                MFGT.gotoSettingActivity(getActivity());
                break;
            case R.id.tv_center_name:
                MFGT.gotoPersonalDataActivity(getActivity());
                break;
        }
    }

}
