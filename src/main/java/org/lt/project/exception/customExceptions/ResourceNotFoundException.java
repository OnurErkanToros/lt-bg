package org.lt.project.exception.customExceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Kayıt bulunamadı.");
    }
    public ResourceNotFoundException(String message){
        super(message);
    }
}
