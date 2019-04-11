package com.onaple.itemizer.exception;

public class ItemNotPresentException extends Exception {

    public ItemNotPresentException(String id) {
        super(id + " is not present in item list");
    }
}
