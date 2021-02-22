package com.code.travellog.core.vm;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.code.travellog.config.Constants;
import com.code.travellog.core.data.pojo.course.CourseDetailVo;
import com.code.travellog.core.data.pojo.course.CourseListVo;
import com.code.travellog.core.data.source.CourseRepository;
import com.mvvm.base.AbsViewModel;

/**
 * @author：tqzhang on 18/7/31 15:13
 */
public class CourseViewModel extends AbsViewModel<CourseRepository> {


    private MutableLiveData<CourseListVo> mCourseListData;

    private MutableLiveData<CourseDetailVo> mCourseDetailData;

    public CourseViewModel(@NonNull Application application) {
        super(application);
    }


    public void getCourseTypeData() {
        mRepository.loadCourseType();
    }

    public void getCourseList(String fCatalogId, String lastId) {
        mRepository.loadCourseList(fCatalogId, lastId, Constants.PAGE_RN);
    }

    public void getCourseRemList() {
        mRepository.loadCourseRemList();
    }

    private void getCourseDetailData(String id) {
        mRepository.loadCourseDetailData(id);
    }

    private void getCourseDetailRemData(String id,
                                        String fCatalogId,
                                        String sCatalogId,
                                        String teacherId,
                                        String rn) {
        mRepository.loadCourseDetailRemData(id, fCatalogId, sCatalogId, teacherId, rn);

    }

    public void getRequestMerge() {
        mRepository.loadCourseDetailMerge();
    }
}
