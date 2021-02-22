package com.code.travellog.core.data.pojo.book;

import com.code.travellog.core.data.pojo.BaseVo;

import java.util.ArrayList;

public class BookListVo extends BaseVo {
    public Data data;

    public static class Data {
        public ArrayList<BookVo> content;
    }

}
