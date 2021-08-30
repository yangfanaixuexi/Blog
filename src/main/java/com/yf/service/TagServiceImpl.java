package com.yf.service;

import antlr.StringUtils;
import com.yf.NotFoundException;
import com.yf.dao.TagRepository;
import com.yf.po.Blog;
import com.yf.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagRepository tagRepository;

    private List<String> TagId2Str = new ArrayList<>();

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getById(id);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Tag> listTag() {
        List<Tag> listTag = tagRepository.findAll();

        return findPublishTags(listTag);
    }

    private List<Long> convertToList(String ids)
    {
        List<Long> list = new ArrayList<>();
        if(!"".equals(ids) && ids != null)
        {
            String [] idarray = ids.split(",");
            for (String s : idarray) {
                if(HasDigit(s) && isDigit(s))   // 含有数字且都是数字
                {
                    list.add(new Long(s));
                }
                else    // 含有中文
                {
                    TagId2Str.add(s);
                }
            }
        }
        return list;
    }

    @Override
    public List<Tag> listTag(String ids) { //1,2,3.... id=>传入的字符串,只需要按照逗号分割字符串再转换为列表形式即可
//        return tagRepository.findAllById(this.convertToList(ids));
        TagId2Str = new ArrayList<>();
        return QuerylistTag(ids);   // 返回该文章的标签
    }

    @Override
    public List<Tag> QuerylistTag(String ids)
    {
        boolean flag = true ;
        List<Long> postList = this.convertToList(ids);
        List<Tag> tags = tagRepository.findAllById(postList);
        for(Long tagId : postList)
        {
            for(Tag tag : tags)
            {
                if(tagId.equals(tag.getId()))
                {
                    flag = false;
                    break;
                }
            }
            // 如果flag = true,说明该ID不存在于tagsId中,则进行插入操作
            if(flag)
            {
                Tag addTag = new Tag();
                addTag.setName(tagId.toString());
                this.saveTag(addTag);
                addTag = tagRepository.findTagByName(addTag.getName());
                tags.add(addTag);
            }

            else
            {
                flag = true;
            }
        }

        for(String TagName : TagId2Str)
        {
            Tag addTag = new Tag();
            addTag.setName(TagName);
            this.saveTag(addTag);
            addTag = tagRepository.findTagByName(addTag.getName());
            tags.add(addTag);
        }

        return tags;
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);

        return findPublishTags(tagRepository.findTop(pageable));
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        // 首先判断是否存在
        Tag findTag = tagRepository.getById(id);
        if(findTag == null)
        {
            throw new NotFoundException("不存在该类型的标签");
        }
        // 对象之间进行赋值, 将findTag的属性赋给tag BeanUtils.copyProperties("转换前的类", "转换后类")
        BeanUtils.copyProperties(tag, findTag);

        return tagRepository.save(tag);

    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findTagByName(name);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteTagById(id);
    }

    private List<Tag> findPublishTags(List<Tag> tags)
    {
        int number = 0;
        for(Tag tag : tags)
        {
            List<Blog> BlogList = tag.getBlogs();
            for(number = 0; number < BlogList.size(); number++)
            {
                if(!BlogList.get(number).isPublished())
                {
                    BlogList.remove(BlogList.get(number));
                }
            }
        }
        return tags;

    }

    // 是否含有数字
    private boolean HasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    // 判断一个字符串是否都为数字
    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }
}

