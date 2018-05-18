package com.acode.player.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.acode.player.R;
import com.acode.player.base.CircleFragment;

/**
 * user:yangtao
 * date:2018/5/111414
 * email:yangtao@bjxmail.com
 * introduce:test
 */
public class TestActivity extends FragmentActivity {
    private FrameLayout fram;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initView();
        initData();
    }

    private void initView() {
        fram = findViewById(R.id.fram);
    }

    private void initData() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fram, new CircleFragment(), CircleFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }

}
