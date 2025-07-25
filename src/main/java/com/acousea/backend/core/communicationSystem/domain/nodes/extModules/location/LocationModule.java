package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location;

import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.ModuleCode;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Getter
@Setter
public class LocationModule extends SerializableModule  {
    public static final String name = "location";
    private float latitude;
    private float longitude;

    public LocationModule(float latitude, float longitude) {
        super(ModuleCode.LOCATION);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public byte[] getVALUE() {
        return  ByteBuffer.allocate(getMinSize())
                .putFloat(latitude)
                .putFloat(longitude)
                .array();
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LocationModule that = (LocationModule) obj;
        return Float.compare(that.latitude, latitude) == 0 && Float.compare(that.longitude, longitude) == 0;
    }
}
