import React from 'react';
import {render, screen} from '@testing-library/react';
import Home from '..';
import { Router } from 'react-router-dom';
import history from 'core/utils/history';

test('should render Home', () => {
    //nestes caso temos de importar ao menos as rotas porque ele tem links dentro
    render(
        <Router history={history} >
            <Home />
        </Router>
    );

    //screen.debug();
    const titleElement = screen.getByText('Conheça o melhor catálogo de produtos');
    const subTitleElement = screen.getByText('Ajudaremos você a encontrar os melhores produtos disponíveis no mercado.');

    expect(titleElement).toBeInTheDocument();
    expect(subTitleElement).toBeInTheDocument();
    expect(screen.getByTestId('main-image')).toBeInTheDocument();
    expect(screen.getByText(/INICIA AGORA A SUA PESQUISA/i)).toBeInTheDocument(); //ignora se está em maiuscula ou minuscula, porque ele é renderizado em minusculas porque é o css que o coloca em maiusculas entao para testar tal como ta la colocamos assim
});