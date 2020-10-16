import React from 'react';
import { Link, NavLink } from 'react-router-dom'; //Link depois funciona como o <a></a> dos links, a diferença do Navlink é que da para meter o activeClassName para o botao que for clicado permanecer ativo, a propria houter Navlink vai gerir isso
import './styles.scss';

const Navbar = () => (

    <nav className="row bg-primary main-nav">
        <div className="col-2">
            <Link to="/" className="nav-logo-text">
                <h4>DS Catalog</h4>
            </Link>

        </div>
        <div className="col-6 offset-2">
            <ul className="main-menu">
                <li>
                    <NavLink to="/" exact>
                        HOME
                    </NavLink>
                </li>
                <li>
                    <NavLink to="/products">
                        CATÁLOGO
                    </NavLink>
                </li>
                <li>
                    <NavLink to="/admin">
                        ADMIN
                    </NavLink>
                </li>


            </ul>
        </div>
    </nav>
);


export default Navbar;