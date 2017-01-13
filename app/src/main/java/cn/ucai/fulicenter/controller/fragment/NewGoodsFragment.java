package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.MainActivity;
import cn.ucai.fulicenter.controller.adapter.GoodsAdapter;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

import static cn.ucai.fulicenter.application.I.ACTION_PULL_UP;
import static cn.ucai.fulicenter.application.I.CAT_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {

    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)


    SwipeRefreshLayout srl;

    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager mManager;

    IModelNewGoods mModel;

    int pageId;

    public NewGoodsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, view);
        mModel = new ModelNewGoods();
        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(getContext(), mList);
        initView();
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition=mManager.findLastVisibleItemPosition();
                if(mAdapter.isMore()&&newState==RecyclerView.SCROLL_STATE_IDLE&&mAdapter.getItemCount()-1==lastPosition){
                    pageId++;
                    download(ACTION_PULL_UP,pageId);
                }
            }
        });
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                pageId = 1;
                download(I.ACTION_PULL_DOWN, pageId);
            }
        });
    }

    private void initData() {
        pageId=1;
        download(I.ACTION_DOWNLOAD, pageId);

    }

    private void download(final int action,int pageId) {
        int catId=getActivity().getIntent().getIntExtra(I.NewAndBoutiqueGoods.CAT_ID,CAT_ID);
        mModel.downData(getActivity(), catId, pageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(Object result) {
                NewGoodsBean[] mResult= (NewGoodsBean[]) result;
                ArrayList<NewGoodsBean> list = ConvertUtils.array2List(mResult);
                mAdapter.setMore(mResult != null&&mResult.length>0);
                if (!mAdapter.isMore()) {
                    if(action==I.ACTION_PULL_UP){
                        mAdapter.setFooter("没有更多数据");
                    }

                    return;
                }
                mAdapter.setFooter("加载更多数据");
                switch (action){
                    case I.ACTION_DOWNLOAD:
                        mAdapter.initData(list);
                        break;
                    case I.ACTION_PULL_DOWN:
                        srl.setRefreshing(false);
                        tvRefresh.setVisibility(View.GONE);
                        mAdapter.initData(list);
                        break;
                    case I.ACTION_PULL_UP:
                        mAdapter.addData(list);
                        break;
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        srl.setColorSchemeColors(getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));

        mManager = new GridLayoutManager(getActivity(), I.COLUM_NUM);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new SpaceItemDecoration(25));

        recyclerView.setAdapter(mAdapter);

        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
            @Override
            public int getSpanSize(int position) {
                if(position==mAdapter.getItemCount()-1) {
                    return 2;
                }
                return 1;
            }
        });
    }
    public void sortGoods(int sortBy){
        mAdapter.sortGoods(sortBy);
    }

}
