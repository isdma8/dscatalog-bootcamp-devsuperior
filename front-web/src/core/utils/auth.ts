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

export const getSessionData = () => {
    const sessionData = localStorage.getItem('authData') ?? '{}'; //??, quando primeira condição for nula ou undefined ele executa a segunda condição ou seja {}, ou seja quando não consegue ir buscar os dados ficamos com o {} e assim a conversão abaixo de string para objeto já não vai dar erro, caso nao haja nenhuma string para converter
                                                                //|| tambem dava mas se a primeira parte desse '' string vazia, isso é considerado true para o javascript entao iria executar a primeira e falhar abaixo no parse
                                                                //'{}' isto é uma string com objeto vazio la dentro
    const parsedSessionData = JSON.parse(sessionData); //converte de string para objeto, oposto à de cima

    return parsedSessionData as LoginResponse; //dizemos para confiar em nos e retorna lo como o tipo LoginResponse senao ia retornar como any e no typscript sem tipo nao conseguimos aceder aos campos deste objeto
}                                               //eu seu que o que ta no locastorage segue este formato de dados LoginResponse