package br.com.fiap.springpgdeposito.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Pedido {
    private Integer qtde;
}
