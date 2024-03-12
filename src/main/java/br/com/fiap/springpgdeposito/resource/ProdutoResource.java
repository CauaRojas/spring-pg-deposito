package br.com.fiap.springpgdeposito.resource;

import br.com.fiap.springpgdeposito.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/produto")
public class ProdutoResource {
    @Autowired
    private ProdutoRepository repository;

    @GetMapping
    public  ResponseEntity<List<Produto>> findAll() {
        return ResponseEntity.ok( repository.findAll() );
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Produto> findById(@PathVariable(name = "id") Long id) {
        Produto objeto = repository.findById( id ).orElse( null );
        if (Objects.isNull( objeto )) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok( objeto );
    }


    @PostMapping
    @Transactional
    public ResponseEntity<Produto> persist(@RequestBody Produto objeto) {
        Produto saved = repository.save( objeto );
        return ResponseEntity.ok( saved );
    }
}
