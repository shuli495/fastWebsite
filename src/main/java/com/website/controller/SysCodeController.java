package com.website.controller;

import com.fastjavaframework.base.BaseController;
import com.fastjavaframework.exception.ThrowPrompt;
import com.fastjavaframework.util.UUID;
import com.fastjavaframework.util.VerifyUtils;
import com.website.model.bo.SysCodeBO;
import com.website.service.SysCodeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典表
 *
 * @author wangshuli
 */
@RestController
@RequestMapping(value="/sysCode")
public class SysCodeController extends BaseController<SysCodeService> {

	/**
	 * 创建
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Object create(@RequestBody SysCodeBO bo) {
		if(VerifyUtils.isEmpty(bo.getId())) {
			bo.setId(UUID.uuid());
		}

		this.service.insert(bo);
		return success(bo.getId());
	}

	/**
	 * 批量创建
	 */
	@RequestMapping(value="/batch",method=RequestMethod.POST)
	public Object createBatch(@RequestBody List<SysCodeBO> boList) {
		if(boList.size() == 0) {
			throw new ThrowPrompt("无创建内容！");
		}

		for(SysCodeBO bo : boList) {
		bo.setId(UUID.uuid());
		}

		this.service.baseInsertBatch(boList);
		return success();
	}

	/**
	 * 更新
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public Object update(@RequestBody SysCodeBO bo) {
		this.service.baseUpdate(bo);
		return success();
	}

	/**
	 * 批量更新
	 */
	@RequestMapping(value="/batch",method=RequestMethod.PUT)
	public Object updateBatch(@RequestBody List<SysCodeBO> boList) {
		if(boList.size() == 0) {
			throw new ThrowPrompt("无修改内容！");
		}

		this.service.baseUpdateBatch(boList);
		return success();
	}

	/**
	 * 物理删除
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public Object delete(@PathVariable String id) {
		this.service.delete(id);
		return success();
	}

	/**
	 * 批量物理删除
	 */
	@RequestMapping(value="/batch",method=RequestMethod.DELETE)
	public Object deleteBatch(@RequestBody List<String> idList) {
		if(idList.size() == 0) {
			throw new ThrowPrompt("无删除内容！");
		}

		this.service.baseDeleteBatch(idList);
		return success();
	}

	/**
	 * id查询详情
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public Object findById(@PathVariable String id) {
		return success(this.service.baseFind(id));
	}

	/**
	 * 列表查询 and条件
	 */
	@RequestMapping(method=RequestMethod.GET)
	public Object list(@RequestParam(required=false) String parentId,
			@RequestParam(required=false) String groupId) {
		SysCodeBO bo = new SysCodeBO();
		bo.setParentId(parentId);
		bo.setGroupId(groupId);

		return success(this.service.baseQueryByAnd(bo));
	}

	/**
	 * 分页查询 and条件
	 */
	@RequestMapping(value="/page",method=RequestMethod.POST)
	public Object page(@RequestBody SysCodeBO bo) {
		return success(this.service.baseQueryPageByAnd(bo));
	}

}