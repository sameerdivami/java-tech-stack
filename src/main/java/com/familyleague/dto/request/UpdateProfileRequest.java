package com.familyleague.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(max = 100)
    private String displayName;

    @Size(max = 100)
    private String avatarName;
}
