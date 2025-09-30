package br.edu.infnet.gestao_compras;

import br.edu.infnet.gestao_compras.dto.request.CompraRequestDTO;
import br.edu.infnet.gestao_compras.dto.request.ItemDeCompraRequestDTO;
import br.edu.infnet.gestao_compras.service.CompraService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
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
        for (CompraRequestDTO compras : processarArquivoCompras(inputStream)) {
            System.out.println("# " + compras.getNotaFiscal());
        }
    }

    public List<CompraRequestDTO> processarArquivoCompras(InputStream inputStream) {
        List<CompraRequestDTO> processedCompras = new ArrayList<>();
        try (BufferedReader leitura = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String linha = leitura.readLine();
            int lineNumber = 0;
            while (linha != null) {
                CompraRequestDTO dto;
                if (linha.charAt(0) == 'C') {
                    try {
                        // Constroi objeto compra
                        dto = parseToCompraDTO(linha);

                        // Busca todas linhas de itens para essa compra
                        List<ItemDeCompraRequestDTO> itens = new ArrayList<>();
                        String linhaDeItens = leitura.readLine();
                        while (linhaDeItens != null) {
                            if (linhaDeItens.charAt(0) != 'I') {
                                break;
                            }
                            ItemDeCompraRequestDTO item = parseToItemDeCompraDTO(linhaDeItens);
                            item.setNotaFiscal(dto.getNotaFiscal());
                            itens.add(item);
                            linhaDeItens = leitura.readLine();
                            lineNumber++;

                        }
                        dto.setItens(itens);
                        compraService.incluir(dto);
                        processedCompras.add(dto);
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha " + lineNumber + "[" + linha + "]");
                        System.out.println(e);
                    }
                }
                linha = leitura.readLine();
                lineNumber++;
                // TODO adicionar itens ao mesmo arquivo de compra e remover loop de dependeência
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
        dto.setDataDaCompra(LocalDate.parse(campos[0].substring(1), formatter));
        dto.setEstabelecimento(campos[1]);
        dto.setNotaFiscal(campos[2]);

        return dto;
    }

    private ItemDeCompraRequestDTO parseToItemDeCompraDTO(String linha) {
        String[] campos = linha.split(";");
        if (campos.length != 3) {
            throw new IllegalArgumentException("Formato de linha inválido. Esperado 4 campos separados por ';'.");
        }

        ItemDeCompraRequestDTO dto = new ItemDeCompraRequestDTO();

        dto.setCodidoDeBarras(campos[0].substring(1));
        dto.setQuantidade(Integer.valueOf(campos[1]));
        dto.setPreco(new BigDecimal(campos[2]));

        return dto;
    }
}
