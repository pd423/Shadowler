package com.inscode.shadowpreviewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.inscode.shadowpreviewer.ui.TabContainer;

public class MainActivity extends AppCompatActivity implements FontSelectDialogFragment.Callback {

    public static final String TAG = "Shadowler";

    private static final int DEFAULT_SEEK_BAR_MAX_DX = 255;
    private static final int DEFAULT_SEEK_BAR_MAX_DY = 255;
    private static final int DEFAULT_SEEK_BAR_MAX_RADIUS = 80;
    private static final int DEFAULT_SEEK_BAR_MAX_COLOR = 255;

    private static final String SEEK_BAR_TAG_RED = "seek_bar_red";
    private static final String SEEK_BAR_TAG_GREEN = "seek_bar_green";
    private static final String SEEK_BAR_TAG_BLUE = "seek_bar_blue";

    private int mCurrentTabIndex = TabContainer.SETTINGS_TAB_DX;

    private TabContainer mTabContainer;

    private RelativeLayout mValueSettingPanel;
    private RelativeLayout mColorSettingPanel;

    private TextView mPreviewTextView;
    private Button mBtnInputText;
    private Button mBtnChangeFont;

    private SeekBar mValueSeekBar;
    private TextView mValueText;
    private Button mBtnSubtract;
    private Button mBtnPlus;

    private SeekBar mColorRedSeekBar;
    private SeekBar mColorGreenSeekBar;
    private SeekBar mColorBlueSeekBar;
    private TextView mColorRedValueText;
    private TextView mColorGreenValueText;
    private TextView mColorBlueValueText;

    private String mText;
    private int mDx = 0;
    private int mDy = 1;
    private int mRadius = 3;
    private int mTextColorRed = 255;
    private int mTextColorGreen = 255;
    private int mTextColorBlue = 255;
    private int mShadowColorRed = 0;
    private int mShadowColorGreen = 0;
    private int mShadowColorBlue = 0;

    private SeekBar.OnSeekBarChangeListener mColorSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }
            String viewTag = String.valueOf(seekBar.getTag());
            switch (mCurrentTabIndex) {
                case TabContainer.SETTINGS_TAB_TEXT_COLOR:
                    switch (viewTag) {
                        case SEEK_BAR_TAG_RED:
                            mTextColorRed = progress;
                            mColorRedValueText.setText(String.valueOf(mTextColorRed));
                            break;
                        case SEEK_BAR_TAG_GREEN:
                            mTextColorGreen = progress;
                            mColorGreenValueText.setText(String.valueOf(mTextColorGreen));
                            break;
                        case SEEK_BAR_TAG_BLUE:
                            mTextColorBlue = progress;
                            mColorBlueValueText.setText(String.valueOf(mTextColorBlue));
                            break;
                    }
                    break;
                case TabContainer.SETTINGS_TAB_SHADOW_COLOR:
                    switch (viewTag) {
                        case SEEK_BAR_TAG_RED:
                            mShadowColorRed = progress;
                            mColorRedValueText.setText(String.valueOf(mShadowColorRed));
                            break;
                        case SEEK_BAR_TAG_GREEN:
                            mShadowColorGreen = progress;
                            mColorGreenValueText.setText(String.valueOf(mShadowColorGreen));
                            break;
                        case SEEK_BAR_TAG_BLUE:
                            mShadowColorBlue = progress;
                            mColorBlueValueText.setText(String.valueOf(mShadowColorBlue));
                            break;
                    }
                    break;
            }
            refreshPreview();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // DO NOTHING
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // DO NOTHING
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = getString(R.string.default_preview_text);

        findViews();
        setViewActions();
        initViews();
        refreshPreview();

        MobileAds.initialize(this, "ca-app-pub-4943010947847600~8271910571");

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void findViews() {
        mTabContainer = findViewById(R.id.tabs);
        mBtnInputText = findViewById(R.id.btn_input_text);
        mBtnChangeFont = findViewById(R.id.btn_change_font);
        mValueSettingPanel = findViewById(R.id.settings_single_value_area);
        mColorSettingPanel = findViewById(R.id.settings_color_area);
        mPreviewTextView = findViewById(R.id.preview_text);
        mValueSeekBar = findViewById(R.id.seek_bar_value);
        mValueText = findViewById(R.id.value_text);
        mBtnSubtract = findViewById(R.id.btn_value_subtract);
        mBtnPlus = findViewById(R.id.btn_value_plus);
        mColorRedSeekBar = findViewById(R.id.seek_bar_r);
        mColorGreenSeekBar = findViewById(R.id.seek_bar_g);
        mColorBlueSeekBar = findViewById(R.id.seek_bar_b);
        mColorRedValueText = findViewById(R.id.text_r_value);
        mColorGreenValueText = findViewById(R.id.text_g_value);
        mColorBlueValueText = findViewById(R.id.text_b_value);
    }

    private void initViews() {
        mCurrentTabIndex = TabContainer.SETTINGS_TAB_DX;
        mValueSeekBar.setMax(DEFAULT_SEEK_BAR_MAX_DX);
        mValueSeekBar.setProgress(mDx);
        mValueText.setText(String.valueOf(mDx));

        mColorRedSeekBar.setMax(DEFAULT_SEEK_BAR_MAX_COLOR);
        mColorGreenSeekBar.setMax(DEFAULT_SEEK_BAR_MAX_COLOR);
        mColorBlueSeekBar.setMax(DEFAULT_SEEK_BAR_MAX_COLOR);

        mColorRedSeekBar.setTag(SEEK_BAR_TAG_RED);
        mColorGreenSeekBar.setTag(SEEK_BAR_TAG_GREEN);
        mColorBlueSeekBar.setTag(SEEK_BAR_TAG_BLUE);
    }

    private void setViewActions() {
        mValueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    switch (mCurrentTabIndex) {
                        case TabContainer.SETTINGS_TAB_DX:
                            mDx = progress;
                            setValueText(mDx);
                            break;
                        case TabContainer.SETTINGS_TAB_DY:
                            mDy = progress;
                            setValueText(mDy);
                            break;
                        case TabContainer.SETTINGS_TAB_RADIUS:
                            mRadius = progress;
                            setValueText(mRadius);
                            break;
                    }
                    refreshPreview();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // DO NOTHING
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // DO NOTHING
            }
        });

        mColorRedSeekBar.setOnSeekBarChangeListener(mColorSeekBarListener);
        mColorGreenSeekBar.setOnSeekBarChangeListener(mColorSeekBarListener);
        mColorBlueSeekBar.setOnSeekBarChangeListener(mColorSeekBarListener);

        mBtnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mCurrentTabIndex) {
                    case TabContainer.SETTINGS_TAB_DX:
                        if (mDx > 1) mDx--;
                        setValueText(mDx);
                        break;
                    case TabContainer.SETTINGS_TAB_DY:
                        if (mDy > 1) mDy--;
                        setValueText(mDy);
                        break;
                    case TabContainer.SETTINGS_TAB_RADIUS:
                        if (mRadius > 1) mRadius--;
                        setValueText(mRadius);
                        break;
                }
                refreshPreview();
            }
        });

        mBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mCurrentTabIndex) {
                    case TabContainer.SETTINGS_TAB_DX:
                        if (mDx < DEFAULT_SEEK_BAR_MAX_DX) mDx++;
                        setValueText(mDx);
                        break;
                    case TabContainer.SETTINGS_TAB_DY:
                        if (mDy < DEFAULT_SEEK_BAR_MAX_DY) mDy++;
                        setValueText(mDy);
                        break;
                    case TabContainer.SETTINGS_TAB_RADIUS:
                        if (mRadius < DEFAULT_SEEK_BAR_MAX_RADIUS) mRadius++;
                        setValueText(mRadius);
                        break;
                }
                refreshPreview();
            }
        });

        mTabContainer.setOnTabClickListener(new TabContainer.OnTabClickListener() {
            @Override
            public void onClick(View view, int position) {
                mCurrentTabIndex = position;
                switch (position) {
                    case TabContainer.SETTINGS_TAB_DX:
                        mValueSeekBar.setMax(DEFAULT_SEEK_BAR_MAX_DX);
                        mValueSeekBar.setProgress(mDx);
                        setValueText(mDx);
                        switchToValueSettingPanel();
                        break;
                    case TabContainer.SETTINGS_TAB_DY:
                        mValueSeekBar.setMax(DEFAULT_SEEK_BAR_MAX_DY);
                        mValueSeekBar.setProgress(mDy);
                        setValueText(mDy);
                        switchToValueSettingPanel();
                        break;
                    case TabContainer.SETTINGS_TAB_RADIUS:
                        mValueSeekBar.setMax(DEFAULT_SEEK_BAR_MAX_RADIUS);
                        mValueSeekBar.setProgress(mRadius);
                        setValueText(mRadius);
                        switchToValueSettingPanel();
                        break;
                    case TabContainer.SETTINGS_TAB_TEXT_COLOR:
                        setTextColorViews();
                        switchToColorSettingPanel();
                        break;
                    case TabContainer.SETTINGS_TAB_SHADOW_COLOR:
                        setShadowColorViews();
                        switchToColorSettingPanel();
                        break;
                }
            }
        });

        mBtnInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_title_input_text);
                final EditText editText = new EditText(MainActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setText(mText);
                builder.setView(editText);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mText = editText.getText().toString();
                        refreshPreview();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        mBtnChangeFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FontSelectDialogFragment.newInstance().show(getFragmentManager(),
                        FontSelectDialogFragment.TAG);
            }
        });
    }

    private void switchToValueSettingPanel() {
        mValueSettingPanel.setVisibility(View.VISIBLE);
        mColorSettingPanel.setVisibility(View.INVISIBLE);
    }

    private void switchToColorSettingPanel() {
        mValueSettingPanel.setVisibility(View.INVISIBLE);
        mColorSettingPanel.setVisibility(View.VISIBLE);
    }

    private void setValueText(int value) {
        setValueText(String.valueOf(value));
    }

    private void setValueText(String text) {
        mValueText.setText(text);
    }

    private void refreshPreview() {
        mPreviewTextView.setText(mText);
        mPreviewTextView.setTextColor(Color.rgb(mTextColorRed, mTextColorGreen, mTextColorBlue));
        mPreviewTextView.setShadowLayer(mRadius, mDx, mDy,
                Color.rgb(mShadowColorRed, mShadowColorGreen, mShadowColorBlue));
    }

    private void setTextColorViews() {
        mColorRedSeekBar.setProgress(mTextColorRed);
        mColorGreenSeekBar.setProgress(mTextColorGreen);
        mColorBlueSeekBar.setProgress(mTextColorBlue);

        mColorRedValueText.setText(String.valueOf(mTextColorRed));
        mColorGreenValueText.setText(String.valueOf(mTextColorGreen));
        mColorBlueValueText.setText(String.valueOf(mTextColorBlue));
    }

    private void setShadowColorViews() {
        mColorRedSeekBar.setProgress(mShadowColorRed);
        mColorGreenSeekBar.setProgress(mShadowColorGreen);
        mColorBlueSeekBar.setProgress(mShadowColorBlue);

        mColorRedValueText.setText(String.valueOf(mShadowColorRed));
        mColorGreenValueText.setText(String.valueOf(mShadowColorGreen));
        mColorBlueValueText.setText(String.valueOf(mShadowColorBlue));
    }

    @Override
    public void onSelectNewFont(Typeface typeface) {
        if (null != mPreviewTextView && null != typeface) {
            mPreviewTextView.setTypeface(typeface);
        }
    }
}
