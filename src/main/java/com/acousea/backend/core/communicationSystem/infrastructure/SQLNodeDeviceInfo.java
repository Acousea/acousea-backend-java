package com.acousea.backend.core.communicationSystem.infrastructure;


import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Getter
@Entity
@Table(name = "node_device_info")
public class SQLNodeDeviceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "operation_mode", nullable = false)
    private int operationMode;

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SQLModule> modules;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // Constructor sin parámetros necesario para JPA
    public SQLNodeDeviceInfo() {
    }

    // Constructor completo
    public SQLNodeDeviceInfo(UUID id ) {
        this.id = id;
    }

    // Convertir de dominio a entidad ORM
    public static SQLNodeDeviceInfo fromDomain(NodeDevice deviceInfo) {
        return new SQLNodeDeviceInfo(
                deviceInfo.getId()
        );
    }

    // Convertir de entidad ORM a dominio
    public NodeDevice toDomain() {
        return new NodeDevice(
                id,
                "name",
                "",
                Map.of()
        );
    }

}
