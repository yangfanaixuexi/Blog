package com.yf.service;

import com.yf.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    List<Type> listPublishType();

    List<Type> listTypeTop(Integer size);

    Type updateType(Long id, Type Type);

    Type getTypeByName(String name);

    void deleteType(Long id);
}
