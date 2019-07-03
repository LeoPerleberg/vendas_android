package br.edu.ifsul.vendas.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import br.edu.ifsul.vendas.R;

public class PedidosActivity extends AppCompatActivity {
    private ListView lvPedidos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        //botão de voltar na barra superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvPedidos = findViewById(R.id.lv_produtos);
        lvPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PedidosActivity.this, "Clicou no pedido", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }
}
