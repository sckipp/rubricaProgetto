package Gui;

import Classi.*;
import Controller.*;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import DAO.StampaContattoDAO;
import ImplementazioniDAO.*;

public class Homepage extends JFrame {

    //ATTRIBUTI
    private JPanel cardHomepage;
    private JPanel homepage;
    private JPanel paneBase;
    private JPanel paneTendina;
    private JScrollPane JScrollBarListaContatti;
    private JButton btnCreaNuovoContatto;
    private JButton btnSwitchTema;
    private JButton stampaListaContattiButton;
    private JButton lineeApertura;


    //OGGETTI
    private Timer timer;
    Controller control;

    static ArrayList<Contatti> contattiDB = new ArrayList<>();

    public Homepage(Controller controller) throws SQLException, IOException {//Da spostare nel controller

        control = controller;
        impostazioniGeneraliHomepage();
        btnSwitchTema();
        stampaContatti();

    }

    //METODI
    public void btnSwitchTema(){
        btnSwitchTema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    timerSwitchTema();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void stampaContatti() throws SQLException {
        contattiDB.clear();
        StampaContattoDAO stampaContatto = new StampaContattoPostgreSQL();
        contattiDB = stampaContatto.stampaContatti();
        int i = 0;
        paneBase.removeAll();
        while(i < contattiDB.size()){
            JPanel paneLista = new JPanel();
            paneLista.setLayout(new GridLayout(0, 1));
            JButton btnSchedaContatto = new JButton();
            btnSchedaContatto.setText(contattiDB.get(i).getNome() +" "+ contattiDB.get(i).getCognome());//Per il momento mi accontento solo di dargli queste informazioni al JButton
            btnSchedaContatto.setActionCommand(contattiDB.get(i).getCellulare());
            btnSchedaContatto.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        control.clickAudio();
                        control.newSchedaInfoContatto();    //Testing svuotamento dei campi nella scheda contatto
                        control.getSchedaInfoContatto().riempimentoInfoContatto(e.getActionCommand());
                        control.switchJPanelInView(control.getSchedaInfoContatto().getSchedaInfoContattoPane());
                        JScrollBarListaContatti.setBorder(BorderFactory.createTitledBorder(""));
                    } catch (SQLException | UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            paneLista.add(btnSchedaContatto);
            paneBase.setLayout(new GridLayout(0,1));
            paneBase.add(paneLista);

            validate();
            i++;
        }
        paneBase.repaint();
        paneBase.validate();

    }

    public void impostazioniGeneraliHomepage() throws IOException {
        cardHomepage = new JPanel();
        cardHomepage.setLayout(new CardLayout());
        cardHomepage.add(homepage);

        setTitle("Homepage");
        setContentPane(cardHomepage);
        setSize(600, 600);

        setDefaultCloseOperation(EXIT_ON_CLOSE); //Serve a terminare il programma quando si preme la X
        setLocation(300, 300);
        JScrollBarListaContatti.getVerticalScrollBar().setUnitIncrement(7);

        TemaScuro();  //INIZIALIZAZZIONE DEL TEMA SCURO BY DEFAULT

    }

    public void timerSwitchTema() throws IOException {
        if (timer == null) {
            timer = new Timer(100, new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                }
            });
            timer.start();
            TemaChiaro();
            control.getCreaContatto().temaChiaro();
        } else if (timer.isRunning()) {
            timer.stop();
            TemaScuro();
            control.getCreaContatto().temaScuro();
        } else {
            timer.start();
            TemaChiaro();
            control.getCreaContatto().temaChiaro();
        }
    }

    //TEMA SCURO SET BY DEFAULT
    public void TemaScuro() throws IOException {
        FlatDarculaLaf.setup();
        SwingUtilities.updateComponentTreeUI(homepage);

        //GUI HOMEPAGE
        try {
            Image imgSwitchTema = ImageIO.read(new File("Immagini/imgTemaScuro.png")); //Da settare la dimensione delle ICONE (PRESI DI MISURA GIUSTA COSI DA NON
            btnSwitchTema.setIcon(new ImageIcon(imgSwitchTema));
        } catch (Exception e) {                                                                                //AVERE NECESSITA' PER IL MOMENTO)
            System.out.println("Icona btnTemaScuro non caricata");
        }
        btnSwitchTema.setFocusable(false);

        //Modifica del JButton ********* TESTING **********
        textureTasti();
    }


    //TEMA CHIARO
    public void TemaChiaro() throws IOException {

        FlatLightLaf.setup();
        SwingUtilities.updateComponentTreeUI(homepage);     //Serve a fare il refresh dei componenti a runtime (UTILE PER DARK MODE TO LIGHT)


        try {
            Image imgSwitchTema = ImageIO.read(new File("Immagini/imgTemaChiaro.png")); //Da settare la dimensione (PRESI DI MISURA GIUSTA COSI DA NON
            btnSwitchTema.setIcon(new ImageIcon(imgSwitchTema));
        } catch (Exception e) {                                                                                //AVERE NECESSITA' PER IL MOMENTO)
            System.out.println("Icona btnTemaChiaroCHIARO non caricata");
        }
        btnSwitchTema.setFocusable(false);

        //Modifica del JButton ********* TESTING **********
        try {
            Image imgCreaNuovoContatto = ImageIO.read(new File("Immagini/imgCreaNuovoContattoCHIARO.png")); //Da settare la dimensione (PRESI DI MISURA GIUSTA COSI DA NON
            btnCreaNuovoContatto.setIcon(new ImageIcon(imgCreaNuovoContatto));
        } catch (Exception e) {                                                                                //AVERE NECESSITA' PER IL MOMENTO)
            System.out.println("Icona btnCreaNuovoContattoCHIARO non caricata");
        }
        btnCreaNuovoContatto.setFocusable(false);

        textureTasti();
    }

    public void textureTasti() throws IOException {
        //btnCreaNuovoContatto SETTAGGIO ESTETICO
        ImageIcon imgCreaNuovoContatto = new ImageIcon("Immagini/imgCreaNuovoContatto24px.png");
        ImageIcon imgCreaNuovoContattoGrande = new ImageIcon("Immagini/imgCreaNuovoContatto32px.png");
        try {
             //Da settare la dimensione (PRESI DI MISURA GIUSTA COSI DA NON
            btnCreaNuovoContatto.setIcon(imgCreaNuovoContatto);
            btnCreaNuovoContatto.setMargin(new Insets(0,0,0,0));
            btnCreaNuovoContatto.setContentAreaFilled(false);
            btnCreaNuovoContatto.setBorderPainted(false);
            btnCreaNuovoContatto.setBorder(null);
            btnCreaNuovoContatto.setFocusPainted(false);
            btnCreaNuovoContatto.setOpaque(true);
        } catch (Exception e) {                                                                                //AVERE NECESSITA' PER IL MOMENTO)
            System.out.println("Icona btnCreaNuovoContattoSCURO non caricata");
        }
        btnCreaNuovoContatto.setFocusable(false);

        btnCreaNuovoContatto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    control.rollOverAudio();
                    control.switchJPanelInView(control.getCreaContatto().getCreaContatto());
                    JScrollBarListaContatti.setBorder(BorderFactory.createTitledBorder(""));
                    control.newCreaContatto();
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCreaNuovoContatto.setIcon(imgCreaNuovoContattoGrande);
                try {
                    control.rollOverAudio();
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnCreaNuovoContatto.setIcon(imgCreaNuovoContatto);
            }
        });


    }

    //GETTER SETTER

    public static ArrayList<Contatti> getContattiDB() {
        return contattiDB;
    }

    public static void setContattiDB(ArrayList<Contatti> contattiDB) {
        Homepage.contattiDB = contattiDB;
    }

    public JButton getLineeApertura() {
        return lineeApertura;
    }

    public void setLineeApertura(JButton lineeApertura) {
        this.lineeApertura = lineeApertura;
    }

    public JButton getLineeChiusura() {
        return lineeApertura;
    }

    public void setLineeChiusura(JButton lineeChiusura) {
        this.lineeApertura = lineeChiusura;
    }

    public JPanel getPaneTendina() {
        return paneTendina;
    }

    public void setPaneTendina(JPanel paneTendina) {
        this.paneTendina = paneTendina;
    }

    public JButton getStampaListaContattiButton() {
        return stampaListaContattiButton;
    }

    public void setStampaListaContattiButton(JButton stampaListaContattiButton) {
        this.stampaListaContattiButton = stampaListaContattiButton;
    }

    public JScrollPane getJScrollBarListaContatti() {
        return JScrollBarListaContatti;
    }

    public void setJScrollBarListaContatti(JScrollPane JScrollBarListaContatti) {
        this.JScrollBarListaContatti = JScrollBarListaContatti;
    }

    public JPanel getPaneBase() {
        return paneBase;
    }

    public void setPaneBase(JPanel paneBase) {
        this.paneBase = paneBase;
    }

    public JPanel getCardHomepage() {
        return cardHomepage;
    }

    public void setCardHomepage(JPanel cardHomepage) {
        this.cardHomepage = cardHomepage;
    }

    public JButton getBtnCreaNuovoContatto() {
        return btnCreaNuovoContatto;
    }

    public void setBtnCreaNuovoContatto(JButton btnCreaNuovoContatto) {
        this.btnCreaNuovoContatto = btnCreaNuovoContatto;
    }

    public JPanel getHomepage() {
        return homepage;
    }

    public void setHomepage(JPanel homepage) {
        this.homepage = homepage;
    }

    public JButton getBtnSwitchTema() {
        return btnSwitchTema;
    }

    public void setBtnSwitchTema(JButton btnSwitchTema) {
        this.btnSwitchTema = btnSwitchTema;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }


}


