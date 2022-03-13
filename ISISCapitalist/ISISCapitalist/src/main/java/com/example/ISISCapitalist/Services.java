/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ISISCapitalist;

import generated.PallierType;
import generated.ProductType;
import generated.World;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import static org.apache.tomcat.jni.User.username;

/**
 *
 * @author Johana Dahan
 */
public class Services {

    private World world;

    World readWorldFromXml(String username) {
        JAXBContext jaxbContext;
        InputStream input = null;
    try {
        input = new FileInputStream(username + "file.xml");

    } catch (Exception ex) {
        input = getClass().getClassLoader().getResourceAsStream("world.xml");
    }
        try {
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Unmarshaller u = cont.createUnmarshaller();
            world = (World) u.unmarshal(input);
            input.close();
            }
          catch (Exception ex) {
            System.out.println("erreur du fichier" + ex.getMessage());
            ex.printStackTrace();

        }

        return world;
    }

    void saveWordlToXml(World world, String username) {
        JAXBContext jaxbContext;
        try {
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Marshaller m = cont.createMarshaller();
            OutputStream output = new FileOutputStream(username+"file.xml");
            m.marshal(world, output);
            output.close();
        } catch (Exception ex) {
            System.out.println("erreur du fichier" + ex.getMessage());
            ex.printStackTrace();
        }

    }

    World getWorld(String username) {
        World world = readWorldFromXml(username);
        return world;

    }
  
    public Boolean updateProduct(String username, ProductType newproduct) {
        World world = getWorld(username);
        ProductType product = findProductById(world, newproduct.getId());
        if (product == null) {
            return false;
        }
        // calculer la variation de quantité. Si elle est positive c'est
        // que le joueur a acheté une certaine quantité de ce produit
        // sinon c’est qu’il s’agit d’un lancement de production.
        int qteChange = newproduct.getQuantite() - product.getQuantite();
        if (qteChange > 0) {
            //  A REVOIR: soustraire de l'argent du joueur le cout de la quantité achetée et mettre à jour la quantité de product
            world.setMoney(world.getMoney() - (product.getCout()*qteChange));
            product.setQuantite(product.getQuantite()+qteChange);
            product.setCout(product.getCout()*Math.pow(product.getCroissance(),(qteChange-1)));
        } else {
            // initialiser product.timeleft à product.vitesse pour lancer la production
            product.setTimeleft(product.getVitesse());
            long lastupdate= System.currentTimeMillis();
            //world.setMoney(world.getMoney() + (product.getRevenu() * product.getQuantite()));
        }
        // sauvegarder les changements du monde
        saveWordlToXml(world, username);
        return true;
    }

    // prend en paramètre le pseudo du joueur et le manager acheté.
    //renvoie false si l’action n’a pas pu être traitée
    public Boolean updateManager(String username, PallierType newmanager) {
        // aller chercher le monde qui correspond au joueur
        World world = getWorld(username);
        // trouver dans ce monde, le manager équivalent à celui passé
        // en paramètre
        PallierType manager = findManagerByName(world, newmanager.getName());
        if (manager == null) {
            return false;
        }
        // débloquer ce manager
        manager.setUnlocked(true);
        // trouver le produit correspondant au manager
        ProductType product = findProductById(world, manager.getIdcible());
        if (product == null) {
            return false;
        }
        // débloquer le manager de ce produit
        // soustraire de l'argent du joueur le cout du manager
        // sauvegarder les changements au monde
        product.setManagerUnlocked(true);
        world.setMoney(world.getMoney() - manager.getSeuil());
        saveWordlToXml(world, username);
        return true;
    }

    private ProductType findProductById(World world, int id) {
        for (ProductType product : world.getProducts().getProduct()) {
            if (id == product.getId()) {
                return product;
            }
        }
        return null;
    }

    private PallierType findManagerByName(World world, String name) {
        for (PallierType manager : world.getManagers().getPallier()) {
            if (manager.getName().equals(name)) {
                return manager;
            }
        }
        return null;
    }

}
