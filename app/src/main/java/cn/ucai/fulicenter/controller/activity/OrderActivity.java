package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.view.DisplayUtils;

public class OrderActivity extends AppCompatActivity {
    int payPrice = 0;
    @BindView(R.id.spinner_city)
    Spinner spinnerCity;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.bt_buy)
    Button btBuy;
    @BindView(R.id.et_people)
    EditText etPeople;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_street)
    EditText etStreet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        payPrice = getIntent().getIntExtra(I.Cart.PAY_PRICE, 0);
        DisplayUtils.initBackWithTitle(this, "信息");
        setView();
    }

    private void setView() {
        tvBuy.setText("合计：¥" + payPrice);
    }

    @OnClick(R.id.bt_buy)
    public void onClick() {
        String receiverName=etPeople.getText().toString();
        if(TextUtils.isEmpty(receiverName)){
            etPeople.setError("不能为空");
            etPeople.requestFocus();
            return;
        }
        String mobile=etPhone.getText().toString();
        if(TextUtils.isEmpty(mobile)){
            etPeople.setError("不能为空");
            etPeople.requestFocus();
            return;
        }
        if(!mobile.matches("[\\d]{11}")){
            etPhone.setError("格式错误");
            etPhone.requestFocus();
            return;
        }
        String area=spinnerCity.getSelectedItem().toString();
        if(TextUtils.isEmpty(area)){
            Toast.makeText(this, "地区不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String address=etStreet.getText().toString();
        if(TextUtils.isEmpty(address)){
            etStreet.setError("不能为空");
            etStreet.requestFocus();
            return;
        }
    }
}
