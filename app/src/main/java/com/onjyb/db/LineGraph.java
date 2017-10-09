package com.onjyb.db;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonProperty;

public class LineGraph implements Serializable {
    String label;
    String xValue;
    String yValue;

    @JsonProperty("label")
    public String getLabel() {
        return this.label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("line_1_value")
    public String getLine1Value() {
        return this.xValue;
    }

    @JsonProperty("line_1_value")
    public void setLine1Value(String xValue) {
        this.xValue = xValue;
    }

    @JsonProperty("line_2_value")
    public String getLine2Value() {
        return this.yValue;
    }

    @JsonProperty("line_2_value")
    public void setLine2Value(String yValue) {
        this.yValue = yValue;
    }
}
