package br.com.fiap.springpgdeposito.resource;

import br.com.fiap.springpgdeposito.entity.ItemEstocado;
import br.com.fiap.springpgdeposito.repository.DepositoRepository;
import br.com.fiap.springpgdeposito.repository.ItemEstocadoRepository;
import br.com.fiap.springpgdeposito.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(value = "/saida")
public class SaidaResource {
    @Autowired
    private DepositoRepository depoRepository;
    @Autowired
    private ItemEstocadoRepository itemRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @PostMapping(value = "deposito/{idDeposito}/produto/{idProduto}")
    public ResponseEntity<List<ItemEstocado>> persist(@PathVariable(name = "idDeposito") Long idDeposito,
                                                      @PathVariable(name = "idProduto") Long idProduto,
                                                      @RequestBody int qtd){
        var deposito = depoRepository.findById(idDeposito);
        var produto = produtoRepository.findById(idProduto);
        if (deposito.isEmpty() || produto.isEmpty()) return ResponseEntity.badRequest().build();
        var items = itemRepository.findAll();
        AtomicInteger count = new AtomicInteger();
        List<ItemEstocado> itemsRetornados = new ArrayList<>();
        items.forEach(item -> {
            if (item.getDeposito().getId().equals(idDeposito) // Item pertence ao deposito
                    && item.getProduto().getId().equals(idProduto) // Item é do produto
                    && count.get() < qtd // Ainda não atingiu a quantidade solicitada
                    &&  item.getSaida() == null){ // Item ainda não foi retirado
                item.setSaida(LocalDateTime.now());
                itemsRetornados.add(item);
                count.addAndGet(1);
            }
        });
        if (count.get() < qtd) return ResponseEntity.badRequest().build(); // Não tem a quantidade solicitada
        itemRepository.saveAll(itemsRetornados);
        return ResponseEntity.ok().body(itemsRetornados);
    }}
