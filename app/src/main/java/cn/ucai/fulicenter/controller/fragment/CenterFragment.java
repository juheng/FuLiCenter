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
import cn.ucai.fulicenter.model.bean.User;
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


    public CenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_center, container, false);
        ButterKnife.bind(this, layout);
        initData();
        return layout;
    }

    private void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            loadUserInfo(user);
        } else {
            MFGT.gotoLoginActivity(getActivity());
        }
    }

    private void loadUserInfo(User user) {
        L.e(TAG, user.toString());
        ImageLoader.downloadImg(getActivity(), ivCenterAvatar, user.getAavatarPath());
        tvCenterName.setText(user.getMuserNick());
    }

    @OnClick({R.id.tv_center_set,R.id.tv_center_name})
    public void onClick() {
        MFGT.gotoSettingActivity(getActivity());
    }
}
