import React from 'react';
import { Switch } from 'react-router-dom';
import Navbar from './Components/Navbar';
import Products from './Components/Products';
import './styles.scss';
import PrivateRoute from 'pages/Admin/Components/Routes/PrivateRoutes';


const Admin = () => (

    <div className="admin-container">
        <Navbar />

        <div className="admin-content">
            <Switch>
                <PrivateRoute path="/admin/products">
                    <Products />
                </PrivateRoute>
                <PrivateRoute path="/admin/categories">
                    <h1>Categories</h1>
                </PrivateRoute>
                <PrivateRoute path="/admin/users" allowedRoutes={['ROLE_ADMIN']}>
                    <h1>Users</h1>
                </PrivateRoute>
            </Switch>
        </div>
    </div>

);

export default Admin;