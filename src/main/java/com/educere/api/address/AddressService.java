package com.educere.api.address;

import com.educere.api.common.exception.ResourceNotFoundException;
import com.educere.api.entity.Address;
import com.educere.api.user.auth.dto.AddressRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address save(Address address){
        return addressRepository.save(address);
    }

    public Address create(AddressRequest addressRequest){
        Address address = new Address();
        address.setCountry(addressRequest.getCountry());
        address.setState(addressRequest.getState());
        address.setCity(addressRequest.getCity());
        address.setStreet(addressRequest.getStreet());
        address.setZip(addressRequest.getZip());
        address.setLatitude(addressRequest.getLongitude());
        address.setLongitude(addressRequest.getLongitude());
        return save(address);
    }

    public Address update(AddressRequest addressRequest, Long addressId){
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        address.setCountry(addressRequest.getCountry());
        address.setState(addressRequest.getState());
        address.setCity(addressRequest.getCity());
        address.setStreet(addressRequest.getStreet());
        address.setZip(addressRequest.getZip());
        address.setLatitude(addressRequest.getLongitude());
        address.setLongitude(addressRequest.getLongitude());
        return save(address);
    }
}
