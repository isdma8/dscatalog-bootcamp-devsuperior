import React from 'react';
import {render, screen} from '@testing-library/react';
import ProductCard from '../ProductCard';
import { Router } from 'react-router-dom';
import history from 'core/utils/history';
import { Product } from 'core/types/Product';

test('should render ProductCard', () => {

    const product = {
        name: 'computer',
        imgUrl: 'image.jpg',
        price: 40

    } as Product; //fazemos um casting para poder ter um produto sem os restantes atributos
    //que sao obrigatorios para criar um product mas que aqui nao precisamos para testar
    //so precisamos de name, imgurl e price, pedimos para o typescript confiar em mim
    //e interpertar este objecto como sendo um Product


    render(
        <ProductCard product={product}/>
    );

    expect(screen.getByText('computer')).toBeInTheDocument();
    expect(screen.getByAltText('computer')).toBeInTheDocument(); //se a imagem tiver alt nao precisamos criar id para ela para saber que ta la aparecendo
    expect(screen.getByText('R$')).toBeInTheDocument();
    expect(screen.getByText('40.00')).toBeInTheDocument(); //aqui testamos o basico do price, la dentor testamos cenarios caoticos
});