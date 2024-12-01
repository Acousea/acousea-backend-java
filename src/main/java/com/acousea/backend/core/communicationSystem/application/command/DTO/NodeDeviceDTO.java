package com.acousea.backend.core.communicationSystem.application.command.DTO;

import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient.AmbientModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.battery.BatteryModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.location.LocationModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.network.NetworkModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.IridiumReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.reportingPeriods.LoRaReportingModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.rtc.RTCModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.storage.StorageModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.ICListenHF;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.PamModule;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenLoggingConfig;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenRecordingStats;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStreamingConfig;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Data
public class NodeDeviceDTO {
    private String id;
    private String name;
    private String icon;
    private ExtModulesDto extModules;
    private List<PamModuleDto> pamModules;

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
        dto.setExtModules(ExtModulesDto.fromExtModules(nodeDevice.getExtModules()));
        dto.setPamModules(PamModuleDto.fromPamModules(nodeDevice.getPamModules()));
        return dto;
    }

    @Data
    public static class ExtModulesDto {
        private LocationModuleDto location;
        private StorageModuleDto storage;
        private BatteryModuleDto battery;
        private AmbientModuleDTO ambient;
        private IridiumReportingModuleDto iridiumReporting;
        private LoRaReportingModuleDto loRaReporting;
        private RtcModuleDto rtc;
        private NetworkModuleDto network;

        public static ExtModulesDto fromExtModules(Map<String, ExtModule> extModules) {
            ExtModulesDto dto = new ExtModulesDto();
            extModules.forEach((key, value) -> {
                if (value instanceof LocationModule) {
                    dto.setLocation(LocationModuleDto.fromLocationModule((LocationModule) value));
                } else if (value instanceof StorageModule) {
                    dto.setStorage(StorageModuleDto.fromStorageModule((StorageModule) value));
                } else if (value instanceof BatteryModule) {
                    dto.setBattery(BatteryModuleDto.fromBatteryModule((BatteryModule) value));
                } else if (value instanceof AmbientModule) {
                    dto.setAmbient(AmbientModuleDTO.fromTemperatureModule((AmbientModule) value));
                } else if (value instanceof IridiumReportingModule) {
                    dto.setIridiumReporting(IridiumReportingModuleDto.fromIridiumReportingModule((IridiumReportingModule) value));
                } else if (value instanceof LoRaReportingModule) {
                    dto.setLoRaReporting(LoRaReportingModuleDto.fromLoRaReportingModule((LoRaReportingModule) value));
                } else if (value instanceof RTCModule) {
                    dto.setRtc(RtcModuleDto.fromRtcModule((RTCModule) value));
                } else if (value instanceof NetworkModule) {
                    dto.setNetwork(NetworkModuleDto.fromNetworkModule((NetworkModule) value));
                }
            });
            return dto;
        }

        @Data
        public static class LocationModuleDto {
            private String name;
            private Double latitude;
            private Double longitude;

            public static LocationModuleDto fromLocationModule(LocationModule value) {
                LocationModuleDto dto = new LocationModuleDto();
                dto.setName(value.getName());
                dto.setLatitude(value.getLatitude());
                dto.setLongitude(value.getLongitude());
                return dto;
            }
        }

        @Data
        public static class StorageModuleDto {
            private String name;
            private Integer storageUsedMegabytes;
            private Integer storageTotalMegabytes;

            public static StorageModuleDto fromStorageModule(StorageModule value) {
                StorageModuleDto dto = new StorageModuleDto();
                dto.setName(value.getName());
                dto.setStorageUsedMegabytes(value.getStorageUsedMegabytes());
                dto.setStorageTotalMegabytes(value.getStorageTotalMegabytes());
                return dto;
            }
        }

        @Data
        public static class BatteryModuleDto {
            private String name;
            private Integer batteryPercentage;
            private Integer batteryStatus;

            public static BatteryModuleDto fromBatteryModule(BatteryModule value) {
                BatteryModuleDto dto = new BatteryModuleDto();
                dto.setName(value.getName());
                dto.setBatteryPercentage(value.getBatteryPercentage());
                dto.setBatteryStatus(value.getBatteryStatus().getValue());
                return dto;
            }
        }

        @Data
        public static class AmbientModuleDTO {
            private String name;
            private Double temperature;
            private Double humidity;

            public static AmbientModuleDTO fromTemperatureModule(AmbientModule value) {
                AmbientModuleDTO dto = new AmbientModuleDTO();
                dto.setName(value.getName());
                dto.setTemperature((double) value.getTemperature());
                dto.setHumidity((double) value.getHumidity());
                return dto;
            }
        }

        @Data
        public static class IridiumReportingModuleDto {
            private String name;
            private Integer technologyId;
            private Map<Byte, Short> reportingPeriods;
            private String imei;

            public static IridiumReportingModuleDto fromIridiumReportingModule(IridiumReportingModule value) {
                IridiumReportingModuleDto dto = new IridiumReportingModuleDto();
                dto.setName(value.getName());
                dto.setTechnologyId((int) value.getTechnologyId());
                dto.setReportingPeriods(value.getReportingPeriodsPerOperationMode());
                dto.setImei(value.getImei());
                return dto;
            }
        }

        @Data
        public static class LoRaReportingModuleDto {
            private String name;
            private Integer technologyId;
            private Map<Byte, Short> reportingPeriods;

            public static LoRaReportingModuleDto fromLoRaReportingModule(LoRaReportingModule value) {
                LoRaReportingModuleDto dto = new LoRaReportingModuleDto();
                dto.setName(value.getName());
                dto.setTechnologyId((int) value.getTechnologyId());
                dto.setReportingPeriods(value.getReportingPeriodsPerOperationMode());
                return dto;
            }
        }

        @Data
        public static class RtcModuleDto {
            private String name;
            private String currentTime;

            public static RtcModuleDto fromRtcModule(RTCModule value) {
                RtcModuleDto dto = new RtcModuleDto();
                dto.setName(value.getName());
                dto.setCurrentTime(value.getCurrentTime().toString());
                return dto;

            }
        }

        @Data
        public static class NetworkModuleDto {
            private String name;
            private Integer localAddress;
            private RoutingTableDto routingTable;

            public static NetworkModuleDto fromNetworkModule(NetworkModule value) {
                NetworkModuleDto dto = new NetworkModuleDto();
                dto.setName(value.getName());
                dto.setLocalAddress((int) value.getLocalAddress().getValue());
                dto.setRoutingTable(RoutingTableDto.fromRoutingTable(value.getRoutingTable()));
                return dto;
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
            }
        }
    }

    @Data
    public static class PamModuleDto {
        private String serialNumber;
        private String name;
        private StatusDto status;
        private LoggingConfigDto loggingConfig;
        private StreamingConfigDto streamingConfig;
        private RecordingStatsDto recordingStats;

        public static List<PamModuleDto> fromPamModules(List<PamModule> pamModules) {
            return pamModules.stream()
                    .map(PamModuleDto::fromPamModule)
                    .toList();
        }

        private static PamModuleDto fromPamModule(PamModule pamModule) {
            PamModuleDto dto = new PamModuleDto();
            dto.setSerialNumber(pamModule.getSerialNumber());
            dto.setName(pamModule.getName());
            if (pamModule instanceof ICListenHF) {
                ICListenHF icListenHF = (ICListenHF) pamModule;
                dto.setStatus(StatusDto.fromStatus(icListenHF.getStatus()));
                dto.setLoggingConfig(LoggingConfigDto.fromLoggingConfig(icListenHF.getLoggingConfig()));
                dto.setStreamingConfig(StreamingConfigDto.fromStreamingConfig(icListenHF.getStreamingConfig()));
                dto.setRecordingStats(RecordingStatsDto.fromRecordingStats(icListenHF.getRecordingStats()));
            }
            return dto;
        }

        @Data
        public static class StatusDto {
            private Integer unitStatus;
            private Integer batteryStatus;
            private Double batteryPercentage;
            private Double temperature;
            private Double humidity;
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
        }

        @Data
        public static class LoggingConfigDto {
            private WaveformConfigDTO waveformConfig;
            private FFTConfigDTO fftConfig;
            private LocalDateTime timestamp;

            @Data
            public static class WaveformConfigDTO {
                private Integer gain;
                private Integer waveformSampleRate;
                private Integer waveformLoggingMode;
                private Integer waveformLogLength;
                private Integer bitDepth;
            }

            @Data
            public static class FFTConfigDTO {
                private Integer fftSampleRate;
                private Integer fftProcessingType;
                private Integer fftsAccumulated;
                private Integer fftLoggingMode;
                private Integer fftLogLength;
            }

            public static LoggingConfigDto fromLoggingConfig(ICListenLoggingConfig loggingConfig) {
                LoggingConfigDto dto = new LoggingConfigDto();
                dto.setWaveformConfig(
                        new WaveformConfigDTO() {{
                            setGain(loggingConfig.getGain());
                            setWaveformSampleRate(loggingConfig.getWaveformSampleRate());
                            setWaveformLoggingMode(loggingConfig.getWaveformLoggingMode());
                            setWaveformLogLength(loggingConfig.getWaveformLogLength());
                            setBitDepth(loggingConfig.getBitDepth());
                        }}
                );
                dto.setFftConfig(
                        new FFTConfigDTO() {{
                            setFftSampleRate(loggingConfig.getFftSampleRate());
                            setFftProcessingType(loggingConfig.getFftProcessingType());
                            setFftsAccumulated(loggingConfig.getFftsAccumulated());
                            setFftLoggingMode(loggingConfig.getFftLoggingMode());
                            setFftLogLength(loggingConfig.getFftLogLength());
                        }}
                );
                dto.setTimestamp(loggingConfig.getTimestamp());
                return dto;
            }
        }

        @Data
        public static class StreamingConfigDto {
            private Boolean recordWaveform;
            private Boolean processWaveform;
            private Integer waveformProcessingType;
            private Integer waveformInterval;
            private Integer waveformDuration;
            private Boolean recordFFT;
            private Boolean processFFT;
            private Integer fftProcessingType;
            private Integer fftInterval;
            private Integer fftDuration;
            private String timestamp;

            public static StreamingConfigDto fromStreamingConfig(ICListenStreamingConfig streamingConfig) {
                StreamingConfigDto dto = new StreamingConfigDto();
                dto.setRecordWaveform(streamingConfig.isRecordWaveform());
                dto.setProcessWaveform(streamingConfig.isProcessWaveform());
                dto.setWaveformProcessingType(streamingConfig.getWaveformProcessingType());
                dto.setWaveformInterval(streamingConfig.getWaveformInterval());
                dto.setWaveformDuration(streamingConfig.getWaveformDuration());
                dto.setRecordFFT(streamingConfig.isRecordFFT());
                dto.setProcessFFT(streamingConfig.isProcessFFT());
                dto.setFftProcessingType(streamingConfig.getFftProcessingType());
                dto.setFftInterval(streamingConfig.getFftInterval());
                dto.setFftDuration(streamingConfig.getFftDuration());
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
        }
    }
}
