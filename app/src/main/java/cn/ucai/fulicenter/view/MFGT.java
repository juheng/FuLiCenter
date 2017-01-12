package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.controller.activity.NewGoodsDetailActivity;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/1/10 0010.
 */
public class MFGT {

    public static void finish(Activity context, Class<?> clz){
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
        context.finish();
    }
    public static void finish(Activity context){
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
        context.finish();
    }
    public static void startActivity(Activity context, Class<?> clz){
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        context.startActivity(new Intent(context,clz));
    }
    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
    }

    public static void gotoBoutiqueChild(Activity mContext, BoutiqueBean boutiqueBean) {
        Intent intent=new Intent(mContext,BoutiqueChildActivity.class);
        intent.putExtra(I.NewAndBoutiqueGoods.CAT_ID,boutiqueBean.getId());
        intent.putExtra(I.MERCHANT_NAME,boutiqueBean.getName());
        MFGT.startActivity(mContext,intent);
    }

    public static void gotoNewGoodsDetail(Context mContext, NewGoodsBean newGoodsBean) {
        Intent intent=new Intent(mContext, NewGoodsDetailActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,newGoodsBean.getGoodsId());
        MFGT.startActivity(mContext,intent);
    }
}
