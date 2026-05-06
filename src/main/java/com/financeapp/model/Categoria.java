package com.financeapp.model;

public enum Categoria {
    SALARIO,
    FREELANCE,
    ALIMENTACAO,
    MORADIA,
    TRANSPORTE,
    SAUDE,
    LAZER,
    EDUCACAO,
    OUTROS;

    // Metodo de exibição personalizado de cada Categoria
    public String exibir() {
        return switch (this) {
            case SALARIO -> "Salário";
            case FREELANCE -> "Freelance";
            case ALIMENTACAO -> "Alimentação";
            case MORADIA -> "Moradia";
            case TRANSPORTE -> "Transporte";
            case SAUDE -> "Saúde";
            case LAZER -> "Lazer";
            case EDUCACAO -> "Educação";
            case OUTROS -> "Outros";
        };
    }
}