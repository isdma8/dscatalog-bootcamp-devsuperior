import React, { useEffect, useState } from 'react';
import './styles.scss';
import { ReactComponent as SearchIcon } from 'core/assets/images/search-icon.svg';
import Select from 'react-select';
import { makeRequest } from 'core/utils/request';
import { Category } from 'core/types/Product';


export type FilterForm = {
    name?: string;
    categoryId?: number;
}

type Props = {
    onSearch: (filter: FilterForm) => void;
}

const ProductFilters = ({onSearch}: Props) => {

    const [isLoadingCategories, setIsLoadingCategories] = useState(false);

    const [categories, setCategories] = useState<Category[]>([]);

    const [name, setName] = useState('');

    const [category, setCategory] = useState<Category>();

    useEffect(() => {
        setIsLoadingCategories(true);
        makeRequest({ url: '/categories' })
            .then(response => setCategories(response.data.content))
            .finally(() => setIsLoadingCategories(false))
    }, []);

    const handleChangeName = (name: string) => {
        setName(name);

        onSearch({name, categoryId: category?.id});
    }

    const handleChangeCategory = (category: Category) => {
        setCategory(category);

        onSearch({name, categoryId: category?.id});
    }

    const clearFilters = () => {
        setCategory(undefined);
        setName('');

        onSearch({name: '', categoryId: undefined});
    }

    return (
        <div className="card-base product-filters-container">
            <div className="input-search">
                <input
                    type="text"
                    value={name}
                    className="form-control"
                    placeholder="Pesquisar produto"
                    onChange={event => handleChangeName(event.target.value)}
                />
                <SearchIcon />
            </div>
            <Select
                name="categories" //mesmo nome que temos no backend para dpeois poder cadastar
                key={`select-${category?.id}`}//para contornar problema do select que nao limpa quando fazemos cleardele 
                value={category}
                isLoading={isLoadingCategories} //tambem tenho aqui opção de definir um isLoading
                options={categories}
                getOptionLabel={(option: Category) => option.name} //valor a mostrar no select 
                getOptionValue={(option: Category) => String(option.id)} //valor enviado para a API na criação do produto
                className="filter-select-container"
                classNamePrefix="product-categories-select"
                placeholder="Categorias"
                inputId="Categorias" //igual ao htmlfor do label para haver ligação e conseguirmos apanhar nos tests
                onChange={value => handleChangeCategory(value as Category)}
                isClearable
            />
            <button
                className="btn btn-outline-secundary border-radius-10"
                onClick={clearFilters}
            >
                LIMPAR FILTRO
            </button>
        </div>
    )

}


export default ProductFilters;