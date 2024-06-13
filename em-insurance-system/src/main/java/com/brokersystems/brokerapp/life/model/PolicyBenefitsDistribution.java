package com.brokersystems.brokerapp.life.model;

import com.brokersystems.brokerapp.uw.model.PolicyTrans;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by waititu on 04/12/2017.
 */
@Entity
@Table(name = "sys_brk_pol_benefits")
public class PolicyBenefitsDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mat_id")
    private Long maturityId;

    @ManyToOne
    @JoinColumn(name = "mat_pol_id",nullable = false)
    private PolicyTrans policyId;

    @Column(name = "mat_yr")
    private int maturityYear;

    @Column (name = "mat_expected_dt")
    private Date maturityExpDate;

    @Column(name = "mat_est_benefit")
    private double estBenefit;

    public Long getMaturityId() {
        return maturityId;
    }

    public void setMaturityId(Long maturityId) {
        this.maturityId = maturityId;
    }

    public PolicyTrans getPolicyId() {
        return policyId;
    }

    public void setPolicyId(PolicyTrans policyId) {
        this.policyId = policyId;
    }

    public int getMaturityYear() {
        return maturityYear;
    }

    public void setMaturityYear(int maturityYear) {
        this.maturityYear = maturityYear;
    }

    public Date getMaturityExpDate() {
        return maturityExpDate;
    }

    public void setMaturityExpDate(Date maturityExpDate) {
        this.maturityExpDate = maturityExpDate;
    }

    public double getEstBenefit() {
        return estBenefit;
    }

    public void setEstBenefit(double estBenefit) {
        this.estBenefit = estBenefit;
    }
}
