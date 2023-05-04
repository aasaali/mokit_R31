package com.example.mokit_r31;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class LaskuController {
    @FXML
    private ListView laskuLista;
    @FXML
    private Button buttonHae;
    @FXML
    private Button buttonPaivita;
    @FXML
    private Button buttonLuoUusi;
    @FXML
    private Button buttonPoista;

   /** @FXML
    private void tallennaVarausButton(ActionEvent event) {
        int asiakas = Integer.parseInt(asiakasTf.getText());
        int mokki = Integer.parseInt(mokkiTf.getText());
        LocalDateTime alkupaiva = alkupaivaDP.getValue().atStartOfDay();
        LocalDateTime loppupaiva = loppupaivaDP.getValue().atStartOfDay();
        Varaus varaus = new Varaus(asiakas,mokki,alkupaiva,loppupaiva);
        try {
            VaraustenHallinta hallinta = new VaraustenHallinta();
            hallinta.lisaaVaraus(varaus);
            tallennaVarausBt.setText("Vahvistettu");
        }
        catch (SQLException e) {
            // Virheen käsittely
        }
    }
   */

}