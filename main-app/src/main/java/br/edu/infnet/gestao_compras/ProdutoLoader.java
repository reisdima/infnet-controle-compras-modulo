package br.edu.infnet.gestao_compras;

import br.edu.infnet.gestao_compras.dto.request.ProdutoRequestDTO;
import br.edu.infnet.gestao_compras.service.ProdutoService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Order(1)
@Component
public class ProdutoLoader implements ApplicationRunner {
    private final ProdutoService produtoService;

    public ProdutoLoader(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("produto.txt");

        if (inputStream == null) {
            throw new FileNotFoundException("Arquivo produto.txt não encontrado no resources!");
        }
        for(ProdutoRequestDTO produto : processarArquivoProdutos(inputStream)) {
            System.out.println("# " + produto.getNome());
        }
    }


    public List<ProdutoRequestDTO> processarArquivoProdutos(InputStream inputStream) {
        List<ProdutoRequestDTO> processedProdutos = new ArrayList<>();
        try (BufferedReader leitura = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String linha;
            int lineNumber = 0;
            while ((linha = leitura.readLine()) != null) {
                lineNumber++;

                try {
                    ProdutoRequestDTO dto = parseToVProdutoDTO(linha);
                    produtoService.incluir(dto);
                    processedProdutos.add(dto);
                } catch (Exception e) {
                    System.err.println("Erro ao processar linha " +lineNumber+"["+linha+"]");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler o arquivo de produtos: " + e.getMessage());
            throw new RuntimeException("Falha ao processar o arquivo de produtos.", e);
        }
        return processedProdutos;
    }

    private ProdutoRequestDTO parseToVProdutoDTO(String linha) {
        String[] campos = linha.split(";");
        if (campos.length != 5) {
            throw new IllegalArgumentException("Formato de linha inválido. Esperado 5 campos separados por ';'.");
        }

        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome(campos[0].trim());
        dto.setMarca(campos[1].trim());
        dto.setCodigoDeBarras(campos[2].trim());
        dto.setQuantidade(Integer.valueOf(campos[3].trim()));
        dto.setUnidade(campos[4].trim());

        return dto;
    }
}
