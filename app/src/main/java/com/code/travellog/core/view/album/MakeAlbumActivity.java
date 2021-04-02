package com.code.travellog.core.view.album;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.code.travellog.AI.AiBoostManager;
import com.code.travellog.R;
import com.code.travellog.config.Constants;
import com.code.travellog.core.view.album.AlbumMakeFragment;
import com.code.travellog.core.view.album.AlbumResultFragment;
import com.luck.picture.lib.entity.LocalMedia;
import com.mvvm.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/3/7
 */
public class MakeAlbumActivity extends BaseActivity {

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
        return R.layout.activity_makealbum;
    }

    private AlbumMakeFragment albumMakeFragment;
    private AlbumResultFragment albumResultFragment;
    private int workid;
    private List<LocalMedia> localMediaList;
    private AiBoostManager aiBoostManager = null;
    private String description ;
    private String title ;
    private boolean isShare;
    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        initToolBar();
        initFragment(0);
        aiBoostManager = AiBoostManager.newInstance();
        aiBoostManager.initialize(this, "mobilenet_quant.tflite",
                1001, "mobilenet_quant_labels.txt");

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
    public AiBoostManager getAiBoostManager() {
        return aiBoostManager;
    }
    public void setAlbumDescription(String description){
        this.description = description ;
    }
    public String getAlbumDescription(){
        return description ;
    }
    public void setAlbumTitle(String title){
        this.title = title ;
    }
    public String getAlbumTitle(){
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
                if (albumMakeFragment == null) {
                    albumMakeFragment = AlbumMakeFragment.newInstance();
                    fragmentTransaction.add(R.id.fragment_content, albumMakeFragment, Constants.ALBUMMAKE_TAG);
                } else fragmentTransaction.show(albumMakeFragment);
                break;
            case 1:
                if (albumResultFragment == null) {
                    albumResultFragment = AlbumResultFragment.newInstance();
                    fragmentTransaction.add(R.id.fragment_content, albumResultFragment, Constants.ALBUMRESULT_TAG);
                } else fragmentTransaction.show(albumResultFragment);
                break;
            default:
                break;
        }
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (albumMakeFragment != null) {
            fragmentTransaction.hide(albumMakeFragment);
        }

        if (albumResultFragment != null) {
            fragmentTransaction.hide(albumResultFragment);
        }
        if (albumResultFragment != null){
            fragmentTransaction.hide(albumResultFragment);
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
