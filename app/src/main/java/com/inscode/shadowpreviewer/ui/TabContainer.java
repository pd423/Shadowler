package com.inscode.shadowpreviewer.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inscode.shadowpreviewer.R;

public class TabContainer extends LinearLayout {

    public interface OnTabClickListener {
        void onClick(View view, int position);
    }

    public static final int SETTINGS_TAB_DX = 0;
    public static final int SETTINGS_TAB_DY = 1;
    public static final int SETTINGS_TAB_RADIUS = 2;
    public static final int SETTINGS_TAB_TEXT_COLOR = 3;
    public static final int SETTINGS_TAB_SHADOW_COLOR = 4;

    private final int[][] mTabPairs = new int[][] {
            { SETTINGS_TAB_DX, R.string.settings_tab_text_dx },
            { SETTINGS_TAB_DY, R.string.settings_tab_text_dy },
            { SETTINGS_TAB_RADIUS, R.string.settings_tab_text_radius },
            { SETTINGS_TAB_TEXT_COLOR, R.string.settings_tab_text_text_color },
            { SETTINGS_TAB_SHADOW_COLOR, R.string.settings_tab_text_shadow_color }
    };

    private SparseArray<TextView> mTabs;
    private OnTabClickListener mTabClickListener;
    private int mSelectedTabIndex = 0;

    public TabContainer(Context context) {
        super(context);
        initViews(context);
        initClickListeners();
    }

    public TabContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
        initClickListeners();
    }

    public TabContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
        initClickListeners();
    }

    private void initViews(@NonNull Context context) {
        mTabs = new SparseArray<>();

        int tabMargin = context.getResources().getDimensionPixelSize(R.dimen.settings_tab_margin);
        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                context.getResources().getDimensionPixelSize(R.dimen.settings_tab_height));
        params.setMargins(tabMargin, tabMargin, 0, tabMargin);

        for (int[] tabPair : mTabPairs) {
            TextView textView = new TextView(context);
            textView.setId(tabPair[0]);
            textView.setText(tabPair[1]);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.WHITE);
            mTabs.put(tabPair[0], textView);
            addView(textView);
        }

        setTabSelectedUI();
    }

    private void initClickListeners() {
        for (int i = 0; i < mTabs.size(); i++) {
            TextView tabView = mTabs.get(i);
            if (null != tabView) {
                tabView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != mTabClickListener) {
                            mTabClickListener.onClick(view, view.getId());
                        }
                        mSelectedTabIndex = view.getId();
                        setTabSelectedUI();
                    }
                });
            }
        }
    }

    private void setTabSelectedUI() {
        for (int i = 0; i < mTabs.size(); i++) {
            TextView tabView = mTabs.get(i);
            if (i == mSelectedTabIndex) {
                tabView.setBackgroundResource(R.drawable.tab_selected_background);
            } else {
                tabView.setBackground(null);
            }
        }
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        mTabClickListener = onTabClickListener;
    }

}
