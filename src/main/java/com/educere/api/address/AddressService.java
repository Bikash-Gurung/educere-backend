package com.educere.api.address;

import com.educere.api.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address save(Address address){
        return addressRepository.save(address);
    }
}
