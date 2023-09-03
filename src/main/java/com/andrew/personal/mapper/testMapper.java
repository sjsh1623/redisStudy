package com.andrew.personal.mapper;

import java.util.List;
import com.andrew.personal.vo.testVo;
import groovy.util.logging.Log4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@Log4j
public interface testMapper {
    List<testVo> selectTest();
}
