
package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ICListenLoggingConfig {
    private UUID id;
    private int gain;
    private int waveformSampleRate;
    private int waveformLoggingMode;
    private int waveformLogLength;
    private int bitDepth;
    private int fftSampleRate;
    private int fftProcessingType;
    private int fftsAccumulated;
    private int fftLoggingMode;
    private int fftLogLength;
    private LocalDateTime timestamp;

    public ICListenLoggingConfig(UUID id, int gain, int waveformSampleRate, int waveformLoggingMode, int waveformLogLength, int bitDepth,
                                 int fftSampleRate, int fftProcessingType, int fftsAccumulated, int fftLoggingMode, int fftLogLength, LocalDateTime timestamp) {
        this.id = id;
        this.gain = gain;
        this.waveformSampleRate = waveformSampleRate;
        this.waveformLoggingMode = waveformLoggingMode;
        this.waveformLogLength = waveformLogLength;
        this.bitDepth = bitDepth;
        this.fftSampleRate = fftSampleRate;
        this.fftProcessingType = fftProcessingType;
        this.fftsAccumulated = fftsAccumulated;
        this.fftLoggingMode = fftLoggingMode;
        this.fftLogLength = fftLogLength;
        this.timestamp = timestamp;
    }

    public static ICListenLoggingConfig createDefault() {
        return new ICListenLoggingConfig(UUID.randomUUID(), 0, 1000, 0, 0, 0, 0, 0, 0, 0, 0, LocalDateTime.now());
    }

    // Métodos Getters y Setters
}
