import React from 'react';
import {render, screen} from '@testing-library/react';
import ButtonIcon from '..';

test('should render ButtonIcon', () => {
    const text = 'logar';
    render(
        <ButtonIcon text={text} />
    )

    const textElement = screen.getByText(text);

    const iconElement = screen.getByTestId('arrow-icon'); //Este 'arrow-icon' serve de id e é o que colocamos no elemento que queremos testar

    expect(textElement).toBeInTheDocument();
    expect(iconElement).toBeInTheDocument();
});