import React, { useCallback, useEffect, useState } from 'react';
import ProductCard from './components/ProductCard';
import './styles.scss';
import { Link } from 'react-router-dom';
import { makeRequest } from 'core/utils/request';
import { ProductsResponse } from 'core/types/Product';
import ProductCardLoader from './components/Loaders/ProductCardLoader';
import Pagination from 'core/components/Pagination';
import ProductFilters, {FilterForm} from 'core/components/ProductFilters';

const Catalog = () => {
    //A logica colocamos antes de retorno
    //Antes do componente iniciar, buscar a lista de produtos
    //Quando a lista de produtos estiver disponivel, popular um estado no componente 
    //e listar os produtos dinamicamente
    const [productsResponse, setProductsResponse] = useState<ProductsResponse>(); //tipo do estado é um ProductResponse como nos criamos em Product
//crio um estado e armazeno tudo o que chega desse estado setProductsResponse na variavel productsResponse

    const [isLoading, setIsLoading] = useState(false); //por padrao usestate nao aparece entao colocamos false

    const [activePage, setActivePage] = useState(0);

    const getProducts = useCallback((filter?: FilterForm) => {
        const params = { //criar objeto
            page: activePage,
            linesPerPage: 12, //12 por padrão
            name: filter?.name,
            categoryId : filter?.categoryId
            
        }

        //Antes de começar requisição inicio o loader
        setIsLoading(true);
        makeRequest({url: '/products', params})
            .then(response => setProductsResponse(response.data))
            .finally(() => {//executado sempre depois de carregar os dados este finally se o chamarmos
                //entao aqui finalizamos o loader
                setIsLoading(false);
            })
    }, [activePage])

    //as chamadas de API ficam dentro do useeffect porque o componente pode ser renderizado muitas vezes para qualquer pequena coisa que mudar
    //e cada vez que o fizesse se colocassemos aqui solto ia fazer uma chamada nova
    //Queremos a chamada ao backend somente quando componente for iniciado
    useEffect(()=> {
        getProducts();
        //console.log('componente de listagem iniciado!');
        //fetch('http://localhost:3000/products')
        //.then(response => response.json())
        //.then(response => console.log(response));//fazemos o pedido no localhost do nosso frontend porque agora temos configurado no package uma proxy que irá reencaminhar sem erros para o localhost do nosso backend o sts

        //fetch, limitações
        //Muito verboso, muito codigo para requisições
        //Não tem suporte para ler o progresso de upload de arquivos
        //Não tem suporte nativo para enviar quary strings page=12 etc
        //Entao usamos o axios

        //axios('http://localhost:3000/products')  //mas vamos fazer isto globalmente atraves do utils->request
        // .then(response => console.log(response));

        /*const params = { //criar objeto
            page: activePage,
            linesPerPage: 12 //12 por padrão
        }

        //Antes de começar requisição inicio o loader
        setIsLoading(true);
        makeRequest({url: '/products', params})
            .then(response => setProductsResponse(response.data))
            .finally(() => {//executado sempre depois de carregar os dados este finally se o chamarmos
                //entao aqui finalizamos o loader
                setIsLoading(false);
            })*/

        }, [getProducts]);
    //}, [activePage]); //1º argumento é uma função onde vamos fazer algo e o segundo uma lista de dependencias que se estiver vazia
    //significa que está aguardar para faazer alguma coisa assim que o componente iniciar, se tiver variaveis temos de as meter porque precisa delas cada vez que renderiza
        //<Link to="/products/1"><ProductCard /></Link> assim era estatico

    return(
    <div className="catalog-container">
    <div className="d-flex justify-content-between"> 
        <h1 className="catalog-title">
            Catálogo de Produtos
        </h1>

        <ProductFilters onSearch={filter => getProducts(filter)}/>
    </div>

    <div className="catalog-products">
        {isLoading ? <ProductCardLoader /> : (
            productsResponse?.content.map(product => (
                <Link to={`/products/${product.id}`} key={product.id}>
                    <ProductCard product={product}/></Link>
                ))

        )}
    </div>
            {productsResponse && ( 
            <Pagination 
                totalPages={productsResponse.totalPages} 
                activePage={activePage}
                onChange={page => setActivePage(page)}
            />
            )}  
            
    </div>

    );
                
};


export default Catalog;