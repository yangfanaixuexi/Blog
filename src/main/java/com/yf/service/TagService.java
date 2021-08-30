package com.yf.service;

import com.yf.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    List<Tag> QuerylistTag(String ids);

    List<Tag> listTagTop(Integer size);

    Tag updateTag(Long id, Tag tag);

    Tag getTagByName(String name);

    void deleteTag(Long id);
}
