import { makePrivateRequest, makeRequest } from 'core/utils/request';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useHistory, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import BaseForm from '../../BaseForm';
import './styles.scss';

type FormState = {
    name: string;
    price: string;
    //category?: string;
    description: string;
    imgUrl: string;
}

type ParamsType = {
    productId: string;
}

const Form = () => {

    const { register, handleSubmit, errors, setValue } = useForm<FormState>() //setValue para setar os valores dinamicamente do form
    const history = useHistory();

    const { productId } = useParams<ParamsType>(); //useparams conseguimos ir buscar o id à url definindo acima um ParamsType com o parametro de entrada com nome igualzinho ao que definimos na url :productId

    const isEditing = productId !== 'create';

    const formTitle = isEditing ? 'Editar produto' : 'Cadastrar um produto';

    useEffect(() => {
        if(isEditing){
            makeRequest({ url: `/products/${productId}` })
            .then(response => {
                setValue('name', response.data.name); //seguir exatamente nomes no formState
                setValue('price', response.data.price);
                setValue('description', response.data.description);
                setValue('imgUrl', response.data.imgUrl);
            }) 
        }
    }, [productId, isEditing, setValue]); // isEditing como esta dentro tem de ser aqui identificado tambem etc




    const onSubmit = (data: FormState) => {
        console.log(data);
        makePrivateRequest({
            url: isEditing ? `/products/${productId}` : '/products',  //editar ou criar
            method: isEditing ? 'PUT' : 'POST', 
            data: data
        })
        .then(() => {
            toast.info('Produto salvo com sucesso!');
            history.push('/admin/products');
        })
        .catch( () => {

            toast.error('Erro ao salvar produto!');
        })
    }

    //Gravar na BD


    return (
        //Aqui nos podiamos passar o children como tamos a passar p title, dava na mesma mas o mais comum é colocarmos o que quisermos dentro das tags   <BaseForm></BaseForm> e vai passar tudo la para dentro
        //crio uma row que por padrao já é display flex
        <form onSubmit={handleSubmit(onSubmit)}>
            <BaseForm 
                title={formTitle}
            >
                <div className="row">
                    <div className="col-6">
                        <div className="margin-bottom-30">
                            <input 
                                ref={register({
                                    required: "Campo obrigatório",
                                    minLength: {value: 5, message: 'O campo deve ter no minimo 5 caracteres'},
                                    maxLength: {value: 60, message: 'O campo deve ter no maximo 60 caracteres'}

                                })}
                                name='name'//precisamos deste campo para fazer o handleOnChange de todos os campos aos mesmo tempo do form
                                type="text" 
                                className="form-control input-base" 
                                placeholder="Nome do produto"
                            />
                            {errors.name && ( //como pode ter mais que um erro diferente nao escrevemos direto chamamos o errors.username.message
                            <div className="invalid-feedback d-block">
                                {errors.name.message} 
                            </div>
                            )}
                        </div>
                         <div className="margin-bottom-30">
                            <input 
                                ref={register({required: "Campo obrigatório"})}
                                name="price"
                                type="number" 
                                className="form-control input-base" 
                                placeholder="Preço"

                            />
                            {errors.price && (
                            <div className="invalid-feedback d-block">
                                {errors.price.message} 
                            </div>
                            )}
                        </div>              
                        
                        <div className="margin-bottom-30">
                            <input 
                                ref={register({required: "Campo obrigatório"})}
                                name='imgUrl'//precisamos deste campo para fazer o handleOnChange de todos os campos aos mesmo tempo do form
                                type="text" 
                                className="form-control input-base" 
                                placeholder="Imagem do produto"
                            />
                            {errors.imgUrl && (
                            <div className="invalid-feedback d-block">
                                {errors.imgUrl.message} 
                            </div>
                            )} 
                        </div>                    
                           
                    </div>
                    <div className="col-6">
                        <textarea 
                            ref={register({required: "Campo obrigatório"})}
                            name="description" 
                            className="form-control input-base"
                            placeholder="Descrição"
                            cols={30}   
                            rows={10} 
                            
                        
                        />
                        {errors.description && (
                            <div className="invalid-feedback d-block">
                                {errors.description.message} 
                            </div>
                        )} 

                    </div>
                </div>
            </BaseForm>
        </form>

    );

}


export default Form;