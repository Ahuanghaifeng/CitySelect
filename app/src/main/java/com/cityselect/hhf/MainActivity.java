package com.cityselect.hhf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 黄海峰 on 2017/3/27.
 */
public class MainActivity extends AppCompatActivity {


    @Bind(R.id.listview)
    ListView sortListView;
    @Bind(R.id.dialog)
    TextView dialog;
    @Bind(R.id.sidebar)
    SideBar sideBar;
    @Bind(R.id.tv_head)
    TextView head;

    private SortAdapter adapter;
    private List<CitySortModel> SourceDateList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDatas();
        initEvents();
        setAdapter();
    }


    private void initDatas() {
        sideBar.setTextView(dialog);
    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position + 1);
                }
            }
        });

        //ListView的点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("city",((CitySortModel) adapter.getItem(position)).getName());
                setResult(100,intent);
                finish();
            }
        });
    }

    private void setAdapter() {
        SourceDateList = filledData(getResources().getStringArray(R.array.citys));
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                char heads = SourceDateList.get(i).getSortLetters().charAt(0);
                head.setText(String.valueOf(heads));
            }
        });
    }



    private List<CitySortModel> filledData(String[] date) {
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();
        String key="";
        for (int i = 0; i < date.length; i++) {
            if (date[i].length()!=1){
                CitySortModel sortModel = new CitySortModel();
                sortModel.setSortLetters(key);
                sortModel.setName(date[i]);
                mSortList.add(sortModel);
            }else if (date[i].length()==1){
                key = date[i];
                indexString.add(key);
            }
        }
        sideBar.setIndexText(indexString);
        return mSortList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
