import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import 'bootstrap/dist/css/bootstrap.css';   //temos de colocar isto para usar o bootstrap

//function App(){
//  return <h1>Hello World!</h1>
//}

//const App = () => {  IGUAL AO DE CIMA
//    return <h1>Hello World!</h1>
//}

ReactDOM.render(
  <App />,

  document.getElementById('root')
);

