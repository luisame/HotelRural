package utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("utilidades/ConfiguracionDB.properties")) {
            if (input == null) {
                throw new RuntimeException("No se pudo encontrar el archivo ConfiguracionDB.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error al cargar el archivo de configuración.", ex);
        }
    }

    public static Properties getProperties() {
        return properties;
    }
}
