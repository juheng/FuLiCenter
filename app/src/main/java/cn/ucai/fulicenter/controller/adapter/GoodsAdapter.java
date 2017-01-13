package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.MainActivity;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    MainActivity context;
    ArrayList<NewGoodsBean> goodsList;
    boolean isMore;
    String footer;

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public GoodsAdapter(Context mContext, ArrayList<NewGoodsBean> list) {
        this.mContext = mContext;
        this.goodsList = new ArrayList<>();
        goodsList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout;
        if (viewType == I.TYPE_FOOTER) {
            layout = inflater.inflate(R.layout.item_footer, null);
            return new FooterViewHolder(layout);
        } else {
            layout = inflater.inflate(R.layout.item_goods, null);
            return new GoodsViewHolder(layout);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, final int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder holder = (FooterViewHolder) parentHolder;
            holder.tvFooter.setText(getFooter());
            return;
        }
        GoodsViewHolder vh = (GoodsViewHolder) parentHolder;
        ImageLoader.downloadImg(mContext, vh.ivGoodsThumb, goodsList.get(position).getGoodsThumb());
        vh.tvGoodsName.setText(goodsList.get(position).getGoodsName());
        vh.tvGoodsPrice.setText(goodsList.get(position).getCurrencyPrice());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFGT.gotoNewGoodsDetail(mContext, goodsList.get(position));
            }
        });
    }


    public void sortGoods(final int sortBy) {
        Collections.sort(goodsList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean leftBean, NewGoodsBean rightBean) {
                int result = 0;
                switch (sortBy) {
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (leftBean.getAddTime() - rightBean.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (rightBean.getAddTime() - leftBean.getAddTime());
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(leftBean.getCurrencyPrice()) - getPrice(rightBean.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(rightBean.getCurrencyPrice()) - getPrice(leftBean.getCurrencyPrice());
                        break;

                }
                return result;
            }
        });
        notifyDataSetChanged();
    }

    int getPrice(String price) {
        int p = 0;
        p = Integer.valueOf(price.substring(price.indexOf("￥")+1));
        return p;
    }

    @Override
    public int getItemCount() {
        return goodsList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initData(ArrayList<NewGoodsBean> list) {
        if (goodsList != null) {

            this.goodsList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<NewGoodsBean> list) {
        goodsList.addAll(list);
        notifyDataSetChanged();
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_footer)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
