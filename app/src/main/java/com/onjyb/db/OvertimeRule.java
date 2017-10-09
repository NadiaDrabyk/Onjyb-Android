package com.onjyb.db;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OvertimeRule implements Serializable {
    String Actualvalue;
    String RuleName;
    String RuleValue;
    String id;
    String ot_type;
    String ref_comp_id;
    String rule_per;
    String total_overtime_per_rule;

    @JsonProperty("rule")
    public String getRuleName() {
        return this.RuleName;
    }

    @JsonProperty("rule")
    public void setRuleName(String ruleName) {
        this.RuleName = ruleName;
    }

    @JsonProperty("minutes")
    public String getRuleValue() {
        return this.RuleValue;
    }

    @JsonProperty("minutes")
    public void setRuleValue(String ruleValue) {
        this.RuleValue = ruleValue;
    }

    @JsonProperty("actual_minute")
    public String getActualvalue() {
        return this.Actualvalue;
    }

    @JsonProperty("actual_minute")
    public void setActualvalue(String actualvalue) {
        this.Actualvalue = actualvalue;
    }

    @JsonProperty("id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("ot_type")
    public String getOt_type() {
        return this.ot_type;
    }

    @JsonProperty("ot_type")
    public void setOt_type(String ot_type) {
        this.ot_type = ot_type;
    }

    @JsonProperty("ref_company_id")
    public String getRef_comp_id() {
        return this.ref_comp_id;
    }

    @JsonProperty("ref_company_id")
    public void setRef_comp_id(String ref_comp_id) {
        this.ref_comp_id = ref_comp_id;
    }

    @JsonProperty("rule_per")
    public String getRule_per() {
        return this.rule_per;
    }

    @JsonProperty("rule_per")
    public void setRule_per(String rule_per) {
        this.rule_per = rule_per;
    }

    @JsonProperty("total_overtime_per_rule")
    public String getTotal_overtime_per_rule() {
        return this.total_overtime_per_rule;
    }

    @JsonProperty("total_overtime_per_rule")
    public void setTotal_overtime_per_rule(String total_overtime_per_rule) {
        this.total_overtime_per_rule = total_overtime_per_rule;
    }
}
