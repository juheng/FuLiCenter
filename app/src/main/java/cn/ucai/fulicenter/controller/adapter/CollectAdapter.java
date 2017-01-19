package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.MainActivity;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class CollectAdapter extends RecyclerView.Adapter{
        Context mContext;
        ArrayList<CollectBean> goodsList;
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

        public CollectAdapter(Context mContext, ArrayList<CollectBean> list) {
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
                layout = inflater.inflate(R.layout.item_collect_goods, null);
                return new CollectViewHolder(layout);
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, final int position) {
            if (getItemViewType(position) == I.TYPE_FOOTER) {
                FooterViewHolder holder= (FooterViewHolder) parentHolder;
                holder.tvFooter.setText(getFooter());
                return;
            }
            CollectViewHolder vh = (CollectViewHolder) parentHolder;
            ImageLoader.downloadImg(mContext, vh.ivGoodsThumb, goodsList.get(position).getGoodsThumb());
            vh.tvGoodsName.setText(goodsList.get(position).getGoodsName());
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MFGT.gotoNewGoodsDetail(mContext, goodsList.get(position));
                }
            });
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

        public void initData(ArrayList<CollectBean> list) {
            if (goodsList != null) {

                this.goodsList.clear();
            }
            addData(list);
        }

        public void addData(ArrayList<CollectBean> list) {
            goodsList.addAll(list);
            notifyDataSetChanged();
        }

        static class CollectViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_GoodsThumb)
            ImageView ivGoodsThumb;
            @BindView(R.id.tv_GoodsName)
            TextView tvGoodsName;

            CollectViewHolder(View view) {
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

