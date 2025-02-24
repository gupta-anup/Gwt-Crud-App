package com.nonstopio.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class GwtCrudApp implements EntryPoint {
  public void onModuleLoad() {
    PersonListPanel personListPanel = new PersonListPanel();
    RootPanel.get("personContainer").add(personListPanel);
  }
}