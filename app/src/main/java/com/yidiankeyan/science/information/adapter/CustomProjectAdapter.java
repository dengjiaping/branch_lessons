package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.information.entity.ProjectBean;

import java.util.List;


/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2016/12/15.
 * 作用：
 */

public class CustomProjectAdapter extends BaseAdapter {

    private List<ProjectBean> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnOrderChangedListener onOrderChangedListener;

    public CustomProjectAdapter(List<ProjectBean> data, Context context) {
        this.mData = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnOrderChangedListener(OnOrderChangedListener onOrderChangedListener) {
        this.onOrderChangedListener = onOrderChangedListener;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size() == 0 ? 0 : 1 + (mData.size() / 5);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_custom_project, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        int count = mData.size() - position * 5;
        if (count > 4) {
            holder.tvOne.setVisibility(View.VISIBLE);
            holder.tvTwo.setVisibility(View.VISIBLE);
            holder.tvThree.setVisibility(View.VISIBLE);
            holder.tvFour.setVisibility(View.VISIBLE);
            holder.tvFive.setVisibility(View.VISIBLE);
            holder.tvOne.setText(mData.get(position * 5).getSubjectname());
            holder.tvTwo.setText(mData.get(position * 5 + 1).getSubjectname());
            holder.tvThree.setText(mData.get(position * 5 + 2).getSubjectname());
            holder.tvFour.setText(mData.get(position * 5 + 3).getSubjectname());
            holder.tvFive.setText(mData.get(position * 5 + 4).getSubjectname());
            if (mData.get(position * 5).isSelected()) {
                holder.tvOne.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvOne.setTextColor(Color.WHITE);
            } else {
                holder.tvOne.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvOne.setTextColor(Color.parseColor("#666666"));
            }
            if (mData.get(position * 5 + 1).isSelected()) {
                holder.tvTwo.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvTwo.setTextColor(Color.WHITE);
            } else {
                holder.tvTwo.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvTwo.setTextColor(Color.parseColor("#666666"));
            }
            if (mData.get(position * 5 + 2).isSelected()) {
                holder.tvThree.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvThree.setTextColor(Color.WHITE);
            } else {
                holder.tvThree.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvThree.setTextColor(Color.parseColor("#666666"));
            }
            if (mData.get(position * 5 + 3).isSelected()) {
                holder.tvFour.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvFour.setTextColor(Color.WHITE);
            } else {
                holder.tvFour.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvFour.setTextColor(Color.parseColor("#666666"));
            }
            if (mData.get(position * 5 + 4).isSelected()) {
                holder.tvFive.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvFive.setTextColor(Color.WHITE);
            } else {
                holder.tvFive.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvFive.setTextColor(Color.parseColor("#666666"));
            }
        } else if (count == 4) {
            holder.tvOne.setVisibility(View.VISIBLE);
            holder.tvTwo.setVisibility(View.VISIBLE);
            holder.tvThree.setVisibility(View.VISIBLE);
            holder.tvFour.setVisibility(View.VISIBLE);
            holder.tvFive.setVisibility(View.GONE);
            holder.tvOne.setText(mData.get(position * 5).getSubjectname());
            holder.tvTwo.setText(mData.get(position * 5 + 1).getSubjectname());
            holder.tvThree.setText(mData.get(position * 5 + 2).getSubjectname());
            holder.tvFour.setText(mData.get(position * 5 + 3).getSubjectname());
            if (mData.get(position * 5).isSelected()) {
                holder.tvOne.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvOne.setTextColor(Color.WHITE);
            } else {
                holder.tvOne.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvOne.setTextColor(Color.parseColor("#666666"));
            }
            if (mData.get(position * 5 + 1).isSelected()) {
                holder.tvTwo.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvTwo.setTextColor(Color.WHITE);
            } else {
                holder.tvTwo.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvTwo.setTextColor(Color.parseColor("#666666"));
            }
            if (mData.get(position * 5 + 2).isSelected()) {
                holder.tvThree.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvThree.setTextColor(Color.WHITE);
            } else {
                holder.tvThree.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvThree.setTextColor(Color.parseColor("#666666"));
            }
            if (mData.get(position * 5 + 3).isSelected()) {
                holder.tvFour.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvFour.setTextColor(Color.WHITE);
            } else {
                holder.tvFour.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvFour.setTextColor(Color.parseColor("#666666"));
            }
        } else if (count == 3) {
            holder.tvOne.setVisibility(View.VISIBLE);
            holder.tvTwo.setVisibility(View.VISIBLE);
            holder.tvThree.setVisibility(View.VISIBLE);
            holder.tvFour.setVisibility(View.GONE);
            holder.tvFive.setVisibility(View.GONE);
            holder.tvOne.setText(mData.get(position * 5).getSubjectname());
            holder.tvTwo.setText(mData.get(position * 5 + 1).getSubjectname());
            holder.tvThree.setText(mData.get(position * 5 + 2).getSubjectname());
            if (mData.get(position * 5).isSelected()) {
                holder.tvOne.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvOne.setTextColor(Color.WHITE);
            } else {
                holder.tvOne.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvOne.setTextColor(Color.parseColor("#666666"));
            }
            if (mData.get(position * 5 + 1).isSelected()) {
                holder.tvTwo.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvTwo.setTextColor(Color.WHITE);
            } else {
                holder.tvTwo.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvTwo.setTextColor(Color.parseColor("#666666"));
            }
            if (mData.get(position * 5 + 2).isSelected()) {
                holder.tvThree.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvThree.setTextColor(Color.WHITE);
            } else {
                holder.tvThree.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvThree.setTextColor(Color.parseColor("#666666"));
            }
        } else if (count == 2) {
            holder.tvOne.setVisibility(View.VISIBLE);
            holder.tvTwo.setVisibility(View.VISIBLE);
            holder.tvThree.setVisibility(View.GONE);
            holder.tvFour.setVisibility(View.GONE);
            holder.tvFive.setVisibility(View.GONE);
            holder.tvOne.setText(mData.get(position * 5).getSubjectname());
            holder.tvTwo.setText(mData.get(position * 5 + 1).getSubjectname());
            if (mData.get(position * 5).isSelected()) {
                holder.tvOne.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvOne.setTextColor(Color.WHITE);
            } else {
                holder.tvOne.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvOne.setTextColor(Color.parseColor("#666666"));
            }
            if (mData.get(position * 5 + 1).isSelected()) {
                holder.tvTwo.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvTwo.setTextColor(Color.WHITE);
            } else {
                holder.tvTwo.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvTwo.setTextColor(Color.parseColor("#666666"));
            }
        } else if (count == 1) {
            holder.tvOne.setVisibility(View.VISIBLE);
            holder.tvTwo.setVisibility(View.GONE);
            holder.tvThree.setVisibility(View.GONE);
            holder.tvFour.setVisibility(View.GONE);
            holder.tvFive.setVisibility(View.GONE);
            holder.tvOne.setText(mData.get(position * 5).getSubjectname());
            if (mData.get(position * 5).isSelected()) {
                holder.tvOne.setBackgroundResource(R.drawable.shape_project_pressed);
                holder.tvOne.setTextColor(Color.WHITE);
            } else {
                holder.tvOne.setBackgroundResource(R.drawable.shape_project_normal);
                holder.tvOne.setTextColor(Color.parseColor("#666666"));
            }
        } else {
            holder.tvOne.setVisibility(View.GONE);
            holder.tvTwo.setVisibility(View.GONE);
            holder.tvThree.setVisibility(View.GONE);
            holder.tvFour.setVisibility(View.GONE);
            holder.tvFive.setVisibility(View.GONE);
        }

        holder.tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position * 5).isSelected()) {
                    mData.get(position * 5).setSelected(false);
                    holder.tvOne.setBackgroundResource(R.drawable.shape_project_normal);
                    holder.tvOne.setTextColor(Color.parseColor("#666666"));
                } else {
                    mData.get(position * 5).setSelected(true);
                    holder.tvOne.setBackgroundResource(R.drawable.shape_project_pressed);
                    holder.tvOne.setTextColor(Color.WHITE);
                }
                if (onOrderChangedListener != null) {
                    onOrderChangedListener.onChanged(mData.get(position * 5).getSubjectid(), mData.get(position * 5).isSelected());
                }
            }
        });
        holder.tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position * 5 + 1).isSelected()) {
                    mData.get(position * 5 + 1).setSelected(false);
                    holder.tvTwo.setBackgroundResource(R.drawable.shape_project_normal);
                    holder.tvTwo.setTextColor(Color.parseColor("#666666"));
                } else {
                    mData.get(position * 5 + 1).setSelected(true);
                    holder.tvTwo.setBackgroundResource(R.drawable.shape_project_pressed);
                    holder.tvTwo.setTextColor(Color.WHITE);
                }
                if (onOrderChangedListener != null) {
                    onOrderChangedListener.onChanged(mData.get(position * 5 + 1).getSubjectid(), mData.get(position * 5 + 1).isSelected());
                }
            }
        });
        holder.tvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position * 5 + 2).isSelected()) {
                    mData.get(position * 5 + 2).setSelected(false);
                    holder.tvThree.setBackgroundResource(R.drawable.shape_project_normal);
                    holder.tvThree.setTextColor(Color.parseColor("#666666"));
                } else {
                    mData.get(position * 5 + 2).setSelected(true);
                    holder.tvThree.setBackgroundResource(R.drawable.shape_project_pressed);
                    holder.tvThree.setTextColor(Color.WHITE);
                }
                if (onOrderChangedListener != null) {
                    onOrderChangedListener.onChanged(mData.get(position * 5 + 2).getSubjectid(), mData.get(position * 5 + 2).isSelected());
                }
            }
        });
        holder.tvFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position * 5 + 3).isSelected()) {
                    mData.get(position * 5 + 3).setSelected(false);
                    holder.tvFour.setBackgroundResource(R.drawable.shape_project_normal);
                    holder.tvFour.setTextColor(Color.parseColor("#666666"));
                } else {
                    mData.get(position * 5 + 3).setSelected(true);
                    holder.tvFour.setBackgroundResource(R.drawable.shape_project_pressed);
                    holder.tvFour.setTextColor(Color.WHITE);
                }
                if (onOrderChangedListener != null) {
                    onOrderChangedListener.onChanged(mData.get(position * 5 + 3).getSubjectid(), mData.get(position * 5 + 3).isSelected());
                }
            }
        });
        holder.tvFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position * 5 + 4).isSelected()) {
                    mData.get(position * 5 + 4).setSelected(false);
                    holder.tvFive.setBackgroundResource(R.drawable.shape_project_normal);
                    holder.tvFive.setTextColor(Color.parseColor("#666666"));
                } else {
                    mData.get(position * 5 + 4).setSelected(true);
                    holder.tvFive.setBackgroundResource(R.drawable.shape_project_pressed);
                    holder.tvFive.setTextColor(Color.WHITE);
                }
                if (onOrderChangedListener != null) {
                    onOrderChangedListener.onChanged(mData.get(position * 5 + 4).getSubjectid(), mData.get(position * 5 + 4).isSelected());
                }
            }
        });

        return convertView;
    }

    public interface OnOrderChangedListener {
        void onChanged(int id, boolean selected);
    }

    class ViewHolder {
        private TextView tvOne;
        private TextView tvTwo;
        private TextView tvThree;
        private TextView tvFour;
        private TextView tvFive;

        public ViewHolder(View convertView) {
            tvOne = (TextView) convertView.findViewById(R.id.tv_one);
            tvTwo = (TextView) convertView.findViewById(R.id.tv_two);
            tvThree = (TextView) convertView.findViewById(R.id.tv_three);
            tvFour = (TextView) convertView.findViewById(R.id.tv_four);
            tvFive = (TextView) convertView.findViewById(R.id.tv_five);
        }
    }
}
