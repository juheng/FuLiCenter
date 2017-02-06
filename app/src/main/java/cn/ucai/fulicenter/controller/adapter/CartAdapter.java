package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.view.MFGT;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/1/20 0020.
 */

public class CartAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<CartBean> mList;
    int itemPosition;
    IModelUser model=new ModelUser();
    User user=FuLiCenterApplication.getUser();



    public CartAdapter(Context context, ArrayList<CartBean> list) {
        this.context = context;
        this.mList =list;
        L.e(TAG, "list111====" + mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_cart, null);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CartViewHolder vh = (CartViewHolder) holder;
        vh.bind(position);

    }


    @Override
    public int getItemCount() {
        return mList!=null?mList.size():0;
    }

    public void initData(ArrayList<CartBean> list) {
        if(list!=null){
            list.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<CartBean> list) {
        this.mList.addAll(list);
        L.e(TAG, "list222===" + list);
        notifyDataSetChanged();
    }


    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_checkbox)
        CheckBox ivCheckbox;
        @BindView(R.id.iv_goodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tv_goodsName)
        TextView tvGoodsName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_cartCount)
        TextView tvCartCount;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            GoodsDetailsBean goodsDetailsBean=mList.get(position).getGoods();
            if (goodsDetailsBean != null) {
                ImageLoader.downloadImg(context, ivGoodsThumb, goodsDetailsBean.getGoodsThumb());
                tvGoodsName.setText(goodsDetailsBean.getGoodsName());
                tvPrice.setText(goodsDetailsBean.getCurrencyPrice());
            }
            tvCartCount.setText("("+mList.get(position).getCount()+")");
            ivCheckbox.setChecked(mList.get(itemPosition).isChecked());
            itemPosition = position;
        }
       /* @OnClick(R.id.iv_goodsThumb)
        public void onClick() {
            MFGT.gotoNewGoodsDetail(context, mList.get(itemPosition).getGoods());
        }*/
        @OnCheckedChanged(R.id.iv_checkbox)
        public void checkListener(boolean checked){
            mList.get(itemPosition).setChecked(checked);
            context.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));

        }
        @OnClick(R.id.iv_addCart)
        public void addCart(){
            model.updateCart(context, I.ACTION_ADD_CART, user.getMuserName(),
                    mList.get(itemPosition).getGoodsId(), 1, mList.get(itemPosition).getId(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(Object result) {
                            MessageBean result1= (MessageBean) result;
                            if(result1!=null&&result1.isSuccess()){
                                mList.get(itemPosition).setCount(mList.get(itemPosition).getCount()+1);
                                context.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                            }

                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
        @OnClick(R.id.iv_deleteCount)
        public void deleteCount() {
            final int count=mList.get(itemPosition).getCount();
            L.e(TAG,"count===="+count);
            int action=I.ACTION_UPDATE_CART;
            if(count>1){
                action=I.ACTION_UPDATE_CART;
            }else{
                action=I.ACTION_DELETE_CART;
            }
            model.updateCart(context, action, user.getMuserName(),
                    mList.get(itemPosition).getGoodsId(), mList.get(itemPosition).getId(), count-1,
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(Object result) {
                            MessageBean result1= (MessageBean) result;
                            L.e(TAG,"result==="+result1.isSuccess());
                            if(result1!=null&&result1.isSuccess()){
                                if(count<=1){
                                    mList.remove(itemPosition);
                                    notifyDataSetChanged();
                                }else{

                                    mList.get(itemPosition).setCount(count-1);
                                }
                                context.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                            }

                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }
}
