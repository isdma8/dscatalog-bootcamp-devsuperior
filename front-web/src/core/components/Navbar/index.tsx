import { getAccessTokenDecoded, logout } from 'core/utils/auth';
import React, { useEffect, useState } from 'react';
import { Link, NavLink, useLocation } from 'react-router-dom'; //Link depois funciona como o <a></a> dos links, a diferença do Navlink é que da para meter o activeClassName para o botao que for clicado permanecer ativo, a propria houter Navlink vai gerir isso
import './styles.scss';


const Navbar = () => {

    const [currentUser, setCurrentUser] = useState('');

    const location = useLocation();

    useEffect(() => {//sempre que existe uma mudança de rota nos capturamos e verificamos sempre se ainda tem acesso o user, ou seja o token
        const currentUserData = getAccessTokenDecoded();
        setCurrentUser(currentUserData.user_name);

    }, [location])

    const handleLogout = (event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) => {
        event.preventDefault();
        logout();
    }

return (

    <nav className="row bg-primary main-nav">
        <div className="col-3">
            <Link to="/" className="nav-logo-text">
                <h4>DS Catalog</h4>
            </Link>

        </div>
        <div className="col-6">
            <ul className="main-menu">
                <li>
                    <NavLink to="/" exact className="nav-link">
                        HOME
                    </NavLink>
                </li>
                <li>
                    <NavLink to="/products" className="nav-link">
                        CATÁLOGO
                    </NavLink>
                </li>
                <li>
                    <NavLink to="/admin" className="nav-link">
                        ADMIN
                    </NavLink>
                </li>


            </ul>
        </div>
        <div className="col-3 text-right" >
            {currentUser && (
                <>
                    {currentUser}
                    <a 
                        href="#logout" 
                        className="nav-link active d-inline"
                        onClick={handleLogout}
                    >
                        LOGOUT
                    </a>
                </> 
            )
            }
            {!currentUser && (
                <Link to="/auth/login" className="nav-link active">
                    LOGIN
                </Link>
            )}

        </div>
    </nav>
)};


export default Navbar;