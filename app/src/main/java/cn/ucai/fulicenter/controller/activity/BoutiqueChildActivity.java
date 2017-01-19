package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;

public class BoutiqueChildActivity extends AppCompatActivity {

    @BindView(R.id.iv_boutique_child_image)
    ImageView ivBoutiqueChildImage;
    @BindView(R.id.tv_boutique_child_textView)
    TextView tvBoutiqueChildTextView;
    @BindView(R.id.boutique_fragment_menu)
    FrameLayout boutiqueFragmentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        ButterKnife.bind(this);
        initData();
        initFragment();
    }

    private void initData() {
        String text = getIntent().getStringExtra(I.MERCHANT_NAME);
        tvBoutiqueChildTextView.setText(text);
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.boutique_fragment_menu, new NewGoodsFragment())
                .show(new NewGoodsFragment())
                .commit();
    }

    @OnClick(R.id.iv_boutique_child_image)
    public void onClick() {
        finish();
    }
}
