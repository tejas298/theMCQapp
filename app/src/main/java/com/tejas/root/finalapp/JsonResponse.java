package com.tejas.root.finalapp;

import java.io.Serializable;

/**
 * Created by root on 24/12/18.
 */

public class JsonResponse implements Serializable{

    private int id;
    private String question;
    private Options options;

    private String ans;
    private char user_ans;

    public char getUser_ans() {
        return user_ans;
    }

    public void setUser_ans(char user_ans) {
        this.user_ans = user_ans;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public class Options implements Serializable{

        private String a;
        private String b;
        private String c;
        private String d;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getD() {
            return d;
        }

        public void setD(String d) {
            this.d = d;
        }
    }
}
