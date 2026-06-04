package com.familyleague.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String displayName;
    private String avatarName;
    private String role;
    private boolean active;
}
