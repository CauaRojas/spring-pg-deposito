package br.com.fiap.springpgdeposito.resource;

import br.com.fiap.springpgdeposito.repository.DepositoRepository;
import br.com.fiap.springpgdeposito.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;

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
            objeto.setAdvogado(endereco.get());
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
