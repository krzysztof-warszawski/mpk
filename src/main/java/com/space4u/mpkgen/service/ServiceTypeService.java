package com.space4u.mpkgen.service;

import com.space4u.mpkgen.entity.ServiceType;

import java.util.List;

public interface ServiceTypeService {

    List<ServiceType> findAll();
    void deleteServiceTypeById(int id);
    ServiceType getServiceTypeById(int id);
}
