package br.edu.ifsul.vendas.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Calendar;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.adapter.CarrinhoAdapter;
import br.edu.ifsul.vendas.model.ItemPedido;
import br.edu.ifsul.vendas.model.Pedido;
import br.edu.ifsul.vendas.setup.AppSetup;

public class CarrinhoActivity extends AppCompatActivity {

    private ListView lv_carrinho;
    private TextView tvTotalPedido, tvCliente;

    public static Pedido pedido = new Pedido();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        //botão de voltar na barra superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTotalPedido = findViewById(R.id.tvTotalPedidoCarrinho);
        tvCliente = findViewById(R.id.tvClienteCarrinho);

        tvCliente.setText(AppSetup.cliente.getNome());
        tvTotalPedido.setText(NumberFormat.getCurrencyInstance().format(pedido.getTotalPedido()));

        lv_carrinho = findViewById(R.id.lv_carrinho);
        lv_carrinho.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                excluirProduto(position);
                return true;
            }
        });

        lv_carrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editarProduto(position);
            }
        });

        lv_carrinho.setAdapter(new CarrinhoAdapter(CarrinhoActivity.this, AppSetup.carrinho));

    }

    private void excluirProduto(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_excluir_item);
        //final ItemPedido item = item.get(position);
        //builder.setMessage(getString(R.string.message_nome_produto) + ": " + item.getProduto().getNome());
        builder.setMessage("Excluir item?");
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                atualizarEstoque(position);
                Toast.makeText(CarrinhoActivity.this, getString(R.string.toast_item_excluido), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(findViewById(R.id.container_activity_carrinho), R.string.snack_operacao_cancelada, Snackbar.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    private void editarProduto(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_editar_item);
        //final ItemPedido item = item.get(position);
        //builder.setMessage(getString(R.string.message_nome_produto) + ": " + item.getProduto().getNome());
        builder.setMessage("Editar item?");
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                atualizarEstoque(position);
                Intent intent = new Intent(CarrinhoActivity.this, ProdutoDetalheActivity.class);
                intent.putExtra("position", AppSetup.carrinho.get(position).getProduto().getIndex());
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(findViewById(R.id.container_activity_carrinho), R.string.snack_operacao_cancelada, Snackbar.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    private void atualizarEstoque(int position) {
        //atualiza o estoque
        final DatabaseReference myRef = database.getReference("vendas/produtos");
        ItemPedido item  =  AppSetup.carrinho.get(position);
        myRef.child(item.getProduto().getKey()).child("quantidade").setValue(item.getQuantidade() + item.getProduto().getQuantidade());
        pedido.setTotalPedido(pedido.getTotalPedido() - item.getTotalItem());
        //limpa os objetos da classe AppSetup
        AppSetup.carrinho.remove(position);
        atualizarView();
    }

    private void atualizarView() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_carrinho, menu);
        return true;
    }

    private void finalizarVenda() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_finalizar_venda);
        //adiciona um texto
        builder.setMessage("Sua venda deu R$:" + pedido.getTotalPedido().toString() );
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                DatabaseReference myRef = database.getReference("vendas/pedidos/");
                pedido.setKey(myRef.push().getKey());
                pedido.setItens(AppSetup.carrinho);
                pedido.setCliente(AppSetup.cliente);
                pedido.setFormaDePagamento("A vista");
                pedido.setSituacao(true);
                pedido.setDataCriacao(Calendar.getInstance().getTime());
                pedido.setDataModificacao(Calendar.getInstance().getTime());
                myRef.child(pedido.getKey()).setValue(pedido);
                DatabaseReference myRef2 = database.getReference("vendas/clientes/"+ AppSetup.cliente.getKey() +"/pedidos/");
                //myRef2.child(pedido.getKey());
                myRef2.push().setValue(pedido.getKey());

                //myRef.push().setValue(pedido);
                AppSetup.carrinho.clear();
                pedido = null;
                pedido = new Pedido();
                AppSetup.cliente = null;
                AppSetup.produtos.clear();
                startActivity(new Intent(CarrinhoActivity.this, ProdutosActivity.class));
                finish();
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(findViewById(R.id.container_activity_carrinho), R.string.snack_operacao_cancelada, Snackbar.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    private void cancelarVenda() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //adiciona um título e uma mensagem
        builder.setTitle(R.string.title_cancelar_venda);
        //adiciona os botões
        builder.setPositiveButton(R.string.alertdialog_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < AppSetup.carrinho.size();) {
                    atualizarEstoque(i);
                }
                AppSetup.carrinho.clear();
                pedido = null;
                pedido = new Pedido();
                AppSetup.cliente = null;
                AppSetup.produtos.clear();
                startActivity(new Intent(CarrinhoActivity.this, ProdutosActivity.class));
                finish();
            }
        });
        builder.setNegativeButton(R.string.alertdialog_nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(findViewById(R.id.container_activity_carrinho), R.string.snack_operacao_cancelada, Snackbar.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_finalizar_pedido:
                finalizarVenda();
                break;
            case R.id.menuitem_cancelar_pedido:
                cancelarVenda();
                break;

            case android.R.id.home:
                finish();
                break;

        }

        return true;
    }


}
