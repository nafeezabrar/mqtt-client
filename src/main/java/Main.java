import com.nafeezabrar.mqtt.client.MqttClient;
import com.nafeezabrar.mqtt.client.PahoMqttClientWrapper;
import com.nafeezabrar.mqtt.client.conversion.AnySeparatorStringToBytesConverter;
import com.nafeezabrar.mqtt.client.conversion.BytesToStringConverter;
import com.nafeezabrar.mqtt.client.conversion.DecimalColonSeparatorBytesToStringConverter;
import com.nafeezabrar.mqtt.client.conversion.StringToBytesConverter;
import com.nafeezabrar.mqtt.client.presenting.MqttClientWindowPresenter;
import com.nafeezabrar.mqtt.client.ui.MqttClientWindow;
import com.nafeezabrar.mqtt.client.ui.UiApplication;

import java.io.IOException;
import java.util.UUID;

public class Main {

    public static void main(String[] args) throws IOException {
        String serverURI = "tcp://192.168.11.248:1883";
        String clientId = UUID.randomUUID().toString();
        MqttClient mqttClient = new PahoMqttClientWrapper(serverURI, clientId);

        UiApplication.viewActivatedListener = () -> {
            MqttClientWindow mqttClientWindow = UiApplication.ActualWindow;
            StringToBytesConverter stringToBytesConverter = new AnySeparatorStringToBytesConverter();
            BytesToStringConverter bytesToStringConverter = new DecimalColonSeparatorBytesToStringConverter();
            MqttClientWindowPresenter mqttClientWindowPresenter = new MqttClientWindowPresenter(mqttClientWindow, stringToBytesConverter, bytesToStringConverter, mqttClient);
            mqttClientWindowPresenter.initialize();
        };
        UiApplication.run(args, (Class) Main.class);
        mqttClient.close();
    }
}
