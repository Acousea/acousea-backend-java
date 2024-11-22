package com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation;

import com.acousea.backend.core.communicationSystem.domain.communication.tags.Tag;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagType;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class RTCTag extends Tag {
    private RTCTag(byte[] value) {
        super(TagType.RTC, value);
    }

    public static RTCTag fromRTCModule(RTCModule module) {
        long epochSecond = module.getCurrentTime().toEpochSecond(ZoneOffset.UTC);
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(epochSecond);
        return new RTCTag(buffer.array());
    }

    public RTCModule toRTCModule() {
        ByteBuffer buffer = ByteBuffer.wrap(this.VALUE);
        return new RTCModule(LocalDateTime.ofEpochSecond(buffer.getLong(), 0, ZoneOffset.UTC));
    }


    public static RTCTag fromBytes(ByteBuffer buffer) {
        return fromRTCModule(
                RTCModule.fromBytes(buffer)
        );
    }
}
