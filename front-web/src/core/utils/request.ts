import axios, {Method} from 'axios';

type RequestParams = {
    method?: Method; //é um tipo vindo do axios e definido por nos, define se é get, post, put etc
    url: string;
    data?: object; //nao sabemos exatamente o que é
    params?: object;//? torna os parametros nao obrigatorios
}

const BASE_URL = 'http://localhost:3000';

export const makeRequest = ({method='GET', url, data, params}:RequestParams) => { //lemos o method la do requestparams que definimos acima no como type de entrada, valor padrao é GET caso não passe


    return axios({
        method,
        url: `${BASE_URL}${url}`, //inserção de javascript no meio de um string, concateno uma à outra
        data, 
        params
    })
}