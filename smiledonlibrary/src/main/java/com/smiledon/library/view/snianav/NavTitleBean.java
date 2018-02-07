package com.smiledon.library.view.snianav;

import com.smiledon.library.R;

/**
 * Created by zhaidong on 2017/9/11.
 */

public class NavTitleBean {

    private String title;
    private String titleId;
    private String titleDesc;
    private int imageId;

    public NavTitleBean(){

    }

    public NavTitleBean( String title,  String titleId){
        this(title, titleId, title);

    }

    public NavTitleBean( String title,  String titleId,  String titleDesc){
        this.title = title;
        this.titleId = titleId;
        this.titleDesc = titleDesc;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTitleDesc() {
        return titleDesc;
    }

    public void setTitleDesc(String titleDesc) {
        this.titleDesc = titleDesc;
    }

    public int getImageId() {
        return R.drawable.xlistview_arrow;
    }
}
