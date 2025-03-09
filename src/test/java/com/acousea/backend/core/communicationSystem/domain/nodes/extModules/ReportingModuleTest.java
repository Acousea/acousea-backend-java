package com.acousea.backend.core.communicationSystem.domain.nodes.extModules;

import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.ReportingModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class ReportingModuleTest {

    @Test
    void testSerialization_LoRaReportingModule() {
        // Given: A LoRa reporting module with predefined operation modes
        Map<Short, OperationMode> modes = new HashMap<>();
        modes.put((short) 1, OperationMode.create((short) 1, "Mode1"));
        modes.put((short) 2, OperationMode.create((short) 2, "Mode2"));

        OperationModesModule operationModesModule = new OperationModesModule(modes);
        LoRaReportingModule reportingModule = new LoRaReportingModule(operationModesModule);
        reportingModule.setReportingPeriod(OperationMode.create((short) 1, "Mode1"), 120);
        reportingModule.setReportingPeriod(OperationMode.create((short) 2, "Mode2"), 300);

        // When: We serialize the LoRaReportingModule
        byte[] serializedBytes = reportingModule.toBytes();

        // Then: The length should match expectations (1 byte for TYPE, 1 byte for length, 1 byte tech ID, 2 bytes per mode)
        Assertions.assertEquals(serializedBytes.length, 8);

        // Checking TYPE and length byte
        Assertions.assertEquals(serializedBytes[0], (byte) ModuleCode.REPORTING.getValue());
        Assertions.assertEquals(serializedBytes[1], (byte) 5); // 1 byte tech ID + 2x (1 byte mode ID + 2 bytes period)

        // Checking serialized values
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, 6);
        byte techId = buffer.get();
        byte mode1 = buffer.get();
        short period1 = buffer.getShort();
        byte mode2 = buffer.get();
        short period2 = buffer.getShort();

        Assertions.assertEquals(LoRaReportingModule.TECHNOLOGY_ID, techId);
        Assertions.assertEquals(1, mode1);
        Assertions.assertEquals(120, period1);
        Assertions.assertEquals(2, mode2);
        Assertions.assertEquals(300, period2);
    }

    @Test
    void testSerialization_IridiumReportingModule() {
        // Given: An Iridium reporting module with predefined operation modes
        Map<Short, OperationMode> modes = new HashMap<>();
        modes.put((short) 3, OperationMode.create((short) 3, "Mode3"));
        OperationModesModule operationModesModule = new OperationModesModule(modes);
        IridiumReportingModule reportingModule = new IridiumReportingModule(operationModesModule, "123456789012345");
        reportingModule.setReportingPeriod(OperationMode.create((short) 3, "Mode3"), 600);

        // When: We serialize the IridiumReportingModule
        byte[] serializedBytes = reportingModule.toBytes();

        // Then: The length should match expectations (1 byte for TYPE, 1 byte for length, 1 byte tech ID, 2 bytes per mode)
        Assertions.assertEquals(6, serializedBytes.length);

        // Checking TYPE and length byte
        Assertions.assertEquals((byte) ModuleCode.REPORTING.getValue(), serializedBytes[0]);
        Assertions.assertEquals((byte) 4, serializedBytes[1]); // 1 byte tech ID + 1 byte mode ID + 2 bytes period

        // Checking serialized values
        ByteBuffer buffer = ByteBuffer.wrap(serializedBytes, 2, 4);
        byte techId = buffer.get();
        byte mode3 = buffer.get();
        short period3 = buffer.getShort();

        Assertions.assertEquals(IridiumReportingModule.TECHNOLOGY_ID, techId);
        Assertions.assertEquals(3, mode3);
        Assertions.assertEquals(600, period3);
    }

    @Test
    void testDeserialization_LoRaReportingModule() {
        // Given: Serialized LoRa reporting module data
        ByteBuffer buffer = ByteBuffer.allocate(6)
                .put(LoRaReportingModule.TECHNOLOGY_ID) // Tech ID
                .put((byte) 1).putShort((short) 120) // Mode 1 -> 120s
                .put((byte) 2).putShort((short) 300); // Mode 2 -> 300s
        buffer.flip(); // Prepare buffer for reading

        // When: We deserialize the bytes
        ReportingModule reportingModule = ReportingModule.fromBytes(buffer);

        // Then: The reconstructed LoRaReportingModule should match the original
        Assertions.assertInstanceOf(LoRaReportingModule.class, reportingModule);
        Assertions.assertEquals(120, reportingModule.getReportingPeriod((byte) 1));
        Assertions.assertEquals(300, reportingModule.getReportingPeriod((byte) 2));
    }

    @Test
    void testDeserialization_IridiumReportingModule() {
        // Given: Serialized Iridium reporting module data
        ByteBuffer buffer = ByteBuffer.allocate(4)
                .put(IridiumReportingModule.TECHNOLOGY_ID) // Tech ID
                .put((byte) 3).putShort((short) 600); // Mode 3 -> 600s
        buffer.flip(); // Prepare buffer for reading

        // When: We deserialize the bytes
        ReportingModule reportingModule = ReportingModule.fromBytes(buffer);

        // Then: The reconstructed IridiumReportingModule should match the original
        Assertions.assertInstanceOf(IridiumReportingModule.class, reportingModule);
        Assertions.assertEquals(600, reportingModule.getReportingPeriod((byte) 3));
    }

    @Test
    void testDeserializationWithInvalidData() {
        // Given: A buffer that is too short
        ByteBuffer buffer = ByteBuffer.allocate(1); // Not enough bytes

        // Expect: An exception due to insufficient data
        Assertions.assertThrows(IllegalArgumentException.class, () -> ReportingModule.fromBytes(buffer));
    }

    @Test
    void testSetAndGetReportingPeriod() {
        // Given: A reporting module
        LoRaReportingModule reportingModule = LoRaReportingModule.createEmpty();
        OperationMode mode = OperationMode.create((short) 5, "TestMode");

        // When: Setting a reporting period
        reportingModule.setReportingPeriod(mode, 180);

        // Then: The period should be retrievable
        Assertions.assertEquals(180, reportingModule.getReportingPeriod((byte) 5));
    }

    @Test
    void testGetReportingPeriodForNonExistingMode() {
        // Given: A reporting module with no modes
        LoRaReportingModule reportingModule = LoRaReportingModule.createEmpty();

        // Expect: An exception when trying to retrieve a period for a non-existing mode
        Assertions.assertThrows(IllegalArgumentException.class, () -> reportingModule.getReportingPeriod((byte) 99));
    }
}
