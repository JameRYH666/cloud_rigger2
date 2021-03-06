package com.jeeadmin.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户分配角色VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AssignRoleVo {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 用户分配角色
     */
    @ApiModelProperty(value = "用户分配角色")
    private List<Long> roleIdList;
}
