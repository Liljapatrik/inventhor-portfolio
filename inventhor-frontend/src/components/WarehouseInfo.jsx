import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getWarehouses, getLocationProductsByWarehouse, getProducts, getLocations, addProductToLocation, updateLocationProduct, deleteLocationProduct } from '../data/ServerData';
import './warehouseInfo.css';

/**
 * @Author Tatiana Fløisbonn
 * @Author Steewen Dennis Chanavi Holden
 *
 * Displays information about a warehouse,including its address and a table of all rack/place locations.
 *
 * Fetches warehouse info, locations, products, and location-product mappings from the backend.
 * Lets users view, search, and sort all product locations.
 * Allows adding, editing, and deleting products at specific warehouse locations via modals.
 * Handles loading and empty/error states.
 *
 * CSS styling is found in 'warehouseInfo.css'
 */

function WarehouseInfo() {
  const { id } = useParams(); // Get the warehouse ID from the URL parameters
  const navigate = useNavigate(); // Initialize the navigate function for navigation

  const [warehouseInfo, setWarehouseInfo] = useState(null);
  const [locationProducts, setLocationProducts] = useState([]);
  const [allLocations, setAllLocations] = useState([]);
  const [allProducts, setAllProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  // Modal states for editing and adding products
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedLocation, setSelectedLocation] = useState(null);

  // Modal state for delete message
  const [isDeleteMessageOpen, setIsDeleteMessageOpen] = useState(false);

  // Modal state for adding new product to location
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [newLocationProduct, setNewLocationProduct] = useState({
    productnr: '',
    quantity: '',
    racknr: '',
    placenr: ''
  });

  let [search, setSearch] = useState("");
  let [sort, setSort] = useState("locationString");

  // Modal states for success and error
  const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);
  const [isErrorModalOpen, setIsErrorModalOpen] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  // Fetch data when component mounts or id changes
  useEffect(() => {
    async function fetchData() {
      // Reset states before fetching new data
      try {
        setLoading(true);

        // Get all products
        const productsData = await getProducts();
        console.log('Fetched products:', productsData);
        setAllProducts(productsData);

        // Get warehouse info
        const warehousesData = await getWarehouses();
        const warehouse = warehousesData.find(w => w.warehousenr === parseInt(id));
        setWarehouseInfo(warehouse);

        // Get all locations for this warehouse
        const locationsData = await getLocations();
        const warehouseLocations = locationsData.filter(loc => loc.warehousenr === parseInt(id));
        console.log('Warehouse locations:', warehouseLocations);
        setAllLocations(warehouseLocations);

        // Get location products for this warehouse
        const locationProductsData = await getLocationProductsByWarehouse(id);
        console.log('Fetched location products for warehouse', id, ':', locationProductsData);
        setLocationProducts(locationProductsData);

        setLoading(false);
      } catch (error) {
        console.error('Error fetching data:', error);
        setLoading(false);
      }
    }

    fetchData();
  }, [id]);

  // Transform location products data for console logs
  const getLocationTableData = () => {
    console.log('All locations:', allLocations);
    console.log('Location products:', locationProducts);
    console.log('All products:', allProducts);

    // Create a map of location products
    const locationProductMap = {};
    locationProducts.forEach(lp => {
      const key = `${lp.warehousenr}-${lp.racknr}-${lp.placenr}`;
      locationProductMap[key] = lp;
    });

    // Map all locations
    return allLocations.map(location => {
      const key = `${location.warehousenr}-${location.racknr}-${location.placenr}`;
      const locationProduct = locationProductMap[key];

      let productnr = null;
      let productName = 'Ledig';
      let productImage = null;
      let productUnit = null;
      let quantity = 0;

      if (locationProduct && locationProduct.product) {
        productnr = locationProduct.product.productnr;
        productName = locationProduct.product.name;
        productImage = locationProduct.product.image;
        productUnit = locationProduct.product.unit;
        quantity = locationProduct.quantity || 0;
      }

      return {
        ...location,
        ...locationProduct,
        productnr: productnr,
        productName: productName,
        productImage: productImage,
        productUnit: productUnit,
        quantity: quantity,
        locationString: `${location.racknr}-${location.placenr}`
      };
    });
  };

  {/* 
    The handleEditClick function sets the selected product and opens the modal.
    The handleModalClose function closes the modal and resets the selected product.
    The handleFormSubmit function updates the w_quantity and location fields of the selected product and closes the modal.
    The handleInputChange function updates the state of the selected product as the user types in the input fields.
    The modal is styled using CSS to appear as a popup window.
  */}

  const handleEditClick = (location) => {
    setSelectedLocation(location);
    setIsModalOpen(true);
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
    setSelectedLocation(null);
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      // Call API to update location product
      const updateData = {
        warehousenr: selectedLocation.warehousenr,
        racknr: selectedLocation.racknr,
        placenr: selectedLocation.placenr,
        productnr: selectedLocation.productnr,
        quantity: parseFloat(selectedLocation.quantity)
      };

      await updateLocationProduct(updateData);

      // Refresh the location products data
      const updatedLocationProducts = await getLocationProductsByWarehouse(id);
      setLocationProducts(updatedLocationProducts);

      handleModalClose();
      setSuccessMessage('Quantity updated successfully.');
      setIsSuccessModalOpen(true);
    } catch (error) {
      console.error('Error updating location product:', error);
      setErrorMessage('Error updating quantity. Please try again.');
      setIsErrorModalOpen(true);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSelectedLocation(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  {/* 
    Delete handler function: checks the quantity and deletes the product if the quantity is 0.
    If the quantity is not 0, it opens a modal with a message.
  */}

  const handleDeleteClick = async (location) => {
    // Check if it's an empty location (no product)
    if (!location.productnr) {
      setErrorMessage('Cannot delete an empty location. This location is already available.');
      setIsErrorModalOpen(true);
      return;
    }

    // Check if quantity is not 0
    if (location.quantity > 0) {
      setErrorMessage('Cannot remove product from location, must be empty. Current quantity: ' + location.quantity);
      setIsErrorModalOpen(true);
      return;
    }

    // If there's a product but quantity is 0, proceed with deletion
    try {
      const deleteData = {
        warehousenr: location.warehousenr,
        racknr: location.racknr,
        placenr: location.placenr,
        productnr: location.productnr
      };

      await deleteLocationProduct(deleteData);

      // Refresh the location products data
      const updatedLocationProducts = await getLocationProductsByWarehouse(id);
      setLocationProducts(updatedLocationProducts);

      setSuccessMessage('Product removed from location successfully.');
      setIsSuccessModalOpen(true);
    } catch (error) {
      console.error('Error deleting location product:', error);
      setErrorMessage('Error removing product from location. Please try again.');
      setIsErrorModalOpen(true);
    }
  };

  const handleDeleteMessageClose = () => {
    setIsDeleteMessageOpen(false);
  };

  {/* 
    Add product modal handling
  */}

  const handleAddProductClick = () => {
    setIsAddModalOpen(true);
  };

  const handleAddModalClose = () => {
    setIsAddModalOpen(false);
    setNewLocationProduct({ productnr: '', quantity: '', racknr: '', placenr: '' });
  };

  const handleAddFormSubmit = async (e) => {
    e.preventDefault();
    try {
      // Check if location is already occupied
      const locationData = getLocationTableData();
      const existingLocation = locationData.find(loc =>
          loc.racknr === parseInt(newLocationProduct.racknr) &&
          loc.placenr === parseInt(newLocationProduct.placenr)
      );

      if (existingLocation && existingLocation.productnr) {
        setErrorMessage('This location is already occupied. You must first delete the product from this location.');
        setIsErrorModalOpen(true);
        return;
      }

      const locationProductData = {
        warehousenr: parseInt(id),
        racknr: parseInt(newLocationProduct.racknr),
        placenr: parseInt(newLocationProduct.placenr),
        product: {
          productnr: parseInt(newLocationProduct.productnr)
        },
        quantity: parseFloat(newLocationProduct.quantity)
      };

      // Call API to add product to location
      await addProductToLocation(locationProductData);

      // Refresh both locations and location products
      const locationsData = await getLocations();
      const warehouseLocations = locationsData.filter(loc => loc.warehousenr === parseInt(id));
      setAllLocations(warehouseLocations);

      const updatedLocationProducts = await getLocationProductsByWarehouse(id);
      setLocationProducts(updatedLocationProducts);

      handleAddModalClose();
      setSuccessMessage('Product added to location successfully.');
      setIsSuccessModalOpen(true);
    } catch (error) {
      console.error('Error adding product to location:', error);
      setErrorMessage('Error adding product to location. Please try again.');
      setIsErrorModalOpen(true);
    }
  };

  const handleNewProductChange = (e) => {
    const { name, value } = e.target;
    setNewLocationProduct(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  {/*Search for table*/}

  const handleSearchChange = (e) => {
    setSearch(e.target.value);
  };

  {/*Sort for table*/}

  const handleSortChange = (e) => {
    setSort(e.target.value);
  };

  // Modal close handlers
  const handleSuccessModalClose = () => {
    setIsSuccessModalOpen(false);
  };
  const handleErrorModalClose = () => {
    setIsErrorModalOpen(false);
  };

  // Get sorted and filtered locations
  const getSortedFilteredLocations = () => {
    const locationData = getLocationTableData();

    // Filter based on search
    const filtered = locationData.filter(location => {
      const searchTerm = search.toLowerCase();
      return (
          location.locationString.toLowerCase().includes(searchTerm) ||
          location.productName.toLowerCase().includes(searchTerm) ||
          (location.productnr && location.productnr.toString().includes(searchTerm))
      );
    });

    // Sort based on selected option
    return filtered.sort((a, b) => {
      if (sort === "locationString") {
        // Sort by rack then place
        if (a.racknr !== b.racknr) {
          return a.racknr - b.racknr;
        }
        return a.placenr - b.placenr;
      } else if (sort === "productnr") {
        // Sort by product number (empty locations last)
        if (!a.productnr) return 1;
        if (!b.productnr) return -1;
        return a.productnr - b.productnr;
      } else if (sort === "productName") {
        // Sort by product name (Ledig last)
        if (a.productName === "Ledig") return 1;
        if (b.productName === "Ledig") return -1;
        return a.productName.localeCompare(b.productName);
      } else if (sort === "quantity") {
        return a.quantity - b.quantity;
      }
      return 0;
    });
  };

  if (loading) {
    return <div className="loading">Loading warehouse information...</div>;
  }

  return (
      <div className="warehouse-info-container">
        <div className='warehouse-info'>
          {warehouseInfo && (
              <>
                <h1>{warehouseInfo.name}</h1>
                <div>
                  <h4>Address:</h4>
                  <p>
                    {warehouseInfo.address ?
                        `${warehouseInfo.address.street}, ${warehouseInfo.address.postcode}, ${warehouseInfo.address.city}, ${warehouseInfo.address.country}`
                        : 'N/A'}
                  </p>
                </div>
              </>
          )}
        </div>

        <div className='warehouse-stock'>
          <div className="table-container">
            <h3 className='mt-1'>Warehouse Locations</h3>

            <div className="tableFunctions">
              <div className="tableSeachbar mb-3">
                <i className="bi bi-search"></i>
                <input
                    className="tableSeachInput"
                    type="text"
                    placeholder="Search"
                    onChange={handleSearchChange}
                    value={search}
                />
              </div>

              <div className="sortWithAddFunctions">
                {/* Sort dropdown */}
                <select className="tableSort" onChange={handleSortChange} value={sort}>
                  <option value="locationString">Location</option>
                  <option value="productnr">Product ID</option>
                  <option value="productName">Product Name</option>
                  <option value="quantity">Quantity</option>
                </select>

                {/* Add Product button */}
                <button className="addBtn" onClick={handleAddProductClick}>
                  Add Product to Location
                </button>
              </div>
            </div>

            <table>
              <thead>
              <tr>
                <th>Product ID</th>
                <th>Image</th>
                <th>Name</th>
                <th>Quantity</th>
                <th>Unit</th>
                <th>Location</th>
                <th></th>
                <th></th>
              </tr>
              </thead>

              <tbody>
              {getSortedFilteredLocations().map((location, index) => (
                  <tr key={`${location.warehousenr}-${location.racknr}-${location.placenr}-${location.productnr || index}`}>
                    <td>{location.productnr || '-'}</td>
                    <td>
                      {location.productImage ? (
                          <img
                              src={location.productImage}
                              alt={location.productName}
                              style={{ width: '50px', height: '50px', objectFit: 'cover' }}
                          />
                      ) : (
                          <span>-</span>
                      )}
                    </td>
                    <td>
                      {location.productnr ? (
                          <a href={`/inventory/product-details/${location.productnr}`}>
                            {location.productName}
                          </a>
                      ) : (
                          <span style={{color: '#28a745', fontStyle: 'italic'}}>Ledig</span>
                      )}
                    </td>
                    <td>{location.quantity}</td>
                    <td>{location.productUnit || '-'}</td>
                    <td>{location.locationString}</td>
                    <td>
                      <button
                          className="table-btn edit-btn"
                          onClick={() => handleEditClick(location)}
                          disabled={!location.productnr}
                      >
                        <i className="bi bi-pencil-square"></i>
                      </button>
                    </td>
                    <td>
                      {/*
                      <button
                          className="table-btn delete-btn"
                          onClick={() => handleDeleteClick(location)}
                          disabled={!location.productnr && location.quantity > 0}
                      >
                        <i className="bi bi-trash"></i>
                      </button>
                      */}
                    </td>
                  </tr>
              ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Edit product modal */}
        {isModalOpen && selectedLocation && (
            <div className="modal">
              <div className="modal-content">
                <span className="close" onClick={handleModalClose}>&times;</span>
                <h2>Edit Quantity</h2>
                <form onSubmit={handleFormSubmit}>
                  <div className="form-group">
                    <label>Product: {selectedLocation.productName}</label>
                  </div>
                  <div className="form-group">
                    <label>Location: {selectedLocation.locationString}</label>
                  </div>
                  <div className="form-group"style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <label htmlFor="quantity">Quantity:</label>
                    <input
                        type="number"
                        id="quantity"
                        name="quantity"
                        value={selectedLocation.quantity}
                        onChange={handleInputChange}
                        min="1"
                        step="1"
                    />
                  </div>
                  <button type="submit">Save</button>
                </form>
              </div>
            </div>
        )}

        {/* Delete message modal */}
        {isDeleteMessageOpen && (
            <div className="modal">
              <div className="modal-content">
                <span className="close" onClick={handleDeleteMessageClose}>&times;</span>
                <h2>Delete Error</h2>
                <p>The product cannot be deleted until the quantity is 0.</p>
                <button className='deleteErrorBtn' onClick={handleDeleteMessageClose}>Close</button>
              </div>
            </div>
        )}

        {/* Add product modal */}
        {isAddModalOpen && (
            <div className="modal">
              <div className="modal-content">
                <span className="close" onClick={handleAddModalClose}>&times;</span>
                <h2>Add Product to Location</h2>
                <p style={{marginBottom: '20px', color: '#666'}}>
                  Select a location and product to add to this warehouse
                </p>
                <form onSubmit={handleAddFormSubmit}>
                  <div className="form-group">
                    <label htmlFor="racknr">Rack Number:</label>
                    <input
                        type="number"
                        id="racknr"
                        name="racknr"
                        value={newLocationProduct.racknr}
                        onChange={handleNewProductChange}
                        placeholder=" rack nr (1, 2, 3)"
                        required
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="placenr">Place Number:</label>
                    <input
                        type="number"
                        id="placenr"
                        name="placenr"
                        value={newLocationProduct.placenr}
                        onChange={handleNewProductChange}
                        placeholder="place nr (101, 201, 301)"
                        required
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="productnr">Product:</label>
                    <select
                        id="productnr"
                        name="productnr"
                        value={newLocationProduct.productnr}
                        onChange={handleNewProductChange}
                        required
                        style={{padding: '8px', fontSize: '14px'}}
                    >
                      <option value="">-- Select a product --</option>
                      {allProducts.map(product => (
                          <option key={product.productnr} value={product.productnr}>
                            #{product.productnr} - {product.name}
                          </option>
                      ))}
                    </select>
                  </div>
                  <div className="form-group">
                    <label htmlFor="quantity">Quantity:</label>
                    <input
                        type="number"
                        id="quantity"
                        name="quantity"
                        value={newLocationProduct.quantity}
                        onChange={handleNewProductChange}
                        min="1"
                        step="1"
                        placeholder="quantity"
                        required
                    />
                    <small style={{color: '#666', display: 'block', marginTop: '5px'}}>
                      The amount of this product to place at this location
                    </small>
                  </div>
                  <div style={{display: 'flex', gap: '10px', marginTop: '20px'}}>
                    <button type="submit" style={{flex: 1}}>Add Product</button>
                    <button type="button" onClick={handleAddModalClose} style={{flex: 1, backgroundColor: '#6c757d'}}>
                      Cancel
                    </button>
                  </div>
                </form>
              </div>
            </div>
        )}

        {/* Success Modal */}
        {isSuccessModalOpen && (
          <div className="t-modal">
            <div className="t-modal-content">
              <span className="t-close" onClick={handleSuccessModalClose}>
                &times;
              </span>
              <h2>Success</h2>
              <p>{successMessage}</p>
              <button className="t-deleteErrorBtn" onClick={handleSuccessModalClose}>
                Close
              </button>
            </div>
          </div>
        )}

        {/* Error Modal */}
        {isErrorModalOpen && (
          <div className="t-modal">
            <div className="t-modal-content">
              <span className="t-close" onClick={handleErrorModalClose}>
                &times;
              </span>
              <h2>Error</h2>
              <p>{errorMessage}</p>
              <button className="t-deleteErrorBtn" onClick={handleErrorModalClose}>
                Close
              </button>
            </div>
          </div>
        )}
      </div>
  );
}

export default WarehouseInfo;