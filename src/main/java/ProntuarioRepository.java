import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

// Esta classe foi inserida para absorver a complexidade que não pertencia ao domínio médico.
public class ProntuarioRepository {

    // Aqui eu recebo o objeto prontuario como parâmetro
    public String salveProntuario(Prontuario prontuario) throws IOException {
        List<String> linhas = new ArrayList<>();
        linhas.add("nome_paciente,tipo_leito,qtde_dias_internacao,tipo_procedimento,qtde_procedimentos");

        String nome = prontuario.getNomePaciente();
        Internacao internacao = prontuario.getInternacao();
        Set<Procedimento> procedimentos = prontuario.getProcedimentos();

        if (internacao != null) {
            linhas.add(nome + "," + internacao.getTipoLeito() + "," + internacao.getQtdeDias() + ",,");
        }

        if (!procedimentos.isEmpty()) {
            Map<TipoProcedimento, Long> agrupados = procedimentos.stream()
                .collect(Collectors.groupingBy(Procedimento::getTipoProcedimento, Collectors.counting()));

            for (TipoProcedimento tipo : agrupados.keySet()) {
                linhas.add(nome + ",,," + tipo + "," + agrupados.get(tipo));
            }
        }

        Path path = Paths.get(nome.replaceAll(" ", "_") + "_" + System.currentTimeMillis() + ".csv");
        Files.write(path, linhas);
        return path.toString();
    }

    public Prontuario carregueProntuario(String arquivoCsv) throws IOException {
        Path path = Paths.get(arquivoCsv);
        List<String> linhas = Files.readAllLines(path);

        // Se só tiver o cabeçalho ou estiver vazio
        if (linhas.size() <= 1) {
            throw new IOException("Arquivo CSV vazio ou inválido.");
        }

        Prontuario prontuario = null;

        // Varrer todas as linhas pulando o cabeçalho
        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i);
            if (linha.trim().isEmpty()) continue;

            // Substitui vírgulas consecutivas para evitar erros de split de células vazias
            String[] dados = linha.split(",", -1); 
            String nomePaciente = dados[0].trim();

            // Instancia o objeto prontuário uma única vez com o nome correto
            if (prontuario == null) {
                prontuario = new Prontuario(nomePaciente);
            }

            // 1. Linha com dados de internação (Verifica coluna 1 e 2)
            if (!dados[1].trim().isEmpty() && !dados[2].trim().isEmpty()) {
                TipoLeito tipoLeito = TipoLeito.valueOf(dados[1].trim());
                int qtdeDias = Integer.parseInt(dados[2].trim());
                prontuario.setInternacao(new Internacao(tipoLeito, qtdeDias));
            }

            // 2. Linha com dados de procedimento (Verifica coluna 3 e 4)
            if (!dados[3].trim().isEmpty() && !dados[4].trim().isEmpty()) {
                TipoProcedimento tipoProcedimento = TipoProcedimento.valueOf(dados[3].trim());
                int qtdeProcedimentos = Integer.parseInt(dados[4].trim());

                // Adiciona a quantidade exata de novas instâncias distintas
                for (int j = 0; j < qtdeProcedimentos; j++) {
                    prontuario.addProcedimento(new Procedimento(tipoProcedimento));
                }
            }
        }

        return prontuario;
    }

}

// Apliquei o padrão da Invenção Pura ao criar essa nova classe, pois ele não existe no "mundo real" do hospital, mas existe para manter o código limpo.

// Tbm usei o padrão da Indireção, pois esse arquivo agora age como uma ponte entre o código Java e o sistema de arquivos (CSV), protegendo o restante do sistema de mudanças no formato de salvamento.

