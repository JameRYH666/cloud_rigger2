package com.jeeadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.api.ICloudOrgService;
import com.jeeadmin.api.ICloudPartyMemberService;
import com.jeeadmin.api.ICloudUserOrgService;
import com.jeeadmin.entity.CloudOrg;
import com.jeeadmin.entity.CloudPartyMember;
import com.jeeadmin.entity.CloudUserOrg;
import com.jeeadmin.mapper.CloudOrgMapper;
import com.jeeadmin.vo.org.CloudOrgTree;
import com.jeeadmin.vo.org.CloudOrgVo;
import com.jeerigger.core.common.core.SnowFlake;
import com.jeerigger.core.module.sys.SysConstant;
import com.jeerigger.core.module.sys.mapper.OrgMapper;
import com.jeerigger.core.module.sys.util.SysDictUtil;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.service.impl.BaseTreeServiceImpl;
import com.jeerigger.frame.enums.StatusEnum;
import com.jeerigger.frame.exception.FrameException;
import com.jeerigger.frame.exception.ValidateException;
import com.jeerigger.frame.page.PageHelper;
import com.jeerigger.frame.support.validate.ValidateUtil;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Seven Lee
 * @description
 *      组织机构表 服务实现类
 * @date 2020/9/9
**/
@Service
public class CloudOrgServiceImpl extends BaseTreeServiceImpl<CloudOrgMapper, CloudOrg> implements ICloudOrgService {
    @Autowired
    private ICloudPartyMemberService sysUserService;
    @Autowired
    private ICloudUserOrgService sysOrgAdminOrgService;
    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private CloudOrgMapper cloudOrgMapper;

    @Override
    public List<CloudOrg> selectChildOrg(Long orgId) {
        QueryWrapper<CloudOrg> wrapper = new QueryWrapper<>();
        if (Objects.isNull(orgId)) {
            orgId = 0L;
        }
        wrapper.lambda().eq(CloudOrg::getParentId, orgId);
        wrapper.lambda().orderByAsc(CloudOrg::getParentId, CloudOrg::getOrgSort);
        return this.getListOrg(wrapper);
    }
    /**
     * @Author: Sgz
     * @Time: 14:27 2020/9/11
     * @Params: [sysOrg]
     * @Return: java.util.List<com.jeeadmin.entity.CloudOrg>
     * @Throws:
     * @Description:
     *  这里是多条件查询，其中
     *      可以根据组织机构代码org_code进行精确查询
     *      可以根据组织名称org_name进行模糊查询
     *      可以根据机构简称org_short_name进行模糊查询
     *      可以根据机构类型org_type进行模糊查询
     *      当没有这这几个数据的时候，就会进行全部查询，并且根据parentId和org_sort进行排序
     *
     */
    @Override
    public Page<CloudOrgVo> selectOrgList(PageHelper<CloudOrg> pageHelper) {

        Page page = new Page(pageHelper.getCurrent(), pageHelper.getSize());
        ArrayList<CloudOrgVo> cloudOrgVos = new ArrayList<>();
        QueryWrapper<CloudOrg> wrapper = new QueryWrapper<>();
        CloudOrg sysOrg = pageHelper.getData();

        if (pageHelper.getData() != null) {

            if (StringUtil.isNotEmpty(sysOrg.getOrgCode())) {
                wrapper.lambda().eq(CloudOrg::getOrgCode, sysOrg.getOrgCode());
            }
            if (StringUtil.isNotEmpty(sysOrg.getOrgName())) {
                wrapper.lambda().like(CloudOrg::getOrgName, sysOrg.getOrgName());
            }
            if (StringUtil.isNotEmpty(sysOrg.getOrgShortName())) {
                wrapper.lambda().like(CloudOrg::getOrgShortName, sysOrg.getOrgShortName());
            }
            if (StringUtil.isNotEmpty(sysOrg.getOrgTypeCode())) {
                wrapper.lambda().like(CloudOrg::getOrgTypeCode, sysOrg.getOrgTypeCode());
            }
            if (StringUtil.isNotEmpty(sysOrg.getOrgStatus())) {
                wrapper.lambda().eq(CloudOrg::getOrgStatus, sysOrg.getOrgStatus());
            }
        }
        wrapper.lambda().orderByAsc(CloudOrg::getParentId, CloudOrg::getOrgSort);
        IPage<CloudOrg> cloudOrgIPage = this.page(page, wrapper);
        List<CloudOrg> listOrg = cloudOrgIPage.getRecords();
        List<CloudOrg> orgList = this.getListOrg(wrapper);

        if (Objects.isNull(listOrg)) {
            throw new ValidateException("没有获取到数据信息");
        }
        Long orgsCount = (long) orgList.size();
        for (CloudOrg cloudOrg : listOrg) {
            // 通过党支部id获取到党员数量
            Integer count = sysUserService.selectCountByOrgId(cloudOrg.getId());
            // 通过党支部党员的id获取党员的详细信息
            CloudPartyMember cloudPartyMember = sysUserService.getById(cloudOrg.getOrgPartyMemberId());
            CloudOrgVo cloudOrgVo = new CloudOrgVo();
            cloudOrgVo.setOrgAddress(cloudOrg.getOrgAddress())
                    .setLeafFlag(cloudOrg.getLeafFlag())
                    .setOrgCode(cloudOrg.getOrgCode())
                    .setOrgName(cloudOrg.getOrgName())
                    .setOrgShortName(cloudOrg.getOrgShortName())
                    .setOrgSort(cloudOrg.getOrgSort())
                    .setOrgTypeCode(cloudOrg.getOrgTypeCode())
                    .setOrgStatus(cloudOrg.getOrgStatus())
                    .setParentId(cloudOrg.getParentId())
                    .setOrgTypeName(cloudOrg.getOrgTypeName())
                    .setParentOrg(cloudOrg.getParentOrg())
                    .setTopLevel(cloudOrg.getTopLevel())
                    .setOrgTelNumber(cloudOrg.getOrgTelNumber())
                    .setId(cloudOrg.getId())
                    .setCreateDate(cloudOrg.getCreateDate())
                    .setCreateUser(cloudOrg.getCreateUser());
            if (Objects.nonNull(cloudPartyMember)){

                cloudOrgVo.setPhoneNumer(cloudPartyMember.getMemberPhoneNumber())
                        .setOrgLeader(cloudPartyMember.getMemberName())
                        .setCount(count);
            }

            cloudOrgVo
                    .setCount(count);
            cloudOrgVos.add(cloudOrgVo);


        }
        Page<CloudOrgVo> cloudOrgPageVo = new Page<>(pageHelper.getCurrent(),pageHelper.getSize());

        cloudOrgPageVo.setRecords(cloudOrgVos)
                .setTotal(orgsCount);
        return cloudOrgPageVo;

    }



    /**
     * 查询所有的党组织结构，并且以树状菜单进行展示
     * @return
     */
    @Override
    public CloudOrgTree selectOrgTree() {
        CloudOrgTree cloudOrgTree = new CloudOrgTree();
        QueryWrapper<CloudOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CloudOrg::getParentId, 0L);
        CloudOrg cloudOrg = this.getOne(queryWrapper);
        if (Objects.isNull(cloudOrg)){
            throw new ValidateException("父组织信息查询不到");
        }
        cloudOrgTree.setId(cloudOrg.getId());
        cloudOrgTree.setOrgName(cloudOrg.getOrgName());
        List<CloudOrg> cloudOrgs = this.selectChild(cloudOrg.getId());
        if (Objects.nonNull(cloudOrgs) && cloudOrgs.size()>0){
            cloudOrgTree.setOrgChilds(cloudOrgs);
            return cloudOrgTree;
        }

        throw new ValidateException("党组织信息查询不到");


    }


/*
    *//**
     * 查询所有的党组织机构详情，并进行分页处理
     * @param pageHelper
     * @return
     */
    @Override
    public Page<CloudOrgVo> selectAll(PageHelper<CloudOrg> pageHelper) {
        Page<CloudOrg> page = new Page<>(pageHelper.getCurrent(),pageHelper.getSize());


        QueryWrapper<CloudOrg> wrapper = new QueryWrapper<CloudOrg>();
        ArrayList<CloudOrgVo> cloudOrgVos = new ArrayList<>();

            wrapper.lambda().ne(CloudOrg::getOrgStatus,StatusEnum.DISABLE.getCode());
            wrapper.lambda().orderByAsc(CloudOrg::getParentId);
        IPage<CloudOrg> cloudOrgIPage = this.page(page, wrapper);
        List<CloudOrg> listOrg = cloudOrgIPage.getRecords();

            if (Objects.isNull(listOrg)) {
                throw new ValidateException("没有获取到数据信息");
            }
            for (CloudOrg cloudOrg : listOrg) {
                // 通过党支部id获取到党员数量
                Integer count = sysUserService.selectCountByOrgId(cloudOrg.getId());
                // 通过党支部党员的id获取党员的详细信息
                CloudPartyMember cloudPartyMember = sysUserService.getById(cloudOrg.getOrgPartyMemberId());
                CloudOrgVo cloudOrgVo = new CloudOrgVo();
                cloudOrgVo.setOrgAddress(cloudOrg.getOrgAddress())
                        .setLeafFlag(cloudOrg.getLeafFlag())
                        .setOrgCode(cloudOrg.getOrgCode())
                        .setOrgName(cloudOrg.getOrgName())
                        .setOrgShortName(cloudOrg.getOrgShortName())
                        .setOrgSort(cloudOrg.getOrgSort())
                        .setOrgTypeCode(cloudOrg.getOrgTypeCode())
                        .setOrgStatus(cloudOrg.getOrgStatus())
                        .setParentId(cloudOrg.getParentId())
                        .setOrgTypeName(cloudOrg.getOrgTypeName())
                        .setParentOrg(cloudOrg.getParentOrg())
                        .setTopLevel(cloudOrg.getTopLevel())
                        .setOrgTelNumber(cloudOrg.getOrgTelNumber())
                        .setId(cloudOrg.getId())
                        .setCreateDate(cloudOrg.getCreateDate())
                        .setCreateUser(cloudOrg.getCreateUser());
                if (Objects.nonNull(cloudPartyMember)){

                            cloudOrgVo.setPhoneNumer(cloudPartyMember.getMemberPhoneNumber())
                            .setOrgLeader(cloudPartyMember.getMemberName())
                            .setCount(count);
                }

                cloudOrgVo
                        .setCount(count);
               cloudOrgVos.add(cloudOrgVo);


            }
          Page<CloudOrgVo> cloudOrgPageVo = new Page<>(pageHelper.getCurrent(),pageHelper.getSize());


            return cloudOrgPageVo.setRecords(cloudOrgVos);


    }

    /**
     * @param orgName
     * @Author: Sgz
     * @Time: 9:37 2020/9/10
     * @Params: [orgName]
     * @Return: CloudOrg
     * @Throws:
     * @Description: 根据orgName 查询组织信息
     */
    @Override
    public CloudOrg selectOrgByOrgName(String orgName) {
        QueryWrapper<CloudOrg> wrapper = new QueryWrapper<>();
        if (Objects.isNull(orgName)){
            throw new ValidateException("党支部的名字不能为空");
        }
             wrapper.lambda().eq(CloudOrg::getOrgName, orgName);
            CloudOrg cloudOrg = this.getOne(wrapper);
        if (Objects.isNull(cloudOrg) ){
                throw  new ValidateException("该党支部不存在");
            }

        return cloudOrg;
    }

    private List<CloudOrg> getListOrg(QueryWrapper<CloudOrg> wrapper) {
        List<CloudOrg> listOrg = this.list(wrapper);
        for (CloudOrg sysOrg : listOrg) {
            if (StringUtil.isNotEmpty(sysOrg.getOrgTypeCode())) {
                String orgTypeName = SysDictUtil.getDictLable(SysConstant.SYS_ORG_TYPE, sysOrg.getOrgTypeCode());
                sysOrg.setOrgTypeName(orgTypeName);
            }
        }
        return listOrg;
    }

    @Override
    public CloudOrg detailOrg(Long orgId) {
        CloudOrg sysOrg = this.getById(orgId);
        if (sysOrg != null && Objects.nonNull(sysOrg.getParentId()) && !sysOrg.getParentId().equals(0L)) {
            sysOrg.setParentOrg(this.getById(sysOrg.getParentId()));
        }
        return sysOrg;
    }

    @Override
    public CloudOrg saveSysOrg(CloudOrg sysOrg) {
        // 通过安全框架拿取userId
        sysOrg.setCreateUser(SecurityUtil.getUserId());
        sysOrg.setId(snowFlake.nextId());
        if (Objects.isNull(sysOrg.getParentId())) {
            sysOrg.setParentId(0L);
        }
        //新增设置状态为正常
        sysOrg.setOrgStatus(StatusEnum.NORMAL.getCode());

        //校验业务数据
        ValidateUtil.validateObject(sysOrg);
        //验证上级组织机构
        if (sysOrg.getParentId()!=0 ){

        validateParentUuid(sysOrg.getParentId());
        }
        //校验组织机构代码是否已存在
        validateOrgCode(sysOrg);
        //验证同一级下名称是否存在
        validateOrgName(sysOrg);
        if (this.save(sysOrg)) {
            return sysOrg;
        } else {
            throw new FrameException(ResultCodeEnum.ERROR_SAVE_FAIL, "新增组织机构信息失败！");
        }
    }


    /**
     * 验证同一级下名称是否存在
     */
    private void validateOrgName(CloudOrg sysOrg) {
        QueryWrapper<CloudOrg> queryWrapper = new QueryWrapper<CloudOrg>();
        if (Objects.nonNull(sysOrg.getId())) {
            queryWrapper.lambda().ne(CloudOrg::getId, sysOrg.getId());
        }
        queryWrapper.lambda().eq(CloudOrg::getParentId, sysOrg.getParentId());
        queryWrapper.lambda().and(wrapper -> wrapper.eq(CloudOrg::getOrgName, sysOrg.getOrgName()).or().eq(CloudOrg::getOrgShortName, sysOrg.getOrgShortName()));
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("组织机构名称或组织机构简称已存在，请核实！");
        }
    }

    /**
     * 验证上级组织机构是否存在
     *
     * @param orgId
     */
    private void validateParentUuid(Long orgId) {
        if (Objects.nonNull(orgId) && !orgId.equals("0")) {
            if (this.getById(orgId) == null) {
                throw new ValidateException("选择的上级组织机构不存在！");
            }
        }
    }

    @Override
    public boolean updateSysOrg(CloudOrg sysOrg) {
        if (this.getById(sysOrg.getId()) == null) {
            throw new ValidateException("该组织机构信息已不存在，不能进行编辑！");
        }
        //校验数据
        ValidateUtil.validateObject(sysOrg);
        //验证上级组织机构
        validateParentUuid(sysOrg.getParentId());
        //校验组织机构代码是否已存在
        validateOrgCode(sysOrg);
        //验证同一级下组织机构名称是否存在
        validateOrgName(sysOrg);
        return this.updateById(sysOrg);
    }

    @Override
    public boolean updateStatus(CloudOrg sysOrg) {
        if (this.getById(sysOrg.getId()) == null) {
            throw new ValidateException("更新的组织机构不存在！");
        }
        List childrenPkList = this.getChildrenPk(sysOrg.getId());
        childrenPkList.add(sysOrg.getId());
        UpdateWrapper<CloudOrg> updateWrapper = new UpdateWrapper<CloudOrg>();
        updateWrapper.lambda().set(CloudOrg::getOrgStatus, sysOrg.getOrgStatus());
        updateWrapper.lambda().in(CloudOrg::getId, childrenPkList);
        boolean updateFlag = this.update(new CloudOrg(), updateWrapper);
        if (updateFlag) {
            //如果将当前节点启用则将所有父级节点也进行启用
            if (sysOrg.getOrgStatus().equals(StatusEnum.NORMAL.getCode())) {
                List parentPkList = this.getParentPk(sysOrg.getId());
                if (parentPkList != null && parentPkList.size() > 0) {
                    UpdateWrapper<CloudOrg> updateParentWrapper = new UpdateWrapper<CloudOrg>();
                    updateParentWrapper.lambda().set(CloudOrg::getOrgStatus, StatusEnum.NORMAL.getCode());
                    updateParentWrapper.lambda().in(CloudOrg::getId, parentPkList);
                    this.update(new CloudOrg(), updateParentWrapper);
                }
            }
        }
        return updateFlag;
    }


    /**
     * 添加或者更新的时候校验组织机构代码是否已存在
     *
     * @param sysOrg
     */
    private void validateOrgCode(CloudOrg sysOrg) {
        QueryWrapper<CloudOrg> queryWrapper = new QueryWrapper<CloudOrg>();
        queryWrapper.lambda().eq(CloudOrg::getOrgCode, sysOrg.getOrgCode());
        if (Objects.nonNull(sysOrg.getId())) {
            queryWrapper.lambda().ne(CloudOrg::getId, sysOrg.getId());
        }
        if (this.count(queryWrapper) > 0) {
            throw new ValidateException("组织机构代码（" + sysOrg.getOrgCode() + "）已存在！");
        }
    }

    @Override
    public boolean deleteSysOrg(Long orgId) {
        //判断是否存在下级组织机构
        List<Long> list = this.getChildrenPk(orgId);
        if (list != null && list.size() > 0) {
            throw new ValidateException("请先删除下级组织结构！");
        }
        //判断该组织机构是否已与人员绑定
        QueryWrapper<CloudPartyMember> userWrapper = new QueryWrapper<CloudPartyMember>();
        userWrapper.lambda().eq(CloudPartyMember::getId, orgId);
        if (sysUserService.count(userWrapper) > 0) {
            throw new ValidateException("该组织机构已与用户绑定不能删除！");
        }
        QueryWrapper<CloudUserOrg> orgAdminOrgWrapper = new QueryWrapper<CloudUserOrg>();
        orgAdminOrgWrapper.lambda().eq(CloudUserOrg::getOrgId, orgId);
        if (sysOrgAdminOrgService.count(orgAdminOrgWrapper) > 0) {
            throw new ValidateException("该组织机构已与组织机构管理员绑定不能删除！");
        }

        return this.removeById(orgId);
    }


}
