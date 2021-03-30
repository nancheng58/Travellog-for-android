package com.code.travellog.core.view.picture;

import com.ibbhub.album.AlbumPreviewFragment;

/**
 * @description
 * @time 2021/3/29 17:06
 */

public class MyPreviewFragment extends AlbumPreviewFragment {
    @Override
    public void onPageChanged(int position) {
        super.onPageChanged(position);
        if (position<mData.size()){
            String title = (position+1)+"/"+mData.size();
            ((MyPreviewActivity)getActivity()).setSubtitle(title);
        }
    }
}
