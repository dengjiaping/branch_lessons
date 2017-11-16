package com.yidiankeyan.science.information.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.adapter.WholeAlbumAdapter;
import com.yidiankeyan.science.subscribe.entity.BusinessAllBean;
import com.yidiankeyan.science.view.rollviewpager.RollPagerView;
import com.yidiankeyan.science.view.rollviewpager.adapter.LoopPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛思FM
 *  -所有专辑
 */
public class WholeAlbumForScienceFMFragment extends Fragment {


    private WholeAlbumAdapter albumAdapter;
    private ListView lvWhole;
    private View viewtop;
    private List<BusinessAllBean> mDatas = new ArrayList<>();

    //控制viewpager显示当前的页码
    private int pageNum = 0;
    private boolean flag = true;
    private RollPagerView mRollViewPager;
    private TestLoopAdapter mLoopAdapter;


    public WholeAlbumForScienceFMFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whole_album, container, false);
        //初始化控件
        initView(view);

        //填充数据
        addData();


        return view;
    }

    private void addData() {


        albumAdapter = new WholeAlbumAdapter(getActivity(), mDatas);
        lvWhole.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        lvWhole = (ListView) view.findViewById(R.id.lv_whole);
        viewtop = LayoutInflater.from(getActivity()).inflate(R.layout.head_img_list, null);
        lvWhole.addHeaderView(viewtop);
        mRollViewPager = (RollPagerView) viewtop.findViewById(R.id.vp_school_course);
        mRollViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mRollViewPager));
        mRollViewPager.setHintAlpha(0);
        mRollViewPager.setHintView(null);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {
//                R.drawable.imgones,
//                R.drawable.imgtwo,
//                R.drawable.imgthree,
//                R.drawable.imgfour,
//                R.drawable.imgfive,
        };
        private int count = imgs.length;

        public void add() {
            Log.i("RollViewPager", "Add");
            count++;
            if (count > imgs.length) count = imgs.length;
            notifyDataSetChanged();
        }

        public void minus() {
            Log.i("RollViewPager", "Minus");
            count--;
            if (count < 1) count = 1;
            notifyDataSetChanged();
        }

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        @Override
        public int getRealCount() {
            return count;
        }

    }
}
