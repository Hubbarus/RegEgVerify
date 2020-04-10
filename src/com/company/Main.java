package com.company;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Проверка номера телефона
Метод checkTelNumber должен проверять, является ли аргумент telNumber валидным номером телефона.

Критерии валидности:
1) если номер начинается с '+', то он содержит 12 цифр
2) если номер начинается с цифры или открывающей скобки, то он содержит 10 цифр
3) может содержать 0-2 знаков '-', которые не могут идти подряд
4) может содержать 1 пару скобок '(' и ')' , причем если она есть, то она расположена левее знаков '-'
5) скобки внутри содержат четко 3 цифры
6) номер не содержит букв
7) номер заканчивается на цифру

Примеры:
+380501234567 - true
+38(050)1234567 - true
+38050123-45-67 - true
050123-4567 - true
+38)050(1234567 - false
+38(050)1-23-45-6-7 - false
050ххх4567 - false
050123456 - false
(0)501234567 - false*/

public class Main {
    public static boolean checkTelNumber(String telNumber) {
        if (telNumber == null) return false;
        int t;
        //1
        boolean plus = telNumber.startsWith("+") && countDigits(telNumber) == 12;

        //2
        Pattern p = Pattern.compile("(^[0-9])|(^\\()");
        Matcher m = p.matcher(telNumber);
        boolean digitBraces = m.find() && countDigits(telNumber) == 10;

        //3
        boolean lines = countLines(telNumber);

        //4-5
        boolean braces = haveBraces(telNumber);

        //6
        p = Pattern.compile("[^0-9\\\\+\\\\(\\\\)\\\\-]");
        m = p.matcher(telNumber);
        boolean onlyDigit = !m.find();

        //7
        p = Pattern.compile("[0-9]$");
        m = p.matcher(telNumber);
        boolean endsWithDigit = m.find();

        return (plus || digitBraces) && lines && braces && onlyDigit && endsWithDigit;
    }

    public static boolean countLines(String tel) {
        int count = 0;
        Pattern p = Pattern.compile("\\d-\\d");
        Matcher m = p.matcher(tel);

        if (!m.find()) return true;
        m.reset();

        while (m.find()) {
            count++;
        }

        if (count > 2) return false;

        p = Pattern.compile("-");
        m = p.matcher(tel);
        while (m.find()) {
            count--;
        }

        if (count == 0) return true;
        else return false;
    }

    public static boolean haveBraces(String tel) {
        if (!tel.contains("(")) return true;

        Pattern p = Pattern.compile("\\(...\\)");
        Matcher mat = p.matcher(tel);
        if (!mat.find()) return false;
        if (mat.find()) return false;
        mat.reset();

        while (mat.find()) {

            Pattern pat = Pattern.compile("-");
            Matcher matcher = pat.matcher(tel);

            if (!matcher.find()) {
                return true;
            }

            int start = mat.start();
            while (matcher.find()) {
                int st = matcher.start();
                if (st < start) return false;
                else return true;
            }
        }
        return false;
    }

    public static int countDigits(String tel) {
        int count = 0;
        Pattern pattern = Pattern.compile("\\d");
        //char[] chars = tel.toCharArray();
        Matcher matcher = pattern.matcher(tel);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static String[] numbers = new String[] {
            "+380501234567", // - true
            "+38(050)1234567", // - true
            "+38050123-45-67", // - true
            "050123-4567", // - true
            "+38)050(1234567", // - false
            "+38(050)1-23-45-6-7", // - false
            "050ххх4567", // - false
            "050123456", // - false
            "(0)501234567"}; // - false

    public static void main(String[] args) {
        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i] + " - " + checkTelNumber(numbers[i]));
        }

    }
}
