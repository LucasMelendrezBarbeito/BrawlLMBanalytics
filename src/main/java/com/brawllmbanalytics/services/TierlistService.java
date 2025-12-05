package com.brawllmbanalytics.services;

import com.brawllmbanalytics.entities.*;
import com.brawllmbanalytics.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@Service
public class TierlistService {

    @Autowired
    private TierlistRepository tierlistRepository;

    @Autowired
    private TierlistItemRepository tierlistItemRepository;

    @Autowired
    private ResenaTierlistRepository resenaTierlistRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BrawlerRepository brawlerRepository;

    public Tierlist crearTierlist(Integer usuarioId, String nombre) {
        Usuario u = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Tierlist t = new Tierlist();
        t.setUsuario(u);
        t.setNombre(nombre);

        return tierlistRepository.save(t);
    }

    public TierlistItem agregarItem(Integer tierlistId, Integer brawlerId, String tier) {

        Tierlist t = tierlistRepository.findById(tierlistId)
                .orElseThrow(() -> new RuntimeException("Tierlist no encontrada"));

        Brawler b = brawlerRepository.findById(brawlerId)
                .orElseThrow(() -> new RuntimeException("Brawler no encontrado"));

        TierlistItem item = new TierlistItem();
        item.setTierlist(t);
        item.setBrawler(b);
        item.setTier(tier);

        return tierlistItemRepository.save(item);
    }

    public ResenaTierlist agregarReview(Integer tierlistId, Integer usuarioId, String comentario, Integer puntuacion) {

        Tierlist t = tierlistRepository.findById(tierlistId)
                .orElseThrow(() -> new RuntimeException("Tierlist no encontrada"));

        Usuario u = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ResenaTierlist review = new ResenaTierlist();
        review.setComentario(comentario);
        review.setPuntuacion(puntuacion); 
        review.setUsuario(u);
        review.setTierlist(t);

        return resenaTierlistRepository.save(review);
    }

    public Tierlist getTierlist(Integer id) {

        Tierlist t = tierlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tierlist no encontrada"));

        
        t.getItems().forEach(it -> {
            Brawler b = it.getBrawler();
            if (b != null) {
                b.setIconUrl("https://cdn.brawlify.com/brawlers/borderless/" + b.getId() + ".png");
            }
        });

        return t;
    }

    public List<Tierlist> getTodas() {
        return tierlistRepository.findAll();
    }

    public void borrarTierlist(Integer id, Usuario usuarioActual) {

        Tierlist tierlist = tierlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tierlist no encontrada"));

        boolean esCreador = tierlist.getUsuario().getId().equals(usuarioActual.getId());
        boolean esAdmin = usuarioActual.getRol().equalsIgnoreCase("ADMIN");

        if (!esCreador && !esAdmin) {
            throw new AccessDeniedException("No tienes permiso para borrar esta tierlist");
        }

        tierlistRepository.delete(tierlist);
    }
}
