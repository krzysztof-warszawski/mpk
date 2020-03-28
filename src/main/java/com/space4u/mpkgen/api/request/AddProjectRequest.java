package com.space4u.mpkgen.api.request;

import com.space4u.mpkgen.entity.Building;
import com.space4u.mpkgen.entity.ServiceType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddProjectRequest {

    private int id;
    private int projectNum;
    private String tenant;
    @NotNull(message = "Wprowadź datę")
    @Size(min=1, message = "Wprowadź datę")
    private String date;
    private String floor;
    @NotNull(message = "Wprowadź opis")
    @Size(min=1, message = "Wprowadź opis")
    private String shortDescription;
    private String mpk;
    private int buildingId;
    @NotNull(message = "Wprowadź typ usługi serwisowej")
    private int serviceTypeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(int projectNum) {
        this.projectNum = projectNum;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getMpk() {
        return mpk;
    }

    public void setMpk(String mpk) {
        this.mpk = mpk;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
}
