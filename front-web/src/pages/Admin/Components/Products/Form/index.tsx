import React, { useState } from 'react';
import BaseForm from '../../BaseForm';
import './styles.scss';

type FormState = {
    name: string;
    price: string;
    category?: string;

}


const Form = () => {

    const [formData, setFormData] = useState<FormState>({
        name:'computador',
        price:'',
        category: ''
    });//inicializo com um objeto vazio ja que este recebe um objeto quando tava useState({}) mas agora como tipei ele com FormState tenho de lhe passar os campos ou entao deixar em cima as propriedades opcionais com ?//podiamos fazer um a um mas assim podemos guardar todos os valores no formData
    //const [price, setPrice] = useState('');
    //const [category, setCategory] = useState('lime');

    const handleOnChange = (event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const name = event.target.name;
        const value = event.target.value;
        setFormData(data => ({
            ...data, [name]: value    //para todos os dados do fomulario eu atribuo dinamicamente um nome e valor
        }));
    }
    /*const handleOnChangePrice = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPrice(event.target.value);
    }
    const handleOnChangeCategory = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setCategory(event.target.value);
    }*/

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();//prevenir com que o submit nao tenha o comportamento padrao, ou seja a submição acontece mas não dá aquele efeito de recarregar pagina
        
        
        
        /*const payload = {
            name: productName,
            price //no JS se o nome for igual ao nome da variavel que entra podemos omitir, no de cima coloquei para ver como fica no caso de serem diferentes
        }*/
        console.log(formData);
    }

    return (
        //Aqui nos podiamos passar o children como tamos a passar p title, dava na mesma mas o mais comum é colocarmos o que quisermos dentro das tags   <BaseForm></BaseForm> e vai passar tudo la para dentro
        //crio uma row que por padrao já é display flex
        <form onSubmit={handleSubmit}>
            <BaseForm title="cadastrar um produto">
                <div className="row">
                    <div className="col-6">
                        <input 
                            value={formData.name}//quem contra agora é o formData
                            name='name'//precisamos deste campo para fazer o handleOnChange de todos os campos aos mesmo tempo do form
                            type="text" 
                            className="form-control mb-5" 
                            onChange={handleOnChange}
                            placeholder="Nome do produto"
                        />

                        <select 
                            value={formData.category}
                            className="form-control mb-5" onChange={handleOnChange}
                            name="category"
                        >
                        <option value="grapefruit">Grapefruit</option>
                        <option value="lime">Lime</option>
                        <option value="coconut">Coconut</option>
                        <option value="mango">Mango</option>
                        </select>

                        <input 
                            value={formData.price}
                            name="price"
                            type="text" 
                            className="form-control" 
                            onChange={handleOnChange}
                            placeholder="Preço"
                        />
                    </div>
                </div>
            </BaseForm>
        </form>

    );

}


export default Form;