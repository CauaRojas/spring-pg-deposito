package br.com.fiap.springpgdeposito.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_DEP_ITEM_ESTOCADO")
public class ItemEstocado {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "SQ_ITEM_ESTOCADO")
    @SequenceGenerator(name = "SQ_ITEM_ESTOCADO", sequenceName = "SQ_ITEM_ESTOCADO", allocationSize = 1)
    private Long id;
    private String numeroDeSerie;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "PRODUTO", referencedColumnName = "ID_PRODUTO", foreignKey = @ForeignKey(name = "FK_ITEM_ESTOCADO_PRODUTO"))
    private Produto produto;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "DEPOSITO", referencedColumnName = "ID_DEPOSITO", foreignKey = @ForeignKey(name = "FK_ITEM_ESTOCADO_DEPOSITO"))
    private Deposito deposito;
}
