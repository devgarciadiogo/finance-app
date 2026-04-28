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

    // Enums podem ter métodos! Vamos adicionar um para exibição
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