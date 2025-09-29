package br.edu.infnet.gestao_compras;

import br.edu.infnet.gestao_compras.dto.request.CompraRequestDTO;
import br.edu.infnet.gestao_compras.service.CompraService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Order(3)
@Component
public class CompraLoader implements ApplicationRunner {
    private final CompraService compraService;

    public CompraLoader(CompraService compraService) {
        this.compraService = compraService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("compras.txt");

        if (inputStream == null) {
            throw new FileNotFoundException("Arquivo compras.txt não encontrado no resources!");
        }
        for(CompraRequestDTO compras : processarArquivoCompras(inputStream)) {
            System.out.println("# " + compras.getNotaFiscal());
        }
    }

    public List<CompraRequestDTO> processarArquivoCompras(InputStream inputStream) {
        List<CompraRequestDTO> processedCompras = new ArrayList<>();
        try (BufferedReader leitura = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String linha;
            int lineNumber = 0;
            while ((linha = leitura.readLine()) != null) {
                lineNumber++;

                try {
                    CompraRequestDTO dto = parseToCompraDTO(linha);
                    compraService.incluir(dto);
                    processedCompras.add(dto);
                } catch (Exception e) {
                    System.err.println("Erro ao processar linha " +lineNumber+"["+linha+"]");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler o arquivo de compras: " + e.getMessage());
            throw new RuntimeException("Falha ao processar o arquivo de compras.", e);
        }
        return processedCompras;
    }

    private CompraRequestDTO parseToCompraDTO(String linha) {
        String[] campos = linha.split(";");
        if (campos.length != 3) {
            throw new IllegalArgumentException("Formato de linha inválido. Esperado 3 campos separados por ';'.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        CompraRequestDTO dto = new CompraRequestDTO();
        dto.setDataDaCompra(LocalDate.parse(campos[0], formatter));
        dto.setEstabelecimento(campos[1]);
        dto.setNotaFiscal(campos[2]);

        return dto;
    }
}
