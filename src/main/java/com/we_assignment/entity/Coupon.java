package com.we_assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Coupon extends Timestamped {

    @Id
    private UUID id;

    @Column(name = "code", length = 16, nullable = false, unique = true)
    private String code;

    @Column(name = "is_redeemed")
    private boolean isRedeemed = false;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(name = "coupon_topic_fk", nullable = false)
    private CouponTopic couponTopic;

    public void useCoupon() {
        this.isRedeemed = true;
    }

    public void inActivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

}
