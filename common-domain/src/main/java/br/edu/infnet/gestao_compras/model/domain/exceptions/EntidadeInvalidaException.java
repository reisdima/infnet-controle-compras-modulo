package br.edu.infnet.gestao_compras.model.domain.exceptions;

public class EntidadeInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntidadeInvalidaException(String mensagem) {
        super(mensagem);
    }
}
