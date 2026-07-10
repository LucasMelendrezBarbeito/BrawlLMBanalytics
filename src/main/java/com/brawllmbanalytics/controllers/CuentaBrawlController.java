package com.brawllmbanalytics.controllers;

import com.brawllmbanalytics.dto.VincularCuentaRequest;
import com.brawllmbanalytics.entities.CuentaBrawl;
import com.brawllmbanalytics.repositories.CuentaBrawlRepository;
import com.brawllmbanalytics.services.CuentaBrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@CrossOrigin
public class CuentaBrawlController {

    @Autowired
    private CuentaBrawlService cuentaBrawlService;

    @Autowired
    private CuentaBrawlRepository cuentaBrawlRepository;

    @PostMapping("/vincular")
    public CuentaBrawl vincular(@RequestBody VincularCuentaRequest req) {
        return cuentaBrawlService.vincularCuenta(
                req.usuarioId(),
                req.tag(),
                req.nombre(),
                req.trofeos(),
                req.nivel()
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<CuentaBrawl> obtenerPorUsuario(@PathVariable Integer usuarioId) {
        return cuentaBrawlRepository.findByUsuarioId(usuarioId);
    }
}
