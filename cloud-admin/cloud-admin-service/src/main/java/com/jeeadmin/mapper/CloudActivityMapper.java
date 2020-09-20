package com.jeeadmin.mapper;

import com.jeeadmin.entity.CloudActivity;
import com.jeeadmin.vo.activity.CloudActivityVo;
import com.jeerigger.frame.base.mapper.BaseMapper;

import java.util.List;

/**
 * @author Seven Lee
 * @description
 *      活动mapper接口
 * @date 2020/9/8
**/
public interface CloudActivityMapper extends BaseMapper<CloudActivity> {

    /**
     *      根据用户ID查询已经发起的活动
     */
    List<CloudActivityVo> selectByUserId(Long id);


}