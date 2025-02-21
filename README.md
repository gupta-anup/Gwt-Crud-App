# Getting Started with GWT: A Step-by-Step Guide

## Introduction

Google Web Toolkit (GWT) is a powerful tool for building web applications in Java. If you are new to GWT and want to set up a project from scratch using Maven, this guide will walk you through the entire process. We will cover project creation, running the development server, understanding the boilerplate code, and deploying the application.

## Prerequisites

Before we begin, ensure you have the following installed on your system:

- Java Development Kit (JDK) 8 or later
- Apache Maven
- IntelliJ IDEA (or any IDE of your choice)

## Step 1: Generate a GWT Project Using Maven

To create a new GWT project, run the following command in your terminal:

```sh
mvn archetype:generate \
  -DarchetypeGroupId=org.codehaus.mojo \
  -DarchetypeArtifactId=gwt-maven-plugin \
  -DarchetypeVersion=2.9.0 \
  -DgroupId=com.nonstopio \
  -DartifactId=gwt-crud-app \
  -Dversion=1.0-SNAPSHOT
```

### Entering the Module Name

Once the command executes, it will prompt you to enter a module name. Type:

```sh
GwtCrudApp
```

This will create a new Maven-based GWT project inside a directory named `gwt-crud-app`.

## Step 2: Open the Project in IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Click **File** → **Open** and navigate to the `gwt-crud-app` directory.
3. Select the `pom.xml` file and click **Open**.
4. IntelliJ will prompt you to import the Maven project; click **Import**.

## Step 3: Understanding the Folder Structure and Boilerplate Code

Once the project is generated, it contains some default files and folders:

### Project Structure

```
gwt-crud-app/
│── src/
│   ├── main/
│   │   ├── java/com/nonstopio/
│   │   │   ├── client/        # Client-side code (Runs in the browser)
│   │   │   │   ├── GwtCrudApp.java  # Entry point of the GWT application
│   │   │   │   ├── GreetingService.java  # Interface for RPC service
│   │   │   │   ├── Messages.java  # Handles text messages for localization
│   │   │   ├── server/        # Server-side code (Runs on Jetty server)
│   │   │   │   ├── GreetingServiceImpl.java  # Implementation of the RPC service
│   │   │   ├── shared/        # Shared code (Used by both client and server)
│   │   │   │   ├── FieldVerifier.java  # Utility class for validating user input
│   │   ├── resources/com/nonstopio/
│   ├── webapp/
│   │   ├── WEB-INF/web.xml  # Web application descriptor
│   │   ├── GwtCrudApp.html  # Main HTML page where the GWT module is loaded
│   │   ├── GwtCrudApp.css  # Stylesheet for the application
│── pom.xml  # Maven configuration file
```

### Flow of the Application

1. When you run the application, `GwtCrudApp.java` (inside the `client` package) is executed first.
2. This file contains the `onModuleLoad()` method, which initializes the UI.
3. The UI elements are defined in `GwtCrudApp.html` and styled in `GwtCrudApp.css`.
4. When a user interacts with the UI, event handlers process the inputs and update the UI dynamically.
5. If backend processing is needed, the client calls `GreetingService`, which is implemented on the server side (`GreetingServiceImpl`).
6. The `FieldVerifier` utility in the `shared` package validates inputs before processing them.

### Code Snippet: Entry Point (`GwtCrudApp.java`)

```java
package com.nonstopio.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class GwtCrudApp implements EntryPoint {
    public void onModuleLoad() {
        Label label = new Label("Hello, GWT!");
        Button button = new Button("Click Me");
        button.addClickHandler(event -> label.setText("Button Clicked!"));
        
        RootPanel.get().add(label);
        RootPanel.get().add(button);
    }
}
```

## Step 4: Build the Project

To generate the WAR (Web Application Archive) file, run the following command:

```sh
mvn war:war
```

This command compiles the project and creates a `.war` file inside the `target/` directory.

## Step 5: Run the GWT Development Mode

Start the GWT development mode using Jetty (an integrated server) by running:

```sh
mvn gwt:run
```

After successful execution, a **GWT Development Mode** window will appear.

## Step 6: Launch the Application

1. In the GWT Development Mode window, locate the **Startup URL** dropdown.
2. Select `GwtCrudApp.html`.
3. Click **Launch Default Browser**.

Your application will now be running in your browser, served by the Jetty development server.

## Conclusion

You have successfully set up a GWT project using Maven, built the WAR file, and run the development server. We also explored the basic structure and flow of the application. From here, you can start developing your web application using Java. In future guides, we will explore UI components, event handling, and deploying your GWT application to a production server.

Happy coding! 🚀