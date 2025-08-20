package com.group3.inventhor.service;

import com.group3.inventhor.dto.AddressDTO;
import com.group3.inventhor.mapper.AddressMapper;
import com.group3.inventhor.model.Address;
import com.group3.inventhor.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Tatiana Fløisbonn
 *
 * The AddressService class provides methods to manage addresses in the Inventhor application.
 * It includes methods to get, update, and create addresses.
 *
 * @Service indicates that this is a service class that contains business logic and interacts with the data access layer.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 */
@Service
@RequiredArgsConstructor
public class AddressService {

    // The AddressRepository instance uses to interact with the database for address-related operations
    private final AddressRepository addressRepository;
    // The AddressMapper instance used to convert between Address and AddressDTO objects
    private final AddressMapper addressMapper;

    /**
     * Get address by address number.
     * @param addressnr the unique identifier for the address.
     * @return AddressDTO containing address details.
     */
    public AddressDTO getAddressByAddressNr(Integer addressnr) {
        // Find address by ID, throw exception if not found
        Address address = addressRepository.findById(addressnr)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        return addressMapper.toAddressDTO(address);
    }

    /**
     * Update address by address number.
     *
     * @param addressnr the unique identifier for the address to be updated.
     * @param addressDTO the AddressDTO containing updated address details.
     * @return AddressDTO containing updated address details.
     */
    public AddressDTO updateAddressByAddressId(Integer addressnr, AddressDTO addressDTO) {
        // Find address by ID, throw exception if not found
        Address address = addressRepository.findById(addressnr)
                .orElseThrow(() -> new EntityNotFoundException("Address not found "));
        // Update address fields with values from addressDTO
        address.setStreet(addressDTO.getStreet());
        address.setPostcode(addressDTO.getPostcode());
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        // Save updated address to the repository
        Address updatedAddress = addressRepository.save(address);
        return addressMapper.toAddressDTO(updatedAddress);
    }

    /**
     * Create a new address.
     *
     * @param addressDTO the AddressDTO containing address details to be created.
     * @return AddressDTO containing the created address details.
     */
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setPostcode(addressDTO.getPostcode());
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toAddressDTO(savedAddress);
    }

    /**
     * Delete an address by address number.
     *
     * @param addressnr the unique identifier for the address to be deleted.
     *
     */
    public void deleteAddressByAddressNr(Integer addressnr) {
        // Find address by ID, throw exception if not found
        Address address = addressRepository.findById(addressnr)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        // Delete the address from the repository
        addressRepository.delete(address);
    }

}
