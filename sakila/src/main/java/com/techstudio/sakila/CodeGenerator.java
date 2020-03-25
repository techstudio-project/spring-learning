package com.techstudio.sakila;

import com.goldwind.goplus.codegenerator.AbstractConfigTemplate;
import com.goldwind.goplus.codegenerator.ConfigTemplate;

/**
 * @author lj
 * @date 2020/3/16
 */
public class CodeGenerator {

    public static void main(String[] args) {
        ConfigTemplate template = new AbstractConfigTemplate() {
            @Override
            public String getProjectPath() {
                return "F:/workspace/src/spring-learning/sakila";
            }
            @Override
            public String getParentPackage() {
                return "com.techstudio.sakila";
            }
        };
        template.doGenerate();
    }

}
