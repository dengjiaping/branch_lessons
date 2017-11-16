package com.yidiankeyan.science.information.acitivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.RecentAnswerAdapter;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 问答专辑详情
 */
public class AnswerAlbumDetailActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private TextView maintitleTxt;
    private ImageButton titleBtn;
    private ListView lvAnswerData;
    private RecentAnswerAdapter adapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_answer_album_detail;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        lvAnswerData = (ListView) findViewById(R.id.lv_answer_data);
    }

    @Override
    protected void initAction() {
        llReturn.setOnClickListener(this);
        maintitleTxt.setText("理论前沿");
       // adapter = new RecentAnswerAdapter(this);
        lvAnswerData.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;

        }
    }
}
