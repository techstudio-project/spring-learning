package com.techstudio.example.component.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lj
 * @since 2020/5/6
 */
public enum EnumTest {

    PICTURE(102, "图文"),
    AUDIO(103, "音频"),
    VIDEO(104, "视频"),;

    private final int index;
    private final String name;

    EnumTest(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, EnumTest> MAPPINGS;

    static {
        Map<Integer, EnumTest> temp = new HashMap<>();
        for (EnumTest courseType : values()) {
            temp.put(courseType.index, courseType);
        }
        MAPPINGS = Collections.unmodifiableMap(temp);
    }

    @EnumConvertMethod
    public static EnumTest resolve(int index) {
        return MAPPINGS.get(index);
    }
}
