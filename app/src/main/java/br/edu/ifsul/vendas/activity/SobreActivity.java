package br.edu.ifsul.vendas.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.edu.ifsul.vendas.R;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        //botão de voltar na barra superior
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}