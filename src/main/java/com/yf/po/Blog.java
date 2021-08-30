package com.yf.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity // 说明与数据库建立链接, Entity注解,表示Mysql层,持久化对象
@Table(name= "t_blog" ) // 数据库的表格
public class Blog
{
    @Id                 // 主键
    @GeneratedValue     // 自动生成值
    private Long id;
    private String title;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content; // 大字段类型

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String firstPicture;
    private String flag;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    private Integer views;

    private boolean appreciation;
    private boolean shareStatement;
    private boolean commentable;
    private boolean published;
    private boolean recommend;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creatTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;


    @ManyToOne    // 关系维护端
    private Type type;

    @ManyToMany(cascade = {CascadeType.PERSIST})   // 设置集成关系,级联新增
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();

    @Transient
    private String tagIds;

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Blog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public boolean isAppreciation() {
        return appreciation;
    }

    public void setAppreciation(boolean appreciation) {
        this.appreciation = appreciation;
    }

    public boolean isShareStatement() {
        return shareStatement;
    }

    public void setShareStatement(boolean shareStatement) {
        this.shareStatement = shareStatement;
    }

    public boolean isCommentable() {
        return commentable;
    }

    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void Init()
    {
        this.tagIds = this.tagsToIds(this.getTags());
    }

    // 将id变成1,2,3
    private String tagsToIds(List<Tag> tags)
    {
        if(!tags.isEmpty())
        {
            StringBuilder ids = new StringBuilder();
            boolean flag = false;
            for(Tag tag : tags)
            {
                if(flag)
                {
                    ids.append(",");
                }
                else
                {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        }
        else
        {
            return tagIds;
        }
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", description='" + description + '\'' +
                ", views=" + views +
                ", appreciation=" + appreciation +
                ", shareStatement=" + shareStatement +
                ", commentable=" + commentable +
                ", published=" + published +
                ", recommend=" + recommend +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", tags=" + tags +
                ", user=" + user +
                ", comments=" + comments +
                ", tagIds='" + tagIds + '\'' +
                '}';
    }

}
