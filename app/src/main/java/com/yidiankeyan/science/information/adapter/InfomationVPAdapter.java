package com.yidiankeyan.science.information.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 定义菜单适配器
 */
public class InfomationVPAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> list_Title; //tab名的列表
    public InfomationVPAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> list_Title) {
        super(fm);
        this.fragmentList = fragmentList;
        this.list_Title = list_Title;
    }

    /**
     * 得到每个页面
     */
    @Override
    public Fragment getItem(int arg0) {
//        return (fragmentList == null || fragmentList.size() == 0) ? null
//                : fragmentList.get(arg0);
        return fragmentList.get(arg0);
    }

    /**
     * 每个页面的title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position % list_Title.size());
    }

    /**
     * 页面的总个数
     */
    @Override
    public int getCount()
    {
//        return fragmentList == null ? 0 : fragmentList.size();
        return list_Title.size();
    }
}
