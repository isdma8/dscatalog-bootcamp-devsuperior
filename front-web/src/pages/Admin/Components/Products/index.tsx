import React from 'react';
import List from './List';
import { Route, Switch } from 'react-router-dom';
import Form from './Form';

const Products = () => {

    return(
        <div>
            
            <Switch>
                <Route path="/admin/products" exact>
                    <List />
                </Route>
                <Route path="/admin/products/:productId">
                    <Form />
                </Route>
            </Switch>
        </div>
    );
}

export default Products;



/*<Link to="/admin/products" className="mr-5">
                Listar Produtos
            </Link>
            <Link to="/admin/products/create" className="mr-5">
                Criar produto
            </Link>
            <Link to="/admin/products/10" className="mr-5">
                Editar produto
            </Link> Servia para testar as rotas, é como ter a navbar*/ 