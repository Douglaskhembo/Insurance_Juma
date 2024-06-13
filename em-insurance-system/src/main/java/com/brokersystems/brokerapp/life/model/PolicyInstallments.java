package com.brokersystems.brokerapp.life.model;

import com.brokersystems.brokerapp.uw.model.PolicyTrans;
import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "sys_brk_life_installments")
public class PolicyInstallments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pi_inst_id")
    private Long installmentsId;

    private BigDecimal installPrem;

    private Date dueDate;

    private String installPaid;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="lrct_policy_id")
    private PolicyTrans policyTrans;



}
