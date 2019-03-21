package com.jw.project.hngcc.vehicle_import;

/**
 * 设置环保标识
 *
 * @author Brook
 */
public class SetElnoInfo {

    /**
     * 分析车牌颜色
     *
     * @param emissionlevel
     * @param ngpower
     * @param ngsmoke
     * @return
     */
    private static String parsePlateColor(String emissionlevel, float ngpower, float ngsmoke) {
        if ("国三".equals(emissionlevel)) {
            return guo_3(ngpower, ngsmoke);
        } else {
            return guo_1_2(ngpower, ngsmoke);
        }
    }

    /**
     * 国一、国二 处理
     *
     * @param ngpower
     * @param ngsmoke
     * @return
     */
    private static String guo_1_2(float ngpower, float ngsmoke) {
        if (ngpower < 19) {
            return ngsmoke < 0.8F ? "绿色" : ngsmoke < 3.0F ? "蓝色" : "橙色";
        } else if (ngpower < 37) {
            return ngsmoke < 0.8F ? "绿色" : ngsmoke < 2.0F ? "蓝色" : "橙色";
        } else if (ngpower <= 560) {
            return ngsmoke < 0.5F ? "绿色" : ngsmoke < 1.61F ? "蓝色" : "橙色";
        }
        return null;
    }

    /**
     * 国三 处理
     *
     * @param ngpower
     * @param ngsmoke
     * @return
     */
    private static String guo_3(float ngpower, float ngsmoke) {
        if (ngpower < 19) {
            return ngsmoke < 0.8F ? "绿色" : ngsmoke < 2.0F ? "蓝色" : "橙色";
        } else if (ngpower < 37) {
            return ngsmoke < 0.8F ? "绿色" : ngsmoke < 1.0F ? "蓝色" : "橙色";
        } else {
            return ngsmoke < 0.5F ? "绿色" : ngsmoke < 0.8F ? "蓝色" : "橙色";
        }
    }

}
