package com.donzelitos.multtenancy.controller;


import com.donzelitos.multtenancy.configuration.RequestContext;
import com.donzelitos.multtenancy.model.FrotaPlaca;
import com.donzelitos.multtenancy.model.Instance;
import com.donzelitos.multtenancy.repository.FrotaPlacaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/frota-placa")
@RequiredArgsConstructor
public class FrotaPlacaController {

    private final FrotaPlacaRepository frotaPlacaRepository;


    @PostMapping("/{instance}/{owner}")
    public ResponseEntity<FrotaPlaca> create(@PathVariable String instance, @PathVariable String owner,  @RequestBody FrotaPlaca frotaPlaca) {
        RequestContext.setCurrentInstance(new Instance(instance, "1234"));
        RequestContext.setTestOwnerString(owner);
        return ResponseEntity.ok(
                frotaPlacaRepository.save(frotaPlaca)
        );
    }

}
