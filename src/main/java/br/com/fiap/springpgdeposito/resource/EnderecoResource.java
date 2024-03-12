package br.com.fiap.springpgdeposito.resource;

import br.com.fiap.springpgdeposito.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return ResponseEntity.ok( saved );
    }
}
