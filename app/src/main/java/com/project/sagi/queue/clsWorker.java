package com.project.sagi.queue;

import java.util.List;

public class clsWorker extends clsUser {
    public clsWorker(){

    }

    public String WorkerImage; //Bitmap
    public String WorkerName;
    public List<clsWorkingHours> WorkerWorkingHours;
    public List<clsWorkingHours> WorkerBreakingHours;
    public List<clsWorkType> WorkerWorkTypes;
}
