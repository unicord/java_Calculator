package com.ca.caunit;

import java.util.LinkedList;

import static java.util.regex.Pattern.matches;

/**
 * 单元测试第四阶段
 * 功能：对乘法除法优先计算（添加括号）
 * 输入位置：Test1 conversion方法后
 * 输入类型：LinkedList<String>
 * 输出类型：LinkedList<String>
 * 已测试类型：               输入格式                                          返回格式
 * 1.                        [5, *, 6]                                        [(, 5, *, 6, )]
 * 2.                     [5, *(, 6, +, 6, )]                              [(, 5, *(, 6, +, 6, ), )]
 * 3.                   [5, *((, 6, +, 6, )+, 6, )]                     [(, 5, *((, 6, +, 6, )+, 6, ))]
 * 4.               [(, 5, *((, 6, +, 6, )+, 6, ))+, 6, /, 6]       [((, 5, *((, 6, +, 6, )+, 6, )))+(, 6, /, 6, )]
 */
public class Test4 {
    int pdCountS = 0;
    int pdCountE = 0;
    public LinkedList<String> pdDeal(LinkedList<String> asp){
        int counti =0;      //记录每次循环的对应位置
        int count1 = 0;     //记录（数
        int count2 = 0;     //记录）数
        for (String i:asp) {
            if (matches(".*\\*.*|.*/.*", i)){
                if (matches(".*\\).*", i)){
                    for (int j = 0; j <i.length() ; j++) {
                        if (i.charAt(j)==')') {
                            count1++;
                        }
                    }
                }
                if (matches(".*\\(.*", i)){
                    for (int j = 0; j <i.length() ; j++) {
                        if (i.charAt(j)=='(') {
                            count2++;
                        }
                    }
                }
                asp = pdAdd(asp,counti,count1,count2);
                System.out.println(asp);
                count1=0;
                count2=0;
            }
            counti++;
        }
        String end ="";
        String start ="";
        for (int i = 0; i < pdCountS; i++) {
            start +="(";
        }
        for (int i = 0; i < pdCountE; i++) {
            end +=")";
        }
        if (start != "") {
            asp.add(0,start);
        }
        if (end != "") {
            asp.add(end);
        }
        return asp;
    }

    public LinkedList<String> pdAdd(LinkedList<String> asp,int counti,int count1,int count2){
        int countL = 0;
        int countR = 0;
        int aLi = 0;
        int aRi = 0;
        for (int i = counti-1; i >= 0; i--) {

            if (matches(".*\\(.*", asp.get(i))){
                for (int j = 0; j <asp.get(i).length() ; j++) {
                    if (asp.get(i).charAt(j)=='(') {
                        countL++;
                    }
                    if (count1 == countL) {
                        aLi=1;
                        break;
                    }
                }
            }
            if (aLi != 1 && matches(".*\\).*", asp.get(i))){
                for (int j = 0; j <asp.get(i).length() ; j++) {
                    if (asp.get(i).charAt(j)==')') {
                        count1++;
                    }
                }
            }
            if (aLi == 1||count1==0) {
                if (i == 0 && !matches(".*\\(.*", asp.get(0))) {
                    pdCountS++;
                    break;
                }
                if (count1 == 0) {
                    asp.set(i-1,asp.get(i-1)+"(");
                }
                else {
                    asp.set(i,asp.get(i)+"(");
                }
                break;
            }
            if (i == 0) {
                pdCountS++;
            }
        }

        for (int i = counti+1; i < asp.size(); i++) {
            if (matches(".*\\).*", asp.get(i))){
                for (int j = 0; j <asp.get(i).length() ; j++) {
                    if (asp.get(i).charAt(j)==')') {
                        countR++;
                    }
                    if (count2 == countR) {
                        aRi=1;
                        break;
                    }
                }
            }
            if (aRi !=1 && matches(".*\\(.*", asp.get(i))){
                for (int j = 0; j <asp.get(i).length() ; j++) {
                    if (asp.get(i).charAt(j)=='(') {
                        count2++;
                    }
                }
            }
            if (aRi==1||count2==0) {
                if (i+1 == asp.size() && !matches(".*\\).*", asp.get(i))) {
                    pdCountE++;
                    break;
                }
                if (count2 == 0) {
                    asp.set(i+1,")"+asp.get(i+1));
                    break;
                }
                else {
                    asp.set(i,")"+asp.get(i));
                }
                return asp;
            }
        }
        return asp;
    }

    public static void main(String[] args) {
        LinkedList<String> as2p = new LinkedList<>();
        LinkedList<String> as3p = new LinkedList<>();
        Test1 t1 = new Test1();
        Test3 t3 = new Test3();
        Test4 t4 = new Test4();
        String a="(9/8*5)";
        System.out.println(t1.conversion(a));
        as3p=t4.pdDeal(t1.conversion(a));
        System.out.println(as3p);

    }
}
