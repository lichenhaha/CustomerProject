package com.chenli.testmvp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.chenli.testmvp.presenter.WeatherPresenterImp;
import com.chenli.testmvp.view.WeatherView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements WeatherView {

    @Bind(R.id.textview)
    TextView textView;

    @Bind(R.id.button)
    Button button;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private WeatherPresenterImp weatherPresenterImp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        init();

        initToolbar();

    }

    private void initToolbar() {

        toolbar.setTitle("App title");
        toolbar.setLogo(R.mipmap.ic_launcher_round);
        toolbar.setSubtitle("sub title");
        setSupportActionBar(toolbar);

    }

    private void init() {
        weatherPresenterImp = new WeatherPresenterImp(this,this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在请求获取数据,请稍等!!!");

    }

    @OnClick(R.id.button)
    public void setBtnData(){
        weatherPresenterImp.loadWeather("c5bb749112664353af44bc99ed263857", "长沙");
    }

    @Override
    public void showProgress() {
        if (progressDialog != null && !progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    @Override
    public void disimissProgress() {
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(String tdata) {
        textView.setText(tdata);
    }

    @Override
    public void loadDataError(Throwable throwable) {
        textView.setText(throwable.getMessage());
    }
}
