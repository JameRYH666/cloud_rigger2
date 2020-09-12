package com.jeeadmin.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.entity.CloudActivity;
import com.jeeadmin.vo.activity.QueryActivityVo;
import com.jeerigger.frame.base.service.BaseService;
import com.jeerigger.frame.page.PageHelper;

import java.util.List;

/**
* @Author: Ryh
* @Description:        活动信息表的实现类
* @Param: [null]
* @Date: Create in 2020/9/9
* @Return: null
* @Throws:
*/
public interface ICloudActivityService extends BaseService<CloudActivity> {

    /***
     *   获取活动信息
     */
    Page<CloudActivity> selectPage(PageHelper<CloudActivity> pageHelper);

    /**
     *      查询单个的活动信息
     * @param
     * @return
     */
    CloudActivity selectOneActivity(Long activityId);

    /**
     *  新增活动数据
     */
    CloudActivity saveActivity(CloudActivity activity);

    /**
     *  修改活动信息
     */
    boolean updateActivity(CloudActivity activity);

    /**
     *  删除活动信息
     */
    boolean deleteActivity(Long activityId);




}