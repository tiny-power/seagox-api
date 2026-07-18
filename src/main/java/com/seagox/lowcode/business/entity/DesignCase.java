package com.seagox.lowcode.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 案例库
 */
public class DesignCase {

    /**
     * 主键
     */
    private Long id;

    /**
     * 案例标题
     */
    private String title;

    /**
     * 封面图片地址
     */
    private String coverUrl;

    /**
     * 案例简介富文本
     */
    private String summary;

    /**
     * 风格列表
     */
    private String styleList;

    /**
     * 地区
     */
    private String regionText;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 建筑面积
     */
    private BigDecimal buildingArea;

    /**
     * 层数
     */
    private Integer floorCount;

    /**
     * 屋顶类型
     */
    private String roofType;

    /**
     * 特殊列表
     */
    private String specialList;

    /**
     * 状态：0草稿，1已发布，2已下架
     */
    private Integer status;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 修改人
     */
    private Long updatedBy;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStyleList() {
        return styleList;
    }

    public void setStyleList(String styleList) {
        this.styleList = styleList;
    }

    public String getRegionText() {
        return regionText;
    }

    public void setRegionText(String regionText) {
        this.regionText = regionText;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public BigDecimal getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(BigDecimal buildingArea) {
        this.buildingArea = buildingArea;
    }

    public Integer getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(Integer floorCount) {
        this.floorCount = floorCount;
    }

    public String getRoofType() {
        return roofType;
    }

    public void setRoofType(String roofType) {
        this.roofType = roofType;
    }

    public String getSpecialList() {
        return specialList;
    }

    public void setSpecialList(String specialList) {
        this.specialList = specialList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
