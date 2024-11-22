package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ICListenStreamingConfig {
    private UUID id;
    private boolean recordWaveform;
    private boolean processWaveform;
    private int waveformProcessingType;
    private int waveformInterval;
    private int waveformDuration;
    private boolean recordFFT;
    private boolean processFFT;
    private int fftProcessingType;
    private int fftInterval;
    private int fftDuration;
    private LocalDateTime timestamp;

    public ICListenStreamingConfig(UUID id, boolean recordWaveform, boolean processWaveform, int waveformProcessingType, int waveformInterval, int waveformDuration,
                                   boolean recordFFT, boolean processFFT, int fftProcessingType, int fftInterval, int fftDuration, LocalDateTime timestamp) {
        this.id = id;
        this.recordWaveform = recordWaveform;
        this.processWaveform = processWaveform;
        this.waveformProcessingType = waveformProcessingType;
        this.waveformInterval = waveformInterval;
        this.waveformDuration = waveformDuration;
        this.recordFFT = recordFFT;
        this.processFFT = processFFT;
        this.fftProcessingType = fftProcessingType;
        this.fftInterval = fftInterval;
        this.fftDuration = fftDuration;
        this.timestamp = timestamp;
    }

    public static ICListenStreamingConfig createDefault() {
        return new ICListenStreamingConfig(UUID.randomUUID(), false, false, 0, 0, 0, false, false, 0, 0, 0, LocalDateTime.now());
    }

    // Métodos Getters y Setters
}
