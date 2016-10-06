package com.nafeezabrar.mqtt.client.ui.controllers;

import com.nafeezabrar.mqtt.client.events.listening.MqttClientWindowEventListener;
import com.nafeezabrar.mqtt.client.ui.MqttClientWindow;
import com.nafeezabrar.mqtt.client.ui.UiApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class MainController implements Initializable, MqttClientWindow {
    public TextField topicTextField;
    public TextField messageTextField;
    public Button sendButton;
    public ListView receivedMessagesListView;
    public Button subscribeButton;
    public TextArea topicFiltersTextArea;
    protected ObservableList<String> receivedMessagesList = FXCollections.observableList(new ArrayList<>());
    protected MqttClientWindowEventListener eventListener;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receivedMessagesListView.setItems(receivedMessagesList);
        UiApplication.ActualWindow = this;
        UiApplication.activated();
    }

    @Override
    public String getTopic() {
        return topicTextField.getText();
    }

    @Override
    public void setTopic(String topic) {
        topicTextField.setText(topic);
    }

    @Override
    public String getMessage() {
        return messageTextField.getText();
    }

    @Override
    public void setMessage(String message) {
        messageTextField.setText(message);
    }

    @Override
    public void setEventListener(MqttClientWindowEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void addReceivedMessage(String topic, String message) {
        receivedMessagesList.add(String.format("Topic: %s, Messages: %s", topic, message));
    }

    @Override
    public String[] getTopicFilters() {
        String text = topicFiltersTextArea.getText();
        StringTokenizer topicFilterTokenizer = new StringTokenizer(text, "\n \t,");
        String[] topicFilters = new String[topicFilterTokenizer.countTokens()];
        int i = 0;
        while (topicFilterTokenizer.hasMoreTokens()) {
            String topicFilter = topicFilterTokenizer.nextToken().trim();
            topicFilters[i++] = topicFilter;
        }
        return topicFilters;
    }

    public void sendButtonAction(ActionEvent actionEvent) {
        if (eventListener != null)
            eventListener.sendButtonClicked();
    }

    public void subscribedButtonAction(ActionEvent actionEvent) {
        if (eventListener != null)
            eventListener.subscribedButtonClicked();
    }
}
