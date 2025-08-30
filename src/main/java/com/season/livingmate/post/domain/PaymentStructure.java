package com.season.livingmate.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PaymentStructure {
    @Column(nullable = false)
    private boolean depositShare;

    @Column(nullable = false)
    private boolean rentShare;

    @Column(nullable = false)
    private boolean maintenanceShare;

    @Column(nullable = false)
    private boolean utilitiesShare;

    protected PaymentStructure() {}
    public PaymentStructure(boolean depositShare, boolean rentShare, boolean maintenanceShare, boolean utilitiesShare) {
        this.depositShare = depositShare;
        this.rentShare = rentShare;
        this.maintenanceShare = maintenanceShare;
        this.utilitiesShare = utilitiesShare;
    }
}
