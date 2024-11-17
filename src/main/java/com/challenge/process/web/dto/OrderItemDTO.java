package com.challenge.process.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItemDTO {

    @JsonProperty("item_id")
    private String itemId;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("price")
    private Double price;

    public OrderItemDTO() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
