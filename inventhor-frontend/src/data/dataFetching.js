// dataFetching
import { products, orders, suppliers, employees, notifications, priceTrend, warehouseProductInfo, sellingTrend, totalCustomers, totalSales, warehouseInfo } from './mockData';

export const getProducts = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(products), 500);  
  });
};

export const addProduct = (product) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      const newProduct = {...product, id: products.length + 1};
      products.push(newProduct);
      resolve(newProduct);
    }, 500);
  });
};

export const getPriceTrend = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(priceTrend), 500);  
  });
};

export const getAllWarehouses = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(warehouseInfo), 500);
  });
};

export const getWarehouseInfo = (warehouseId) => {
  return new Promise((resolve) => {
    setTimeout(() => {

      let warehouseInfoForWarehouse = warehouseInfo.find(item => item.warehouse_id == warehouseId);
      resolve([warehouseInfoForWarehouse]);
    
    }, 500);  
  });
}

export const getWarehouseProductInfo = (productId) => {
  return new Promise((resolve) => {

    setTimeout(() => {
      // backend emulator

      let warehouseProductInfoForProduct = warehouseProductInfo.filter(item => item.product_id == productId);
      
      warehouseProductInfoForProduct.forEach(item => {
        item.warehouse_name = warehouseInfo.find(warehouse => warehouse.warehouse_id === item.warehouse_id).warehouse_name;
      });

      resolve(warehouseProductInfoForProduct);

    }, 500); 

  });
};

export const getProductsForWarehouse = (warehouseId) => {
  return new Promise((resolve) => {
    setTimeout(() => {

      
      let warehouseProductInfoForWarehouse = warehouseProductInfo.filter(item => item.warehouse_id == warehouseId);
      
      warehouseProductInfoForWarehouse.forEach(item => {

        let product = products.find(product => product.id == item.product_id);
        item.product_picture = product.product_picture;
        item.name = product.name;
        item.unit = product.unit;
      });


      resolve(warehouseProductInfoForWarehouse);
    }, 500);
  });
}

export const getSellingTrend = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(sellingTrend), 500);  
  });
};

export const getTotalSales = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(totalSales), 500);  
  });
};

export const getOrders = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(orders), 500);
  });  
};

export const getSuppliers = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(suppliers), 500);
  });
};

export const getSuppliersProduct = (supplierId) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      let supplierProducts = products.filter(product => product.supplier_id == supplierId);
      resolve(supplierProducts);
    }, 500);
  });
};

export const addSupplier = (newSupplier)=> {
  suppliers.push(newSupplier);
};


export const getEmployees = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(employees), 500);  
  });
};

export const addEmployee = (newEmployee)=> {
  employees.push(newEmployee);
};


export const getNotifications = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(notifications), 500);  
  });
};


export const getTotalCustomers = () => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(totalCustomers), 500);  
  });
};
