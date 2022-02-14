/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ISISCapitalist;

import generated.World;
import java.io.File;
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

/**
 *
 * @author Johana Dahan
 */
public class Services {
private World world;

    World readWorldFromXml() {
        JAXBContext jaxbContext;
        try {
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Unmarshaller u = cont.createUnmarshaller(); 
            InputStream input = getClass().getClassLoader().getResourceAsStream("world.xml");
            world = (World) u.unmarshal(input);
           
            
        } catch (Exception ex) {
            System.out.println("erreur du fichier" + ex.getMessage());
            ex.printStackTrace();

        }
        
        return world;
    }

    void saveWordlToXml(World world) {
        JAXBContext jaxbContext;
        try {
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Marshaller m = cont.createMarshaller();
            OutputStream output = new FileOutputStream("file.xml");
            m.marshal(world, output);
        } catch (Exception ex) {
            System.out.println("erreur du fichier" + ex.getMessage());
            ex.printStackTrace();
        }

    }

    World getWorld() {
        return readWorldFromXml();
    }
}
