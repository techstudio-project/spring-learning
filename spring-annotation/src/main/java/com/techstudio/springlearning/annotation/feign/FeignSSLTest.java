package com.techstudio.springlearning.annotation.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lj
 * @date 2020/2/18
 */
@FeignClient(value = "feignSSLTest",url = "")
public interface FeignSSLTest {

    @GetMapping("/attendanceCheck/checkInfo")
    JSONObject getScore(@RequestParam("wfId") String wfId, @RequestParam("beginTime") String beginTime,
                        @RequestParam("stopTime") String stopTime);

}
