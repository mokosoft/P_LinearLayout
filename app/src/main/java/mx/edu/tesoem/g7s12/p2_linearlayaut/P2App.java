package mx.edu.tesoem.g7s12.p2_linearlayaut;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

/**
 * Clase Application de la práctica.
 *
 * Su única responsabilidad es activar <b>Material You</b> (color dinámico):
 * DynamicColors toma la paleta que el sistema genera a partir del fondo de
 * pantalla del usuario (Android 12 / API 31 o superior) y la aplica a TODAS
 * las Activities antes de que se dibujen. Todos los atributos de color del
 * tema (?attr/colorPrimary, ?attr/colorSurface, etc.) toman esos valores.
 *
 * Se registra en AndroidManifest.xml con android:name=".P2App".
 */
public class P2App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Si el dispositivo no soporta color dinámico, se conserva la paleta base del tema.
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
