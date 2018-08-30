package com.ca.caunit;


import java.math.BigDecimal;
import java.util.LinkedList;

import static java.util.regex.Pattern.matches;

/**
 * 单元测试第二阶段
 * 功能：计算处理完括号后的数组
 * 输入类型：LinkedList<String>
 * 输出类型：String
 * 输出格式：6.5+1.2-4.1
 * 输出格式：1.6
 * 已测试类型：
 * 1.两个数直接加法      通过      6.4+4.9=11.3
 * 2.两个数直接减法      通过      6.4-4.9=1.5
 * 3.两个数直接乘法      通过      6.4*4.9=31.36
 * 4.两个数直接除法      通过      6.4/4.9=1.30612245
 * 5.三个数连续乘法      通过      6.4*4.9*9.2=288.512
 * 6.四个数混合算法      通过      9.9/6.4*4.9+9.2-5.2=11.5796875
 * 7.0位精度除法         通过      5/2.5=2
 * 8.1位精度除法         通过      5/2=2.5
 * 9.2为精度除法         通过      5.5/2=2.75
 * 备注：
 * 1.绑定测试read方法（判断）和judgmentC方法（计算）
 * 2.除法的精度设为小数点后8位
 * 3.自适应精度
 * @author hqhq
 */

public class Test2 {

    /**
     * @param as1 最后处理结果
     * @return String类型的计算结果
     */

    protected String read(LinkedList<String> as1){   //读取计算无括号内容
        int size =0;
        String result = "";
        String start = "";
        String end = "";
        String nums = "";
        String nume = "";
        for (String i:as1) {   //读取数组
            if ( !matches("\\D", i)){   //String为数字时
                if (nums == "") {
                    nums=i;
                }
                else nume=i;
            }
            else if (start == "") {             //String为运算符时
                start=i;
            }
            else end=i;
            size++;
            if (end != ""||size==as1.size()) { //进入运算
                result=judgmentC(start,nums,nume);
                start = end;
                end ="";
                nume="";
                nums=String.valueOf(result);
            }
        }
        return result;
    }

    /**
     * 四则运算
     * 除法定义为8位精度
     * 功能：
     * 1.除法消除无效精度
     * 2.除法为整数时去掉小数点符号
     */

    protected String judgmentC(String start, String nums, String nume) {
        BigDecimal b1 = new BigDecimal(nums);
        BigDecimal b2 = new BigDecimal(nume);
        String re="";
        if ( "+".equals(start)) {
            re = b1.add(b2).toString();
        }
        if ("-".equals(start)) {
            re =b1.subtract(b2).toString();
        }
        if ("*".equals(start)) {
            re =b1.multiply(b2).toString();
        }
        if ("/".equals(start)) {
            re =b1.divide(b2,8, BigDecimal.ROUND_HALF_EVEN).toString();
            String div = "";
            int len = re.length();
            int lencount = 0;
            for (int i = len-1; i > 0; i--) {
                if (re.charAt(i)=='0') {
                   lencount++;
                   continue;
                }
                break;
            }
            for (int i = 0; i < len-lencount; i++) {
                if (i == len - lencount-1 && re.charAt(len-lencount-1) == '.') {
                    break;
                }
                div +=re.charAt(i);
            }
            re = div;
        }
        return re;
    }

    public static void main(String[] args) {
        String a="66/5";
        Test1 t1 = new Test1();
        System.out.println(new Test2().read(t1.conversion(a)));
    }
}
