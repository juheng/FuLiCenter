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
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter {
    MainActivity mContext;

    ArrayList<BoutiqueBean> boutiqueList;
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

    public BoutiqueAdapter(final Context mContext, ArrayList<BoutiqueBean> list) {
        this.mContext = (MainActivity) mContext;
        this.boutiqueList = new ArrayList<>();
        boutiqueList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout;
        if (viewType == I.TYPE_FOOTER) {
            layout = inflater.inflate(R.layout.item_footer, null);
            return new FooterViewHolder(layout);
        } else {
            layout = inflater.inflate(R.layout.item_boutique, null);
            return new BoutiqueViewHolder(layout);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, final int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder holder = (FooterViewHolder) parentHolder;
            holder.tvFooter.setText(getFooter());
            return;
        }
        final BoutiqueViewHolder vh = (BoutiqueViewHolder) parentHolder;
        ImageLoader.downloadImg(mContext, vh.tvImage, boutiqueList.get(position).getImageurl());
        vh.tvText1.setText(boutiqueList.get(position).getTitle());
        vh.tvText2.setText(boutiqueList.get(position).getName());
        vh.tvText3.setText(boutiqueList.get(position).getDescription());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFGT.gotoBoutiqueChild(mContext, boutiqueList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return boutiqueList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initData(ArrayList<BoutiqueBean> list) {
        if (boutiqueList != null) {

            this.boutiqueList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<BoutiqueBean> list) {
        boutiqueList.addAll(list);
        notifyDataSetChanged();
    }


    static class FooterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_footer)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_image)
        ImageView tvImage;
        @BindView(R.id.tv_text1)
        TextView tvText1;
        @BindView(R.id.tv_text2)
        TextView tvText2;
        @BindView(R.id.tv_text3)
        TextView tvText3;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

