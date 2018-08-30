package com.ca.caunit;

import java.util.LinkedList;

import static java.util.regex.Pattern.matches;

/**
 * 单元测试第三阶段
 * 功能：
 * 1.计算出第一个外围括号的结果并返回
 * 2.括号超过2层解一层立即返回
 * 输入单元：Test1
 * 输入类型：LinkedList<String>
 * 输出类型：LinkedList<String>
 * 已测试类型：       输入格式                                          返回格式
 * 1.        [(, 6.4, +, 4.9, )+(, 9.6, -, 4.2, )]              [11.3, +(, 9.6, -, 4.2, )]
 * 2.               [(, 6.4, +, 9.6, )-, 4.2]                       [6.4, +, 9.6, -, 4.2]
 * 3.             [(, 6.4, +, 9.6, -, 4.2, )]                        [6.4, +, 9.6, -, 4.2]
 * 4.              [((, 6.4, +, 9.6, )-, 4.2, )]                    [(, 6.4, +, 9.6, )-, 4.2]
 * 5.              [(, 6.4, +(, 9.6, -, 4.2, ))]                    [6.4, +(, 9.6, -, 4.2, )]
 * 6.         [6.4, +(, 4.9, +(, 9.6, -, 4.2, ))]                         [6.4, +, 10.3]
 * 7.        [6.4, +(, 4.9, +(, 9.6, -, 4.2, ))+, 6]                   [6.4, +, 10.3, +, 6]
 * 8.       [6.4, +((, 4.9, +(, 9.6, -, 4.2, ))+, 6, )]         [6.4, +(, 4.9, +(, 9.6, -, 4.2, ))+, 6]
 * 备注：
 * 1.绑定测试asm,as2二次处理类reduce
 */
public class Test3 {

    /**
     *
     * @param asm Tset1单元传入带括号
     * @return
     */
    private LinkedList<String> replace(LinkedList<String> asm){
        LinkedList<String> as2 = new LinkedList<>();
        int count1 = 0;    //（ 号的数量
        int count2 = 0;    // ）号的数量
        int key1 = 0;      //锁1，第一个括号处理完后打开
        int key2 = 0;      //锁2，检测到第一个括号时打开
        int index=0;       //指针，第一个 ( 括号出现时对应的i位置

        //解最外圈括号并添加到as2
        for (int i = 0; i < asm.size()-1; i++) {
            if (key2 != 1 && matches(".*\\(.*", asm.get(i))) {
                key2=1;
                index=i;
            }
            //静态锁2打开后开始添加到as2
            if (key2 == 1) {
                as2.add(asm.get(i));
            }
            //拆外圈括号
            if (matches(".*\\(.*", asm.get(i))) {
                int cmt1=0;
                for (int j = 0; j < asm.get(i).length(); j++) {
                    if (asm.get(i).charAt(j) == '(') {
                        cmt1++;
                    }
                }
                count1 += cmt1;
                if (key1 != 1) {
                    asm.set(i, asm.get(i).replaceFirst("\\(", ""));
                }
                key1=1;
            }
            if (matches(".*\\).*", asm.get(i + 1))) {
                int cmt1=0;
                int a=asm.get(i+1).length();
                for (int j = 0; j < a; j++) {
                    if (asm.get(i+1).charAt(j) == ')') {
                        cmt1++;
                    }
                }
                count2 += cmt1;
                if (count1 == count2) {
                    as2.add(asm.get(i+1));
                    asm.set(i + 1, asm.get(i + 1).replaceFirst("\\)", ""));
                }
            }
            System.out.println(asm);
            System.out.println(as2);


            if (count1 == count2 && count1 != 0) {
                int index2=0;
                if (matches(".*\\(.*",as2.get(0))) {
                    int countS = 0;
                    int countE = 0;
                    for (int j = 0; j < as2.get(0).length(); j++) {
                        if (as2.get(0).charAt(j)=='(') {
                            countS++;
                        }
                    }
                    for (int j = 0; j < as2.get(as2.size()-1).length(); j++) {
                        if (as2.get(as2.size()-1).charAt(j)==')') {
                            countE++;
                        }
                    }
                    if (countE > 1 && countS > 1) {
                        return asm;
                    }
                }

                if ("".equals(asm.get(asm.size()-1))) {
                    asm.remove(asm.size()-1);
                }
                if ("".equals(asm.get(0))) {
                    asm.remove(0);
                    index2=1;
                }
                if (asm.size() < as2.size()) {
                    return asm;
                }
                if (as2 !=null) {
                    asm=asmReduce(asm,as2,index,index2);
                    return asm;
                }
                break;
            }
        }
        if ("".equals(asm.get(asm.size()-1))) {
            asm.remove(asm.size()-1);
        }
        return asm;
    }

    /**
     *
     * @param asm
     * @return
     */
    private LinkedList<String> asmReduce(LinkedList<String> asm,LinkedList<String> as2,int index,int index2){
        for (int j = 1; j <as2.size()-1-index2; j++) {
            asm.remove(index+1-index2);
        }
        if (matches(".*\\(.*", asm.get(index))) {
            asm.set(index, asm.get(index).replaceAll("\\(", ""));
        }
        if (asm.size()>index+1 && matches(".*\\).*", asm.get(index+1))) {
            asm.set(index+1, asm.get(index+1).replaceAll("\\)", ""));
        }
        as2=as2Reduce(asm,as2,index);
        for (int j = 0; j < as2.size(); j++) {
            if (matches(".*\\(.*", as2.get(j))) {
                as2=replace(as2);
                System.out.println(as2);
                System.out.println(asm);
                if (index+1-index2==asm.size()) {
                    asm.add(new Test2().read(as2));
                    return asm;
                }
                if ("".equals(asm.get(0))) {
                    asm.set(0,new Test2().read(as2));
                    return asm;
                }
                if (index+1-index2>=asm.size() || matches(".*\\d.*||.* .*", asm.get(index+1-index2))) {
                    asm.set(index+1-index2,new Test2().read(as2));
                }
                else {
                    asm.add(index+1-index2,new Test2().read(as2));
                }
                return asm;
            }
        }
        System.out.println(asm);
        if (index+1-index2 >= asm.size()) {
            asm.add(new Test2().read(as2));
        }
        else if (index+1-index2 ==0) {
            asm.set(index+1-index2,new Test2().read(as2));
        }
        else {
            asm.add(index+1-index2,new Test2().read(as2));
        }
        return asm;
    }


    /**
     *
     * @param as2
     * @return
     */
    private LinkedList<String> as2Reduce(LinkedList<String> asm,LinkedList<String> as2,int index){
        if (matches(".*\\).*", as2.get(as2.size()-1))) {
            as2.set(as2.size()-1, as2.get(as2.size()-1).replaceFirst("\\)", ""));
            as2.set(as2.size()-1, as2.get(as2.size()-1).replaceAll("[^)]", ""));
        }
        if (!matches(".*\\).*", as2.get(as2.size()-1))) {
            as2.remove(as2.size()-1);
        }

        if (matches(".*\\(.*", as2.get(0))) {
            as2.set(0, as2.get(0).replaceFirst("\\(", ""));
            as2.set(0, as2.get(0).replaceAll("[^(]", ""));
        }
        if (!matches(".*\\(.*", as2.get(0))) {
            as2.remove(0);
        }
        return as2;
    }

    public static void main(String[] args) {
        LinkedList<String> as2p = new LinkedList<>();
        String a="11.3+(9.5+(9.6-4.2))";
        Test1 t1 = new Test1();
        Test3 t3 = new Test3();
        as2p=t3.replace(t1.conversion(a));
        System.out.println(as2p);
    }
}

