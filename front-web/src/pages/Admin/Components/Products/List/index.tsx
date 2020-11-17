import React from 'react';
import {useHistory} from 'react-router-dom';
import Card from '../Card';

const List= () => {
    
    const history = useHistory(); 

    const handleCreate = () => {
        history.push('/admin/products/create');
    }


//bootstrap tem as classes btn btn-primary btn-lg etc, so pesquisar por button e tem la tudo
return (
    <div className="admin-products-list">
        <button className="btn btn-primary btn-lg" onClick={handleCreate}>
            ADICIONAR
        </button>
        <div className="admin-list-container">
            <Card />
            <Card />
            <Card />
        </div>

    </div>

)


}

export default List;