package org.lt.project.dto.resultDto;

public class SuccessDataResult<T> extends DataResult<T>{

    public SuccessDataResult() {
        super(true);
    }
    public SuccessDataResult(String message) {
        super(true,message);
    }
    public SuccessDataResult(String message,T data) {
        super(true,message,data);
    }
    public SuccessDataResult(T data) {
        super(true,data);
    }
}
