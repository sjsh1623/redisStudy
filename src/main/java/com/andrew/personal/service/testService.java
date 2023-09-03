package com.andrew.personal.service;

import com.andrew.personal.mapper.testMapper;
import com.andrew.personal.vo.testVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class testService {

    @Autowired
    public testMapper mapper;

    public List<testVo> selectTest() {
        return mapper.selectTest();
    }
}
