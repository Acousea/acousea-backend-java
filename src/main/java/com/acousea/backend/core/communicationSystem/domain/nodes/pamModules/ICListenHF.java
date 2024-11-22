package com.acousea.backend.core.communicationSystem.domain.nodes.pamModules;

import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenLoggingConfig;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenRecordingStats;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStatus;
import com.acousea.backend.core.communicationSystem.domain.nodes.pamModules.iclisten.ICListenStreamingConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ICListenHF extends PamModule {
    private ICListenStatus status;
    private ICListenLoggingConfig loggingConfig;
    private ICListenStreamingConfig streamingConfig;
    private ICListenRecordingStats recordingStats;

    public ICListenHF(String serialNumber) {
        super(serialNumber, "ICListenHF");
        this.status =  ICListenStatus.createDefault();
        this.loggingConfig = ICListenLoggingConfig.createDefault();
        this.streamingConfig =  ICListenStreamingConfig.createDefault();
        this.recordingStats = ICListenRecordingStats.createDefault();
    }
}
