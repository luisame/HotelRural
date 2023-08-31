/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Habitaciones;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import utilidades.UsuarioInfo;

/**
 * FXML Controller class
 *
 * @author luisa
 */
public class HabitacionesController implements Initializable {

    private int nivelAcceso;
    private UsuarioInfo usuarioInfo;

    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    } /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
