package com.onjyb.db;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SummaryTotal implements Serializable {
    String Total_Work_Overtime;
    String Total_break_time;
    String Total_km_drive;
    String Total_overtime;
    String Total_overtime1;
    String Total_overtime2;
    String total_work_hours;

    @JsonProperty("total_work_hours")
    public String getTotal_work_hours() {
        return this.total_work_hours;
    }

    @JsonProperty("total_work_hours")
    public void setTotal_work_hours(String total_work_hours) {
        this.total_work_hours = total_work_hours;
    }

    @JsonProperty("total_break_time")
    public String getTotal_break_time() {
        return this.Total_break_time;
    }

    @JsonProperty("total_break_time")
    public void setTotal_break_time(String total_break_time) {
        this.Total_break_time = total_break_time;
    }

    @JsonProperty("total_overtime1")
    public String getTotal_overtime1() {
        return this.Total_overtime1;
    }

    @JsonProperty("total_overtime1")
    public void setTotal_overtime1(String total_overtime1) {
        this.Total_overtime1 = total_overtime1;
    }

    @JsonProperty("total_km_drive")
    public String getTotal_km_drive() {
        return this.Total_km_drive;
    }

    @JsonProperty("total_km_drive")
    public void setTotal_km_drive(String total_km_drive) {
        this.Total_km_drive = total_km_drive;
    }

    @JsonProperty("total_overtime")
    public String getTotal_overtime() {
        return this.Total_overtime;
    }

    @JsonProperty("total_overtime")
    public void setTotal_overtime(String total_overtime) {
        this.Total_overtime = total_overtime;
    }

    @JsonProperty("total_overtime2")
    public String getTotal_overtime2() {
        return this.Total_overtime2;
    }

    @JsonProperty("total_overtime2")
    public void setTotal_overtime2(String total_overtime2) {
        this.Total_overtime2 = total_overtime2;
    }

    @JsonProperty("total_work_overtime")
    public String getTotal_Work_Overtime() {
        return this.Total_Work_Overtime;
    }

    @JsonProperty("total_work_overtime")
    public void setTotal_Work_Overtime(String total_Work_Overtime) {
        this.Total_Work_Overtime = total_Work_Overtime;
    }
}
