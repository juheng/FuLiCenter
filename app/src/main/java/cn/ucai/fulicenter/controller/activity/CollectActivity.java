package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.CollectAdapter;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

import static cn.ucai.fulicenter.application.I.ACTION_PULL_UP;

public class CollectActivity extends AppCompatActivity {
    private static String TAG = CollectActivity.class.getSimpleName();
    IModelNewGoods model;
    User user;
    int pageId;
    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    CollectAdapter mAdapter;
    ArrayList<CollectBean> list;
    GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        list=new ArrayList<>();
        mAdapter = new CollectAdapter(this, list);
        DisplayUtils.initBackWithTitle(this, "收藏的宝贝");

        user = FuLiCenterApplication.getUser();
        if (user != null) {
            initData();
            initView();
            setListener();
        } else {
            finish();
        }
    }

    private void initView() {
        srl.setColorSchemeColors(getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));

        manager = new GridLayoutManager(this, I.COLUM_NUM);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new SpaceItemDecoration(25));

        recyclerView.setAdapter(mAdapter);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.getItemCount() - 1) {
                    return 2;
                }
                return 1;
            }
        });
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
                int lastPosition = manager.findLastVisibleItemPosition();
                if (mAdapter.isMore() && newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.getItemCount() - 1 == lastPosition) {
                    pageId++;
                    download(ACTION_PULL_UP, pageId);
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
        pageId = 1;
        download(I.ACTION_DOWNLOAD, pageId);

    }

    private void download(final int action, int pageId) {
        model = new ModelNewGoods();
        model.getCollect(this, user.getMuserName(), pageId, I.PAGE_SIZE_DEFAULT, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(Object result) {
                CollectBean[] result1 = (CollectBean[]) result;
                if (result1 != null) {
                    ArrayList<CollectBean> list = ConvertUtils.array2List(result1);
                    L.e(TAG, "list=" + list.size());
                    mAdapter.setMore(result1 != null && result1.length > 0);
                    if (!mAdapter.isMore()) {
                        if (action == I.ACTION_PULL_UP) {
                            mAdapter.setFooter("没有更多数据");
                        }

                        return;
                    }
                    mAdapter.setFooter("加载更多数据");
                    switch (action) {
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

            }

            @Override
            public void onError(String error) {
                L.e(TAG, "error=" + error);
            }
        });
    }
}
