package com.ca.caunit;

import java.util.LinkedList;

import static java.util.regex.Pattern.matches;

/**
 * 单元测试第一阶段
 * 功能：
 * 1.将String类型的数据转换成LinkedList<String>，内容不做修改
 * 2.提供多括号识别
 * 3.提供最外圈全括号识别
 * 4.提供输入异常处理
 * 输入类型：String
 * 输出类型：LinkedList<String>
 * 输入格式：（5.2+（4.6-5））+5.2
 * 输出格式：（5.2+（4.6-5））+5.2
 * 已测试通过类型:
 * 1.无括号              通过     6.4+9.6-4.2
 * 2.前部一个括号        通过    (6.4+9.6)-4.2
 * 3.最外层一个大括号    通过    (6.4+9.6-4.2)
 * 4.前置双括号          通过    ((6.4+9.6)-4.2)
 * 5.后置双括号          通过    (6.4+(9.6-4.2))
 * 6.前置三括号          通过    (((6.4+4.9)+9.6)-4.2))
 * 7.后置三括号          通过    (6.4+(4.9+(9.6-4.2)))
 * 8.前二后二            通过    ((6.4+4.9)+(9.6-4.2))
 * 9.前二后三            通过    ((6.4+4.9)+(9.5+(9.6-4.2)))
 * 10.后置一个括号       通过     6.4+(9.6-4.2)
 * 备注：可能出现的错误有，非法字符输入时未捕获，在合法格式下传入下一级，现在的测试暂未发现这种情况
 * @author hqhq
 */
public class Test1 {

    /**
     * @param string 控制器接收的字符串类型
     * @return LinkedList<String> 下级
     * @exception IndexOutOfBoundsException
     */
    protected LinkedList<String> conversion(String string) {                   //字符串转链表
        LinkedList<String> asm = new LinkedList<>();
        LinkedList<String> as2 = new LinkedList<>();
        String[] arr1 = string.split("\\+|-|\\*|/|\\(|\\)");  //读取String数字部分
        String[] arr2 = string.split("\\d|\\.");                //读取String运算符部分
        for (String i : arr1) {       //清空空值
            if (!"".equals(i)) {
                asm.add(i);
            }
        }
        for (String i : arr2) {      //清空空值
            if (!"".equals(i)) {
                if (matches(".*[（）].*+", i) ) {
                    System.err.println("输入错误：括号格式错误，请输入英文格式");
                    System.exit(1);
                }
                as2.add(i);
            }
        }
        try {
            if (!matches("^\\(.+|\\(", as2.get(0))) {           //数字和运算符合并，开头不是“（”时
                for (int i = 0; i < as2.size(); i++) {
                    asm.add(2 * i + 1, as2.get(i));
                }
                return asm;
            }
            for (int i = 0; i < as2.size(); i++) {               //数字和运算符合并，开头为数字时
                asm.add(2 * i, as2.get(i));
            }
        }catch (IndexOutOfBoundsException ex){
                System.err.println("输入异常：请输入正确符号，不要输入英文类型的.()运算符和数字以外的字符");
                System.exit(1);
        }
        return asm;
    }
    public static void main(String[] args) {
        String a="((6.4+ 4.9)+(9.5+(9.6-4.2)))";
        System.out.println(new Test1().conversion(a));
    }
}
