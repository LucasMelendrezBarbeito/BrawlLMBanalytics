package com.brawllmbanalytics.services;

import com.brawllmbanalytics.entities.CuentaBrawl;
import com.brawllmbanalytics.entities.Usuario;
import com.brawllmbanalytics.repositories.CuentaBrawlRepository;
import com.brawllmbanalytics.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaBrawlService {

    @Autowired
    private CuentaBrawlRepository cuentaRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    public CuentaBrawl vincularCuenta(Integer usuarioId,
                                      String tag,
                                      String nombre,
                                      Integer trofeos,
                                      Integer nivel) {

        
        cuentaRepo.findByTag(tag).ifPresent(c -> {
            throw new RuntimeException("Esta cuenta ya está vinculada.");
        });

        
        Usuario u = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        
        CuentaBrawl c = new CuentaBrawl();
        c.setUsuario(u);
        c.setTag(tag);
        c.setNombre(nombre);
        c.setTrofeos(trofeos);
        c.setNivel(nivel);

        return cuentaRepo.save(c);
    }

    public void eliminarCuenta(Integer cuentaId, Integer usuarioId) {
        CuentaBrawl cuenta = cuentaRepo.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (!cuenta.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para eliminar esta cuenta");
        }

        cuentaRepo.deleteById(cuentaId);
    }
}
