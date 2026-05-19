import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;
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
        // Força a localização para garantir o padrão R$
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        StringBuilder sb = new StringBuilder();

        sb.append("----------------------------------------------------------------------------------------------\n");
        
        // Remove o espaço não-quebrável que o NumberFormat gera e padroniza com um espaço comum
        String totalConta = formatter.format(getTotalConta()).replace("\u00A0", " ");
        
        // Texto exato esperado pelo teste: "A conta do(a) paciente " sem os dois pontos adicionais
        sb.append("A conta do(a) paciente ").append(nomePaciente).append(" tem valor total de __ ").append(totalConta).append(" __\n");
        sb.append("\n");
        sb.append("Conforme os detalhes abaixo:\n");
        sb.append("\n");

        // Detalhe das Diárias de Internação
        if (internacao != null) {
            String valorInternacao = formatter.format(internacao.getTipoLeito().calcularValor(internacao.getQtdeDias())).replace("\u00A0", " ");
            sb.append("Valor Total Diárias:\t\t\tR$ ").append(valorInternacao.replace("R$", "").trim()).append("\n");
            
            String sufixoLeito = internacao.getTipoLeito().toString().toLowerCase();
            String sufixoDias = internacao.getQtdeDias() == 1 ? "diária" : "diárias";
            sb.append("\t\t\t\t\t").append(internacao.getQtdeDias()).append(" ").append(sufixoDias).append(" em ").append(sufixoLeito).append("\n");
            sb.append("\n");
        }

        // Detalhe dos Procedimentos
        if (!procedimentos.isEmpty()) {
            double somaProcedimentos = procedimentos.stream()
                    .mapToDouble(p -> p.getTipoProcedimento().getValor())
                    .sum();
            String valorProcedimentos = formatter.format(somaProcedimentos).replace("\u00A0", " ");
            
            sb.append("Valor Total Procedimentos:\t\tR$ ").append(valorProcedimentos.replace("R$", "").trim()).append("\n");

            long basicos = procedimentos.stream().filter(p -> p.getTipoProcedimento() == TipoProcedimento.BASICO).count();
            long comuns = procedimentos.stream().filter(p -> p.getTipoProcedimento() == TipoProcedimento.COMUM).count();
            long avancados = procedimentos.stream().filter(p -> p.getTipoProcedimento() == TipoProcedimento.AVANCADO).count();

            if (basicos > 0) {
                String sufixo = basicos == 1 ? "procedimento básico" : "procedimentos básicos";
                sb.append("\t\t\t\t\t").append(basicos).append(" ").append(sufixo).append("\n");
            }
            if (comuns > 0) {
                String sufixo = comuns == 1 ? "procedimento comum" : "procedimentos comuns";
                sb.append("\t\t\t\t\t").append(comuns).append(" ").append(sufixo).append("\n");
            }
            if (avancados > 0) {
                String sufixo = avancados == 1 ? "procedimento avançado" : "procedimentos avançados";
                sb.append("\t\t\t\t\t").append(avancados).append(" ").append(sufixo).append("\n");
            }
            sb.append("\n");
        }

        sb.append("Volte sempre, a casa é sua!\n");
        sb.append("----------------------------------------------------------------------------------------------");

        return sb.toString();
    }
}

// Aqui eu exclui os métodos salveProntuario e carregueProntuario, que agora estão no ProntuarioRepository. O Prontuario agora é apenas um modelo de dados, sem lógica de persistência.

//Apliquei tbm o padrão SRP, pois eu removi responsabilidade de lidar com arquivos CSV. Agora a classe só cuida dos dados do paciente.

// Usei o padrão do Baixo Acoplamento ao remover os métodos de arquivo, a classe não precisa mais importar bibliotecas de I/O (java.nio.file), reduzindo a dependência de tecnologias externas.

// Usei o padrão da Alta Coesão, pois agora a classe agora foca 100% em representar um prontuário médico, sem misturar regras de persistência de dados.