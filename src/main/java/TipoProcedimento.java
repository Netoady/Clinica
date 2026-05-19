public enum TipoProcedimento {
	BASICO(50.0), 
    COMUM(150.0), 
    AVANCADO(500.0);

	private final double valor;

    TipoProcedimento(double valor) { 
        this.valor = valor; 
    }
    public double getValor() { 
        return valor; 
    }
}
