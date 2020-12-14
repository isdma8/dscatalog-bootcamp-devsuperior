//Auxiliar ao teste, temos de adicionar --testPathIgnorePatterns='fixtures.ts' ao package.json tests para o jest ignorar este arquivo senao dá erro a dizer que não tem nenhum teste aqui

export const productsResponse = {
    "content": [
        {
            "id": 3,
            "name": "Macbook Pro",
            "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            "price": 1250.0,
            "imgUrl": "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg",
            "date": "2020-07-14T10:00:00Z",
            "categories": []
        },
        {
            "id": 4,
            "name": "PC Gamer",
            "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            "price": 1200.0,
            "imgUrl": "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/4-big.jpg",
            "date": "2020-07-14T10:00:00Z",
            "categories": []
        }
    ],
    "totalPages": 2
};