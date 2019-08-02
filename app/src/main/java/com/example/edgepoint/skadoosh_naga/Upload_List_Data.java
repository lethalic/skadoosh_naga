package com.example.edgepoint.skadoosh_naga;

public class Upload_List_Data {

    private String votersname,phone,mayor,indicator,deceased,encoder;
    private int votersid;

    public Upload_List_Data(){
    }

    public Upload_List_Data(String votersname,String phone,String mayor,String indicator,String deceased,String encoder){
        this.votersname = votersname;
        this.phone = phone;
        this.mayor = mayor;
        this.indicator = indicator;
        this.deceased = deceased;
        this.encoder = encoder;
    }
    public String getVotersname() {
        return votersname;
    }

    public void setVotersname(String votersname) {
        this.votersname = votersname;
    }

    public String getPhoneUpload() {
        return phone;
    }

    public void setPhoneUpload(String phone) {
        this.phone = phone;
    }

    public String getMayorUpload() {
        return mayor;
    }

    public void setMayorUpload(String mayor) {
        this.mayor = mayor;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getDeceased() {
        return deceased;
    }

    public void setDeceased(String deceased) {
        this.deceased = deceased;
    }

    public String getEncoder() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
    }
}
