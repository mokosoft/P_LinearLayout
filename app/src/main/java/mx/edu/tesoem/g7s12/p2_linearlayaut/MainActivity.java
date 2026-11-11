package mx.edu.tesoem.g7s12.p2_linearlayaut;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla única de la práctica: demuestra LinearLayout con componentes Material 3.
 *
 * Nota: la práctica es DEMOSTRATIVA, por lo que no se valida ningún dato.
 * Cada listener sólo refleja en pantalla lo que hace el componente.
 */
public class MainActivity extends AppCompatActivity {

    /** Paso con que los botones −/+ modifican el progreso. */
    private static final int PASO_PROGRESO = 10;

    // Referencias a vistas que se actualizan en tiempo de ejecución
    private View raiz;
    private TextView tvVistaPrevia;
    private TextView tvFiltros;
    private TextView tvChecks;
    private TextView tvRadio;
    private TextView tvProgreso;
    private ProgressBar pbClasico;
    private LinearProgressIndicator lpiAvance;
    private CheckBox cbConstancia;
    private CheckBox cbMaterial;
    private CheckBox cbAviso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dibuja detrás de las barras del sistema (edge-to-edge, obligatorio desde API 35)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        raiz = findViewById(R.id.main);
        aplicarInsets();

        configurarEditText();
        configurarBotones();
        configurarChips();
        configurarCards();
        configurarCheckBoxes();
        configurarRadioButtons();
        configurarProgreso();
    }

    /**
     * Agrega al LinearLayout raíz un padding igual al tamaño de las barras del
     * sistema (estado y navegación) para que el contenido no quede debajo de ellas.
     */
    private void aplicarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(raiz, (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });
    }

    // ------------------------------------------------------------------ EditText / TextView

    /** Copia en tiempo real el texto del EditText "Nombre" al TextView de vista previa. */
    private void configurarEditText() {
        tvVistaPrevia = findViewById(R.id.tvVistaPrevia);
        TextInputEditText etNombre = findViewById(R.id.etNombre);

        etNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se requiere
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se requiere
            }

            @Override
            public void afterTextChanged(Editable s) {
                String nombre = s.toString().trim();
                if (nombre.isEmpty()) {
                    tvVistaPrevia.setText(R.string.tv_preview_vacio);
                } else {
                    tvVistaPrevia.setText(getString(R.string.tv_preview, nombre));
                }
            }
        });
    }

    // ------------------------------------------------------------------ Buttons

    /** Asigna a cada variante de botón un listener que muestra su nombre en un Snackbar. */
    private void configurarBotones() {
        int[] ids = {
                R.id.btnFilled, R.id.btnTonal, R.id.btnElevated,
                R.id.btnOutlined, R.id.btnText, R.id.btnIcono
        };
        for (int id : ids) {
            Button boton = findViewById(id);
            boton.setOnClickListener(v -> mostrarMensaje(
                    getString(R.string.msg_boton, boton.getText())));
        }

        // Los botones de icono no tienen texto: se usa su contentDescription
        View.OnClickListener porDescripcion = v -> mostrarMensaje(
                getString(R.string.msg_boton, v.getContentDescription()));
        findViewById(R.id.btnIconoFavorito).setOnClickListener(porDescripcion);
        findViewById(R.id.btnIconoAgregar).setOnClickListener(porDescripcion);

        // Grupo segmentado: informa qué opción quedó marcada
        com.google.android.material.button.MaterialButtonToggleGroup grupo =
                findViewById(R.id.toggleGrupo);
        grupo.addOnButtonCheckedListener((g, idBoton, marcado) -> {
            if (marcado) {
                Button b = findViewById(idBoton);
                mostrarMensaje(getString(R.string.msg_boton, b.getText()));
            }
        });

        // Botón principal de la barra inferior
        findViewById(R.id.fabInscribirme).setOnClickListener(
                v -> mostrarMensaje(getString(R.string.msg_inscripcion)));
    }

    // ------------------------------------------------------------------ Chips

    /** Filter chips actualizan un resumen; el Input chip se elimina con su icono de cierre. */
    private void configurarChips() {
        tvFiltros = findViewById(R.id.tvFiltros);
        ChipGroup grupo = findViewById(R.id.chipGroup);

        Chip[] filtros = {
                findViewById(R.id.chipAndroid),
                findViewById(R.id.chipJava),
                findViewById(R.id.chipUi)
        };
        CompoundButton.OnCheckedChangeListener alCambiar =
                (boton, marcado) -> actualizarFiltros(filtros);
        for (Chip chip : filtros) {
            chip.setOnCheckedChangeListener(alCambiar);
        }
        actualizarFiltros(filtros);

        Chip chipInput = findViewById(R.id.chipInput);
        chipInput.setOnCloseIconClickListener(v -> {
            grupo.removeView(v);
            mostrarMensaje(getString(R.string.msg_chip_eliminado));
        });
    }

    /** Construye el texto "Filtros activos: ..." a partir de los chips marcados. */
    private void actualizarFiltros(Chip[] filtros) {
        List<String> activos = new ArrayList<>();
        for (Chip chip : filtros) {
            if (chip.isChecked()) {
                activos.add(chip.getText().toString());
            }
        }
        if (activos.isEmpty()) {
            tvFiltros.setText(R.string.msg_filtros_ninguno);
        } else {
            tvFiltros.setText(getString(R.string.msg_filtros, String.join(", ", activos)));
        }
    }

    // ------------------------------------------------------------------ Cards

    /** Clic en las tarjetas muestra su tipo; la Outlined además se marca con pulsación larga. */
    private void configurarCards() {
        MaterialCardView elevated = findViewById(R.id.cardElevated);
        MaterialCardView filled = findViewById(R.id.cardFilled);
        MaterialCardView outlined = findViewById(R.id.cardOutlined);

        elevated.setOnClickListener(v ->
                mostrarMensaje(getString(R.string.msg_card, getString(R.string.card_elevated))));
        filled.setOnClickListener(v ->
                mostrarMensaje(getString(R.string.msg_card, getString(R.string.card_filled))));
        outlined.setOnClickListener(v ->
                mostrarMensaje(getString(R.string.msg_card, getString(R.string.card_outlined))));

        outlined.setOnLongClickListener(v -> {
            outlined.toggle(); // alterna el estado "checked" y muestra/oculta el icono
            return true;
        });
    }

    // ------------------------------------------------------------------ CheckBox

    /** Cuenta cuántas casillas están marcadas cada vez que cambia alguna. */
    private void configurarCheckBoxes() {
        tvChecks = findViewById(R.id.tvChecks);
        cbConstancia = findViewById(R.id.cbConstancia);
        cbMaterial = findViewById(R.id.cbMaterial);
        cbAviso = findViewById(R.id.cbAviso);

        CompoundButton.OnCheckedChangeListener alCambiar =
                (boton, marcado) -> actualizarChecks();
        cbConstancia.setOnCheckedChangeListener(alCambiar);
        cbMaterial.setOnCheckedChangeListener(alCambiar);
        cbAviso.setOnCheckedChangeListener(alCambiar);
        actualizarChecks();
    }

    private void actualizarChecks() {
        int total = 0;
        if (cbConstancia.isChecked()) total++;
        if (cbMaterial.isChecked()) total++;
        if (cbAviso.isChecked()) total++;
        tvChecks.setText(getString(R.string.msg_checks, total));
    }

    // ------------------------------------------------------------------ RadioButton

    /** RadioGroup notifica el id del RadioButton seleccionado (selección única). */
    private void configurarRadioButtons() {
        tvRadio = findViewById(R.id.tvRadio);
        RadioGroup grupo = findViewById(R.id.rgModalidad);

        grupo.setOnCheckedChangeListener((g, idMarcado) -> mostrarModalidad(idMarcado));
        mostrarModalidad(grupo.getCheckedRadioButtonId());
    }

    private void mostrarModalidad(int idMarcado) {
        RadioButton rb = findViewById(idMarcado);
        if (rb != null) {
            tvRadio.setText(getString(R.string.msg_radio, rb.getText()));
        }
    }

    // ------------------------------------------------------------------ ProgressBar

    /** Los botones −/+ modifican a la vez el ProgressBar clásico y el indicador M3. */
    private void configurarProgreso() {
        pbClasico = findViewById(R.id.pbClasico);
        lpiAvance = findViewById(R.id.lpiAvance);
        tvProgreso = findViewById(R.id.tvProgreso);

        findViewById(R.id.btnMenos).setOnClickListener(v -> cambiarProgreso(-PASO_PROGRESO));
        findViewById(R.id.btnMas).setOnClickListener(v -> cambiarProgreso(PASO_PROGRESO));
        cambiarProgreso(0);
    }

    private void cambiarProgreso(int delta) {
        // Se limita el valor al rango 0..100
        int valor = Math.max(0, Math.min(100, pbClasico.getProgress() + delta));
        pbClasico.setProgress(valor);
        lpiAvance.setProgressCompat(valor, true); // true = animación suave
        tvProgreso.setText(getString(R.string.msg_progreso, valor));
    }

    // ------------------------------------------------------------------ Utilidades

    /** Muestra un Snackbar (mensaje breve M3) anclado al LinearLayout raíz. */
    private void mostrarMensaje(String texto) {
        Snackbar.make(raiz, texto, Snackbar.LENGTH_SHORT).show();
    }
}
