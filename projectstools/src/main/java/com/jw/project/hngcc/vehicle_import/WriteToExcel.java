package com.jw.project.hngcc.vehicle_import;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc
 *
 * @author Brook
 */
class WriteToExcel {

    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    private final static FastDateFormat FORMAT = FastDateFormat.getInstance("yyyyMMdd");

    public static void main(String[] args) throws Exception {
        Map<String, Vehicle> vehicleMap = new HashMap<>();

        Vehicle vehicle = new Vehicle();
        vehicle.setMhyplate("粤B193812");
        vehicle.setDeviceId("183812131231");
        vehicle.setMhyprodDate("20180101");
        vehicle.setGroupId("2");
        vehicle.setLongitude("113.19812312");
        vehicle.setLatitude("22.19812312");
        vehicleMap.put("12312", vehicle);
        writeVehicle(vehicleMap, null);
    }

    public static void writeVehicle(Map<String, Vehicle> vehicleMap, String date) throws Exception {
        File sourceFile = new File("E:\\document\\公司文档\\项目\\河南项目\\迁移数据\\web_vehicle - 模板.xlsx");
        File destFile = new File("E:\\document\\公司文档\\项目\\河南项目\\迁移数据\\web_vehicle" + (date != null ? "_" + date : "") + ".xlsx");

        copyFile(sourceFile, destFile);

        // 获取workbook
        Workbook workbook = getWorkBook(destFile);

        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy/MM/dd"));

        // 获取sheet
        Sheet sheet = workbook.getSheetAt(0);

        int rowIndex = 1;

        // 获取row
        for (Map.Entry<String, Vehicle> item : vehicleMap.entrySet()) {
            Vehicle vehicle = item.getValue();
            Row row = sheet.createRow(rowIndex++);
            int i = 0;
            try {
                setCellValueWithString(row.createCell(i++), vehicle.getMhyplate());
                setCellValueWithString(row.createCell(i++), vehicle.getDeviceId());
                setCellValueWithString(row.createCell(i++), vehicle.getCreateTime());
                setCellValueWithString(row.createCell(i++), vehicle.getElno());
                setCellValueWithString(row.createCell(i++), vehicle.getEmissionlevel());
                setCellValueWithString(row.createCell(i++), vehicle.getNgsmoke());
                setCellValueWithString(row.createCell(i++), vehicle.getMhybrand());
                setCellValueWithString(row.createCell(i++), vehicle.getMhytype());
                setCellValueWithString(row.createCell(i++), vehicle.getMhycolor());
                setCellValueWithString(row.createCell(i++), vehicle.getMhymodel());
                setCellValueWithString(row.createCell(i++), vehicle.getMhyframeNo());
                setCellValueWithString(row.createCell(i++), vehicle.getNgno());
                setCellValueWithString(row.createCell(i++), vehicle.getOwnerName());
                setCellValueWithString(row.createCell(i++), vehicle.getOwneridNo());
                setCellValueWithString(row.createCell(i++), vehicle.getOwnerMobile());
                setCellValueWithString(row.createCell(i++), vehicle.getOwneraDdress());
                setCellValueWithString(row.createCell(i++), vehicle.getNgpower());
                setCellValueWithString(row.createCell(i++), vehicle.getMhyweight());
                setCellValueWithString(row.createCell(i++), vehicle.getMhyprice());
                setCellValueWithDate(row.createCell(i++), dateCellStyle, vehicle.getMhyprodDate());
                setCellValueWithString(row.createCell(i++), vehicle.getMhyrespdept());
                setCellValueWithString(row.createCell(i++), vehicle.getMhyplateColor());
                setCellValueWithString(row.createCell(i++), vehicle.getMhyreglocation());
                i += 6;
                setCellValueWithString(row.createCell(i++), vehicle.getGroupId());
                i += 2;
                setCellValueWithString(row.createCell(i++), vehicle.getLongitude());
                setCellValueWithString(row.createCell(i++), vehicle.getLatitude());

                // 给刘处的
                if (vehicle.getPositionTime() != null) {
                    setCellValueWithString(row.createCell(i++), vehicle.getStatus());
                    setCellValueWithString(row.createCell(i++), vehicle.getAccStatus());
                    setCellValueWithString(row.createCell(i++), vehicle.getPositionTime());
                    setCellValueWithString(row.createCell(i++), vehicle.getPositionAddress());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(vehicle.getMhyplate());
            }
        }

        FileOutputStream out = new FileOutputStream(destFile);
        workbook.write(out);

        // 关闭流
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeVehicleForLiuchu(Map<String, Vehicle> vehicleMap) throws Exception {
        File file = new File("E:\\document\\公司文档\\项目\\河南项目\\迁移数据\\给刘处\\vehicle_for_liuchu.xlsx");

        // 获取workbook
        Workbook workbook = getWorkBook(file);

        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy/MM/dd"));

        // 获取sheet
        Sheet sheet = workbook.getSheetAt(0);

        int rowIndex = 1;

        // 获取row
        for (Map.Entry<String, Vehicle> item : vehicleMap.entrySet()) {
            Vehicle vehicle = item.getValue();
            Row row = sheet.createRow(rowIndex++);
            int i = 0;

            setCellValueWithString(row.createCell(i++), vehicle.getMhyplate());
            setCellValueWithString(row.createCell(i++), vehicle.getDeviceId());
            setCellValueWithString(row.createCell(i++), vehicle.getCreateTime());
            setCellValueWithString(row.createCell(i++), vehicle.getElno());
            setCellValueWithString(row.createCell(i++), vehicle.getEmissionlevel());
            setCellValueWithString(row.createCell(i++), vehicle.getNgsmoke());
            setCellValueWithString(row.createCell(i++), vehicle.getMhybrand());
            setCellValueWithString(row.createCell(i++), vehicle.getMhytype());
            setCellValueWithString(row.createCell(i++), vehicle.getMhycolor());
            setCellValueWithString(row.createCell(i++), vehicle.getMhymodel());
            setCellValueWithString(row.createCell(i++), vehicle.getMhyframeNo());
            setCellValueWithString(row.createCell(i++), vehicle.getNgno());
            setCellValueWithString(row.createCell(i++), vehicle.getOwnerName());
            setCellValueWithString(row.createCell(i++), vehicle.getOwneridNo());
            setCellValueWithString(row.createCell(i++), vehicle.getOwnerMobile());
            setCellValueWithString(row.createCell(i++), vehicle.getOwneraDdress());
            setCellValueWithString(row.createCell(i++), vehicle.getNgpower());
            setCellValueWithString(row.createCell(i++), vehicle.getMhyweight());
            setCellValueWithString(row.createCell(i++), vehicle.getMhyprice());
            setCellValueWithDate(row.createCell(i++), dateCellStyle, vehicle.getMhyprodDate());
            setCellValueWithString(row.createCell(i++), vehicle.getMhyrespdept());
            setCellValueWithString(row.createCell(i++), vehicle.getMhyplateColor());
            setCellValueWithString(row.createCell(i++), vehicle.getMhyreglocation());
            i += 6;
            setCellValueWithString(row.createCell(i++), vehicle.getGroupId());
            i += 2;
            setCellValueWithString(row.createCell(i++), vehicle.getLongitude());
            setCellValueWithString(row.createCell(i++), vehicle.getLatitude());

            // 给刘处的
            setCellValueWithString(row.createCell(i++), vehicle.getStatus());
            setCellValueWithString(row.createCell(i++), vehicle.getAccStatus());
            setCellValueWithString(row.createCell(i++), vehicle.getPositionTime());
            setCellValueWithString(row.createCell(i++), vehicle.getPositionAddress());
        }

        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);

        // 关闭流
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDevice(Map<String, Vehicle> vehicleMap, String date) throws Exception {
        File sourceFile = new File("E:\\document\\公司文档\\项目\\河南项目\\迁移数据\\web_device - 副本.xlsx");
        File destFile = new File("E:\\document\\公司文档\\项目\\河南项目\\迁移数据\\web_device" + (date != null ? "_" + date : "") + ".xlsx");

        copyFile(sourceFile, destFile);

        // 获取workbook
        Workbook workbook = getWorkBook(destFile);

        // 获取sheet
        Sheet sheet = workbook.getSheetAt(0);
        // deviceHeard(sheet);

        // 获取row
        int rowIndex = 1;
        for (Map.Entry<String, Vehicle> item : vehicleMap.entrySet()) {
            Row row = sheet.createRow(rowIndex++);
            int i = 0;

            setCellValueWithString(row.createCell(i++), item.getValue().getDeviceId());
            setCellValueWithString(row.createCell(i++), "D1500");
            setCellValueWithString(row.createCell(i++), item.getValue().getSim());
        }

        FileOutputStream out = new FileOutputStream(destFile);
        workbook.write(out);

        // 关闭流
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            System.err.println("源文件不存在！");
            return;
        }

        if (destFile.exists()) {
            System.err.println("目标文件已经存在");
            destFile.delete();
        }

        FileInputStream in = new FileInputStream(sourceFile);
        FileOutputStream out = new FileOutputStream(destFile);
        byte[] buff = new byte[1024];
        int len;
        while ((len = in.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
        out.flush();
        in.close();
        out.close();
    }

    private static void deviceHeard(Sheet sheet) {
        Row row = sheet.createRow(1);
        int i = 0;
        setCellValueWithString(row.createCell(i++), "id");
        setCellValueWithString(row.createCell(i++), "deviceType");
        setCellValueWithString(row.createCell(i++), "sim");
        setCellValueWithString(row.createCell(i++), "iccid");
        setCellValueWithString(row.createCell(i++), "imsi");
    }

    private static void setCellValueWithString(Cell cell, String value) {
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);
    }

    private static void setCellValueWithDate(Cell cell, CellStyle dateCellStyle, String value) throws Exception {
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        if (value != null && !"".equals(value)) {
            cell.setCellStyle(dateCellStyle);
            cell.setCellValue(FORMAT.parse(value));
        }
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
}
