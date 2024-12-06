package com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation;

import com.acousea.backend.core.communicationSystem.domain.communication.tags.Tag;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagType;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;

import java.nio.ByteBuffer;

public class LocationTag extends Tag {
    public LocationTag(byte[] value) {
        super(TagType.LOCATION, value);
    }

    public static LocationTag fromLocationModule(LocationModule module) {
        ByteBuffer buffer = ByteBuffer.allocate(LocationModule.getMinSize());
        buffer.putFloat(module.getLatitude());
        buffer.putFloat(module.getLongitude());
        return new LocationTag(buffer.array());
    }

    public LocationModule toLocationModule() {
        ByteBuffer buffer = ByteBuffer.wrap(this.VALUE);
        return new LocationModule(buffer.getFloat(), buffer.getFloat());
    }

    public static LocationTag fromBytes(ByteBuffer value) {
        return fromLocationModule(
                LocationModule.fromBytes(value)
        );
    }
}
