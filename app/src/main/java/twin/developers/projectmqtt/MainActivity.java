package twin.developers.projectmqtt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    // Variables para la base de datos Firebase y los botones de la interfaz
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private Button btnEncender, btnApagar;
    private Mqtt mqttManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de los botones y el cliente MQTT
        btnEncender = findViewById(R.id.btnEncender);
        btnApagar = findViewById(R.id.btnApagar);
        mqttManager = new Mqtt(getApplicationContext());

        // Conecta al cliente MQTT con el broker
        mqttManager.connectToMqttBroker();

        // Establece un listener para el botón de encendido
        btnEncender.setOnClickListener(v -> {
            // Cambia el estado de la luz a "encendido" y publica un mensaje MQTT
            cambiarEstadoLuz("encendido");
            mqttManager.publishMessage("Encender Luz");
        });

        // Establece un listener para el botón de apagado
        btnApagar.setOnClickListener(v -> {
            // Cambia el estado de la luz a "apagado" y publica un mensaje MQTT
            cambiarEstadoLuz("apagado");
            mqttManager.publishMessage("Apagar Luz");
        });
    }

    // Método para cambiar el estado de la luz en la base de datos Firebase
    private void cambiarEstadoLuz(String estado) {
        // Crea un mapa para el nuevo estado de la luz
        Map<String, Object> luz = new HashMap<>();
        luz.put("estado", estado);

        // Obtiene una referencia a la base de datos de Firebase y actualiza el estado de la luz
        DatabaseReference myRef = db.getReference("luz");
        myRef.setValue(luz);
    }
}