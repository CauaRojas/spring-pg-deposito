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
            if (item.getDeposito().getId().equals(idDeposito) && item.getProduto().getId().equals(idProduto) && count.get() < qtd && item.getSaida()== null) {
                item.setSaida(LocalDateTime.now());
                itemRepository.save(item);
                itemsRetornados.add(item);
                count.addAndGet(1);
            }
        });
        return ResponseEntity.ok().body(itemsRetornados);
    }}
