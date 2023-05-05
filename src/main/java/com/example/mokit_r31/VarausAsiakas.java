package com.example.mokit_r31;

import java.sql.Connection;
import java.sql.SQLException;

public class VarausAsiakas {

        private Varaus varaus;
        private Asiakas asiakas;
        Tietokanta tietokanta = new Tietokanta();

        public VarausAsiakas(Varaus varaus, Asiakas asiakas) {
            this.varaus = varaus;
            this.asiakas = asiakas;
        }

        public Varaus getVaraus() {
            return varaus;
        }

        public void setVaraus(Varaus varaus) {
            this.varaus = varaus;
        }

        public Asiakas getAsiakas() {
            return asiakas;
        }

        public void setAsiakas(Asiakas asiakas) {
            this.asiakas = asiakas;
        }



}
