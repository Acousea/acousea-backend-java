package com.acousea.backend.core.communicationSystem.infrastructure.modules;

import jakarta.persistence.*;

@Entity
@Table(name = "modules")
public class SQLModule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int moduleId;

    @Column(name = "module_name")
    private String moduleName;

    // Constructor sin parámetros necesario para JPA
    public SQLModule() {
    }

    // Constructor completo
    public SQLModule(int moduleId, String moduleName) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}