package com.website.service;

import com.fastjavaframework.base.BaseService;
import com.fastjavaframework.exception.ThrowPrompt;
import com.website.dao.SysCodeDao;
import com.website.model.bo.SysCodeBO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangshuli
 */
@Service
public class SysCodeService extends BaseService<SysCodeDao,SysCodeBO> {

	
	/**
	 * 新增code
	 * @param bo SysCodeBO
	 * @return 新增数量
	 */
	public int insert(SysCodeBO bo) {
		SysCodeBO isExist = super.baseFind(bo.getId());
		if(null != isExist) {
			throw new ThrowPrompt("当前id已存在，请重新输入！");
		}
		
		return super.baseInsert(bo);
	}
	
	/**
	 * 删除节点及子节点
	 * @param id
	 * @return 删除数量
	 */
	public int delete(String id) {
		//删除 当前节点
		super.baseDelete(id);
		
		//删除子节点
		List<String> ids = new ArrayList<>();
		setDelIds(ids, id);
		int delNum = super.baseDeleteBatch(ids);
		
		return delNum + 1;
	}
	
	/**
	 * 设置子节点的id
	 * @param ids 保存id集合
	 * @param id  当前结点id
	 */
	private void setDelIds(List<String> ids, String id) {
		SysCodeBO sysCodeBO = new SysCodeBO();
		sysCodeBO.setParentId(id);

		//当前子节点列表
		List<SysCodeBO> codeList = super.baseQueryByAnd(sysCodeBO);

		for(SysCodeBO code : codeList) {
			ids.add(code.getId());
			
			this.setDelIds(ids, code.getId());
		}
		
	}
}