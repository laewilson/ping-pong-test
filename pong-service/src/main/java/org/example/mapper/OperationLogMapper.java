package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.model.OperationLog;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

}