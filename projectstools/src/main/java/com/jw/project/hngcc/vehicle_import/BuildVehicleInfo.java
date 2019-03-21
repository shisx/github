package com.jw.project.hngcc.vehicle_import;

/**
 * 导给exe平台的
 *
 * @author Brook
 */
class BuildVehicleInfo {

    /**
     * 机械类型
     *
     * @param mhyType
     * @return
     */
    public static String getMhyType(String mhyType) {
        if (mhyType == null || mhyType.equals("")) {
            return mhyType;
        }

        switch (mhyType) {
            case "挖掘机":
                return "1";
            case "压路机":
                return "2";
            case "装载机":
            case "装载车":
                return "3";
            case "叉车":
                return "4";
            case "摊铺机":
                return "5";
            case "推土机":
                return "6";
            case "平地机":
            case "平路机":
                return "7";
            case "开槽机":
                return "8";
            case "桩工机械":
                return "9";
            case "起重机":
                return "10";
            case "装卸搬运机械":
                return "111";
            case "牵引车":
                return "12";
            case "拆包机":
                return "13";
            case "拆炉机":
                return "14";
            case "冷再生机":
                return "15";
        }

        System.err.println("未知机械类型：" + mhyType);
        return "1"; // 使用挖掘机
    }

    /**
     * 生产日期
     *
     * @param prodDate
     * @return
     */
    public static String getMhyProdDate(String prodDate) {
        if (prodDate == null || prodDate.equals("")) {
            return prodDate;
        }

        if (prodDate.length() == 4) {
            return prodDate + "0101";
        } else if (prodDate.startsWith("20")) {
            if (prodDate.length() == 7) { // 2018.02
                return prodDate.replace(".", "") + "01";
            } else if (prodDate.length() == 10) { // 2018.02.12
                return prodDate.replace(".", "");
            }
        } else if (prodDate.length() == 8) { // 18.02.12
            return "20" + (prodDate.replace(".", ""));
        } else if (prodDate.length() == 5) { // 18.02
            return "20" + (prodDate.replace(".", "")) + "01";
        }
        System.err.println("未知生产日期：" + prodDate);
        return prodDate;
    }

    /**
     * 车牌颜色
     *
     * @param plateColor
     * @return
     */
    public static String getMhyPlateColor(String plateColor) {
        if (plateColor == null || plateColor.equals("")) {
            return plateColor;
        }

        if (plateColor.contains("绿")) {
            return "#00FF00,绿";
        } else if (plateColor.contains("橙")) {
            return "#FFAA00,橙";
        } else if (plateColor.contains("蓝") || plateColor.contains("黄")) {
            return "#0066FF,蓝";
        }
        System.err.println("未知车牌颜色：" + plateColor);
        return plateColor;
    }

    /**
     * 组ID
     *
     * @param mhyreglocation
     * @return
     */
    public static String getGroupId(String mhyreglocation) {
        if (mhyreglocation == null || mhyreglocation.equals("")) {
            return mhyreglocation;
        }
        // 安阳市
        if (mhyreglocation.contains("文峰区")) {
            return "7";
        } else if (mhyreglocation.contains("北关区")) {
            return "8";
        } else if (mhyreglocation.contains("殷都区")) {
            return "9";
        } else if (mhyreglocation.contains("龙安区")) {
            return "10";
        } else if (mhyreglocation.contains("安阳县")) {
            return "11";
        } else if (mhyreglocation.contains("汤阴县")) {
            return "12";
        } else if (mhyreglocation.contains("滑县")) {
            return "13";
        } else if (mhyreglocation.contains("内黄县")) {
            return "14";
        } else if (mhyreglocation.contains("林州市")) {
            return "15";
        }
        // 洛阳市
        else if (mhyreglocation.contains("老城区")) {
            return "47";
        } else if (mhyreglocation.contains("西工区")) {
            return "48";
        } else if (mhyreglocation.contains("瀍河回族区")) {
            return "49";
        } else if (mhyreglocation.contains("涧西区")) {
            return "50";
        } else if (mhyreglocation.contains("吉利区")) {
            return "51";
        } else if (mhyreglocation.contains("洛龙区")) {
            return "52";
        } else if (mhyreglocation.contains("孟津县")) {
            return "53";
        } else if (mhyreglocation.contains("新安县")) {
            return "54";
        } else if (mhyreglocation.contains("栾川县")) {
            return "55";
        } else if (mhyreglocation.contains("嵩县")) {
            return "56";
        } else if (mhyreglocation.contains("汝阳县")) {
            return "57";
        } else if (mhyreglocation.contains("宜阳县")) {
            return "58";
        } else if (mhyreglocation.contains("洛宁县")) {
            return "59";
        } else if (mhyreglocation.contains("伊川县")) {
            return "60";
        } else if (mhyreglocation.contains("偃师市")) {
            return "61";
        }

        System.err.println("未知注册地：" + mhyreglocation);
        return mhyreglocation;
    }

}
