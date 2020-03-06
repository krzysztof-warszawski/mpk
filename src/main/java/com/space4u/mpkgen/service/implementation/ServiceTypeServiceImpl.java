package com.space4u.mpkgen.service.implementation;

import com.space4u.mpkgen.model.ServiceType;
import com.space4u.mpkgen.repository.ServiceTypeRepository;
import com.space4u.mpkgen.service.ServiceTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private ServiceTypeRepository serviceTypeRepository;

    @Override
    public List<ServiceType> findAll() {
        return serviceTypeRepository.findAll();
    }

    @Override
    public void deleteServiceTypeById(int id) {
        serviceTypeRepository.deleteById(id);
    }

    @Override
    public ServiceType getServiceTypeById(int id) {
        return serviceTypeRepository.getOne(id);
    }
}
