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

export const saveSessionData = (loginResponse: LoginResponse) => {
    localStorage.setItem('authData', JSON.stringify(loginResponse)); //crio uma string com chave authData(nome qualquer) la no localstorage do browser e converto o objeto para string sendo esse o valor que será armazenado no formato string
}