package com.group3.inventhor.service;

import com.group3.inventhor.dto.AddressDTO;
import com.group3.inventhor.dto.SupplierDTO;
import com.group3.inventhor.mapper.SupplierMapper;
import com.group3.inventhor.model.Address;
import com.group3.inventhor.model.Supplier;
import com.group3.inventhor.model.Employee;
import com.group3.inventhor.repository.AddressRepository;
import com.group3.inventhor.repository.SupplierRepository;
import com.group3.inventhor.repository.EmployeeRepository;
import com.group3.inventhor.repository.ProductSupplierRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nils Patrik Lilja
 */
@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final AddressRepository addressRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductSupplierRepository productSupplierRepository;

    /**
     * Retrieves a supplier by its ID.
     * param suppliernr the supplier's ID
     * return SupplierDTO representing the supplier
     * throws EntityNotFoundException if no supplier is found with the given ID
     */
    public SupplierDTO getSupplierById(Integer suppliernr) {
        Supplier supplier = supplierRepository
                .findById(suppliernr)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found for given supplier"));
        return supplierMapper.toDTO(supplier);
    }

    /** GET - All Suppliers
    /*
     * Method to get all suppliers and convert them to DTOs.
     * List<supplier> - fetch all suppliers entities from database through supplierRepository
     * return - convert the list of supplier entities to a list of SupplierDTOs and return it
     */
    public List<SupplierDTO> findAllDTOs() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return convertToDTOList(suppliers);
    }

    /** Convert DTO-list
    /*
     * Method to convert a list of supplier to a list of supplierDTO.
     * Creating an empty list to hold the converted SupplierDTOs.
     * For every Supplier in the input list:
     * Convert each supplier entity to a SupplierDTO using the mapper.
     * Then adding the converted SupplierDTO to the list.
     * At last return the list of converted SupplierDTOs.
     */
    private List<SupplierDTO> convertToDTOList(List<Supplier> suppliers) {
        List<SupplierDTO> supplierDTOsList = new ArrayList<>();

        for (Supplier supplier : suppliers) {
            SupplierDTO supplierDTO = supplierMapper.toDTO(supplier);
            supplierDTOsList.add(supplierDTO);
        }
        return supplierDTOsList;
    }


    private SupplierDTO convertToDTO(Supplier supplier) {
        return supplierMapper.toDTO(supplier);
    }

    /**POST – Create supplier
    /*
     * Checks if email is null, if so, throw an exception to prevent saving
     * Manually create a new Supplier entity from the incoming SupplierDTO -
     * 'name', 'contact person', 'email', 'phone number', 'website'.
     * Saving the Supplier entity to the database.
     * Converting the saved supplier entity back to a DTO and return it.
     */
    public SupplierDTO save(SupplierDTO supplierDTO, Integer employeenr) {

        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }

        Employee employee = employeeOpt.get();
        System.out.println("Employee ID: " + employee.getEmployeenr() + ", Role: " + employee.getRole());

        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to delete suppliers");
        }


        if (emailExists(supplierDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }


        if (phoneNumberExists(supplierDTO.getPhone())) {
            throw new IllegalArgumentException("A supplier with this phone number already exists");
        }


        if (websiteExists(supplierDTO.getWebsite())) {
            throw new IllegalArgumentException("A supplier with this website already exists");
        }


        Optional<Address> existingAddress = addressRepository.findByCountryAndCityAndStreetAndPostcode(
                supplierDTO.getAddress().getStreet(),
                supplierDTO.getAddress().getPostcode(),
                supplierDTO.getAddress().getCity(),
                supplierDTO.getAddress().getCountry()
        );

        Address address = existingAddress.orElseGet(() -> {

            Address newAddress = new Address();
            newAddress.setStreet(supplierDTO.getAddress().getStreet());
            newAddress.setPostcode(supplierDTO.getAddress().getPostcode());
            newAddress.setCity(supplierDTO.getAddress().getCity());
            newAddress.setCountry(supplierDTO.getAddress().getCountry());

            return addressRepository.save(newAddress);
        });


        Supplier supplier = new Supplier();
        supplier.setName(supplierDTO.getName());
        supplier.setContactperson(supplierDTO.getContact());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setWebsite(supplierDTO.getWebsite());
        supplier.setAddress(address);
        supplier.setNotes(supplierDTO.getNotes());


        Supplier savedSupplier = supplierRepository.save(supplier);


        return supplierMapper.toDTO(savedSupplier);
    }

    /**
     * Updates an existing supplier with the given SupplierDTO data.
     * param suppliernr The ID of the supplier to update.
     * param supplierDTO The new supplier data.
     * param employeenr The employee ID performing the update (for authorization).
     * return Optional containing updated SupplierDTO if update successful, or empty if supplier not found.
     * throws IllegalArgumentException if employee not found or validation fails.
     * throws SecurityException if employee is unauthorized.
     */
    public Optional<SupplierDTO> updateSupplier(Integer suppliernr, SupplierDTO supplierDTO, Integer employeenr) {

        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);

        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }
        Employee employee = employeeOpt.get();
        System.out.println("Employee ID: " + employee.getEmployeenr() + ", Role: " + employee.getRole());

        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to delete suppliers");
        }

        Optional<Supplier> optionalSupplier = supplierRepository.findById(suppliernr);
        if (optionalSupplier.isEmpty()) {
            return Optional.empty();
        }

        Supplier existingSupplier = optionalSupplier.get();

        if (!existingSupplier.getEmail().equals(supplierDTO.getEmail()) &&
                emailExists(supplierDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!existingSupplier.getPhone().equals(supplierDTO.getPhone()) &&
                phoneNumberExists(supplierDTO.getPhone())) {
            throw new IllegalArgumentException("A supplier with this phone number already exists");
        }

        if (!existingSupplier.getWebsite().equals(supplierDTO.getWebsite()) &&
                websiteExists(supplierDTO.getWebsite())) {
            throw new IllegalArgumentException("A supplier with this website already exists");
        }


        Address currentAddress = existingSupplier.getAddress();
        AddressDTO newAddressDTO = supplierDTO.getAddress();

        boolean addressChanged = currentAddress == null ||
                !Objects.equals(currentAddress.getStreet(), newAddressDTO.getStreet()) ||
                !Objects.equals(currentAddress.getPostcode(), newAddressDTO.getPostcode()) ||
                !Objects.equals(currentAddress.getCity(), newAddressDTO.getCity()) ||
                !Objects.equals(currentAddress.getCountry(), newAddressDTO.getCountry());

        Address addressToSet;

        if (addressChanged) {

            Address newAddress = new Address();
            newAddress.setStreet(newAddressDTO.getStreet());
            newAddress.setPostcode(newAddressDTO.getPostcode());
            newAddress.setCity(newAddressDTO.getCity());
            newAddress.setCountry(newAddressDTO.getCountry());
            addressToSet = addressRepository.save(newAddress);
        } else {
            addressToSet = currentAddress;
        }


        existingSupplier.setName(supplierDTO.getName());
        existingSupplier.setContactperson(supplierDTO.getContact());
        existingSupplier.setEmail(supplierDTO.getEmail());
        existingSupplier.setPhone(supplierDTO.getPhone());
        existingSupplier.setWebsite(supplierDTO.getWebsite());
        existingSupplier.setNotes(supplierDTO.getNotes());
        existingSupplier.setAddress(addressToSet);

        Supplier updated = supplierRepository.save(existingSupplier);
        return Optional.of(supplierMapper.toDTO(updated));
    }

    /**
     * Checks if a supplier with the given email exists.
     */
    public boolean emailExists(String email) {
        return supplierRepository.findByEmail(email).isPresent();
    }

    /**
     * Checks if a supplier with the given phone number exists.
     */
    public boolean phoneNumberExists(String phone) {
        return supplierRepository.findByPhone(phone).isPresent();
    }

    /**
     * Checks if a supplier with the given website exists.
     */
    public boolean websiteExists(String website) {
        return supplierRepository.findByWebsite(website).isPresent();
    }

    /**
     * Deletes a supplier by its ID.
     * param suppliernr The ID of the supplier to delete.
     * param employeenr The employee ID performing the deletion (for authorization).
     * return Optional containing the deleted SupplierDTO if deletion was successful, or empty if supplier not found.
     * throws IllegalArgumentException if employee not found.
     * throws SecurityException if employee is unauthorized to delete suppliers.
     */
    public Optional<SupplierDTO> deleteSupplier(Integer suppliernr, Integer employeenr) {

        Optional<Employee> employeeOpt = employeeRepository.findById(employeenr);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employee not found");
        }
        Employee employee = employeeOpt.get();
        System.out.println("Employee ID: " + employee.getEmployeenr() + ", Role: " + employee.getRole());

        if (employee.getRole() == null || employee.getRole().getRolenr() != 1) {
            throw new SecurityException("You are not authorized to delete suppliers");
        }

        Optional<Supplier> optionalSupplier = supplierRepository.findById(suppliernr);
        if (optionalSupplier.isEmpty()) {
            return Optional.empty();
        }
        Supplier supplier = optionalSupplier.get();

        if (productSupplierRepository.existsBySupplier_Suppliernr(suppliernr)) {
            throw new IllegalStateException("Cannot delete supplier because products are linked.");
        }

        supplierRepository.delete(supplier);
        SupplierDTO deletedSupplierDTO = convertToDTO(supplier);
        return Optional.of(deletedSupplierDTO);
    }
}


