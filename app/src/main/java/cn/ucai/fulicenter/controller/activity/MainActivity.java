package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.controller.fragment.CategoryFragment;
import cn.ucai.fulicenter.controller.fragment.CenterFragment;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.view.MFGT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    int index, currentIndex;
    RadioButton[] rbs = new RadioButton[5];
    Fragment[] mFragements;

    @BindView(R.id.layout_new_good)
    RadioButton layoutNewGood;
    @BindView(R.id.layout_boutique)
    RadioButton layoutBoutique;
    @BindView(R.id.layout_category)
    RadioButton layoutCategory;
    @BindView(R.id.layout_cart)
    RadioButton layoutCart;
    @BindView(R.id.layout_personal_center)
    RadioButton layoutPersonalCenter;

    FuLiCenterApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        application = new FuLiCenterApplication();

        rbs[0] = layoutNewGood;
        rbs[1] = layoutBoutique;
        rbs[2] = layoutCategory;
        rbs[3] = layoutCart;
        rbs[4] = layoutPersonalCenter;

        initFragment();
    }

    private void initFragment() {
        mFragements = new Fragment[5];
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        mFragements[0] = new NewGoodsFragment();
        mFragements[1] = new BoutiqueFragment();
        mFragements[2] = new CategoryFragment();
        mFragements[4] = new CenterFragment();
        ft.add(R.id.layout_menu, mFragements[0]).commit();
    }


    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
                break;
            case R.id.layout_category:
                index = 2;
                break;
            case R.id.layout_cart:
                index = 3;
                break;
            case R.id.layout_personal_center:
                if (application.getUser() == null) {
                    MFGT.gotoLoginActivity(this);
                } else {
                    index = 4;
                }
                break;
        }
        if (currentIndex != index) {
            setFragment();
            setRadioStatus();
        }

    }

    private void setFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (!mFragements[index].isAdded()) {
            ft.add(R.id.layout_menu, mFragements[index]);
        }
        ft.show(mFragements[index])
                .hide(mFragements[currentIndex]);
        ft.commit();
    }

    private void setRadioStatus() {
        for (int i = 0; i < rbs.length; i++) {
            if (index != i) {
                rbs[i].setChecked(false);
            } else {
                rbs[i].setChecked(true);
            }
            currentIndex = index;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e("onResume,currentIndex=" + currentIndex + ",index=" + index +
                ",user=" + FuLiCenterApplication.getUser());
        if (index == 4 && FuLiCenterApplication.getUser() == null) {
            index = 0;
            setFragment();
        }
        setRadioStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e(TAG, "onActivtiyResult,resultCode=" + resultCode + ",requestCode=" + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_LOGIN) {
            index = 4;
            setFragment();
            setRadioStatus();
        }
    }


}
