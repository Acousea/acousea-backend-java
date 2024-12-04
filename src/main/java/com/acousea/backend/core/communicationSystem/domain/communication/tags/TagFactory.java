package com.acousea.backend.core.communicationSystem.domain.communication.tags;

import com.acousea.backend.core.communicationSystem.domain.communication.tags.implementation.*;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModeModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.ReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagFactory {
    /**
     * Interfaz funcional que representa un constructor de tag.
     */
    @FunctionalInterface
    private interface TagCreatorFromBytes {
        Tag create(ByteBuffer buffer);
    }

    @FunctionalInterface
    private interface TagCreatorFromModule<E extends ExtModule, T extends Tag> {
        T create(E extModule);
    }


    // Mapa que asocia cada TagType con su constructor de tag
    private static final Map<TagType, TagCreatorFromBytes> tagCreatorsFromBytes = new HashMap<>() {{
        put(TagType.BATTERY, BatteryTag::fromBytes);
        put(TagType.LOCATION, LocationTag::fromBytes);
        put(TagType.NETWORK, NetworkTag::fromBytes);
        put(TagType.OPERATION_MODES, OperationModeTag::fromBytes);
        put(TagType.REPORTING, ReportingPeriodTag::fromBytes);
        put(TagType.RTC, RTCTag::fromBytes);
        put(TagType.STORAGE, StorageTag::fromBytes);
        put(TagType.AMBIENT, AmbientTag::fromBytes);
    }};

    private static final Map<TagType, TagCreatorFromModule<?, ?>> tagCreatorsFromModules = new HashMap<>() {{
        put(TagType.BATTERY, (TagCreatorFromModule<BatteryModule, BatteryTag>) BatteryTag::fromBatteryModule);
        put(TagType.LOCATION, (TagCreatorFromModule<LocationModule, LocationTag>) LocationTag::fromLocationModule);
        put(TagType.NETWORK, (TagCreatorFromModule<NetworkModule, NetworkTag>) NetworkTag::fromNetworkModule);
        put(TagType.OPERATION_MODES, (TagCreatorFromModule<OperationModeModule, OperationModeTag>) OperationModeTag::fromOperationModeModule);
        put(TagType.REPORTING, (TagCreatorFromModule<ReportingModule, ReportingPeriodTag>) ReportingPeriodTag::fromReportingModule);
        put(TagType.RTC, (TagCreatorFromModule<RTCModule, RTCTag>) RTCTag::fromRTCModule);
        put(TagType.STORAGE, (TagCreatorFromModule<StorageModule, StorageTag>) StorageTag::fromStorageModule);
        put(TagType.AMBIENT, (TagCreatorFromModule<AmbientModule, AmbientTag>) AmbientTag::fromAmbientModule);
    }};


    public static List<Tag> createTags(NodeDevice nodeDevice) {
        List<Tag> tags = new ArrayList<>();
        nodeDevice.getExtModules().forEach((moduleName, extModule) -> {
            TagType tagType = TagType.fromModuleName(moduleName);

            @SuppressWarnings("unchecked")
            TagCreatorFromModule<ExtModule, ? extends Tag> tagCreator = (TagCreatorFromModule<ExtModule, ? extends Tag>) tagCreatorsFromModules.get(tagType);

            if (tagCreator == null) {
                throw new IllegalArgumentException("Tag type not supported: " + tagType);
            }

            Tag tag = tagCreator.create(extModule);
            tags.add(tag);
        });


        return tags;
    }


    /**
     * Método principal para decodificar un `Tag` a partir de un `ByteBuffer`.
     *
     * @param buffer ByteBuffer que contiene los datos del tag.
     * @return La instancia de `Tag` correspondiente.
     * @throws IllegalArgumentException si el tipo de tag no está registrado.
     */
    @SneakyThrows
    public static Tag createTag(ByteBuffer buffer) {
        buffer.limit(buffer.capacity());

        byte typeCode = buffer.get();
        TagType tagType = TagType.fromValue(typeCode);

        int tagLength = buffer.get();
        if (tagLength < buffer.remaining()) {
            throw new IllegalArgumentException("Invalid byte array for " + tagType);
        }

        TagCreatorFromBytes tagCreatorFromBytes = tagCreatorsFromBytes.get(tagType);
        if (tagCreatorFromBytes == null) {
            throw new IllegalArgumentException("Tag type not supported: " + tagType);
        }

        buffer.limit(buffer.position() + tagLength);
        return tagCreatorFromBytes.create(buffer);
    }

    public static List<Tag> createTags(ByteBuffer buffer) {
        buffer.limit(buffer.capacity());
        List<Tag> tags = new ArrayList<>();
        while (buffer.hasRemaining()) {
            tags.add(createTag(buffer));
        }
        return tags;
    }


}
