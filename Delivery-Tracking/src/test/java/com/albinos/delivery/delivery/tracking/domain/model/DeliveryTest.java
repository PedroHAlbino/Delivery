package com.albinos.delivery.delivery.tracking.domain.model;

import com.albinos.delivery.delivery.tracking.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {


    @Test
    public void shouldChangeStatusToPlaced(){
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.place();

        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlacedAt());
    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {

        ContactPoint sender = ContactPoint.builder()
                .zipCode("00000-001")
                .street("Street 2")
                .mumber("500")
                .complement("")
                .name("Joao Silva")
                .phone("(81) 99999-9999")
                .build();

        ContactPoint recipient = ContactPoint.builder()
                .zipCode("00000-000")
                .street("Street 1")
                .mumber("100")
                .complement("Sala 401")
                .name("Silva")
                .phone("(81) 99999-9999")
                .build();

        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(new BigDecimal("15.00"))
                .courierPayout(new BigDecimal("5.00"))
                .expectedDeliveryTime(Duration.ofHours(5))
                .build();
    }

    @Test
    public void shouldNotPlace(){
        Delivery delivery = Delivery.draft();
        assertThrows(DomainException.class, () -> {
            delivery.place();
        });
        assertEquals(DeliveryStatus.DRAFT, delivery.getStatus());
        assertNull(delivery.getPlacedAt());
    }

}