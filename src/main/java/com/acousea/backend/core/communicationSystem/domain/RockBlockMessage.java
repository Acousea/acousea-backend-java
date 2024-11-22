package com.acousea.backend.core.communicationSystem.domain;


import lombok.Getter;

import java.util.UUID;

@Getter
public class RockBlockMessage {

    private final UUID id;
    private final String imei;
    private final String serial;
    private final int momsn;
    private final String transmitTime;
    private final double iridiumLatitude;
    private final double iridiumLongitude;
    private final int iridiumCep;
    private final String data;

    public RockBlockMessage(String imei, String serial, int momsn, String transmitTime,
                            double iridiumLatitude, double iridiumLongitude, int iridiumCep, String data) {
        this.id = UUID.randomUUID();
        this.imei = imei;
        this.serial = serial;
        this.momsn = momsn;
        this.transmitTime = transmitTime;
        this.iridiumLatitude = iridiumLatitude;
        this.iridiumLongitude = iridiumLongitude;
        this.iridiumCep = iridiumCep;
        this.data = data;
    }

    @Override
    public String toString() {
        return "RockBlockMessage{" +
                "id='" + id + '\'' +
                ", imei='" + imei + '\'' +
                ", serial='" + serial + '\'' +
                ", momsn=" + momsn +
                ", transmitTime='" + transmitTime + '\'' +
                ", iridiumLatitude=" + iridiumLatitude +
                ", iridiumLongitude=" + iridiumLongitude +
                ", iridiumCep=" + iridiumCep +
                ", data='" + data + '\'' +
                '}';
    }
}
