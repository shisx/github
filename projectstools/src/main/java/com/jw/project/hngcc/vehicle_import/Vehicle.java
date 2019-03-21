package com.jw.project.hngcc.vehicle_import;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Desc
 *
 * @author Brook
 */
@Getter
@Setter
@ToString
class Vehicle {

    private String mhyplate;      // 车牌号码
    private String deviceId;      // 设备号
    private String sim;      // SIM
    private String createTime;      // 创建时间
    private String elno;      // 环保标识
    private String emissionlevel;      // 排放阶段
    private String ngsmoke;      // 烟度值
    private String mhybrand;      // 车辆品牌
    private String mhytype;      // 机械类型
    private String mhycolor;      // 车身颜色
    private String mhymodel;      // 机械型号
    private String mhyframeNo;      // 车架号
    private String ngno;      // 发动机编号
    private String ownerName;      // 联系人
    private String owneridNo;      // 身份证号
    private String ownerMobile;      // 联系人电话
    private String owneraDdress;      // 地址
    private String ngpower;      // 功率
    private String mhyweight;      // 吨位/座位
    private String mhyprice;      // 新车购置价
    private String mhyprodDate;      // 生产年份
    private String mhyrespdept;      // 责任单位
    private String mhyplateColor;      // 车牌颜色
    private String mhyreglocation;      // 车籍地
    private String groupId;      //
    private String longitude;
    private String latitude;

    // 给刘处的
    private String status;
    private String accStatus;
    private String positionAddress;
    private String positionTime;
}
