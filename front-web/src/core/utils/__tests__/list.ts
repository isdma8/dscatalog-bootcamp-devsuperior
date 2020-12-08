import {generateList} from '../list';

//criamos ficheiro com mesmo nome do ficheiro que queremos testar e fazemos teste simples do comportamento que queremos lá

test('should generate a list', () => { //argumento nome do teste e função com o que pretendemos que faça
    //Entrada: 5
    //ARRANGE
    const amount = 5;

    //ACT
    const result = generateList(amount);

    //Saida: [0,1,2,3,4]
    //ASSERT
    expect(result).toEqual([0,1,2,3,4]); //eu espero que o resultado seja um array com estes valores para o teste passar
    
    //.tobe verifica se as referencias do array se sao iguais ou seja o teste aqui nao ia passar por isso temos de usar o toEqual que compara os valores mesmo
    
});

test('should generate an empty list when amount is zero', () => {
    //ARRANGE
    const amount = 0;

    //ACT
    const result = generateList(amount);

    //ASSERT
    expect(result).toEqual([]);
    
});