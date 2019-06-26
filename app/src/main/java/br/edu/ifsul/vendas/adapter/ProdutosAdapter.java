package br.edu.ifsul.vendas.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.List;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.model.Produto;
import br.edu.ifsul.vendas.setup.AppSetup;


public class ProdutosAdapter extends ArrayAdapter<Produto> {

    private final String TAG = "produtosadapter";
    private final Context context;
    private Bitmap fotoEmBitmap;

    public ProdutosAdapter(@NonNull Context context, @NonNull List<Produto> produtos) {
        super(context, 0, produtos);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("StatementWithEmptyBody")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //declara o objeto que irá segurar os objetos escaneados da view
        final ViewHolder holder;
        //infla a view
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_produto_adapter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder); //anexa à view o holder
        }else{
            holder = (ViewHolder) convertView.getTag(); //pega da view o holder
        }

        //vincula os dados do objeto de modelo à view
        final Produto produto = getItem(position); //devolve o objeto do modelo
        holder.tvNome.setText(produto.getNome());
        holder.tvEstoque.setText(produto.getQuantidade().toString());
        holder.tvValor.setText(NumberFormat.getCurrencyInstance().format(produto.getValor()));
        holder.pbFotoProduto.setVisibility(ProgressBar.VISIBLE);
        holder.imvFoto.setImageResource(R.drawable.img_carrinho_de_compras);
        if(produto.getUrl_foto().equals("")){
            holder.pbFotoProduto.setVisibility(ProgressBar.INVISIBLE);
        }else{
            //faz o download do servidor
            if(AppSetup.cacheProdutos.get(produto.getKey()) == null){
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("images/produtos/" + produto.getCodigoDeBarras() + ".jpeg");
                final long ONE_MEGABYTE = 1024 * 1024;
                mStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        fotoEmBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imvFoto.setImageBitmap(fotoEmBitmap);
                        AppSetup.cacheProdutos.put(produto.getKey(), fotoEmBitmap);
                        holder.pbFotoProduto.setVisibility(ProgressBar.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, "Download da foto do produto falhou: " + "images/produtos/" + produto.getCodigoDeBarras() + ".jpeg");
                    }
                });
            }else{
                holder.imvFoto.setImageBitmap(AppSetup.cacheProdutos.get(produto.getKey()));
                holder.pbFotoProduto.setVisibility(ProgressBar.INVISIBLE);
            }
        }

        return convertView;
    }

    /*
        A classe ViewHolder irá segurar os objetos escaneados da view (isso acelera o
        processamento dos cartões).
     */
    private class ViewHolder {

        final TextView tvNome;
        final TextView tvEstoque;
        final TextView tvValor;
        final ImageView imvFoto;
        final ProgressBar pbFotoProduto;

        public ViewHolder(View view) {
            //mapeia os componentes da UI para vincular os dados do objeto de modelo
            tvNome = view.findViewById(R.id.tvNomeProdutoAdapter);
            tvEstoque = view.findViewById(R.id.tvEstoqueProdutoAdapater);
            tvValor = view.findViewById(R.id.tvValorProdutoItemAdapter);
            imvFoto = view.findViewById(R.id.imvFotoProdutoAdapter);
            pbFotoProduto = view.findViewById(R.id.pb_foto_produtos_adapter);
        }
    }

}//fim classe