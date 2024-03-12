package br.com.fiap.springpgdeposito.resource;

import br.com.fiap.springpgdeposito.entity.ItemEstocado;
import br.com.fiap.springpgdeposito.repository.DepositoRepository;
import br.com.fiap.springpgdeposito.repository.ItemEstocadoRepository;
import br.com.fiap.springpgdeposito.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/entrada")
public class EntradaResource {
    @Autowired
    private DepositoRepository repoDepository;
    @Autowired
    private ProdutoRepository repoProduto;
    @Autowired
    private ItemEstocadoRepository repoItem;

    @PostMapping(value = "/deposito/{idDeposito}/produto/{idProduto}")
    @Transactional
    public ResponseEntity<List<ItemEstocado>> persist(@RequestBody int qtd, @PathVariable(name = "idDeposito") Long idDeposito, @PathVariable(name = "idProduto") Long idProduto) {
    // recebe um inteiro no corpo da requisição.

        var deposito = repoDepository.findById(idDeposito);
        var produto = repoProduto.findById(idProduto);

        if (deposito.isEmpty() || produto.isEmpty()) return ResponseEntity.badRequest().build();

        List<ItemEstocado> itensEstocados = new ArrayList<>();

        for (int i = 0; i < qtd; i++){
            ItemEstocado itemEstocado = new ItemEstocado();
            itemEstocado.setDeposito(deposito.get());
            itemEstocado.setProduto(produto.get());
            itemEstocado.setEntrada(LocalDateTime.now());
            itemEstocado.setNumeroDeSerie(UUID.randomUUID().toString());
            itensEstocados.add(itemEstocado);
        }
        repoItem.saveAll(itensEstocados);

        return ResponseEntity.ok( itensEstocados );
    }
}
