import React from 'react';
import {render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Form from '../Form';
import { Router } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import selectEvent from 'react-select-event'
import { rest } from 'msw';
import { setupServer } from 'msw/node';
import history from 'core/utils/history';
import { categoriesResponse } from './fixtures';

////[yarn add react-select-event]

//mocar uma biblioteca porque para testar o formulario presisamos indicar se estamos a criar ou editar o formulario, quando criamos no link nao tem id, apenas create, se tiver algo diferente é editar
jest.mock('react-router-dom', () => ({ //assim tou a criar objeto dentro do retorno, quero retornar objeto
    ...jest.requireActual('react-router-dom'), //retorna instancia original do react-router-dom, porque nos temos de colocar toda a biblioteca mas como so alteramos o productId abaixo queremos que tudo o rsto seja igual 
    //... junta o useParams com tudo o resto, mantenho a implementação original do react-router-dom, somente alterando o useParams
    useParams: () => ({
        productId: 'create' //porque quero simular e testar a criação de produto, se aqui meter um numero ele ja vai entende que é o editar se meter um screen.debug por exemplo ja irei ver editar
    })
}));

const server = setupServer(
    rest.get('http://localhost:8090/categories', (req, res, ctx) => { //quando houver uma requisição deste tipo ele tem de reponder na linha abaixo com uma lista mockada do tipo
      return res(ctx.json(categoriesResponse))
    }),
    rest.post('http://localhost:8090/products', (req, res, ctx) => {
      return res(ctx.status(201)) //o estado que tem de retornar quando cria produto com sucesso é o mesmo que o backend retornaria
    })//mock simular cenario em que deu sucesso
  );
  
  beforeAll(() => server.listen()); //utilitarios do jest, este executa antes de todos os testes e o msw entra a estar a escuta de requisições 
  afterEach(() => server.resetHandlers());
  afterAll(() => server.close());

test('should render Form', async () => {

    render(
        <Router history={history} >
            <ToastContainer /> 
            <Form />
        </Router>
    );

    const submitButton = screen.getByRole('button', {name: /salvar/i});

    const nameInput = screen.getByTestId('name');
    const priceInput = screen.getByTestId('price');
    const imgUrlInput = screen.getByTestId('imgUrl');
    const descriptionInput = screen.getByTestId('description');
    const categoriesInput = screen.getByLabelText('Categorias');

    userEvent.type(nameInput, 'Computador');

    await selectEvent.select(categoriesInput, ['Computadores', 'Eletrônicos']);

    userEvent.type(priceInput, '5000');
    userEvent.type(imgUrlInput, 'image.jpg');
    userEvent.type(descriptionInput, 'ótimo computador');

    userEvent.click(submitButton);

    await waitFor(() => expect(screen.getByText('Produto salvo com sucesso!')).toBeInTheDocument());

    expect(history.location.pathname).toBe('/admin/products'); //verificar se o link fica aqui quando termina a inserção com sucesso

    expect(screen.getByText(/CADASTRAR UM PRODUTO/i)).toBeInTheDocument();
    //screen.debug();
});


test('should render Form', async () => {

    render(
        <Router history={history} >

            <Form />
        </Router>
    );

    const submitButton = screen.getByRole('button', {name: /salvar/i});

    userEvent.click(submitButton);

    await waitFor(() => expect(screen.getAllByText('Campo obrigatório')).toHaveLength(5));//tem de ter 5 alertas destes quando clicamos no salvar sem preencher nada

    //ate aqui submeti form sem dados e testei que realmente aparecia as mensagens de campo obrigartorio
    //em seguida vou preencher form e submeter de novo para testar se mesngens deixam de aparecer

    const nameInput = screen.getByTestId('name');
    const priceInput = screen.getByTestId('price');
    const imgUrlInput = screen.getByTestId('imgUrl');
    const descriptionInput = screen.getByTestId('description');
    const categoriesInput = screen.getByLabelText('Categorias');

    userEvent.type(nameInput, 'Computador');

    await selectEvent.select(categoriesInput, ['Computadores', 'Eletrônicos']);

    userEvent.type(priceInput, '5000');
    userEvent.type(imgUrlInput, 'image.jpg');
    userEvent.type(descriptionInput, 'ótimo computador');


    await waitFor(() => expect(screen.queryAllByText('Campo obrigatório')).toHaveLength(0));

});