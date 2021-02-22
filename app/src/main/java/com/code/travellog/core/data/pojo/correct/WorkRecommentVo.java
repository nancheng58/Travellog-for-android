package com.code.travellog.core.data.pojo.correct;

import com.code.travellog.core.data.pojo.BaseVo;
import com.code.travellog.core.data.pojo.course.CourseInfoVo;
import com.code.travellog.core.data.pojo.live.LiveRecommendVo;

import java.util.ArrayList;
import java.util.List;

public class WorkRecommentVo extends BaseVo {

    public Data data;

    public static class Data {
        public ArrayList<WorkInfoVo> content;
        public List<CourseInfoVo> course;
        public List<LiveRecommendVo> live;
    }

}
