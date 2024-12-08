package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location;

import com.acousea.backend.core.communicationSystem.domain.communication.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.communication.serialization.ModuleCode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Getter
@Setter
public class LocationModule extends SerializableModule implements ExtModule {
    public static final String name = "location";
    private float latitude;
    private float longitude;

    public LocationModule(float latitude, float longitude) {
        super(ModuleCode.LOCATION, ByteBuffer.allocate(getMinSize())
                .putFloat(latitude)
                .putFloat(longitude)
                .array());
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int getFullSize() {
        return getMinSize();
    }

    public static int getMinSize() {
        return Float.BYTES * 2; // 4 bytes for latitude + 4 bytes for longitude
    }

    public static LocationModule create() {
        return new LocationModule(0, 0);
    }

    public static LocationModule fromBytes(ByteBuffer buffer) {
        if (buffer.remaining() < getMinSize()) {
            throw new IllegalArgumentException("Invalid byte array for LocationModule");
        }
        float latitude = buffer.getFloat();
        float longitude = buffer.getFloat();
        return new LocationModule(latitude, longitude);
    }

    @Override
    public String toString() {
        return "LocationModule{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
