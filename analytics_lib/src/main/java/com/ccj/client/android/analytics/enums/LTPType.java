package com.ccj.client.android.analytics.enums;

/**
 * LTPType 屏幕加载方式
 * Created by chenchangjun on 18/2/24.
 */

public enum LTPType {
    SCREEN_LTP_REFRESH(1),//下拉刷新
    SCREEN_LTP_NEXT_PAGE(2), //翻页
    SCREEN_LTP_NEXT_TAB(3),//标签切换
    SCREEN_LTP_POP_WINDOW(4), //局部弹出
    SCREEN_LTP_FILTER_REFRESH(5);//筛选刷新

    private int typeName;

    LTPType(int typeName) {
        this.typeName = typeName;
    }

    /**
     * 根据类型的名称，返回类型的枚举实例。
     *
     * @param typeName 类型名称
     */
    public static LTPType fromTypeName(int typeName) {
        for (LTPType type : LTPType.values()) {
            if (type.getTypeName() == typeName) {
                return type;
            }
        }
        return null;
    }

    public int getTypeName() {
        return this.typeName;
    }
}