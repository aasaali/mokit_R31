package com.example.mokit_r31;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MuokkaaAlueController {

        @FXML
        private TextField TxAlueId;

        @FXML
        private TextField TxAlueNimi;

        @FXML
        private Button BtTallenna;
        private Alue alue;
        public void setAlue(Alue alue) {
            this.alue = alue;
            naytaAlueenTiedot();

        }
        Tietokanta tietokanta = new Tietokanta();
        AlueidenHallinta alueidenHallinta = new AlueidenHallinta(tietokanta);
        public void alustaMokkienhallinta(MokkienHallinta mokkienHallinta){
            this.alueidenHallinta = alueidenHallinta;
        }
        @FXML
        private void initialize() {

            BtTallenna.setOnAction(e -> {
                BtTallenna();
            });
        }
        @FXML
        private void BtTallenna() {
            System.out.println("Testi");
            // Tallenna käyttäjän tekemät muutokset tietokantaan
            lisaaAlueenTiedot();
            alueidenHallinta.paivitaAlueenTiedot(alue);

            // Sulje ikkuna, jos tallennus onnistui
            Stage stage = (Stage) BtTallenna.getScene().getWindow();
            stage.close();

        }

        private void lisaaAlueenTiedot() {
            alue.setNimi(TxAlueNimi.getText());
        }


        public void setAlueidenHallinta(AlueidenHallinta alueidenHallintaHallinta) {

            this.alueidenHallinta= alueidenHallintaHallinta;
        }
        @FXML
        private void naytaAlueenTiedot() {
            TxAlueNimi.setText((alue.getNimi()));
            TxAlueId.setText(String.valueOf(alue.getAlueId()));

        }
    }


