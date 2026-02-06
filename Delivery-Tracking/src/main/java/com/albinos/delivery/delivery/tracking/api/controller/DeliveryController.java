package com.albinos.delivery.delivery.tracking.api.controller;

import com.albinos.delivery.delivery.tracking.api.model.CourierIdInput;
import com.albinos.delivery.delivery.tracking.api.model.DeliveryInput;
import com.albinos.delivery.delivery.tracking.domain.model.Delivery;
import com.albinos.delivery.delivery.tracking.domain.repository.DeliveryRepository;
import com.albinos.delivery.delivery.tracking.domain.service.DeliveryCheckpointService;
import com.albinos.delivery.delivery.tracking.domain.service.DeliveryPreparationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryPreparationService deliveryPreparationService;

    private final DeliveryCheckpointService deliveryCheckpointService;

    private final DeliveryRepository deliveryRepository;

    public DeliveryController(DeliveryPreparationService deliveryPreparationService, DeliveryCheckpointService deliveryCheckpointService, DeliveryRepository deliveryRepository) {
        this.deliveryPreparationService = deliveryPreparationService;
        this.deliveryCheckpointService = deliveryCheckpointService;
        this.deliveryRepository = deliveryRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery draft(@RequestBody @Valid DeliveryInput input) {
        return deliveryPreparationService.draft(input);
    }

    @PutMapping("{deliveryId}")
    public Delivery edit(@PathVariable UUID deliveryId, @RequestBody @Valid DeliveryInput input) {
        return deliveryPreparationService.edit(deliveryId, input);
    }

    @GetMapping
    public PagedModel<Delivery> findAll(@PageableDefault Pageable pageable) {
        return new PagedModel<>(
            deliveryRepository.findAll(pageable)
        );
    }

    @GetMapping("/{deliveryId}")
    public Delivery find(@PathVariable UUID deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));
    }

    @PostMapping("/{deliveryId}/placement")
    public void place(@PathVariable UUID deliveryId) {
        deliveryCheckpointService.place(deliveryId);
    }

    @PostMapping("/{deliveryId}/pickups")
    public void pickup(@PathVariable UUID deliveryId, @Valid @RequestBody CourierIdInput input) {
        deliveryCheckpointService.pickup(deliveryId, input.getCourierId());
    }

    @PostMapping("/{deliveryId}/completion")
    public void complete(@PathVariable UUID deliveryId) {
        deliveryCheckpointService.complete(deliveryId);
    }
}
