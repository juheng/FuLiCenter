package cn.ucai.fulicenter.controller.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;
import cn.ucai.fulicenter.view.CatFilterButton;

public class CategoryDetailActivity extends AppCompatActivity {


    IModelNewGoods model;
    @BindView(R.id.iv_category_detail_back)
    ImageView ivCategoryDetailBack;

    NewGoodsFragment mNewGoodsFragment;

    boolean flag1 = true;
    boolean flag2 = true;
    @BindView(R.id.category_detail_addTime)
    Button categoryDetailAddTime;
    @BindView(R.id.category_detail_price)
    Button categoryDetailPrice;
   @BindView(R.id.catButton_category)
    CatFilterButton catButtonCategory;


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
        mNewGoodsFragment = new NewGoodsFragment();
        ft.add(R.id.category_fragment_menu, mNewGoodsFragment)
                .show(mNewGoodsFragment)
                .commit();
    }


    private void initData() {
        String name = getIntent().getStringExtra("CategoryChildBean_name");
        catButtonCategory.setText(name);
        ArrayList<CategoryChildBean>list= (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra("Cat_Filter_List");
        catButtonCategory.initCatFilterButton(name,list);
    }



    /*private void showImage1(boolean flag) {
        ivUp.setImageResource(flag ? R.drawable.arrow_order_down : R.drawable.arrow_order_up);

    }

    private void showImage2(boolean flag) {
        ivDown.setImageResource(flag ? R.drawable.arrow_order_down : R.drawable.arrow_order_up);
    }*/


    @OnClick({R.id.iv_category_detail_back,R.id.catButton_category,R.id.category_detail_addTime, R.id.category_detail_price})
    public void onClick(View view) {
        Drawable right;
        switch (view.getId()) {
            case R.id.iv_category_detail_back:
                this.finish();
                break;
            case R.id.catButton_category:
                break;
            case R.id.category_detail_addTime:
                if (flag1) {
                    mNewGoodsFragment.sortGoods(I.SORT_BY_ADDTIME_ASC);
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                } else {
                    mNewGoodsFragment.sortGoods(I.SORT_BY_ADDTIME_DESC);
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                categoryDetailAddTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                flag1 = !flag1;
                break;
            case R.id.category_detail_price:
                if (flag2) {
                    mNewGoodsFragment.sortGoods(I.SORT_BY_PRICE_ASC);
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                } else {
                    mNewGoodsFragment.sortGoods(I.SORT_BY_PRICE_DESC);
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                categoryDetailPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                flag2 = !flag2;
                break;
        }
    }

}