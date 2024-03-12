package br.com.fiap.springpgdeposito.resource;

import br.com.fiap.springpgdeposito.entity.Endereco;
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
@RequestMapping(value = "/endereco")
public class EnderecoResource {
    @Autowired
    private EnderecoRepository repository;

    @GetMapping
    public  ResponseEntity<List<Endereco>> findAll() {
        return ResponseEntity.ok( repository.findAll() );
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Endereco> findById(@PathVariable(name = "id") Long id) {
        Endereco objeto = repository.findById( id ).orElse( null );
        if (Objects.isNull( objeto )) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok( objeto );
    }


    @PostMapping
    @Transactional
    public ResponseEntity<Endereco> persist(@RequestBody Endereco objeto) {
        Endereco saved = repository.save( objeto );

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(uri).body( saved );
    }
}
