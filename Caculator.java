package com.ca;

import java.math.BigDecimal;
import java.util.LinkedList;
import static java.util.regex.Pattern.matches;

/**
 * 实现带括号和小数计算运算
 * 优先运算符号和数学方法一致
 * 小数最多保留8位
 * 小数只有有效精度
 */
public class Caculator {
    //遍历数组时不能改变数组长度，这里记录需要添加的外层括号数量
    private int pdCountS = 0;
    private int pdCountE = 0;

    /**
     * 对外接口
     * @param string 字符串类型的公式，英文符号，不能使用空格
     * @return 结果以字符串类型返回
     */
    public static String  run(String string){

        return new Caculator().start(string);
    }

    /**
     * 主方法入口
     * @param string 从唯一接口获取数据
     * @return  结果返回接口
     */
    private String start(String string){
        LinkedList<String> asp = conversion(string);
        asp=pdDeal(asp);
        for (int i = 0; i <asp.size() ; i++) {
            if (matches(".*\\(.*", asp.get(i))) {
                asp=replace(asp);
                i=-1;
            }
        }
        return  read(asp);
    }

    /**
     * @param string 控制器接收的字符串类型
     * @return LinkedList<String> 下级
     */
    private LinkedList<String> conversion(String string) {                   //字符串转链表
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
                if (matches(".*[（） ].*+", i) ) {
                    System.err.println("输入格式错误：本机不接受非英文字符和括号");
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

    /**
     * 读取计算无括号内容
     * @param as1 任何输入的无空格链表
     * @return  字符串类型结果
     */
    private String read(LinkedList<String> as1){
        int size =0;
        String result = "";
        String start = "";
        String end = "";
        String nums = "";
        String nume = "";
        for (String i:as1) {
            if ( !matches("\\D", i)){   //String为数字时
                if ("".equals(nums)) {
                    nums=i;
                }
                else nume=i;
            }
            else if ("".equals(start)) {             //String为运算符时
                start=i;
            }
            else end=i;
            size++;
            if (!"".equals(end) ||size==as1.size()) {
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
     * @param start 运算符
     * @param nums  运算符左侧数字
     * @param nume  运算符右侧数字
     * @return 字符串类型结果
     */
    private String judgmentC(String start, String nums, String nume) {
        BigDecimal b1 = new BigDecimal(nums);
        BigDecimal b2 = new BigDecimal(nume);
        String re="";
        if ("+".equals(start)) {
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
                div += re.charAt(i);
                if (re.charAt(len-lencount-1) == '.') {
                    break;
                }
            }
            re = div;
        }
        return re;
    }

    /**
     * 括号内优先运算
     * @param asm Tset1单元传入带括号
     * @return  消除括号后返回括号内结果
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
     * 仅供replace方法调用
     * 收缩准备返回的主链表
     * @param asm replace方法中的asm链表
     * @return  修改后返回asm链表
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
        as2=as2Reduce(as2);
        for (int j = 0; j < as2.size(); j++) {
            if (matches(".*\\(.*", as2.get(j))) {
                as2=replace(as2);
                if (index+1-index2==asm.size()) {
                    asm.add(read(as2));
                    return asm;
                }
                if ("".equals(asm.get(0))) {
                    asm.set(0,read(as2));
                    return asm;
                }
                if (index+1-index2>=asm.size() || matches(".*\\d.*||.* .*", asm.get(index+1-index2))) {
                    asm.set(index+1-index2,read(as2));
                }
                else {
                    asm.add(index+1-index2,read(as2));
                }
                return asm;
            }
        }
        if (index+1-index2 >= asm.size()) {
            asm.add(read(as2));
        }
        else if (index+1-index2 ==0) {
            asm.set(index+1-index2,read(as2));
        }
        else {
            asm.add(index+1-index2,read(as2));
        }
        return asm;
    }

    /**
     * 仅供replace方法调用
     * as2为需要计算的括号内部分
     * @param as2 replace 中的as2
     * @return 返回下一步结果
     */
    private LinkedList<String> as2Reduce(LinkedList<String> as2){
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

    /**
     * 对/*号提供括号实现优先级
     * @param asp  conversion方法后
     * @return  添加括号后返回
     */
    private LinkedList<String> pdDeal(LinkedList<String> asp){
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
        if (!"".equals(start)) {
            asp.add(0,start);
        }
        if (!"".equals(end)) {
            asp.add(end);
        }
        return asp;
    }

    /**
     * 仅供pdeal方法调用
     * @param asp       传递链表
     * @param counti    优先级符号出现的位置
     * @param count1    该位置已有的）号数量
     * @param count2    该位置已有的（号数量
     * @return          返回添加距哦好后结果
     */
    private LinkedList<String> pdAdd(LinkedList<String> asp,int counti,int count1,int count2){
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
        String a="5/(6-5)+(9.5+(9.6-4.26))";
        System.out.println(run(a));
    }
}
