package com.jw.project.hngcc.vehicle_import;


import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.jw.project.hngcc.vehicle_import.BuildVehicleInfo.*;


/**
 * 直接从客户提供的excel文档提取出数据保存为exe导入数据
 *
 * @author Brook
 */
class BuildVehicleInfoFromExcel {
    private static Map<String, Vehicle> vehicles;
    private static Map<String, String> devices;


    public static void main(String[] args) throws Exception {
        /*
        注意事项：
        1.源文件身份证号需要转文本类型
         */
        vehicles = getVehicleMap();
        System.out.println("车辆数：" + vehicles.size());
        WriteToExcel.writeVehicle(vehicles, "20190319");
        WriteToExcel.writeDevice(vehicles, "20190319");
    }

    public static Map<String, Vehicle> getVehicleMap() throws Exception {
        File file = new File("E:\\document\\公司文档\\项目\\河南项目\\车辆录入记录\\03-19.xlsx");
        List<List<String>> rowList = ImportFromExcel.loadFromExcel(1531, file);

        build2(rowList);
        return vehicles;
    }

    private static Map<String, Vehicle> build(List<List<String>> rowList) {
        vehicles = new LinkedHashMap<>(rowList.size());
        devices = new LinkedHashMap<>(rowList.size());
        for (List<String> cell : rowList) {
            int i = 0;
            Vehicle vehicle = new Vehicle();
            vehicle.setMhyplate(cell.get(i++));      // 车牌号码
            vehicle.setDeviceId(cell.get(i++));      // 设备号
            vehicle.setSim(cell.get(i++)); // SIM
            vehicle.setCreateTime(ImportFromExcel.getCreateTime(cell.get(i++)));      // 创建时间
            vehicle.setElno(cell.get(i++));      // 环保标识
            vehicle.setMhyplateColor(getMhyPlateColor(cell.get(i++)));      // 车牌颜色
            vehicle.setEmissionlevel(cell.get(i++));      // 排放阶段
            vehicle.setNgsmoke(cell.get(i++));      // 烟度值
            vehicle.setMhybrand(cell.get(i++));      // 车辆品牌
            vehicle.setMhytype(getMhyType(cell.get(i++)));      // 机械类型
            vehicle.setMhycolor(cell.get(i++));      // 车身颜色
            vehicle.setMhymodel(cell.get(i++));      // 机械型号
            vehicle.setMhyframeNo(cell.get(i++));      // 车架号
            vehicle.setNgno(cell.get(i++));      // 发动机编号
            vehicle.setOwnerName(cell.get(i++));      // 联系人
            vehicle.setOwneridNo(cell.get(i++));      // 身份证号
            vehicle.setOwnerMobile(cell.get(i++));      // 联系人电话
            vehicle.setOwneraDdress(cell.get(i++));      // 地址
            vehicle.setNgpower(cell.get(i++));      // 功率
            vehicle.setMhyweight(cell.get(i++));      // 吨位/座位
            vehicle.setMhyprice(cell.get(i++));      // 新车购置价
            vehicle.setMhyprodDate(getMhyProdDate(cell.get(i++)));      // 生产年份
            vehicle.setMhyrespdept(cell.get(i++));      // 责任单位
            vehicle.setMhyreglocation(cell.get(i++));      // 车籍地
            vehicle.setGroupId(getGroupId(vehicle.getMhyreglocation()));      //
            vehicles.put(vehicle.getMhyplate(), vehicle);
            devices.put(cell.get(1), cell.get(2));
        }
        return vehicles;
    }

    private static Map<String, Vehicle> build2(List<List<String>> rowList) {
        vehicles = new LinkedHashMap<>(rowList.size());
        devices = new LinkedHashMap<>(rowList.size());
        for (List<String> cell : rowList) {
            int i = 0;
            Vehicle vehicle = new Vehicle();
            vehicle.setMhyplate(cell.get(i++));      // 车牌号码
            vehicle.setDeviceId(cell.get(i++));      // 设备号
            vehicle.setSim(cell.get(i++)); // SIM
            vehicle.setMhyprodDate(getMhyProdDate(cell.get(i++)));      // 生产年份
            vehicle.setMhyreglocation(cell.get(i++));      // 车籍地
            vehicle.setElno(cell.get(i++));      // 环保标识
            vehicle.setMhyplateColor(getMhyPlateColor(cell.get(i++)));      // 车牌颜色
            vehicle.setEmissionlevel(cell.get(i++));      // 排放阶段
            vehicle.setNgsmoke(cell.get(i++));      // 烟度值
            vehicle.setMhytype(getMhyType(cell.get(i++)));      // 机械类型
            vehicle.setMhybrand(cell.get(i++));      // 车辆品牌
            vehicle.setMhymodel(cell.get(i++));      // 机械型号
            vehicle.setMhyframeNo(cell.get(i++));      // 车架号
            vehicle.setMhyweight(cell.get(i++));      // 吨位/座位
            vehicle.setMhycolor(cell.get(i++));      // 车身颜色
            vehicle.setNgno(cell.get(i++));      // 发动机编号
            vehicle.setNgpower(cell.get(i++));      // 功率
            vehicle.setOwnerName(cell.get(i++));      // 联系人
            vehicle.setOwnerMobile(cell.get(i++));      // 联系人电话
            vehicle.setOwneridNo(cell.get(i++));      // 身份证号
            vehicle.setOwneraDdress(cell.get(i++));      // 地址
            vehicle.setMhyrespdept(cell.get(i++));      // 责任单位
            vehicle.setCreateTime(ImportFromExcel.getCreateTime(cell.get(i++)));      // 创建时间

            vehicle.setMhyprice(null);      // 新车购置价

            vehicle.setGroupId(getGroupId(vehicle.getMhyreglocation()));      //
            vehicles.put(vehicle.getMhyplate(), vehicle);
            devices.put(cell.get(1), cell.get(2));
        }
        return vehicles;
    }
}
