package com.techstudio.springlearning.annotation.aop;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring 2.0 以后，引入了 @AspectJ 和 Schema-based 的两种配置方式
 * : @AspectJ 和 AspectJ 没多大关系，并不是说基于 AspectJ 实现的，而仅仅是使用了 AspectJ 中的概念，包括使用的注解也是直接来自于 AspectJ 的包。
 * : @Aspect 在spring中只能作用与ioc容器中的bean
 *
 * @author lj
 * @date 2020/2/14
 */
//@Configuration
// 开启 @AspectJ 的注解配置
@EnableAspectJAutoProxy
public class SpringAopConfig_2_0 {

}
