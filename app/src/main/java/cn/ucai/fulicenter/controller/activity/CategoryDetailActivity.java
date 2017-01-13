package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;

public class CategoryDetailActivity extends AppCompatActivity {


    IModelNewGoods model;
    @BindView(R.id.iv_category_detail_back)
    ImageView ivCategoryDetailBack;
    @BindView(R.id.tv_category_detail_textView)
    TextView tvCategoryDetailTextView;
    @BindView(R.id.iv_category_detail_image)
    ImageView ivCategoryDetailImage;

    NewGoodsFragment mNewGoodsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        ButterKnife.bind(this);
        model = new ModelNewGoods();
        initData();
        initFragment();
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        mNewGoodsFragment=new NewGoodsFragment();
        ft.add(R.id.category_fragment_menu, mNewGoodsFragment)
                .show(mNewGoodsFragment)
                .commit();
    }


    private void initData() {
        String name = getIntent().getStringExtra("CategoryChildBean_name");
        tvCategoryDetailTextView.setText(name);
    }

    @OnClick(R.id.iv_category_detail_back)
    public void onClick() {
        this.finish();
    }

    @OnClick({R.id.iv_category_detail_back, R.id.category_detail_price, R.id.category_detail_addTime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_category_detail_back:
                this.finish();
                break;
            case R.id.category_detail_price:
                mNewGoodsFragment.sortGoods(I.SORT_BY_PRICE_ASC);
                break;
            case R.id.category_detail_addTime:
                mNewGoodsFragment.sortGoods(I.SORT_BY_ADDTIME_ASC);
                break;
        }
    }
}
