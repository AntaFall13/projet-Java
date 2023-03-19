import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class Demineur extends JFrame implements ActionListener, MouseListener {
 
    // Déclaration des variables
    int nbrColonnes, nbrLignes, numMine;
    int nbLignes = 9, nbColonnes = 9, numMines = 10;
    GridLayout grille = new GridLayout(nbLignes, nbColonnes);
    boolean[] mines = new boolean[nbLignes * nbColonnes];  
    boolean[] clickable = new boolean[nbLignes * nbColonnes];   
    boolean perdu = false; 
    boolean gagne = false;  
    int[] cases = new int[nbLignes * nbColonnes];   
    JButton[] buttons = new JButton[nbLignes * nbColonnes];  
    boolean[] estClique = new boolean[nbLignes * nbColonnes];   
    JMenuItem nouvellePartie = new JMenuItem("Nouveau Jeu"); 
    JMenuItem difficulte = new JMenuItem("OPTIONS");    
    JLabel renseignementDesMines = new JLabel("Bombes: " + numMines + " MARQUE(S): 0");  
    JPanel p = new JPanel();
 
    // Constructeur
    public Demineur() {
        p.setLayout(grille);    
        commencer();
        //Menu
        for (int i = 0; i < (nbLignes * nbColonnes); i++) {
            p.add(buttons[i]);
        }
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu(" Minesweeper ANTA ET RAMATOULAYE");
        nouvellePartie.addActionListener(this); 
        m.add(nouvellePartie);   
        difficulte.addActionListener(this); 
        m.add(difficulte);  
        mb.add(m);  
        this.setJMenuBar(mb);   
        this.add(p); 
        this.add(renseignementDesMines, BorderLayout.SOUTH);     
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        this.setVisible(true); 
    }
 
    // Générer les mines
    public void genererMines() {
        int needed = numMines;  
        while (needed > 0) {   
            int x = (int) Math.floor(Math.random() * nbLignes);
            int y = (int) Math.floor(Math.random() * nbColonnes);   
            if (!mines[(nbLignes * y) + x]) {   
                mines[(nbLignes * y) + x] = true;  
                needed--;   
            }
        }
    }
 
    // Générer les cases
    public void genererCases() {
        for (int x = 0; x < nbLignes; x++) {
            for (int y = 0; y < nbColonnes; y++) {
                int cur = (nbLignes * y) + x;  
                if (mines[cur]) {  
                    cases[cur] = 0; 
                    continue;  
                }
                int temp = 0;   
                boolean l = (x - 1) >= 0;  
                boolean r = (x + 1) < nbLignes;  
                boolean u = (y - 1) >= 0; 
                boolean d = (y + 1) < nbColonnes;   
                int left = (nbLignes * (y)) + (x - 1);  
                int right = (nbLignes * (y)) + (x + 1);
                int up = (nbLignes * (y - 1)) + (x);   
                int upleft = (nbLignes * (y - 1)) + (x - 1);    
                int upright = (nbLignes * (y - 1)) + (x + 1);   
                int down = (nbLignes * (y + 1)) + (x); 
                int downleft = (nbLignes * (y + 1)) + (x - 1); 
                int downright = (nbLignes * (y + 1)) + (x + 1);
                if (u) {   
                    if (mines[up]) {    
                        temp++; 
                    }
                    if (l) {    
                        if (mines[upleft]) {    
                            temp++;
                        }
                    }
                    if (r) {    
                        if (mines[upright]) {   
                            temp++;
                        }
                    }
                }
                if (d) {    
                    if (mines[down]) { 
                        temp++;
                    }
                    if (l) {    
                        if (mines[downleft]) { 
                            temp++;
                        }
                    }
                    if (r) {   
                        if (mines[downright]) { 
                            temp++;
                        }
                    }
                }
                if (l) {
                    if (mines[left]) {
                        temp++;
                    }
                }
                if (r) {    
                    if (mines[right]) { 
                        temp++;
                    }
                }
                cases[cur] = temp;  
            }
        }
    }
    //Demander la difficulte
    public void difficulte() {
        String[] difficulte = {"Debutant", "Intermediaire", "Expert"};
        int choix = JOptionPane.showOptionDialog(null, "Choisissez votre Niveaux", "Niveaux", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, difficulte, difficulte[0]);
        if (choix == 0) {
            nbrLignes = 9;
            nbrColonnes = 9;
            numMine = 10;
        } else if (choix == 1) {
            nbrLignes = 16;
            nbrColonnes = 16;
            numMine = 40;
        } else if (choix == 2) {
            nbrLignes = 16;
            nbrColonnes = 30;
            numMine = 99;
        }
    }
 
    // Commencer une partie
    public void commencer() {
        difficulte();
        nbLignes = nbrLignes;
        nbColonnes = nbrColonnes;
        numMines = numMine;
        mines = new boolean[nbLignes * nbColonnes];
        cases = new int[nbLignes * nbColonnes];
        commencerPartie(nbrLignes, nbrColonnes, numMines, mines, cases);
    }

    void commencerPartie(int nbLignes, int nbColonnes, int numMines, boolean[] mines, int[] cases) {
        mines = new boolean[nbLignes * nbColonnes];
        cases = new int[nbLignes * nbColonnes];
        estClique = new boolean[nbLignes * nbColonnes];
        clickable = new boolean[nbLignes * nbColonnes];
        buttons = new JButton[nbLignes * nbColonnes];
        estClique = new boolean[nbLignes * nbColonnes];
        for (int x = 0; x < nbLignes; x++) {
            for (int y = 0; y < nbColonnes; y++) {
                mines[(nbLignes * y) + x] = false;
                estClique[(nbLignes * y) + x] = false;
                clickable[(nbLignes * y) + x] = true;
                buttons[(nbLignes * y) + x] = new JButton( /*"" + ( x * y )*/);
                buttons[(nbLignes * y) + x].setPreferredSize(new Dimension(
                        45, 45));
                buttons[(nbLignes * y) + x].addActionListener(this);
                buttons[(nbLignes * y) + x].addMouseListener(this);
            }
        }

        genererMines();
        genererCases();
    }
 
    // Générer les cases autour de la case cliquée
    public void recommencer() {
        this.remove(p);
        p = new JPanel();
        grille = new GridLayout(nbLignes, nbColonnes);
        p.setLayout(grille);
        buttons = new JButton[nbLignes * nbColonnes];
        mines = new boolean[nbLignes * nbColonnes];
        estClique = new boolean[nbLignes * nbColonnes];
        clickable = new boolean[nbLignes * nbColonnes];
        cases = new int[nbLignes * nbColonnes];
        commencer();
        for (int i = 0; i < (nbLignes * nbColonnes); i++) {
            p.add(buttons[i]);
        }
        this.add(p);
        this.pack();
        genererMines();
        genererCases();
    }
 
    public void reprendre() {
        for (int x = 0; x < nbLignes; x++) {
            for (int y = 0; y < nbColonnes; y++) {
                mines[(nbLignes * y) + x] = false;
                estClique[(nbLignes * y) + x] = false;
                clickable[(nbLignes * y) + x] = true;
                buttons[(nbLignes * y) + x].setEnabled(true);
                buttons[(nbLignes * y) + x].setText("");
            }
        }
        genererMines();
        genererCases();
        perdu = false;
        renseignementDesMines.setText("BOMBES: " + numMines + " FLAGS: 0");    
    }
 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == difficulte) {  
            nbLignes = Integer.parseInt((String) JOptionPane.showInputDialog(   
                    this, "nbLignes", "nbLignes", JOptionPane.PLAIN_MESSAGE, null,  null, 10));
            nbColonnes = Integer.parseInt((String) JOptionPane.showInputDialog(
                    this, "Columns", "Columns", JOptionPane.PLAIN_MESSAGE,null, null, 10));
            numMines = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Mines", "Mines",
                    JOptionPane.PLAIN_MESSAGE, null, null, 10));
            recommencer();
        }
        if (!gagne) { 
            for (int x = 0; x < nbLignes; x++) {    
                for (int y = 0; y < nbColonnes; y++) { 
                    if (e.getSource() == buttons[(nbLignes * y) + x]   
                            && !gagne && clickable[(nbLignes * y) + x]) { 
                        verifierCheck(x, y);  
                        break;
                    }
                }
            }
        }
        if (e.getSource() == nouvellePartie) {  
            reprendre();   
            gagne = false; 
            return;
 
        }
        checkWin(); 
    }
 
    public void mouseEntered(MouseEvent e) {    
    }
 
    public void mouseExited(MouseEvent e) {
    }
 
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 3) { 
            int n = 0;
            for (int x = 0; x < nbLignes; x++) {
                for (int y = 0; y < nbColonnes; y++) {
                    if (e.getSource() == buttons[(nbLignes * y) + x]) { 
                        clickable[(nbLignes * y) + x] = !clickable[(nbLignes * y)  + x]; 
                    }
                    if (!estClique[(nbLignes * y) + x]) {  
                        if (!clickable[(nbLignes * y) + x]) {  
                            buttons[(nbLignes * y) + x].setText("F");  
                            n++;   
                        } else {
                            buttons[(nbLignes * y) + x].setText("");    
                        }
                        renseignementDesMines.setText("Bombes: " + numMines + "FLAGS: " + n);  
                    }
                }
            }
        }
    }
 
    public void mouseReleased(MouseEvent e) {
    }
 
    public void mouseClicked(MouseEvent e) {
    }
 
    // Vérifier si la partie est gagnée ou perdue
    public void verifierCheck(int x, int y) {
        int cur = (nbLignes * y) + x;   
        boolean l = (x - 1) >= 0;   
        boolean r = (x + 1) < nbLignes;  
        boolean u = (y - 1) >= 0;   
        boolean d = (y + 1) < nbColonnes;  
        int left = (nbLignes * (y)) + (x - 1); 
        int right = (nbLignes * (y)) + (x + 1);
        int up = (nbLignes * (y - 1)) + (x);    
        int upleft = (nbLignes * (y - 1)) + (x - 1);    
        int upright = (nbLignes * (y - 1)) + (x + 1);  
        int down = (nbLignes * (y + 1)) + (x);  
        int downleft = (nbLignes * (y + 1)) + (x - 1);  
        int downright = (nbLignes * (y + 1)) + (x + 1); 
        
        estClique[cur] = true;  
        buttons[cur].setEnabled(false);   
        if (cases[cur] == 0 && !mines[cur] && !perdu && !gagne) {   
            if (u && !gagne) {  
                if (!estClique[up] && !mines[up]) { 
                    estClique[up] = true;  
                    buttons[up].doClick();  
                }   
                if (l && !gagne) { 
                    if (!estClique[upleft] && cases[upleft] != 0 && !mines[upleft]) {  
                        estClique[upleft] = true;   
                        buttons[upleft].doClick(); 
                    }
                }
                if (r && !gagne) {  
                    if (!estClique[upright] && cases[upright] != 0   && !mines[upright]) {  
                        estClique[upright] = true;  
                        buttons[upright].doClick();
                    }
                }
            }
            if (d && !gagne) {  
                if (!estClique[down] && !mines[down]) {     
                    estClique[down] = true;
                    buttons[down].doClick();
                }
                if (l && !gagne) {  
                    if (!estClique[downleft] && cases[downleft] != 0 && !mines[downleft]) {
                        estClique[downleft] = true;
                        buttons[downleft].doClick();
                    }
                }
                if (r && !gagne) {  
                    if (!estClique[downright] &&  cases[downright] != 0 && !mines[downright]) { 
                        estClique[downright] = true;
                        buttons[downright].doClick();
                    }
                }
            }
            if (l && !gagne) {  
                if (!estClique[left] && !mines[left]) {
                    estClique[left] = true;
                    buttons[left].doClick();
                }
            }
            if (r && !gagne) {  
                if (!estClique[right] && !mines[right]) { 
                    estClique[right] = true;
                    buttons[right].doClick();
                }
            }
        } else {   
            buttons[cur].setText("" + cases[cur]);  
            if (!mines[cur] && cases[cur] == 0) {  
                buttons[cur].setText("");
            }
        }
        if (mines[cur] && !gagne) {
            buttons[cur].setText("B"); 
            doLose();  
        }
    }
 
    // Partie Perdue
    public void checkWin() {    
        for (int x = 0; x < nbLignes; x++) {   
            for (int y = 0; y < nbColonnes; y++) { 
                int cur = (nbLignes * y) + x;  
                if (!estClique[cur]) {  
                    if (mines[cur]) {   
                        continue; 
                    } else {   
                        return;  
                    }
                }
            }
        }
 
        doWin();    
    }
 
    // Partie gagner
    public void doWin() {
        if (!perdu && !gagne) { 
            gagne = true;  
            JOptionPane.showMessageDialog(null, "Voulez vous redemarrer une nouvelle partie ?", "VOUS AVEZ GAGNÉ(E)",
                JOptionPane.INFORMATION_MESSAGE);   
            nouvellePartie.doClick();   
        }
    }
 
    // Partie perdu
    public void doLose() {
        if (!perdu && !gagne) {
            perdu = true;
            for (int i = 0; i < nbLignes * nbColonnes; i++) {
                if (!estClique[i]) {   
                    buttons[i].doClick(0);  
                }
            }
            JOptionPane.showMessageDialog(null,"Voulez vous redemarrer une nouvelle partie ?", "VOUS AVEZ PERDU(E)",
                JOptionPane.ERROR_MESSAGE);
            reprendre();    // Reinitialisation de la partie
        }
    }

    public static void main(String[] args) {
        new Demineur();
    }
}