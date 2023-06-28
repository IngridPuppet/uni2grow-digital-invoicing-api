# uni2grow-digital-invoicing-api

## Synopsis

As part of a coding interview at Uni2Grow Cameroun SARL, this project builds a REST API that exposes invoicing capabilities.

## Guide

1. Create a new MariaDB/MySQL database in your local environment, and override pertaining configuration entries in
   `src/main/resources/application-default.yml`. Since the file is uncommitted, please create it.

   ```yaml
   # Example
   spring:
    datasource:
      url: jdbc:mariadb://localhost:3306/digital_invoicing
      username: root
      password: toor
   ```
   
2. Next, run the provided `AppDataFakerTests` class to populate the database with fake data so as to demonstrate operability.

3. Boot the application as per usage.

4. Hit the root path `http://localhost:8092` to test the app is up and running.

   ```json
   {
     "app": "DigitalInvoice",
     "clk": "2023-06-28T03:13:47.653265265Z"
   }
   ```

5. The inventory of available endpoints is parsed by Swagger at `http://localhost:8092/swagger-ui/index.html`.
