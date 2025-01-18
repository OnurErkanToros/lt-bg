package org.lt.project.dto.resultDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result {
    private boolean success;
    private String message;

    public Result(boolean success) {
        this.success = success;
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
