import axios, {Method} from 'axios';
import qs from 'qs';
import { CLIENT_ID, CLIENT_SECRET } from './auth';

type RequestParams = {
    method?: Method; //é um tipo vindo do axios e definido por nos, define se é get, post, put etc
    url: string;
    data?: object | string; //pode receber objeto ou string porque vamos precisar que receba string gerada pelo qs
    params?: object;//? torna os parametros nao obrigatorios
    headers?: object;
}

type LoginData = {
    username: string;
    password: string;
}

const BASE_URL = 'http://localhost:3000'; //ja nao preciso usar o proxy, podia colocar aqui direto o link do backend porque ja configurei o CORS no backend qu resolve esse problema

export const makeRequest = ({method='GET', url, data, params, headers}:RequestParams) => { //lemos o method la do requestparams que definimos acima no como type de entrada, valor padrao é GET caso não passe


    return axios({
        method,
        url: `${BASE_URL}${url}`, //inserção de javascript no meio de um string, concateno uma à outra
        data, 
        params,
        headers
    })
}

export const makeLogin = (loginData: LoginData) => {
    const token = `${CLIENT_ID}:${CLIENT_SECRET}`;//concatenação destas variaveis

    const headers = {
        Authorization: `Basic ${window.btoa(token)} `, //passar as variveis para bytoask
        'Content-Type': 'application/x-www-form-urlencoded'
    }

    const payload = qs.stringify({ ...loginData, grant_type: 'password'}); //passo tudo o que tem dentro da loginData e o grant_type escrevo direto e criamos uma string
    //paracido a username=maria@gmail.com&password=123456&grant_type=password
    // '/oauth/token'

    return makeRequest({ url:'/oauth/token', data: payload, method: 'POST', headers: headers })

}