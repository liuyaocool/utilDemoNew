package com.liuyao.demo.ws;

public class User{

    private String CHEM_CAS;
    private String CHEM_SHAPE;
    private String CHEM_CATEGORY;
    private String CHEM_NAME;

    public User(String cas){
        this.CHEM_CAS = cas;
    }

    public String getCHEM_CAS() {
        return CHEM_CAS;
    }

    public void setCHEM_CAS(String CHEM_CAS) {
        this.CHEM_CAS = CHEM_CAS;
    }

    public String getCHEM_SHAPE() {
        return CHEM_SHAPE;
    }

    public void setCHEM_SHAPE(String CHEM_SHAPE) {
        this.CHEM_SHAPE = CHEM_SHAPE;
    }

    public String getCHEM_CATEGORY() {
        return CHEM_CATEGORY;
    }

    public void setCHEM_CATEGORY(String CHEM_CATEGORY) {
        this.CHEM_CATEGORY = CHEM_CATEGORY;
    }

    public String getCHEM_NAME() {
        return CHEM_NAME;
    }

    public void setCHEM_NAME(String CHEM_NAME) {
        this.CHEM_NAME = CHEM_NAME;
    }
}
