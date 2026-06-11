package com.ejemplo.eje01sesion09;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Ejercicio 1 – Lab 09 Tecnologías Móviles
 * Autenticación con Firebase Authentication en JAVA
 * Funciones: Registro, Login y Logout de usuarios
 */
public class LoginActivity extends AppCompatActivity {

    // ─── Firebase ───────────────────────────────────────────────────────────
    private FirebaseAuth mAuth;

    // ─── Vistas ─────────────────────────────────────────────────────────────
    private TextInputEditText etEmail, etPassword;
    private Button btnRegister, btnLogin, btnLogout;
    private ProgressBar progressBar;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Enlazar vistas
        etEmail      = findViewById(R.id.etEmail);
        etPassword   = findViewById(R.id.etPassword);
        btnRegister  = findViewById(R.id.btnRegister);
        btnLogin     = findViewById(R.id.btnLogin);
        btnLogout    = findViewById(R.id.btnLogout);
        progressBar  = findViewById(R.id.progressBar);
        tvStatus     = findViewById(R.id.tvStatus);

        // Listeners de botones
        btnRegister.setOnClickListener(v -> registerUser());
        btnLogin.setOnClickListener(v -> loginUser());
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    // ─── onStart: verificar sesión activa ───────────────────────────────────
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    // ────────────────────────────────────────────────────────────────────────
    //  REGISTRO
    // ────────────────────────────────────────────────────────────────────────
    private void registerUser() {
        String email    = getEmail();
        String password = getPassword();

        if (!validarCampos(email, password)) return;

        showLoading(true);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showLoading(false);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this,
                                    "✅ Registro exitoso: " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "❌ Error al registrar: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // ────────────────────────────────────────────────────────────────────────
    //  LOGIN
    // ────────────────────────────────────────────────────────────────────────
    private void loginUser() {
        String email    = getEmail();
        String password = getPassword();

        if (!validarCampos(email, password)) return;

        showLoading(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showLoading(false);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this,
                                    "✅ Bienvenido: " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "❌ Error al iniciar sesión: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // ────────────────────────────────────────────────────────────────────────
    //  LOGOUT
    // ────────────────────────────────────────────────────────────────────────
    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        updateUI(null);
    }

    // ────────────────────────────────────────────────────────────────────────
    //  VALIDACIÓN
    // ────────────────────────────────────────────────────────────────────────
    private boolean validarCampos(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Formato de correo inválido");
            etEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("La contraseña debe tener al menos 6 caracteres");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    // ────────────────────────────────────────────────────────────────────────
    //  HELPERS UI
    // ────────────────────────────────────────────────────────────────────────
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Ir a MainActivity si el usuario está autenticado
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cerrar LoginActivity
        } else {
            tvStatus.setText("No has iniciado sesión");
            btnLogin.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
        btnLogin.setEnabled(!show);
    }

    private String getEmail() {
        return etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
    }

    private String getPassword() {
        return etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
    }
}