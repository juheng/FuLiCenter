package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.MFGT;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class NewGoodsDetailActivity extends AppCompatActivity {
    private static String TAG = NewGoodsDetailActivity.class.getSimpleName();

    int goodsId;

    boolean isCollect;

    IModelNewGoods model;

    @BindView(R.id.goods_detail_back)
    ImageView goodsDetailBack;
    @BindView(R.id.goods_detail_English_name)
    TextView goodsDetailEnglishName;
    @BindView(R.id.goods_detail_name)
    TextView goodsDetailName;
    @BindView(R.id.goods_detail_price)
    TextView goodsDetailPrice;
    @BindView(R.id.goods_detail_salv)
    SlideAutoLoopView goodsDetailSalv;
    @BindView(R.id.goods_detail_indicator)
    FlowIndicator goodsDetailIndicator;
    @BindView(R.id.goods_detail_webView)
    WebView goodsDetailWebView;
    @BindView(R.id.goods_detail_collect)
    ImageView goodsDetailCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goos_detail);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            MFGT.finish(this);
        } else {
            initData();
        }
    }

    private void initData() {
        model = new ModelNewGoods();
        model.downDetailData(this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {

            @Override
            public void onSuccess(Object result) {
                GoodsDetailsBean mResult = (GoodsDetailsBean) result;
                if (result != null) {

                    showGoodsDetail(mResult);
                } else {
                    MFGT.finish(NewGoodsDetailActivity.this);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(NewGoodsDetailActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showGoodsDetail(GoodsDetailsBean goods) {
        goodsDetailName.setText(goods.getGoodsName());
        goodsDetailEnglishName.setText(goods.getGoodsEnglishName());
        goodsDetailPrice.setText(goods.getCurrencyPrice());
        goodsDetailSalv.startPlayLoop(goodsDetailIndicator, getAlbumUrl(goods), getAlbumCount(goods));
        goodsDetailWebView.loadDataWithBaseURL(null, goods.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlbumCount(GoodsDetailsBean goods) {
        if (goods != null && goods.getProperties() != null && goods.getProperties().length > 0) {
            return goods.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumUrl(GoodsDetailsBean goods) {
        if (goods != null && goods.getProperties() != null && goods.getProperties().length > 0) {
            AlbumsBean[] albums = goods.getProperties()[0].getAlbums();
            if (albums != null && albums.length > 0) {
                String[] urls = new String[albums.length];
                for (int i = 0; i < albums.length; i++) {
                    urls[i] = albums[i].getImgUrl();
                }
                return urls;
            }
        }
        return new String[0];
    }

    @OnClick({R.id.goods_detail_back, R.id.goods_detail_collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_detail_back:
                this.finish();
                break;
            case R.id.goods_detail_collect:
                goodsDetailCollect.setEnabled(false);
                User user = FuLiCenterApplication.getUser();
                if (user != null) {
                    checkInput(user);
                } else {
                    MFGT.gotoLoginActivity(this);
                    goodsDetailCollect.setEnabled(true);
                }
                break;

        }
    }

    private void checkInput(User user) {
        model.addCollect(this, goodsId, user.getMuserName()
                , isCollect ? I.ACTION_DELETE_COLLECT : I.ACTION_ADD_COLLECT, new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            MessageBean result1= (MessageBean) result;
                            isCollect = !isCollect;
                            setCollectStatus();
                            CommonUtils.showShortToast(result1.getMsg());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG, "error=" + error);
                        goodsDetailCollect.setEnabled(true);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        initCollectStatus();
    }

    private void initCollectStatus() {
        goodsDetailCollect.setEnabled(false);
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            model.isCollect(this, goodsId, user.getMuserName(), new OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(Object result) {
                    MessageBean result1 = (MessageBean) result;
                    L.e(TAG, "result1=========" + result1);
                    if (result1 != null && result1.isSuccess()) {
                        isCollect = true;

                    } else {
                        isCollect = false;
                    }
                    setCollectStatus();
                }

                @Override
                public void onError(String error) {
                    isCollect = false;
                }
            });
        }
    }

    private void setCollectStatus() {
        L.e(TAG, "000000000000000000000000000000000000000000000000");
            if (isCollect) {
            goodsDetailCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            goodsDetailCollect.setImageResource(R.mipmap.bg_collect_in);

        }
        goodsDetailCollect.setEnabled(true);
    }

}

