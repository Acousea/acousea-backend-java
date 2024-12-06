package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Setter
@Getter
public class LocationModule extends ExtModule {
    public static final String name = "location";
    private final float latitude;
    private final float longitude;

    public LocationModule(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationModule create() {
        return new LocationModule(0, 0);
    }

    public static LocationModule fromBytes(ByteBuffer value) {
        if (value.remaining() < Float.BYTES * 2) {
            throw new IllegalArgumentException("Invalid byte array for LocationModule");
        }
        float latitude = value.getFloat();
        float longitude = value.getFloat();
        return new LocationModule(latitude, longitude);
    }

    @Override
    public int getFullSize() {
        return getMinSize();
    }

    public static int getMinSize() {
        return Float.BYTES * 2;
    }
}