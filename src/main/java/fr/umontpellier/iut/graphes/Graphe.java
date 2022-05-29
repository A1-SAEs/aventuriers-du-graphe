package fr.umontpellier.iut.graphes;


import java.util.ArrayList;
import java.util.Iterator;

public class Graphe {
    /**
     * matrice d'adjacence du graphe, un entier supérieur à 0 représentant la distance entre deux sommets
     * mat[i][i] = 0 pour tout i parce que le graphe n'a pas de boucle
     */
    private final int[][] mat;

    /**
     * Construit un graphe à n sommets
     *
     * @param n le nombre de sommets du graphe
     */
    public Graphe(int n) {
        mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = 0;
            }
        }
    }

    /**
     * @return le nombre de sommets
     */
    public int nbSommets() {
        return mat.length;
    }

    /**
     * Supprime l'arête entre les sommets i et j
     *
     * @param i un entier représentant un sommet
     * @param j un autre entier représentant un sommet
     */
    public void supprimerArete(int i, int j) {
        mat[i][j] = 0;
        mat[j][i] = 0;
    }

    /**
     * @param i un entier représentant un sommet
     * @param j un autre entier représentant un sommet
     * @param k la distance entre i et j (k>0)
     */
    public void ajouterArete(int i, int j, int k) {
        mat[i][j] = k;
        mat[j][i] = k;
    }

    /*** 
     * @return le nombre d'arête du graphe
     */
    public int nbAretes() {
        int nbAretes = 0;
        for (int i = 0; i < mat.length; i++) {
            for (int j = i + 1; j < mat.length; j++) {
                if (mat[i][j] > 0) {
                    nbAretes++;
                }
            }
        }
        return nbAretes;
    }

    /**
     * @param i un entier représentant un sommet
     * @param j un autre entier représentant un sommet
     * @return vrai s'il existe une arête entre i et j, faux sinon
     */
    public boolean existeArete(int i, int j) {
        return mat[i][j] > 0 && mat[j][i] > 0;
    }

    /**
     * @param v un entier représentant un sommet du graphe
     * @return la liste des sommets voisins de v
     */
    public ArrayList<Integer> voisins(int v) {
        ArrayList<Integer> voisins = new ArrayList<>();
        for(int i=0; i<mat[v].length; i++){
            if(existeArete(v, i)){
                voisins.add(i);
            }
        }
        return voisins;
    }

    /**
     * @return une chaîne de caractères permettant d'afficher la matrice mat
     */
    public String toString() {
        StringBuilder res = new StringBuilder("\n");
        for (int[] ligne : mat) {
            for (int j = 0; j < mat.length; j++) {
                String x = String.valueOf(ligne[j]);
                res.append(x);
            }
            res.append("\n");
        }
        return res.toString();
    }

    /**
     * Calcule la classe de connexité du sommet v
     *
     * @param v un entier représentant un sommet
     * @return une liste d'entiers représentant les sommets de la classe de connexité de v
     */
    public ArrayList<Integer> calculerClasseDeConnexite(int v) {
        ArrayList<Integer> rouge = new ArrayList<>();
        ArrayList<Integer> bleu = new ArrayList<>();
        bleu.add(v);
        while (!bleu.isEmpty()) {
            int sommetActuel = bleu.get(0);
            for (int sommet : voisins(sommetActuel)) {
                if (!rouge.contains(sommet) && !bleu.contains(sommet)) {
                    bleu.add(sommet);
                }
            }
            rouge.add(bleu.remove(0));
        }
        return rouge;
    }

    /**
     * @return la liste des classes de connexité du graphe
     */
    public ArrayList<ArrayList<Integer>> calculerClassesDeConnexite() {
        ArrayList<Integer> listeSommets = new ArrayList<>();
        for(int i = 0; i<nbSommets(); i++){
            listeSommets.add(i);
        }

        ArrayList<ArrayList<Integer>> listeClassesDeConnexite = new ArrayList<>();

        while(!listeSommets.isEmpty()){
            ArrayList<Integer> classe = calculerClasseDeConnexite(listeSommets.remove(0));
            listeClassesDeConnexite.add(classe);
            listeSommets.removeAll(classe);
        }
        return listeClassesDeConnexite;
    }


    /**
     * @return le nombre de classes de connexité
     */
    public int nbCC() {
        return calculerClassesDeConnexite().size();
    }

    /**
     * @param u un entier représentant un sommet
     * @param v un entie représentant un sommet
     * @return vrai si (u,v) est un isthme, faux sinon
     */
    public boolean estUnIsthme(int u, int v) {
        if (!existeArete(u,v)){
            return false;
        }
        int distance = mat[u][v];
        this.supprimerArete(u,v);
        boolean reponse = true;
        if (this.calculerClasseDeConnexite(u).contains(v)){
                 reponse = false;
        }
        this.ajouterArete(u,v,distance);
        return reponse;
    }


    /**
     * Calcule le plus long chemin présent dans le graphe
     *
     * @return une liste de sommets formant le plus long chemin dans le graphe
     */
    public ArrayList<Integer> plusLongChemin() {
        ArrayList<Integer> plusLong = new ArrayList<>();

        return plusLong;
    }

    /**
     * @return vrai s'il existe un parcours eulérien dans le graphe, faux sinon
     */
    public boolean existeParcoursEulerien() {
        int sommetsImpairs = 0;
        if(this.nbCC() != 1){
            return false;
        }

        for(int i=0;i<mat.length;i++){
            int degre =0;
            for(int j=0;j< mat.length;j++){
                if (mat[i][j]!=0){
                    degre++;
                }
            }
            if(degre%2!=0){
                sommetsImpairs++;
            }
        }

        return sommetsImpairs==0 || sommetsImpairs==2;
    }

    /**
     * @return vrai si le graphe est un arbre, faux sinon
     */
    public boolean estUnArbre() {
        return nbCC() == 1 && nbAretes() == nbSommets()-1;
    }

    /**
     * @return vrai si le graphe est une forêt, faux sinon
     */
    public boolean estUneForet() {
        if (this.nbCC()<2){
            return false;
        }

        return nbAretes() == nbSommets()-nbCC();
    }
}