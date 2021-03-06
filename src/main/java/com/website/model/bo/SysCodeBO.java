package com.website.model.bo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fastjavaframework.base.BaseBean;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

/**
 * @author wangshuli
 */
public class SysCodeBO extends BaseBean {
	private static final long serialVersionUID = 1L;

	private String id;	

	@Size(max=64, min=0, message="{ma_sys_code.parentId.size}")
	private String parentId;	

	@Size(max=64, min=0, message="{ma_sys_code.groupId.size}")
	private String groupId;	

	@Size(max=255, min=0, message="{ma_sys_code.code.size}")
	private String code;	

	@Size(max=255, min=0, message="{ma_sys_code.value.size}")
	private String value;	

	@Max(value=999999999, message="{ma_sys_code.sqeuence.max}")
	private Integer sqeuence;	

	private Boolean enable;

	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		json.putAll((JSONObject)JSON.toJSON(this));
		json.putAll(JSONObject.parseObject(super.toString()));
		return json.toJSONString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSqeuence() {
		return sqeuence;
	}

	public void setSqeuence(Integer sqeuence) {
		this.sqeuence = sqeuence;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

}