import java.util.Objects;

public class Procedimento {

	private TipoProcedimento tipoProcedimento;

	public Procedimento(TipoProcedimento tipoProcedimento) {
		this.tipoProcedimento = tipoProcedimento;
	}

	public TipoProcedimento getTipoProcedimento() {
		return this.tipoProcedimento;
	}
	

	// Para quando for debugar/imprimir, o resultado sair limpo e legível!
	@Override
	public String toString() {
		return this.tipoProcedimento.toString();
	}
}