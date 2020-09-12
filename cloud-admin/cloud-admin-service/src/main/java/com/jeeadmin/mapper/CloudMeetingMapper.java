package com.jeeadmin.mapper;

import com.jeeadmin.vo.meeting.CloudMeetingDetailVo;
import com.jeeadmin.vo.meeting.CloudMeetingVo;
import com.jeerigger.frame.base.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author Seven Lee
 * @description
 *      会议mapper接口
 * @date 2020/9/8
**/
public interface CloudMeetingMapper extends BaseMapper<CloudMeetingVo> {

    /**
     * @author Seven Lee
     * @description
     *      查询复合会议信息
     * @param [ids]
     * @date 2020/9/12
     * @return java.util.List<com.jeeadmin.vo.meeting.CloudMeetingVo>
     * @throws
    **/
    List<CloudMeetingVo> selectAllMeetings(List<Long> ids);

    /**
     * @author Seven Lee
     * @description
     *      查询某个会议详情
     * @param []
     * @date 2020/9/12
     * @return java.util.List<com.jeeadmin.vo.meeting.CloudMeetingDetailVo>
     * @throws
    **/
    CloudMeetingDetailVo selectMeetingDetail(Long id);

}