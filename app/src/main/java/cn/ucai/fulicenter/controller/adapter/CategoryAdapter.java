package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.CategoryDetailActivity;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<CategoryGroupBean> groupList = new ArrayList<>();
    ArrayList<ArrayList<CategoryChildBean>> childList = new ArrayList<>();

    public CategoryAdapter(Context context, ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        this.context = context;
        this.groupList.addAll(groupList);
        this.childList.addAll(childList);
    }

    @Override
    public int getGroupCount() {
        return groupList == null ? 0 : groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition)!= null&&childList!= null ? childList.get(groupPosition).size():0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {

            return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        GroupHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_category_group, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        holder.tvCategoryGroupDown.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        holder.tvCategoryGroupText.setText(groupList.get(groupPosition).getName());
        ImageLoader.downloadImg(context, holder.ivCategoryGroupImage, groupList.get(groupPosition).getImageUrl());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        ChildHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_category_child, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        holder.tvCategoryChildText.setText(childList.get(groupPosition).get(childPosition).getName());
        ImageLoader.downloadImg(context, holder.ivCategoryChildImage, childList.get(groupPosition).get(childPosition).getImageUrl());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFGT.gotoCategoryDetail(context,childList.get(groupPosition).get(childPosition),
                       childList.get(groupPosition));
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        this.groupList.clear();
        this.childList.clear();
        this.groupList.addAll(groupList);
        this.childList.addAll(childList);
        notifyDataSetChanged();
    }

    static class ChildHolder {
        @BindView(R.id.iv_category_child_image)
        ImageView ivCategoryChildImage;
        @BindView(R.id.tv_category_child_text)
        TextView tvCategoryChildText;

        ChildHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    static class GroupHolder {
        @BindView(R.id.iv_category_group_image)
        ImageView ivCategoryGroupImage;
        @BindView(R.id.tv_category_group_text)
        TextView tvCategoryGroupText;
        @BindView(R.id.tv_category_group_down)
        ImageView tvCategoryGroupDown;

        GroupHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
