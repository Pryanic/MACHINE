package com.vanya.service;

import com.vanya.mapper.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MapperService {

    @Autowired
    private MyMapper myMapper;

}
