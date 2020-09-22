package com.jeeadmin.entity;

import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
/**
* @Author: Ryh
* @Description:
 *              外出报备表的实体
* @Param: [null]
* @Date: Create in 2020/9/18
* @Return: null
* @Throws:
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CloudRecord extends BaseModel<CloudRecord> {

    /**
     *  外出时间
     */
    private Date recordStartTime;

    /**
     *  结束时间
     */
    private Date recordEndTime;

    /**
     *  外出天数
     */
    private Integer recordDay;

    /**
     *  外出地址
     */
    private String recordAddress;

    /**
     *  同行人员
     */
    private String recordWithPerson;

    /**
     *  外出联系人
     */
    private String recordContact;

    /**
     *  外出联系人电话
     */
    private String recordContactPhoneNumber;

    /**
     *  外出代主持工作人
     */
    private String recordJobContact;

    /**
     *  外出代主持工作人电话
     */
    private String recordJobContactPhoneNumber;

    /**
     *  报备类型
     */
    private String typeCode;

    /**
     *  外出发起人
     */
    private Long partyMemberId;

    /**
     *  报备状态
     */
    private String recordStatus;

    /**
     *      备注信息
     */
    private String remark;

    /**
     *  外出事由
     */
    private String recordReason;

    private Date createDate;

}