import ButtonIcon from 'core/components/ButtonIcon';
import React, { useState } from 'react';
import { Link, useHistory, useLocation } from 'react-router-dom';
import { useForm } from 'react-hook-form';   

import AuthCard from '../Card';
import './styles.scss';
import { makeLogin } from 'core/utils/request';
import { saveSessionData } from 'core/utils/auth';

type FormData = {
    username: string;
    password: string;
}

type LocationState = {
    from: string;
}


const Login = () => {

    const { register, handleSubmit, errors } = useForm<FormData>();

    const [hasError, setHasError] = useState(false);

    const history = useHistory();

    const location = useLocation<LocationState>();

    const { from } = location.state || { from: { pathname: "/admin" } }; //caso nao existe a rota para onde o user queria ir redireciono para admin

    const onSubmit = (data: FormData) => {
        console.log(data);

        makeLogin(data) //todos os pedidos tem um sucesso e caem no then e quando dao erro caem no catch
        .then(response => {
            setHasError(false);
            saveSessionData(response.data);
            //history.push('/admin');
            history.replace(from); //para ele trocar na pilha de endereços o endereço usado para o auth pelo link que o user tava antes de querer ir para este link onde precisou de fazer login, assim que fizer retroceder nao vai para a pagina login mas para a pagina onde tava antes
        })
        .catch(() => {
            setHasError(true);
        })
    }

    return (
        <AuthCard title="login">
            {hasError && (
                <div className="alert alert-danger mt-5">
                Usuário ou senha inválidos!
                </div>
            )}
            <form className="login-form" onSubmit={handleSubmit(onSubmit)}> 
                    <div className="margin-bottom-30">

                        <input 
                            type="email" 
                            className={`form-control input-base ${errors.username && 'is-invalid'} `}
                            placeholder="Email"
                            name="username" 
                            ref={register({
                                required: "Campo obrigatório",
                                pattern: {
                                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                  message: "Email inválido"
                                }
                            })}//forma de validar email tirada do react hook form
                        />
                        {errors.username && ( //como pode ter mais que um erro diferente nao escrevemos direto chamamos o errors.username.message
                            <div className="invalid-feedback d-block">
                                {errors.username.message} 
                            </div>
                        )}
                                            
                    </div>
                    <div className="margin-bottom-30">

                        <input 
                            type="password" 
                            className={`form-control input-base ${errors.password ? 'is-invalid' : ''} `}
                            placeholder="Senha"
                            name="password" 
                            ref={register({required: "Campo obrigatório", minLength: 5})}//colocar required true para ser campo obrigatorio, para dizermos ao react hook form que é obrigatorio este campo
                        />
                        {errors.password && (
                            <div className="invalid-feedback d-block">
                                {errors.password.message}
                            </div>
                        )}

                    </div>
                    <Link to="/auth/recover" className="login-link-recover">  
                        Esqueci a senha ?
                    </Link>
                    <div className="login-submit">
                        <ButtonIcon text="logar" />
                    </div>

                    <div className="text-center">
                        <span className="not-registered"> 
                            Não tem Cadastro?
                        </span>
                        <Link to="/auth/register" className="login-link-register">
                            CADASTRAR
                        </Link>
                    </div>
                    
            </form>
        </AuthCard>
    );
}


export default Login;