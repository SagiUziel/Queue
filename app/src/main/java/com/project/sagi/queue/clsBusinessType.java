package com.project.sagi.queue;

public class clsBusinessType{
    public int Id;
    public String BusinessType;

    public clsBusinessType(int Id, String BusinessType){
        this.Id = Id;
        this.BusinessType = BusinessType;
    }

    public String toString()
    {
        return BusinessType;
    }
}
