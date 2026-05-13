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
        // Lógica de leitura do arquivo CSV e criação do objeto Prontuario
        return new Prontuario("Nome Extraído do CSV"); 
    }
}

// Apliquei o padrão da Invenção Pura ao criar essa nova classe, pois ele não existe no "mundo real" do hospital, mas existe para manter o código limpo.

// Tbm usei o padrão da Indireção, pois esse arquivo agora age como uma ponte entre o código Java e o sistema de arquivos (CSV), protegendo o restante do sistema de mudanças no formato de salvamento.

