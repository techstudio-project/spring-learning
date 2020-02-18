package com.techstudio.springlearning.annotation.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author lj
 * @date 2020/2/18
 */
public class StreamUtils {

    public static String streamToString(InputStream is) throws IOException {
        try {
            byte[] buf = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, StandardCharsets.UTF_8));
            }
            return sb.toString();
        }
        finally {
            is.close();
        }
    }

}
