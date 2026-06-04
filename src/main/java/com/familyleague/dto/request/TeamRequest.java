package com.familyleague.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 10)
    private String shortName;

    private String logoUrl;
}
