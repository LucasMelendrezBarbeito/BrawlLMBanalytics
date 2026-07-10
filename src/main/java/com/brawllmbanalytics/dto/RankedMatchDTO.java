package com.brawllmbanalytics.dto;

import java.time.LocalDateTime;

public record RankedMatchDTO(
        LocalDateTime battleTime,
        String mode,
        Integer rank,
        Integer trophyChange,
        String brawlerName,
        Integer brawlerId,
        String result
) {}
