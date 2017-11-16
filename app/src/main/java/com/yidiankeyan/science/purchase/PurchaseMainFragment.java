package com.yidiankeyan.science.purchase;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.purchase.fragment.PurchaseFragment;
import com.yidiankeyan.science.purchase.fragment.RecentFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseMainFragment extends Fragment {

    private RadioGroup radioGroup;
    private RadioButton rbRecent;
    private RadioButton rbPurchase;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private PurchaseFragment purchaseFragment;
    private RecentFragment recentFragment;

    public PurchaseMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchase_main, container, false);
        initView(view);
        rbRecent.setChecked(true);
        initFragment();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (fragmentManager == null) {
                    fragmentManager = getChildFragmentManager();
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                if (checkedId == R.id.rb_recent) {
                    //最近
                    if (purchaseFragment != null) {
                        fragmentTransaction.hide(purchaseFragment);
                    }
                    if (recentFragment == null) {
                        recentFragment = new RecentFragment();
                        fragmentTransaction.add(R.id.fl_container, recentFragment);
                    } else {
                        fragmentTransaction.show(recentFragment);
                    }
                } else {
                    //已购
                    if (recentFragment != null) {
                        fragmentTransaction.hide(recentFragment);
                    }
                    if (purchaseFragment == null) {
                        purchaseFragment = new PurchaseFragment();
                        fragmentTransaction.add(R.id.fl_container, purchaseFragment);
                    } else {
                        fragmentTransaction.show(purchaseFragment);
                    }
                    Intent intent = new Intent();
                    intent.setAction("action.read.audio");
                    getActivity().sendBroadcast(intent);
                }
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        return view;
    }

    private void initFragment() {
        if (fragmentManager == null) {
            fragmentManager = getChildFragmentManager();
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        if (purchaseFragment != null) {
            fragmentTransaction.hide(purchaseFragment);
        }
        if (recentFragment == null) {
            recentFragment = new RecentFragment();
            fragmentTransaction.add(R.id.fl_container, recentFragment);
        } else {
            fragmentTransaction.show(recentFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initView(View view) {
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        rbRecent = (RadioButton) view.findViewById(R.id.rb_recent);
        rbPurchase = (RadioButton) view.findViewById(R.id.rb_purchase);
    }

}
