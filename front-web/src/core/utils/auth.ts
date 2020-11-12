import jwtDecode from 'jwt-decode';

import history from './history';

export const CLIENT_ID = 'dscatalog'; //variaveis para usar em toda a aplicação colocamos tudo uppercase
export const CLIENT_SECRET = 'dscatalog123';

type LoginResponse = {
    access_token: string;
    token_type: string;
    expires_in: number;
    scope: string;
    userFirstName: string;
    userId: number;
}

export type Role = 'ROLE_OPERATOR' | 'ROLE_ADMIN';

type AccessToken = {
    exp: number,
    user_name: string,
    authorities: Role[]  //lista de roles que criamos acima
}

export const saveSessionData = (loginResponse: LoginResponse) => {
    localStorage.setItem('authData', JSON.stringify(loginResponse)); //crio uma string com chave authData(nome qualquer) la no localstorage do browser e converto o objeto para string sendo esse o valor que será armazenado no formato string
}

export const getSessionData = () => {
    const sessionData = localStorage.getItem('authData') ?? '{}'; //??, quando primeira condição for nula ou undefined ele executa a segunda condição ou seja {}, ou seja quando não consegue ir buscar os dados ficamos com o {} e assim a conversão abaixo de string para objeto já não vai dar erro, caso nao haja nenhuma string para converter
                                                                //|| tambem dava mas se a primeira parte desse '' string vazia, isso é considerado true para o javascript entao iria executar a primeira e falhar abaixo no parse
                                                                //'{}' isto é uma string com objeto vazio la dentro
    const parsedSessionData = JSON.parse(sessionData); //converte de string para objeto, oposto à de cima

    return parsedSessionData as LoginResponse; //dizemos para confiar em nos e retorna lo como o tipo LoginResponse senao ia retornar como any e no typscript sem tipo nao conseguimos aceder aos campos deste objeto
}                                               //eu seu que o que ta no locastorage segue este formato de dados LoginResponse


export const getAccessTokenDecoded = () => { //retornar access token descodificado
    const sessionData = getSessionData();

    try{
        const tokenDecoded = jwtDecode(sessionData.access_token); //retorna um any ou unknown entao temos de fazer um type casting
        return tokenDecoded as AccessToken;
    }
    catch(error){
        return {} as AccessToken; //retornamos objeto vazio para nosso fluxo nao quebrar
    }
}

export const isTokenValid = () => {
    const { exp } = getAccessTokenDecoded(); //fazer assim é mais simples de aceder por fazes, chama-se Destruct

    //if(Date.now() <= exp*1000){ //multiplico por 1000 porque o exp vem em segundos e o Date.now é os milisegundos desde 1/1/1970 entao convertemos multiolicando por 1000
    //    return true;
    //}

    //return false;
    return Date.now() <= exp*1000; 
}

export const isAuthenticated = () => {
        //SABER SE ESTÀ AUTENTICADO
        //"authData" tem de estar no localstorage
        //access_token nao pode estar expirado

        const sessionData = getSessionData();

        return sessionData.access_token && isTokenValid();

}


export const isAllowedByRole = (routeRoles: Role[] = []) => { //recebemos uma lista de role e o valor padrao é inicializado isto porque como este valor é opcional ia dar erro na chamada do metodo a dizer que o valor podia ser null e nao pode

    if(routeRoles.length === 0){ //se nao especifiquei nenhuma role é porque pode ver o link
        return true;
    }

    const userToken = getAccessTokenDecoded();
    const userRoles = userToken.authorities;

    return routeRoles.some(role => userRoles?.includes(role)); //em vez de ser role === 'ROLE_ADMIN' ou isso, como sao varias, vamos testando cada uma existe no userRole e ao mesmo tempo ele ve se essa Role existe no routeRoles
}


export const logout = () => {

    localStorage.removeItem('authData');
    history.replace('/auth/login');
}