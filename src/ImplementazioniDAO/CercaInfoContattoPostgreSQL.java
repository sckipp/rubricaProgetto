package ImplementazioniDAO;

import Classi.Contatti;
import ConnessioneDB.Connessione;
import DAO.CercaInfoContattoDAO;

import java.sql.*;

public class CercaInfoContattoPostgreSQL implements CercaInfoContattoDAO{

    private Connection conn;


    public CercaInfoContattoPostgreSQL(){

    }


    //METODI
    @Override
    public Contatti cercaInfoContatti(String dati) throws SQLException {

        try{
            conn = Connessione.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Contatti contatto = new Contatti();

        String queryCercaInfoContatto = ("SELECT * FROM Contatto as c JOIN Email as e ON c.cellulare = e.cellulare " +
                                         "JOIN Indirizzo as i ON c.cellulare = i.cellulare WHERE c.cellulare = '"+dati+"'");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(queryCercaInfoContatto);

        while (rs.next()) {

            String nome = rs.getString("nome");
            contatto.setNome(nome);
            String cognome = rs.getString("cognome");
            contatto.setCognome(cognome);
            String cellulare = rs.getString("cellulare");
            contatto.setCellulare(cellulare);
            String fisso = rs.getString("fisso");
            contatto.setFisso(fisso);
            String email = rs.getString("email");
            contatto.setEmail(email);
            String indirizzo = rs.getString("via" );
            String civico = rs.getString("civico");
            String cap = rs.getString("cap");
            String citta = rs.getString("citta");
            String nazione = rs.getString("nazione");
            contatto.setIndirizzo(indirizzo+", "+civico+", "+cap+", "+citta+", "+nazione);
            String foto = rs.getString("foto");
            contatto.setFoto(foto);

        }
        conn.close();
        return contatto;
    }

}
