
package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten;


import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.PamModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
public class ICListenLoggingConfig extends SerializableModule {
    public static final String name = "ICListenLoggingConfig";
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

    public ICListenLoggingConfig(UUID id, int gain, int waveformSampleRate, int waveformLoggingMode, int waveformLogLength, int bitDepth, int fftSampleRate, int fftProcessingType, int fftsAccumulated, int fftLoggingMode, int fftLogLength, LocalDateTime timestamp) {
        super(ModuleCode.ICLISTEN_LOGGING_CONFIG);
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

    @Override
    public byte[] getVALUE() {
        ByteBuffer buffer = ByteBuffer.allocate(16 + 4 * 10 + 8);
        buffer.putShort((short) gain);
        buffer.putInt(waveformSampleRate);
        buffer.put((byte) waveformLoggingMode);
        buffer.put((byte) waveformLogLength);
        buffer.put((byte) bitDepth);
        buffer.putInt(fftSampleRate);
        buffer.putShort((short) fftProcessingType);
        buffer.putShort((short) fftsAccumulated);
        buffer.put((byte) fftLoggingMode);
        buffer.put((byte) fftLogLength);
        buffer.flip();
        return buffer.array();
    }

    public static ICListenLoggingConfig fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < 56) {
            throw new IllegalArgumentException("Invalid data size for ICListenLoggingConfig");
        }

        UUID id = UUID.randomUUID();
        int gain = buffer.getShort();
        int waveformSampleRate = buffer.getInt();
        int waveformLoggingMode = buffer.get() & 0xFF;
        int waveformLogLength = buffer.get() & 0xFF;
        int bitDepth = buffer.get() & 0xFF;
        int fftSampleRate = buffer.getInt();
        int fftProcessingType = buffer.getShort();
        int fftsAccumulated = buffer.getShort();
        int fftLoggingMode = buffer.get() & 0xFF;
        int fftLogLength = buffer.get() & 0xFF;
        LocalDateTime timestamp = LocalDateTime.now();

        return new ICListenLoggingConfig(id, gain, waveformSampleRate, waveformLoggingMode, waveformLogLength, bitDepth, fftSampleRate, fftProcessingType, fftsAccumulated, fftLoggingMode, fftLogLength, timestamp);
    }

    public static ICListenLoggingConfig createDefault() {
        return new ICListenLoggingConfig(UUID.randomUUID(), 0, 1000, 0, 0, 0, 1000, 0, 0, 0, 0, LocalDateTime.now());
    }
}
