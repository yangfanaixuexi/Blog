package com.yf.vo;

public class BlogQuery {
    private String title;
    private Long typeId;
    private Boolean recommend;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "BlogQuery{" +
                "title='" + title + '\'' +
                ", typeId=" + typeId +
                ", recommend=" + recommend +
                '}';
    }
}
