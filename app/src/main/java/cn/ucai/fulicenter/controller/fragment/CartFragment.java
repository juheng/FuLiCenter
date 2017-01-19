package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.CollectActivity;
import cn.ucai.fulicenter.controller.adapter.GoodsAdapter;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.view.MFGT;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CartFragment extends Fragment {
    private static String TAG = CollectActivity.class.getSimpleName();


    User user;


    ArrayList<CartBean> mList;
    GridLayoutManager mManager;

    IModelUser mModel;

    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tv_all_price)
    TextView tvAllPrice;
    @BindView(R.id.tv_save_price)
    TextView tvSavePrice;
    @BindView(R.id.bt_settlement)
    Button btSettlement;
    @BindView(R.id.tv_nothing)
    TextView tvNothing;

    public CartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        mModel = new ModelUser();
        mList = new ArrayList<>();
        initView();
        initData();
       // setListener();
        return view;
    }

   /* private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }*/

   /* private void setPullUpListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = mManager.findLastVisibleItemPosition();
                if (mAdapter.isMore() && newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.getItemCount() - 1 == lastPosition) {
                    download(I.ACTION_PULL_UP);
                }
            }
        });
    }*/

    /*private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                download(I.ACTION_PULL_DOWN);
            }
        });
    }*/

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLoginActivity(getActivity());
        } else {

            download(I.ACTION_DOWNLOAD);
        }

    }

    private void download(final int action) {
        mModel.getCart(getActivity(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(Object result) {
                CartBean[] result1 = (CartBean[]) result;
                L.e(TAG,"result="+result1);
                ArrayList<CartBean> list = ConvertUtils.array2List(result1);
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "error=" + error);
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


       /* mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.getItemCount() - 1) {
                    return 2;
                }
                return 1;
            }
        });*/
    }


}
