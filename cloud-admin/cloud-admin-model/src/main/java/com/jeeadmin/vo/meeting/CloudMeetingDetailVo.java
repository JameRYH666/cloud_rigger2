package com.jeeadmin.vo.meeting;

import com.jeeadmin.entity.CloudMeeting;
import com.jeeadmin.entity.CloudPartyMember;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @Author Seven Lee
 * @Date Create in 2020/9/12 15:44
 * @Description
 **/

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CloudMeetingDetailVo extends CloudMeeting {

    private String formName;
    private String typeName;
    /**
     * 组织人员名称
     */
    private String memberName;
    private Date oneCreateDate;
    private String enclosureName;
    private String enclosurePath;
    /**
     * 参会人员名称
     */
    private List<String> joinMemberName;

}