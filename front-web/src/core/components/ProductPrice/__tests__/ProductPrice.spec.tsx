import React from 'react';
import {render, screen} from '@testing-library/react';
import ProductPrice from '..'; //não preciso escrever index, é redundante quando é index escreve lo

test('should render ProductPrice', () => {
    //ARRANGE
        const price = 1200;
    //ACT
    render(
        <ProductPrice price={price}/> //simula-o
    )
    //ASSERT
    const currencyElement = screen.getByText('R$');
    const priceElement = screen.getByText('1,200.00');
    
    expect(currencyElement).toBeInTheDocument();
    expect(priceElement).toBeInTheDocument();
    //vamos no ecrã vemos como queremos que seja mostrado o valor dos reais e o preço e colocamos aqui
    //queremos saber se o valor está a ser bem apresentado

});

test('should render ProductPrice with price equals zero', () => {
    //ARRANGE
        const price = 0;
    //ACT
    render(
        <ProductPrice price={price}/>
    )
    //ASSERT
    const currencyElement = screen.getByText('R$');
    const priceElement = screen.getByText('0.00');
    
    expect(currencyElement).toBeInTheDocument();
    expect(priceElement).toBeInTheDocument();

});

test('should render ProductPrice without thousand separator', () => {
    //ARRANGE
        const price = 100;
    //ACT
    render(
        <ProductPrice price={price}/>
    )
    //ASSERT
    const currencyElement = screen.getByText('R$');
    const priceElement = screen.getByText('100.00');
    
    expect(currencyElement).toBeInTheDocument();
    expect(priceElement).toBeInTheDocument();

});