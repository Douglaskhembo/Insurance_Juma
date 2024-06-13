package com.brokersystems.brokerapp.server.utils;

/**
 * Created by HP on 9/22/2017.
 */
public class PremiumResultBean {

    private final double premium;
    private final double premiumFull;
    private final double sumInsured;

    public PremiumResultBean(double premium, double premiumFull, double sumInsured) {
        this.premium = premium;
        this.premiumFull = premiumFull;
        this.sumInsured = sumInsured;
    }

    public PremiumResultBean(double sumInsured, double premium) {
        this.sumInsured = sumInsured;
        this.premium = premium;
        this.premiumFull=0;
    }

    public double getPremium() {
        return premium;
    }

    public double getPremiumFull() {
        return premiumFull;
    }

    public double getSumInsured() {
        return sumInsured;
    }
}
