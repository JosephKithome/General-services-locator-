package com.example.sejjoh.gsls.Model;

public class Plus_code {
    private String global_code;

    public String getGlobal_code ()
    {
        return global_code;
    }

    public void setGlobal_code (String global_code)
    {
        this.global_code = global_code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [global_code = "+global_code+"]";
    }
}