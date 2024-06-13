package com.brokersystems.brokerapp.accounts.model;

import javax.persistence.*;

/**
 * Created by peter on 4/7/2017.
 */
@Entity
@Table(name="sys_brk_coa_main")
public class CoaMainAccounts {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="co_Id")
    private Long coId;

    @Column(name="co_code",nullable = false)
    private String code;

    @Column(name="co_name",nullable = false)
    private String name;

    @Column(name="co_header",nullable = false)
    private String header;

    @Column(name="co_footer",nullable = false)
    private String footer;

    @Column(name="co_acc_type",nullable = false)
    private String accountType;

    @Column(name="co_pl_bs",nullable = false)
    private String plBalSheet;

    @Column(name="co_accounts_order",nullable = false)
    private String accountsOrder;

    public Long getCoId() {
        return coId;
    }

    public void setCoId(Long coId) {
        this.coId = coId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPlBalSheet() {
        return plBalSheet;
    }

    public void setPlBalSheet(String plBalSheet) {
        this.plBalSheet = plBalSheet;
    }

    public String getAccountsOrder() {
        return accountsOrder;
    }

    public void setAccountsOrder(String accountsOrder) {
        this.accountsOrder = accountsOrder;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }
}
