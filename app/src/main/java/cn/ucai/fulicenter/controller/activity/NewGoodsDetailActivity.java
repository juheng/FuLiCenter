package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.MFGT;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class NewGoodsDetailActivity extends AppCompatActivity {

    int goodsId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goos_detail);
        ButterKnife.bind(this);
        goodsId = (int) getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
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
       goodsDetailWebView.loadDataWithBaseURL(null,goods.getGoodsBrief(),I.TEXT_HTML,I.UTF_8,null);
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

    @OnClick(R.id.goods_detail_back)
    public void onClick() {
        this.finish();
    }
}
