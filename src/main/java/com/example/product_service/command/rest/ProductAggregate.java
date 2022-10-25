package com.example.product_service.command.rest;

import com.example.product_service.event.ProductCreateEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private  String productId;
    private  String title;
    private  BigDecimal price;
    private  Integer quantity;

    public  ProductAggregate(){}
    @CommandHandler
    public ProductAggregate(CreateProductCommand command){
        //Business logic validation
        if (command.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Price cannot be less than or equal to zero.");
        }
        if(command.getTitle() == null || command.getTitle().isBlank()){
            throw new IllegalArgumentException("Title cannot be blank.");
        }

        //event
        ProductCreateEvent event = new ProductCreateEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);

    }
    @EventSourcingHandler
    public void on(ProductCreateEvent event){
        this.productId = event.getProductId();
        this.title = event.getTitle();
        this.price = event.getPrice();
        this.quantity = event.getQuantity();
        System.out.println(this.productId);
    }

}
