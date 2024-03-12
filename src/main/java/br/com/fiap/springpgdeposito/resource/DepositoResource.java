package br.com.fiap.springpgdeposito.resource;

import br.com.fiap.springpgdeposito.entity.Deposito;
import br.com.fiap.springpgdeposito.repository.DepositoRepository;
import br.com.fiap.springpgdeposito.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/deposito")
public class DepositoResource {

    @Autowired
    private DepositoRepository repository;

    @Autowired
    private EnderecoRepository repoEndereco;

    @GetMapping
    public  ResponseEntity<List<Deposito>> findAll() {
        return ResponseEntity.ok( repository.findAll() );
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Deposito> findById(@PathVariable(name = "id") Long id) {
        Deposito objeto = repository.findById( id ).orElse( null );
        if (Objects.isNull( objeto )) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok( objeto );
    }


    @PostMapping
    @Transactional
    public ResponseEntity<Deposito> persist(@RequestBody Deposito objeto) {
        if (Objects.nonNull(objeto.getEndereco().getId())) {

            //Buscando o endereco com o Id informado na requisição
            var endereco = repoEndereco.findById(objeto.getEndereco().getId());

            //Se não encontrou o endereco com o id informado então foi uma requisição maliciosa
            if (endereco.isEmpty()) return ResponseEntity.badRequest().build();
            objeto.setEndereco(endereco.get());
        }


        Deposito saved = repository.save( objeto );

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(uri).body( saved );
    }

}
