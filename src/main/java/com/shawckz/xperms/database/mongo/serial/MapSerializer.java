package com.shawckz.xperms.database.mongo.serial;

import com.mongodb.util.JSON;
import com.shawckz.ipractice.configuration.AbstractSerializer;

import java.util.HashMap;

public class MapSerializer extends AbstractSerializer<HashMap> {

    @Override
    public String toString(HashMap data) {
        return JSON.serialize(data);
    }

    @Override
    public HashMap fromString(Object data) {
        HashMap map = (HashMap) JSON.parse(((String) data));
        return map;
    }
}
