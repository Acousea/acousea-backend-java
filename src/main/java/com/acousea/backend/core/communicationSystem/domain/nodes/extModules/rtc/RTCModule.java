package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
public class RTCModule extends ExtModule {
    public static final String name = "rtc";
    private LocalDateTime currentTime;

    public RTCModule(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    // Método para crear una instancia con la hora actual
    public static RTCModule createDefault() {
        return new RTCModule(LocalDateTime.now());
    }

    // Método para obtener la hora actual

    public static RTCModule fromBytes(ByteBuffer bytes) {
        if (bytes.remaining() < Long.BYTES) {
            throw new IllegalArgumentException("Invalid byte array for RTCModule");
        }
        long epochSecond = bytes.getLong();
        return new RTCModule(LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC));
    }

    @Override
    public int getFullSize() {
        return getMinSize();
    }

    public static int getMinSize() {
        return Long.BYTES;
    }
}

