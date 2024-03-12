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

        ItemEstocado itemEstocado = new ItemEstocado();
        var deposito = repoDepository.findById(idDeposito);

        if (deposito.isEmpty()) return ResponseEntity.badRequest().build();
        itemEstocado.setDeposito(deposito.get());

        var produto = repoProduto.findById(idProduto);

        if(produto.isEmpty())return ResponseEntity.badRequest().build();
        itemEstocado.setProduto(produto.get());

        itemEstocado.setEntrada(LocalDateTime.now());

        List itensEstocados = new ArrayList<ItemEstocado>();

        for (int i = 0; i < qtd; i++){
            var saved = repoItem.save(itemEstocado);
            itensEstocados.add(saved);
        }

        return ResponseEntity.ok( itensEstocados );
    }
}
