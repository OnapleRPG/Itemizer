package com.onaple.itemizer.data.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AttributeParams extends HashMap<String, Object> {

    public AttributeParams() {
    }

    public AttributeParams(Map<? extends String, ?> map) {
        super(map);
    }

    public AttributeParams(AttributeBean attributeBean) {
        super();
        UUID uuid = UUID.randomUUID();
        this.put("AttributeName", attributeBean.getName());
        this.put("Name", attributeBean.getName());
        this.put("Amount", attributeBean.getAmount());
        this.put("Operation", attributeBean.getOperation());
        this.put("Slot", attributeBean.getSlot());
        this.put("UUIDMost", uuid.getMostSignificantBits());
        this.put("UUIDLeast", uuid.getLeastSignificantBits());
    }

    public AttributeBean fromMap() {
        return AttributeBean.builder()
                .name((String) this.get("Name"))
                .amount((Float) this.get("Amount"))
                .operation((int) this.get("Operation"))
                .slot( (String) this.get("Slot"))
                .build();
    }
}
