import { isAllowedByRole, isAuthenticated, Role } from 'core/utils/auth';
import React from 'react';
import { Redirect, Route } from 'react-router-dom';

type Props = {
    children: React.ReactNode;
    path: string;
    allowedRoutes?: Role[];
}


const PrivateRoute = ({ children, path, allowedRoutes }: Props) => {

    return (
      <Route   //vou renderizar algo nesta rota, se estiver autenticado rederizo o children senao rerireciono para onde quiser
        path={path}
        render={({ location }) => {

          if(!isAuthenticated()){
            return(
              <Redirect
                to={{
                  pathname: "/admin/auth/login",
                  state: { from: location }
                }}
              />
            )
          }else if( isAuthenticated() && !isAllowedByRole(allowedRoutes)){
            return(
              <Redirect
                to={{
                  pathname: "/admin" //ta autenticado mas nao é admin, tem outra ROLE este user
                }}
              />
            )
          }

          return children; 

        }}
      />
    );
  }

  export default PrivateRoute;