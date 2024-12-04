package com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ambient;


import com.acousea.backend.core.communicationSystem.domain.nodes.extModules.ExtModule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmbientModule extends ExtModule {
    public static final String name = "ambient";
    private int temperature;
    private int humidity;

    public AmbientModule(int temperature, int humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    @Override
    public int getFullSize() {
        return getMinSize();
    }

    public static int getMinSize() {
        return Integer.BYTES * 2;
    }

    public static AmbientModule create() {
        return new AmbientModule(0, 0);
    }

}
