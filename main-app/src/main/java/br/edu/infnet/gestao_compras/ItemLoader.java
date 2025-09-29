package br.edu.infnet.gestao_compras;

import br.edu.infnet.gestao_compras.dto.request.ItemDeCompraRequestDTO;
import br.edu.infnet.gestao_compras.service.ItemService;
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
import java.util.ArrayList;
import java.util.List;

@Order(4)
@Component
public class ItemLoader implements ApplicationRunner {
    private final ItemService itemService;

    public ItemLoader(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("itens.txt");

        if (inputStream == null) {
            throw new FileNotFoundException("Arquivo itens.txt não encontrado no resources!");
        }
        for (ItemDeCompraRequestDTO item : processarArquivoItens(inputStream)) {
            System.out.println("# " + item.getCodidoDeBarras());
        }
    }


    public List<ItemDeCompraRequestDTO> processarArquivoItens(InputStream inputStream) {
        List<ItemDeCompraRequestDTO> processedItens = new ArrayList<>();
        try (BufferedReader leitura = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String linha;
            int lineNumber = 0;
            while ((linha = leitura.readLine()) != null) {
                lineNumber++;

                try {
                    ItemDeCompraRequestDTO dto = parseToItemDeCompraDTO(linha);
                    itemService.incluir(dto);
                    processedItens.add(dto);
                } catch (Exception e) {
                    System.err.println("Erro ao processar linha " + lineNumber + "[" + linha + "]");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler o arquivo de itens: " + e.getMessage());
            throw new RuntimeException("Falha ao processar o arquivo de itens.", e);
        }
        return processedItens;
    }

    private ItemDeCompraRequestDTO parseToItemDeCompraDTO(String linha) {
        String[] campos = linha.split(";");
        if (campos.length != 4) {
            throw new IllegalArgumentException("Formato de linha inválido. Esperado 4 campos separados por ';'.");
        }

        ItemDeCompraRequestDTO dto = new ItemDeCompraRequestDTO();

        dto.setCodidoDeBarras(campos[0]);
        dto.setQuantidade(Integer.valueOf(campos[1]));
        dto.setPreco(new BigDecimal(campos[2]));
        dto.setNotaFiscal(campos[3]);

        return dto;
    }
}
