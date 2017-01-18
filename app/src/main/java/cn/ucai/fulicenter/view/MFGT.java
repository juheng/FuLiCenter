package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.controller.activity.CategoryDetailActivity;
import cn.ucai.fulicenter.controller.activity.LoginActivity;
import cn.ucai.fulicenter.controller.activity.MainActivity;
import cn.ucai.fulicenter.controller.activity.NewGoodsDetailActivity;
import cn.ucai.fulicenter.controller.activity.PersonalDataActivity;
import cn.ucai.fulicenter.controller.activity.SettingActivity;
import cn.ucai.fulicenter.controller.activity.UpdateNickActivity;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.OnCompleteListener;

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

    public static void gotoCategoryDetail(Context context, CategoryChildBean categoryChildBean, ArrayList<CategoryChildBean> categoryChildList) {
        Intent intent=new Intent(context, CategoryDetailActivity.class);
        intent.putExtra(I.NewAndBoutiqueGoods.CAT_ID, categoryChildBean.getId());
        intent.putExtra("CategoryChildBean_name", categoryChildBean.getName());
        intent.putExtra("Cat_Filter_List",categoryChildList);
        MFGT.startActivity(context,intent);
    }

    public static void gotoLoginActivity(Activity context) {
        context.startActivityForResult(new Intent(context,LoginActivity.class),I.REQUEST_CODE_LOGIN );
    }

    public static void gotoSettingActivity(Context context) {
        startActivity((Activity) context, SettingActivity.class);
    }

    public static void gotoPersonalDataActivity(Context context) {
        startActivity((Activity) context, PersonalDataActivity.class);

    }

    public static void gotoUpdateNickActivity(Activity activity) {
        activity.startActivityForResult(new Intent(activity, UpdateNickActivity.class),I.REQUEST_CODE_NICK);
    }
}
