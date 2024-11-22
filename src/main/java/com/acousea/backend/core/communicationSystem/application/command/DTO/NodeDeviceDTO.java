package com.acousea.backend.core.communicationSystem.application.command.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class NodeDeviceDTO {
    private String id;
    private String name;
    private String icon;
    private ExtModulesDto extModules;
    private List<PamModuleDto> pamModules;

    @Data
    public static class ExtModulesDto {
        private LocationModuleDto location;
        private StorageModuleDto storage;
        private BatteryModuleDto battery;
        private TemperatureModuleDto temperature;
        private IridiumReportingModuleDto iridiumReporting;
        private LoRaReportingModuleDto loRaReporting;
        private RtcModuleDto rtc;
        private NetworkModuleDto network;

        @Data
        public static class LocationModuleDto {
            private String name;
            private Double latitude;
            private Double longitude;
        }

        @Data
        public static class StorageModuleDto {
            private String name;
            private Integer storageUsedMegabytes;
            private Integer storageTotalMegabytes;
        }

        @Data
        public static class BatteryModuleDto {
            private String name;
            private Integer batteryPercentage;
            private String batteryStatus;
        }

        @Data
        public static class TemperatureModuleDto {
            private String name;
            private Double temperature;
            private Double humidity;
        }

        @Data
        public static class IridiumReportingModuleDto {
            private String name;
            private Integer technologyId;
            private Map<String, Integer> reportingPeriods;
            private String imei;
        }

        @Data
        public static class LoRaReportingModuleDto {
            private String name;
            private Integer technologyId;
            private Map<String, Integer> reportingPeriods;
        }

        @Data
        public static class RtcModuleDto {
            private String name;
            private String currentTime;
        }

        @Data
        public static class NetworkModuleDto {
            private String name;
            private Integer localAddress;
            private RoutingTableDto routingTable;

            @Data
            public static class RoutingTableDto {
                private Map<Integer, Integer> peerRoutes;
                private Integer defaultGateway;
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

        @Data
        public static class StatusDto {
            private String id;
            private Integer unitStatus;
            private Integer batteryStatus;
            private Double batteryPercentage;
            private Double temperature;
            private Double humidity;
            private String timestamp;
        }

        @Data
        public static class LoggingConfigDto {
            private String id;
            private Integer gain;
            private Integer waveformSampleRate;
            private Integer waveformLoggingMode;
            private Integer waveformLogLength;
            private Integer bitDepth;
            private Integer fftSampleRate;
            private Integer fftProcessingType;
            private Integer fftsAccumulated;
            private Integer fftLoggingMode;
            private Integer fftLogLength;
            private String timestamp;
        }

        @Data
        public static class StreamingConfigDto {
            private String id;
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
        }

        @Data
        public static class RecordingStatsDto {
            private String id;
            private String epochTime;
            private Integer numberOfClicks;
            private Integer recordedMinutes;
            private Integer numberOfFiles;
        }
    }
}
