package com.familyleague.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BulkNotifyRequest {

    @NotEmpty
    private List<Long> userIds;

    @NotBlank
    private String subject;

    @NotBlank
    private String message;
}
