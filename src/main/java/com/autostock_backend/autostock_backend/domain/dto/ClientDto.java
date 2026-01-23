package com.autostock_backend.autostock_backend.domain.dto;

import lombok.Data;

@Data
public class ClientDto {
    private Long idClient;
    private String nom;
    private String telephone;
}
