import React from 'react';
import BaseForm from '../../BaseForm';
import './styles.scss';

const Form = () => {


    return (
        //Aqui nos podiamos passar o children como tamos a passar p title, dava na mesma mas o mais comum é colocarmos o que quisermos dentro das tags   <BaseForm></BaseForm> e vai passar tudo la para dentro
        //crio uma row que por padrao já é display flex
        <BaseForm title="cadastrar um produto">
            <div className="row">
                <div className="col-6">
                    <input type="text" className="form-control" />
                </div>
            </div>
        </BaseForm>

    );

}


export default Form;