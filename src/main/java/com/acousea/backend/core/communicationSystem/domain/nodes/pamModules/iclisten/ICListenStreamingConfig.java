package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten;

import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ICListenStreamingConfig extends SerializableModule {
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
        super(ModuleCode.ICLISTEN_STREAMING_CONFIG);
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

    @Override
    public byte[] getVALUE() {
        ByteBuffer buffer = ByteBuffer.allocate(16 + 1 + 1 + 4 * 7 + 8);
        buffer.put((byte) (recordWaveform ? 1 : 0));
        buffer.put((byte) (processWaveform ? 1 : 0));
        buffer.putInt(waveformProcessingType);
        buffer.putInt(waveformInterval);
        buffer.putInt(waveformDuration);
        buffer.put((byte) (recordFFT ? 1 : 0));
        buffer.put((byte) (processFFT ? 1 : 0));
        buffer.putInt(fftProcessingType);
        buffer.putInt(fftInterval);
        buffer.putInt(fftDuration);
        buffer.putLong(timestamp.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
        buffer.flip();
        return buffer.array();
    }

    public static ICListenStreamingConfig fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < 48) {
            throw new IllegalArgumentException("Invalid data size for ICListenStreamingConfig");
        }

        UUID id = UUID.randomUUID();
        boolean recordWaveform = buffer.get() == 1;
        boolean processWaveform = buffer.get() == 1;
        int waveformProcessingType = buffer.getInt();
        int waveformInterval = buffer.getInt();
        int waveformDuration = buffer.getInt();
        boolean recordFFT = buffer.get() == 1;
        boolean processFFT = buffer.get() == 1;
        int fftProcessingType = buffer.getInt();
        int fftInterval = buffer.getInt();
        int fftDuration = buffer.getInt();
        long timestampMillis = buffer.getLong();
        LocalDateTime timestamp = LocalDateTime.ofEpochSecond(timestampMillis / 1000, 0, java.time.ZoneOffset.UTC);

        return new ICListenStreamingConfig(id, recordWaveform, processWaveform, waveformProcessingType, waveformInterval, waveformDuration, recordFFT, processFFT, fftProcessingType, fftInterval, fftDuration, timestamp);
    }

    public static ICListenStreamingConfig createDefault() {
        return new ICListenStreamingConfig(UUID.randomUUID(), false, false, 0, 0, 0, false, false, 0, 0, 0, LocalDateTime.now());
    }
}
