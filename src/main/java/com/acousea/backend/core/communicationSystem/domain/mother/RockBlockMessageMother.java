package com.acousea.backend.core.communicationSystem.domain.mother;


import com.acousea.backend.core.communicationSystem.domain.RockBlockMessage;
import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationPacket;

import java.nio.ByteBuffer;
import java.util.HexFormat;
import java.util.UUID;

public class RockBlockMessageMother {

    public static RockBlockMessage fromCustomValues(String imei, String serial, int momsn, String transmitTime,
                                                    double latitude, double longitude, double cep, String data) {
        return new RockBlockMessage(imei, serial, momsn, transmitTime, latitude, longitude, cep, data);
    }

    public static RockBlockMessage fromPacket(CommunicationPacket packet){
        return fromData(ByteBuffer.wrap(packet.toBytes()));
    }

    public static RockBlockMessage fromData(ByteBuffer responseData){
        return new RockBlockMessage(
                "300234063123450", // Sample IMEI
                UUID.randomUUID().toString(), // Unique serial
                123, // Sample MOMSN
                "2024-03-07T12:30:00Z", // ISO-8601 timestamp
                37.7749, // Sample latitude
                -122.4194, // Sample longitude
                1.5, // Sample CEP
                HexFormat.of().formatHex(responseData.array())
        );
    }

    public static RockBlockMessage withInvalidData() {
        return new RockBlockMessage(
                "300234063123450", // Sample IMEI
                UUID.randomUUID().toString(), // Unique serial
                123, // Sample MOMSN
                "2024-03-07T12:30:00Z", // ISO-8601 timestamp
                37.7749, // Sample latitude
                -122.4194, // Sample longitude
                1.5, // Sample CEP
                "SGVsbG8gd29ybGQ=" // Base64-encoded "Hello world" message
        );
    }
}

