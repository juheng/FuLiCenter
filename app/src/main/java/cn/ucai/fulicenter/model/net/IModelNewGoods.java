package cn.ucai.fulicenter.model.net;

import android.content.Context;
import android.media.MediaPlayer;

import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public interface IModelNewGoods {
    void  downData(Context context, int catId, int pageId,OnCompleteListener<NewGoodsBean[]> listener);
}
