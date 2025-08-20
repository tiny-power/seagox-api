package com.seagox.lowcode.assets.entity;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
 
/**
* 计提折旧
*/
public class ProvisionDepreciation {
	
	/**
     * id
     */
    private Long id;
	
    /**
    * 所属公司
    */ 
    private Long companyId;
	
    /**
    * 创建者
    */ 
    private Long userId;
	
    /**
    * 申请日期
    */ 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date applyDate;
	
    /**
    * 折旧账期
    */ 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date period;
	
    /**
    * 变动资产计提起点
    */ 
    private Integer changeProvision;
	
    /**
    * 处置资产的计提起点
    */ 
    private Integer disposeProvision;
	
    /**
    * 新增资产的计提起点
    */ 
    private Integer insertProvision;
	
    /**
    * 备注
    */ 
    private String remark;
	
    /**
    * 经办人
    */ 
    private String operator;
	
    /**
    * 经办人电话
    */ 
    private String operatorPhone;
	
    /**
    * 资产原值总额
    */ 
    private Double originalValueTotal;
	
    /**
    * 本期折旧/摊销总额
    */ 
    private Double depreciationPeriodTotal;
	
    /**
    * 累计折旧/摊销总额
    */ 
    private Double accumulatedDepreciationTotal;
	
    /**
    * 净值
    */ 
    private Double netWorthTotal;
    
    /**
     * 状态(0:未确认;1:已确认;)
     */ 
     private Integer status;
	
	/**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    /**
     * 折旧资产
     */
    @TableField(exist = false)
    private String assetsStr;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

    public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

    public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

    public Integer getChangeProvision() {
		return changeProvision;
	}

	public void setChangeProvision(Integer changeProvision) {
		this.changeProvision = changeProvision;
	}

    public Integer getDisposeProvision() {
		return disposeProvision;
	}

	public void setDisposeProvision(Integer disposeProvision) {
		this.disposeProvision = disposeProvision;
	}

    public Integer getInsertProvision() {
		return insertProvision;
	}

	public void setInsertProvision(Integer insertProvision) {
		this.insertProvision = insertProvision;
	}

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

    public String getOperatorPhone() {
		return operatorPhone;
	}

	public void setOperatorPhone(String operatorPhone) {
		this.operatorPhone = operatorPhone;
	}

    public Double getOriginalValueTotal() {
		return originalValueTotal;
	}

	public void setOriginalValueTotal(Double originalValueTotal) {
		this.originalValueTotal = originalValueTotal;
	}

    public Double getDepreciationPeriodTotal() {
		return depreciationPeriodTotal;
	}

	public void setDepreciationPeriodTotal(Double depreciationPeriodTotal) {
		this.depreciationPeriodTotal = depreciationPeriodTotal;
	}

    public Double getAccumulatedDepreciationTotal() {
		return accumulatedDepreciationTotal;
	}

	public void setAccumulatedDepreciationTotal(Double accumulatedDepreciationTotal) {
		this.accumulatedDepreciationTotal = accumulatedDepreciationTotal;
	}

    public Double getNetWorthTotal() {
		return netWorthTotal;
	}

	public void setNetWorthTotal(Double netWorthTotal) {
		this.netWorthTotal = netWorthTotal;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getAssetsStr() {
		return assetsStr;
	}

	public void setAssetsStr(String assetsStr) {
		this.assetsStr = assetsStr;
	}
	
}