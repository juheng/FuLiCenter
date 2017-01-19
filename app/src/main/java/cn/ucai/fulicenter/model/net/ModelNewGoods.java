package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class ModelNewGoods implements IModelNewGoods{

    @Override
    public void downData(Context context, int catId, int pageId,OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]>utils=new OkHttpUtils<>(context);
        if(catId==0){

            utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                    .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(catId))
                    .addParam(I.PAGE_ID,String.valueOf(pageId))
                    .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                    .targetClass(NewGoodsBean[].class)
                    .execute(listener);
        }else{
            utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                    .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(catId))
                    .addParam(I.PAGE_ID,String.valueOf(pageId))
                    .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                    .targetClass(NewGoodsBean[].class)
                    .execute(listener);
        }
    }




    @Override
    public void downDetailData(Context context, int goodsId, OnCompleteListener<GoodsDetailsBean> listener) {
        OkHttpUtils<GoodsDetailsBean>utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,goodsId+"")
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }

    @Override
    public void isCollect(Context context, int goodsId, String username, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean>utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,goodsId+"")
                .addParam(I.Collect.USER_NAME,username)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void addCollect(Context context, int goodsId, String username,int action, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean>utils=new OkHttpUtils<>(context);
        String url=I.REQUEST_ADD_COLLECT;
        if(action==I.ACTION_DELETE_COLLECT){
            url=I.REQUEST_DELETE_COLLECT;
        }
        utils.setRequestUrl(url)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,goodsId+"")
                .addParam(I.Collect.USER_NAME,username)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void CollectCount(Context context, String username, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean>utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,username)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void getCollect(Context context, String username, int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener) {
        OkHttpUtils<CollectBean[]>utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME,String.valueOf(username))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CollectBean[].class)
                .execute(listener);
    }
}
