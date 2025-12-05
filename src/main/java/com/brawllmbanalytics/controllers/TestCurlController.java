package com.brawllmbanalytics.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
public class TestCurlController {

    @GetMapping("/test-curl")
    public String ejecutarCurl() {
        try {

            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjIwMTNkYWJjLTEzMzQtNGIyYS05M2ExLWE1NDg0YzM2NjFiYSIsImlhdCI6MTc2NDcxMzQzNSwic3ViIjoiZGV2ZWxvcGVyLzgyNDFkZGUzLWRjN2MtMTc3Yy1lOWNmLTEzN2Q4MzAyNjczNyIsInNjb3BlcyI6WyJicmF3bHN0YXJzIl0sImxpbWl0cyI6W3sidGllciI6ImRldmVsb3Blci9zaWx2ZXIiLCJ0eXBlIjoidGhyb3R0bGluZyJ9LHsiY2lkcnMiOlsiMTM5LjQ3LjUyLjQ2Il0sInR5cGUiOiJjbGllbnQifV19.R3htXyxc8Ng5PwV-liflnyofttzjcfHn8V6fk4pbhTi2jbFWE7faA2P6zv4sy8pKNFdg-OPDZQDwqCXgFyPvGw";

            String command =
                    "powershell -Command \"Invoke-WebRequest -Uri 'https://api.brawlstars.com/v1/players/%23GLUQGJ9P' -Headers @{ Authorization = 'Bearer " 
                    + token + "' } | Select-Object -ExpandProperty Content\"";

            Process process = Runtime.getRuntime().exec(command);

            BufferedReader stdInput =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            BufferedReader stdError =
                    new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder salida = new StringBuilder("OUTPUT:\n");
            StringBuilder errores = new StringBuilder("\nERRORS:\n");

            String line;

            // Leer salida normal
            while ((line = stdInput.readLine()) != null) {
                salida.append(line).append("\n");
            }

            // Leer errores
            while ((line = stdError.readLine()) != null) {
                errores.append(line).append("\n");
            }

            process.waitFor();

            return salida.toString() + errores.toString();

        } catch (Exception e) {
            return "ERROR ejecutando PowerShell: " + e.getMessage();
        }
    }
}
