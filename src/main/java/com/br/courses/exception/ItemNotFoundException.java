package com.br.courses.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(Long id) {
        super("Item não encontrado com id: " + id);
    }
}