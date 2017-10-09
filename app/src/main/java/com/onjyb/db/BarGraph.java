package com.onjyb.db;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonProperty;

public class BarGraph implements Serializable {
    String DeviationValueColor;
    String actualValue;
    String deviationValue;
    String expectedValue;
    String expectedValueColor;
    String label;

    @JsonProperty("actual_value")
    public String getActualValue() {
        return this.actualValue;
    }

    @JsonProperty("actual_value")
    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    @JsonProperty("deviation_value")
    public String getDeviationValue() {
        return this.deviationValue;
    }

    @JsonProperty("deviation_value")
    public void setDeviationValue(String deviationValue) {
        this.deviationValue = deviationValue;
    }

    @JsonProperty("expected_value")
    public String getExpectedValue() {
        return this.expectedValue;
    }

    @JsonProperty("expected_value")
    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    @JsonProperty("color_2")
    public String getDeviationValueColor() {
        return this.DeviationValueColor;
    }

    @JsonProperty("color_2")
    public void setDeviationValueColor(String deviationValueColor) {
        this.DeviationValueColor = deviationValueColor;
    }

    @JsonProperty("color_1")
    public String getExpectedValueColor() {
        return this.expectedValueColor;
    }

    @JsonProperty("color_1")
    public void setExpectedValueColor(String expectedValueColor) {
        this.expectedValueColor = expectedValueColor;
    }

    @JsonProperty("label")
    public String getLabel() {
        return this.label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }
}
