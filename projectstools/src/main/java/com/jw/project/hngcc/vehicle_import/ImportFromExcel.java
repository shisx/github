package com.jw.project.hngcc.vehicle_import;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * Desc
 *
 * @author Brook
 */
class ImportFromExcel {
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    private static final List<FastDateFormat> FORMATS = Arrays.asList(
            FastDateFormat.getInstance("yyyy年MM月dd日"),
            FastDateFormat.getInstance("yyyy年MM月d日"),
            FastDateFormat.getInstance("yyyy.MM.dd"),
            FastDateFormat.getInstance("yyyy年M月d日"),
            FastDateFormat.getInstance("yyyy.M.d")
    );
    private static final FastDateFormat CREATE_TIME_FORMAT = FastDateFormat.getInstance("yyyyMMdd");

    public static void main(String[] args) throws Exception {
        File file = new File("E:\\document\\公司文档\\项目\\河南项目\\车辆录入记录/旧平台数据.xlsx");

        List<List<String>> row_cell_list = loadFromExcel(1, file);
        sql_addVehicleSQL(file, row_cell_list);
        sql_updateDevice(file, row_cell_list);
        //sql_updatePlateColorForExe(file, row_cell_list);
    }

    public static List<List<String>> loadFromExcel(int startRow, File file) {
        // 获取workbook
        Workbook workbook = getWorkBook(file);

        // 获取sheet
        Sheet sheet = workbook.getSheetAt(0);
        // Sheet sheet = workbook.getSheet("sheet name");

        List<List<String>> row_cell_list = new ArrayList<>();
        Set<String> chePaiHaoSet = new HashSet<>();
        Set<String> deviceIdSet = new HashSet<>();

        // 获取row
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("一共 " + (lastRowNum) + " 条记录。");

        // 连续的空行数量
        int emptyRow = 0;

        for (int i = startRow; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);

            List<String> thisRow = new ArrayList<>();
            for (int j = 1; j < 25; j++) {
                String value = getCellValue(row.getCell(j)).trim();
                if (j == 1) {
                    if ("".equals(value)) {
                        ++emptyRow;
                        System.out.println("空的车牌号，行：" + i);
                        break;
                    }
                    if (chePaiHaoSet.contains(value)) {
                        System.out.println("车牌号重复：" + value);
                        break;
                    }
                    chePaiHaoSet.add(value);
                } else if (j == 2) {
                    if ("".equals(value)) {
                        System.out.println("空的设备号，行：" + i);
                        break;
                    }
                    if (deviceIdSet.contains(value)) {
                        System.out.println("设备号重复：" + value);
                        break;
                    }
                    deviceIdSet.add(value);
                }

                thisRow.add(value);
                emptyRow = 0;
            }

            if (emptyRow >= 10) {
                System.out.println("连续空行太多，结束遍历。");
                break;
            }
            if (thisRow.size() > 10) {
                row_cell_list.add(thisRow);
            }
        }

        // 关闭流
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return row_cell_list;
    }

    private static void sql_addVehicleSQL(File file, List<List<String>> row_cell_list) throws Exception {
        String addVehicleSQL = "insert into obd_device_extend (ODE_DEVICE_ID, ODE_CREATE_TIME, ODE_PARAM1, ODE_PARAM2, ODE_PARAM3, ODE_PARAM4, ODE_PARAM5, ODE_PARAM6, ODE_PARAM7, ODE_PARAM8, ODE_PARAM9, ODE_PARAM10, ODE_PARAM11, ODE_PARAM12, ODE_PARAM13, ODE_PARAM14, ODE_PARAM15, ODE_PARAM16, ODE_PARAM17, ODE_PARAM18, ODE_PARAM19, ODE_PARAM20)\n" +
                "values ('ODE_DEVICE_ID_value', 'ODE_CREATE_TIME_value', 'ODE_PARAM1_value', 'ODE_PARAM2_value', 'ODE_PARAM3_value', 'ODE_PARAM4_value', 'ODE_PARAM5_value', 'ODE_PARAM6_value', 'ODE_PARAM7_value', 'ODE_PARAM8_value', 'ODE_PARAM9_value', 'ODE_PARAM10_value', 'ODE_PARAM11_value', 'ODE_PARAM12_value', 'ODE_PARAM13_value', 'ODE_PARAM14_value', 'ODE_PARAM15_value', 'ODE_PARAM16_value', 'ODE_PARAM17_value', 'ODE_PARAM18_value', 'ODE_PARAM19_value', 'ODE_PARAM20_value');";

        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(file.getParent(), file.getName() + "_SQL_addVehicleSQL.txt")));
        for (List<String> row : row_cell_list) {
            if (row.isEmpty()) {
                continue;
            }
            try {
                int i = 1;
                String sql = addVehicleSQL
                        .replace("ODE_DEVICE_ID_value", row.get(i++))
                        .replace("null--", row.get(i++))
                        .replace("ODE_CREATE_TIME_value", getCreateTime(row.get(i++)))
                        .replace("ODE_PARAM1_value", row.get(i++))// 环保标识
                        .replace("ODE_PARAM19_value", row.get(i++))// 车牌颜色
                        .replace("ODE_PARAM2_value", row.get(i++))// 排放阶段
                        .replace("ODE_PARAM3_value", row.get(i++))// 烟度值
                        .replace("ODE_PARAM4_value", row.get(i++))// 机械品牌
                        .replace("ODE_PARAM5_value", row.get(i++))// 机械类型
                        .replace("ODE_PARAM6_value", row.get(i++))// 机械颜色
                        .replace("ODE_PARAM7_value", row.get(i++))// 机械型号
                        .replace("ODE_PARAM8_value", row.get(i++))// 整机编号
                        .replace("ODE_PARAM9_value", row.get(i++))// 发动机编号
                        .replace("ODE_PARAM10_value", row.get(i++))// 所有人
                        .replace("ODE_PARAM11_value", row.get(i++))// 证件号
                        .replace("ODE_PARAM12_value", row.get(i++))// 联系电话
                        .replace("ODE_PARAM13_value", row.get(i++))// 联系地址
                        .replace("ODE_PARAM14_value", row.get(i++))// 功率
                        .replace("ODE_PARAM15_value", row.get(i++))// 吨位
                        .replace("ODE_PARAM16_value", row.get(i++))// 新车购置价
                        .replace("ODE_PARAM17_value", row.get(i++))// 生产年份
                        .replace("ODE_PARAM18_value", row.get(i++))// 责任单位
                        .replace("ODE_PARAM20_value", row.get(i++)) // 车籍地
                        ;

                writer.write(sql);
                writer.newLine();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("处理错误，行" + row);
            }
        }

        writer.close();
    }

    private static void sql_updateDevice(File file, List<List<String>> row_cell_list) throws Exception {
        String updateDevice = "update obd_device set od_name = 'od_name_value' where pk_od_id = 'pk_od_id_value';";
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(file.getParent(), file.getName() + "_SQL_updateDevice.txt")));
        for (List<String> row : row_cell_list) {
            if (row.isEmpty()) {
                continue;
            }
            try {
                String sql = updateDevice.replace("od_name_value", row.get(0)).replace("pk_od_id_value", row.get(1));
                writer.write(sql);
                writer.newLine();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("处理错误，行" + row);
            }
        }

        writer.close();
    }

    private static void sql_updatePlateColor(File file, List<List<String>> row_cell_list) throws Exception {
        String updatePlateColor = "update obd_device set od_name = 'od_name_value' where pk_od_id = 'pk_od_id_value';";
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(file.getParent(), file.getName() + "_SQL_updatePlateColor.txt")));
        for (List<String> row : row_cell_list) {
            if (row.isEmpty()) {
                continue;
            }
            try {
                String sql = updatePlateColor.replace("ODE_PARAM19_value", row.get(5)).replace("ODE_DEVICE_ID_value", row.get(1));
                writer.write(sql);
                writer.newLine();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("处理错误，行" + row);
            }
        }

        writer.close();
    }

    private static void sql_updatePlateColorForExe(File file, List<List<String>> row_cell_list) throws Exception {
        String updatePlateColor = "update mechanics_car set elno='elno_value', mhyplate_color='mhyplate_color_value', emissionlevel='emissionlevel_value' where mhyplate='mhyplate_value';";
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(file.getParent(), file.getName() + "_SQL_updatePlateColor_for_exe.txt")));
        for (List<String> row : row_cell_list) {
            if (row.isEmpty()) {
                continue;
            }
            try {
                if (StringUtils.isBlank(row.get(5))) {
                    continue;
                }
                String sql = updatePlateColor
                        .replace("mhyplate_value", row.get(0))
                        .replace("elno_value", row.get(4))
                        .replace("mhyplate_color_value", BuildVehicleInfo.getMhyPlateColor(row.get(5)))
                        .replace("emissionlevel_value", row.get(6));
                writer.write(sql);
                writer.newLine();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("处理错误，行" + row);
            }
        }

        writer.close();
    }

    /**
     * 获取Workbook实例
     *
     * @param file
     * @return
     */
    public static Workbook getWorkBook(File file) {
        String fileName = file.getName();
        Workbook workbook = null;
        try {
            InputStream is = new FileInputStream(file);
            if (fileName.endsWith(xls)) {
                // 2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                // 2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    public static String getCreateTime(String value) {
        Date date = null;
        if (StringUtils.isNotBlank(value)) {
            if (StringUtils.isNumeric(value)) {
                // 由于excel中的日期数字是从1900年算的，java中是从1970算的，所以要减去中间的天数
                date = new Date((Integer.parseInt(value) - 25569) * (24L * 60L * 60L * 1000L));
            } else {
                for (FastDateFormat format : FORMATS) {
                    try {
                        date = format.parse(value);
                        break;
                    } catch (ParseException e) {
                    }
                }
            }
        }
        if (date == null) {
            System.err.println("创建时间日期格式错误：" + value);
            return CREATE_TIME_FORMAT.format(System.currentTimeMillis()) + "101231";
        }
        return CREATE_TIME_FORMAT.format(date) + "101231";
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }

        // 把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        // 判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                System.err.println("未知类型");
                cellValue = "";
                break;
        }
        return cellValue;
    }
}
