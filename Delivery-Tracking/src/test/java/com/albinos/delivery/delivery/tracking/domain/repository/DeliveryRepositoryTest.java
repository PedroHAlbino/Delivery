package com.albinos.delivery.delivery.tracking.domain.repository;

import com.albinos.delivery.delivery.tracking.domain.model.ContactPoint;
import com.albinos.delivery.delivery.tracking.domain.model.Delivery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void shouldPersist(){
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.addItem("Computador", 2);
        delivery.addItem("Notebook", 3);

        deliveryRepository.saveAndFlush(delivery);

        Delivery persistendDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();

        assertEquals(2, persistendDelivery.getItems().size());

    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {

        ContactPoint sender = ContactPoint.builder()
                .zipCode("00000-001")
                .street("Street 2")
                .number("500")
                .complement("")
                .name("Joao Silva")
                .phone("(81) 99999-9999")
                .build();

        ContactPoint recipient = ContactPoint.builder()
                .zipCode("00000-000")
                .street("Street 1")
                .number("100")
                .complement("Sala 401")
                .name("Silva")
                .phone("(81) 99999-9999")
                .build();

        return com.albinos.delivery.delivery.tracking.domain.model.Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(new BigDecimal("15.00"))
                .courierPayout(new BigDecimal("5.00"))
                .expectedDeliveryTime(Duration.ofHours(5))
                .build();
    }
}