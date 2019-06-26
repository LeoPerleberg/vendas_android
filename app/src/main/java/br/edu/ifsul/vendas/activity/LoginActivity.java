package br.edu.ifsul.vendas.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import br.edu.ifsul.vendas.R;
import br.edu.ifsul.vendas.model.User;
import br.edu.ifsul.vendas.setup.AppSetup;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "loginActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //mapeia os botões e trata o evento onClick
        findViewById(R.id.bt_sigin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
                String senha = ((EditText)findViewById(R.id.etSenha)).getText().toString();
                if(!email.isEmpty() && !senha.isEmpty()) {
                    signin(email,senha);
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.bt_sigup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText)findViewById(R.id.etEmail)).getText().toString();
                String senha = ((EditText)findViewById(R.id.etSenha)).getText().toString();
                if(!email.isEmpty() && !senha.isEmpty()) {
                    signup(email,senha);
                }else{
                    Snackbar.make(findViewById(R.id.container_activity_login), "Preencha todos os campos.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signup(String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            cadastrarUser(mAuth.getCurrentUser());
                            startActivity(new Intent(LoginActivity.this, ProdutosActivity.class));
                            finish();
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if(Objects.requireNonNull(task.getException()).getMessage().contains("email")){
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_already, Snackbar.LENGTH_LONG).show();
                            }else {
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.signup_fail, Snackbar.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }

    private void cadastrarUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setFirebaseUser(firebaseUser);
        user.setFuncao("vendedor");
        user.setEmail(firebaseUser.getEmail());
        FirebaseDatabase.getInstance().getReference().child("vendas/users")
                .child(user.getFirebaseUser().getUid())
                .setValue(user);
        AppSetup.user = user;
    }

    private void signin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            setUserSessao(mAuth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure ",  task.getException());
                            if(Objects.requireNonNull(task.getException()).getMessage().contains("password")){
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.password_fail, Snackbar.LENGTH_LONG).show();
                            }else{
                                Snackbar.make(findViewById(R.id.container_activity_login), R.string.email_fail, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void setUserSessao(final FirebaseUser firebaseUser) {

        FirebaseDatabase.getInstance().getReference()
                .child("vendas/users").child(firebaseUser.getUid())
                .addListenerForSingleValueEvent (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AppSetup.user = dataSnapshot.getValue(User.class);
                        AppSetup.user.setFirebaseUser(firebaseUser);
                        startActivity(new Intent(LoginActivity.this, ProdutosActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_problemas_signin), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}