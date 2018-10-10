package com.ccj.client.android.analytics.enums;

/**
 * 统计类别
 * Created by chenchangjun on 18/2/26.
 */
public enum EventType {


    EVENT_TYPE_DEFAULT("default"), //暂时先用默认值
    EVENT_TYPE_PV("pv"),//屏幕
    EVENT_TYPE_EVENT("event"),//点击
    EVENT_TYPE_EXPOSE("expose");//曝光,接口不同


    private String typeName;

    EventType(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 根据类型的名称，返回类型的枚举实例。
     *
     * @param typeName 类型名称
     */
    public static EventType fromTypeName(int typeName) {
        for (EventType type : EventType.values()) {
            if (type.getTypeName().equals(typeName)) {
                return type;
            }
        }
        return null;
    }

    public String getTypeName() {
        return this.typeName;
    }


}
