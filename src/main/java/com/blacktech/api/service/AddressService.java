package com.blacktech.api.service;

import com.blacktech.api.domain.address.Address;
import com.blacktech.api.domain.event.Event;
import com.blacktech.api.domain.event.EventRequestDTO;
import com.blacktech.api.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    public Address createAddress(EventRequestDTO data, Event event){
        Address address = new Address();
        address.setCity(data.city());
        address.setUf(data.state());
        address.setEvent(event);

        return addressRepository.save(address);
    }
}
