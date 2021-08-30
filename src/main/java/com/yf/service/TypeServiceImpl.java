package com.yf.service;

import com.yf.NotFoundException;
import com.yf.dao.TypeRepository;
import com.yf.po.Blog;
import com.yf.po.Comment;
import com.yf.po.Tag;
import com.yf.po.Type;
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

@Service
public class TypeServiceImpl implements TypeService{
    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.getById(id);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Type> listType() {
        return  typeRepository.findAll();
    }

    @Transactional
    @Override
    public List<Type> listPublishType() {
        List<Type> types = typeRepository.findAll();
        return findPublishType(types);
    }

    @Transactional
    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        List<Type> types = typeRepository.findTop(pageable);
        return findPublishType(types);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.getById(id);
        if(t == null)
        {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type, t);

        return typeRepository.save(t);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findTypeByName(name);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteTypeById(id);
    }


    private List<Type> findPublishType(List<Type> types)
    {
        int number = 0;
        for(Type type : types)
        {
            List<Blog> BlogList = type.getBlogs();
            for(number = 0; number < BlogList.size(); number++)
            {
                if(!BlogList.get(number).isPublished())
                {
                    BlogList.remove(BlogList.get(number));
                }
            }
        }
        return types;
    }
}
