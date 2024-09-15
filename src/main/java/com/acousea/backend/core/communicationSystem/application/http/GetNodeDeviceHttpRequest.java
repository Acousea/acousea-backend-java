package com.acousea.backend.core.communicationSystem.application.http;


import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.domain.NodeDevice;
import com.acousea.backend.core.shared.domain.httpWrappers.HttpRequest;
import com.acousea.backend.core.shared.domain.httpWrappers.HttpResponse;

public class GetNodeDeviceHttpRequest extends HttpRequest<Void, NodeDevice>{
    NodeDeviceRepository nodeDeviceRepository;

    public GetNodeDeviceHttpRequest(NodeDeviceRepository nodeDeviceRepository) {
        this.nodeDeviceRepository = nodeDeviceRepository;
    }

    @Override
    public HttpResponse<NodeDevice> execute(Void unused) {
        NodeDevice NodeDevice = nodeDeviceRepository.getNodeDeviceInfo();
        return new HttpResponse<>(200, "Drifter Device Info", NodeDevice);
    }
}