package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public interface IModelCategory {
    void downGroupData(Context context, OnCompleteListener<CategoryGroupBean[]>listener);
    void downChildData(Context context,int parentId, OnCompleteListener<CategoryChildBean[]>listener);
}
