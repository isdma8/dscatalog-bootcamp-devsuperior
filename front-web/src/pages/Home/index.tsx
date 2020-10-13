import React from 'react';
import './styles.scss';
import { ReactComponent as MainImage } from '../../core/assets/images/main-image.svg'; //buscar a image e renomea la para Mainimage
import ButtonIcon from '../../core/components/ButtonIcon';

import { Link } from 'react-router-dom';

const Home = () => (

    <div className="home-container">
        <div className="row home-content">
            <div className="col-6 home-text">
                <h1 className="text-title">
                    Conheça o melhor <br /> catálogo de produtos
                </h1>
                <p className="text-subtitle">
                    Ajudaremos você a encontrar os melhores <br /> produtos disponíveis no mercado.
                </p>
                <Link to="/catalog">
                    <ButtonIcon text="inicia agora a sua pesquisa" />
                </Link>
            </div>
            <div className="col-6">
                <MainImage className="main-image"/>
            </div>
        </div>
    </div>
    
);
//Bootstrap funciona com 12 colunas entao dividimos com 6 para o texto e 6 para a imagem com o col-6, para tal temos de colocar o row
//no className do contente para trabalharmos com colunas

export default Home;