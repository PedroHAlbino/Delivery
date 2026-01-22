package com.albinos.delivery.courier.management.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
public class Courier {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter
    private String name;

    @Setter
    private String phone;

    private Integer fulfilledDeliveriesQuantity;

    private Integer pendingDeliveriesQuantity;

    private OffsetDateTime lastFulfilledDeliveryAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "courier")
    private List<AssignedDelivery> pendingDeliveries = new ArrayList<>();

    public List<AssignedDelivery> getPendingDeliveries() {
        return Collections.unmodifiableList(this.pendingDeliveries);
    }

    public static Courier brandNew(String name, String phone) {
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID());
        courier.setName(name);
        courier.setPhone(phone);
        return courier;
    }

    public void assing(UUID deliveryId) {
        this.pendingDeliveries.add(
                AssignedDelivery.pending(deliveryId, this)
        );
        this.pendingDeliveriesQuantity += 1;
    }

    public void fulfilledDelivery(UUID deliveryId) {
        AssignedDelivery delivery = this.pendingDeliveries.stream().filter(d -> d.getId().equals(deliveryId))
                .findFirst().orElseThrow();

        this.pendingDeliveries.remove(delivery);
        this.pendingDeliveriesQuantity -= 1;
        this.fulfilledDeliveriesQuantity += 1;
        this.lastFulfilledDeliveryAt = OffsetDateTime.now();
    }
}
