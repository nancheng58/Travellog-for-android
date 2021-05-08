package com.code.travellog.core.view.plog;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.code.travellog.ai.AiBoostYoloV5Classifier;
import com.code.travellog.R;
import com.code.travellog.config.Constants;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.entity.LocalMedia;
import com.mvvm.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/3/7
 */
public class MakePlogActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_title_bar)
    RelativeLayout rlTitleBar;
    @BindView(R.id.fragment_content)
    FrameLayout fragmentContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_makeplog;
    }

    private PlogMakeFragment plogMakeFragment;
    private PlogResultFragment plogResultFragment;
    private List<LocalMedia> localMediaList;
    private AiBoostYoloV5Classifier aiBoostYoloV5Classifier = null;
    private String description ;
    private String title ;
    private boolean isShare;
    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        initToolBar();
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        initFragment(0);
        aiBoostYoloV5Classifier = AiBoostYoloV5Classifier.newInstance();
        aiBoostYoloV5Classifier.initialize(this, "yolov5s-fp16.tflite",
                86, "coco.txt");

    }
    public void setShare(boolean isShare)
    {
        this.isShare = isShare ;
    }

    public boolean getShare(){
        return isShare ;
    }
    public void setLocalMediaList(List<LocalMedia> localMediaList){
        this.localMediaList = localMediaList ;
    }
    public AiBoostYoloV5Classifier getAiBoostYoloV5Classifier() {
        return aiBoostYoloV5Classifier;
    }
    public void setPlogDescription(String description){
        this.description = description ;
    }
    public String getPlogDescription(){
        return description ;
    }
    public void setPlogTitle(String title){
        this.title = title ;
    }
    public String getPlogTitle(){
        return title;
    }
    public List<LocalMedia> getLocalMediaList(){
        return localMediaList;
    }
    public void initFragment(int i) {
        FragmentManager mfragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mfragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (i) {
            case 0:
                if (plogMakeFragment == null) {
                    plogMakeFragment = PlogMakeFragment.newInstance();
                    fragmentTransaction.add(R.id.fragment_content, plogMakeFragment, Constants.ALBUMMAKE_TAG);
                } else fragmentTransaction.show(plogMakeFragment);
                break;
            case 1:
                if (plogResultFragment == null) {
                    plogResultFragment = PlogResultFragment.newInstance();
                    fragmentTransaction.add(R.id.fragment_content, plogResultFragment, Constants.ALBUMRESULT_TAG);
                } else fragmentTransaction.show(plogResultFragment);
                break;
            default:
                break;
        }
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (plogMakeFragment != null) {
            fragmentTransaction.hide(plogMakeFragment);
        }

        if (plogResultFragment != null) {
            fragmentTransaction.hide(plogResultFragment);
        }
        if (plogResultFragment != null){
            fragmentTransaction.hide(plogResultFragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
