package com.nafeezabrar.mqtt.client.presenting;

import com.nafeezabrar.mqtt.client.MqttClient;
import com.nafeezabrar.mqtt.client.events.listening.MqttClientEventListener;
import com.nafeezabrar.mqtt.client.conversion.BytesToStringConverter;
import com.nafeezabrar.mqtt.client.conversion.StringToBytesConverter;
import com.nafeezabrar.mqtt.client.ui.MqttClientWindow;
import com.nafeezabrar.mqtt.client.events.listening.MqttClientWindowEventListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MqttClientWindowPresenterTest {

    private MqttClientWindow mqttClientWindow;
    private StringToBytesConverter stringToBytesConverter;
    private BytesToStringConverter bytesToStringConverter;
    private MqttClientWindowPresenter mqttClientWindowPresenter;
    private MqttClient mqttClient;

    @Before
    public void setUp() throws Exception {
        mqttClientWindow = mock(MqttClientWindow.class);
        stringToBytesConverter = mock(StringToBytesConverter.class);
        bytesToStringConverter = mock(BytesToStringConverter.class);
        mqttClient = mock(MqttClient.class);
        mqttClientWindowPresenter = new MqttClientWindowPresenter(mqttClientWindow, stringToBytesConverter, bytesToStringConverter, mqttClient);
    }

    @Test
    public void whenInitialized_setEmptyTopicAndMessage() throws Exception {
        mqttClientWindowPresenter.initialize();

        verify(mqttClientWindow).setTopic("");
        verify(mqttClientWindow).setMessage("");
    }

    @Test
    public void whenInitialized_setEventListenerToMqttClientAndWindow() throws Exception {
        mqttClientWindowPresenter.initialize();

        verify(mqttClient).setEventListener(any(MqttClientEventListener.class));
        verify(mqttClientWindow).setEventListener(any(MqttClientWindowEventListener.class));
    }

    @Test
    public void whenMqttMessageIsReceived_addTopicAndMessageStringToWindow() throws Exception {
        ArgumentCaptor<MqttClientEventListener> eventListenerCaptor = ArgumentCaptor.forClass(MqttClientEventListener.class);
        mqttClientWindowPresenter.initialize();
        verify(mqttClient).setEventListener(eventListenerCaptor.capture());
        MqttClientEventListener eventListener = eventListenerCaptor.getValue();

        String topic = "Any valid string as topic";
        String message = "Any valid string as message";
        byte[] messageBytes = new byte[0];

        when(bytesToStringConverter.convertBytesToString(messageBytes)).thenReturn(message);

        eventListener.messageReceived(topic, messageBytes);

        verify(mqttClientWindow).addReceivedMessage(topic, message);
    }

    @Test
    public void whenMqttSubscribedButtonIsClicked_subscribeOnTheTopics() throws Exception {
        ArgumentCaptor<MqttClientWindowEventListener> eventListenerCaptor = ArgumentCaptor.forClass(MqttClientWindowEventListener.class);
        mqttClientWindowPresenter.initialize();
        verify(mqttClientWindow).setEventListener(eventListenerCaptor.capture());

        MqttClientWindowEventListener eventListener = eventListenerCaptor.getValue();

        String[] topicFilters = new String[0];

        when(mqttClientWindow.getTopicFilters()).thenReturn(topicFilters);

        eventListener.subscribedButtonClicked();

        verify(mqttClient).subscribe(topicFilters);
    }

    @Test
    public void whenMqttSendButtonIsClicked_convertMessageToByteAndSendMessageToMqttClient() throws Exception {
        ArgumentCaptor<MqttClientWindowEventListener> eventListenerCaptor = ArgumentCaptor.forClass(MqttClientWindowEventListener.class);
        mqttClientWindowPresenter.initialize();
        verify(mqttClientWindow).setEventListener(eventListenerCaptor.capture());

        MqttClientWindowEventListener eventListener = eventListenerCaptor.getValue();

        String topic = "Any valid string as topic";
        String message = "Any valid string as message";
        byte[] messageBytes = new byte[0];

        when(mqttClientWindow.getTopic()).thenReturn(topic);
        when(mqttClientWindow.getMessage()).thenReturn(message);
        when(stringToBytesConverter.convertStringToBytes(message)).thenReturn(messageBytes);

        eventListener.sendButtonClicked();

        verify(stringToBytesConverter).convertStringToBytes(message);
        verify(mqttClient).sendMessage(topic, messageBytes);
    }
}
