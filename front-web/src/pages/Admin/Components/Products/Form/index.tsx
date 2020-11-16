import { makePrivateRequest } from 'core/utils/request';
import React from 'react';
import { useForm } from 'react-hook-form';
import { useHistory } from 'react-router-dom';
import { toast } from 'react-toastify';
import BaseForm from '../../BaseForm';
import './styles.scss';

type FormState = {
    name: string;
    price: string;
    //category?: string;
    description: string;
    imageUrl: string;
}

const Form = () => {

    const { register, handleSubmit, errors } = useForm<FormState>()
    const history = useHistory();

    const onSubmit = (data: FormState) => {

        makePrivateRequest({url: '/products', method: 'POST', data: data})
        .then(() => {
            toast.info('Produto Cadastrado com sucesso!');
            history.push('/admin/products');
        });
    }

    //Gravar na BD


    return (
        //Aqui nos podiamos passar o children como tamos a passar p title, dava na mesma mas o mais comum é colocarmos o que quisermos dentro das tags   <BaseForm></BaseForm> e vai passar tudo la para dentro
        //crio uma row que por padrao já é display flex
        <form onSubmit={handleSubmit(onSubmit)}>
            <BaseForm title="cadastrar um produto">
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
                                name='imageUrl'//precisamos deste campo para fazer o handleOnChange de todos os campos aos mesmo tempo do form
                                type="text" 
                                className="form-control input-base" 
                                placeholder="Imagem do produto"
                            />
                            {errors.imageUrl && (
                            <div className="invalid-feedback d-block">
                                {errors.imageUrl.message} 
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