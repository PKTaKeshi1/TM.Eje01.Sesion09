package com.ejemplo.eje01sesion09;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Pantalla principal (visible sólo cuando el usuario está autenticado).
 * El manejo de sesión se hace desde LoginActivity;
 * esta clase muestra información del usuario logueado.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Si no hay sesión, regresar a Login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        Button btnLogoutMain = findViewById(R.id.btnLogoutMain);

        if (tvWelcome != null) {
            tvWelcome.setText("Bienvenido\n" + user.getEmail());
        }

        if (btnLogoutMain != null) {
            btnLogoutMain.setVisibility(View.VISIBLE);
            btnLogoutMain.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            });
        }
    }
}