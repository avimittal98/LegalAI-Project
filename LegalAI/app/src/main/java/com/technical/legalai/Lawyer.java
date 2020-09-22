package com.technical.legalai;

import java.io.Serializable;

public class Lawyer implements Serializable {

    public String lawyer_UID;
    public String lawyer_name;
    public String phone;
    public String email;
    public String dop;
    public String address;
    public String education;
    public String courts_prac;
    public String areas_prac;
    public String charges;
    public String lawyer_firm;
    public String no_cases;
    public String no_wins;
    public String IPC;
    public String more_info;
    public String current_case;
    public String new_case;
    public String photo;

    public Lawyer() {
        // Default constructor required for calls to DataSnapshot.getValue(Lawyer.class)
    }

    public Lawyer(String lawyer_name, String email, String dop, String phone, String address, String education, String courts_prac, String areas_prac, String charges, String lawyer_firm, String no_cases, String no_wins, String IPC, String more_info, String Uid,String photo) {
        this.lawyer_name = lawyer_name;
        this.email = email;
        this.dop = dop;
        this.phone = phone;
        this.address = address;
        this.education = education;
        this.courts_prac = courts_prac;
        this.areas_prac = areas_prac;
        this.charges = charges;
        this.lawyer_firm = lawyer_firm;
        this.no_cases = no_cases;
        this.no_wins = no_wins;
        this.IPC = IPC;
        this.more_info = more_info;
        this.lawyer_UID = Uid;
        this.photo = photo;
        this.current_case = null;
        this.new_case = null;
    }
}