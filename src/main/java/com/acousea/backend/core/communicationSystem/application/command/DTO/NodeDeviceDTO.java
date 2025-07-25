package com.acousea.backend.core.communicationSystem.application.command.DTO;

import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import com.acousea.backend.core.communicationSystem.domain.communication.constants.BatteryStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModeGraph.OperationModesGraphModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationMode;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.operationModes.OperationModesModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.ICListenHF;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenLoggingConfig;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenRecordingStats;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStreamingConfig;
import com.acousea.backend.core.communicationSystem.domain.nodes.serialization.SerializableModule;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Data
public class NodeDeviceDTO {
    private String id;
    private String name;
    private String icon;
    private Map<String, SerializableModuleDto> modules;

    public NodeDevice toNodeDevice() {
        return new NodeDevice(
                UUID.fromString(this.id),
                this.name,
                this.icon,
                (this.modules != null) ? SerializableModuleDto.toSerializableModules(this.modules) : new HashMap<>()
        );
    }

    public static List<NodeDeviceDTO> fromNodeDevices(List<NodeDevice> nodeDevicesWithIconUrl) {
        return nodeDevicesWithIconUrl.stream()
                .map(NodeDeviceDTO::fromNodeDevice)
                .toList();
    }

    public static NodeDeviceDTO fromNodeDevice(NodeDevice nodeDevice) {
        NodeDeviceDTO dto = new NodeDeviceDTO();
        dto.setId(nodeDevice.getId().toString());
        dto.setName(nodeDevice.getName());
        dto.setIcon(nodeDevice.getIcon());
        dto.setModules(SerializableModuleDto.fromSerializableModules(nodeDevice.getSerializableModulesMap()));
        return dto;
    }

    @SuppressWarnings("unchecked")
    public <T> T getExtModule(String moduleName, Class<T> type) {
        Object module = modules.get(moduleName);
        if (module == null) {
            throw new IllegalArgumentException("Module not found: " + moduleName);
        }
        if (!type.isInstance(module)) {
            throw new IllegalArgumentException("Module " + moduleName + " is not of type " + type.getName());
        }
        return (T) module;
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "name",
            visible = true
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SerializableModuleDto.BatteryModuleDto.class, name = BatteryModule.name),
            @JsonSubTypes.Type(value = SerializableModuleDto.AmbientModuleDTO.class, name = AmbientModule.name),
            @JsonSubTypes.Type(value = SerializableModuleDto.LocationModuleDto.class, name = LocationModule.name),
            @JsonSubTypes.Type(value = SerializableModuleDto.NetworkModuleDto.class, name = NetworkModule.name),
            @JsonSubTypes.Type(value = SerializableModuleDto.RtcModuleDto.class, name = RTCModule.name),
            @JsonSubTypes.Type(value = SerializableModuleDto.StorageModuleDto.class, name = StorageModule.name),
            @JsonSubTypes.Type(value = SerializableModuleDto.OperationModeModuleDto.class, name = OperationModesModule.name),
            @JsonSubTypes.Type(value = SerializableModuleDto.OperationModesGraphModuleDto.class, name = OperationModesGraphModule.name),
            @JsonSubTypes.Type(value = SerializableModuleDto.IridiumReportingModuleDto.class, name = IridiumReportingModule.name),
            @JsonSubTypes.Type(value = SerializableModuleDto.LoRaReportingModuleDto.class, name = LoRaReportingModule.name),
            @JsonSubTypes.Type(value = SerializableModuleDto.ICListenHFDto.class, name = ICListenHF.name)
    })
    @Data
    public static class SerializableModuleDto {
        public static Map<String, SerializableModule> toSerializableModules(Map<String, SerializableModuleDto> extModules) {
            Map<String, SerializableModule> serializableModulesMap = new HashMap<>();
            extModules.forEach((key, value) -> {
                if (value instanceof BatteryModuleDto) {
                    serializableModulesMap.put(key, ((BatteryModuleDto) value).toBatteryModule());
                } else if (value instanceof AmbientModuleDTO) {
                    serializableModulesMap.put(key, ((AmbientModuleDTO) value).toAmbientModule());
                } else if (value instanceof OperationModeModuleDto) {
                    serializableModulesMap.put(key, ((OperationModeModuleDto) value).toOperationModeModule());
                } else if (value instanceof OperationModesGraphModuleDto) {
                    serializableModulesMap.put(key, ((OperationModesGraphModuleDto) value).toOperationModesGraphModule());
                } else if (value instanceof IridiumReportingModuleDto) {
                    serializableModulesMap.put(key, ((IridiumReportingModuleDto) value).toIridiumReportingModule());
                } else if (value instanceof LoRaReportingModuleDto) {
                    serializableModulesMap.put(key, ((LoRaReportingModuleDto) value).toLoRaReportingModule());
                } else if (value instanceof RtcModuleDto) {
                    serializableModulesMap.put(key, ((RtcModuleDto) value).toRtcModule());
                } else if (value instanceof NetworkModuleDto) {
                    serializableModulesMap.put(key, ((NetworkModuleDto) value).toNetworkModule());
                } else if (value instanceof StorageModuleDto) {
                    serializableModulesMap.put(key, ((StorageModuleDto) value).toStorageModule());
                } else if (value instanceof LocationModuleDto) {
                    serializableModulesMap.put(key, ((LocationModuleDto) value).toLocationModule());
                } else if (value instanceof ICListenHFDto) {
                    serializableModulesMap.put(key, ((ICListenHFDto) value).toSerializableModule());
                } else {
                    throw new IllegalArgumentException("Unknown ExtModule type: " + value.getClass().getName());
                }
            });
            return serializableModulesMap;
        }

        public static Map<String, SerializableModuleDto> fromSerializableModules(Map<String, SerializableModule> extModules) {
            Map<String, SerializableModuleDto> dto = new HashMap<>();
            extModules.forEach((key, value) -> {
                if (value instanceof BatteryModule) {
                    dto.put(BatteryModule.name, BatteryModuleDto.fromBatteryModule((BatteryModule) value));
                } else if (value instanceof AmbientModule) {
                    dto.put(AmbientModule.name, AmbientModuleDTO.fromTemperatureModule((AmbientModule) value));
                } else if (value instanceof OperationModesModule) {
                    dto.put(OperationModesModule.name, OperationModeModuleDto.fromOperationModeModule((OperationModesModule) value));
                } else if (value instanceof OperationModesGraphModule) {
                    dto.put(OperationModesGraphModule.name, OperationModesGraphModuleDto.fromOperationModesGraphModule((OperationModesGraphModule) value));
                } else if (value instanceof IridiumReportingModule) {
                    dto.put(IridiumReportingModule.name, IridiumReportingModuleDto.fromIridiumReportingModule((IridiumReportingModule) value));
                } else if (value instanceof LoRaReportingModule) {
                    dto.put(LoRaReportingModule.name, LoRaReportingModuleDto.fromLoRaReportingModule((LoRaReportingModule) value));
                } else if (value instanceof RTCModule) {
                    dto.put(RTCModule.name, RtcModuleDto.fromRtcModule((RTCModule) value));
                } else if (value instanceof NetworkModule) {
                    dto.put(NetworkModule.name, NetworkModuleDto.fromNetworkModule((NetworkModule) value));
                } else if (value instanceof StorageModule) {
                    dto.put(StorageModule.name, StorageModuleDto.fromStorageModule((StorageModule) value));
                } else if (value instanceof LocationModule) {
                    dto.put(LocationModule.name, LocationModuleDto.fromLocationModule((LocationModule) value));
                } else if (value instanceof ICListenHF) {
                    dto.put(ICListenHF.name, ICListenHFDto.fromSerializable((ICListenHF) value));
                } else {
                    throw new IllegalArgumentException("Unknown ExtModule type: " + value.getClass().getName());
                }
            });
            return dto;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        public static class LocationModuleDto extends SerializableModuleDto {
            private Float latitude;
            private Float longitude;

            public static LocationModuleDto fromLocationModule(LocationModule value) {
                LocationModuleDto dto = new LocationModuleDto();
                dto.setLatitude(value.getLatitude());
                dto.setLongitude(value.getLongitude());
                return dto;
            }

            public LocationModule toLocationModule() {
                return new LocationModule(
                        this.latitude,
                        this.longitude
                );
            }
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        public static class StorageModuleDto extends SerializableModuleDto {
            private Integer storageUsedMegabytes;
            private Integer storageTotalMegabytes;

            public static StorageModuleDto fromStorageModule(StorageModule value) {
                StorageModuleDto dto = new StorageModuleDto();
                dto.setStorageUsedMegabytes(value.getStorageUsedMegabytes());
                dto.setStorageTotalMegabytes(value.getStorageTotalMegabytes());
                return dto;
            }

            public StorageModule toStorageModule() {
                return new StorageModule(
                        this.storageUsedMegabytes,
                        this.storageTotalMegabytes
                );
            }
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class BatteryModuleDto extends SerializableModuleDto {
            private Integer batteryPercentage;
            private Integer batteryStatus;

            public static BatteryModuleDto fromBatteryModule(BatteryModule value) {
                BatteryModuleDto dto = new BatteryModuleDto();
                dto.setBatteryPercentage(value.getBatteryPercentage());
                dto.setBatteryStatus(value.getBatteryStatus().getValue());
                return dto;
            }

            public BatteryModule toBatteryModule() {
                return new BatteryModule(
                        this.batteryPercentage,
                        BatteryStatus.fromInt(this.batteryStatus)
                );
            }
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class AmbientModuleDTO extends SerializableModuleDto {
            private Integer temperature;
            private Integer humidity;

            public static AmbientModuleDTO fromTemperatureModule(AmbientModule value) {
                AmbientModuleDTO dto = new AmbientModuleDTO();
                dto.setTemperature(value.getTemperature());
                dto.setHumidity(value.getHumidity());
                return dto;
            }

            public AmbientModule toAmbientModule() {
                return new AmbientModule(
                        this.temperature,
                        this.humidity
                );
            }
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class OperationModeModuleDto extends SerializableModuleDto {
            private Map<Short, String> modes;
            private Short activeOperationModeIdx;

            public static OperationModeModuleDto fromOperationModeModule(OperationModesModule module) {
                OperationModeModuleDto dto = new OperationModeModuleDto();
                dto.setActiveOperationModeIdx(module.getActiveOperationModeIdx());
                dto.setModes(module.getModes()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(e -> (short) e.getKey().byteValue(), e -> e.getValue().getName())));
                return dto;
            }

            public OperationModesModule toOperationModeModule() {
                Map<Short, OperationMode> modes = this.modes.entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> OperationMode.create(e.getKey(), e.getValue())));
                return new OperationModesModule(modes, activeOperationModeIdx);
            }
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class IridiumReportingModuleDto extends SerializableModuleDto {
            private Integer technologyId;
            private Map<Short, Short> reportingPeriodsPerOperationModeIdx; // Changed List to Map
            private String imei;

            public static IridiumReportingModuleDto fromIridiumReportingModule(IridiumReportingModule value) {
                IridiumReportingModuleDto dto = new IridiumReportingModuleDto();
                dto.setTechnologyId((int) value.getTechnologyId());
                dto.setImei(value.getImei());

                // Convert ReportingModule Map to DTO Map
                dto.setReportingPeriodsPerOperationModeIdx(value.getReportingPeriodsPerOperationMode()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().getId(), Map.Entry::getValue))
                );

                return dto;
            }

            public IridiumReportingModule toIridiumReportingModule() {
                Map<OperationMode, Short> reportingPeriods = reportingPeriodsPerOperationModeIdx.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                entry -> OperationMode.create(entry.getKey(), "operationMode" + entry.getKey()),
                                Map.Entry::getValue
                        ));
                IridiumReportingModule module = new IridiumReportingModule(reportingPeriods, this.imei);
                return module;
            }
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class LoRaReportingModuleDto extends SerializableModuleDto {
            private Integer technologyId;
            private Map<Short, Short> reportingPeriodsPerOperationModeIdx; // Changed List to Map

            public static LoRaReportingModuleDto fromLoRaReportingModule(LoRaReportingModule value) {
                LoRaReportingModuleDto dto = new LoRaReportingModuleDto();
                dto.setTechnologyId((int) value.getTechnologyId());

                // Convert ReportingModule Map to DTO Map
                dto.setReportingPeriodsPerOperationModeIdx(value.getReportingPeriodsPerOperationMode()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().getId(), Map.Entry::getValue))
                );

                return dto;
            }

            public LoRaReportingModule toLoRaReportingModule() {
                Map<OperationMode, Short> reportingPeriods = reportingPeriodsPerOperationModeIdx.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                entry -> OperationMode.create(entry.getKey(), "operationMode" + entry.getKey()),
                                Map.Entry::getValue
                        ));
                LoRaReportingModule module = new LoRaReportingModule(reportingPeriods);
                return module;
            }
        }


        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class OperationModesGraphModuleDto extends SerializableModuleDto {
            private Map<Integer, TransitionDto> graph;

            @Data
            public static class TransitionDto {
                private Integer targetMode;
                private Integer duration;

                public TransitionDto(Integer targetMode, Integer duration) {
                    this.targetMode = targetMode;
                    this.duration = duration;
                }
            }

            public static OperationModesGraphModuleDto fromOperationModesGraphModule(OperationModesGraphModule module) {
                OperationModesGraphModuleDto dto = new OperationModesGraphModuleDto();
                // Convert graph to TransitionDto map
                Map<Integer, TransitionDto> graphDto = module.getGraph().entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new TransitionDto(entry.getValue().targetMode(), entry.getValue().duration())
                        ));

                dto.setGraph(graphDto);
                return dto;
            }

            public OperationModesGraphModule toOperationModesGraphModule() {
                // Convert TransitionDto map back to the original graph structure
                Map<Integer, OperationModesGraphModule.Transition> graph = this.graph.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new OperationModesGraphModule.Transition(
                                        entry.getValue().getTargetMode(),
                                        entry.getValue().getDuration()
                                )
                        ));

                return new OperationModesGraphModule(graph);
            }
        }


        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class RtcModuleDto extends SerializableModuleDto {
            private String currentTime;

            public static RtcModuleDto fromRtcModule(RTCModule value) {
                RtcModuleDto dto = new RtcModuleDto();
                dto.setCurrentTime(value.getCurrentTime().toString());
                return dto;
            }

            public RTCModule toRtcModule() {
                return new RTCModule(
                        LocalDateTime.parse(this.currentTime)
                );
            }
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class NetworkModuleDto extends SerializableModuleDto {
            private Integer localAddress;
            private RoutingTableDto routingTable;

            public static NetworkModuleDto fromNetworkModule(NetworkModule value) {
                NetworkModuleDto dto = new NetworkModuleDto();
                dto.setLocalAddress((int) value.getLocalAddress().getValue());
                dto.setRoutingTable(RoutingTableDto.fromRoutingTable(value.getRoutingTable()));
                return dto;
            }

            public NetworkModule toNetworkModule() {
                return new NetworkModule(
                        Address.fromValue(this.localAddress),
                        this.routingTable.toRoutingTable()
                );
            }

            @Data
            public static class RoutingTableDto {
                private Map<Integer, Integer> peerRoutes;
                private Integer defaultGateway;

                public static RoutingTableDto fromRoutingTable(NetworkModule.RoutingTable routingTable) {
                    RoutingTableDto dto = new RoutingTableDto();
                    // Convert peerRoutes Map<Address, Address> to Map<Integer, Integer>
                    dto.setPeerRoutes(routingTable.getPeerRoutes()
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(e ->
                                            (int) e.getKey().getValue(),
                                    e -> (int) e.getValue().getValue())
                            )
                    );

                    dto.setDefaultGateway((int) routingTable.getDefaultGateway().getValue());
                    return dto;
                }

                public NetworkModule.RoutingTable toRoutingTable() {
                    NetworkModule.RoutingTable routingTable = new NetworkModule.RoutingTable();

                    // Convertir el Map<Integer, Integer> a Map<Address, Address>
                    this.peerRoutes.forEach((key, value) ->
                            routingTable.addRoute(Address.fromValue(key), Address.fromValue(value))
                    );

                    // Establecer el defaultGateway
                    routingTable.setDefaultGateway(Address.fromValue(this.defaultGateway));

                    return routingTable;
                }
            }
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class ICListenHFDto extends SerializableModuleDto {
            private String serialNumber;
            @Nullable
            private StatusDto status;
            @Nullable
            private LoggingConfigDto loggingConfig;
            @Nullable
            private StreamingConfigDto streamingConfig;
            @Nullable
            private RecordingStatsDto recordingStats;

            private static ICListenHFDto fromSerializable(SerializableModule serializableModule) {
                ICListenHFDto dto = new ICListenHFDto();
                dto.setSerialNumber(ICListenHF.serialNumber);
                if (serializableModule instanceof ICListenHF icListenHF) {
                    if (icListenHF.getStatus() != null) {
                        dto.setStatus(StatusDto.fromStatus(icListenHF.getStatus()));
                    }
                    if (icListenHF.getLoggingConfig() != null) {
                        dto.setLoggingConfig(LoggingConfigDto.fromLoggingConfig(icListenHF.getLoggingConfig()));
                    }
                    if (icListenHF.getStreamingConfig() != null) {
                        dto.setStreamingConfig(StreamingConfigDto.fromStreamingConfig(icListenHF.getStreamingConfig()));
                    }
                    if (icListenHF.getRecordingStats() != null) {
                        dto.setRecordingStats(RecordingStatsDto.fromRecordingStats(icListenHF.getRecordingStats()));
                    }
                }
                return dto;
            }

            public SerializableModule toSerializableModule() {
                ICListenHF module = new ICListenHF();
                if (this.status != null) {
                    module.setStatus(this.status.toStatus());
                }
                if (this.loggingConfig != null) {
                    module.setLoggingConfig(this.loggingConfig.toLoggingConfig());
                }
                if (this.streamingConfig != null) {
                    module.setStreamingConfig(this.streamingConfig.toStreamingConfig());
                }
                if (this.recordingStats != null) {
                    module.setRecordingStats(this.recordingStats.toRecordingStats());
                }
                return module;
            }

            @Data
            public static class StatusDto {
                private Integer unitStatus;
                private Integer batteryStatus;
                private Float batteryPercentage;
                private Float temperature;
                private Float humidity;
                private LocalDateTime timestamp;

                public static StatusDto fromStatus(ICListenStatus status) {
                    StatusDto dto = new StatusDto();
                    dto.setUnitStatus(status.getUnitStatus());
                    dto.setBatteryStatus(status.getBatteryStatus());
                    dto.setBatteryPercentage(status.getBatteryPercentage());
                    dto.setTemperature(status.getTemperature());
                    dto.setHumidity(status.getHumidity());
                    dto.setTimestamp(status.getTimestamp());
                    return dto;
                }

                public ICListenStatus toStatus() {
                    return new ICListenStatus(
                            UUID.randomUUID(),
                            this.unitStatus,
                            this.batteryStatus,
                            this.batteryPercentage,
                            this.temperature,
                            this.humidity,
                            this.timestamp
                    );
                }
            }

            @Data
            public static class LoggingConfigDto {
                private WaveformConfigDTO wav;
                private FFTConfigDTO fft;
                private LocalDateTime timestamp;

                public ICListenLoggingConfig toLoggingConfig() {
                    return new ICListenLoggingConfig(
                            UUID.randomUUID(),
                            this.wav.getGain(),
                            this.wav.getSampleRate(),
                            this.wav.getLoggingMode(),
                            this.wav.getLogLength(),
                            this.wav.getBitDepth(),
                            this.fft.getSampleRate(),
                            this.fft.getProcessingType(),
                            this.fft.getFftsAccumulated(),
                            this.fft.getLoggingMode(),
                            this.fft.getLogLength(),
                            this.timestamp
                    );
                }

                @Data
                public static class WaveformConfigDTO {
                    private Integer gain;
                    private Integer sampleRate;
                    private Integer bitDepth;
                    private Integer loggingMode;
                    private Integer logLength;
                }

                @Data
                public static class FFTConfigDTO {
                    private Integer processingType;
                    private Integer sampleRate;
                    private Integer fftsAccumulated;
                    private Integer loggingMode;
                    private Integer logLength;
                }

                public static LoggingConfigDto fromLoggingConfig(ICListenLoggingConfig loggingConfig) {
                    LoggingConfigDto dto = new LoggingConfigDto();
                    dto.setWav(
                            new WaveformConfigDTO() {{
                                setGain(loggingConfig.getGain());
                                setSampleRate(loggingConfig.getWaveformSampleRate());
                                setLoggingMode(loggingConfig.getWaveformLoggingMode());
                                setLogLength(loggingConfig.getWaveformLogLength());
                                setBitDepth(loggingConfig.getBitDepth());
                            }}
                    );
                    dto.setFft(
                            new FFTConfigDTO() {{
                                setSampleRate(loggingConfig.getFftSampleRate());
                                setProcessingType(loggingConfig.getFftProcessingType());
                                setFftsAccumulated(loggingConfig.getFftsAccumulated());
                                setLoggingMode(loggingConfig.getFftLoggingMode());
                                setLogLength(loggingConfig.getFftLogLength());
                            }}
                    );
                    dto.setTimestamp(loggingConfig.getTimestamp());
                    return dto;
                }
            }

            @Data
            public static class StreamingConfigDto {
                private WaveformConfigDTO wav;
                private FFTConfigDTO fft;
                private String timestamp;

                public ICListenStreamingConfig toStreamingConfig() {
                    return new ICListenStreamingConfig(
                            UUID.randomUUID(),
                            this.wav.getRecordWaveform(),
                            this.wav.getProcessWaveform(),
                            this.wav.getWaveformProcessingType(),
                            this.wav.getWaveformInterval(),
                            this.wav.getWaveformDuration(),
                            this.fft.getRecordFFT(),
                            this.fft.getProcessFFT(),
                            this.fft.getFftProcessingType(),
                            this.fft.getFftInterval(),
                            this.fft.getFftDuration(),
                            LocalDateTime.parse(this.timestamp)
                    );
                }

                @Data
                public static class WaveformConfigDTO {
                    private Boolean recordWaveform;
                    private Boolean processWaveform;
                    private Integer waveformProcessingType;
                    private Integer waveformInterval;
                    private Integer waveformDuration;
                }

                @Data
                public static class FFTConfigDTO {
                    private Boolean recordFFT;
                    private Boolean processFFT;
                    private Integer fftProcessingType;
                    private Integer fftInterval;
                    private Integer fftDuration;
                }

                public static StreamingConfigDto fromStreamingConfig(ICListenStreamingConfig streamingConfig) {
                    StreamingConfigDto dto = new StreamingConfigDto();
                    dto.setWav(
                            new WaveformConfigDTO() {{
                                setRecordWaveform(streamingConfig.isRecordWaveform());
                                setProcessWaveform(streamingConfig.isProcessWaveform());
                                setWaveformProcessingType(streamingConfig.getWaveformProcessingType());
                                setWaveformInterval(streamingConfig.getWaveformInterval());
                                setWaveformDuration(streamingConfig.getWaveformDuration());
                            }}
                    );
                    dto.setFft(
                            new FFTConfigDTO() {{
                                setRecordFFT(streamingConfig.isRecordFFT());
                                setProcessFFT(streamingConfig.isProcessFFT());
                                setFftProcessingType(streamingConfig.getFftProcessingType());
                                setFftInterval(streamingConfig.getFftInterval());
                                setFftDuration(streamingConfig.getFftDuration());
                            }}
                    );
                    dto.setTimestamp(streamingConfig.getTimestamp().toString());
                    return dto;
                }
            }


            @Data
            public static class RecordingStatsDto {
                private String epochTime;
                private Integer numberOfClicks;
                private Integer recordedMinutes;
                private Integer numberOfFiles;

                public static RecordingStatsDto fromRecordingStats(ICListenRecordingStats recordingStats) {
                    RecordingStatsDto dto = new RecordingStatsDto();
                    dto.setEpochTime(recordingStats.getEpochTime().toString());
                    dto.setNumberOfClicks(recordingStats.getNumberOfClicks());
                    dto.setRecordedMinutes(recordingStats.getRecordedMinutes());
                    dto.setNumberOfFiles(recordingStats.getNumberOfFiles());
                    return dto;
                }

                public ICListenRecordingStats toRecordingStats() {
                    return new ICListenRecordingStats(
                            UUID.randomUUID(),
                            LocalDateTime.parse(this.epochTime),
                            this.numberOfClicks,
                            this.recordedMinutes,
                            this.numberOfFiles
                    );
                }
            }
        }

    }

}
