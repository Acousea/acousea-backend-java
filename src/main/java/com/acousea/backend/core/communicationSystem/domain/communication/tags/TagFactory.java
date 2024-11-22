package com.acousea.backend.core.communicationSystem.domain.communication.tags;

import com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation.*;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class TagFactory {
    // Mapa que asocia cada TagType con su constructor de tag
    private static final Map<TagType, TagCreator> tagCreators = new HashMap<>();

    static {
        tagCreators.put(TagType.BATTERY, BatteryTag::fromBytes);
        tagCreators.put(TagType.LOCATION, LocationTag::fromBytes);
        tagCreators.put(TagType.NETWORK, NetworkTag::fromBytes);
        tagCreators.put(TagType.OPERATION_MODES, OperationModeTag::fromBytes);
        tagCreators.put(TagType.REPORTING_PERIODS, ReportingPeriodTag::fromBytes);
        tagCreators.put(TagType.RTC, RTCTag::fromBytes);
        tagCreators.put(TagType.STORAGE, StorageTag::fromBytes);
        tagCreators.put(TagType.TEMPERATURE, AmbientTag::fromBytes);
    }

    /**
     * Método principal para decodificar un `Tag` a partir de un `ByteBuffer`.
     *
     * @param buffer ByteBuffer que contiene los datos del tag.
     * @return La instancia de `Tag` correspondiente.
     * @throws IllegalArgumentException si el tipo de tag no está registrado.
     */
    @SneakyThrows
    public static Tag createTag(ByteBuffer buffer)  {
        buffer.limit(buffer.capacity());

        byte typeCode = buffer.get();
        TagType tagType = TagType.fromValue(typeCode);

        int tagLength = buffer.get();
        if (tagLength < buffer.remaining()) {
            throw new IllegalArgumentException("Invalid byte array for " + tagType);
        }

        TagCreator tagCreator = tagCreators.get(tagType);
        if (tagCreator == null) {
            throw new IllegalArgumentException("Tag type not supported: " + tagType);
        }

        buffer.limit(buffer.position() + tagLength);
        return tagCreator.create(buffer);
    }

    /**
     * Interfaz funcional que representa un constructor de tag.
     */
    @FunctionalInterface
    private interface TagCreator {
        Tag create(ByteBuffer buffer);
    }
}
