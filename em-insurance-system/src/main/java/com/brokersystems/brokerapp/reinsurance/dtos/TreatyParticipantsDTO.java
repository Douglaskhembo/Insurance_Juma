package com.brokersystems.brokerapp.reinsurance.dtos;

import java.math.BigDecimal;

public class TreatyParticipantsDTO {

    private  Long treatyClassId;
    private  Long participantId;
    private  String participant;
    private  BigDecimal rate;
    private  String brokerType;
    private  String taxChargeable;
    private  Long brokerId;
    private  Long treatyId;
    private  String broker;
    private  Long revenueItemId;
    private  String revenueItemDesc;
    private  BigDecimal taxRate;
    private  String taxRateType;
    private  BigDecimal recoveryPercent;

    public TreatyParticipantsDTO() {
    }

    private TreatyParticipantsDTO(final Long treatyClassId,
                                  final Long participantId,
                                  final String participant,
                                  final BigDecimal rate,
                                  final String brokerType,
                                  final String taxChargeable,
                                  final Long brokerId,
                                  final String broker,
                                  final Long revenueItemId,
                                  final String revenueItemDesc,
                                  final BigDecimal taxRate,
                                  final String taxRateType,
                                  final BigDecimal recoveryPercent,
                                  final Long treatyId) {
        this.treatyClassId = treatyClassId;
        this.participantId = participantId;
        this.participant = participant;
        this.rate = rate;
        this.brokerType = brokerType;
        this.taxChargeable = taxChargeable;
        this.brokerId = brokerId;
        this.broker = broker;
        this.revenueItemId = revenueItemId;
        this.revenueItemDesc = revenueItemDesc;
        this.taxRate = taxRate;
        this.taxRateType = taxRateType;
        this.recoveryPercent = recoveryPercent;
        this.treatyId = treatyId;
    }

    public  static TreatyParticipantsDTO data(final Long treatyClassId,
                                              final Long participantId,
                                              final String participant,
                                              final BigDecimal rate,
                                              final String brokerType,
                                              final String taxChargeable,
                                              final Long brokerId,
                                              final String broker,
                                              final Long revenueItemId,
                                              final String revenueItemDesc,
                                              final BigDecimal taxRate,
                                              final String taxRateType,
                                              final BigDecimal recoveryPercent,
                                              final Long treatyId){
        return new TreatyParticipantsDTO(treatyClassId, participantId, participant, rate, brokerType,
                taxChargeable, brokerId, broker, revenueItemId, revenueItemDesc, taxRate, taxRateType, recoveryPercent,treatyId);
    }

    public void setTreatyClassId(Long treatyClassId) {
        this.treatyClassId = treatyClassId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public void setBrokerType(String brokerType) {
        this.brokerType = brokerType;
    }

    public void setTaxChargeable(String taxChargeable) {
        this.taxChargeable = taxChargeable;
    }

    public void setBrokerId(Long brokerId) {
        this.brokerId = brokerId;
    }

    public void setTreatyId(Long treatyId) {
        this.treatyId = treatyId;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public void setRevenueItemId(Long revenueItemId) {
        this.revenueItemId = revenueItemId;
    }

    public void setRevenueItemDesc(String revenueItemDesc) {
        this.revenueItemDesc = revenueItemDesc;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public void setTaxRateType(String taxRateType) {
        this.taxRateType = taxRateType;
    }

    public void setRecoveryPercent(BigDecimal recoveryPercent) {
        this.recoveryPercent = recoveryPercent;
    }

    public Long getTreatyId() {
        return treatyId;
    }

    public Long getTreatyClassId() {
        return treatyClassId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public String getParticipant() {
        return participant;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public String getBrokerType() {
        return brokerType;
    }

    public String getTaxChargeable() {
        return taxChargeable;
    }

    public Long getBrokerId() {
        return brokerId;
    }

    public String getBroker() {
        return broker;
    }

    public Long getRevenueItemId() {
        return revenueItemId;
    }

    public String getRevenueItemDesc() {
        return revenueItemDesc;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getTaxRateType() {
        return taxRateType;
    }

    public BigDecimal getRecoveryPercent() {
        return recoveryPercent;
    }
}
