package com.familyleague.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerResponse {
    private Long id;
    private Long teamId;
    private String teamName;
    private String name;
    private String role;
    private boolean active;
}
