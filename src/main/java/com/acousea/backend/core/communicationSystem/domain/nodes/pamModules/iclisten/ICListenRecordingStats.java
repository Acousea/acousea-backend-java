package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.time.LocalDateTime;

@Getter
@Setter
public class ICListenRecordingStats {
    private UUID id;
    private LocalDateTime epochTime;
    private int numberOfClicks;
    private int recordedMinutes;
    private int numberOfFiles;

    public ICListenRecordingStats(UUID id, LocalDateTime epochTime, int numberOfClicks, int recordedMinutes, int numberOfFiles) {
        this.id = id;
        this.epochTime = epochTime;
        this.numberOfClicks = numberOfClicks;
        this.recordedMinutes = recordedMinutes;
        this.numberOfFiles = numberOfFiles;
    }

    public static ICListenRecordingStats createDefault() {
        return new ICListenRecordingStats(UUID.randomUUID(), LocalDateTime.now(), 0, 0, 0);
    }
    // Métodos Getters y Setters
}

