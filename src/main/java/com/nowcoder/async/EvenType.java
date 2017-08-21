package com.nowcoder.async;

/**
 * Created by albert on 2017/8/15.
 */
public enum EvenType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    QUESTION(4),
    FOLLOW(5),
    ADDQUESTION(6);

    private int value;

    EvenType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static void main(String[] args) {
        System.out.println(EvenType.LOGIN);
    }
}
