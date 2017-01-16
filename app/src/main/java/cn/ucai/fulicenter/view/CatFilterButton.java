package cn.ucai.fulicenter.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

public class CatFilterButton extends Button {
    boolean isExpan;
    PopupWindow mPopupWindow;
    Context mContext;

    public CatFilterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void initCatFilterButton(String groupName, ArrayList<CategoryChildBean> list) {
        this.setText(groupName);
        setOnClickListener();
    }

    public void setOnClickListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExpan) {
                    initPopup();
                } else {
                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
                setArrow();
            }
        });
    }

    private void initPopup() {
        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
        TextView text = new TextView(mContext);
        text.setText("nihaoya");
        mPopupWindow.setContentView(text);
        mPopupWindow.showAsDropDown(this);

    }

    private void setArrow() {
        Drawable right;
        if (isExpan) {
            right = getResources().getDrawable(R.mipmap.arrow2_up);
        } else {
            right = getResources().getDrawable(R.mipmap.arrow2_down);
        }
        right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
        isExpan = !isExpan;
    }

    static class CatFilterAdapter extends BaseAdapter {
        Context context;
        ArrayList<CategoryChildBean> list;

        public CatFilterAdapter(Context context, ArrayList<CategoryChildBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CatFilterViewHolder vh = null;
            if (view == null) {
                view = View.inflate(context, R.layout.item_cat_filter, null);
                vh = new CatFilterViewHolder(view);
                view.setTag(vh);
            }else{
                vh= (CatFilterViewHolder) view.getTag();
            }
            return view;
        }

        static class CatFilterViewHolder {
            @BindView(R.id.iv_cat_filter_image)
            ImageView ivCatFilterImage;
            @BindView(R.id.tv_category_child_text)
            TextView tvCategoryChildText;
            @BindView(R.id.rll)
            RelativeLayout rll;

            CatFilterViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
