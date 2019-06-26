package br.edu.ifsul.vendas.setup;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifsul.vendas.model.Cliente;
import br.edu.ifsul.vendas.model.ItemPedido;
import br.edu.ifsul.vendas.model.Produto;
import br.edu.ifsul.vendas.model.User;

public class AppSetup {

    public static List<Produto> produtos = new ArrayList<>();
    public static Cliente cliente = null;
    public static List<ItemPedido> carrinho = new ArrayList<>();
    public static User user = null;
    public static Map<String, Bitmap> cacheProdutos = new HashMap<>();
}
