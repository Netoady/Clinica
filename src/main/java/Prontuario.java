import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

public class Prontuario {
    private String nomePaciente;
    private Internacao internacao;
    private Set<Procedimento> procedimentos = new HashSet<>();

    public Prontuario(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}

	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}

	public String getNomePaciente() {
		return this.nomePaciente;
	}

	public void setInternacao(Internacao internacao) {
		this.internacao = internacao;
	}

	public Internacao getInternacao() {
		return this.internacao;
	}

	public void addProcedimento(Procedimento procedimento) {
		this.procedimentos.add(procedimento);
	}

	public Set<Procedimento> getProcedimentos() {
		return this.procedimentos;
	}

    public double getTotalConta() {
        double total = 0;
        if (internacao != null) {
            total += internacao.getTipoLeito().calcularValor(internacao.getQtdeDias());
        }
        total += procedimentos.stream()
                .mapToDouble(p -> p.getTipoProcedimento().getValor())
                .sum();
        return total;
    }

    public String gerarRelatorio() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("Conta do Paciente: ").append(nomePaciente).append("\n");
        sb.append("Total: ").append(formatter.format(getTotalConta()));
        return sb.toString();
    }
}

// Aqui eu exclui os métodos salveProntuario e carregueProntuario, que agora estão no ProntuarioRepository. O Prontuario agora é apenas um modelo de dados, sem lógica de persistência.

//Apliquei tbm o padrão SRP, pois eu removi responsabilidade de lidar com arquivos CSV. Agora a classe só cuida dos dados do paciente.

// Usei o padrão do Baixo Acoplamento ao remover os métodos de arquivo, a classe não precisa mais importar bibliotecas de I/O (java.nio.file), reduzindo a dependência de tecnologias externas.

// Usei o padrão da Alta Coesão, pois agora a classe agora foca 100% em representar um prontuário médico, sem misturar regras de persistência de dados.