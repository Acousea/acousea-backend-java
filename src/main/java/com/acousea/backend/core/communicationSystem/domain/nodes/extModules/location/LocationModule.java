package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location;

import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

@Setter
@Getter
public class LocationModule extends ExtModule {
    private final double latitude;
    private final double longitude;

    public LocationModule(double latitude, double longitude) {
        super("location");
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationModule create() {
        return new LocationModule(0, 0);
    }

    public static LocationModule fromBytes(ByteBuffer value) {
        if (value.remaining() < Double.BYTES * 2) {
            throw new IllegalArgumentException("Invalid byte array for LocationModule");
        }
        double latitude = value.getDouble();
        double longitude = value.getDouble();
        return new LocationModule(latitude, longitude);
    }

    @Override
    public int getFullSize() {
        return getMinSize();
    }

    public static int getMinSize() {
        return Double.BYTES * 2;
    }
}