package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.MainActivity;
import cn.ucai.fulicenter.controller.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.net.IModelBoutique;
import cn.ucai.fulicenter.model.net.ModelBoutique;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {


    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    BoutiqueAdapter mAdapter;
    ArrayList<BoutiqueBean> mList;
    LinearLayoutManager mManager;

    IModelBoutique mModel;

    MainActivity mContext;
    @BindView(R.id.tv_boutique_load)
    TextView tvBoutiqueLoad;

    public BoutiqueFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, view);
        mModel = new ModelBoutique();
        mList = new ArrayList<>();
        mContext = (MainActivity) getContext();
        mAdapter = new BoutiqueAdapter(mContext, mList);
        initView();
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        setPullDownListener();
    }


    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                download(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initData() {
        download(I.ACTION_DOWNLOAD);

    }

    private void download(final int action) {
        mModel.downData(mContext, new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(Object result) {
                srl.setVisibility(View.VISIBLE);
                tvBoutiqueLoad.setVisibility(View.GONE);
                BoutiqueBean[] mResult = (BoutiqueBean[]) result;
                if(mResult!=null&&mResult.length>0){

                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(mResult);
                    mAdapter.setFooter("没有更多数据");
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            mAdapter.initData(list);
                            break;
                        case I.ACTION_PULL_DOWN:
                            srl.setRefreshing(false);
                            tvRefresh.setVisibility(View.GONE);
                            mAdapter.initData(list);
                            break;
                }
                }else{
                    srl.setVisibility(View.GONE);
                    tvBoutiqueLoad.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onError(String error) {
                srl.setVisibility(View.GONE);
                tvBoutiqueLoad.setVisibility(View.VISIBLE);
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        srl.setColorSchemeColors(getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));

        mManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mManager);

        recyclerView.addItemDecoration(new SpaceItemDecoration(25));

        recyclerView.setAdapter(mAdapter);
        srl.setVisibility(View.GONE);
        tvBoutiqueLoad.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.tv_boutique_load)
    public void onClick() {
        download(I.ACTION_DOWNLOAD);
    }
}
