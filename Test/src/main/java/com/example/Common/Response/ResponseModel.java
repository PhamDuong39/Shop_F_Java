package com.example.Common.Response;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseModel<T> {
    @Null
    private String Message;
    @Null
    private int StatusCode;
    private boolean Success;
    @Null
    private T Data;
}
