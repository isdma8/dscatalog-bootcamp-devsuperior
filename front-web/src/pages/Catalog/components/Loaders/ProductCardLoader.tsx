import React from "react"
import ContentLoader from "react-content-loader"
import { generateList } from "core/utils/list";


//<> é um fragment que o react usa para podermos juntar mais que um div ao lado do outro mas isso depois nao é passado para o browser
const ProductCardLoader = () => {

    //const loaderItems = [0, 1, 2, 4];
    const loaderItems = generateList(4);

    return (
        <>
            {   
                loaderItems.map(item => (//gera os contentloader, tantos quanto o tamanho da lista que criamos

                    <ContentLoader
                        key={item} //temos de passar sempr eum key nos map
                        speed={1}
                        width={250}
                        height={335}
                        viewBox="0 0 250 335"
                        backgroundColor="#ecebeb"
                        foregroundColor="#d6d2d2"
                    >
                        <rect x="0" y="0" rx="10" ry="10" width="250" height="335" />
                    </ContentLoader>
                ))

            }



        </>

    )
}

export default ProductCardLoader
