package com.familyleague.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamResponse {
    private Long id;
    private String name;
    private String shortName;
    private String logoUrl;
}
