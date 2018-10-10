package com.ccj.client.android.analytics.net.core;


import com.ccj.client.android.analytics.net.gson.JsonDeserializationContext;
import com.ccj.client.android.analytics.net.gson.JsonDeserializer;
import com.ccj.client.android.analytics.net.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * @author Aidi on 2018/1/8.
 */
public class IntegerAdapter implements JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            return json.getAsInt();
        } catch (Exception e) {
            return 0;
        }
    }

}
