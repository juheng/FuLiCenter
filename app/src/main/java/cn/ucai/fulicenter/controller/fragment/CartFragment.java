package cn.ucai.fulicenter.controller.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.CollectActivity;
import cn.ucai.fulicenter.controller.adapter.CartAdapter;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.view.MFGT;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CartFragment extends Fragment {
    private static String TAG = CollectActivity.class.getSimpleName();


    User user;

    CartAdapter adapter;
    ArrayList<CartBean> mList;
    LinearLayoutManager mManager;

    IModelUser mModel;

    int sumPrice = 0;
    int payPrice = 0;
    ArrayList<CartBean> cartList = new ArrayList<>();

    UpdateCartReceiver receiver;

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
        srl.setVisibility(View.GONE);
        tvNothing.setVisibility(View.VISIBLE);
        initData();
        adapter = new CartAdapter(getContext(), cartList);
        initView();
        setListener();
        setReceiver();
        return view;
    }

    private void setReceiver() {
        receiver = new UpdateCartReceiver();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATE_CART);
        getContext().registerReceiver(receiver, filter);
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
                L.e(TAG, "result=" + result1.toString());
                srl.setVisibility(View.VISIBLE);
                tvNothing.setVisibility(View.GONE);
                if (result1 != null && result1.length > 0) {

                    ArrayList<CartBean> list = ConvertUtils.array2List(result1);
                    cartList.addAll(list);
                    L.e(TAG, "list=" + list.size());
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            adapter.initData(list);
                            break;
                        case I.ACTION_PULL_DOWN:
                            srl.setRefreshing(false);
                            tvRefresh.setVisibility(View.GONE);
                            adapter.initData(list);
                            break;
                    }
                }
            }

            @Override
            public void onError(String error) {
                srl.setVisibility(View.GONE);
                tvNothing.setVisibility(View.VISIBLE);
                L.e(TAG, "error=" + error);
            }
        });
    }

    private void initView() {
        srl.setColorSchemeColors(getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));

        mManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new SpaceItemDecoration(25));

        recyclerView.setAdapter(adapter);

        /*srl.setVisibility(View.GONE);
        tvNothing.setVisibility(View.VISIBLE);*/

    }


    @OnClick(R.id.tv_nothing)
    public void onClick() {
        download(I.ACTION_DOWNLOAD);
    }

    @Override
    public void onResume() {
        super.onResume();
        setPrice();
    }

    private void setPrice() {
        sumPrice = 0;
        payPrice=0;
        int savePrice = 0;
        if (cartList != null && cartList.size() > 0) {
            for (CartBean cart : cartList) {
                GoodsDetailsBean goods = cart.getGoods();
                if (cart.isChecked() && goods != null) {
                    sumPrice += cart.getCount() * getPrice(goods.getCurrencyPrice());
                    savePrice += cart.getCount() * (getPrice(goods.getCurrencyPrice()) - getPrice(goods.getRankPrice()));
                }
            }
        }
        tvAllPrice.setText("合计：¥" + sumPrice);
        tvSavePrice.setText("节省：¥" + savePrice);
        adapter.notifyDataSetChanged();
        payPrice=sumPrice-savePrice;
    }

    int getPrice(String price) {
        int p = 0;
        p = Integer.valueOf(price.substring(price.indexOf("¥") + 1));
        return p;
    }

    class UpdateCartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            L.e(TAG, "onReceiver");
            setPrice();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getContext().unregisterReceiver(receiver);
        }
    }

    @OnClick(R.id.bt_settlement)
    public void onBuyClick() {
        if (sumPrice > 0) {
            L.e(TAG, "sumPrice=" + sumPrice);
            MFGT.gotoOrderActivity(getContext(),payPrice);
        } else {
            CommonUtils.showLongToast(R.string.order_nothing);
        }
    }
}
