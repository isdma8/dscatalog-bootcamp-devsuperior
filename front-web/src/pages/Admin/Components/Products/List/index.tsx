import Pagination from 'core/components/Pagination';
import { ProductsResponse } from 'core/types/Product';
import { makePrivateRequest, makeRequest } from 'core/utils/request';
import React, { useEffect, useState, useCallback } from 'react';
import {useHistory} from 'react-router-dom';
import Card from '../Card';
import CardLoader from '../Loaders/ProductCardLoader';
import { toast } from 'react-toastify';

const List= () => {
    const [productsResponse, setProductsResponse] = useState<ProductsResponse>(); 
    
        const [isLoading, setIsLoading] = useState(false); 
    
        const [activePage, setActivePage] = useState(0);

        const history = useHistory();  
        
        const getProducts= useCallback(() => {   //para poder fazer isto ou seja criar uma função com o que tava dentro do useEffect sem dar erro por causa da dependecnia que tinha que se nao for usada chama esta função a cada renderização usamos o useCallback
            const params = {
                page: activePage,
                linesPerPage: 4,
                direction: 'DESC',
                orderBy: 'id'
            }
    
            setIsLoading(true);
            makeRequest({url: '/products', params})
                .then(response => setProductsResponse(response.data))
                .finally(() => {
                    setIsLoading(false);
                })
        }, [activePage]); //Assim com o useCallback temos na mesma o array de dependencias e o efeito fica o mesmo que tinha dentro do useEffect sendo que neste caso podemos fazer uma função independnete fora do useeffect e continuamos com a chamada dela apenas quando usamos neste caso o activePage
    
        useEffect(()=> {
            getProducts();
    
        }, [getProducts]);

    

    const handleCreate = () => {
        history.push('/admin/products/create');
    }

    const onRemove = (productId: number) => {
        const confirm = window.confirm('Deseja realmente remover este produto?')
        if(confirm){
            makePrivateRequest({url: `/products/${productId}`, method:'DELETE'})
            .then(() => {
                toast.info('Produto removido com sucesso!');
                getProducts();
            })
            .catch( () => {
    
                toast.error('Erro ao remover produto!');
            })
        }
    }

//bootstrap tem as classes btn btn-primary btn-lg etc, so pesquisar por button e tem la tudo
return (
    <div className="admin-products-list">
        <button className="btn btn-primary btn-lg" onClick={handleCreate}>
            ADICIONAR
        </button>
        <div className="admin-list-container">
            {isLoading ? <CardLoader /> : (
                productsResponse?.content.map(product => (
                    <Card product={product} key={product.id} onRemove={onRemove}/>
                ))
            )}
            
            {productsResponse && ( 
                <Pagination 
                    totalPages={productsResponse.totalPages} 
                    activePage={activePage}
                    onChange={page => setActivePage(page)}
                />
            )}
        </div>

    </div>

)


}

export default List;