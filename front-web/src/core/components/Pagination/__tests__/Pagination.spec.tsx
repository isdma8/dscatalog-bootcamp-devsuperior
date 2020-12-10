import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Pagination from '..';

test('should render Pagination', () => {
    //Arrange
    const totalPages = 3;
    const activePage = 0;
    const onChange = () => null;


    //Act
    render(
        <Pagination
            totalPages={totalPages}
            activePage={activePage}
            onChange={onChange}
        />
    );

    //ASSERT

    //screen.debug();

    //acedemos a seta de voltar seta de seguinte, 1, 2 e o 3 das paginas
    const previousElement = screen.getByTestId('arrow-icon-previous');
    const nextElement = screen.getByTestId('arrow-icon-next');
    const firstPageItem = screen.getByText('1');
    const secondPageItem = screen.getByText('2');
    const thirdPageItem = screen.getByText('3');

    expect(previousElement).toBeInTheDocument();
    expect(previousElement).toHaveClass('page-inactive'); //é esperado que fique desativo e sabemos que fica se tiver com esta classe
    expect(nextElement).toBeInTheDocument();
    expect(nextElement).toHaveClass('page-active');
    expect(firstPageItem).toBeInTheDocument();
    expect(firstPageItem).toHaveClass('active');//a pagina ativa terá de ser a primeira
    expect(secondPageItem).toBeInTheDocument();
    expect(secondPageItem).not.toHaveClass('active');
    expect(thirdPageItem).toBeInTheDocument();
    expect(thirdPageItem).not.toHaveClass('active');
});

test('should enable previous action and disable next action', () => {
    //Arrange
    const totalPages = 3;
    const activePage = 2; //ativa ultima pagina neste caso
    const onChange = () => null;


    //Act
    render(
        <Pagination
            totalPages={totalPages}
            activePage={activePage}
            onChange={onChange}
        />
    );

    //Assert
    const previousElement = screen.getByTestId('arrow-icon-previous');
    const nextElement = screen.getByTestId('arrow-icon-next');

    expect(previousElement).toBeInTheDocument();
    expect(previousElement).toHaveClass('page-active');

    expect(nextElement).toBeInTheDocument();
    expect(nextElement).toHaveClass('page-inactive');
});

test('should trigger onChange action', () => {
    //Arrange
    const totalPages = 3;
    const activePage = 1; 
    const onChange = jest.fn();//jest.fn é um mock e com isto o jest ja consegui monitorar a função


    //Act
    render(
        <Pagination
            totalPages={totalPages}
            activePage={activePage}
            onChange={onChange}
        />
    );

    //Assert
    const previousElement = screen.getByTestId('arrow-icon-previous');
    const nextElement = screen.getByTestId('arrow-icon-next');
    const firstPageItem = screen.getByText('1');

    userEvent.click(firstPageItem);

//ate aqui teste passa sempre porque o que faz o teste valer a pena e testar de facto é o expect
    expect(onChange).toHaveBeenCalled();//verificar apenas se foi ou nao chamada
    expect(onChange).toHaveBeenCalledWith(0); //espero que a função tenha sido chamada com o valor 0 que corresponde ao clicar na pagina 1

});