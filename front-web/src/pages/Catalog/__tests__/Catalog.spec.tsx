import React from 'react';
import {render, screen, waitFor} from '@testing-library/react';
import { rest } from 'msw';
import { setupServer } from 'msw/node';
import { Router } from 'react-router-dom';
import history from 'core/utils/history';
import Catalog from '..';
import { productsResponse } from './fixtures';


//Necessario instalar [yarn add msw]



//Para mockar o backend, não podemos depender dele, quando é feita uma requisição é apanhada aqui
const server = setupServer(
    rest.get('http://localhost:8090/products', (req, res, ctx) => { //quando houver uma requisição deste tipo ele tem de reponder na linha abaixo com uma lista mockada do tipo
      return res(ctx.json(productsResponse))
    })
  );
  
  beforeAll(() => server.listen()); //utilitarios do jest, este executa antes de todos os testes e o msw entra a estar a escuta de requisições 
  afterEach(() => server.resetHandlers());
  afterAll(() => server.close());

test('should render ProductCard', async () => { //para ter o await precisamos do async, e precisamos do await porque queremos pegar o texto de uma operação assincrona, nos não sabemos quando ele fica disponivel porque ele é pedido a outro compomente ou bd etc
//e precisamos ainda instlaar o [yarn add mutationobserver-shim] porque usamos o react hook form e lá é dito que se não o fizermos isto não funciona com ele.
//e no setupTests.ts colocar import 'mutationobserver-shim';

    render(
        <Router history={history} >
            <Catalog /> 
        </Router> 
    );

    expect(screen.getByText('Catálogo de Produtos')).toBeInTheDocument();
    expect(screen.getAllByTitle('Loading...')).toHaveLength(4);//porque sao 4 elementos com mesmo texto
    

    await waitFor(() => expect(screen.getByText('Macbook Pro')).toBeInTheDocument());
    expect(screen.getByText('PC Gamer')).toBeInTheDocument(); // basta o primeiro wait para segurar ate aparecer depois ja nao precisa
    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('2')).toBeInTheDocument();
    expect(screen.queryAllByTitle('Loading...')).toHaveLength(0);//Não quero loading depois, nao podemos usar o get porque ele quebra sempre que nao encontra entao nunca passava, temos de usar o query

    
});