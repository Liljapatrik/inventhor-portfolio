// mockData

export const products = [
    { id: 1, name: 'Deluxe Camera', category: 'Tech', unit: 'pcs', buy_price: 1000, sell_price:2000,  product_picture: 'bi-camera', supplier_id: 1 },
    { id: 2, name: 'Monkey T-shirt', category: 'Clothes', unit: 'pcs', buy_price: 2000, sell_price: 3500, product_picture: 'bi-camera', supplier_id: 2 },
    { id: 3, name: 'Teddy Bear with mango', category: 'Toys', unit: 'pcs', buy_price: 3000, sell_price: 5000,  product_picture: 'bi-camera', supplier_id: 3 },
    { id: 4, name: 'Banana', category: 'Food', unit: 'kg', buy_price: 100, sell_price: 200,  product_picture: 'bi-camera', supplier_id: 1 },
    { id: 5, name: 'Monkey Hat', category: 'Clothes', unit: 'pcs', buy_price: 200, sell_price: 350, product_picture: 'bi-camera', supplier_id: 2 },
    { id: 6, name: 'Mango Teddy Bear', category: 'Toys', unit: 'pcs', buy_price: 300, sell_price: 500,  product_picture: 'bi-camera', supplier_id: 3 },
    { id: 7, name: 'Banana Cake', category: 'Food', unit: 'kg', buy_price: 100, sell_price: 200,  product_picture: 'bi-camera', supplier_id: 1 },
    { id: 8, name: 'Monkey Pants', category: 'Clothes', unit: 'pcs', buy_price: 200, sell_price: 350, product_picture: 'bi-camera', supplier_id: 2 },
    { id: 9, name: 'Mango Monkey', category: 'Toys', unit: 'pcs', buy_price: 300, sell_price: 500,  product_picture: 'bi-camera', supplier_id: 3 },
    { id: 10, name: 'Banana Ice Cream', category: 'Food', unit: 'kg', buy_price: 100, sell_price: 200,  product_picture: 'bi-camera', supplier_id: 1 },
    { id: 11, name: 'Monkey Shoes', category: 'Clothes', unit: 'pcs', buy_price: 200, sell_price: 350, product_picture: 'bi-camera', supplier_id: 2 },
    { id: 12, name: 'Mango Banana', category: 'Toys', unit: 'pcs', buy_price: 300, sell_price: 500,  product_picture: 'bi-camera', supplier_id: 3 }
  ];
  
export const orders = [
{ id: 1, customerName: 'Marte Staard', items: [1, 2], total: 3000, status: 'Picked', date: '2021-10-10', payment: 'Card' },
{ id: 2, customerName: 'Arne Gaard', items: [3], total: 3000, status: 'Shipped', date: '2024-11-11', payment: 'Vipps' },
{ id: 3, customerName: 'Ola Nordmann', items: [1, 2, 3], total: 6000, status: 'Delivered', date: '2021-12-12', payment: 'Cash' },
{ id: 4, customerName: 'Kari Nordmann', items: [1, 3], total: 4000, status: 'Ordered', date: '2022-01-01', payment: 'Card' },
];

export var suppliers = [
{ id: 1, name: 'Banana INC', contact: 'Bananmannen', email: 'banan@hotmail.com', phone: '12345678', website: 'www.banana.com', city: 'Oslo', postalCode: '1234', street:'Karl Johan vei', country: 'Norway', notes: 'Supplier of fresh bananas' },
{ id: 2, name: 'King Kong AS', contact: 'Anna Sørdame', email: 'kingkong@gmail.com', phone: '87654321', website: 'www.kingkong.com', city: 'Bergen', postalCode: '4321', street:'Kongeveien', country: 'Norway', notes: 'Supplier of monkey clothes' },
{ id: 3, name: 'Mingo Mango AS', contact: 'Ola Nordmann', email: 'tarmango@hotmail.com', phone: '12348765', website: 'www.mingomango.com', city: 'Trondheim', postalCode: '5678', street:'Mango veien', country: 'Norway', notes: 'Supplier of mango teddy bears' }
];

export const employees = [
{ id: 1, picture:'/images/UserIcon.svg', username: 'kimB', password: 'password1', firstName: 'Kim', lastName: 'Bolle', role: 'Administrator', position: 'Sale Manager', email: 'kimbinv@mail.mail', employeedDate: '2021-10-10', postalCode: '4766', street:'Karling vei', city: 'Oslo', country: 'Norway', isActive: true }, 
{ id: 2, picture:'/images/UserIcon.svg', username: 'bjornO', password: 'password2', firstName: 'bjorn', lastName: 'olavsen', role:'Employee', position: 'Warehouse employee', email: 'bjorno@mail.mail', employeedDate: '2021-11-14', postalCode: '4490', street:'Monkvei', city: 'Bergen', country: 'Norway', isActive: true },
{ id: 3, picture:'/images/UserIcon.svg', username: 'siljeM', password: 'password3', firstName: 'silje', lastName: 'mikkelsen', role: 'Administrator', position: 'Support manager', email: 'silinv@mail.mail', employeedDate: '2021-12-12', postalCode: '4321', street:'Ambravei', city: 'Hønefoss', country: 'Norway', isActive: false },
];

export const notifications = [
    { id: 1, type:'warning', message: 'low order ', date: '2021-10-10', isRead: false, isDeleted: false, email: [1] },
    { id: 2, type:'info', message: 'Order 2 is shipped', date: '2024-11-11', isRead: false, isDeleted: false, email: [0] },
    { id: 3, type:'success', message: 'Order 3 is delivered', date: '2021-12-12', isRead: false, isDeleted: false, email: [2] },
    { id: 4, type:'error', message: 'Order 4 is cancelled', date: '2022-01-01', isRead: false, isDeleted: false, email: [2] },
];

export const priceTrend = [
{
    product_id: 1,
    month: "Jan",
    amount_money_past_year: 3000,
    amount_money_current_year: 8000
},

{
    product_id: 1,
    month: "Feb",
    amount_money_past_year: 1000,
    amount_money_current_year: 2000
},

{
    product_id: 1,
    month: "Mar",
    amount_money_past_year: 2000,
    amount_money_current_year: 9000
},
{
    product_id: 1,
    month: "Apr",
    amount_money_past_year: 5000,
    amount_money_current_year: 9000
},

{
    product_id: 1,
    month: "May",
    amount_money_past_year: 2000,
    amount_money_current_year: 8000
},

{
    product_id: 1,
    month: "Jun",
    amount_money_past_year: 6000,
    amount_money_current_year: 3000
},

{
    product_id: 1,
    month: "Jul",
    amount_money_past_year: 7000,
    amount_money_current_year: 4000
},

{
    product_id: 1,
    month: "Aug",
    amount_money_past_year: 8000,
    amount_money_current_year: 5000
},

{
    product_id: 1,
    month: "Sep",
    amount_money_past_year: 9000,
    amount_money_current_year: 6000
},
{
    product_id: 1,
    month: "Oct",
    amount_money_past_year: 10000,
    amount_money_current_year: 7000
},

{
    product_id: 1,
    month: "Nov",
    amount_money_past_year: 11000
},

{
    product_id: 1,
    month: "Dec",
    amount_money_past_year: 13000
},
];

export const warehouseInfo = [
{
    warehouse_id: 1,
    warehouse_name: 'Warehouse 1',
    address: 'Ambravei 1, 1234 Hønefoss'
},

{
    warehouse_id: 2,
    warehouse_name: 'Warehouse 2',
    address: 'Kongeveien 1, 4321 Bergen'
},

{
    warehouse_id: 3,
    warehouse_name: 'Warehouse 3',
    address: 'Karl Johan vei 1, 1234 Oslo'
},
];

export const warehouseProductInfo = [
    {warehouse_id: 1, product_id: 1, w_quantity: 0, location: 'R3 P104'},
    {warehouse_id: 1, product_id: 2, w_quantity: 5 , location: 'R1 P307'},
    {warehouse_id: 1, product_id: 3, w_quantity: 5, location: 'R2 P104'},
    {warehouse_id: 2, product_id: 1, w_quantity: 5, location: 'R1 P104'},
    {warehouse_id: 2, product_id: 2, w_quantity: 3, location: 'R3 P504'},
    {warehouse_id: 2, product_id: 3, w_quantity: 2, location: 'R2 P610'},
    {warehouse_id: 3, product_id: 3, w_quantity: 15, location: 'R1 P204'},
    {warehouse_id: 3, product_id: 2, w_quantity: 7, location: 'R3 P307'},
];

export const sellingTrend = [
{
    product_id: 1,
    month: "Jan",
    amount_past_year: 300,
    amount_current_year: 800,
},

{
    product_id: 1,
    month: "Feb",
    amount_past_year: 100,
    amount_current_year: 200,
},

{
    product_id: 1,
    month: "Mar",
    amount_past_year: 200,
    amount_current_year: 900,
},

{
    product_id: 1,
    month: "Apr",
    amount_past_year: 500,
    amount_current_year: 900,
},

{ 
    product_id: 1,
    month: "May",
    amount_past_year: 200,
    amount_current_year: 800,
},

{
    product_id: 1,
    month: "Jun",
    amount_past_year: 600,
    amount_current_year: 300,
},

{
    product_id: 1,
    month: "Jul",
    amount_past_year: 700,
    amount_current_year: 400
},

{
    product_id: 1,
    month: "Aug",
    amount_past_year: 800,
    amount_current_year: 500,
},

{
    product_id: 1,
    month: "Sep",
    amount_past_year: 900,
    amount_current_year: 600,
},

{
    product_id: 1,
    month: "Oct",
    amount_past_year: 1000,
    amount_current_year: 700,
},

{
    product_id: 1,
    month: "Nov",
    amount_past_year: 1100,
    amount_current_year: null
},

{
    product_id: 1,
    month: "Dec",
    amount_past_year: 1200,
    amount_current_year: null
}

];

export const totalCustomers = [
{
    month: "Jan",
    amount_customers_past_year: 10000,
    amount_customers_current_year: 20000
},

{
    month: "Feb",
    amount_customers_past_year: 11000,
    amount_customers_current_year: 21000
},

{
    month: "Mar",
    amount_customers_past_year: 12000,
    amount_customers_current_year: 22000
},

{
    month: "Apr",
    amount_customers_past_year: 13000,
    amount_customers_current_year: 23000
},

{
    month: "May",
    amount_customers_past_year: 14000,
    amount_customers_current_year: 24000
},

{
    month: "Jun",
    amount_customers_past_year: 15000,
    amount_customers_current_year: 25000
},

{
    month: "Jul",
    amount_customers_past_year: 16000,
    amount_customers_current_year: 26000
},

{
    month: "Aug",
    amount_customers_past_year: 17000,
    amount_customers_current_year: 27000
},

{
    month: "Sep",
    amount_customers_past_year: 18000,
    amount_customers_current_year: 28000
}


];

export const totalSales = [
{
    month: "Jan",
    amount_money_past_year: 133300,
    amount_money_current_year: 567800
},

{
    month: "Feb",
    amount_money_past_year: 137500,
    amount_money_current_year: 576700
},

{
    month: "Mar",
    amount_money_past_year: 140000,
    amount_money_current_year: 589000
},

{
    month: "Apr",
    amount_money_past_year: 145000,
    amount_money_current_year: 600000
},

{
    month: "May",
    amount_money_past_year: 150000,
    amount_money_current_year: 610000
},

{
    month: "Jun",
    amount_money_past_year: 155000,
    amount_money_current_year: 620000
},

{
    month: "Jul",
    amount_money_past_year: 160000,
    amount_money_current_year: 630000
},

{
    month: "Aug",
    amount_money_past_year: 165000,
    amount_money_current_year: 640000
},

{
    month: "Sep",
    amount_money_past_year: 170000,
    amount_money_current_year: 650000
},

{
    month: "Oct",
    amount_money_past_year: 175000,
    amount_money_current_year: 660000
},

{
    month: "Nov",
    amount_money_past_year: 180000,
    amount_money_current_year: 670000
},

{
    month: "Dec",
    amount_money_past_year: 185000,
    amount_money_current_year: 680000
}

];