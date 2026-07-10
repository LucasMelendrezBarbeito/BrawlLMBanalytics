package com.brawllmbanalytics.services;

import com.brawllmbanalytics.dto.RankedDetailDTO;
import com.brawllmbanalytics.dto.RankedMatchDTO;
import com.brawllmbanalytics.dto.RankedPlayerDTO;
import com.brawllmbanalytics.entities.CuentaBrawl;
import com.brawllmbanalytics.entities.RankedMatch;
import com.brawllmbanalytics.entities.RankedSeason;
import com.brawllmbanalytics.repositories.CuentaBrawlRepository;
import com.brawllmbanalytics.repositories.RankedMatchRepository;
import com.brawllmbanalytics.repositories.RankedSeasonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RankedService {

    private final String apiToken;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final CuentaBrawlRepository cuentaRepo;
    private final RankedSeasonRepository seasonRepo;
    private final RankedMatchRepository matchRepo;

    private static final String API_BASE = "https://api.brawlstars.com/v1";

    public RankedService(@Value("${supercell.token}") String apiToken,
                         CuentaBrawlRepository cuentaRepo,
                         RankedSeasonRepository seasonRepo,
                         RankedMatchRepository matchRepo) {
        this.apiToken = apiToken;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.cuentaRepo = cuentaRepo;
        this.seasonRepo = seasonRepo;
        this.matchRepo = matchRepo;
    }

    public List<RankedPlayerDTO> getGlobalRankings() {
        return getRankings("/rankings/global/players?limit=200");
    }

    public List<RankedPlayerDTO> getLocalRankings(String countryCode) {
        return getRankings("/rankings/" + countryCode + "/players?limit=200");
    }

    @SuppressWarnings("unchecked")
    private List<RankedPlayerDTO> getRankings(String path) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + path))
                    .header("Authorization", "Bearer " + apiToken)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error al obtener clasificación: " + response.statusCode());
            }

            Map<String, Object> data = objectMapper.readValue(response.body(), new TypeReference<>() {});
            List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");

            return items.stream().map(item -> {
                Map<String, Object> icon = (Map<String, Object>) item.get("icon");
                Map<String, Object> club = (Map<String, Object>) item.get("club");
                return new RankedPlayerDTO(
                        (String) item.get("tag"),
                        (String) item.get("name"),
                        icon != null ? (Integer) icon.get("id") : null,
                        (Integer) item.get("trophies"),
                        (Integer) item.get("rank"),
                        club != null ? (String) club.get("name") : null
                );
            }).collect(Collectors.toList());

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al contactar la API de Supercell: " + e.getMessage());
        }
    }

    public RankedDetailDTO getCuentaRankedDetail(Long cuentaId, Integer usuarioId) {
        CuentaBrawl cuenta = cuentaRepo.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (!cuenta.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para ver esta cuenta");
        }

        List<RankedSeason> temporadas = seasonRepo.findByCuentaBrawlIdOrderBySeasonNumberDesc(cuentaId);
        List<RankedMatch> partidas = matchRepo.findTop10ByCuentaBrawlIdOrderByBattleTimeDesc(cuentaId);

        String currentRank = temporadas.isEmpty() ? "Sin datos" : temporadas.get(0).getRank();
        Integer currentTrophies = temporadas.isEmpty() ? 0 : temporadas.get(0).getTrophies();

        List<RankedDetailDTO.RankedSeasonDTO> temporadasDTO = temporadas.stream()
                .limit(5)
                .map(s -> new RankedDetailDTO.RankedSeasonDTO(s.getSeasonNumber(), s.getRank(), s.getTrophies()))
                .collect(Collectors.toList());

        List<RankedMatchDTO> partidasDTO = partidas.stream()
                .map(m -> new RankedMatchDTO(m.getBattleTime(), m.getMode(), m.getRank(),
                        m.getTrophyChange(), m.getBrawlerName(), m.getBrawlerId(), m.getResult()))
                .collect(Collectors.toList());

        return new RankedDetailDTO(
                cuentaId,
                cuenta.getTag(),
                cuenta.getNombre(),
                new RankedDetailDTO.Ranked(currentRank, currentTrophies),
                temporadasDTO,
                partidasDTO
        );
    }

    @Transactional
    public void syncCuentaRanked(Long cuentaId, Integer usuarioId) {
        CuentaBrawl cuenta = cuentaRepo.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (!cuenta.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para sincronizar esta cuenta");
        }

        String tag = cuenta.getTag().replace("#", "");
        try {
            String encodedTag = java.net.URLEncoder.encode("#" + tag, java.nio.charset.StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/players/" + encodedTag + "/battlelog"))
                    .header("Authorization", "Bearer " + apiToken)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error al obtener battlelog: " + response.statusCode());
            }

            Map<String, Object> data = objectMapper.readValue(response.body(), new TypeReference<>() {});
            List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");

            int partidasGuardadas = 0;
            for (Map<String, Object> item : items) {
                Map<String, Object> battle = (Map<String, Object>) item.get("battle");
                String type = (String) battle.get("type");

                if (!"ranked".equals(type)) continue;

                String battleTime = (String) item.get("battleTime");
                Map<String, Object> event = (Map<String, Object>) item.get("event");
                String mode = (String) event.get("mode");

                Integer rank = (Integer) battle.get("rank");
                Integer trophyChange = (Integer) battle.get("trophyChange");

                List<List<Map<String, Object>>> teams = (List<List<Map<String, Object>>>) battle.get("teams");
                String brawlerName = "Unknown";
                Integer brawlerId = 0;

                if (teams != null) {
                    for (List<Map<String, Object>> team : teams) {
                        for (Map<String, Object> player : team) {
                            if (tag.equals(((String) player.get("tag")).replace("#", ""))) {
                                Map<String, Object> brawler = (Map<String, Object>) player.get("brawler");
                                brawlerName = (String) brawler.get("name");
                                brawlerId = (Integer) brawler.get("id");
                            }
                        }
                    }
                }

                String result = rank != null ? "rank_" + rank : (trophyChange != null && trophyChange > 0 ? "victory" : "defeat");

                RankedMatch match = new RankedMatch();
                match.setCuentaBrawl(cuenta);
                match.setBattleTime(LocalDateTime.parse(battleTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                match.setMode(mode);
                match.setRank(rank);
                match.setTrophyChange(trophyChange);
                match.setBrawlerName(brawlerName);
                match.setBrawlerId(brawlerId);
                match.setResult(result);

                matchRepo.save(match);
                partidasGuardadas++;

                if (partidasGuardadas >= 10) break;
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al sincronizar datos ranked: " + e.getMessage());
        }
    }
}
