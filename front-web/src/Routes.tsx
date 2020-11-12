import React from 'react';
import { Router, Switch, Route, Redirect } from 'react-router-dom'; //instalar primeiro com o yarn, BrowserRouter este vai gerencias as nossas rotas, e o swith vai decidir qual rota deve usar
import Navbar from './core/components/Navbar';
import Admin from './pages/Admin';
import Catalog from './pages/Catalog';
import ProductDetails from './pages/Catalog/components/ProductDetails';
import Home from './pages/Home';
import Auth from './pages/Auth';
import history from './core/utils/history';
                                                //importamos tmabem o route é onde definimos de facto a url de cada rota
const Routes = () => (
        //<BrowserRouter> Agora iremos gerenciar nos com o Router chamando o history que criamos, assim ja podemos fazer redirecionamentos em arquivos ts e nao so em componentes, e nos ts eu nao posso usar os react hooks
        <Router history={history}> 
            <Navbar />
            <Switch>
                    <Route path="/" exact>
                        <Home />
                    </Route>
                    <Route path="/products" exact>
                        <Catalog />
                    </Route>
                    <Route path="/products/:productId">
                        <ProductDetails />
                    </Route>
                    <Route path="/auth">
                        <Auth />
                    </Route>
                    <Redirect from="/admin" to="/admin/products" exact/>
                    <Route path="/admin">
                        <Admin />
                    </Route>
            </Switch>

        </Router>
        //</BrowserRouter>

);


export default Routes;