package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules;


import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.*;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ICListenHFTest {

    @Test
    public void testSerializationDeserialization_AllModules() {
        ICListenStatus status = ICListenStatus.createDefault();
        ICListenLoggingConfig loggingConfig = ICListenLoggingConfig.createDefault();
        ICListenStreamingConfig streamingConfig = ICListenStreamingConfig.createDefault();
        ICListenRecordingStats recordingStats = ICListenRecordingStats.createDefault();

        ICListenHF original = new ICListenHF(status, loggingConfig, streamingConfig, recordingStats);
        ByteBuffer buffer = ByteBuffer.wrap(original.getVALUE());

        ICListenHF deserialized = ICListenHF.fromBytes(buffer);

        assertNotNull(deserialized);
        assertNotNull(deserialized.getStatus());
        assertNotNull(deserialized.getLoggingConfig());
        assertNotNull(deserialized.getStreamingConfig());
        assertNotNull(deserialized.getRecordingStats());
    }

    @Test
    public void testSerializationDeserialization_OnlyStatus() {
        ICListenStatus status = ICListenStatus.createDefault();

        ICListenHF original = new ICListenHF(status, null, null, null);
        ByteBuffer buffer = ByteBuffer.wrap(original.getVALUE());

        ICListenHF deserialized = ICListenHF.fromBytes(buffer);

        assertNotNull(deserialized);
        assertNotNull(deserialized.getStatus());
        assertNull(deserialized.getLoggingConfig());
        assertNull(deserialized.getStreamingConfig());
        assertNull(deserialized.getRecordingStats());
    }

    @Test
    public void testSerializationDeserialization_OnlyLoggingAndRecording() {
        ICListenLoggingConfig loggingConfig = ICListenLoggingConfig.createDefault();
        ICListenRecordingStats recordingStats = ICListenRecordingStats.createDefault();

        ICListenHF original = new ICListenHF(null, loggingConfig, null, recordingStats);
        ByteBuffer buffer = ByteBuffer.wrap(original.getVALUE());

        ICListenHF deserialized = ICListenHF.fromBytes(buffer);

        assertNotNull(deserialized);
        assertNull(deserialized.getStatus());
        assertNotNull(deserialized.getLoggingConfig());
        assertNull(deserialized.getStreamingConfig());
        assertNotNull(deserialized.getRecordingStats());
    }

    @Test
    public void testSerializationDeserialization_NoModules() {
        ICListenHF original = new ICListenHF(null, null, null, null);
        ByteBuffer buffer = ByteBuffer.wrap(original.getVALUE());

        ICListenHF deserialized = ICListenHF.fromBytes(buffer);

        assertNotNull(deserialized);
        assertNull(deserialized.getStatus());
        assertNull(deserialized.getLoggingConfig());
        assertNull(deserialized.getStreamingConfig());
        assertNull(deserialized.getRecordingStats());
    }

    @Test
    public void testSerializationIntegrity() {
        ICListenStatus status = new ICListenStatus(UUID.randomUUID(), 1, 1, 55.0f, 25.0f, 60.0f, LocalDateTime.now());
        ICListenHF original = new ICListenHF(status, null, null, null);
        ByteBuffer buffer = ByteBuffer.wrap(original.getVALUE());

        ICListenHF deserialized = ICListenHF.fromBytes(buffer);

        assertEquals(status.getUnitStatus(), deserialized.getStatus().getUnitStatus());
        assertEquals(status.getBatteryStatus(), deserialized.getStatus().getBatteryStatus());
        assertEquals((int) status.getBatteryPercentage(), (int) deserialized.getStatus().getBatteryPercentage());
    }
}

