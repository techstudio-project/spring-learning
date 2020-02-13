package com.techstudio.springlearning.annotation.finaltest;

/**
 * @author lj
 * @date 2020/1/15
 */
public class MainTest {


    private static final Integer age;

    static {
        age = 10;
    }

    public void main(final ClassTest1 num) {

        final String param;
        param = "";
        // 不能再进行赋值了，用于不希望后续改变值的变量
        //param = "123";

        DoSomethingTest doSomethingTest = new DoSomethingTest() {
            @Override
            public void doSomething() {
                num.setName("111");
                System.out.println(num);
            }

            @Override
            public void doSomething2() {

            }
        };

        doSomethingTest.doSomething();
    }

    public class ClassTest1 {
        private String name;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class ClassTest2 extends ClassTest1 {

    }

}
