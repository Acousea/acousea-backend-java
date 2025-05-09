package com.acousea.backend.app.config;

import com.acousea.backend.core.communicationSystem.application.ports.CommunicationRequestHistoryRepository;
import com.acousea.backend.core.communicationSystem.application.ports.NodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.application.ports.RockblockMessageRepository;
import com.acousea.backend.core.communicationSystem.infrastructure.mocks.MockNodeDevices;
import com.acousea.backend.core.communicationSystem.infrastructure.mocks.MockRockBlockMessages;
import com.acousea.backend.core.communicationSystem.infrastructure.ports.InMemory.InMemoryCommunicationRequestHistoryRepository;
import com.acousea.backend.core.communicationSystem.infrastructure.ports.InMemory.InMemoryNodeDeviceRepository;
import com.acousea.backend.core.communicationSystem.infrastructure.ports.InMemory.InMemoryRockblockMessageRepository;
import com.acousea.backend.core.shared.application.services.StorageService;
import com.acousea.backend.core.shared.application.services.authentication.SessionService;
import com.acousea.backend.core.shared.infrastructure.services.authentication.InMemorySessionService;
import com.acousea.backend.core.shared.infrastructure.services.storage.LocalStorageService;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.infrastructure.ports.InMemoryUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@Profile("test")
@TestConfiguration
public class TestConfig {

    public TestConfig() {
        System.out.println("===========>\uD83D\uDFE1\uD83D\uDFE1 TestConfig loaded");
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository(); // Para usar el repositorio en memoria
    }

    @Bean
    public SessionService sessionService() {
        return new InMemorySessionService(); // Usar la implementación en memoria para desarrollo
    }


    @Bean
    public StorageService localStorageService(@Value("${storage.local.directory}") String storageDirectory) {
        return new LocalStorageService(storageDirectory);
    }


    @Bean // Must inject Environment to get the value of the property
    public NodeDeviceRepository nodeDeviceRepository(Environment environment) {
        return new InMemoryNodeDeviceRepository(new MockNodeDevices(environment));
    }

    @Bean
    public RockblockMessageRepository rockblockMessageRepository(Environment environment) {
        return new InMemoryRockblockMessageRepository(new MockRockBlockMessages(environment));
    }

    @Bean
    public CommunicationRequestHistoryRepository communicationRequestHistoryRepository() {
        return new InMemoryCommunicationRequestHistoryRepository();
    }


}


