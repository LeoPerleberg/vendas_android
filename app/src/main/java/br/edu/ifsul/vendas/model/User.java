package br.edu.ifsul.vendas.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

public class User {
    private FirebaseUser firebaseUser;
    private String funcao;
    private String email;

    public User(){

    }

    @Exclude
    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    @Exclude
    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
