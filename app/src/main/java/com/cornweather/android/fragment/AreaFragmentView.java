package com.cornweather.android.fragment;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cornweather.android.R;
import com.cornweather.android.helper.EventHelper;
import com.cornweather.android.view.ViewImp;

import java.util.List;

/**
 * Created by dufangyu on 2017/3/6.
 */

public class AreaFragmentView extends ViewImp{



    private ProgressDialog progressDialog;

    private TextView titleText;
    private Button backButton;
    private ListView listView;



    private ArrayAdapter<String>adapter;


    @Override
    public int getLayoutId() {
        return R.layout.choose_area;
    }

    @Override
    public void findViews() {
        titleText = findViewById(R.id.title_text);
        backButton = findViewById(R.id.back_button);
        listView = findViewById(R.id.list_view);
    }

    @Override
    public void bindEvent() {
        EventHelper.click(mPresenter,backButton);
        EventHelper.itemClick(mPresenter,listView);
    }


    public void initList(List<String> dataList)
    {
        adapter = new ArrayAdapter<String>(mRootView.getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
    }

    public void setTextNdButtonShow(String text,boolean isShow)
    {
        titleText.setText(text);
        if(isShow)
            backButton.setVisibility(View.VISIBLE);
        else
            backButton.setVisibility(View.GONE);
    }

    public void notifyData()
    {
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }


    public void showProgressDialog()
    {
        if(progressDialog==null)
        {
            progressDialog = new ProgressDialog(mRootView.getContext());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }


    public void closeProDialog()
    {
        if(progressDialog!=null)
        {
            progressDialog.dismiss();
        }
    }
}
