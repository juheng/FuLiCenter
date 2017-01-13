package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.activity.MainActivity;
import cn.ucai.fulicenter.controller.adapter.CategoryAdapter;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.IModelCategory;
import cn.ucai.fulicenter.model.net.ModelCategory;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    CategoryAdapter mAdapter;
    ArrayList<CategoryGroupBean> groupList = new ArrayList<>();
    ArrayList<ArrayList<CategoryChildBean>> childList = new ArrayList<>();

    IModelCategory mModel;

    MainActivity mContext;

    int groupId = 0;
    @BindView(R.id.listView)
    ExpandableListView listView;
    @BindView(R.id.load_more)
    TextView loadMore;

    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        mModel = new ModelCategory();
        mContext = (MainActivity) getContext();

        mAdapter = new CategoryAdapter(mContext, groupList, childList);
        initData();
        listView.setGroupIndicator(null);
        listView.setAdapter(mAdapter);
        initView(false);
        return view;
    }


    private void initData() {
        mModel.downGroupData(mContext, new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(Object result) {
                CategoryGroupBean[] mResult = (CategoryGroupBean[]) result;
                if (mResult != null) {
                    initView(true);
                    ArrayList<CategoryGroupBean> list1 = ConvertUtils.array2List(mResult);
                    groupList.addAll(list1);
                    for (int i = 0; i < list1.size(); i++) {
                        downloadChildData(list1.get(i).getId());
                    }
                } else {
                    initView(false);
                }
            }

            @Override
            public void onError(String error) {
                initView(false);
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void downloadChildData(final int parentId) {
        mModel.downChildData(mContext, parentId, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(Object result) {
                CategoryChildBean[] mResult = (CategoryChildBean[]) result;
                if (mResult != null) {

                    ArrayList<CategoryChildBean> list2 = ConvertUtils.array2List(mResult);
                    groupId++;
                    childList.add(list2);
                    if (groupId == groupList.size()) {
                        mAdapter.initData(groupList, childList);
                    }
                }
            }

            @Override
            public void onError(String error) {
                groupId++;
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView(boolean hasData) {
        listView.setVisibility(hasData ? View.VISIBLE : View.GONE);
        loadMore.setVisibility(hasData ? View.GONE : View.VISIBLE);

    }

}

